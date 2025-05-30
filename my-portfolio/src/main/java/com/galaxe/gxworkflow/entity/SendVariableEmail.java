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
@Table(name = "sendvariableemails")
public class SendVariableEmail {

	private int id;
	private SendVariables sendVariableId;
	private EmailType emailTypeId;
	private String emailId;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;
	

	public SendVariableEmail() {

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
	@JoinColumn(name = "sendVariableId")
	public SendVariables getSendVariableId() {
		return sendVariableId;
	}

	public void setSendVariableId(SendVariables sendVariableId) {
		this.sendVariableId = sendVariableId;
	}

	@ManyToOne
	@JoinColumn(name = "emailTypeId")
	public EmailType getEmailTypeId() {
		return emailTypeId;
	}

	public void setEmailTypeId(EmailType emailTypeId) {
		this.emailTypeId = emailTypeId;
	}

	@Column(name = "emailid")
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
