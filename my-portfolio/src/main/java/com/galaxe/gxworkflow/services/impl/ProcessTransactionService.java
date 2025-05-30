package com.galaxe.gxworkflow.services.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.entity.NodeStatusColor;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessStatus;
import com.galaxe.gxworkflow.repository.NodeStatusRepository;
import com.galaxe.gxworkflow.repository.ProcessInstanceNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessInstanceRepository;
import com.galaxe.gxworkflow.repository.ProcessNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessStatusRepository;
import com.galaxe.gxworkflow.services.ProcessStatusService;

@Service
public class ProcessTransactionService {
	
	@Autowired
	private ProcessNodeInfoRepository processNodeInfoRepository;

	@Autowired
	private ProcessInstanceRepository processInstanceRepository;
	
	@Autowired
	private ProcessNodeInfoServiceImpl processNodeInfoServceImpl;
	
	@Autowired
	private ProcessStatusService processStatusService;
	
	@Autowired
	private ProcessStatusRepository processStatusRepository;
	
	@Autowired
	private ProcessInstanceNodeInfoRepository processNodeInstanceInfoRepository;
	
	@Autowired
	private NodeStatusRepository nodeStatusRepository;

	@Transactional(value = TxType.REQUIRES_NEW)
	public ProcessInstanceNodeInfo preTransaction(int processInstanceId,int processNodeInfoId,String userName) throws Exception {
		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
		//FIXME: USE CACHE

//		ProcessNodeInfo scriptNode = processNodeInfoRepository.findById(processNodeInfoId).get();
		ProcessNodeInfo processNodeInfo = processNodeInfoServceImpl.findById(processNodeInfoId);
		
//		set the status as started
		//FIXME: USE CACHE
		ProcessStatus statusStarted = processStatusService.findProcessStatusByCode(ProcessStatusConstant.STARTED.getCode());
		
		return populateAndSave(userName, processInstance, processNodeInfo, statusStarted);
		
	}
	
	// GxCapture
	@Transactional(value = TxType.REQUIRES_NEW)
	public ProcessInstanceNodeInfo preTransactionForWorkflow(int processInstanceId, int processNodeInfoId, String userName, WorkflowRequest workflowRequest) {
		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
		//FIXME: USE CACHE
		ProcessNodeInfo scriptNode = processNodeInfoRepository.findById(processNodeInfoId).get();
//		set the status as started
		//FIXME: USE CACHE
		ProcessStatus statusStarted = processStatusRepository.findByCode(ProcessStatusConstant.STARTED.getCode());
		return populateAndSave(userName, processInstance, scriptNode, statusStarted);
		
		
	}

	private ProcessInstanceNodeInfo populateAndSave(String userName, ProcessInstance processInstance, ProcessNodeInfo scriptNode,
			ProcessStatus statusStarted) {
		//FIXME: USE TIMESTAMPUTIL
		Timestamp time = new Timestamp(new Date().getTime());
		ProcessInstanceNodeInfo processNodeInstanceInfo = new ProcessInstanceNodeInfo();
		processNodeInstanceInfo.setProcessNodeInfoId(scriptNode);
		processNodeInstanceInfo.setProcessInstanceId(processInstance);
		processNodeInstanceInfo.setStatusId(statusStarted);
		processNodeInstanceInfo.setCreatedOn(time);
		processNodeInstanceInfo.setCreatedBy(userName);
		return processNodeInstanceInfoRepository.save(processNodeInstanceInfo);
	}

	@Transactional(value = TxType.REQUIRES_NEW)
    public void postTransaction(int processInstanceId,int processNodeInfoId,String userName) throws Exception {
    	
    	ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
    	//FIXME: USE CACHE
//		ProcessNodeInfo scriptNode = processNodeInfoRepository.findById(processNodeInfoId).get();
		ProcessNodeInfo processNodeInfo = processNodeInfoServceImpl.findById(processNodeInfoId);
		
//    	set the status as Completed
		//FIXME: USE CACHE
		ProcessStatus statusCompleted =  processStatusService.findProcessStatusByCode(ProcessStatusConstant.COMPLETED.getCode());
		populateAndSave(userName, processInstance, processNodeInfo, statusCompleted);
	}
	
	
	// GxCapture
	@Transactional(value = TxType.REQUIRES_NEW)
	public void postTransactionForWorkflow(int processInstanceId, int processNodeInfoId, String userName) {
	
		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
    	//FIXME: USE CACHE
		ProcessNodeInfo scriptNode = processNodeInfoRepository.findById(processNodeInfoId).get();
		ProcessStatus statusCompleted = processStatusRepository.findByCode(ProcessStatusConstant.COMPLETED.getCode());
		populateAndSave(userName, processInstance, scriptNode, statusCompleted);
		
		
		
	}

	
	@Transactional(value = TxType.REQUIRES_NEW)
    public void postTransactionFailed(int processInstanceId,int processNodeInfoId,String userName, String status) throws Exception {
    	
    	ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
    	//FIXME: USE CACHE
//		ProcessNodeInfo processNodeInfo = processNodeInfoRepository.findById(processNodeInfoId).get();
		ProcessNodeInfo processNodeInfo = processNodeInfoServceImpl.findById(processNodeInfoId);
//    	set the status as Completed
		//FIXME: USE CACHE
		ProcessStatus statusFailed = processStatusService.findProcessStatusByCode(status);
		populateAndSave(userName, processInstance, processNodeInfo, statusFailed);
	}
	
	 public void postTransactionPendingForWorkflow(int processInstanceId,int processNodeInfoId,String userName) {
	    	
	    	ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
	    	//FIXME: USE CACHE
			ProcessNodeInfo scriptNode = processNodeInfoRepository.findById(processNodeInfoId).get();
			
//	    	set the status as Completed
			//FIXME: USE CACHE
			ProcessStatus statusPending = processStatusRepository.findByCode(ProcessStatusConstant.PENDING.getCode());
			populateAndSave(userName, processInstance, scriptNode, statusPending);
		}

	public void postTransactionForWorkflowEnd(int processInstanceId, int processNodeInfoId, String createdBy,WorkflowRequest workflowRequest) {
	
		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
    	//FIXME: USE CACHE
		ProcessNodeInfo scriptNode = processNodeInfoRepository.findById(processNodeInfoId).get();
		ProcessStatus statusCompleted = processStatusRepository.findByCode(ProcessStatusConstant.COMPLETED.getCode());
		populateAndSave(workflowRequest.getUserName(), processInstance, scriptNode, statusCompleted);
	}

	
	
 
}
