package com.galaxe.gxworkflow.mapper;

import org.springframework.stereotype.Component;

import com.galaxe.gxworkflow.dto.ProcessNodeInfoDTO;
import com.galaxe.gxworkflow.entity.NodeType;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;

/**
 * The ProcessNodeInfoMapper class is responsible for converting
 * ProcessNodeInfoDTO into ProcessNodeInfo entity class and Vice versa.
 *
 */
@Component
public class ProcessNodeInfoMapper {

	/**
	 * Transforming the List of DTO data of type {@link ProcessNodeInfoDTO} into List of entity of type {@link ProcessNodeInfo}. 
	 * 
	 * @param processNodeInfoDto, type of  list of {@link ProcessNodeInfoDTO}.
	 * 
	 * @return  List of {@link ProcessNodeInfo}. 
	 */
	public ProcessNodeInfo toProcessNodeInfoEntity(ProcessNodeInfoDTO processNodeInfoDto) {

		ProcessNodeInfo processNodeInfo = new ProcessNodeInfo();
		processNodeInfo.setId(processNodeInfoDto.getNodeId());
//		processNodeInfo.setNodeName(processNodeInfoDto.getNodeName());
		NodeType nodeType = new NodeType();
		nodeType.setId(processNodeInfoDto.getNodeType().getNodeTypeid());
		processNodeInfo.setNodeTypeId(nodeType);
//		processNodeInfo.setNodeFlowId(processNodeInfoDto.getNodeFlowId());
		processNodeInfo.setCreatedOn(processNodeInfoDto.getCreatedOn());
		processNodeInfo.setModifiedOn(processNodeInfoDto.getModifiedOn());
		return processNodeInfo;
	}

	public ProcessNodeInfoMapper() {

	}

}
