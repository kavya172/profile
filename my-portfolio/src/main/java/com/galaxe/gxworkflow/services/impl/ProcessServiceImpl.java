package com.galaxe.gxworkflow.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.galaxe.gxworkflow.constants.NodeTypeConstant;
import com.galaxe.gxworkflow.dto.BpmnDTO;
import com.galaxe.gxworkflow.dto.NodeColorStatusDTO;
import com.galaxe.gxworkflow.dto.NodeInfoProcessDTO;
import com.galaxe.gxworkflow.dto.ProcessDTO;
import com.galaxe.gxworkflow.dto.ProcessResponseDTO;
import com.galaxe.gxworkflow.dto.ProcessVersionDTO;
import com.galaxe.gxworkflow.entity.DatabaseDetails;
import com.galaxe.gxworkflow.entity.EmailType;
import com.galaxe.gxworkflow.entity.NodeStatusColor;
import com.galaxe.gxworkflow.entity.NodeType;
import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessRoles;
import com.galaxe.gxworkflow.entity.ProcessSequenceDetails;
import com.galaxe.gxworkflow.entity.ProcessUsers;
import com.galaxe.gxworkflow.entity.ProcessVariables;
import com.galaxe.gxworkflow.entity.ProcessVersion;
import com.galaxe.gxworkflow.entity.SendVariableEmail;
import com.galaxe.gxworkflow.entity.SendVariables;
import com.galaxe.gxworkflow.entity.VariableType;
import com.galaxe.gxworkflow.exception.DataNotFoundException;
import com.galaxe.gxworkflow.exception.DuplicateNameException;
import com.galaxe.gxworkflow.exception.WorkflowException;
import com.galaxe.gxworkflow.mapper.ProcessMapper;
import com.galaxe.gxworkflow.repository.DatabaseDetailsRepository;
import com.galaxe.gxworkflow.repository.EmailTypeRepository;
import com.galaxe.gxworkflow.repository.NodeStatusRepository;
import com.galaxe.gxworkflow.repository.ProcessFlowDetailsRepository;
import com.galaxe.gxworkflow.repository.ProcessInstanceNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessInstanceRepository;
import com.galaxe.gxworkflow.repository.ProcessNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessRepository;
import com.galaxe.gxworkflow.repository.ProcessRolesRepository;
import com.galaxe.gxworkflow.repository.ProcessSequenceDetailsRepository;
import com.galaxe.gxworkflow.repository.ProcessUsersRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableRepository;
import com.galaxe.gxworkflow.repository.ProcessVersionRepository;
import com.galaxe.gxworkflow.repository.SendVariableEmailRepository;
import com.galaxe.gxworkflow.repository.SendVariableRepository;
import com.galaxe.gxworkflow.repository.VariableTypeRepository;
import com.galaxe.gxworkflow.services.NodeTypeService;
import com.galaxe.gxworkflow.services.ProcessService;
import com.galaxe.gxworkflow.services.VariableTypeService;
import com.galaxe.gxworkflow.util.Constants;
import com.galaxe.gxworkflow.util.TimestampUtil;

/**
 * 
 * This class provides the implementation for the methods defined in the
 * interface {@link ProcessService}
 * 
 * The ProcessServiceImpl class provides operations for managing Process Related
 * Information.
 * 
 * This service class handles business logic related to Process management.
 *
 * @Autowired is used for creating an reference object for class level usage.
 *
 */
@Transactional
@Service
public class ProcessServiceImpl implements ProcessService {

	@Autowired
	private ProcessMapper processMapper;

	@Autowired
	private ProcessRepository processRepository;

	@Autowired
	private ProcessVersionRepository processVersionRepo;

	@Autowired
	private ProcessNodeInfoRepository nodeInfoRepository;

	@Autowired
	private ProcessInstanceNodeInfoRepository processInstanceNodeInfoRepository;

	@Autowired
	private ProcessFlowDetailsRepository processFlowDetailsRepository;

	@Autowired
	private ProcessVariableRepository processVariableRepository;

	@Autowired
	private VariableTypeRepository variableTypeRepository;

	@Autowired
	private ProcessInstanceRepository processInstanceRepository;

	@Autowired
	private SendVariableRepository sendVariableRepository;

	@Autowired
	private SendVariableEmailRepository sendVariableEmailRepository;

	@Autowired
	private EmailTypeRepository emailTypeRepository;

	@Autowired
	private ProcessRolesRepository processRolesRepository;

	@Autowired
	private ProcessUsersRepository processUsersRepository;

	@Autowired
	private DatabaseDetailsRepository databaseDetailsRepository;

	@Autowired
	private ProcessVersionRepository processVersionRepository;

	@Autowired
	private ProcessSequenceDetailsRepository processSequenceDetailsRepository;

	@Autowired
	private NodeStatusRepository nodestatusRepository;

	@Autowired
	private NodeTypeService nodeTypeService;

	@Autowired
	private VariableTypeService variableTypeService;

	@Autowired
	private ProcessNodeInfoServiceImpl processNodeInfoServiceImpl;

	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findAllProcessWithVersion")
	@Override
	public List<Object[]> findAllProcessWithVersion() throws Exception {

		List<Object[]> processList = processRepository.findAllProcessWithVersion();
		return processList;
	}

	/**
	 * This method retrieves the list of all available process.
	 * <p>
	 * findAll is a method of {@link ProcessRepository} which is used to retrieve
	 * all the available process details.
	 * 
	 * checking if process is empty, if empty {@link throw}
	 * {@link DataNotFoundException}.
	 * 
	 * The processList is converted into DTO form using toListDto method from the
	 * {@link ProcessMapper} class.
	 * 
	 * @return List of {@link ProcessDTO}.
	 * 
	 */
	public List<ProcessDTO> getAllProcesses() {
		List<ProcessDTO> processDTODtos = new ArrayList<ProcessDTO>();

		List<Object[]> processList = processRepository.findAllProcessWithVersion();

		if (!CollectionUtils.isEmpty(processList)) {

			for (Object[] obj : processList) {

				ProcessDTO process = new ProcessDTO();
				Boolean isExists = false;
				Optional<ProcessDTO> existingObj = processDTODtos.stream().filter(item -> item.getId() == (int) obj[6])
						.findFirst();
				if (ObjectUtils.isEmpty(existingObj)) {
					isExists = true;
					process.setId((int) obj[6]);
					process.setProcessName(obj[0] != null ? obj[0].toString() : "");
				} else {
					process = existingObj.get();
				}
				ProcessVersionDTO processVersion = new ProcessVersionDTO();

				processVersion.setVersion((int) obj[1]);
				processVersion.setVersionId((int) obj[2]);
				processVersion.setIsActive((Boolean) obj[3]);
				processVersion.setModifiedOn(obj[4] != null ? obj[4].toString() : "");
				processVersion.setModifiedBy(obj[5] != null ? obj[5].toString() : "");
				processVersion.setVersionName(obj[7] != null ? obj[7].toString() : "");
				processVersion.setIsLocked((int) obj[8]);

				if (process.getChilds() == null) {
					List<ProcessVersionDTO> pvlist = new ArrayList<>();
					pvlist.add(processVersion);
					process.setChilds(pvlist);
				} else {
					process.getChilds().add(processVersion);
				}
				if (isExists) {
					processDTODtos.add(process);
				}
			}
		}
//		else {
//			// FIXME: Having no data is okay. It should not be an exception. Still we can
//			// safely display the 200 response with no data.
//			throw new DataNotFoundException("No Process Details Available");
//		}
		return processDTODtos;
	}

	/**
	 * This method retrieves the Process for a specific processId.
	 * <p>
	 * findById is a method of {@link ProcessRepository} which returns the process
	 * for a particular processId.
	 * 
	 * If no Process is found, {@link throw} {@link DataNotFoundException} is
	 * thrown.
	 * 
	 * The Obtained process is converted into DTO using the toDto method of the
	 * {@link ProcessMapper} class.
	 * 
	 * @return {@link ProcessDTO}
	 * 
	 */
//	@Cacheable(value = "getProcessNameById", key = "#processId")
	public ProcessDTO getProcessNameById(Integer processId) {

		Process process = processRepository.findById(processId)
				.orElseThrow(() -> new DataNotFoundException("No Data Found."));
		// if (process != null) {
		return processMapper.toDto(process);
//		} else {
//			throw new DataNotFoundException("No Data Found.");
//		}
	}

	@Transactional
//	@CacheEvict(value = { "findAllProcessWithVersion" })
	public void saveProcessDiagram(String processName, MultipartFile file, String createdBy, Boolean isIncrVersoin)
			throws Exception {

		Process process = processRepository.findByProcessName(processName);
		Timestamp timestamp = TimestampUtil.getCurrentTimestamp();

		if (!isIncrVersoin) {
			if (!ObjectUtils.isEmpty(process)) {
				throw new DuplicateNameException("Process name " + processName + " " + "already exists");
			}
			process = new Process();
			process.setProcessName(processName);
			process.setCreatedBy(createdBy);
			process.setCreatedOn(timestamp);
			process.setModifiedBy(createdBy);
			process.setModifiedOn(timestamp);
		}

		ProcessVersion verEntityObj = new ProcessVersion();
		verEntityObj.setCreatedBy(createdBy);
		verEntityObj.setModifiedBy(createdBy);

		verEntityObj.setCreatedOn(timestamp);
		verEntityObj.setModifiedOn(timestamp);

		processAndSaveXml(process, verEntityObj, file, createdBy);

	}

