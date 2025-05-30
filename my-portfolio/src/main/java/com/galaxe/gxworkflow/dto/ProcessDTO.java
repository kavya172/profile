package com.galaxe.gxworkflow.dto;

import java.util.ArrayList;
import java.util.List;

import com.galaxe.gxworkflow.entity.ProcessVersion;

/**
 * 
 * This class represents a Data Transfer Object for a Process.
 *
 * <p>
 * This class is used for transferring Process data between application and data
 * layer.
 * 
 * This class extends with GenericDTO to inherit additional functionality.
 * 
 * processId type of int, is a unique identifier of the ProcessDTO.
 * 
 * processName type of String, is a name of the Process.
 * 
 * bpmnDesign type of String, is a BPMN Design of the Process.
 * 
 * descriptions type of String, is a note of the Process.
 * 
 * isActive type of int, is a active Status of the ProcessDTO.
 *
 *@Getter and @Setter methods to handle Get and Set operations.
 */
public class ProcessDTO {

	private int id;
	private String processName;
	private Boolean isActive;
	private String modifiedBy;
	private String modifiedOn;
//	private int isLocked;
	private int version;
	private int processVersionId;
	private String versionName;
	private List<ProcessVersionDTO> childs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

//	public int getIsLocked() {
//		return isLocked;
//	}
//
//	public void setIsLocked(int isLocked) {
//		this.isLocked = isLocked;
//	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getProcessVersionId() {
		return processVersionId;
	}

	public void setProcessVersionId(int processVersionId) {
		this.processVersionId = processVersionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public List<ProcessVersionDTO> getChilds() {
		return childs;
	}

	public void setChilds(List<ProcessVersionDTO> childs) {
		this.childs = childs;
	}

}
