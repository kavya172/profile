package com.galaxe.gxworkflow.dto;

import com.galaxe.gxworkflow.entity.ProcessNodeInfo;

public class NodeInfoProcessDTO {

	private ProcessNodeInfo processNodeInfo;
	private String nodeId;
	private String sourceRef;
	private String targetRef;
	private String nodeTypeName;
	

	
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public ProcessNodeInfo getProcessNodeInfo() {
		return processNodeInfo;
	}
	public void setProcessNodeInfo(ProcessNodeInfo processNodeInfo) {
		this.processNodeInfo = processNodeInfo;
	}
	public String getSourceRef() {
		return sourceRef;
	}
	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}
	public String getTargetRef() {
		return targetRef;
	}
	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}
	public String getNodeTypeName() {
		return nodeTypeName;
	}
	public void setNodeTypeName(String nodeTypeName) {
		this.nodeTypeName = nodeTypeName;
	}
	
	
}