	private void saveNodeDetails(Document newDocument, NodeList nodeList, int processVersionId, Timestamp createdon,
			String createdBy, Transformer transformer, Integer nodeTypeId, String nodeType,
			List<NodeInfoProcessDTO> nodeInfoProcessDTOList) throws Exception {

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String nodeString = nodeToString(newDocument, node, transformer);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeString)));
			String namespace = Constants.NAMESPACE;
			boolean active = true;
			NodeList isActive = null;
			if (nodeType.equalsIgnoreCase(Constants.SERVICE_TASK) || nodeType.equalsIgnoreCase(Constants.SCRIPT_TASK)) {
				isActive = definitionsDocument.getElementsByTagNameNS(namespace, "isActive");
			} else if (nodeType.equalsIgnoreCase(Constants.SEND_TASK)) {
				isActive = definitionsDocument.getElementsByTagNameNS(namespace, "emailProperties");
			}

			if (isActive != null && isActive.getLength() > 0) {
				for (int j = 0; j < isActive.getLength(); j++) {
					Element variableElement = (Element) isActive.item(j);
					String value = variableElement.getTextContent();
					if (nodeType.equalsIgnoreCase(Constants.SEND_TASK)) {
						value = variableElement.getAttribute("isActive");
					}
					if (StringUtils.isNotBlank(value)) {
						if (value.equals("1")) {
							active = true;
						} else if (value.equals("0")) {
							active = false;
						}
					}
				}
			}

			Element element = (Element) nodeList.item(i);
			String nodeid = element.getAttribute("id");

			// Save processNodeInfo table
			ProcessNodeInfo processNodeInfo = nodeInfoRepository.saveProcessNodeInfo(nodeTypeId, processVersionId,
					nodeString, createdon, createdBy, createdBy, createdon, nodeid, active);

			if (!Constants.BPMN_DIAGRAMN.equalsIgnoreCase(nodeType)) {
				populateVariableDetails(nodeString, processNodeInfo, createdBy);
			}

			if (Constants.SEND_TASK.equalsIgnoreCase(nodeType)) {
				populateSendVariableDetails(nodeString, processNodeInfo, createdBy);
			}

			if (Constants.USER_TASK.equalsIgnoreCase(nodeType)) {
				populateUserTaskDetails(nodeString, processNodeInfo, createdBy);
			}

			if (Constants.SCRIPT_TASK.equalsIgnoreCase(nodeType)) {
				populateScriptTaskDetails(nodeString, processNodeInfo, createdBy, processVersionId);
			}

			// exclusivegateway code is same for all above nodetypes
			switch (nodeType.toLowerCase()) {
			case "bpmn:startevent":
			case "bpmn:endevent":
			case "bpmn:usertask":
			case "bpmn:scripttask":
			case "bpmn:servicetask":
			case "bpmn:sendtask":
			case "bpmn:exclusivegateway":
				storeColorStatus(nodeString, processNodeInfo, createdBy);
				String nodeId = null;
				NodeInfoProcessDTO nodeInfoProcessDTO = new NodeInfoProcessDTO();
				if (nodeList.getLength() > 0) {
					Element eventElement = (Element) nodeList.item(i);
					nodeId = eventElement.getAttribute("id");
				} else {
					// handle scenario for no start node
					System.out.println("No bpmn:startEvent element found in the XML string.");
				}
				nodeInfoProcessDTO.setNodeTypeName(nodeType);
				nodeInfoProcessDTO.setNodeId(nodeId);
				nodeInfoProcessDTO.setProcessNodeInfo(processNodeInfo);
				nodeInfoProcessDTOList.add(nodeInfoProcessDTO);
				break;

			case "bpmn:sequenceflow":
				NodeInfoProcessDTO nodeInfoProcessDTO1 = new NodeInfoProcessDTO();
				String sourceSeq = null;
				String targetSeq = null;
				String id = null;
				if (nodeList.getLength() > 0) {
					Element eventElement = (Element) nodeList.item(i);
					sourceSeq = eventElement.getAttribute("sourceRef");
					targetSeq = eventElement.getAttribute("targetRef");
					id = eventElement.getAttribute("id");
				}
//			                else {
//			            	//handle scenario for no start node
//			                System.out.println("No bpmn:sequenceFlow element found in the XML string.");
//			            }
				nodeInfoProcessDTO1.setNodeTypeName(nodeType);
				nodeInfoProcessDTO1.setProcessNodeInfo(processNodeInfo);
				nodeInfoProcessDTO1.setSourceRef(sourceSeq);
				nodeInfoProcessDTO1.setTargetRef(targetSeq);
				nodeInfoProcessDTO1.setNodeId(id);
				nodeInfoProcessDTOList.add(nodeInfoProcessDTO1);
				break;
			case Constants.BPMN_DIAGRAMN:

				break;
			case "bpmn:process":

				break;
			case "bpmn:task":
// throw exception or allow to save
				break;
			default:
				System.out.println("Found a new node type");
				break;
			}
		}
	}

	private void populateScriptTaskDetails(String nodeString, ProcessNodeInfo processNodeInfo, String createdBy,
			int processVersionId) throws ParserConfigurationException, SAXException, IOException {

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeString)));

		// Assuming the namespace is known and fixed
		String namespace = Constants.NAMESPACE;

		// extract input variables
		NodeList sendVariableNodes = definitionsDocument.getElementsByTagNameNS(namespace, "databaseProperty");
		if (sendVariableNodes.getLength() > 0) {
			List<DatabaseDetails> details = databaseDetailsRepository
					.getDatabaseDetailsByNodeInfoId(processNodeInfo.getId());
			if (!CollectionUtils.isEmpty(details)) {
				databaseDetailsRepository.deleteAllByNodeInfoId(processNodeInfo.getId());
			}
			for (int i = 0; i < sendVariableNodes.getLength(); i++) {

				Element variableElement = (Element) sendVariableNodes.item(i);
				String serverName = variableElement.getAttribute("server");
				String serverPort = variableElement.getAttribute("port");
				String databaseName = variableElement.getAttribute("databaseName");
				String databaseSchema = variableElement.getAttribute("schema");
				String databaseType = variableElement.getAttribute("databaseType");
				String databaseUsername = variableElement.getAttribute("username");
				String databasePassword = variableElement.getAttribute("password");

				Optional<ProcessVersion> processVersion = processVersionRepository.findById(processVersionId);
				if (processVersion.isPresent()) {
					DatabaseDetails databaseDetails = new DatabaseDetails();
					// databaseDetails.setVersionid(processVersion.get());
					databaseDetails.setProcessNodeInfoId(processNodeInfo);
					databaseDetails.setServer(serverName);
					databaseDetails.setPort(serverPort);
					databaseDetails.setDatabaseName(databaseName);
					databaseDetails.setSchema(databaseSchema);
					databaseDetails.setDatabaseType(databaseType);
					databaseDetails.setUsername(databaseUsername);
					databaseDetails.setPassword(databasePassword);
					databaseDetails.setCreatedOn(time);
					databaseDetails.setCreatedBy(createdBy);
					databaseDetails.setModifiedBy(createdBy);
					databaseDetails.setModifiedOn(time);
					databaseDetailsRepository.save(databaseDetails);
				}
			}
		}
	}

	private void populateSendVariableDetails(String nodeString, ProcessNodeInfo processNodeInfo, String createdBy)
			throws ParserConfigurationException, SAXException, IOException {

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeString)));

		// Assuming the namespace is known and fixed
		String namespace = Constants.NAMESPACE;

		// extract input variables
		NodeList sendVariableNodes = definitionsDocument.getElementsByTagNameNS(namespace, "emailProperties");

		if (sendVariableNodes.getLength() > 0) {
			List<SendVariables> variables = sendVariableRepository
					.getSendVariablesByNodeInfoId(processNodeInfo.getId());
			if (!CollectionUtils.isEmpty(variables)) {
				sendVariableEmailRepository.deleteAllByNodeInfoId(processNodeInfo.getId());
				sendVariableRepository.deleteAllByNodeInfoId(processNodeInfo.getId());
			}
			for (int i = 0; i < sendVariableNodes.getLength(); i++) {

				SendVariables sendVariables = new SendVariables();
				Element variableElement = (Element) sendVariableNodes.item(i);
				String subject = variableElement.getAttribute("subject");
				String body = variableElement.getAttribute("body");
				String bodyType = variableElement.getAttribute("bodyType");

				sendVariables.setSubject(subject);
				sendVariables.setBody(body);

				sendVariables.setProcessNodeInfoId(processNodeInfo);
				sendVariables.setBodyType(bodyType);

				sendVariables.setCreatedBy(createdBy);
				sendVariables.setModifiedBy(createdBy);
				sendVariables.setCreatedOn(time);
				sendVariables.setModifiedOn(time);

				sendVariables = sendVariableRepository.save(sendVariables);

				populateSendVariableEmailValues(sendVariables, variableElement, createdBy);
			}
		}

	}

	private void storeColorStatus(String nodeString, ProcessNodeInfo processNodeInfo, String createdBy)
			throws ParserConfigurationException, SAXException, IOException {

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeString)));
		String namespace = Constants.NAMESPACE;
		NodeList colorNodes = definitionsDocument.getElementsByTagNameNS(namespace, Constants.COLOR_PROPERTIES);
		List<NodeStatusColor> nodeStatusColors = nodestatusRepository.getByProcessNodeInfoId(processNodeInfo.getId());
		if (!CollectionUtils.isEmpty(nodeStatusColors)) {
			nodestatusRepository.deleteAllByProcessNodeInfoId(processNodeInfo.getId());
		}
		if (colorNodes.getLength() > 0) {
			for (int i = 0; i < colorNodes.getLength(); i++) {
				NodeStatusColor nodeStatusColor = new NodeStatusColor();
				Element colorNode = (Element) colorNodes.item(i);
				String name = colorNode.getAttribute(Constants.NAME);
				String color = colorNode.getAttribute(Constants.COLOR);
				String status = colorNode.getTextContent();
				if (StringUtils.equalsIgnoreCase(name, Constants.DEFAULT_COLOR)) {
					nodeStatusColor.setStatusId(6);
				}
				if (StringUtils.equalsIgnoreCase(name, Constants.SUCCESS_COLOR)) {
					nodeStatusColor.setStatusId(4);
				}
				if (StringUtils.equalsIgnoreCase(name, Constants.FAILURE_COLOR)) {
					nodeStatusColor.setStatusId(3);
				}
				nodeStatusColor.setStatusName(status);
				nodeStatusColor.setStatusColor(color);
				nodeStatusColor.setProcessNodeInfo(processNodeInfo);
				nodeStatusColor.setCreatedBy(createdBy);
				nodeStatusColor.setModifiedBy(createdBy);
				nodeStatusColor.setCreatedOn(time);
				nodeStatusColor.setModifiedOn(time);
				if (!StringUtils.isBlank(color) && !StringUtils.isBlank(status)) {
					nodestatusRepository.save(nodeStatusColor);
				}
			}
		}
	}

	private void populateSendVariableEmailValues(SendVariables sendVariables, Element variableElement,
			String userName) {
		List<EmailType> emailTypes = emailTypeRepository.findAll();
		List<SendVariableEmail> sendVariableEmails = new ArrayList<SendVariableEmail>();

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		emailTypes.stream().forEach(emailType -> {
			String variableValues = variableElement.getAttribute(emailType.getDescriptions());
			if (variableValues.matches("\\{([^}]*)\\}")) {
				SendVariableEmail sendVariableEmail = new SendVariableEmail();
				sendVariableEmail.setEmailId(variableValues);
				sendVariableEmail.setEmailTypeId(emailType);
				sendVariableEmail.setSendVariableId(sendVariables);
				sendVariableEmail.setCreatedBy(userName);
				sendVariableEmail.setCreatedOn(time);
				sendVariableEmail.setModifiedBy(userName);
				sendVariableEmail.setModifiedOn(time);
				sendVariableEmails.add(sendVariableEmail);
			} else {
				splitStaticEmailValues(variableValues).stream().forEach(email -> {
					SendVariableEmail sendVariableEmail = new SendVariableEmail();
					sendVariableEmail.setEmailId(email);
					sendVariableEmail.setEmailTypeId(emailType);
					sendVariableEmail.setSendVariableId(sendVariables);
					sendVariableEmail.setCreatedBy(userName);
					sendVariableEmail.setCreatedOn(time);
					sendVariableEmail.setModifiedBy(userName);
					sendVariableEmail.setModifiedOn(time);
					sendVariableEmails.add(sendVariableEmail);
				});
			}
		});

		sendVariableEmailRepository.saveAll(sendVariableEmails);

	}

	private List<String> splitStaticEmailValues(String emials) {
		return new ArrayList<>(Arrays.asList(emials.split(",")));
	}

	private void processAndSaveXml(Process process, ProcessVersion verEntityObj, MultipartFile file, String createdBy)
			throws Exception {

		InputStream inputStream = file.getInputStream();

		// Used to parse the xml document from the inputStream
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(inputStream);
		document.getDocumentElement().normalize();

		// Create a clone to get the process definition details
		NodeList processElements = document.getElementsByTagName(NodeTypeConstant.PROCESS.getCode());
		Element processElement = (Element) processElements.item(0);

		// FIXME: REMOVE HARD CODE
		process.setProcessName(processElement.getAttribute(Constants.NAME));
//		process.setIsActive(Boolean.parseBoolean(processElement.getAttribute(Constants.ISEXECUTABLE)));

//		process.setVersion(Integer.parseInt(processElement.getAttribute(Constants.VERSION)));
		process.setAllUsers(processElement.getAttribute(Constants.ALLUSERS));

		process = processRepository.save(process);

		verEntityObj.setProcessId(process);
		verEntityObj.setIsActive(Boolean.parseBoolean(processElement.getAttribute(Constants.ISEXECUTABLE)));
		verEntityObj.setVersionNumber(Integer.parseInt(processElement.getAttribute(Constants.VERSION)));
		verEntityObj.setVersionName(processElement.getAttribute(Constants.VERSION_NAME));

		verEntityObj = processVersionRepo.save(verEntityObj);

		int processVersionId = verEntityObj.getId();

		Timestamp time = new Timestamp(new Date().getTime());
		List<String> nodeTypeList = new ArrayList<String>();
		Collections.addAll(nodeTypeList, NodeTypeConstant.START.getCode(), NodeTypeConstant.END.getCode(),
				NodeTypeConstant.USER.getCode(), NodeTypeConstant.SEQUENCE.getCode(),
				NodeTypeConstant.DIAGRAMN.getCode(), NodeTypeConstant.SCRIPT.getCode(),
				NodeTypeConstant.SERVICE.getCode(), NodeTypeConstant.EXCLUSIVE.getCode(),
				NodeTypeConstant.SEND.getCode(), NodeTypeConstant.TASK.getCode());

		List<NodeInfoProcessDTO> nodeInfoProcessDTOList = new ArrayList<NodeInfoProcessDTO>();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Document newDocument = (Document) document.cloneNode(true);

		for (String nodeType : nodeTypeList) { // Loop thru all static list of nodes.
			NodeList nodeList = document.getElementsByTagName(nodeType); // get the node details for the looping static
																			// node
			if (nodeList.getLength() > 0) {
				saveNodeDetails(newDocument, nodeList, processVersionId, time, createdBy, transformer,
						nodeTypeService.findNodeTypeByName(nodeType).getId(), nodeType, nodeInfoProcessDTOList);
			}

			// FIXME: FOR LOOP IS NOT READABLE.
			for (int i = nodeList.getLength() - 1; i >= 0; i--) {

				Node node = nodeList.item(i);
				// FIXME: NO HARD CODING
				if (node.getNodeType() == Node.ELEMENT_NODE && !Constants.BPMN_DIAGRAMN.equalsIgnoreCase(nodeType)) {
					processElement.removeChild(node);
				}
			}
		}

		NodeList diagramElements = document.getElementsByTagName(NodeTypeConstant.DIAGRAMN.getCode());
		if (diagramElements.getLength() > 0) {
			Node diagramElement = diagramElements.item(0);
			Node parentNode = diagramElement.getParentNode();
			parentNode.removeChild(diagramElement);
		}

		saveFlowDetails(nodeInfoProcessDTOList, verEntityObj, createdBy, time);

		saveXmlVariables(process, processElement, document, verEntityObj, createdBy, time);
	}

	public void updateUserDetails(Process process, Element processElement, ProcessVersion processVersion,
			String createdBy, Timestamp time, ProcessNodeInfo processNodeInfo) {

		// processUsersRepository.deleteByProcessId(process);
//		processRolesRepository.deleteByProcessId(process);

		// Get the bpmn:extensionElements within bpmn:process
		NodeList extensionElementsList = processElement.getElementsByTagName(Constants.EXTENSION_ELEMENTS);
		for (int j = 0; j < extensionElementsList.getLength(); j++) {
			Element extensionElementsElement = (Element) extensionElementsList.item(j);

			// Get the gxw:userDetails within bpmn:extensionElements
			NodeList userDetailsList = extensionElementsElement.getElementsByTagName(Constants.CUSTOM_USER_DETAILS);
			for (int k = 0; k < userDetailsList.getLength(); k++) {
				Element userDetailsElement = (Element) userDetailsList.item(k);
				String isAllUsers = userDetailsElement.getAttribute(Constants.ALLUSERS);
				String name = userDetailsElement.getAttribute(Constants.NAME);
				String role = userDetailsElement.getAttribute(Constants.ROLE);
				saveProcessUsers(name, processVersion, createdBy, time, processNodeInfo);
				saveProcessRoles(role, processVersion, createdBy, time, processNodeInfo);
				process.setAllUsers(isAllUsers);
			}
		}
		processRepository.save(process);
	}

	public void saveProcessUsers(String processUsers, ProcessVersion processVersion, String createdBy, Timestamp time,
			ProcessNodeInfo processNodeInfo) {
		String[] userList = processUsers.split(",");
		List<ProcessUsers> users = processUsersRepository.getUsersByNodeInfoId(processNodeInfo.getId());
		if (!CollectionUtils.isEmpty(users)) {
			processUsersRepository.deleteAllByNodeInfoId(processNodeInfo.getId());
		}
		if (userList.length > 0 && !userList[0].isEmpty()) {
			for (String user : userList) {
				if (!StringUtils.isAllBlank(user)) {
					ProcessUsers processUser = new ProcessUsers();
					processUser.setEmailId(user);
					// processUser.setIsActive(true);
//					processUser.setVersionId(processVersion);
					processUser.setCreatedBy(createdBy);
					processUser.setCreatedOn(time);
					processUser.setModifiedOn(time);
					processUser.setModifiedBy(createdBy);
					processUser.setProcessNodeInfoId(processNodeInfo);
					processUsersRepository.save(processUser);
				}
			}
		}
	}

	public void saveProcessRoles(String processRoles, ProcessVersion processVersion, String createdBy, Timestamp time,
			ProcessNodeInfo processNodeInfo) {
		String[] roleList = processRoles.split(",");
		List<ProcessRoles> roles = processRolesRepository.getRolesByNodeInfoId(processNodeInfo.getId());
		if (!CollectionUtils.isEmpty(roles)) {
			processRolesRepository.deleteAllByNodeInfoId(processNodeInfo.getId());
		}
		if (roleList.length > 0 && !roleList[0].isEmpty()) {
			for (String role : roleList) {
				if (!StringUtils.isAllBlank(role)) {
					ProcessRoles processRole = new ProcessRoles();
					processRole.setRoleName(role);
					// processRole.setIsActive(true);
					processRole.setCreatedBy(createdBy);
					processRole.setCreatedOn(time);
					processRole.setModifiedOn(time);
					processRole.setModifiedBy(createdBy);
					processRole.setProcessNodeInfoId(processNodeInfo);
					processRolesRepository.save(processRole);
				}
			}
		}
	}

	private void saveXmlVariables(Process process, Element processElement, Document document,
			ProcessVersion processVersion, String createdBy, Timestamp time)
			throws TransformerFactoryConfigurationError, Exception {

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new StringWriter());
		transformer.transform(source, result);

		String simplifiedXML = result.getWriter().toString();
		String namespace = Constants.NAMESPACE;
		NodeList inputVariableNodes = document.getElementsByTagNameNS(namespace, Constants.INPUT_VARIABLE);

		List<ProcessVariables> processVariableDetailsList = new ArrayList<ProcessVariables>();
		// FIXME: MOVE THIS TO SERVICE CALL, SO THAT WE CAN CACHE. AND CACHE IT.
		VariableType variableType = variableTypeService.findVariableTypeByName(Constants.INPUT);
