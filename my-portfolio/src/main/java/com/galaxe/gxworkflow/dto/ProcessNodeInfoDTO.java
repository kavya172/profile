package com.galaxe.gxworkflow.dto;

/**
 * 
 * This class represents a Data Transfer Object for a ProcessNodeInfo.
 *
 * <p>
 * This class is used for transferring Process NodeInfo data between application
 * and data layer.
 * 
 * This class extends with GenericDTO to inherit additional functionality.
 * 
 * nodeId type of int, is a unique identifier of the ProcessNodeInfoDTO.
 * 
 * processId type of int, is a Id of the process where the node belong to.
 * 
 * nodeType type of NodeTypeDTO, is a nodeType of a node.
 * 
 * nodeName type of String, is the name of the node.
 * 
 * nodeFlowId type of String, is a node flow Id of the node belonging to the process.
 * 
 */
public class ProcessNodeInfoDTO extends GenericDTO {

	private int nodeId;
	private int processId;
	private NodeTypeDTO nodeType;
	private String nodeName;
	private String nodeFlowId;
	private int versionId;

	public ProcessNodeInfoDTO() {
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public NodeTypeDTO getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeTypeDTO nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeFlowId() {
		return nodeFlowId;
	}

	public void setNodeFlowId(String nodeFlowId) {
		this.nodeFlowId = nodeFlowId;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public ProcessNodeInfoDTO(int processId, NodeTypeDTO nodeType, String nodeName, String nodeFlowId, int versionId) {
		super();
		this.processId = processId;
		this.nodeType = nodeType;
		this.nodeName = nodeName;
		this.nodeFlowId = nodeFlowId;
		this.versionId = versionId;
	}

}
