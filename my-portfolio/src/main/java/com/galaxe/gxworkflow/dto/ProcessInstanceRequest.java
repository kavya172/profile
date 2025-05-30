package com.galaxe.gxworkflow.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ProcessInstanceRequest {

	private String processName;
	private Integer versionNumber;
	//private int processNodeInfoId;
	private boolean newInstanceFlag;
	private String targetAction;
	private Integer processInstanceId;
	private String userName;
	private String userRole;
	private String possibleActions;
	//private String processName;
	private Map<String, Object> dynamicAttributes = new LinkedHashMap<>();
	
	public Integer getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(Integer processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
//	public int getProcessNodeInfoId() {
//		return processNodeInfoId;
//	}
//	public void setProcessNodeInfoId(int processNodeInfoId) {
//		this.processNodeInfoId = processNodeInfoId;
//	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
//	public String getProcessName() {
//		return processName;
//	}
//	public void setProcessName(String processName) {
//		this.processName = processName;
//	}
	
	@JsonAnySetter
    public void setDynamicAttribute(String key, Object value) {
        dynamicAttributes.put(key, value);
    }

    public Map<String, Object> getDynamicAttributes() {
        return dynamicAttributes;
    }
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public Integer getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public boolean isNewInstanceFlag() {
		return newInstanceFlag;
	}
	public void setNewInstanceFlag(boolean newInstanceFlag) {
		this.newInstanceFlag = newInstanceFlag;
	}
	public String getTargetAction() {
		return targetAction;
	}
	public void setTargetAction(String targetAction) {
		this.targetAction = targetAction;
	}
	public String getPossibleActions() {
		return possibleActions;
	}
	public void setPossibleActions(String possibleActions) {
		this.possibleActions = possibleActions;
	}
	
}
