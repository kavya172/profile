package com.galaxe.gxworkflow.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galaxe.gxworkflow.dto.ProcessInstanceResponse;
import com.galaxe.gxworkflow.services.ProcessInstanceService;

/**
 * The ProcessInstanceController class handles HTTP requests related to ProcessInstance
 * operations. Which perform CRUD operations Requests for the process.
 *
 * <p>
 * Follows the RESTful architectural style and adheres to best practices for
 * designing APIs. It receives requests and delegates the processing to the
 * appropriate methods in the ProcessService, then returns the appropriate HTTP
 * response to the client.
 *
 *
 * <p>
 * Note: This class assumes that the end points are prefixed with "/gxworkflow/processInstance".
 *
 */

@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

	private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);
	
	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@GetMapping(value = "/get/{processInstanceId}")
	public ResponseEntity<ProcessInstanceResponse> getByProcesaInstanceId(@PathVariable("processInstanceId") int processInstanceId) {
		try {
			ProcessInstanceResponse processInstanceResponse = processInstanceService.getProcessInstanceById(processInstanceId);
			return new ResponseEntity<>(processInstanceResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getByProcesaInstanceId:" + e.getMessage(), e);
			throw e;
		}
	}
	
	@GetMapping(value = "/getAll")
	public ResponseEntity<List<ProcessInstanceResponse>> getAllProcessInstances() {
		try {
			List<ProcessInstanceResponse> processInstanceResponseList = processInstanceService.getProcessInstances();
			return new ResponseEntity<>(processInstanceResponseList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getAllProcessInstances:" + e.getMessage(), e);
			throw e;
		}
	}
}