//		VariableType variableType = variableTypeRepository.findByName(Constants.INPUT);
		// FIXME: MOVE THIS TO SERVICE CALL, SO THAT WE CAN CACHE. AND CACHE IT.
		NodeType processNodeType = nodeTypeService.findNodeTypeByName(NodeTypeConstant.PROCESS.getCode());
//		NodeType processNodeType = nodeTypeRepository.findByNameIgnoreCase(NodeTypeConstant.PROCESS.getCode());
//		
		ProcessNodeInfo processNodeInfo = nodeInfoRepository.saveProcessNodeInfo(processNodeType.getId(),

				processVersion.getId(), simplifiedXML, time, createdBy, createdBy, time, "Process_1", true);
		updateUserDetails(process, processElement, processVersion, createdBy, time, processNodeInfo);
		if (inputVariableNodes.getLength() > 0) {
			// FIXME: Change it to foreach or advanced versions
			for (int i = 0; i < inputVariableNodes.getLength(); i++) {
				ProcessVariables processVariableDetails = new ProcessVariables();
				// String varType = processVariableDetails.getNodeType();
				Element inputVariableElement = (Element) inputVariableNodes.item(i);
				String variableName = inputVariableElement.getAttribute(Constants.NAME);
				String textContent = inputVariableElement.getTextContent();

				// processVariableDetails.setVersionId(processVersion);
				processVariableDetails.setName(variableName);
				processVariableDetails.setValue(textContent);
				// default value should be changed
				processVariableDetails.setIsGlobal(true);
				processVariableDetails.setProcessNodeInfoId(processNodeInfo);
				processVariableDetails.setCreatedBy(createdBy);
				processVariableDetails.setCreatedOn(time);
				processVariableDetails.setModifiedBy(createdBy);
				processVariableDetails.setModifiedOn(time);
				processVariableDetails.setVariableTypeId(variableType);
				processVariableDetailsList.add(processVariableDetails);
			}

		}

		if (!CollectionUtils.isEmpty(processVariableDetailsList)) {
			processVariableRepository.saveAll(processVariableDetailsList);
		}

	}

	private void saveXmlVariablesForUpdate(Process process, Element processElement, Document document,
			ProcessVersion processVersion, String createdBy, Timestamp time, List<Object[]> nodeinfo)
			throws TransformerFactoryConfigurationError, Exception {

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new StringWriter());
		transformer.transform(source, result);

		String simplifiedXML = result.getWriter().toString();
		String namespace = Constants.NAMESPACE;
		NodeList inputVariableNodes = document.getElementsByTagNameNS(namespace, Constants.INPUT_VARIABLE);

		List<ProcessVariables> processVariableDetailsList = new ArrayList<ProcessVariables>();
		// FIXME: MOVE THIS TO SERVICE CALL, SO THAT WE CAN CACHE. AND CACHE IT.
		VariableType variableType = variableTypeService.findVariableTypeByName(Constants.INPUT);
//		VariableType variableType = variableTypeRepository.findByName(Constants.INPUT);
		// FIXME: MOVE THIS TO SERVICE CALL, SO THAT WE CAN CACHE. AND CACHE IT.
		NodeType processNodeType = nodeTypeService.findNodeTypeByName(NodeTypeConstant.PROCESS.getCode());
