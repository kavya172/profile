package com.galaxe.gxworkflow.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.VariableType;
import com.galaxe.gxworkflow.repository.VariableTypeRepository;
import com.galaxe.gxworkflow.services.VariableTypeService;

@Transactional
@Service
public class VariableTypeServiceImpl implements VariableTypeService {
	
	@Autowired
	private VariableTypeRepository variableTypeRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findVariableTypeByName", key = "#name")
	@Override
	public VariableType findVariableTypeByName(String name) throws Exception {
		
		VariableType variableType = variableTypeRepository.findByName(name);
		return variableType;
	}

	
	
	}


