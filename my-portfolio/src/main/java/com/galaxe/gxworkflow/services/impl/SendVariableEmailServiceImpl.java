package com.galaxe.gxworkflow.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.SendVariableEmail;
import com.galaxe.gxworkflow.entity.SendVariables;
import com.galaxe.gxworkflow.repository.SendVariableEmailRepository;
import com.galaxe.gxworkflow.services.SendVariableEmailService;

@Transactional
@Service
public class SendVariableEmailServiceImpl implements SendVariableEmailService {
	
	@Autowired
	private SendVariableEmailRepository sendVariableEmailRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findBySendVariableId", key = "#sendVariableId")
	@Override
	public List<SendVariableEmail> findBySendVariableId(SendVariables sendVariableId) throws Exception {
		
		List<SendVariableEmail> sendVariableEmails = sendVariableEmailRepository.findBySendVariableId(sendVariableId);
		return sendVariableEmails;
	}

	
	
	}


