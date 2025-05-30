package com.galaxe.gxworkflow.services;

import com.galaxe.gxworkflow.entity.ProcessStatus;

/**
 * The ProcessService has a declaration of methods to perform process operation.
 * 
 */
public interface ProcessStatusService {
	ProcessStatus findProcessStatusByCode(String code) throws Exception;
}
