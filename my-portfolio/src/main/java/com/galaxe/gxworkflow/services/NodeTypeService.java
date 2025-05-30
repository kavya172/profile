package com.galaxe.gxworkflow.services;

import com.galaxe.gxworkflow.entity.NodeType;

public interface NodeTypeService {

	NodeType findNodeTypeByName(String name) throws Exception;
	
}