//		NodeType processNodeType = nodeTypeRepository.findByNameIgnoreCase(NodeTypeConstant.PROCESS.getCode());
//		if (!CollectionUtils.isEmpty(nodeinfo)) {
		String nodeid = processElement.getAttribute("id");

		for (Object[] res : nodeinfo) {
			if (res != null && res.length > 0) {
				if (nodeid.equals((String) res[8])) {
					int id = (int) res[0];
					ProcessNodeInfo processNodeInfo = new ProcessNodeInfo();
					processNodeInfo.setId(id);
					nodeInfoRepository.updateProcessNodeInfo(simplifiedXML, createdBy, time, "Process_1", true, id);
					updateUserDetails(process, processElement, processVersion, createdBy, time, processNodeInfo);

					if (inputVariableNodes.getLength() > 0) {
						// FIXME: Change it to foreach or advanced versions
						for (int i = 0; i < inputVariableNodes.getLength(); i++) {
							ProcessVariables processVariableDetails = new ProcessVariables();
							// String varType = processVariableDetails.getNodeType();
							Element inputVariableElement = (Element) inputVariableNodes.item(i);
							String variableName = inputVariableElement.getAttribute(Constants.NAME);
							String textContent = inputVariableElement.getTextContent();
							ProcessVariables variable = processVariableRepository.findByProcessNodeInfoIdIdAndName(id,
									variableName);
							if (!ObjectUtils.isEmpty(variable)) {
								processVariableRepository.updateProcessVariable(variable.getId(), textContent,
										createdBy, time);
							} else {
								// processVariableDetails.setVersionId(processVersion);
								processVariableDetails.setName(variableName);
								processVariableDetails.setValue(textContent);
								// default value should be changed
								processVariableDetails.setIsGlobal(true);
								processVariableDetails.setProcessNodeInfoId(processNodeInfo);
								processVariableDetails.setCreatedBy(createdBy);
								processVariableDetails.setCreatedOn(time);
								processVariableDetails.setModifiedBy(createdBy);
								processVariableDetails.setModifiedOn(time);
								processVariableDetails.setVariableTypeId(variableType);
								processVariableDetailsList.add(processVariableDetails);
							}
						}
					}
				}
			}
		}

		if (!CollectionUtils.isEmpty(processVariableDetailsList)) {
			processVariableRepository.saveAll(processVariableDetailsList);
		}

	}

	private void saveFlowDetails(List<NodeInfoProcessDTO> nodeInfoProcessDTOList, ProcessVersion processVerison,
			String createdBy, Timestamp time) throws Exception {
		if (!CollectionUtils.isEmpty(nodeInfoProcessDTOList)) {
			List<ProcessSequenceDetails> processSequenceDetailsList = new ArrayList<ProcessSequenceDetails>();
			for (NodeInfoProcessDTO nodeInfoProcessDTO : nodeInfoProcessDTOList) {
				// FIXME: REMOVE HARD CODE
				if (null != nodeInfoProcessDTO.getNodeTypeName()
						&& Constants.SEQUENCE_FLOW_TASK.equalsIgnoreCase(nodeInfoProcessDTO.getNodeTypeName())) {
					ProcessSequenceDetails processSequenceDetails = new ProcessSequenceDetails();

					ProcessNodeInfo sourceProcessNodeInfo = nodeInfoProcessDTOList.stream()
							.filter(node -> node.getNodeId().equalsIgnoreCase(nodeInfoProcessDTO.getSourceRef()))
							.findAny().get().getProcessNodeInfo();

					ProcessNodeInfo targetProcessNodeInfo = nodeInfoProcessDTOList.stream()
							.filter(node -> node.getNodeId().equalsIgnoreCase(nodeInfoProcessDTO.getTargetRef()))
							.findAny().get().getProcessNodeInfo();

					processSequenceDetails.setProcessSequenceId(nodeInfoProcessDTO.getProcessNodeInfo());
					processSequenceDetails.setVersionId(processVerison);
					processSequenceDetails.setSourceNodeId(sourceProcessNodeInfo);
					processSequenceDetails.setTargetNodeId(targetProcessNodeInfo);
					processSequenceDetails.setCreatedBy(createdBy);
					processSequenceDetails.setModifiedBy(createdBy);
					processSequenceDetails.setCreatedOn(time);
					processSequenceDetails.setModifiedOn(time);
					// Save sequences to the processflowdetails table.
					processSequenceDetailsList.add(processSequenceDetails);
				}
			}
			if (!CollectionUtils.isEmpty(nodeInfoProcessDTOList)) {
				processFlowDetailsRepository.saveAll(processSequenceDetailsList);
			}
		}
	}

	private String extractActionFromXML(String nodexml) throws Exception {

		String action = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(nodexml)) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(nodexml));
			Document doc = builder.parse(is);

			// Get the name attribute
			NodeList sequenceFlows = doc.getElementsByTagName(Constants.SEQUENCE_FLOW_TASK);
			if (sequenceFlows.getLength() > 0) {
				Element sequenceFlow = (Element) sequenceFlows.item(0);
				action = sequenceFlow.getAttribute("name");
			}
		}
		return action;
	}

	private static String nodeToString(Document newDocument, Node node, Transformer transformer) throws Exception {
		// FIXME: REMOVE HARD CODE
		if (node.getNodeType() == Node.ELEMENT_NODE && Constants.BPMN_DIAGRAMN.equalsIgnoreCase(node.getNodeName())
				&& node.getNamespaceURI() == null) {

			// FIXME: MOVE THEM TO A CONSTANT FILE.
			String biocNamespaceURI = "http://bpmn.io/schema/bpmn/biocolor/1.0";
			String biocNamespacePrefix = "bioc";
			String colorNamespaceURI = "http://www.omg.org/spec/BPMN/non-normative/color/1.0";
			String colorNamespacePrefix = "color";

			Element rootElement = (Element) node;
			rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + biocNamespacePrefix,
					biocNamespaceURI);
			rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + colorNamespacePrefix,
					colorNamespaceURI);
		}
		NodeList definitionsList = newDocument.getElementsByTagName("bpmn:definitions");
		Node definitionsNode = definitionsList.item(0);

		while (definitionsNode.hasChildNodes()) {
			definitionsNode.removeChild(definitionsNode.getFirstChild());
		}

		StringWriter writer = new StringWriter();
		Node importedNode = newDocument.importNode(node, true);

		definitionsNode.appendChild(importedNode);

		transformer.transform(new DOMSource(newDocument), new StreamResult(writer));
		return writer.toString();
	}

	public BpmnDTO getProcessWorkflowForWorkflow(int processVersionId, int processInstanceId)
			throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError,
			TransformerException {

		BpmnDTO bpmnDTO = new BpmnDTO();
		String diagramString = null;
		String definitioXmlString = null;
		List<Node> processNodeDocument = new ArrayList<Node>();
		List<String> nodeTypeList = new ArrayList<String>();

		// FIXME: MOVE THIS CALL INTO IT'S OWN SERVICE CALL AND CACHE IT.
		List<Object[]> processNodeObjectList = nodeInfoRepository.getProcessByVersionIdForWorkflow(processVersionId);

		Collections.addAll(nodeTypeList, NodeTypeConstant.START.getCode(), NodeTypeConstant.END.getCode(),
				NodeTypeConstant.USER.getCode(), NodeTypeConstant.SEQUENCE.getCode(),
				NodeTypeConstant.DIAGRAMN.getCode(), NodeTypeConstant.SCRIPT.getCode(),
				NodeTypeConstant.SERVICE.getCode(), NodeTypeConstant.EXCLUSIVE.getCode(),
				NodeTypeConstant.SEND.getCode());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		HashMap<String, String> nodeColors = new HashMap<String, String>();
		for (Object[] processNodeObject : processNodeObjectList) {
			int processNodeId = (int) processNodeObject[12];
			ProcessInstanceNodeInfo processinstanceNodinfo = processInstanceNodeInfoRepository
					.findByProcessInstanceIdAndProcessNodeInfoIdForWorkflow(processInstanceId, processNodeId);
			// FIXME: MOVE THIS CALL TO IT'S OWN SERVICE CALL AND CACHE IT.
			// String nodeTypeName = nodeTypeRepository.findById((int)
			// processNodeObject[1]).get().getName();
			String nodeTypeName = (String) processNodeObject[1];
			String xmlData = (String) processNodeObject[6];

			String colorNamespaceURI = "http://www.omg.org/spec/BPMN/nonnormative/color/1.0";
			if (!xmlData.contains("xmlns:color")) {
				xmlData = xmlData.replace("<bpmn:definitions ",
						"<bpmn:definitions xmlns:color=\"" + colorNamespaceURI + "\" ");
			}

			updateNodeColorsForWorkflow(nodeTypeName, xmlData, nodeColors, processinstanceNodinfo);
			if (StringUtils.equalsIgnoreCase(Constants.BPMN_DIAGRAMN, nodeTypeName)) {
				Document document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));
				NodeList processElements = document.getElementsByTagName("bpmndi:BPMNShape");
				for (int i = 0; i < processElements.getLength(); i++) {
					Element processElement = (Element) processElements.item(i);
					if (nodeColors.containsKey(processElement.getAttribute("bpmnElement"))
							&& nodeColors.get(processElement.getAttribute("bpmnElement")) != null) {
						processElement.setAttribute("color:backgroundcolor",
								nodeColors.get(processElement.getAttribute("bpmnElement")));
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(document);
						StringWriter writer = new StringWriter();
						StreamResult result = new StreamResult(writer);
						transformer.transform(source, result);
						xmlData = writer.toString();
					}
				}
			}

			bpmnDTO.setCurrentVersion((int) processNodeObject[5]);
			bpmnDTO.setMaxVersion((int) processNodeObject[11]);

			// FIXME: USE CONSTANTS, NO HARD CODING
			switch (nodeTypeName.toLowerCase()) {
			case "bpmn:startevent":
			case "bpmn:endevent":
			case "bpmn:usertask":
			case "bpmn:scripttask":
			case "bpmn:servicetask":
			case "bpmn:exclusivegateway":
			case "bpmn:sequenceflow":
			case "bpmn:sendtask":
				Document nodeDocumnet = builder.parse(new InputSource(new StringReader(xmlData)));
				Node definitionTag = nodeDocumnet.getElementsByTagName(nodeTypeName).item(0);
				if (null != definitionTag)
					processNodeDocument.add(definitionTag);
				// processNodes.add(xmlData);
				break;
			case Constants.BPMN_DIAGRAMN:
				diagramString = xmlData;
				break;
			case "bpmn:process":
				definitioXmlString = xmlData;
				break;
			default:
				System.out.println("Found a new task type");
				// FIXME: CHANGE TO LOGGER STATEMETN.
				break;
			}
		}

		Document definitionsDocument = builder.parse(new InputSource(new StringReader(definitioXmlString)));
		Document bpmnDiagramDocument = builder.parse(new InputSource(new StringReader(diagramString)));

		Node bpmnDiagramNode = definitionsDocument.importNode(bpmnDiagramDocument.getDocumentElement().getFirstChild(),
				true);
		if (bpmnDiagramNode != null)
			definitionsDocument.getDocumentElement().appendChild(bpmnDiagramNode);

		// FIXME: NO HARD CODING
		NodeList processElements = definitionsDocument.getElementsByTagName("bpmn:process");

		if (processElements.getLength() > 0) {
			Element processElement = (Element) processElements.item(0);
			bpmnDTO.setProcessName(processElement.getAttribute("name"));
			bpmnDTO.setVersionNumber(Integer.parseInt(processElement.getAttribute("version")));
			bpmnDTO.setVersionName(processElement.getAttribute("versionName"));

			// Append the start event, end event, user task, and sequence flow to the
			// <bpmn:process> element
			for (Node node : processNodeDocument) {
				// Import the node directly
				Node importedNode = processElement.getOwnerDocument().importNode(node, true);

				// Append the imported node to the process element
				processElement.appendChild(importedNode);
			}
		} else {
			// Handle the case where the <bpmn:process> element is not found
			System.err.println("The <bpmn:process> element was not found in the definitions document.");
			// FIXME: CHANGE IT TO LOGGER.
		}

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(definitionsDocument);

		StreamResult result = new StreamResult(new StringWriter());
		transformer.transform(source, result);
		String combinedXML = result.getWriter().toString();
		// FIXME: CHANGE IT TO LOGGER.
		bpmnDTO.setProcessVersionId(processVersionId);
		bpmnDTO.setXmlString(combinedXML);

		return bpmnDTO;
	}

	public BpmnDTO getProcessWorkflow(int processVersionId) throws ParserConfigurationException, SAXException,
			IOException, TransformerFactoryConfigurationError, TransformerException {

		BpmnDTO bpmnDTO = new BpmnDTO();
		String diagramString = null;
		String definitioXmlString = null;
		List<Node> processNodeDocument = new ArrayList<Node>();
		List<String> nodeTypeList = new ArrayList<String>();

		// FIXME: MOVE THIS CALL INTO IT'S OWN SERVICE CALL AND CACHE IT.
		List<Object[]> processNodeObjectList = nodeInfoRepository.getProcessByVersionId(processVersionId);

		Collections.addAll(nodeTypeList, NodeTypeConstant.START.getCode(), NodeTypeConstant.END.getCode(),
				NodeTypeConstant.USER.getCode(), NodeTypeConstant.SEQUENCE.getCode(),
				NodeTypeConstant.DIAGRAMN.getCode(), NodeTypeConstant.SCRIPT.getCode(),
				NodeTypeConstant.SERVICE.getCode(), NodeTypeConstant.EXCLUSIVE.getCode(),
				NodeTypeConstant.SEND.getCode());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		HashMap<String, String> nodeColors = new HashMap<String, String>();
		for (Object[] processNodeObject : processNodeObjectList) {
			// FIXME: MOVE THIS CALL TO IT'S OWN SERVICE CALL AND CACHE IT.
//			String nodeTypeName = nodeTypeRepository.findById((int) processNodeObject[1]).get().getName();
			String nodeTypeName = (String) processNodeObject[1];
			String xmlData = (String) processNodeObject[6];

			String colorNamespaceURI = "http://www.omg.org/spec/BPMN/non-normative/color/1.0";
			if (!xmlData.contains("xmlns:color")) {
				xmlData = xmlData.replace("<bpmn:definitions ",
						"<bpmn:definitions xmlns:color=\"" + colorNamespaceURI + "\" ");
			}

			updateNodeColors(nodeTypeName, xmlData, nodeColors);
			if (StringUtils.equalsIgnoreCase(Constants.BPMN_DIAGRAMN, nodeTypeName)) {
				Document document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));
				NodeList processElements = document.getElementsByTagName("bpmndi:BPMNShape");
				for (int i = 0; i < processElements.getLength(); i++) {
					Element processElement = (Element) processElements.item(i);
					if (nodeColors.containsKey(processElement.getAttribute("bpmnElement"))
							&& nodeColors.get(processElement.getAttribute("bpmnElement")) != null) {
						processElement.setAttribute("color:background-color",
								nodeColors.get(processElement.getAttribute("bpmnElement")));
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(document);
						StringWriter writer = new StringWriter();
						StreamResult result = new StreamResult(writer);
						transformer.transform(source, result);
						xmlData = writer.toString();
					}
				}
			}

			bpmnDTO.setCurrentVersion((int) processNodeObject[5]);
			bpmnDTO.setMaxVersion((int) processNodeObject[11]);

			// FIXME: USE CONSTANTS, NO HARD CODING
			switch (nodeTypeName.toLowerCase()) {
			case "bpmn:startevent":
			case "bpmn:endevent":
			case "bpmn:usertask":
			case "bpmn:scripttask":
			case "bpmn:servicetask":
			case "bpmn:exclusivegateway":
			case "bpmn:sequenceflow":
			case "bpmn:sendtask":
				Document nodeDocumnet = builder.parse(new InputSource(new StringReader(xmlData)));
				Node definitionTag = nodeDocumnet.getElementsByTagName(nodeTypeName).item(0);
				if (null != definitionTag)
					processNodeDocument.add(definitionTag);
//			    	 processNodes.add(xmlData);
				break;
			case Constants.BPMN_DIAGRAMN:
				diagramString = xmlData;
				break;
			case "bpmn:process":
				definitioXmlString = xmlData;
				break;
			default:
				System.out.println("Found a new task type");
				// FIXME: CHANGE TO LOGGER STATEMETN.
				break;
			}
		}

		Document definitionsDocument = builder.parse(new InputSource(new StringReader(definitioXmlString)));
		Document bpmnDiagramDocument = builder.parse(new InputSource(new StringReader(diagramString)));

		Node bpmnDiagramNode = definitionsDocument.importNode(bpmnDiagramDocument.getDocumentElement().getFirstChild(),
				true);
		if (bpmnDiagramNode != null)
			definitionsDocument.getDocumentElement().appendChild(bpmnDiagramNode);

		// FIXME: NO HARD CODING
		NodeList processElements = definitionsDocument.getElementsByTagName("bpmn:process");

		if (processElements.getLength() > 0) {
			Element processElement = (Element) processElements.item(0);
			bpmnDTO.setProcessName(processElement.getAttribute("name"));
			bpmnDTO.setVersionNumber(Integer.parseInt(processElement.getAttribute("version")));
			bpmnDTO.setVersionName(processElement.getAttribute("versionName"));

			// Append the start event, end event, user task, and sequence flow to the
			// <bpmn:process> element
			for (Node node : processNodeDocument) {
				// Import the node directly
				Node importedNode = processElement.getOwnerDocument().importNode(node, true);

				// Append the imported node to the process element
				processElement.appendChild(importedNode);
			}
		} else {
			// Handle the case where the <bpmn:process> element is not found
			System.err.println("The <bpmn:process> element was not found in the definitions document.");
			// FIXME: CHANGE IT TO LOGGER.
		}

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(definitionsDocument);

		StreamResult result = new StreamResult(new StringWriter());
		transformer.transform(source, result);
		String combinedXML = result.getWriter().toString();
		// FIXME: CHANGE IT TO LOGGER.
		bpmnDTO.setProcessVersionId(processVersionId);
		bpmnDTO.setXmlString(combinedXML);

		return bpmnDTO;
	}

	/**
	 * This method performs operation of updating the process with the provided BPMN
	 * Design file and processId.
	 * <p>
	 * findById is a method of {@link ProcessRepository} which returns the process
	 * for a particular processId.
	 * 
	 * If no Process is found, {@link throw} {@link DataNotFoundException} is
	 * thrown.
	 * 
	 * findByNodeTypeName is a method of{@link NodeTypeReposittory} which is used to
	 * get the node Details for the start event.
	 * 
	 * findNodeIdByProcessIdAndNodeTypeId is a method of
	 * {@link ProcessNodeInfoRepository} which gives the node details for the
	 * provided node Id and process Id.
	 * 
	 * findByNodeId is a method of {@link ProcessTasksRepository}, which returns the
	 * tasks for the provided node.
	 *
	 * If tasks are present,{@link throw} {@link Exception} is thrown.
	 *
	 * findByProcessId is a method in {@link processNodeInfoRepository} which
	 * returns process Node Info Details available for a particular nodes.
	 * 
	 * deleteByParentNodeIdIn and deleteByProcessId are the methods of
	 * {@link ProcessNodeDetailsRepository} and {@link ProcessNodeInfo Repository}
	 * respectively which is used to delete all the node details in both the table.
	 * 
	 * Once the nodes are deleted, process is saved and again processDeploy method
	 * of the {@link workFlowDeploy} is called for saving node details and parent -
	 * child relations.
	 * 
	 * @param process    is a type of MultipartFile, holds the updated file of an
	 *                   BPMN design.
	 * @param processId  type of Integer, is Id of an {@link Process} Entity.
	 * 
	 * @param modifiedBy is a type of String, will holds data modifier information
	 */

	@CacheEvict(value = { "findAllProcessWithVersion" })
	public void updateProcess(int versionId, MultipartFile file, String modifiedBy) throws Exception {

		// FIXME: IT SHOULD GO THRU A SEPARATE SERVICE CALL AND CACHED.

		ProcessVersion processVersion = new ProcessVersion();

		processVersion = processVersionRepo.findById(versionId).orElse(null);

		List<Integer> versionIds = Arrays.asList(versionId);

		Process process = new Process();

		if (!ObjectUtils.isEmpty(processVersion)) {

			process = processRepository.findById(processVersion.getProcessId().getId()).orElse(null);
			if (ObjectUtils.isEmpty(process))
				throw new DataNotFoundException("Process not found");

			List<ProcessInstance> processInstance = processInstanceRepository
					.findProcessInstanceByVersionId(processVersion);
			if (null != processInstance && !CollectionUtils.isEmpty(processInstance)) {
				processVersion.setModifiedBy(modifiedBy);
				processVersion.setModifiedOn(TimestampUtil.getCurrentTimestamp());
				updateInitiatedProcess(processVersion, process, file, modifiedBy);

//				throw new WorkflowException("Cannot save process as workflow already started.");
			} else {
				processVersion.setModifiedBy(modifiedBy);
				processVersion.setModifiedOn(TimestampUtil.getCurrentTimestamp());
				deleteProcessVersionDetailsForUpdate(versionIds);
				processAndSaveXml(process, processVersion, file, modifiedBy);
			}

		} else {
			Timestamp timestamp = TimestampUtil.getCurrentTimestamp();

			processVersion.setCreatedOn(timestamp);
			processVersion.setCreatedBy(modifiedBy);
			processVersion.setModifiedBy(modifiedBy);
			processVersion.setModifiedOn(timestamp);
			deleteProcessVersionDetailsForUpdate(versionIds);
			processAndSaveXml(process, processVersion, file, modifiedBy);

		}
		// FIXME: IT SHOULD GO THRU A SEPARATE SERVICE CALL AND CACHED.

	}

	private void updateInitiatedProcess(ProcessVersion processVersion, Process process, MultipartFile file,
			String modifiedBy) throws Exception {

		if (!ObjectUtils.isEmpty(processVersion)) {
			List<Object[]> nodeinfo = nodeInfoRepository.getNodeInfoByVersionId(processVersion.getId());
			if (!CollectionUtils.isEmpty(nodeinfo)) {
				processAndSaveXmlForUpdate(process, processVersion, file, modifiedBy, nodeinfo);
			}
		}

	}

	private void processAndSaveXmlForUpdate(Process process, ProcessVersion verEntityObj, MultipartFile file,
			String createdBy, List<Object[]> nodeinfo) throws Exception {

		InputStream inputStream = file.getInputStream();

		// Used to parse the xml document from the inputStream
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(inputStream);
		document.getDocumentElement().normalize();

		// Create a clone to get the process definition details
		NodeList processElements = document.getElementsByTagName(NodeTypeConstant.PROCESS.getCode());
		Element processElement = (Element) processElements.item(0);

		// FIXME: REMOVE HARD CODE
		process.setProcessName(processElement.getAttribute(Constants.NAME));
//	process.setIsActive(Boolean.parseBoolean(processElement.getAttribute(Constants.ISEXECUTABLE)));

//	process.setVersion(Integer.parseInt(processElement.getAttribute(Constants.VERSION)));
		process.setAllUsers(processElement.getAttribute(Constants.ALLUSERS));

		process = processRepository.save(process);

		verEntityObj.setProcessId(process);
		verEntityObj.setIsActive(Boolean.parseBoolean(processElement.getAttribute(Constants.ISEXECUTABLE)));
		verEntityObj.setVersionNumber(Integer.parseInt(processElement.getAttribute(Constants.VERSION)));
		verEntityObj.setVersionName(processElement.getAttribute(Constants.VERSION_NAME));

		verEntityObj = processVersionRepo.save(verEntityObj);

		int processVersionId = verEntityObj.getId();

		Timestamp time = new Timestamp(new Date().getTime());
		List<String> nodeTypeList = new ArrayList<String>();
		Collections.addAll(nodeTypeList, NodeTypeConstant.START.getCode(), NodeTypeConstant.END.getCode(),
				NodeTypeConstant.USER.getCode(), NodeTypeConstant.SEQUENCE.getCode(),
				NodeTypeConstant.DIAGRAMN.getCode(), NodeTypeConstant.SCRIPT.getCode(),
				NodeTypeConstant.SERVICE.getCode(), NodeTypeConstant.EXCLUSIVE.getCode(),
				NodeTypeConstant.SEND.getCode(), NodeTypeConstant.TASK.getCode());

		List<NodeInfoProcessDTO> nodeInfoProcessDTOList = new ArrayList<NodeInfoProcessDTO>();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Document newDocument = (Document) document.cloneNode(true);

		List<Node> totalNodesInTheProcess = new ArrayList<>();
		for (String nodeType : nodeTypeList) { // Loop thru all static list of nodes.
			NodeList nodeList = document.getElementsByTagName(nodeType);
			if (nodeList.getLength() > 0) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					totalNodesInTheProcess.add(node);
				}
			}

		}

		for (String nodeType : nodeTypeList) { // Loop thru all static list of nodes.
			NodeList nodeList = document.getElementsByTagName(nodeType); // get the node details for the looping static
																			// node
			if (nodeList.getLength() > 0) {
				saveNodeDetailsForUpdate(newDocument, nodeList, processVersionId, time, createdBy, transformer,
						nodeTypeService.findNodeTypeByName(nodeType).getId(), nodeType, nodeInfoProcessDTOList,
						nodeinfo, totalNodesInTheProcess);
			}

			// FIXME: FOR LOOP IS NOT READABLE.
			for (int i = nodeList.getLength() - 1; i >= 0; i--) {

				Node node = nodeList.item(i);
				// FIXME: NO HARD CODING
				if (node.getNodeType() == Node.ELEMENT_NODE && !Constants.BPMN_DIAGRAMN.equalsIgnoreCase(nodeType)) {
					processElement.removeChild(node);
				}
			}
		}
		saveXmlVariablesForUpdate(process, processElement, document, verEntityObj, createdBy, time, nodeinfo);
	}

	private void saveNodeDetailsForUpdate(Document newDocument, NodeList nodeList, int processVersionId,
			Timestamp createdon, String createdBy, Transformer transformer, Integer nodeTypeId, String nodeType,
			List<NodeInfoProcessDTO> nodeInfoProcessDTOList, List<Object[]> nodeinfo, List<Node> totalNodesInTheProcess)
			throws Exception {

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String nodeString = nodeToString(newDocument, node, transformer);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeString)));
			String namespace = Constants.NAMESPACE;
			boolean active = true;
			NodeList isActive = null;
			if (nodeType.equalsIgnoreCase(Constants.SERVICE_TASK) || nodeType.equalsIgnoreCase(Constants.SCRIPT_TASK)) {
				isActive = definitionsDocument.getElementsByTagNameNS(namespace, "isActive");
			} else if (nodeType.equalsIgnoreCase(Constants.SEND_TASK)) {
				isActive = definitionsDocument.getElementsByTagNameNS(namespace, "emailProperties");
			}

			if (isActive != null && isActive.getLength() > 0) {
				for (int j = 0; j < isActive.getLength(); j++) {
					Element variableElement = (Element) isActive.item(j);
					String value = variableElement.getTextContent();
					if (StringUtils.isNotBlank(value)) {
						if (value.equals("1")) {
							active = true;
						} else if (value.equals("0")) {
							active = false;
						}
					}
				}
			}
			Element element = (Element) nodeList.item(i);
			String nodeid = element.getAttribute("id");

			List<String> existingNodeIds = nodeinfo.stream().map(nodes -> (String) nodes[8])
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(existingNodeIds)) {
				if (!(existingNodeIds.size() == totalNodesInTheProcess.size() + 1)) {
					throw new WorkflowException("One or more nodes are missing");
				}
			}

			// Save processNodeInfo table
			if (!CollectionUtils.isEmpty(nodeinfo)) {
				for (Object[] result : nodeinfo) {
					if (result != null && result.length > 0) {
						if (nodeid.equals((String) result[8])) {
							int id = (int) result[0];
							nodeInfoRepository.updateProcessNodeInfo(nodeString, createdBy, createdon, nodeid, active,
									id);
							ProcessNodeInfo processNodeInfo = nodeInfoRepository.findById(id).orElse(null);
							if (!ObjectUtils.isEmpty(processNodeInfo)) {
								if (!Constants.BPMN_DIAGRAMN.equalsIgnoreCase(nodeType)) {
									populateVariableDetails(nodeString, processNodeInfo, createdBy);
								}

								if (Constants.SEND_TASK.equalsIgnoreCase(nodeType)) {
									populateSendVariableDetails(nodeString, processNodeInfo, createdBy);
								}

								if (Constants.USER_TASK.equalsIgnoreCase(nodeType)) {
									populateUserTaskDetails(nodeString, processNodeInfo, createdBy);
								}

								if (Constants.SCRIPT_TASK.equalsIgnoreCase(nodeType)) {
									populateScriptTaskDetails(nodeString, processNodeInfo, createdBy, processVersionId);
								}
							}

						}
					}
				}
			}
		}
	}

	@CacheEvict(value = { "getProcesByVersionId" }, key = "#versionIds"
//			,beforeInvocation = true, allEntries = true
	)
	private void deleteProcessVersionDetailsForUpdate(List<Integer> versionIds) {

		sendVariableEmailRepository.deleteAllByProcessVersionId(versionIds);
		sendVariableRepository.deleteAllByProcessVersionId(versionIds);
		processSequenceDetailsRepository.deleteAllByProcessVersionId(versionIds);

		processVariableRepository.deleteAllByProcessVersionId(versionIds);
		processUsersRepository.deleteAllByProcessVersionId(versionIds);
		processRolesRepository.deleteAllByProcessVersionId(versionIds);

		databaseDetailsRepository.deleteAllByProcessVersionId(versionIds);
		nodestatusRepository.deleteAllByProcessVersionId(versionIds);

		nodeInfoRepository.deleteAllByProcessVersionId(versionIds);

	}

	/**
	 * This method is used to delete the process with the provided List of Process
	 * Id's.
	 * <p>
	 * findByNodeTypeName is a method of{@link NodeTypeReposittory} which is used to
	 * get the node Details for the start event.
	 * 
	 * findByProcessIdProcessIdInAndNodeTypeId is a method of
	 * {@link ProcessNodeInfoRepository} which gives the node details for the
	 * provided node Id and process Id.
	 * 
	 * findByNodeIdIn is a method of {@link processTaskRepository} which is used to
	 * obtain the tasks for the provided node details.
	 *
	 * If tasks are present for the provided Id's, those Id's are filtered from the
	 * list.
	 *
	 * For Id's with no transactions, the processes are deleted along with Process
	 * Node Info details and Process Node Details.
	 *
	 * deleteByParentNodeIdProcessIdProcessIdIn,
	 * deleteByProcessIdProcessIdIn,deleteByProcessIdIn are the method of
	 * {@link ProcessNodeDetailsRepository},{@link ProcessNodeInfoRepository},
	 * {@link ProcessRepository} Respectively which is used to delete the Process,
	 * Process Node Info and Process Node Details.
	 *
	 * If the ProcessId's have transaction, then {@link throw}
	 * {@link DataNotFoundException} is thrown.
	 *
	 * @param processIds type of List of Integers, is a Id of {@link Process}
	 *                   Entity.
	 *
	 */

	@Override
	// FIXME: MAKE IT A PROC IN DB
	public void deleteByProcessIds(List<Integer> processIds) {
		List<Process> process = processRepository.getProcessByIds(processIds);
		if (!CollectionUtils.isEmpty(process) && (process.size() == processIds.size())) {
			sendVariableEmailRepository.deleteAllByProcessIds(processIds);
			sendVariableRepository.deleteAllByProcessIds(processIds);
			processSequenceDetailsRepository.deleteAllByProcessIds(processIds);
			processVariableRepository.deleteAllByProcessIds(processIds);
			processUsersRepository.deleteAllByProcessIds(processIds);
			processRolesRepository.deleteAllByProcessIds(processIds);
			databaseDetailsRepository.deleteAllByProcessIds(processIds);
			nodestatusRepository.deleteAllByProcessIds(processIds);
			nodeInfoRepository.deleteAllByProcessIds(processIds);
			processVersionRepo.deleteAllByProcessIds(processIds);
			processRepository.deleteByProcessIdIn(processIds);
		} else {
			deleteByProcessVersionIds(processIds);
		}

	}

	@Override
	// FIXME: MAKE IT A PROC IN DB
	public void deleteByProcessVersionIds(List<Integer> processVersionIds) {

		sendVariableEmailRepository.deleteAllByProcessVersionId(processVersionIds);
		sendVariableRepository.deleteAllByProcessVersionId(processVersionIds);
		processSequenceDetailsRepository.deleteAllByProcessVersionId(processVersionIds);
		processVariableRepository.deleteAllByProcessVersionId(processVersionIds);
		processUsersRepository.deleteAllByProcessVersionId(processVersionIds);
		processRolesRepository.deleteAllByProcessVersionId(processVersionIds);
		databaseDetailsRepository.deleteAllByProcessVersionId(processVersionIds);
		nodestatusRepository.deleteAllByProcessVersionId(processVersionIds);
		nodeInfoRepository.deleteAllByProcessVersionId(processVersionIds);
		processVersionRepo.deleteAllByProcessVersionId(processVersionIds);

	}

