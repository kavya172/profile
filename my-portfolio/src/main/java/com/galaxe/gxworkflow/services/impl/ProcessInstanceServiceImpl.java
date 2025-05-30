package com.galaxe.gxworkflow.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.dto.ProcessInstanceResponse;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.entity.NodeStatusColor;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessStatus;
import com.galaxe.gxworkflow.entity.ProcessVariables;
import com.galaxe.gxworkflow.entity.ProcessVariablesDetails;
import com.galaxe.gxworkflow.entity.ProcessVersion;
import com.galaxe.gxworkflow.exception.DataNotFoundException;
import com.galaxe.gxworkflow.exception.WorkflowException;
import com.galaxe.gxworkflow.processor.ProcessComplete;
import com.galaxe.gxworkflow.processor.ProcessEndEvent;
import com.galaxe.gxworkflow.processor.ProcessScriptTask;
import com.galaxe.gxworkflow.processor.ProcessSendTask;
import com.galaxe.gxworkflow.processor.ProcessUserTask;
import com.galaxe.gxworkflow.repository.NodeStatusRepository;
import com.galaxe.gxworkflow.repository.ProcessInstanceNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessInstanceRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableDetailsRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableRepository;
import com.galaxe.gxworkflow.repository.ProcessVersionRepository;
import com.galaxe.gxworkflow.services.ProcessInstanceService;
import com.galaxe.gxworkflow.services.ProcessService;
import com.galaxe.gxworkflow.services.ProcessStatusService;
import com.galaxe.gxworkflow.services.ProcessVariableDetailsService;
import com.galaxe.gxworkflow.services.ProcessVariablesService;
import com.galaxe.gxworkflow.services.ProcessVersionService;
import com.galaxe.gxworkflow.util.Constants;
import com.galaxe.gxworkflow.util.TimestampUtil;

/**
 * 
 * This class provides the implementation for the methods defined in the
 * interface {@link ProcessInstanceService}
 * 
 * The ProcessInstanceServiceImpl class provides operations for managing Process
 * Related Information.
 * 
 * This service class handles business logic related to Process management.
 *
 * @Autowired is used for creating an reference object for class level usage.
 *
 */
@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

	private static final Logger logger = LoggerFactory.getLogger(ProcessInstanceServiceImpl.class);

	@Autowired
	private ProcessInstanceRepository processInstanceRepository;

	@Autowired
	private ProcessStatusService processStatusService;

	@Autowired
	private ProcessInstanceNodeInfoRepository processNodeInstanceInfoRepository;

	@Autowired
	private ProcessScriptTask processScriptTask;

	@Autowired
	private ProcessEndEvent processEndEvent;

	@Autowired
	private ProcessUserTask processUserTask;

	@Autowired
	private com.galaxe.gxworkflow.processor.ProcessServiceTask processServiceTask;

	@Autowired
	private ProcessSendTask processSendTask;

	@Autowired
	private ProcessTransactionService processTransactionService;

//	@Autowired
//	private ProcessExclusiveGateway processExclusiveGateway;

	@Autowired
	private ProcessVariableRepository processVariableRepository;

	@Autowired
	ProcessComplete processComplete;

	@Autowired
	private ProcessService processService;

	@Autowired
	private ProcessVersionRepository processVersionRepo;

	@Autowired
	private ProcessVariableDetailsRepository processVariableDetailsRepository;

	@Autowired
	ProcessNodeInfoServiceImpl processNodeInfoServiceImpl;

	@Autowired
	ProcessInstanceNodeInfoRepository processInstanceNodeInfoRepository;

	@Autowired
	private ProcessVersionService processVersionService;
	
	@Autowired
	NodeStatusRepository nodeStatusRepository;

	@Autowired
	private ProcessVariablesService processVariablesService;
	
	@Autowired
	private ProcessVariableDetailsService processVariableDetailsService;

//	@Transactional(propagation = Propagation.SUPPORTED)
//	@Cacheable(value = "findByProcessVersion", key = "#process.id_processverison.versionId")
//	@Override
//	public ProcessInstance findByProcessVersion(ProcessVersion processVersion) throws Exception {
//		ProcessInstance processInstance = processInstanceRepository.findByVersionId(processVersion);
//		return processInstance;
//	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Boolean validatePreInitiate(ProcessInstanceRequest processInstanceRequest) throws Exception {

		// Step 1: Validate the processId required for the instance creation.
		if (null == processInstanceRequest.getProcessName()) {
			throw new DataNotFoundException("Process name is required.");
		}

		if (null == processInstanceRequest.getVersionNumber()) {
			throw new DataNotFoundException("Version Number is required.");
		}

		if (StringUtils.isEmpty(processInstanceRequest.getUserName())) {
			throw new DataNotFoundException("User name is required.");
		}

		if (StringUtils.isEmpty(processInstanceRequest.getUserRole())) {
			throw new DataNotFoundException("User role is required.");
		}

		// Step 2: Check for the authorization, active, executable.
		boolean isExecutable = processNodeInfoServiceImpl.isProcessExecutable(processInstanceRequest);
		if (!isExecutable) {
			throw new WorkflowException("Process is not executable");
		}

