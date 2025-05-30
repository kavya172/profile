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

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;



@Entity
@Table(name = "processinstance")
public class ProcessInstance {

	private int id;
	private ProcessVersion versionId;
	private ProcessStatus statusId;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;

	public ProcessInstance() {

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

	@ManyToOne
	@JoinColumn(name = "versionid")
	public ProcessVersion getVersionId() {
		return versionId;
	}

	public void setVersionId(ProcessVersion versionId) {
		this.versionId = versionId;
	}
	
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

	@ManyToOne
	@JoinColumn(name = "statusid")
	public ProcessStatus getStatusId() {
		return statusId;
	}

	public void setStatusId(ProcessStatus statusId) {
		this.statusId = statusId;
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
	
}
