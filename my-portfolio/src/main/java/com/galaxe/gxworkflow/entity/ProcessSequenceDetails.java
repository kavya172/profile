package com.galaxe.gxworkflow.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;



@Entity
@Table(name = "processsequencedetails")
public class ProcessSequenceDetails {

	private int id;
	private ProcessVersion versionId;
	private ProcessNodeInfo processSequenceId;
	private ProcessNodeInfo sourceNodeId;
	private ProcessNodeInfo targetNodeId;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	

	public ProcessSequenceDetails() {

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
	@JoinColumn(name = "versionId")
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


	@ManyToOne
	@JoinColumn(name = "processsequenceid")
	public ProcessNodeInfo getProcessSequenceId() {
		return processSequenceId;
	}

	public void setProcessSequenceId(ProcessNodeInfo processSequenceId) {
		this.processSequenceId = processSequenceId;
	}

	@ManyToOne
	@JoinColumn(name = "sourcenodeid")
	public ProcessNodeInfo getSourceNodeId() {
		return sourceNodeId;
	}

	public void setSourceNodeId(ProcessNodeInfo sourceNodeId) {
		this.sourceNodeId = sourceNodeId;
	}

	@ManyToOne
	@JoinColumn(name = "targetnodeid")
	public ProcessNodeInfo getTargetNodeId() {
		return targetNodeId;
	}

	public void setTargetNodeId(ProcessNodeInfo targetNodeId) {
		this.targetNodeId = targetNodeId;
	}

}
