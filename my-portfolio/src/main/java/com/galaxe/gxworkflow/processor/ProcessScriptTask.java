package com.galaxe.gxworkflow.processor;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.DatabaseConnectionDetailsDTO;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.entity.DatabaseDetails;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessVariables;
import com.galaxe.gxworkflow.entity.ProcessVariablesDetails;
import com.galaxe.gxworkflow.repository.DatabaseDetailsRepository;
import com.galaxe.gxworkflow.repository.ProcessNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableDetailsRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableRepository;
import com.galaxe.gxworkflow.services.impl.ProcessTransactionService;
import com.galaxe.gxworkflow.util.Constants;
import com.galaxe.gxworkflow.util.DatabaseConnectionUtil;

@Service
@Transactional
public class ProcessScriptTask {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ProcessNodeInfoRepository processNodeInfoRepository;

	@Autowired
	private ProcessVariableRepository processVariableRepository;

	@Autowired
	private ProcessVariableDetailsRepository processVariableDetailsRepository;

	@Autowired
	private DatabaseDetailsRepository databaseDetailsRepository;

	@Autowired
	private ProcessTransactionService processTransactionService;

	@Autowired
	private ProcessComplete processComplete;

	@Transactional
	public void executeScriptTask(int processNodeInfoId, int processInstanceId, String processName,
			ProcessInstanceNodeInfo processInstanceNodeInfo, ProcessInstanceRequest processInstanceRequest)
			throws Exception {

		try {
			String nodeXml = processNodeInfoRepository.getNodeXmlByNodeId(processNodeInfoId);
			if (nodeXml != null) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeXml)));

				String namespace = Constants.NAMESPACE;
				NodeList scriptType = definitionsDocument.getElementsByTagNameNS(namespace, "scriptType");
				NodeList script = definitionsDocument.getElementsByTagNameNS(namespace, "script");
				NodeList executionType = definitionsDocument.getElementsByTagNameNS(namespace, "executionType");
				NodeList resultVariable = definitionsDocument.getElementsByTagNameNS(namespace, "resultVariable");

				String typeofScript = null;
				String scriptToExecute = null;
				String typeofExecution = null;
				String result = null;

				if (scriptType.getLength() > 0) {
					for (int i = 0; i < scriptType.getLength(); i++) {
						Element variableElement = (Element) scriptType.item(i);
						typeofScript = variableElement.getTextContent();
						System.out.println(typeofScript);
					}
				}
				if (script.getLength() > 0) {
					for (int i = 0; i < script.getLength(); i++) {
						Element variableElement = (Element) script.item(i);
						scriptToExecute = variableElement.getTextContent();
						System.out.println(scriptToExecute);
					}
				}
				if (executionType.getLength() > 0) {
					for (int i = 0; i < executionType.getLength(); i++) {
						Element variableElement = (Element) executionType.item(i);
						typeofExecution = variableElement.getTextContent();
						System.out.println(typeofExecution);
					}
				}
				if (resultVariable.getLength() > 0) {
					for (int i = 0; i < resultVariable.getLength(); i++) {
						Element variableElement = (Element) resultVariable.item(i);
						result = variableElement.getTextContent();
						System.out.println(result);
					}
				}
				if (typeofScript != null && typeofScript.equals(Constants.DATABASE)) {
					executeDataBaseScriptBasedonExecutionType(typeofExecution, scriptToExecute, processInstanceId,
							nodeXml, processNodeInfoId, result, processInstanceRequest.getUserName(),
							processInstanceNodeInfo);
				}
				if (typeofScript != null && typeofScript.equals(Constants.JAVASCRIPT)) {
					executejavaScriptTask(typeofExecution, scriptToExecute, processInstanceId, nodeXml,
							processNodeInfoId, result, processInstanceRequest.getUserName(), processInstanceNodeInfo);
				}

			}
		} catch (Exception e) {
			logFailedScriptTask(processNodeInfoId, processInstanceRequest);
			System.out.println("error: " + e.getLocalizedMessage());
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeDataBaseScriptBasedonExecutionType(String executionType, String scriptToExecute,
			int processInstanceId, String nodeXml, int processNodeInfoId, String resultVariable, String createdBy,
			ProcessInstanceNodeInfo processInstanceNodeInfo) throws SQLException {

		switch (executionType) {
		case Constants.QUERY:

			List<Object[]> details = processVariableDetailsRepository
					.getProcessVariablesByProcessInstanceId(processInstanceNodeInfo.getId());
			String valueToUpdate = null;
			if (!CollectionUtils.isEmpty(details) && (scriptToExecute.contains("{") && scriptToExecute.contains("}"))) {
				for (Object[] obj : details) {

					if (scriptToExecute.contains((String) obj[1])) {
						valueToUpdate = (String) obj[1];
					} else if (scriptToExecute.contains((String) obj[0])) {
						valueToUpdate = (String) obj[0];
					}
					scriptToExecute = scriptToExecute.replace(valueToUpdate, "'" + (String) obj[2] + "'")
							.replace("{", "").replace("}", "");

				}
			}
			if (scriptToExecute.toLowerCase().startsWith(Constants.SELECT.toLowerCase())) {
				executeSelectQuery(scriptToExecute, processInstanceId, processNodeInfoId);

			} else {
				executeQuery(scriptToExecute, processInstanceId, processNodeInfoId);
			}
			break;
		case Constants.PROCEDURE:
			executeStoredProcedure(scriptToExecute, processInstanceId, processNodeInfoId);
			break;

		case Constants.FUNCTION:

			break;

		case Constants.PROCEDURAL_LANGUAGE:
			break;

		default:
			break;
		}

	}

	public void executejavaScriptTask(String executionType, String scriptToExecute, int processInstanceId,
			String nodeXml, int processNodeInfoId, String resultVariable, String createdBy,
			ProcessInstanceNodeInfo processInstanceNodeInfo) {

		Context context = Context.create();

		List<Object[]> details = processVariableDetailsRepository
				.getProcessVariablesByProcessInstanceId(processInstanceNodeInfo.getId());
		String valueToUpdate = null;
		if (!CollectionUtils.isEmpty(details)) {

			for (Object[] obj : details) {
				if (scriptToExecute.contains((String) obj[1])) {
					valueToUpdate = (String) obj[1];
				} else if (scriptToExecute.contains((String) obj[0])) {
					valueToUpdate = (String) obj[0];
				}
				scriptToExecute = scriptToExecute.replace(valueToUpdate, (String) obj[2]);

			}
		}

		Value result = context.eval("js", scriptToExecute);
		System.out.println("Result: " + result.asString());
		createResultVariable(resultVariable, result.toString(), processInstanceId, processNodeInfoId, createdBy,
				processInstanceNodeInfo);

	}

	private void createResultVariable(String resultVariable, String result, int processInstanceId,
			int processNodeInfoId, String createdBy, ProcessInstanceNodeInfo processInstanceNodeInfo) {
		Timestamp time = new Timestamp(new Date().getTime());
//		create process variables
		ProcessVariables processVariableDetails = new ProcessVariables();
		// com.galaxe.gxworkflow.entity.Process process = new Process();
		// process.setId(processId);
		// processVariableDetails.setProcessId(process);
		processVariableDetails.setName(resultVariable);
		processVariableDetails.setValue("{" + resultVariable + "}");
		processVariableDetails.setIsGlobal(true);

		ProcessNodeInfo nodeInfo = new ProcessNodeInfo();
		nodeInfo.setId(processNodeInfoId);
		processVariableDetails.setProcessNodeInfoId(nodeInfo);

		processVariableDetails.setCreatedBy(createdBy);
		processVariableDetails.setCreatedOn(time);
		processVariableDetails.setModifiedBy(createdBy);
		processVariableDetails.setModifiedOn(time);
		ProcessVariables processvariableId = processVariableRepository.save(processVariableDetails);

//		create process Variable details

		ProcessVariablesDetails processVariablesDetails = new ProcessVariablesDetails();
		processVariablesDetails.setProcessVariableId(processvariableId);
		processVariablesDetails.setValue(result);
//
//		ProcessInstance processInstance = new ProcessInstance();
//		processInstance.setId(processInstanceId);
//		processVariablesDetails.setProcessInstanceId(processInstance);

		processVariablesDetails.setProcessInstanceNodeInfoId(processInstanceNodeInfo);

		processVariablesDetails.setCreatedOn(time);
		processVariablesDetails.setCreatedBy(createdBy);
		processVariablesDetails.setModifiedOn(time);
		processVariablesDetails.setModifiedBy(createdBy);
		processVariableDetailsRepository.save(processVariablesDetails);

	}

	private void executeSelectQuery(String scriptToExecute, int processInstanceId, int processNodeInfoId)
			throws SQLException {
		DatabaseConnectionDetailsDTO connectionDetails = acquireDatabaseConnection(processInstanceId,
				processNodeInfoId);
		if (!ObjectUtils.isEmpty(connectionDetails)) {
			try (Connection connection = DriverManager.getConnection(connectionDetails.getUrl(),
					connectionDetails.getUsername(), connectionDetails.getPassword());
					PreparedStatement preparedStatement = connection.prepareStatement(scriptToExecute);
					ResultSet resultSet = preparedStatement.executeQuery()) {

				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnCount = metaData.getColumnCount();
				Map<String, String> columnNamesandValues = new HashMap<>();
				int rowCount = 0;
				while (resultSet.next() && rowCount <= 1) {
					rowCount++;
					for (int i = 1; i <= columnCount; i++) {
						String columnName = metaData.getColumnName(i);
						Object columnValue = resultSet.getObject(i);
						columnNamesandValues.put(columnName, columnValue != null ? columnValue.toString() : null);
					}
				}
				if (!CollectionUtils.isEmpty(columnNamesandValues)) {
					createScriptTaskOutputVariables(columnNamesandValues, processInstanceId, processNodeInfoId);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("error: " + e.getLocalizedMessage());
				throw e;
			}
		}

	}

	private void executeQuery(String scriptToExecute, int processInstanceId, int processNodeInfoId)
			throws SQLException {
		DatabaseConnectionDetailsDTO connectionDetails = acquireDatabaseConnection(processInstanceId,
				processNodeInfoId);
		if (!ObjectUtils.isEmpty(connectionDetails)) {
			try (Connection connection = DriverManager.getConnection(connectionDetails.getUrl(),
					connectionDetails.getUsername(), connectionDetails.getPassword());
					PreparedStatement preparedStatement = connection.prepareStatement(scriptToExecute);) {
				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				System.out.println(e.getLocalizedMessage());
				throw e;
			}
		}

	}

	private void executeStoredProcedure(String procedure, int processInstanceId, int processNodeInfoId)
			throws SQLException {
		DatabaseConnectionDetailsDTO connectionDetails = acquireDatabaseConnection(processInstanceId,
				processNodeInfoId);
		if (!ObjectUtils.isEmpty(connectionDetails)) {
			try (Connection connection = DriverManager.getConnection(connectionDetails.getUrl(),
					connectionDetails.getUsername(), connectionDetails.getPassword());
					CallableStatement callableStatement = connection.prepareCall(procedure)) {
				boolean hasResultSet = callableStatement.execute();

				if (hasResultSet) {
					try (ResultSet resultSet = callableStatement.getResultSet()) {
						while (resultSet.next()) {
						}
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getLocalizedMessage());
				throw e;
			}
		}

	}

	private void createScriptTaskOutputVariables(Map<String, String> columnNamesandValues, int processInstanceId,
			int processNodeInfoId) {

		List<Object[]> results = processVariableDetailsRepository.getValueforScriptTaskOutputVariable(processInstanceId,
				processNodeInfoId, Constants.OUTPUT);
		if (!CollectionUtils.isEmpty(results)) {
			for (Object[] obj : results) {
				if (columnNamesandValues.containsKey(((String) obj[2]).replace("{", "").replace("}", ""))) {
					processVariableDetailsRepository.updateProcessVariablesDetailsvalues(
							columnNamesandValues.get(((String) obj[2]).replace("{", "").replace("}", "")),
							(Integer) obj[3]);
				}
			}
		}

	}

	public DatabaseConnectionDetailsDTO acquireDatabaseConnection(int processId, int processNodeInfoId) {
		DatabaseDetails databaseDetails = databaseDetailsRepository.findByProcessNodeInfoId(processNodeInfoId);
		DatabaseConnectionDetailsDTO connectionDetails = new DatabaseConnectionDetailsDTO();
		if (!ObjectUtils.isEmpty(databaseDetails)) {
			connectionDetails = DatabaseConnectionUtil.getDataSource(databaseDetails);
		}
		return connectionDetails;

	}

	private void logFailedScriptTask(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest) throws Exception {
		processTransactionService.postTransactionFailed(processInstanceRequest.getProcessInstanceId(),
				processNodeInfoId, processInstanceRequest.getUserName(),ProcessStatusConstant.FAILED.getCode());
		processComplete.updateToFailed(processNodeInfoId, processInstanceRequest.getProcessInstanceId(),
				processInstanceRequest.getUserName());
	}
}
