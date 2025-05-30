package com.galaxe.gxworkflow.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.ProcessStatus;
import com.galaxe.gxworkflow.entity.ProcessVersion;
import com.galaxe.gxworkflow.repository.ProcessStatusRepository;
import com.galaxe.gxworkflow.repository.ProcessVersionRepository;
import com.galaxe.gxworkflow.services.ProcessService;
import com.galaxe.gxworkflow.services.ProcessStatusService;
import com.galaxe.gxworkflow.services.ProcessVersionService;

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
public class ProcessVersionServiceImpl implements ProcessVersionService {


	@Autowired
	private ProcessVersionRepository processVersionRepo;
	
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Cacheable(value = "findProcessVersion", key="#processName.concat('-').concat(#versionNumber.toString())")
	@Override
	public ProcessVersion findProcessVersion(String processName, int versionNumber) throws Exception {
		ProcessVersion processVersion = processVersionRepo.findByProcessNameAndVersionNumber(processName, versionNumber);
		return processVersion;
	}

	}


