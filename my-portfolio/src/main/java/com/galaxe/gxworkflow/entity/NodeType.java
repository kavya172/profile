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
 * This NodeType class represents a persistent entity of the workflow.
 *
 * <p>
 * 
 * This entity class is a component that represents a table of  WF_NodeType
 * 
 * Here we explicitly assigning the table name WF_NodeType to entity class using @Table annotation.
 * 
 * nodeTypeId type of Integer,is a unique identifier of the NodeType
 * 
 * nodeTypeName type of String, is a name of the NodeType
 * 
 * descriptions type of String, is a description related to the NodeType
 * 
 * isActive type of integer, which holds 0 (inactive) or 1 (active), by default it is 1.
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
 */

@Entity
@Table(name = "nodetype")
public class NodeType {

	private int id;
	private String name;
	private String descriptions;
	private Boolean isActive;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;

	public NodeType() {

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

	@Column(name = "descriptions")
	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	@Column(name = "isactive")
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
