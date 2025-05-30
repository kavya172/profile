package com.galaxe.gxworkflow.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.repository.ProcessVariableDetailsRepository;
import com.galaxe.gxworkflow.services.ProcessVariableDetailsService;

@Transactional
@Service
public class ProcessVariableDetailesServiceImpl implements ProcessVariableDetailsService {
	
	@Autowired
	private ProcessVariableDetailsRepository processVariableDetailsRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findValueforProcessVariableByProcessVariableName", key = "#processinstanceid.toString().concat('-').concat(#variable)")
	@Override
	public Object findValueforProcessVariableByProcessVariableName(int processinstanceid, String variable) throws Exception {
		
		Object  value = processVariableDetailsRepository.getValueforProcessVariableByProcessVariableName(processinstanceid, variable);
		return value;
	}

	
	
	}



