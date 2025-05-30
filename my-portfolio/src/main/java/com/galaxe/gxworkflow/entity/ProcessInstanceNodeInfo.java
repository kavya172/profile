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
@Table(name = "processinstancenodeinfo")
public class ProcessInstanceNodeInfo {

	private int id;
	private ProcessNodeInfo processNodeInfoId;
	private ProcessInstance processInstanceId;
	//private ProcessVersion versionId;
	private ProcessStatus statusId;
	private Timestamp createdOn;
	private String createdBy;

	public ProcessInstanceNodeInfo() {

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
	@JoinColumn(name = "processnodeinfoid")
	public ProcessNodeInfo getProcessNodeInfoId() {
		return processNodeInfoId;
	}

	public void setProcessNodeInfoId(ProcessNodeInfo processNodeInfoId) {
		this.processNodeInfoId = processNodeInfoId;
	}
	@ManyToOne
	@JoinColumn(name = "processinstanceid")
	public ProcessInstance getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(ProcessInstance processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
//
//	@ManyToOne
//	@JoinColumn(name = "versionid")
//	public ProcessVersion getVersionId() {
//		return versionId;
//	}
//
//	public void setVersionId(ProcessVersion versionId) {
//		this.versionId = versionId;
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

	@ManyToOne
	@JoinColumn(name = "statusid")
	public ProcessStatus getStatusId() {
		return statusId;
	}

	public void setStatusId(ProcessStatus statusId) {
		this.statusId = statusId;
	}

}