//		Process process = processService.findByProcessId(processInstanceRequest.getProcessId());
//		if (findByProcess(process) != null) {
//			throw new WorkflowException("Process already started.");
//		}
		logger.info("Validations passed.");
		return true;
	}

	@Override
	public void validatePreInitiateProcess(WorkflowRequest workflowRequest) {

		if (workflowRequest.getVersionId() == null || workflowRequest.getVersionId() == 0) {
			throw new DataNotFoundException("Version Number is required.");
		}
		boolean isExecutable = processNodeInfoServiceImpl.isProcessExecutableForExecution(workflowRequest);
		if (!isExecutable) {
			throw new WorkflowException("Process is not executable");
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Integer initiateProcessInstanceNew(ProcessInstanceRequest processInstanceRequest) throws Exception {

		logger.info("Process starting....");
		
		ProcessVersion processVersion = processVersionService.findProcessVersion(processInstanceRequest.getProcessName(), processInstanceRequest.getVersionNumber());

//		ProcessVersion processVersion = processVersionRepo.findByProcessNameAndVersionNumber(
//				processInstanceRequest.getProcessName(), processInstanceRequest.getVersionNumber());
		if (processVersion == null) {
			throw new WorkflowException("Process not found.");
		}

		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setVersionId(processVersion);
		Timestamp time = TimestampUtil.getCurrentTimestamp();
		ProcessStatus processStatus = processStatusService
				.findProcessStatusByCode(ProcessStatusConstant.STARTED.getCode());
		processInstance.setStatusId(processStatus);
		processInstance.setCreatedBy(processInstanceRequest.getUserName());
		processInstance.setCreatedOn(time);
		processInstance.setModifiedBy(processInstanceRequest.getUserName());
		processInstance.setModifiedOn(time);

		processInstance = processInstanceRepository.save(processInstance);

		processInstanceRequest.setProcessInstanceId(processInstance.getId());

		// Assuming only one process Node present in the system for a given processId
		ProcessNodeInfo processNodeInfo = processNodeInfoServiceImpl
				.findByVersionIdAndNodeTypeId(processVersion.getId(), 6);

		saveProcessInstanceNodeInfoAndVariableDetails(processInstanceRequest, processNodeInfo);

		// Step 5: Execute start node.
		executeStartNode(processInstanceRequest);

		logger.info("Process started.");

		return processInstance.getId();
	}

	// GxCapture
	@Override
	public Integer initiateProcessInstanceNewWorkflow(WorkflowRequest workflowRequest) throws Exception {

		ProcessVersion processVersion = processVersionRepo.findById(workflowRequest.getVersionId()).orElse(null);

		if (processVersion == null) {
			throw new WorkflowException("Process not found.");
		}

		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setVersionId(processVersion);
		Timestamp time = TimestampUtil.getCurrentTimestamp();
		ProcessStatus processStatus = processStatusService
				.findProcessStatusByCode(ProcessStatusConstant.STARTED.getCode());
		processInstance.setStatusId(processStatus);
		processInstance.setCreatedBy(workflowRequest.getUserName());
		processInstance.setCreatedOn(time);
		processInstance.setModifiedBy(workflowRequest.getUserName());
		processInstance.setModifiedOn(time);

		processInstance = processInstanceRepository.save(processInstance);
		workflowRequest.setProcessInstanceId(processInstance.getId());
		ProcessNodeInfo processNodeInfo = processNodeInfoServiceImpl
				.findByVersionIdAndNodeTypeId(processVersion.getId(), 6);
		saveProcessInstanceNodeInfoAndVariableDetailsForWorkflow(workflowRequest, processNodeInfo);

		executeStartNodeForWorkflow(workflowRequest);

		return processInstance.getId();
	}

	// GxCapture
	private void executeStartNodeForWorkflow(WorkflowRequest workflowRequest) throws Exception {

		ProcessVersion processVersion = processVersionRepo.findById(workflowRequest.getVersionId()).orElse(null);

		if (processVersion == null) {
			throw new WorkflowException("Process not found.");
		}

		ProcessNodeInfo startNodeInfo = processNodeInfoServiceImpl.findByVersionIdAndNodeTypeId(processVersion.getId(),
				2);
		saveProcessInstanceNodeInfoAndVariableDetailsForWorkflow(workflowRequest, startNodeInfo);

	}

	// GxCapture
	private void saveProcessInstanceNodeInfoAndVariableDetailsForWorkflow(WorkflowRequest workflowRequest,
			ProcessNodeInfo processNodeInfo) throws Exception {
		
			// FIXME: USE CACHE: Use after complete implementation
			List<ProcessVariables> processVariables = processVariablesService.findProcessVariablesByProcessNodeInfo(processNodeInfo);
//			List<ProcessVariables> processVariables = processVariableRepository.findByProcessNodeInfoId(processNodeInfo);

		// FIXME: USE CACHE
//		List<ProcessVariables> processVariables = processVariableRepository.findByProcessNodeInfoId(processNodeInfo);

		List<ProcessVariablesDetails> processVariablesDetailsList = new ArrayList<ProcessVariablesDetails>();
		Timestamp time = TimestampUtil.getCurrentTimestamp();
		String createdBy = workflowRequest.getUserName();

		ProcessInstanceNodeInfo processNodeInstanceInfo = saveProcessInstanceNodeInfo(
				workflowRequest.getProcessInstanceId(), processNodeInfo, time, createdBy);

		saveProcessVariablesForWorkflow(workflowRequest, processVariables, processVariablesDetailsList, time, createdBy,
				processNodeInstanceInfo);

		// get the status for the Start Node and set it
		// if only start node
	}

	// GxCapture
	private void saveProcessVariablesForWorkflow(WorkflowRequest workflowRequest,
			List<ProcessVariables> processVariables, List<ProcessVariablesDetails> processVariablesDetailsList,
			Timestamp time, String createdBy, ProcessInstanceNodeInfo processNodeInstanceInfo) {

		// Step: extract all process variables dynamic values from the input request
		// object.
		if (!CollectionUtils.isEmpty(processVariables)) {
			processVariables.stream().forEach(processVariable -> {
				ProcessVariablesDetails processVariablesDetails = new ProcessVariablesDetails();
				Object obj = workflowRequest.getDynamicAttributes()
						.get(processVariable.getValue().replace("{", "").replace("}", ""));
				if (null != obj) {
					processVariablesDetails.setValue(obj.toString());
				} else {
					processVariablesDetails.setValue(processVariable.getValue());
				}
				processVariablesDetails.setProcessInstanceNodeInfoId(processNodeInstanceInfo);
				processVariablesDetails.setProcessVariableId(processVariable);
				processVariablesDetails.setCreatedBy(createdBy);
				processVariablesDetails.setCreatedOn(time);
				processVariablesDetails.setModifiedBy(createdBy);
				processVariablesDetails.setModifiedOn(time);
				processVariablesDetailsList.add(processVariablesDetails);
			});
			processVariableDetailsRepository.saveAll(processVariablesDetailsList);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Async
	public void continueWorkflowProcess(ProcessInstanceRequest processInstanceRequest) throws Exception {

		logger.info("Process continuing.");

		// ProcessInstance processInstance =
		// processInstanceRepository.findById(processInstanceRequest.getProcessInstanceId()).get();

		// Step 3: Loop thru the nodes.
		resumeWorkflowExecution(processInstanceRequest);

		logger.info("Process completed.");

	}

	// GxCapture
	public void continueExecutionProcess(WorkflowRequest workflowRequest) throws Exception {

		logger.info("Process continuing.");

		// ProcessInstance processInstance =
		// processInstanceRepository.findById(processInstanceRequest.getProcessInstanceId()).get();

		// Step 3: Loop thru the nodes.
		resumeWorkflowExecutionForWorkflow(workflowRequest);

		logger.info("Process completed.");

	}

	@Override
	// Not cacheable.
	public ProcessInstanceResponse getProcessInstanceById(int processInstanceId) {
		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
		if (ObjectUtils.isEmpty(processInstance)) {
			throw new WorkflowException("Data not Found");
		}

		ProcessVersion verObj = processVersionRepo.findById(processInstance.getVersionId().getId()).orElse(null);

		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		processInstanceResponse.setProcessInstanceId(processInstanceId);
		processInstanceResponse.setStatus(processInstance.getStatusId().getDescription());
		processInstanceResponse.setProcessName(verObj.getProcessId().getProcessName());
		processInstanceResponse.setVersion(verObj.getVersionNumber());
		processInstanceResponse.setVersionId(verObj.getId());

		return processInstanceResponse;
	}

	@Override
	// Not cachable
	public List<ProcessInstanceResponse> getProcessInstances() {
		List<ProcessInstance> processInstanceList = processInstanceRepository.findAll();

		// collecting version ids
		List<Integer> ids = processInstanceList.stream().map(ProcessInstance::getVersionId).map(ProcessVersion::getId)
				.collect(Collectors.toList());

		List<ProcessVersion> processVersionList = processVersionRepo.findAllById(ids);

		List<ProcessInstanceResponse> processInstanceResponseList = new ArrayList<ProcessInstanceResponse>();
		processInstanceList.stream().forEach(processInstance -> {
			ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
			processInstanceResponse.setProcessInstanceId(processInstance.getId());
			processInstanceResponse.setStatus(processInstance.getStatusId().getDescription());

			Optional<ProcessVersion> verObj = processVersionList.stream()
					.filter(list -> list.getId() == processInstance.getVersionId().getId()).findFirst();

			processInstanceResponse.setProcessName(verObj.get().getProcessId().getProcessName());
			processInstanceResponse.setVersion(verObj.get().getVersionNumber());
			processInstanceResponse.setVersionId(verObj.get().getId());

			processInstanceResponseList.add(processInstanceResponse);
		});
		return processInstanceResponseList;
	}

	@Override
	@Async
	public void resumeWorkflowExecution(ProcessInstanceRequest processInstanceRequest) throws Exception {

		processComplete.updateProcessStatus(processInstanceRequest.getProcessInstanceId(),
				processInstanceRequest.getUserName(), ProcessStatusConstant.RUNNING.getCode());

		List<Object[]> nextNodeInfo = null;
		do {
			// FIXME: STATUS IS HARD CODED IN QUERY PLEASE CHECK. WE SHOULD PASS IT FROM
			// HERE FROM CONSTANT FILE.
			nextNodeInfo = processNodeInstanceInfoRepository
					.findByProcessInstanceIdAndStatusId(processInstanceRequest.getProcessInstanceId());
			if (!CollectionUtils.isEmpty(nextNodeInfo)) {
				executeNextNodeBasedOnNodeType(nextNodeInfo.get(0), processInstanceRequest);
			}
		} while (!CollectionUtils.isEmpty(nextNodeInfo));
	}

//GxCapture
	public void resumeWorkflowExecutionForWorkflow(WorkflowRequest workflowRequest) throws Exception {

		processComplete.updateProcessStatus(workflowRequest.getProcessInstanceId(), workflowRequest.getUserName(),
				ProcessStatusConstant.RUNNING.getCode());

		List<Object[]> nextNodeInfo = null;
		do {
			// FIXME: STATUS IS HARD CODED IN QUERY PLEASE CHECK. WE SHOULD PASS IT FROM
			// HERE FROM CONSTANT FILE.
			nextNodeInfo = processNodeInstanceInfoRepository
					.findByProcessInstanceIdAndStatusId(workflowRequest.getProcessInstanceId());
			if (nextNodeInfo.size() > 1) {
				Integer processinstancenodeinfoid = processNodeInstanceInfoRepository
						.checkLatestRecord(workflowRequest.getProcessInstanceId());
				if (processinstancenodeinfoid != null)
					nextNodeInfo = nextNodeInfo.stream().filter(each -> (int) each[0] == processinstancenodeinfoid)
							.collect(Collectors.toList());
			}
			if (!CollectionUtils.isEmpty(nextNodeInfo)) {
				if (!executeNextNodeBasedOnNodeTypeForWorkflow(nextNodeInfo.get(0), workflowRequest)) {
					break;
				}
			}
		} while (!CollectionUtils.isEmpty(nextNodeInfo));

		ProcessInstanceNodeInfo processInstanceNodeInfo = processInstanceNodeInfoRepository
				.getLatestTransaction(workflowRequest.getProcessInstanceId(), 4);
//		String message = "Workflow resumed successfully.";
		if (processInstanceNodeInfo != null) {
			NodeStatusColor nodeStatusColor = null;
//	        if(processInstanceNodeInfo.getStatusId().getId() == 5) {
//	        	nodeStatusColor = nodeStatusRepository.findByProcessNodeInfoAndStatusId(processInstanceNodeInfo.getProcessNodeInfoId(),6);
//	        }
//	        if(processInstanceNodeInfo.getStatusId().getId() == 4) {
			nodeStatusColor = nodeStatusRepository
					.findByProcessNodeInfoAndStatusId(processInstanceNodeInfo.getProcessNodeInfoId(), 4);
//	        }

			workflowRequest.setStatus(nodeStatusColor.getStatusName());
		}
	}


	public void continueWorkflowProcessForWorkflow(WorkflowRequest workflowRequest) throws Exception {
		processComplete.updateProcessStatus(workflowRequest.getProcessInstanceId(), workflowRequest.getUserName(), ProcessStatusConstant.RUNNING.getCode());
		List<Object[]> nextNodeInfo = null;
		do {
			// FIXME: STATUS IS HARD CODED IN QUERY PLEASE CHECK. WE SHOULD PASS IT FROM
			// HERE FROM CONSTANT FILE.
			nextNodeInfo = processNodeInstanceInfoRepository
					.findByProcessInstanceIdAndStatusId(workflowRequest.getProcessInstanceId());
			if (!CollectionUtils.isEmpty(nextNodeInfo)) {
//				executeNextNodeBasedOnNodeType(nextNodeInfo.get(0), processInstanceRequest);
			}
		} while (!CollectionUtils.isEmpty(nextNodeInfo));

	}

	private boolean executeNextNodeBasedOnNodeTypeForWorkflow(Object[] nextNodeInfo, WorkflowRequest workflowRequest)
			throws Exception {

		ProcessInstanceNodeInfo processInstanceNodeInfo = new ProcessInstanceNodeInfo();
		int processInstanceId = workflowRequest.getProcessInstanceId();
//		processComplete.updateToRunning((int) nextNodeInfo[0], processInstanceId, processInstanceRequest.getUserName());
		String createdBy = workflowRequest.getUserName();
		switch ((String) nextNodeInfo[1]) {
//		case Constants.SCRIPT_TASK:
//			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
//					createdBy);
//			populateVariableValues(processInstanceRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
//			processScriptTask.executeScriptTask((int) nextNodeInfo[0], processInstanceId, createdBy,
//					 processInstanceNodeInfo,processInstanceRequest);
//			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
//			logger.info("Script task execution completed");
//			break;

//		case Constants.SERVICE_TASK:
//			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
//					createdBy);
//			populateVariableValues(processInstanceRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
//			processServiceTask.ExecuteServiceTask((int) nextNodeInfo[0], processInstanceId, createdBy,processInstanceNodeInfo.getId(),processInstanceRequest);
//			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
//			logger.info("Service task execution completed");
//			break;
		case Constants.SEND_TASK:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
//			populateVariableValues(processInstanceRequest,processInstanceNodeInfo,(int) nextNodeInfo[0]);
			processSendTask.ExecuteSendTaskForWorkflow((int) nextNodeInfo[0], createdBy, workflowRequest);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			logger.info("Send task execution completed");
			break;

		case Constants.USER_TASK:
			processInstanceNodeInfo = processTransactionService.preTransactionForWorkflow(processInstanceId,
					(int) nextNodeInfo[0], createdBy, workflowRequest);
			boolean val = processUserTask.ExecuteUserTaskForWorkflow((int) nextNodeInfo[0], workflowRequest,
					processInstanceNodeInfo);
			if (!val) {
				return false;
			}
			processTransactionService.postTransactionForWorkflow(processInstanceId, (int) nextNodeInfo[0], createdBy);
			logger.info("User task execution completed");
			break;

		case Constants.EXCLUSIVE_GATEWAY:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
			populateVariableValuesForWorkflow(workflowRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			boolean val1 = ExecuteExclusiveGatewayForWorkflow((int) nextNodeInfo[0], workflowRequest);
			if (!val1) {
				return false;
			}
			logger.info("Gateway task execution completed");
			break;
//
		case Constants.END_EVENT:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
			populateVariableValuesForWorkflow(workflowRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
			processEndEvent.ExecuteEndTask((int) nextNodeInfo[0], processInstanceId, createdBy);
			processTransactionService.postTransactionForWorkflowEnd(processInstanceId, (int) nextNodeInfo[0], createdBy,
					workflowRequest);
			processComplete.updateToComplete(processInstanceId, processInstanceId, createdBy);
			logger.info("end task execution completed");
			break;

		default:
			logger.info("unknown task execution completed");
			break;
		}
		return true;

	}

	@Override
	public void executeNextNodeBasedOnNodeType(Object[] nextNodeInfo, ProcessInstanceRequest processInstanceRequest)
			throws Exception {
		ProcessInstanceNodeInfo processInstanceNodeInfo = new ProcessInstanceNodeInfo();
		int processInstanceId = processInstanceRequest.getProcessInstanceId();
//		processComplete.updateToRunning((int) nextNodeInfo[0], processInstanceId, processInstanceRequest.getUserName());
		String createdBy = processInstanceRequest.getUserName();
		switch ((String) nextNodeInfo[1]) {
		case Constants.SCRIPT_TASK:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
			populateVariableValues(processInstanceRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
			processScriptTask.executeScriptTask((int) nextNodeInfo[0], processInstanceId, createdBy,
					processInstanceNodeInfo, processInstanceRequest);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			logger.info("Script task execution completed");
			break;

		case Constants.SERVICE_TASK:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
			populateVariableValues(processInstanceRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
			processServiceTask.ExecuteServiceTask((int) nextNodeInfo[0], processInstanceId, createdBy,
					processInstanceNodeInfo.getId(), processInstanceRequest);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			logger.info("Service task execution completed");
			break;
		case Constants.SEND_TASK:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
//			populateVariableValues(processInstanceRequest,processInstanceNodeInfo,(int) nextNodeInfo[0]);
			processSendTask.ExecuteSendTask((int) nextNodeInfo[0], createdBy, processInstanceRequest);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			logger.info("Send task execution completed");
			break;

		case Constants.USER_TASK:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
			processUserTask.ExecuteUserTask((int) nextNodeInfo[0], processInstanceRequest, processInstanceNodeInfo);
//			populateVariableValues(processInstanceRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			logger.info("User task execution completed");
			break;

		case Constants.EXCLUSIVE_GATEWAY:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
			populateVariableValues(processInstanceRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			ExecuteExclusiveGateway((int) nextNodeInfo[0], processInstanceRequest);
			logger.info("Gateway task execution completed");
			break;

		case Constants.END_EVENT:
			processInstanceNodeInfo = processTransactionService.preTransaction(processInstanceId, (int) nextNodeInfo[0],
					createdBy);
			populateVariableValues(processInstanceRequest, processInstanceNodeInfo, (int) nextNodeInfo[0]);
			processEndEvent.ExecuteEndTask((int) nextNodeInfo[0], processInstanceId, createdBy);
			processTransactionService.postTransaction(processInstanceId, (int) nextNodeInfo[0], createdBy);
			processComplete.updateToComplete(processInstanceId, processInstanceId, createdBy);
			logger.info("end task execution completed");
			break;

		default:
			logger.info("unknown task execution completed");
			break;
		}
	}

	public void populateVariableValuesForWorkflow(WorkflowRequest workflowRequest,
			ProcessInstanceNodeInfo processInstanceNodeInfo, int nextNodeInfo) throws Exception {

//		ProcessInstance processInstance = processInstanceRepository
//				.findById(processInstanceRequest.getProcessInstanceId()).get();
		ProcessNodeInfo processNodeInfo = processNodeInfoServiceImpl.findById(nextNodeInfo);

		// FIXME: USE CACHE
		List<ProcessVariables> processVariables = processVariablesService
				.findProcessVariablesByProcessNodeInfo(processNodeInfo);
//		List<ProcessVariables> processVariables = processVariableRepository.findByProcessNodeInfoId(processNodeInfo);

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
							String glovalValue = processVariable.getValue().replace("{", "").replace("}", "");
							String value = processVariableDetailsRepository
									.getValueforProcessVariable(processNodeInfo.getId(), glovalValue);
							processVariablesDetails.setValue(value != null ? value : processVariable.getValue());
						} else {
							processVariablesDetails.setValue(processVariable.getValue());
						}
					}
				}
				// processVariablesDetails.setProcessInstanceId(processInstance);
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

	public void populateVariableValues(ProcessInstanceRequest processInstanceRequest,
			ProcessInstanceNodeInfo processInstanceNodeInfo, int nextNodeInfo) throws Exception {

//		ProcessInstance processInstance = processInstanceRepository
//				.findById(processInstanceRequest.getProcessInstanceId()).get();
		ProcessNodeInfo processNodeInfo = processNodeInfoServiceImpl.findById(nextNodeInfo);

		// FIXME: USE CACHE
		List<ProcessVariables> processVariables = processVariablesService
				.findProcessVariablesByProcessNodeInfo(processNodeInfo);
//		List<ProcessVariables> processVariables = processVariableRepository.findByProcessNodeInfoId(processNodeInfo);

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
							String glovalValue = processVariable.getValue().replace("{", "").replace("}", "");
							String value = processVariableDetailsRepository
									.getValueforProcessVariable(processNodeInfo.getId(), glovalValue);
							processVariablesDetails.setValue(value != null ? value : processVariable.getValue());
						} else {
							processVariablesDetails.setValue(processVariable.getValue());
						}
					}
				}
				if (processInstanceNodeInfo.getProcessNodeInfoId().getNodeTypeId().getName().equalsIgnoreCase(Constants.SERVICE_TASK)
						&& processVariable.getName().equalsIgnoreCase("requestBody")) {
					String requestBody = frameRequestBody(processInstanceRequest, processVariable);
					if (requestBody != null) {
						processVariablesDetails.setValue(requestBody);
					}
				}
				// processVariablesDetails.setProcessInstanceId(processInstance);
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

	private String frameRequestBody(ProcessInstanceRequest processInstanceRequest, ProcessVariables processVariable) {
		StringBuilder modifiedRequestBody = new StringBuilder();
		if (processVariable.getName().equalsIgnoreCase("requestBody")) {
			processInstanceRequest.getDynamicAttributes().forEach((key, value) -> {
				String modifiedKey = "{" + key + "}";
				if (processVariable.getValue().contains("{" + key + "}")) {
					modifiedRequestBody.append(processVariable.getValue().replace(modifiedKey, value.toString()));

				}
			});
		}
		return modifiedRequestBody.toString();

	}

	@Override
	@Transactional
	public void executeStartNode(ProcessInstanceRequest processInstanceRequest) throws Exception {

		// Step:Extract the Start node Info based on nodeType
		// FIXME: REMOVE HARD CODED 2

		ProcessVersion processVersion = processVersionRepo.findByProcessNameAndVersionNumber(
				processInstanceRequest.getProcessName(), processInstanceRequest.getVersionNumber());
		if (processVersion == null) {
			throw new WorkflowException("Process not found.");
		}

		ProcessNodeInfo startNodeInfo = processNodeInfoServiceImpl.findByVersionIdAndNodeTypeId(processVersion.getId(),
				2);
		saveProcessInstanceNodeInfoAndVariableDetails(processInstanceRequest, startNodeInfo);
	}

	@Override
	// Not cacheable
	public String getCurrentProcessInstanceStatus(int processInstanceId) throws Exception {
		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
		return processInstance.getStatusId().getCode();

	}

	private void saveProcessInstanceNodeInfoAndVariableDetails(ProcessInstanceRequest processInstanceRequest,
			ProcessNodeInfo processNodeInfo) throws Exception {
		// FIXME: USE CACHE
		List<ProcessVariables> processVariables = processVariablesService
				.findProcessVariablesByProcessNodeInfo(processNodeInfo);
//		List<ProcessVariables> processVariables = processVariableRepository.findByProcessNodeInfoId(processNodeInfo);

		List<ProcessVariablesDetails> processVariablesDetailsList = new ArrayList<ProcessVariablesDetails>();
		Timestamp time = TimestampUtil.getCurrentTimestamp();
		String createdBy = processInstanceRequest.getUserName();

		ProcessInstanceNodeInfo processNodeInstanceInfo = saveProcessInstanceNodeInfo(
				processInstanceRequest.getProcessInstanceId(), processNodeInfo, time, createdBy);

		saveProcessVariables(processInstanceRequest, processVariables, processVariablesDetailsList, time, createdBy,
				processNodeInstanceInfo);
	}

	private void saveProcessVariables(ProcessInstanceRequest processInstanceRequest,
			List<ProcessVariables> processVariables, List<ProcessVariablesDetails> processVariablesDetailsList,
			Timestamp time, String createdBy, ProcessInstanceNodeInfo processNodeInstanceInfo) {
		// Step: extract all process variables dynamic values from the input request
		// object.
		if (!CollectionUtils.isEmpty(processVariables)) {
			processVariables.stream().forEach(processVariable -> {
				ProcessVariablesDetails processVariablesDetails = new ProcessVariablesDetails();
				Object obj = processInstanceRequest.getDynamicAttributes()
						.get(processVariable.getValue().replace("{", "").replace("}", ""));
				if (null != obj) {
					processVariablesDetails.setValue(obj.toString());
				} else {
					processVariablesDetails.setValue(processVariable.getValue());
				}
				processVariablesDetails.setProcessInstanceNodeInfoId(processNodeInstanceInfo);
				processVariablesDetails.setProcessVariableId(processVariable);
				processVariablesDetails.setCreatedBy(createdBy);
				processVariablesDetails.setCreatedOn(time);
				processVariablesDetails.setModifiedBy(createdBy);
				processVariablesDetails.setModifiedOn(time);
				processVariablesDetailsList.add(processVariablesDetails);
			});
			processVariableDetailsRepository.saveAll(processVariablesDetailsList);
		}
	}

	private ProcessInstanceNodeInfo saveProcessInstanceNodeInfo(Integer processInstanceId,
			ProcessNodeInfo processNodeInfo, Timestamp time, String createdBy) throws Exception {
		// Step: Create a process node entry with completed status in transaction table.
		ProcessInstanceNodeInfo processInstanceNodeInfo = new ProcessInstanceNodeInfo();
		processInstanceNodeInfo.setProcessNodeInfoId(processNodeInfo);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setId(processInstanceId);
		processInstanceNodeInfo.setProcessInstanceId(processInstance);
		processInstanceNodeInfo.setCreatedOn(time);
		// processInstanceNodeInfo.setModifiedOn(time);
		processInstanceNodeInfo.setCreatedBy(createdBy);
		// processInstanceNodeInfo.setModifiedBy(createdBy);

		ProcessStatus statusCompleted = processStatusService
				.findProcessStatusByCode(ProcessStatusConstant.COMPLETED.getCode());
		processInstanceNodeInfo.setStatusId(statusCompleted);
		ProcessInstanceNodeInfo processNodeInstanceInfo = processNodeInstanceInfoRepository
				.save(processInstanceNodeInfo);
		return processNodeInstanceInfo;
	}

	public void ExecuteExclusiveGateway(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest)
			throws Exception {

		try {
			//Can we cache??, Beacuse in each call the data that must be fetched will change
			List<Object[]> collectChildNodes = processNodeInstanceInfoRepository
					.findByProcessInstanceIdAndStatusId(processInstanceRequest.getProcessInstanceId());
			if (!CollectionUtils.isEmpty(collectChildNodes)) {
				for (Object[] child : collectChildNodes) {
					String sequenceFlowCondition = processVariableRepository
							.getExpressionValueforSequence((int) child[2]);
					executeBasedonCondition(sequenceFlowCondition, child, processInstanceRequest, processNodeInfoId);
				}
			}
		} catch (Exception e) {
			logFailedExclusiveGatewayTask(processNodeInfoId, processInstanceRequest);
			throw e;
		}
	}

	// GxCapture
	public boolean ExecuteExclusiveGatewayForWorkflow(int processNodeInfoId, WorkflowRequest workflowRequest)
			throws Exception {

		try {
			List<Object[]> collectChildNodes = processNodeInstanceInfoRepository
					.findByProcessInstanceIdAndStatusId(workflowRequest.getProcessInstanceId());
			if (!CollectionUtils.isEmpty(collectChildNodes)) {
				for (Object[] child : collectChildNodes) {
					String sequenceFlowCondition = processVariableRepository
							.getExpressionValueforSequence((int) child[2]);
					if (!executeBasedonConditionForWorkflow(sequenceFlowCondition, child, workflowRequest,
							processNodeInfoId)) {
						return false;
					}
				}
			}
		} catch (Exception e) {
//			logFailedExclusiveGatewayTask(processNodeInfoId, workflowRequest);
			throw e;
		}
		return true;
	}

	private boolean executeBasedonConditionForWorkflow(String sequenceFlowCondition, Object[] child,
			WorkflowRequest workflowRequest, int processNodeInfoId) throws Exception {

		char operator = '\0';
		int index = -1;

		if (sequenceFlowCondition.contains("=")) {
			operator = '=';
			index = sequenceFlowCondition.indexOf("=");
		} else if (sequenceFlowCondition.contains(">")) {
			operator = '>';
			index = sequenceFlowCondition.indexOf(">");
		} else if (sequenceFlowCondition.contains("<")) {
			operator = '<';
			index = sequenceFlowCondition.indexOf("<");
		}

		if (index != -1) {
			String variableToFind = sequenceFlowCondition.substring(0, index).trim() + "}";
			String[] valueAssigned1 = StringUtils.split(sequenceFlowCondition, String.valueOf(operator)); // {amount
																											// 100} //
																											// '>=',
																											// '<='
			String valueAssigned = valueAssigned1[1].replace("}", "");
			Map<String, Object> dynamicAttributes = workflowRequest.getDynamicAttributes();

			String actualValue = null;

			if (variableToFind.matches("\\{([^}]*)\\}")) {
				if (org.apache.commons.lang3.StringUtils
						.equalsAnyIgnoreCase(variableToFind.replace("{", "").replace("}", ""), "action")) {
					actualValue = workflowRequest.getAction();
				} else if (null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					String dynamicActualValue = getDynamicValue(dynamicAttributes, variableToFind);
					if (null != dynamicActualValue && !dynamicActualValue.isEmpty()) {
						actualValue = dynamicActualValue;
					} else {
						String dyActual = getProcessVariableValueForGivenValue(variableToFind,
								workflowRequest.getProcessInstanceId());
						actualValue = dyActual;
					}
				} else {
					String dyActual = getProcessVariableValueForGivenValue(variableToFind,
							workflowRequest.getProcessInstanceId());
					actualValue = dyActual;
				}
			}

//			String actualValue = processVariableDetailsRepository.getValueforProcessVariableByProcessVariableName(processInstanceRequest.getProcessInstanceId(),
//					variableToFind);

			switch (operator) {
			case '=':
				if (valueAssigned.equalsIgnoreCase(actualValue)) {
					if (!executeNextNodeBasedOnNodeTypeForWorkflow(child, workflowRequest)) {
						return false;
					}
				}
				break;
			case '>':
				if (Double.parseDouble(actualValue) >= Double.parseDouble(valueAssigned)) {
					executeNextNodeBasedOnNodeTypeForWorkflow(child, workflowRequest);
				}
				break;
			case '<':
				if (Double.parseDouble(actualValue) <= Double.parseDouble(valueAssigned)) {
					executeNextNodeBasedOnNodeTypeForWorkflow(child, workflowRequest);
				}
				break;
			default:
				break;
			}
		}
		return true;

	}

	private void executeBasedonCondition(String sequenceFlowCondition, Object[] child,
			ProcessInstanceRequest processInstanceRequest, int processNodeInfoId) throws Exception {

		char operator = '\0';
		int index = -1;

		if (sequenceFlowCondition.contains("=")) {
			operator = '=';
			index = sequenceFlowCondition.indexOf("=");
		} else if (sequenceFlowCondition.contains(">")) {
			operator = '>';
			index = sequenceFlowCondition.indexOf(">");
		} else if (sequenceFlowCondition.contains("<")) {
			operator = '<';
			index = sequenceFlowCondition.indexOf("<");
		}

		if (index != -1) {
			String variableToFind = sequenceFlowCondition.substring(0, index).trim() + "}";
			String[] valueAssigned1 = StringUtils.split(sequenceFlowCondition, String.valueOf(operator)); // {amount
																											// 100} //
																											// '>=',
																											// '<='
			String valueAssigned = valueAssigned1[1].replace("}", "");
			Map<String, Object> dynamicAttributes = processInstanceRequest.getDynamicAttributes();

			String actualValue = null;

			if (variableToFind.matches("\\{([^}]*)\\}")) {
				if (null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					String dynamicActualValue = getDynamicValue(dynamicAttributes, variableToFind);
					if (null != dynamicActualValue && !dynamicActualValue.isEmpty()) {
						actualValue = dynamicActualValue;
					} else {
						String dyActual = getProcessVariableValueForGivenValue(variableToFind,
								processInstanceRequest.getProcessInstanceId());
						actualValue = dyActual;
					}
				} else {
					String dyActual = getProcessVariableValueForGivenValue(variableToFind,
							processInstanceRequest.getProcessInstanceId());
					actualValue = dyActual;
				}
			}

//			String actualValue = processVariableDetailsRepository.getValueforProcessVariableByProcessVariableName(processInstanceRequest.getProcessInstanceId(),
//					variableToFind);

			switch (operator) {
			case '=':
				if (valueAssigned.equalsIgnoreCase(actualValue)) {
					executeNextNodeBasedOnNodeType(child, processInstanceRequest);

				}
				break;
			case '>':
				if (Double.parseDouble(actualValue) >= Double.parseDouble(valueAssigned)) {
					executeNextNodeBasedOnNodeType(child, processInstanceRequest);
				}
				break;
			case '<':
				if (Double.parseDouble(actualValue) <= Double.parseDouble(valueAssigned)) {
					executeNextNodeBasedOnNodeType(child, processInstanceRequest);
				}
				break;
			default:
				break;
			}
		}

	}

	private void logFailedExclusiveGatewayTask(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest) throws Exception {
		processTransactionService.postTransactionFailed(processInstanceRequest.getProcessInstanceId(),
				processNodeInfoId, processInstanceRequest.getUserName(),ProcessStatusConstant.FAILED.getCode());
		processComplete.updateToFailed(processNodeInfoId, processInstanceRequest.getProcessInstanceId(),
				processInstanceRequest.getUserName());
	}

	private String getDynamicValue(Map<String, Object> dynamicAttributes, String value) {
		if (value.matches("\\{([^}]*)\\}")) {
			Object obj = dynamicAttributes.get(value.replace("{", "").replace("}", ""));
			if (null != obj) {
				return obj.toString();
			} else {
				return null;
			}
		} else {
			return value;
		}
	}

	private String getProcessVariableValueForGivenValue(String inputValue, Integer processInstanceId) throws Exception {
		String globalValue = inputValue.replace("{", "").replace("}", "");

		Object  value = processVariableDetailsService.findValueforProcessVariableByProcessVariableName(processInstanceId, globalValue);
		if(null != value) {
			return value.toString();
		} else
			return null;
	}

}
