package com.galaxe.gxworkflow.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.galaxe.gxworkflow.repository.NodeTypeRepository;
import com.galaxe.gxworkflow.repository.ProcessNodeInfoRepository;

/**
 * The ProcessStartEvent class provides operations to be performed on StartEvent of a process. 
 * 
 * @Autowired is used for creating an reference object for class level usage.
 *
 */
@Component
@Deprecated
public class ProcessStartEvent {
	
	@Autowired
	private NodeTypeRepository nodeTypeRepo;
	
	
	@Autowired
	private ProcessNodeInfoRepository processNodeInfoRepo;
	
	
	/**
	 * This Method performs an operation to start transaction for process by using processId and transactionInfo.
	 * 
	 * To find the nodeType, findByNodeTypeName is method of {@link NodeTypeRepository} is used by passing @param NodeTypeName.
	 * 
	 * findNodeIdByProcessIdAndNodeTypeId is method of {@link ProcessNodeInfoRepository} to retrieve the nodeInfo for a particular processId and nodeTypeId.
	 * 
	 * calling the findNodeIdByProcessIdAndNodeTypeId method by providing data as Process , nodeType.
	 * 
	 * To create a task for startEvent, createTask is a method of {@link ProcessTasks} is used, by providing the data as nodeId obtained from nodeInfo, transactionInfo.
	 * 
	 * createTaskAction is a method of {@link ProcessTaskAction}, which is to create taskAction (transactionDetails) for the created task(transaction).
	 * 
	 * Next, to Collect the child nodes for achieving sequential transaction, findByParentNodeId is a method of {@link ProcessNodeDetailsRepository} is used.
	 * 
	 * calling the findByParentNodeId method by providing data as nodeInfo, to collect all the childNodes.
	 * 
	 * executor is a method of {@link ExecutorJob} which is used to execute the sequential transaction for the childNodes.
	 * 
	 * @param processId type of Integer,is a Id of an Relational entity which is mapped in the {@link Process} Entity
	 * @param transactionInfo type of {@link TransactionInfo} object containing the process ID and additional data
	 *  
	 */
//	public void startProcess(int processId, TransactionInfo transactionInfo) {
//		
//		//FIXME: USE CACHE
//		NodeType nodeType = nodeTypeRepo.findByNodeTypeNameIgnoreCase(Constants.START_EVENT);
//		
//		Process process = new Process();
////		process.setProcessId(processId);
//		
//		//FIXME: USE CACHE
//		ProcessNodeInfo nodeInfo = processNodeInfoRepo.findNodeIdByProcessIdAndNodeTypeId(process, nodeType);
//		
//		//FIXME: USE FROM CONSTANT
//		transactionInfo.setStatus("COMPLETED");
////		ProcessTasks taskInfo = processTasks.createTask(nodeInfo.getNodeId(), transactionInfo);
//		
//		//FIXME: USE FROM CONSTANT
//		transactionInfo.setStatus("Initiated");
//		
////		processTaskAction.createTaskAction(taskInfo.getTaskId(), transactionInfo);
//		
//		
////		List<ProcessNodeDetails> childsInfo = processNodeDetailsRepo.findByParentNodeId(nodeInfo); 
//		
//		//FIXME: USE FROM CONSTANT
//		transactionInfo.setStatus("INPROGRESS");
//
////		executorJob.executor(childsInfo, transactionInfo);
//	}
	
}
