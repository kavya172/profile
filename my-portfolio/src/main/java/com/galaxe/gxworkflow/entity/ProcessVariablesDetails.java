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
@Table(name = "processvariabledetails")
public class ProcessVariablesDetails {

	private int id;
	private String value;
	private ProcessVariables processVariableId;
	//private ProcessInstance processInstanceId;
	private ProcessInstanceNodeInfo processInstanceNodeInfoId;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	

	public ProcessVariablesDetails() {

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
//	@JoinColumn(name = "processinstanceid")
//	public ProcessInstance getProcessInstanceId() {
//		return processInstanceId;
//	}
//
//	public void setProcessInstanceId(ProcessInstance processInstanceId) {
//		this.processInstanceId = processInstanceId;
//	}

	@ManyToOne
	@JoinColumn(name = "processinstancenodeinfoid")
	public ProcessInstanceNodeInfo getProcessInstanceNodeInfoId() {
		return processInstanceNodeInfoId;
	}

	public void setProcessInstanceNodeInfoId(ProcessInstanceNodeInfo processInstanceNodeInfoId) {
		this.processInstanceNodeInfoId = processInstanceNodeInfoId;
	}

	@ManyToOne
	@JoinColumn(name = "processvariableid")
	public ProcessVariables getProcessVariableId() {
		return processVariableId;
	}

	public void setProcessVariableId(ProcessVariables processVariables) {
		this.processVariableId = processVariables;
	}

	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
