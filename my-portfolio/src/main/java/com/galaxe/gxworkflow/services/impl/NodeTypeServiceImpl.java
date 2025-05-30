package com.galaxe.gxworkflow.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.NodeType;
import com.galaxe.gxworkflow.repository.NodeTypeRepository;
import com.galaxe.gxworkflow.services.NodeTypeService;

@Transactional
@Service
public class NodeTypeServiceImpl implements NodeTypeService {
	
	@Autowired
	private NodeTypeRepository nodeTypeRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findNodeTypeByName", key = "#name")
	@Override
	public NodeType findNodeTypeByName(String name) throws Exception {
		
		NodeType nodeType = nodeTypeRepository.findByNameIgnoreCase(name);
		return nodeType;
	}
	
	}


