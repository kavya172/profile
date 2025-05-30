//package com.galaxe.gxworkflow.services;
//
//import java.util.List;
//
//import com.galaxe.gxworkflow.dto.ActionListDTO;
//import com.galaxe.gxworkflow.dto.NodeStatusDTO;
//import com.galaxe.gxworkflow.dto.TransactionInfo;
//import com.galaxe.gxworkflow.dto.TransactionResponse;
//import com.galaxe.gxworkflow.entity.Process;
//import com.galaxe.gxworkflow.entity.ProcessTasks;
//
//
///**
// * The WorkflowService is had a declaration of methods to perform workflow process operation.
// * 
// */
//
//public interface WorkflowService {
//
//	/**
//	 * Initiate/start the process using transactionInfo for a process. 
//	 *
//	 * @param transactionInfo the TransactionInfo object containing the process ID and additional data
//	 * @throws Exception 
//	 */
//	void processInitialize(TransactionInfo tansactionInfo);
//
//	/**
//	 * Get the list of transaction data for the process with the provided workflowId and processId.
//	 *
//	 * @param workflowId type of Integer, is a Id of an Relational entity which is mapped in the {@link ProcessTasks} Entity
//	 * @param processId  type of Integer, is Id of an {@link Process} Entity
//	 * 
//	 * @return List of {@link TransactionResponse}
//	 */
//	List<TransactionResponse> processTransactions(Integer workFlowId,Integer processId);
//	
//	/**
//	 * To create transaction of uesrTask for a action using transactionInfo.
//	 *
//	 * @param transactionInfo type of {@link TransactionInfo}
//	 */
//	public void createUserTaskAction(TransactionInfo transactionInfo) throws Exception;
//	
//	public ActionListDTO getActionListForUi(int workflowId);
//	
//	public NodeStatusDTO getCompletedNodes(int workflowId);
//}
