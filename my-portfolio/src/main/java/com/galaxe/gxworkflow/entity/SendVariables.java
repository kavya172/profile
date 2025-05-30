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
@Table(name = "sendvariables")
public class SendVariables {

	private int id;
	//private ProcessVersion versionId;
	private ProcessNodeInfo processNodeInfoId;
	private String subject;
	private String body;
	private String bodyType;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	

	public SendVariables() {

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
	
	
	@ManyToOne
	@JoinColumn(name = "processnodeinfoid")
	public ProcessNodeInfo getProcessNodeInfoId() {
		return processNodeInfoId;
	}

	public void setProcessNodeInfoId(ProcessNodeInfo processNodeInfoId) {
		this.processNodeInfoId = processNodeInfoId;
	}
	
	@Column(name = "subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "body")
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Column(name = "bodytype")
	public String getBodyType() {
		return bodyType;
	}

	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
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

	
}
