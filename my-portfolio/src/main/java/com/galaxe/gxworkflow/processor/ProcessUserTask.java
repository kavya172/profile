package com.galaxe.gxworkflow.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.dto.TransactionInfo;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessVariables;
import com.galaxe.gxworkflow.entity.ProcessVariablesDetails;
import com.galaxe.gxworkflow.exception.WorkflowException;
import com.galaxe.gxworkflow.repository.NodeStatusRepository;
import com.galaxe.gxworkflow.repository.ProcessNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableDetailsRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableRepository;
import com.galaxe.gxworkflow.services.ProcessVariableDetailsService;
import com.galaxe.gxworkflow.services.ProcessVariablesService;
import com.galaxe.gxworkflow.services.impl.ProcessNodeInfoServiceImpl;
import com.galaxe.gxworkflow.services.impl.ProcessTransactionService;
import com.galaxe.gxworkflow.util.TimestampUtil;

/**
 * The ProcessUserTask class provides operations to be performed for UserTask of
 * a process.
 * 
 * @Autowired is used for creating an reference object for class level usage.
 */

@Component
public class ProcessUserTask {

	@Autowired
	private ProcessVariableDetailsRepository processVariableDetailsRepository;

	@Autowired
	private ProcessNodeInfoServiceImpl processNodeInfoServiceImpl;

	@Autowired
	private ProcessNodeInfoRepository processNodeInfoRepository;

	@Autowired
	private ProcessTransactionService processTransactionService;

	@Autowired
	private ProcessComplete processComplete;

	@Autowired
	private NodeStatusRepository nodeStatusRepository;

	@Autowired
	private ProcessVariablesService processVariablesService;

	@Autowired
	private ProcessVariableDetailsService processVariableDetailsService;
	
	@Autowired
	private ProcessVariableRepository processVariableRepository;

	/**
	 * Create a userTask action for a task using nodeInfo and transactionInfo.
	 *
	 * <p>
	 * This method creates TransactionDetails based on the provided nodeInfo and
	 * transactionInfo.
	 * 
	 * To create userTask action (transactionDetails), createTaskAction is a method
	 * of {@link ProcessTasksActionService} is used by providing data as taskId,
	 * transactionInfo
	 * 
	 * If the transactionDetails for the node is "Completed" Then will collect the
	 * child of that node to achieve sequential transaction.
	 * 
	 * findByParentNodeId is a method of {@link ProcessNodeDetailsRepository} is to
	 * retrieves the list of childsNodeInfos.
	 * 
	 * Calling the findByParentNodeId method by providing data as nodeInfo.
	 * 
	 * To achieve sequential transaction for the childNodes , executor is a method
	 * of {@link ExecutorJob} is used by providing data as childsNodeInfos,
	 * transactionInfo.
	 *
	 * @param nodeInfo  type of ProcessNodeDetails, containing the details about the
	 *                  node
	 * @param taskId    type of Integer, is a Id of an Relational entity which is
	 *                  mapped in the {@link ProcessTasks} Entity
	 * @param transInfo type of {@link TransactionInfo}, object contains necessary
	 *                  information to create transactionDetails
	 */
	@Deprecated
	public void createUserTaskAction(ProcessNodeInfo nodeInfo, int taskId, TransactionInfo transactionInfo) {

//		processTasksActionService.createTaskAction(taskId, transactionInfo);

		// FIXME: USE FROM CONSTANT
		if (!transactionInfo.getStatus().equalsIgnoreCase("INPROGRESS")) {

//			processTasksService.updateTask(taskId, transactionInfo);

//			List<ProcessNodeDetails> childsNodeInfo = processNodeDetailsRepo.findByParentNodeId(nodeInfo);

			// FIXME: USE FROM CONSTANT
			transactionInfo.setStatus("INPROGRESS");

//			executorJob.executor(childsNodeInfo, transactionInfo);
		}
	}