//	@Override
//	public void deleteByProcessId(Integer processId) {
//		Process process = processRepository.findById(processId).orElse(null);
//		processVariableRepository.deleteByProcessId(process);
//		processFlowDetailsRepository.deleteByProcessId(process);
//		sendVariableRepository.deleteByProcessId(process);
//		nodeInfoRepository.deleteByProcessId(process);
//		processRepository.delete(process);
//	}

//	@Override
//	public List<String> getAllNodeDetailsForProcess(int processId) {
//
//		Process process = processRepository.findById(processId).orElse(null);
//		if (ObjectUtils.isEmpty(process))
//			throw new DataNotFoundException("Process not found");
//		List<String> nodes = new ArrayList<>();
//		List<ProcessNodeInfo> nodeInfo = nodeInfoRepository.findByProcessId(process);
//		nodeInfo.forEach(node -> {
////			nodes.add(node.getNodeName());
//		});
//		return nodes;
//	}
//	

	public void populateVariableDetails(String nodeXml, ProcessNodeInfo currentProcessNodeInfo, String createdBy)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeXml)));

		// Assuming the namespace is known and fixed
		String namespace = Constants.NAMESPACE;
		;
		List<ProcessVariables> processVariableDetailsList = new ArrayList<ProcessVariables>();

		if (!currentProcessNodeInfo.getNodeTypeId().getName().equals(Constants.SERVICE_TASK)) {
			// extract input variables
			NodeList inputVariableNodes = definitionsDocument.getElementsByTagNameNS(namespace,
					Constants.INPUT_VARIABLE);
			// FIXME: MOVE IT TO CONSTANT
			VariableType variableInputType = variableTypeRepository.findByName("input");
			populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, inputVariableNodes,
					variableInputType, createdBy, time, false, null);

			// extract output variables
			// FIXME: MOVE IT TO CONSTANT
			NodeList outputVariableNodes = definitionsDocument.getElementsByTagNameNS(namespace,
					Constants.OUTPUT_VARIABLE);
			VariableType variableOutputType = variableTypeRepository.findByName(Constants.OUTPUT);
			populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, outputVariableNodes,
					variableOutputType, createdBy, time, true, null);
		}
		// FIXME: MOVE IT TO CONSTANT
		NodeList sequenceVariableNodes = definitionsDocument.getElementsByTagNameNS(namespace,
				Constants.CONDITIONAL_EXPRESSION);
		// FIXME: MOVE IT TO CONSTANT
		VariableType sequenceOutputType = variableTypeRepository.findByName(Constants.CONDITIONAL);
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, sequenceVariableNodes,
				sequenceOutputType, createdBy, time, true, null);

		NodeList possibleActionNodes = definitionsDocument.getElementsByTagNameNS(namespace,
				Constants.POSSIBLE_ACTIONS);
		// FIXME: MOVE IT TO CONSTANT
		VariableType possibleActions = variableTypeRepository.findByName(Constants.POSSIBLE_ACTIONS);
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, possibleActionNodes,
				possibleActions, createdBy, time, false, null);

		if (currentProcessNodeInfo.getNodeTypeId().getName().equals(Constants.SERVICE_TASK)) {
			saveVariablesForServiceTask(definitionsDocument, namespace, processVariableDetailsList,
					currentProcessNodeInfo, sequenceOutputType, createdBy, time);
		}
		if (!CollectionUtils.isEmpty(processVariableDetailsList)) {
			processVariableRepository.saveAll(processVariableDetailsList);
		}
	}

	private void saveVariablesForServiceTask(Document definitionsDocument, String namespace,
			List<ProcessVariables> processVariableDetailsList, ProcessNodeInfo currentProcessNodeInfo,
			VariableType sequenceOutputType, String createdBy, Timestamp time) {

		VariableType headerVariableType = variableTypeRepository.findByName("headerParam");
		NodeList header = definitionsDocument.getElementsByTagNameNS(namespace, "headerParams");
		saveInputVariables(header, currentProcessNodeInfo, createdBy, time, headerVariableType);

		VariableType queryVariableType = variableTypeRepository.findByName("queryParam");
		NodeList query = definitionsDocument.getElementsByTagNameNS(namespace, "queryParams");
		saveInputVariables(query, currentProcessNodeInfo, createdBy, time, queryVariableType);

		VariableType serviceIntegrationVariableType = variableTypeRepository.findByName("serviceIntegration");

		NodeList authTypeNode = definitionsDocument.getElementsByTagNameNS(namespace, "authType");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, authTypeNode,
				serviceIntegrationVariableType, createdBy, time, false, "authType");

		NodeList authInfoNode = definitionsDocument.getElementsByTagNameNS(namespace, "authInfo");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, authInfoNode,
				serviceIntegrationVariableType, createdBy, time, false, "authInfo");

		NodeList methodNode = definitionsDocument.getElementsByTagNameNS(namespace, "method");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, methodNode,
				serviceIntegrationVariableType, createdBy, time, false, "method");
