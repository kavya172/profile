package com.galaxe.gxworkflow.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * This Process class represents a persistent entity of the process.
 *
 * <p>
 * 
 * This entity class is a component that represents a table of  WF_Process
 * 
 * Here we explicitly assigning the table name WF_Process to entity class using @Table annotation.
 * 
 * processId type of Integer, is a unique identifier of the Process
 * 
 * processName type of String, is a name of the Process
 * 
 * bpmnDesign type of String, is a bpmnDesign of the Process
 * 
 * descriptions type of String, is a descriptions related to the Process
 * 
 * isActive type of integer, which holds 0 (inactive) or 1 (active), by default it is 1
 * 
 * createdOn type of Timestamp
 * 
 * createdBy type of String, will holds data creator information
 * 
 * modifiedBy type of String, will holds data modifier information
 * 
 * modifiedOn type of Timestamp
 * 
 * @Getter and @Setter methods to handle Get and Set operations.
 * 
 */

@Entity
@Table(name = "process")
public class Process {

	private int id;
	private String processName;
//	private String description;
//	private Boolean isActive;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	private String allUsers;
	

	public Process() {

	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(name = "processname")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

//	@Column(name = "description")
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String descriptions) {
//		this.description = descriptions;
//	}

//	@Column(name = "isactive")
//	public Boolean getIsActive() {
//		return isActive;
//	}
//
//	public void setIsActive(Boolean isActive) {
//		this.isActive = isActive;
//	}

	@Column(name = "createdon")
	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "createdby")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "modifiedby")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "modifiedon")
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	@Column(name = "allUsers")
	public String getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(String allUsers) {
		this.allUsers = allUsers;
	}
	
	
}
