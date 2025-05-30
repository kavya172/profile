package com.galaxe.gxworkflow.dto;

/**
 * 
 * This class represents a Data Transfer Object for a NodeType.
 *
 * <p>
 * This class is used for transferring Node Type data between application and
 * data layer.
 * 
 * This class extends with GenericDTO to inherit additional functionality.
 * 
 * nodeTypeid type of int, is a unique identifier of the NodeTypeDTO
 * 
 * nodeTypeName type of String, is a nodeTypeName of a node.
 * 
 * descriptions type of String, is a note of a node type.
 * 
 * isActive type of int, is a active Status of the node type.
 * 
 * @Getter and @Setter methods to handle Get and Set operations.
 * 
 */
public class NodeTypeDTO extends GenericDTO {

	private int nodeTypeid;
	private String nodeTypeName;
	private String descriptions;
	private Boolean isActive;

	public int getNodeTypeid() {
		return nodeTypeid;
	}

	public void setNodeTypeid(int nodeTypeid) {
		this.nodeTypeid = nodeTypeid;
	}

	public String getNodeTypeName() {
		return nodeTypeName;
	}

	public void setNodeTypeName(String nodeTypeName) {
		this.nodeTypeName = nodeTypeName;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