//		
		NodeList urlNode = definitionsDocument.getElementsByTagNameNS(namespace, "url");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, urlNode,
				serviceIntegrationVariableType, createdBy, time, false, "url");
//		
		NodeList contentTypeNode = definitionsDocument.getElementsByTagNameNS(namespace, "contentType");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, contentTypeNode,
				serviceIntegrationVariableType, createdBy, time, false, "contentType");

		NodeList requestBodyNode = definitionsDocument.getElementsByTagNameNS(namespace, "requestBody");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, requestBodyNode,
				serviceIntegrationVariableType, createdBy, time, false, "requestBody");
//		
		NodeList connectionTimeoutNode = definitionsDocument.getElementsByTagNameNS(namespace, "connectionTimeout");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, connectionTimeoutNode,
				serviceIntegrationVariableType, createdBy, time, false, "connectionTimeout");
//		
		NodeList retriesCountNode = definitionsDocument.getElementsByTagNameNS(namespace, "retriesCount");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, retriesCountNode,
				serviceIntegrationVariableType, createdBy, time, false, "retriesCount");

		NodeList retriesBackOffNode = definitionsDocument.getElementsByTagNameNS(namespace, "retriesBackOff");
		populateProcessVariableDetails(processVariableDetailsList, currentProcessNodeInfo, retriesBackOffNode,
				serviceIntegrationVariableType, createdBy, time, false, "retriesBackOff");

	}

	private void saveInputVariables(NodeList variableNodes, ProcessNodeInfo currentProcessNodeInfo, String createdBy,
			Timestamp time, VariableType variableType) {
		if (variableNodes.getLength() > 0) {
			for (int i = 0; i < variableNodes.getLength(); i++) {
				Element variableElement = (Element) variableNodes.item(i);
				NodeList inputVariablesList = variableElement.getElementsByTagName(Constants.CUSTOM_INPUT_VARIABLE);
				if (inputVariablesList.getLength() > 0) {
					for (int j = 0; j < inputVariablesList.getLength(); j++) {
						Element inputVariable = (Element) inputVariablesList.item(j);
						String name = inputVariable.getAttribute(Constants.NAME);
						String value = inputVariable.getTextContent();
						saveProcessVariables(currentProcessNodeInfo, createdBy, time, name, value, variableType);
					}
				}
			}
		}

	}

	private void populateProcessVariableDetails(List<ProcessVariables> processVariableDetailsList,
			ProcessNodeInfo currentProcessNodeInfo, NodeList variableNodes, VariableType variableType, String createdBy,
			Timestamp time, Boolean isGlobal, String name) {
		if (variableNodes.getLength() > 0) {
			// FIXME: USE LATEST FOR LOOP
			for (int i = 0; i < variableNodes.getLength(); i++) {
				ProcessVariables processVariableDetails = new ProcessVariables();
				// String varType = processVariableDetails.getNodeType();
				Element variableElement = (Element) variableNodes.item(i);
				String variableName = variableElement.getAttribute(Constants.NAME);
				String textContent = variableElement.getTextContent();

				ProcessVariables variable = processVariableRepository
						.findByProcessNodeInfoIdIdAndName(currentProcessNodeInfo.getId(), variableName);
				if (!ObjectUtils.isEmpty(variable)) {
					processVariableRepository.updateProcessVariable(variable.getId(), textContent, createdBy, time);
				} else {
					// processVariableDetails.setVersionId(currentProcessNodeInfo.getVersionId());

					processVariableDetails.setName(variableName);
					if (variableName == null || variableName == "") {
						processVariableDetails.setName(name);
					}
					processVariableDetails.setValue(textContent);
					// default value should be changed
					processVariableDetails.setIsGlobal(isGlobal);
					processVariableDetails.setProcessNodeInfoId(currentProcessNodeInfo);
					processVariableDetails.setCreatedBy(createdBy);
					processVariableDetails.setCreatedOn(time);
					processVariableDetails.setModifiedBy(createdBy);
					processVariableDetails.setModifiedOn(time);
					processVariableDetails.setVariableTypeId(variableType);
					processVariableDetailsList.add(processVariableDetails);
				}
			}
		}
	}

	private void saveProcessVariables(ProcessNodeInfo processNodeInfo, String createdBy, Timestamp time, String name,
			String value, VariableType variableType) {
		ProcessVariables processVariables = new ProcessVariables();

		ProcessVariables variable = processVariableRepository.findByProcessNodeInfoIdIdAndName(processNodeInfo.getId(),
				name);
		if (!ObjectUtils.isEmpty(variable)) {
			processVariableRepository.updateProcessVariable(variable.getId(), value, createdBy, time);
		} else {
			processVariables.setIsGlobal(Boolean.TRUE);
			processVariables.setName(name);
			processVariables.setValue(value);
			processVariables.setProcessNodeInfoId(processNodeInfo);
			// processVariables.setVersionId(processNodeInfo.getVersionId());
			processVariables.setVariableTypeId(variableType);

			processVariables.setCreatedBy(createdBy);
			processVariables.setCreatedOn(time);
			processVariables.setModifiedBy(createdBy);
			processVariables.setModifiedOn(time);

			processVariableRepository.save(processVariables);
		}

	}

	// My
	@Override
	public List<NodeColorStatusDTO> getProcessInstanceStatus(int processId, int versionId) throws Exception {

		Process process = new Process();
		process.setId(versionId);
		ProcessVersion processVerison = processVersionRepo.findByIdAndProcessId(versionId, process);
		if (ObjectUtils.isEmpty(processVerison)) {

			throw new WorkflowException("Requested Process details not found");
		}
		ProcessInstance processInstance = processInstanceRepository.findByVersionId(processVerison);

		if (ObjectUtils.isEmpty(processInstance)) {
			throw new WorkflowException("Process Instances not Found");
		}
		List<ProcessInstanceNodeInfo> processInstanceNodeInfos = processInstanceNodeInfoRepository
				.getProcessInfoFromProcessIdAndLatestExecutedNode(processInstance.getId());
		List<NodeColorStatusDTO> nodeColorStatusDTOs = new ArrayList<>();
		for (ProcessInstanceNodeInfo instanceNodeInfo : processInstanceNodeInfos) {
			NodeColorStatusDTO nodeColorStatusDTO = new NodeColorStatusDTO();
			// FIXME: MOVE IT TO IT'S OWN SERVICE CALL, IT SHOULD BE CACHED
			ProcessNodeInfo processNodeInfo = processNodeInfoServiceImpl
					.findById(instanceNodeInfo.getProcessNodeInfoId().getId());

			if (null != processNodeInfo) {
				nodeColorStatusDTO.setNodeId(processNodeInfo.getId());
				nodeColorStatusDTO.setNodeName(processNodeInfo.getNodeTypeId().getName());
				nodeColorStatusDTO.setStatus(instanceNodeInfo.getStatusId().getCode());
				nodeColorStatusDTOs.add(nodeColorStatusDTO);
			}
		}
		return nodeColorStatusDTOs;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findByProcessId", key = "#processId")
	@Override
	public Process findByProcessId(Integer processId) throws Exception {
		Process process = processRepository.findById(processId).get();
		return process;
	}

	private void populateUserTaskDetails(String nodeString, ProcessNodeInfo processNodeInfo, String createdBy)
			throws Exception {

		ProcessVersion processVersion = processNodeInfo.getVersionId();

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document definitionsDocument = builder.parse(new InputSource(new StringReader(nodeString)));
		// Assuming the namespace is known and fixed
		String namespace = Constants.NAMESPACE;
		VariableType inputVariableType = variableTypeService.findVariableTypeByName(Constants.INPUT);
		// extract input variables
		NodeList sendVariableNodes = definitionsDocument.getElementsByTagNameNS(namespace, "assignmentProperties");

		if (sendVariableNodes.getLength() > 0) {
			for (int i = 0; i < sendVariableNodes.getLength(); i++) {
				Element variableElement = (Element) sendVariableNodes.item(i);
				// String varType = processVariableDetails.getNodeType();
				String assignAll = variableElement.getAttribute("assignAll");
				String users = variableElement.getAttribute("users");
				String roles = variableElement.getAttribute("roles");

				saveProcessUsers(users, processVersion, createdBy, time, processNodeInfo);
				saveProcessRoles(roles, processVersion, createdBy, time, processNodeInfo);

				saveProcessVariables(processNodeInfo, createdBy, time, assignAll, "assignAll", inputVariableType);

			}
		}

		NodeList variableNodes = definitionsDocument.getElementsByTagNameNS(namespace, "variables");

		saveInputVariables(variableNodes, processNodeInfo, createdBy, time, inputVariableType);
//		if (variableNodes.getLength() > 0) {
//			for (int i = 0; i < sendVariableNodes.getLength(); i++) {
//				Element variableElement = (Element) sendVariableNodes.item(i);
//				NodeList inputVariablesList = variableElement.getElementsByTagName(Constants.CUSTOM_INPUT_VARIABLE);
//				if (inputVariablesList.getLength() > 0) {
//					Element inputVariable = (Element) inputVariablesList.item(0);
//					String name = inputVariable.getAttribute("name");
//					String value = inputVariable.getTextContent();
//					saveProcessVariables(processNodeInfo, createdBy, time, name, value,inputVariableType);
//				}
//
//			}
//		}
	}

//	private void saveProcessVariables(ProcessNodeInfo processNodeInfo, String createdBy, Timestamp time,
//			String assignAll, String name) {
//		ProcessVariables processVariables = new ProcessVariables();
//
//		processVariables.setIsGlobal(Boolean.TRUE);
//		processVariables.setName(name);
//		processVariables.setValue(assignAll);
//		processVariables.setProcessNodeInfoId(processNodeInfo);
//		// processVariables.setVersionId(processNodeInfo.getVersionId());
//
//		VariableType variableType = variableTypeRepository.findByName(Constants.INPUT);
//		processVariables.setVariableTypeId(variableType);
//
//		processVariables.setCreatedBy(createdBy);
//		processVariables.setCreatedOn(time);
//		processVariables.setModifiedBy(createdBy);
//		processVariables.setModifiedOn(time);
//
//		processVariableRepository.save(processVariables);
//	}
//	@Override
//	public List<NodeInstanceTransactionDTO> getSingeNodeTransactionDetails(int processId, String nodeName, String nodeId) {
//		NodeType nodeType = nodeTypeRepository.findByLowerNodeType(nodeName.toLowerCase());
//		ProcessInstance processInstance = processInstanceRepository.findByProcessId(processId);
//		if(null != nodeType && processInstance!= null) {
//			List<ProcessInstanceNodeInfo> processInstanceNodeInfos = processInstanceNodeInfoRepository.getNodeInfoTransactionDetails(processInstance.getId(), nodeType.getNodeTypeId(),nodeId);
//			return processInstanceNodeInfoMapper.toTransactionListDto(processInstanceNodeInfos);
//		} 
//		throw new RequestDataNotFoundException("Node Transaction Details Not Found");
//	} 

	@Override
	public List<ProcessResponseDTO> getProcessNames(int onlyActive) {
		List<ProcessResponseDTO> processNames = new ArrayList<ProcessResponseDTO>();
		List<Object[]> processList = null;
		if (onlyActive == 1) {
			processList = processRepository.getActiveProcessNames();
		} else {
			processList = processRepository.getAvailableProcessNames();
		}
		if (!CollectionUtils.isEmpty(processList)) {
			for (Object[] obj : processList) {
				ProcessResponseDTO process = new ProcessResponseDTO();
				process.setId((int) obj[0]);
				process.setName(obj[1] != null ? obj[1].toString() : "");
				processNames.add(process);
			}
		}
		return processNames;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "getProcessVersionNamesByProcessId", key = "#processId")
	@Override
	public List<Object[]> getProcessVersionNamesByProcessId(Integer processId) throws Exception {

		List<Object[]> processVersionList = processRepository.getProcessVersionNames(processId);
		return processVersionList;
	}

	@Override
	public List<ProcessResponseDTO> getProcessVersions(int processId) throws Exception {
		List<ProcessResponseDTO> versionNames = new ArrayList<ProcessResponseDTO>();
		List<Object[]> processVersionList = getProcessVersionNamesByProcessId(processId);
		if (!CollectionUtils.isEmpty(processVersionList)) {
			for (Object[] obj : processVersionList) {
				ProcessResponseDTO processVersion = new ProcessResponseDTO();
				processVersion.setId((int) obj[0]);
				processVersion.setName(obj[1] != null ? obj[1].toString() : "");
				processVersion.setVersion((int) obj[2]);
				versionNames.add(processVersion);
			}
		}
		return versionNames;
	}

	public void updateNodeColors(String nodeTypeName, String xmlString, HashMap<String, String> nodeColors)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
		if (!StringUtils.equalsIgnoreCase(Constants.BPMN_DIAGRAMN, nodeTypeName)
				&& !StringUtils.equalsIgnoreCase("bpmn:process", nodeTypeName)) {
			NodeList processElements = document.getElementsByTagName(nodeTypeName);
			Element processElement = (Element) processElements.item(0);
			String id = processElement.getAttribute("id");
			String nodeColor = null;
			NodeList colorNodes = document.getElementsByTagNameNS(Constants.NAMESPACE, "colorProperties");
			for (int i = 0; i < colorNodes.getLength(); i++) {
				Element colorNode = (Element) colorNodes.item(i);
				if (StringUtils.equalsIgnoreCase(colorNode.getAttribute("name"), "DefaultColor")) {
					nodeColor = colorNode.getAttribute("color");
				}
			}
			nodeColors.put(id, nodeColor);
		}
	}

	public void updateNodeColorsForWorkflow(String nodeTypeName, String xmlString, HashMap<String, String> nodeColors,
			ProcessInstanceNodeInfo processinstanceNodinfo)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
		if (!StringUtils.equalsIgnoreCase(Constants.BPMN_DIAGRAMN, nodeTypeName)
				&& !StringUtils.equalsIgnoreCase("bpmn:process", nodeTypeName)) {
			NodeList processElements = document.getElementsByTagName(nodeTypeName);
			Element processElement = (Element) processElements.item(0);
			String id = processElement.getAttribute("id");
			String nodeColor = null;
			NodeList colorNodes = document.getElementsByTagNameNS(Constants.NAMESPACE, "colorProperties");
			for (int i = 0; i < colorNodes.getLength(); i++) {
				Element colorNode = (Element) colorNodes.item(i);

				if (processinstanceNodinfo != null && processinstanceNodinfo.getStatusId().getId() == 5
						&& StringUtils.equalsIgnoreCase(colorNode.getAttribute("name"), "DefaultColor")) {
					nodeColor = colorNode.getAttribute("color");
				}
				if ((processinstanceNodinfo == null || processinstanceNodinfo.getStatusId().getId() == 1
						|| processinstanceNodinfo.getStatusId().getId() == 2)
						&& StringUtils.equalsIgnoreCase(colorNode.getAttribute("name"), "DefaultColor")) {

					nodeColor = "White";
				}

				if (processinstanceNodinfo != null && processinstanceNodinfo.getStatusId().getId() == 3
						&& StringUtils.equalsIgnoreCase(colorNode.getAttribute("name"), "FailureColor")) {
					nodeColor = colorNode.getAttribute("color");
				}

				if (processinstanceNodinfo != null && processinstanceNodinfo.getStatusId().getId() == 4
						&& StringUtils.equalsIgnoreCase(colorNode.getAttribute("name"), "SuccessColor")) {
					nodeColor = colorNode.getAttribute("color");
				}
			}
			nodeColors.put(id, nodeColor);
		}
	}

	@Override
	public List<String> getPossibleActions(int processInstanceId, int processVersionId, String userRole) {

		List<String> actions = new ArrayList<>();
		List<ProcessSequenceDetails> processSequenceDetailsList = new ArrayList<>();

		if (processVersionId > 0) {
			processSequenceDetailsList = processSequenceDetailsRepository
					.getProcessSequenceDetailsByVersionid(processVersionId);
			if (!processSequenceDetailsList.isEmpty()) {
				if (processInstanceId > 0) {
					int processNodeId = processInstanceNodeInfoRepository.getProcessNodeInfoId(processInstanceId);
					if (processNodeId > 0) {
						actions = getActionsFromProcessSequence(processVersionId, processNodeId,
								processSequenceDetailsList, userRole);

					}
				}
			}
		}
		return actions;
	}

	private List<String> getActionsFromProcessSequence(int processVersionId, int processNodeId,
			List<ProcessSequenceDetails> processSequenceDetailsList, String userRole) {

		List<String> actions = new ArrayList<>();
		List<ProcessVariables> processVariables = new ArrayList<>();

		processVariables = processVariableRepository.getProcessVariablesByNodeIdAndVariableType(processVersionId,
				Constants.POSSIBLE_ACTIONS);

		if (!processVariables.isEmpty()) {

			actions = getActionsFromNode(actions, processVariables, processSequenceDetailsList, processNodeId,
					userRole);
		}
		return actions;
	}

	private List<String> getActionsFromNode(List<String> actions, List<ProcessVariables> processVariables,
			List<ProcessSequenceDetails> processSequenceDetailsList, int processNodeId, String userRole) {
		boolean exists = processVariables.stream()
				.anyMatch(variable -> variable.getProcessNodeInfoId().getId() == processNodeId);
		if (exists) {
			boolean isEligible = Boolean.FALSE;
			String allUsers = nodeInfoRepository.getUserNodeAssignAllDetailsForGivenProcessNodeInfoId(processNodeId);
			if (org.thymeleaf.util.StringUtils.equalsIgnoreCase(allUsers, Boolean.TRUE)) {

				isEligible = Boolean.TRUE;
			} else {
				List<Object[]> resultList = nodeInfoRepository.getUserDetailsByProcessNodeInfoId(processNodeId);
				for (Object[] result : resultList) {
					if (result != null && result.length > 0) {

						String userNameDynamic = result[0].toString();
						String userRoleDynamic = result[1].toString();
						if (StringUtils.equalsIgnoreCase(userRoleDynamic, userRole)) {
							isEligible = Boolean.TRUE;
						}
					} else {
						isEligible = Boolean.TRUE;
					}

				}
			}
			if (isEligible) {
				actions = processVariables.stream()
						.filter(variable -> variable.getProcessNodeInfoId().getId() == (processNodeId))
						.map(ProcessVariables::getValue) // Extract value
						.findFirst().map(action -> Arrays.asList(action.split(","))) // Split by comma and convert to
																						// list
						.orElse(Collections.emptyList());
			}
		} else {
			actions = getActionsFromNextNode(processNodeId, processSequenceDetailsList, processVariables, userRole);

		}
		return actions;
	}

	private List<String> getActionsFromNextNode(int processNodeId,
			List<ProcessSequenceDetails> processSequenceDetailsList, List<ProcessVariables> processVariables,
			String userRole) {

		List<String> nextNodeActions = new ArrayList<>();

		int nextNodeId = processSequenceDetailsList.stream()
				.filter(sequence -> sequence.getSourceNodeId().getId() == processNodeId).map(sequence -> {
					ProcessNodeInfo targetNodeId = sequence.getTargetNodeId();
					if (targetNodeId != null) {
						return targetNodeId.getId();
					} else {
						return 0;
					}
				}).findFirst().orElse(0);

		if (nextNodeId > 0) {
			nextNodeActions = getActionsFromNode(nextNodeActions, processVariables, processSequenceDetailsList,
					nextNodeId, userRole);

		}
		return nextNodeActions;
	}

}
