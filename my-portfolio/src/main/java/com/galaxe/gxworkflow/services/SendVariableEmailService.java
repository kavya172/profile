package com.galaxe.gxworkflow.services;

import java.util.List;

import com.galaxe.gxworkflow.entity.SendVariableEmail;
import com.galaxe.gxworkflow.entity.SendVariables;

public interface SendVariableEmailService {

	List<SendVariableEmail> findBySendVariableId(SendVariables sendVariableId) throws Exception;

	
}
