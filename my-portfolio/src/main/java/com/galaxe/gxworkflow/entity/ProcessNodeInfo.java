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


/**
 * 
 * This ProcessNodeInfo class represents a persistent entity of the workflow.
 *
 * <p>
 * This entity class is a component that represents a table of  WF_ProcessNodeInfo
 * 
 * Here we explicitly assigning the table name WF_ProcessNodeInfo to entity class using @Table annotation.
 * 
 * nodeId type of Integer,is a unique identifier of the ProcessNodeInfo
 * 
 * nodeName type of String, is a name of the node in ProcessNodeInfo
 * 
 * nodeTypeId type of NodeType, (Many-to-One mapping) The join column "nodeTypeId" is used to establish a relationship between the @ProcessNodeInfo and @NodeType.
 * 
 * processId type of Process, (Many-to-One mapping) The join column "processId" is used to establish a relationship between the @ProcessNodeInfo and @Process.
 * 
 * nodeFlowId type of String, is a nodeFlowId of the ProcessNodeInfo
 * 
 * createdOn type of Timestamp
 * 
 * createdBy type of String, will holds data creator information
 * 
 * modifiedBy type of String, will holds data modifier information
 * 
 * @Getter and @Setter methods to handle Get and Set operations.
 * 
 * modifiedOn type of Timestamp
 * 
 *  
 * 
 */
@Entity
@Table(name = "processnodeinfo")
public class ProcessNodeInfo {

	private int id;
	private NodeType nodeTypeId;
	private ProcessVersion versionId;
	private String nodexml;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	private String nodeid;
	private boolean active;
	//private Integer order;


	public ProcessNodeInfo() {

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

	@Column(name = "nodexml",columnDefinition = "XML")
	public String getNodexml() {
		return nodexml;
	}

	public void setNodexml(String nodexml) {
		this.nodexml = nodexml;
	}
	
	/**
	 * 
	 * Establish Many-To-One relationship of many of this entity instances to a {@link NodeType} instance.
	 */
	@ManyToOne
	@JoinColumn(name = "nodetypeid")
	public NodeType getNodeTypeId() {
		return nodeTypeId;
	}

	public void setNodeTypeId(NodeType nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	/**
	 * 
	 * Establish Many-To-One relationship of many of this entity instances to a {@link Process} instance.
	 */
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
	@Column(name = "nodeid")
	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	@Column(name = "isactive")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	//@Column(name="order")
//	public Integer getOrder() {
//		return order;
//	}
//
//	public void setOrder(Integer order) {
//		this.order = order;
//	}
}
