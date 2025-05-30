package com.galaxe.gxworkflow.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "emailtype")
public class EmailType {

	private int emailTypeId;
	private String emailTypeName;
	private String descriptions;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;

	public EmailType() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public int getEmailTypeId() {
		return emailTypeId;
	}

	public void setEmailTypeId(int emailTypeId) {
		this.emailTypeId = emailTypeId;
	}

	@Column(name = "name")
	public String getEmailTypeName() {
		return emailTypeName;
	}

	public void setEmailTypeName(String emailTypeName) {
		this.emailTypeName = emailTypeName;
	}

	@Column(name = "description")
	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
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
