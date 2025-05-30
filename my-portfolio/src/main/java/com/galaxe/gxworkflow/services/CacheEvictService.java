package com.galaxe.gxworkflow.services;

import java.util.List;

/**
 * The ProcessService has a declaration of methods to perform process operation.
 * 
 */
public interface CacheEvictService {

	public void clearAll() throws Exception;
	public void clearByNames(List<String> cacheNames) throws Exception;
}
