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
@Table(name = "processvariables")
public class ProcessVariables {

	private int id;
	private String name;
	private String value;
	//private ProcessVersion versionId;
	private ProcessNodeInfo processNodeInfoId;
	private VariableType variableTypeId;
	private Boolean isGlobal;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	

	public ProcessVariables() {

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

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name = "variabletypeid")	
	public VariableType getVariableTypeId() {
		return variableTypeId;
	}

	public void setVariableTypeId(VariableType variableTypeId) {
		this.variableTypeId = variableTypeId;
	}

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

	public Boolean getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(Boolean isGlobal) {
		this.isGlobal = isGlobal;
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
