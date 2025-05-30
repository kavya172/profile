package com.galaxe.gxworkflow.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.SendVariables;
import com.galaxe.gxworkflow.repository.SendVariableRepository;
import com.galaxe.gxworkflow.services.SendVariableService;
import com.galaxe.gxworkflow.services.VariableTypeService;

@Transactional
@Service
public class SendVariableServiceImpl implements SendVariableService {
	
	@Autowired
	private SendVariableRepository sendVariableRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findSendVariablesByProcessNodeInfoId", key = "#processNodeInfo")
	@Override
	public SendVariables findSendVariablesByProcessNodeInfoId(ProcessNodeInfo processNodeInfo) throws Exception {
		
		SendVariables sendVariables = sendVariableRepository.findByProcessNodeInfoId(processNodeInfo);
		return sendVariables;
	}

	
	
	}


