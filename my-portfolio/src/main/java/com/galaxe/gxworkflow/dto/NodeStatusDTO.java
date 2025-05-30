package com.galaxe.gxworkflow.dto;

import java.util.Set;

public class NodeStatusDTO {

	private Set<String> completedNodes;
	private String activeNode;
	private String rejectedNode;
	
	public NodeStatusDTO() {
		
	}

	public NodeStatusDTO(Set<String> completedNodes, String activeNode, String rejectedNode) {
		super();
		this.completedNodes = completedNodes;
		this.activeNode = activeNode;
		this.rejectedNode = rejectedNode;
	}

	public Set<String> getCompletedNodes() {
		return completedNodes;
	}

	public void setCompletedNodes(Set<String> completedNodes) {
		this.completedNodes = completedNodes;
	}

	public String getActiveNode() {
		return activeNode;
	}

	public void setActiveNode(String activeNode) {
		this.activeNode = activeNode;
	}

	public String getRejectedNode() {
		return rejectedNode;
	}

	public void setRejectedNode(String rejectedNode) {
		this.rejectedNode = rejectedNode;
	}

}
