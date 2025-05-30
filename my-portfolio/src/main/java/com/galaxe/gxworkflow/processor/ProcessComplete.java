package com.galaxe.gxworkflow.processor;

import java.sql.Timestamp;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.entity.ProcessStatus;
import com.galaxe.gxworkflow.repository.ProcessInstanceRepository;
import com.galaxe.gxworkflow.repository.ProcessStatusRepository;
import com.galaxe.gxworkflow.services.ProcessStatusService;
import com.galaxe.gxworkflow.util.TimestampUtil;
/**
 * The ProcessEndEvent class provides operations to be performed on EndEvent of a process. 
 * 
 * @Autowired is used for creating an reference object for class level usage.
 *
 */
@Service
public class ProcessComplete {
	
	@Autowired
	private ProcessInstanceRepository processInstanceRepository;
	
	@Autowired
	private ProcessStatusRepository processStatusRepository;
	
	@Autowired
	private ProcessStatusService processStatusService;
		
	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateToComplete(int processNodeInfoId, int processInstanceId, String createdBy) throws Exception {

		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		
//		set the status as Completed
		//FIXME: USE CACHE
		ProcessStatus statusCompleted = processStatusService.findProcessStatusByCode(ProcessStatusConstant.COMPLETED.getCode());
		processInstance.setStatusId(statusCompleted);
		processInstance.setModifiedBy(createdBy);
		processInstance.setModifiedOn(time);
		processInstanceRepository.save(processInstance);
	}
	
	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateToFailed(int processNodeInfoId, int processInstanceId, String createdBy) throws Exception {

		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		
//		set the status as Completed
		//FIXME: USE CACHE
		ProcessStatus statusFailed = processStatusService.findProcessStatusByCode(ProcessStatusConstant.FAILED.getCode());
		processInstance.setStatusId(statusFailed);
		processInstance.setModifiedBy(createdBy);
		processInstance.setModifiedOn(time);
		processInstanceRepository.save(processInstance);
	}
	
	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateProcessStatus(int processInstanceId, String createdBy, String code) throws Exception {

		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();

		Timestamp time = TimestampUtil.getCurrentTimestamp();
		
//		set the status as Completed
		//FIXME: USE CACHE
		ProcessStatus statusPending =processStatusService.findProcessStatusByCode(code);
		processInstance.setStatusId(statusPending);
		processInstance.setModifiedBy(createdBy);
		processInstance.setModifiedOn(time);
		processInstanceRepository.save(processInstance);
	}
	
//	@Transactional(value = TxType.REQUIRES_NEW)
//	public void updateToRunning(int processInstanceId, String createdBy, String code) {
//
//		ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId).get();
//
//		Timestamp time = TimestampUtil.getCurrentTimestamp();
//		
////		set the status as Completed
//		//FIXME: USE CACHE
//		ProcessStatus statusRunning = processStatusRepository.findByCode(ProcessStatusConstant.RUNNING.getCode());
//		processInstance.setStatusId(statusRunning);
//		processInstance.setModifiedBy(createdBy);
//		processInstance.setModifiedOn(time);
//		processInstanceRepository.save(processInstance);
//	}
	

}
