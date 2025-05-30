package com.galaxe.gxworkflow.dto;

import java.sql.Timestamp;

public class ProcessInstanceResponse {

	private int processInstanceId;
	private String ProcessInstanceName;
	private String processName;
	private int version;
	private int versionId;
	private String status;
	private String modifiedBy;
	private Timestamp modifiedOn;
	
	public int getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(int processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessInstanceName() {
		return ProcessInstanceName;
	}
	public void setProcessInstanceName(String processInstanceName) {
		ProcessInstanceName = processInstanceName;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public int getVersionId() {
		return versionId;
	}
	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	
}