	/**
	 * Create a userTask transaction using nodeInfo and transactionInfo.
	 *
	 * <p>
	 * This method is used to create userTask transaction based on the provided
	 * nodeInfo and transactionInfo.
	 * 
	 * findByWorkFlowIdAndNodeIdAndWfStatus is a method of
	 * {@link ProcessTasksRepository} which is to retrieve the available userTask.
	 * 
	 * If no task with "InProgress" state is available for a particular @param
	 * workflowId, @param nodeInfo. Then we will create a new task.
	 * 
	 * createTask is a method of {@link ProcessTasksService} which to create a
	 * userTask by providing data as nodeId obtained from nodeInfo, transactionInfo.
	 * 
	 * createUserTaskAction method is used to create taskAction (transactionDetails)
	 * for that userTask (transaction) by providing data as taskId obtained from
	 * created userTask, transactionInfo.
	 * 
	 * @param processInstanceRequest
	 * @param processInstanceNodeInfo
	 *
	 * @param nodeInfo                type of ProcessNodeDetails, containing the
	 *                                details about the node
	 * @param transInfo               type of {@link TransactionInfo}, the
	 *                                TransactionInfo object containing the process
	 *                                ID and additional data
	 * @throws Exception
	 */
//	public void createUserTaskTransaction(ProcessNodeInfo nodeInfo, TransactionInfo transactionInfo) {
//		ProcessTasks userTask = processTasksRepo.findByWorkFlowIdAndNodeIdAndWfStatus(transactionInfo.getWorkFlowId(),
//				nodeInfo, "INPROGRESS");
//		if (ObjectUtils.isEmpty(userTask)) {
//			userTask = processTasksService.createTask(nodeInfo.getNodeId(), transactionInfo);
//		}
//		this.createUserTaskAction(nodeInfo, userTask.getTaskId(), transactionInfo);
//	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void ExecuteUserTask(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest,
			ProcessInstanceNodeInfo processInstanceNodeInfo) throws Exception {

		try {
				Boolean isExecutable = isUserTaskExecutable(processNodeInfoId, processInstanceRequest);
				if (isExecutable) {
					populateVariableValues(processInstanceRequest, processInstanceNodeInfo, processNodeInfoId);
				} else {
//				logFailedUserTask(processNodeInfoId, processInstanceRequest);
					throw new WorkflowException("User is not authorized to complete the User Task.");
				}
		} catch (Exception e) {
			logFailedUserTask(processNodeInfoId, processInstanceRequest);
			throw e;
		}
	}

	// GxCapture
		public boolean ExecuteUserTaskForWorkflow(int processNodeInfoId, WorkflowRequest workflowRequest,
				ProcessInstanceNodeInfo processInstanceNodeInfo) throws Exception {
			try {
				Boolean isExecutable = isUserTaskExecutableForWorkflow(processNodeInfoId, workflowRequest);
				
				if (isExecutable) {
					populateVariableValuesForWorkflow(workflowRequest, processInstanceNodeInfo, processNodeInfoId);
				} else {
					logFailedUserTaskForWorkflow(processNodeInfoId, workflowRequest);
					return false;
				}
			} catch (Exception e) {
//				logFailedUserTask(processNodeInfoId, processInstanceRequest);
				throw  e;
			}
			return true;

	}

	private void logFailedUserTaskForWorkflow(int processNodeInfoId, WorkflowRequest workflowRequest) throws Exception {
			
		processTransactionService.postTransactionPendingForWorkflow(workflowRequest.getProcessInstanceId(),
				processNodeInfoId, workflowRequest.getUserName());
		processComplete.updateProcessStatus(workflowRequest.getProcessInstanceId(),
				workflowRequest.getUserName(), ProcessStatusConstant.PENDING.getCode());
			
		}

	private void logFailedUserTask(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest)
			throws Exception {
		processTransactionService.postTransactionFailed(processInstanceRequest.getProcessInstanceId(),
				processNodeInfoId, processInstanceRequest.getUserName(),ProcessStatusConstant.FAILED.getCode());
		processComplete.updateProcessStatus(processInstanceRequest.getProcessInstanceId(),
				processInstanceRequest.getUserName(), ProcessStatusConstant.PENDING.getCode());
	}
	
	// @Cacheable(value = "isProcessExecutable", key = "#processId")
	public boolean isUserTaskExecutable(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest)
			throws Exception {

		String allUsers = processNodeInfoServiceImpl
				.getUserNodeAssignAllDetailsForGivenProcessNodeInfoId(processNodeInfoId);
		if (StringUtils.equalsIgnoreCase(allUsers, Boolean.TRUE)) {
			return true;
		}

		List<Object[]> resultList = processNodeInfoServiceImpl.getUserDetailsByProcessNodeInfoId(processNodeInfoId);

		String userName = processInstanceRequest.getUserName();
		String userRole = processInstanceRequest.getUserRole();

		Boolean isEligible = Boolean.FALSE;
		Boolean isUserEligible = Boolean.FALSE;
		Boolean isRoleEligible = Boolean.FALSE;
		for (Object[] result : resultList) {
			if (result != null && result.length > 0) {

				String userNameDynamic = null != result[0].toString()?result[0].toString():"";
				String userRoleDynamic = null != result[1].toString()?result[1].toString():"";
				if (((String) result[0]).matches("\\{([^}]*)\\}")) {
					userNameDynamic = getDynamicValue(processInstanceRequest.getDynamicAttributes(),
							(String) result[0]);
				}
				if (((String) result[1]).matches("\\{([^}]*)\\}")) {
					userRoleDynamic = getDynamicValue(processInstanceRequest.getDynamicAttributes(),
							(String) result[1]);
				}

				if (StringUtils.equalsIgnoreCase(userNameDynamic, userName)) {
					isUserEligible = Boolean.TRUE;
				}

				if (StringUtils.equalsIgnoreCase(userRoleDynamic, userRole)) {
					isRoleEligible = Boolean.TRUE;
				}
			} else {
				isEligible = Boolean.TRUE;
			}
		}

		if (!isEligible && (isUserEligible || isRoleEligible)) {
			isEligible = Boolean.TRUE;
		} else {
			isEligible = Boolean.FALSE;
		}
		return isEligible;
	}

	
	//GxCapture
	private Boolean isUserTaskExecutableForWorkflow(int processNodeInfoId, WorkflowRequest workflowRequest) {
		String allUsers = processNodeInfoRepository.getUserNodeAssignAllDetailsForGivenProcessNodeInfoId(processNodeInfoId);
		ProcessVariables processVariable  = processVariableRepository.findByProcessNodeInfoIdIdAndName(processNodeInfoId,"possibleActions");
		List<String> namesList = Arrays.asList(processVariable.getValue().split(","));
		
		if(StringUtils.equalsIgnoreCase(allUsers,Boolean.TRUE) && !StringUtils.isEmpty(workflowRequest.getAction()) && namesList.contains(workflowRequest.getAction())) {
			return true;
		}
		
		if(StringUtils.isEmpty(workflowRequest.getAction())) {
			workflowRequest.setMessage("Action is Required to Perform ");
			return false;
			
		}
		
		if(!namesList.contains(workflowRequest.getAction())) {
			workflowRequest.setMessage("Action Doesnot Matches");
			return false;
		}
		
		List<Object[]> resultList = processNodeInfoRepository
				.getUserDetailsByProcessNodeInfoId(processNodeInfoId);

		String userName = workflowRequest.getUserName();
		String userRole = workflowRequest.getUserRole();
		Boolean isEligible = Boolean.FALSE;
		Boolean isUserEligible = Boolean.FALSE;
		Boolean isRoleEligible = Boolean.FALSE;
		for (Object[] result : resultList) {
			if (result != null && result.length > 0) {

				String userNameDynamic = null != result[0].toString()?result[0].toString():"";
				String userRoleDynamic = null != result[1].toString()?result[1].toString():"";
					if(((String) result[0]).matches("\\{([^}]*)\\}")) {
						userNameDynamic = getDynamicValue(workflowRequest.getDynamicAttributes(),(String) result[0]);
					}
					if (((String) result[1]).matches("\\{([^}]*)\\}")) {
						userRoleDynamic = getDynamicValue(workflowRequest.getDynamicAttributes(),
								(String) result[1]);
					}

					if (StringUtils.equalsIgnoreCase(userNameDynamic, userName)) {
						isUserEligible = Boolean.TRUE;
					}

					if (StringUtils.equalsIgnoreCase(userRoleDynamic, userRole)) {
						isRoleEligible = Boolean.TRUE;
					}
				}else {
					isEligible = Boolean.TRUE;
				}
			
		}
		
		if(!isEligible && (isUserEligible || isRoleEligible)) {
			isEligible = Boolean.TRUE;
		}else {
			isEligible = Boolean.FALSE;
			workflowRequest.setMessage("User is not Authorized");
		}
		return isEligible;
		}
	
	

	private String getDynamicValue(Map<String, Object> dynamicAttributes, String value) {
		if (value.matches("\\{([^}]*)\\}")) {
			Object obj = dynamicAttributes.get(value.replace("{", "").replace("}", ""));
			if (null != obj) {
				return obj.toString();
			} else {
				return "";
			}
		} else {
			return value;
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void populateVariableValues(ProcessInstanceRequest processInstanceRequest,
			ProcessInstanceNodeInfo processInstanceNodeInfo, int nextNodeInfo) throws Exception {

//		ProcessInstance processInstance = processInstanceRepository
//				.findById(processInstanceRequest.getProcessInstanceId()).get();
		ProcessNodeInfo processNodeInfo = processNodeInfoServiceImpl.findById(nextNodeInfo);

		// FIXME: USE CACHE
		List<ProcessVariables> processVariables = processVariablesService
				.findProcessVariablesByProcessNodeInfo(processNodeInfo);
		List<ProcessVariablesDetails> processVariablesDetailsList = new ArrayList<ProcessVariablesDetails>();
		Timestamp time = TimestampUtil.getCurrentTimestamp();

		if (!CollectionUtils.isEmpty(processVariables)) {
			processVariables.stream().forEach(processVariable -> {
				ProcessVariablesDetails processVariablesDetails = new ProcessVariablesDetails();
				Object obj = processInstanceRequest.getDynamicAttributes()
						.get(processVariable.getValue().replace("{", "").replace("}", ""));
				if (null != obj) {
					processVariablesDetails.setValue(obj.toString());
				} else {
					if (processVariable.getValue() != null) {

						if (processVariable.getValue().startsWith("{") && processVariable.getValue().endsWith("}")) {
							String globalValue = processVariable.getValue().replace("{", "").replace("}", "");
							Object value;
							try {
								value = processVariableDetailsService.findValueforProcessVariableByProcessVariableName(
										processInstanceRequest.getProcessInstanceId(), globalValue);
								if (null != value) {
									processVariablesDetails.setValue(value.toString());
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								throw new WorkflowException(e.getLocalizedMessage());
							}
						} else {
							processVariablesDetails.setValue(processVariable.getValue());
						}
					}
				}
//				processVariablesDetails.setProcessInstanceId(processInstance);
				processVariablesDetails.setProcessInstanceNodeInfoId(processInstanceNodeInfo);
				processVariablesDetails.setProcessVariableId(processVariable);
				processVariablesDetails.setCreatedBy(processInstanceRequest.getUserName());
				processVariablesDetails.setCreatedOn(time);
				processVariablesDetails.setModifiedBy(processInstanceRequest.getUserName());
				processVariablesDetails.setModifiedOn(time);
				processVariablesDetailsList.add(processVariablesDetails);
			});

			processVariableDetailsRepository.saveAll(processVariablesDetailsList);
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void populateVariableValuesForWorkflow(WorkflowRequest workflowRequest,
			ProcessInstanceNodeInfo processInstanceNodeInfo, int nextNodeInfo) throws Exception {

//		ProcessInstance processInstance = processInstanceRepository
//				.findById(processInstanceRequest.getProcessInstanceId()).get();
		ProcessNodeInfo processNodeInfo = processNodeInfoServiceImpl.findById(nextNodeInfo);

		// FIXME: USE CACHE
		List<ProcessVariables> processVariables = processVariableRepository.findByProcessNodeInfoId(processNodeInfo);
		List<ProcessVariablesDetails> processVariablesDetailsList = new ArrayList<ProcessVariablesDetails>();
		Timestamp time = TimestampUtil.getCurrentTimestamp();

		if (!CollectionUtils.isEmpty(processVariables)) {
			processVariables.stream().forEach(processVariable -> {
				ProcessVariablesDetails processVariablesDetails = new ProcessVariablesDetails();
				Object obj = workflowRequest.getDynamicAttributes()
						.get(processVariable.getValue().replace("{", "").replace("}", ""));
				if (null != obj) {
					processVariablesDetails.setValue(obj.toString());
				} else {
					if (processVariable.getValue() != null) {

						if (processVariable.getValue().startsWith("{") && processVariable.getValue().endsWith("}")) {
							String globalValue = processVariable.getValue().replace("{", "").replace("}", "");
							String value = processVariableDetailsRepository
									.getValueforProcessVariable(processNodeInfo.getId(), globalValue);
							processVariablesDetails.setValue(value);
						} else {
							processVariablesDetails.setValue(processVariable.getValue());
						}
					}
				}
//				processVariablesDetails.setProcessInstanceId(processInstance);
				processVariablesDetails.setProcessInstanceNodeInfoId(processInstanceNodeInfo);
				processVariablesDetails.setProcessVariableId(processVariable);
				processVariablesDetails.setCreatedBy(workflowRequest.getUserName());
				processVariablesDetails.setCreatedOn(time);
				processVariablesDetails.setModifiedBy(workflowRequest.getUserName());
				processVariablesDetails.setModifiedOn(time);
				processVariablesDetailsList.add(processVariablesDetails);
			});

			processVariableDetailsRepository.saveAll(processVariablesDetailsList);
		}
	}
	

}
