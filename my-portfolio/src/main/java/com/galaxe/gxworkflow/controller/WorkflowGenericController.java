package com.galaxe.gxworkflow.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.dto.WorkflowResponse;
import com.galaxe.gxworkflow.entity.NodeStatusColor;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;
import com.galaxe.gxworkflow.exception.DataNotFoundException;
import com.galaxe.gxworkflow.exception.WorkflowException;
import com.galaxe.gxworkflow.processor.ProcessServiceTask;
import com.galaxe.gxworkflow.repository.NodeStatusRepository;
import com.galaxe.gxworkflow.repository.ProcessInstanceNodeInfoRepository;
import com.galaxe.gxworkflow.services.ProcessInstanceService;
import com.galaxe.gxworkflow.services.impl.ProcessNodeInfoServiceImpl;

@RestController()
@RequestMapping("/workflow")
public class WorkflowGenericController {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowGenericController.class);

	@Autowired
	private ProcessInstanceService processInstanceService;

	@Autowired
	ProcessNodeInfoServiceImpl processNodeInfoServiceImpl;
	

	@Autowired
	private ProcessServiceTask processServiceTask;
	

	
	@PostMapping(value = "/execution")
	private ResponseEntity<?> startOrResumeWorkflow(@RequestBody ProcessInstanceRequest processInstanceRequest) throws Exception {
		if(!ObjectUtils.isEmpty(processInstanceRequest)) {
			if(processInstanceRequest.isNewInstanceFlag() && processInstanceRequest.getProcessInstanceId() == null) {
				return startWorkflow(processInstanceRequest);
			}
			else if(!processInstanceRequest.isNewInstanceFlag() && processInstanceRequest.getProcessInstanceId() != null) {
				return resumeWorkflow(processInstanceRequest);
			}
			else {
				throw new WorkflowException("Flag and Process Instance Id are not related");
			}
		}
		else {		
			 return ResponseEntity.badRequest().body("Invalid request");
		}
		
	}

//	@PostMapping(value = "/execution/start")
	public ResponseEntity<WorkflowResponse> startWorkflow(@RequestBody ProcessInstanceRequest processInstanceRequest) throws Exception {
		try {

			// Step 1: Validate the processId required for the instance creation.

			processInstanceService.validatePreInitiate(processInstanceRequest);

			// Step 2: Create new instance.
			Integer processInstanceId = processInstanceService.initiateProcessInstanceNew(processInstanceRequest);
			
			//Note: ProcessInstanceId is populated after creating the instance.
			processInstanceRequest.setProcessInstanceId(processInstanceId);
			
			//Step 3: Resume the initiation process
			processInstanceService.continueWorkflowProcess(processInstanceRequest);

			String message = "Workflow initiated successfully.";
			logger.info(message);
			WorkflowResponse workflowResponse = new WorkflowResponse();
			workflowResponse.setProcessInstanceId(processInstanceId);
			workflowResponse.setMessage(message);
			
			return new ResponseEntity<WorkflowResponse>(workflowResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
	
	// Gx Capture Workflow 
	@PostMapping(value = "/initiate/start")
	public WorkflowRequest workflowExecution(@RequestBody WorkflowRequest workflowRequest) throws Exception {
		
		processInstanceService.validatePreInitiateProcess(workflowRequest); 
		
		Integer processInstanceId = processInstanceService.initiateProcessInstanceNewWorkflow(workflowRequest);
		workflowRequest.setProcessInstanceId(processInstanceId);

		processInstanceService.continueExecutionProcess(workflowRequest);
//		String message = "Workflow initiated successfully.";
//		workflowRequest.setMessage(message);
		return workflowRequest;
	}
	
	@PostMapping(value = "/initiate/resume")
	public ResponseEntity<?> resumeWorkflowProcess(@RequestBody WorkflowRequest workflowRequest) throws Exception {
		
		try {
		if (null == workflowRequest.getProcessInstanceId()) {
			throw new DataNotFoundException("Workflow process instance Id is required.");
		}
		processInstanceService.validatePreInitiateProcess(workflowRequest); 
		
		String status = processInstanceService.getCurrentProcessInstanceStatus(workflowRequest.getProcessInstanceId());
		if (ProcessStatusConstant.COMPLETED.getCode().equals(status)) {
			throw new WorkflowException("Process already completed.");
		}else if (ProcessStatusConstant.RUNNING.getCode().equals(status)) {
			throw new WorkflowException("Process is already in running state.");
		}else if (ProcessStatusConstant.STARTED.getCode().equals(status)) {
			throw new WorkflowException("Process is already in started state.");
		}else {
			//FAILED OR PENDING STATE
			//We can resume the workflow.
		}
		processInstanceService.resumeWorkflowExecutionForWorkflow(workflowRequest);
//		logger.info(message);
		return new ResponseEntity<>(workflowRequest, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
	
	

//	@PostMapping(value = "/execution/resume")
	public ResponseEntity<?> resumeWorkflow(@RequestBody ProcessInstanceRequest processInstanceRequest) throws Exception {
		try {

			// Step 1: Validate the processInstanceId.
			if (null == processInstanceRequest.getProcessInstanceId()) {
				throw new DataNotFoundException("Workflow process instance Id is required.");
			}
			String status = processInstanceService.getCurrentProcessInstanceStatus(processInstanceRequest.getProcessInstanceId());
			if (ProcessStatusConstant.COMPLETED.getCode().equals(status)) {
				throw new WorkflowException("Process already completed.");
			}else if (ProcessStatusConstant.RUNNING.getCode().equals(status)) {
				throw new WorkflowException("Process is already in running state.");
			}else if (ProcessStatusConstant.STARTED.getCode().equals(status)) {
				throw new WorkflowException("Process is already in started state.");
			}else {
				//FAILED OR PENDING STATE
				//We can resume the workflow.
			}

			processInstanceService.resumeWorkflowExecution( processInstanceRequest);
			
			String message = "Workflow resumed successfully.";
			logger.info(message);
			return new ResponseEntity<>(message, HttpStatus.OK);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	//FIXME: REMOVE AFTER DEVELOPMENT COMPLETED.
	@GetMapping(value = "/test")
	public ResponseEntity<?> test() {
		try {
			processServiceTask.ExecuteServiceTask(0, 374, null, 0, null);

		} catch (Exception e) {
			logger.error("deployProcess:" + e.getMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return null;
	}

}
