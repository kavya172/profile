package com.galaxe.gxworkflow.services;

import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.SendVariables;

public interface SendVariableService {

	SendVariables findSendVariablesByProcessNodeInfoId(ProcessNodeInfo processNodeInfo) throws Exception;

	
}
