package com.galaxe.gxworkflow.dto;

/**
 * This class Represents a Data Transfer Object (DTO) with common attributes for
 * updating nodeStatus for individual nodes based on their procesId and
 * WorkflowId.
 * 
 * name type of String, is the name of the node.
 * 
 * processId type of int, is a processId of the process.
 * 
 * workFlowId type of int, is a workFlowId of the TransactionInfo.
 * 
 * notes type of String, is comments for that name.
 * 
 * status type of String, is a status of the node.
 * 
 * transUser type of String, will holds transaction creator information.
 */
public class TransactionInfo {

	private String name;
	private int processId;
	private int workFlowId;
	private String notes;
	private String status;
	private String transUser;
	private String originalStatus;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(int workFlowId) {
		this.workFlowId = workFlowId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransUser() {
		return transUser;
	}

	public void setTransUser(String transUser) {
		this.transUser = transUser;
	}

	public String getOriginalStatus() {
		return originalStatus;
	}

	public void setOriginalStatus(String originalStatus) {
		this.originalStatus = originalStatus;
	}

}
