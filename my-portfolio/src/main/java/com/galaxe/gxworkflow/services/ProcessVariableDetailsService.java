package com.galaxe.gxworkflow.services;

public interface ProcessVariableDetailsService {

	Object findValueforProcessVariableByProcessVariableName(int processinstanceid, String variable) throws Exception;

	
}
