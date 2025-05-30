package com.galaxe.gxworkflow.mapper;

import java.util.ArrayList;
import java.util.List;

import com.galaxe.gxworkflow.dto.NodeTypeDTO;
import com.galaxe.gxworkflow.entity.NodeType;

/**
 * The NodeTypeMapper class is responsible for converting NodeTypeDTO into
 * NodeType entity class and Vice versa.
 *
 */
public class NodeTypeMapper {

	/**
	 * Transforming the List of entity data of type {@link NodeType} into List of DTO of type {@link NodeTypeDTO}. 
	 * 
	 * @param nodetype, type of  list of {@link NodeType}.
	 * 
	 * @return  List of {@link NodeTypeDTO}. 
	 */
	public List<NodeTypeDTO> nodeToDto(List<NodeType> nodetype) {

		NodeTypeDTO nodeTypeDto = new NodeTypeDTO();
		List<NodeTypeDTO> nodeTypeDtos = new ArrayList<>();
		for (NodeType node : nodetype) {
			nodeTypeDto.setNodeTypeid(node.getId());
			nodeTypeDto.setDescriptions(node.getDescriptions());
			nodeTypeDto.setNodeTypeName(node.getDescriptions());
			nodeTypeDto.setCreatedOn(node.getCreatedOn());
			nodeTypeDto.setModifiedOn(node.getModifiedOn());
			nodeTypeDto.setIsActive(node.getIsActive());
			nodeTypeDtos.add(nodeTypeDto);
		}
		return nodeTypeDtos;

	}

	public NodeTypeMapper() {

	}

}
