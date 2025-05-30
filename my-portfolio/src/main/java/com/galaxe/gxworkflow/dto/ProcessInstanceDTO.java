package com.galaxe.gxworkflow.dto;

import java.sql.Timestamp;

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
public class ProcessInstanceDTO {

	private int id;
	private int processId;
	private String processName;
	private int versionId;
	private int isActive;
	private String modifiedBy;
	private Timestamp modifiedOn;

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

	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
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

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

}
