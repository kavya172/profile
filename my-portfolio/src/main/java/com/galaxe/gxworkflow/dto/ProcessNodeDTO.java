package com.galaxe.gxworkflow.dto;

public class ProcessNodeDTO {

	private String userName;
	private String userRole;
	private int processInstanceId;

	public ProcessNodeDTO() {

	}

	public ProcessNodeDTO(String userName, String userRole, int processInstanceId) {
		super();
		this.userName = userName;
		this.userRole = userRole;
		this.processInstanceId = processInstanceId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public int getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(int processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
