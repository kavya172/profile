package com.galaxe.gxworkflow.dto;

public class BpmnDTO {

	private int processVersionId;
	private int versionNumber;
	private String processName;
	private String xmlString;
	private int currentVersion;
	private int maxVersion;
	private String versionName;
	

	public int getProcessVersionId() {
		return processVersionId;
	}
	public void setProcessVersionId(int processVersionId) {
		this.processVersionId = processVersionId;
	}
	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getXmlString() {
		return xmlString;
	}
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	public int getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}
	public int getMaxVersion() {
		return maxVersion;
	}
	public void setMaxVersion(int maxVersion) {
		this.maxVersion = maxVersion;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
}
