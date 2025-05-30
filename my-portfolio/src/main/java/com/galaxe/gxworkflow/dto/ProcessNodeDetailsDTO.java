package com.galaxe.gxworkflow.dto;

/**
 * 
 * This class represents a Data Transfer Object for a Process Node Details.
 *
 * <p>
 * This class is used for transferring Process Node Details data between
 * application and data layer.
 * 
 * This class extends with GenericDTO to inherit additional functionality.
 * 
 * id type of int, is a unique identifier of the ProcessNodeDetailsDTO
 * 
 * parentNodeId type of ProcessNodeInfoDTO, is a NodeDetails of the
 * ProcessNodeInfo.
 * 
 * childNodeId type of ProcessNodeInfoDTO, is a NodeDetails of the
 * ProcessNodeInfo.
 * 
 * sequenceName type of String, is a sequenceName of the ProcessNodeDetails.
 *
 */
public class ProcessNodeDetailsDTO extends GenericDTO {

	private int id;
	private ProcessNodeInfoDTO parentNodeId;
	private ProcessNodeInfoDTO childNodeId;
	private String sequenceName;

	public ProcessNodeDetailsDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProcessNodeInfoDTO getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(ProcessNodeInfoDTO parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public ProcessNodeInfoDTO getChildNodeId() {
		return childNodeId;
	}

	public void setChildNodeId(ProcessNodeInfoDTO childNodeId) {
		this.childNodeId = childNodeId;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

}
