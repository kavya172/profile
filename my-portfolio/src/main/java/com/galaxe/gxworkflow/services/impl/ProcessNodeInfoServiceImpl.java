package com.galaxe.gxworkflow.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.exception.WorkflowException;
import com.galaxe.gxworkflow.repository.ProcessInstanceRepository;
import com.galaxe.gxworkflow.repository.ProcessNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessVersionRepository;

/**
 * The ProcessNodeInfoServiceImpl class provides operations for managing Process
 * Node Info Related Information.
 * 
 * This service class handles business logic related to Process Node Info
 * management like getting Process Node Info Details and so on.
 *
 * @Autowired is used for creating an reference object for class level usage.
 * 
 */
@Transactional
@Service
public class ProcessNodeInfoServiceImpl {

	@Autowired
	private ProcessNodeInfoRepository processNodeInfoRepository;

	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findByVersionIdAndNodeTypeId", key = "#versionId.toString().concat('-').concat(#nodeTypeId.toString())")
	public ProcessNodeInfo findByVersionIdAndNodeTypeId(Integer versionId, int nodeTypeId) throws Exception {
		ProcessNodeInfo nodeInfo = processNodeInfoRepository.findByVersionIdAndNodeTypeId(versionId, nodeTypeId).get(0);
		return nodeInfo;
	}
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findProcessNodeInfoById", key = "#processNodeInfoId")
	public ProcessNodeInfo findById(Integer processNodeInfoId) throws Exception {
		ProcessNodeInfo processNodeInfo = processNodeInfoRepository.findById(processNodeInfoId).get();
		return processNodeInfo;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "getUserDetailsByProcessNodeInfoId", key = "#processNodeInfoId")
	public List<Object[]> getUserDetailsByProcessNodeInfoId(int processNodeInfoId) throws Exception {
		List<Object[]> processNodeInfo = processNodeInfoRepository.getUserDetailsByProcessNodeInfoId(processNodeInfoId);
		return processNodeInfo;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "getUserNodeAssignAllDetailsForGivenProcessNodeInfoId", key = "#processNodeInfoId")
	public String getUserNodeAssignAllDetailsForGivenProcessNodeInfoId(int processNodeInfoId) throws Exception {
		String allUsers = processNodeInfoRepository.getUserNodeAssignAllDetailsForGivenProcessNodeInfoId(processNodeInfoId);
		return allUsers;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "getProcesByVersionId", key = "#versionId")
	public List<Object[]> getProcesByVersionId(int versionId) throws Exception {
		List<Object[]> processNodeObjectList = processNodeInfoRepository
				.getProcessByVersionId(versionId);
		return processNodeObjectList;
	}

	//@Cacheable(value = "isProcessExecutable", key = "#processId")
	public boolean isProcessExecutable(ProcessInstanceRequest processInstanceRequest) throws Exception {

//		ProcessInstance processIntance = processInstanceRepo.findById(processIntanceId).orElse(null);
//		if(ObjectUtils.isEmpty(processIntance)) {
//			throw new WorkflowException("Process Instance not found");
//		}
		//FIXME: 6
		Object[] result = processNodeInfoRepository.getProcessInformationForVersionId(processInstanceRequest.getProcessName(), processInstanceRequest.getVersionNumber(), 6);

		if(result != null && result.length > 0) {
			Object[] obj = (Object[])result[0];
			if(!(boolean)obj[0]) {
				throw new WorkflowException("Process is inactive.");
			}

//			if(!(boolean)obj[1]) {
//				throw new WorkflowException("Process is not executable.");
//			}

			if(StringUtils.equalsIgnoreCase(obj[1], "No")) {

				if(StringUtils.contains(obj[2], processInstanceRequest.getUserName())) {
					return true;
				}
					
				if(StringUtils.contains(obj[3], processInstanceRequest.getUserRole())) {
				return true;	
				}
				
				throw new WorkflowException("User is not authorized to start the workflow.");
			}
		}else {
			throw new WorkflowException("No process found to start the workflow.");
		}
		return true;
	}

	public boolean isProcessExecutableForExecution(WorkflowRequest workflowRequest) {

		Object[] result = processNodeInfoRepository
				.getProcessInformationForVersionIdWorkflow(workflowRequest.getVersionId(), 6);

		if (result != null && result.length > 0) {
			Object[] obj = (Object[]) result[0];
			if (!(boolean) obj[0]) {
				throw new WorkflowException("Process is inactive.");
			}

			if (StringUtils.equalsIgnoreCase(obj[1], "No")) {

				if (StringUtils.contains(obj[2], workflowRequest.getUserName())) {
					return true;
				}

				if (StringUtils.contains(obj[3], workflowRequest.getUserRole())) {
					return true;
				}

				throw new WorkflowException("User is not authorized to start the workflow.");
			}

		} else {
			throw new WorkflowException("No process found to start the workflow.");
		}
		return true;
	}
}