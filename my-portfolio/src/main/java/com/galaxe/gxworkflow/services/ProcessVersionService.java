package com.galaxe.gxworkflow.services;

import com.galaxe.gxworkflow.entity.ProcessVersion;

/**
 * The ProcessService has a declaration of methods to perform process operation.
 * 
 */
public interface ProcessVersionService {

	ProcessVersion findProcessVersion(String processName, int versionNumber) throws Exception;
	
}
