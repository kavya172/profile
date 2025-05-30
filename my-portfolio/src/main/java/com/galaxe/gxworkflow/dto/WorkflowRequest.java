package com.galaxe.gxworkflow.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class WorkflowRequest {

	private Integer versionId;
	private Integer processInstanceId;
	private String status;
	private String action;
	private String userName;
	private String userRole;
	private String message;
	private Map<String, Object> dynamicAttributes = new LinkedHashMap<>();
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
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
	public Integer getVersionId() {
		return versionId;
	}
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	public Integer getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(Integer processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setDynamicAttributes(Map<String, Object> dynamicAttributes) {
		this.dynamicAttributes = dynamicAttributes;
	}
	
}
