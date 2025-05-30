package com.galaxe.gxworkflow.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessVariables;
import com.galaxe.gxworkflow.entity.VariableType;
import com.galaxe.gxworkflow.repository.ProcessVariableRepository;
import com.galaxe.gxworkflow.repository.VariableTypeRepository;
import com.galaxe.gxworkflow.services.ProcessVariablesService;
import com.galaxe.gxworkflow.services.VariableTypeService;

@Transactional
@Service
public class ProcessVariablesServiceImpl implements ProcessVariablesService {
	
	@Autowired
	private ProcessVariableRepository processVariableRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findProcessVariablesByProcessNodeInfo", key = "#processNodeInfo")
	@Override
	public List<ProcessVariables> findProcessVariablesByProcessNodeInfo(ProcessNodeInfo processNodeInfo) throws Exception {
		
		List<ProcessVariables> processVariables = processVariableRepository.findByProcessNodeInfoId(processNodeInfo);
		return processVariables;
	}

	
	
	}



