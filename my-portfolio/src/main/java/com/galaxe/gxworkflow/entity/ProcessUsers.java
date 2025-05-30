package com.galaxe.gxworkflow.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "processusers")
public class ProcessUsers {

	private int id;
	private String emailId;
	//private Boolean isActive;
	private ProcessNodeInfo processNodeInfoId;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	

	public ProcessUsers() {

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

	@Column(name = "emailid")
	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

//	@Column(name = "isactive")
//	public Boolean getIsActive() {
//		return isActive;
//	}
//
//
//	public void setIsActive(Boolean isActive) {
//		this.isActive = isActive;
//	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	@Column(name = "createdon")
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}


	public String getCreatedBy() {
		return createdBy;
	}

	@Column(name = "createdby")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}

	@Column(name = "modifiedby")
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	@Column(name = "modifiedon")
	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	@ManyToOne
	@JoinColumn(name = "processNodeInfoId")
	public ProcessNodeInfo getProcessNodeInfoId() {
		return processNodeInfoId;
	}


	public void setProcessNodeInfoId(ProcessNodeInfo processNodeInfoId) {
		this.processNodeInfoId = processNodeInfoId;
	}
	
}
