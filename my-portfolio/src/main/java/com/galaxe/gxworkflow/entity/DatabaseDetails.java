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
@Table(name = "databasedetails")
public class DatabaseDetails {

	private int id;
	//private ProcessVersion versionId;
	private ProcessNodeInfo processNodeInfoId;
	private String server;
	private String port;
	private String databaseName;
	private String schema;
	private String databaseType;
	private String username;
	private String password;
	private Timestamp createdOn;
	private String createdBy;
	private String modifiedBy;
	private Timestamp modifiedOn;

	public DatabaseDetails() {
		
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

	@Column(name = "server")
	public String getServer() {
		return server;
	}


	public void setServer(String server) {
		this.server = server;
	}

	@Column(name = "port")
	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "databasename")
	public String getDatabaseName() {
		return databaseName;
	}


	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Column(name = "schema")
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	@Column(name = "databasetype")
	public String getDatabaseType() {
		return databaseType;
	}


	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
