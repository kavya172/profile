package com.galaxe.gxworkflow.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.ProcessStatus;
import com.galaxe.gxworkflow.repository.ProcessStatusRepository;
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
public class ProcessStatusImpl implements ProcessStatusService {
	@Autowired
	private ProcessStatusRepository processStatusRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findProcessStatusByCode", key = "#code")
	@Override
	public ProcessStatus findProcessStatusByCode(String code) throws Exception {
		ProcessStatus processStatus = processStatusRepository.findByCode(code);
		return processStatus;
	}

	}


