package com.galaxe.gxworkflow.services;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.dto.ProcessInstanceResponse;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;

/**
 * The ProcessInstanceService has a declaration of methods to perform process operation.
 * 
 */
public interface ProcessInstanceService {


	ProcessInstanceResponse getProcessInstanceById(int processInstanceId);

	List<ProcessInstanceResponse> getProcessInstances();
	
	void resumeWorkflowExecution(ProcessInstanceRequest processInstanceRequest) throws MessagingException, IOException, ParserConfigurationException, SAXException, Exception;

	 void resumeWorkflowExecutionForWorkflow(WorkflowRequest workflowRequest) throws Exception; 
		
	public Integer initiateProcessInstanceNew(ProcessInstanceRequest processInstanceRequest) throws Exception;
	
	void executeStartNode(ProcessInstanceRequest processInstanceRequest) throws Exception;

	String getCurrentProcessInstanceStatus(int processInstanceId) throws Exception;
	
	void executeNextNodeBasedOnNodeType(Object[] nextNodeInfo, ProcessInstanceRequest processInstanceRequest) throws MessagingException, IOException, ParserConfigurationException, SAXException, Exception;

//	ProcessInstance findByProcess(Process process) throws Exception;

	void continueWorkflowProcess(ProcessInstanceRequest processInstanceRequest)
			throws Exception;

	Boolean validatePreInitiate(ProcessInstanceRequest processInstanceRequest) throws Exception;

	
	public void populateVariableValues(ProcessInstanceRequest processInstanceRequest,
			ProcessInstanceNodeInfo processInstanceNodeInfo, int nextNodeInfo) throws Exception;

	void validatePreInitiateProcess(WorkflowRequest workflowRequest);

	//GxCapture
	Integer initiateProcessInstanceNewWorkflow(WorkflowRequest workflowRequest) throws Exception;

	void continueWorkflowProcessForWorkflow(WorkflowRequest workflowRequest) throws Exception;

	void continueExecutionProcess(WorkflowRequest workflowRequest) throws Exception;


}
