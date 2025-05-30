package com.galaxe.gxworkflow.dto;

import java.sql.Timestamp;

/**
 * 
 * * Represents a generic Data Transfer Object (DTO) with common attributes for
 * tracking creation and modification information.
 *
 * <p>
 * This class is used for transferring Generic data between application and data 
 * layer.
 * 
 * createdBy type of String, will holds data creator information.
 * 
 * createdOn type of Timestamp.
 * 
 * modifiedBy type of String, will holds data modifier information.
 * 
 * modifiedOn type of Timestamp.
 * 
 * @Getter and @Setter methods to handle Get and Set operations.
 */
public class GenericDTO {

	private String createdBy;
	private Timestamp createdOn;
	private String modifiedBy;
	private Timestamp modifiedOn;

	public GenericDTO() {

	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
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
