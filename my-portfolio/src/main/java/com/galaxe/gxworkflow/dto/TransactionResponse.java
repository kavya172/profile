package com.galaxe.gxworkflow.dto;

/**
 * This class Represents a Data Transfer Object (DTO) with common attributes for
 * tracking transaction response for individual nodes.
 * 
 * nodeId type of int, is a nodeId of the node.
 * 
 * nodeFlowId type of String, is a Id of each node belonging to a process.
 * 
 * actionStatus type of String, is the recent status activity with respective to that node..
 * 
 * notes type of String, is a comments of the node.
 * 
 * nodeName type of String, is the name of the node.
 * 
 */
public class TransactionResponse {

	private int nodeId;
	private String nodeFlowId;
	private String actionStatus;
	private String notes;
	private String nodeName;

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeFlowId() {
		return nodeFlowId;
	}

	public void setNodeFlowId(String nodeFlowId) {
		this.nodeFlowId = nodeFlowId;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
