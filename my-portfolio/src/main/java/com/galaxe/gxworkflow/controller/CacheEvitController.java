package com.galaxe.gxworkflow.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.exception.DataNotFoundException;
import com.galaxe.gxworkflow.exception.WorkflowException;
import com.galaxe.gxworkflow.processor.ProcessScriptTask;
import com.galaxe.gxworkflow.services.CacheEvictService;
import com.galaxe.gxworkflow.services.ProcessInstanceService;
import com.galaxe.gxworkflow.services.impl.ProcessNodeInfoServiceImpl;

@RestController()
@RequestMapping("/clearCache")
public class CacheEvitController {

	private static final Logger logger = LoggerFactory.getLogger(CacheEvitController.class);
	
	@Autowired
	private CacheEvictService cacheEvictService;

	@PostMapping(value = "/all")
	public ResponseEntity<String> clean() throws Exception {
		try {
			cacheEvictService.clearAll();
			return new ResponseEntity<>("Cache cleared successfully.", HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@PostMapping(value = "/byName")
	public ResponseEntity<String> clean(List<String> cacheNames) throws Exception {
		try {
			cacheEvictService.clearByNames(cacheNames);
			return new ResponseEntity<>("Cache cleared successfully.", HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
}
