package com.galaxe.gxworkflow.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.ProcessStatus;
import com.galaxe.gxworkflow.repository.ProcessStatusRepository;
import com.galaxe.gxworkflow.services.CacheEvictService;
import com.galaxe.gxworkflow.services.ProcessService;
import com.galaxe.gxworkflow.services.ProcessStatusService;

/**
 * 
 * This class provides the implementation for the methods defined in the
 * interface {@link ProcessService}
 * 
 * The ProcessServiceImpl class provides operations for managing Process Related
 * Information.
 * 
 * This service class handles business logic related to Process management.
 *
 * @Autowired is used for creating an reference object for class level usage.
 *
 */
@Transactional
@Service
public class CacheEvictServiceImpl implements CacheEvictService {


	@Autowired
	CacheManager cacheManager;
	
	@Override
	public void clearAll() throws Exception {
		cacheManager.getCacheNames().parallelStream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
		//cacheManager.getCache(cacheName).evict(cacheKey);
	}
	
	@Override
	public void clearByNames(List<String> cacheNames) throws Exception {
		cacheNames.parallelStream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	}


