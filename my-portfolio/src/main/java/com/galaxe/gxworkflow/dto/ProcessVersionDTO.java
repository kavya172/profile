package com.galaxe.gxworkflow.dto;

import java.sql.Timestamp;

import com.galaxe.gxworkflow.entity.Process;

public class ProcessVersionDTO {
	
	private int versionId;
	private Process processId;
	private String versionName;
//	private Timestamp createdOn;
//	private String createdBy;
	private String modifiedBy;
	private String modifiedOn;
	private int version;
	private Boolean isActive;
	private int isLocked;
	
	public ProcessVersionDTO() {
	}
	
	public int getVersionId() {
		return versionId;
	}
	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}
	public Process getProcessId() {
		return processId;
	}
	public void setProcessId(Process processId) {
		this.processId = processId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
//	public Timestamp getCreatedOn() {
//		return createdOn;
//	}
//	public void setCreatedOn(Timestamp createdOn) {
//		this.createdOn = createdOn;
//	}
//	public String getCreatedBy() {
//		return createdBy;
//	}
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public int getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(int isLocked) {
		this.isLocked = isLocked;
	}

}
