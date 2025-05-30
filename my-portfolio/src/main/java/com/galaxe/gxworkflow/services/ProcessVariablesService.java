package com.galaxe.gxworkflow.services;

import java.util.List;

import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessVariables;

public interface ProcessVariablesService {

	List<ProcessVariables> findProcessVariablesByProcessNodeInfo(ProcessNodeInfo processNodeInfo) throws Exception;
	
}
