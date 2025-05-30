package com.galaxe.gxworkflow.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="nodestatuscolor")
public class NodeStatusColor {
	
	private int id;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	private ProcessNodeInfo processNodeInfo;
	private int statusId;
	private String statusName;
	private String statusColor;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name ="processnodeinfoid")
	public ProcessNodeInfo getProcessNodeInfo() {
		return processNodeInfo;
	}
	public void setProcessNodeInfo(ProcessNodeInfo processNodeInfo) {
		this.processNodeInfo = processNodeInfo;
	}
	
	@Column(name = "statusid")
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Column(name = "statusname")
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	@Column(name = "statuscolor")
	public String getStatusColor() {
		return statusColor;
	}
	public void setStatusColor(String statusColor) {
		this.statusColor = statusColor;
	}

	
}
