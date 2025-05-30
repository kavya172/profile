package com.galaxe.gxworkflow.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.galaxe.gxworkflow.dto.BpmnDTO;
import com.galaxe.gxworkflow.dto.ProcessDTO;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.exception.RequestDataNotFoundException;
import com.galaxe.gxworkflow.services.ProcessService;

/**
 * The ProcessController class handles HTTP requests related to Process
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
 * Note: This class assumes that the end points are prefixed with "/gxworkflow".
 *
 */

@RestController
@RequestMapping("/gxworkflow")
public class ProcessController {

	/**
	 * Creating an Instance of Logger by bounding the className for LoggerFactory.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);

	/**
	 * Autowired the ProcessService for creating an reference object for class level
	 * usage.
	 */
	@Autowired
	private ProcessService processService;

	/**
	 * This Method performs an operation of Deploying workflow process.
	 * 
	 * It will accept the request in @RequestParam with the BPMN Design file and 
	 * processName in the @pathVarilable.
	 * 
	 * Verifying the process for a file, if not present throwing error
	 * as{@link RequestDataNotFoundException} with appropriate message.
	 * 
	 * <p>
	 * deployProcessWorkflow is an interface method of {@link ProcessService} which
	 * is implemented in Implementation class to perform saving of Process.
	 *
	 * @param process     is a type of MultipartFile, holds the file of an BPMN
	 *                    design.
	 * @param processName is a type of String, is a name of a process of an BPMN
	 *                    design.
	 * @param createdBy is a type of String, will holds data creator information
	 *                                       
	 * @return ResponseEntity,
	 * 				 Success, return appropriate String of message with 200 status code(HttpStatus.OK).
	 *         		 Fail, return with catch exception with 400 status code(HttpStatus.BAD_REQUEST).
	 * 
	 *         {@link Logger}, trace logging for error.
	 * @throws Exception 
	 */
	@PostMapping(value = "/process/save/{processName}/{createdBy}/{isIncrVersoin}")
	public ResponseEntity<String> saveProcessDiagram(@RequestParam("process") MultipartFile process,
			@PathVariable("processName") String processName, @PathVariable("createdBy") String createdBy, @PathVariable("isIncrVersoin") Boolean isIncrVersoin) throws Exception {
		try {
			if (process == null)
				throw new RequestDataNotFoundException("Missing XML file.");
			processService.saveProcessDiagram(processName,process,createdBy,isIncrVersoin);
			return new ResponseEntity<>("Workflow created successfully.", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Process creation failed:" + e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping(value = "/process/get/{processVersionId}")
	public ResponseEntity<?> getProcessWorkflow(@PathVariable("processVersionId") int processVersionId) throws Exception {
		try {
			BpmnDTO processXml =processService.getProcessWorkflow(processVersionId);
			return new ResponseEntity<>(processXml, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Fetching process details failed:" + e.getMessage(), e);
			throw e;
		}
	}
	
	//GxCapture
	
	@GetMapping(value = "/process/getTransaction/{processVersionId}/{processInstanceId}")
	public ResponseEntity<?> getProcessWorkflowForTransaction(@PathVariable("processVersionId") int processVersionId,@PathVariable("processInstanceId") int processInstanceId) throws Exception {
		try {
			BpmnDTO processXml =processService.getProcessWorkflowForWorkflow(processVersionId,processInstanceId);
			return new ResponseEntity<>(processXml, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Fetching process details failed:" + e.getMessage(), e);
			throw e;
		}
	}
	
	
//	@DeleteMapping(value = "/process/deleteByProcessId/{processId}")
//	public ResponseEntity<String> deleteWorkflowById(@PathVariable("processId") int processId) {
//		try {
//			processService.deleteByProcessId(processId);
//			return new ResponseEntity<>("Workflow deleted successfully", HttpStatus.OK);
//		} catch (Exception e) {
//			logger.error("deleteworkflow:" + e.getMessage(), e);
//			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
	/**
	 * This method will retrieve all process details.
	 * 
	 * getProcessDetails interface method of {@link ProcessService} to retrieve list
	 * of Process details.
	 * 
	 * @return ResponseEntity, 
	 * 					Success, List of {@link ProcessDTO} with 200 status code(HttpStatus.OK).
	 * 					Fail, return with catch exception with 400 status code(HttpStatus.BAD_REQUEST).
	 * 
	 *         {@link Logger}, trace logging for error.
	 */
	@GetMapping(value = "/process/getAll")
	public ResponseEntity<?> getProcessDetails() {
		try {
			return new ResponseEntity<>(processService.getAllProcesses(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Loading all processes failed:" + e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * This method will retrieve the specific process detail.
	 * 
	 * It will accept the request in @pathVarilable.
	 * 
	 * getProcessById interface method of {@link ProcessService} to retrieve process
	 * detail by ProcessId.
	 * 
	 * @param processId type of Integer, is Id of an {@link Process} Entity.
	 * 
	 * @return ResponseEntity,
	 * 					 Success, return  {@link ProcessDTO} with 200 status code(HttpStatus.OK). 
	 * 					 Fail, return with catch exception with 400 status code(HttpStatus.BAD_REQUEST).
	 * 
	 *         {@link Logger}, trace logging for error.
	 */
	@GetMapping(value = "/process/{processId}")
	public ResponseEntity<?> getProcessDetailsById(@PathVariable("processId") Integer processId) {
		try {
			return new ResponseEntity<>(processService.getProcessNameById(processId), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Fetching process details failed:" + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * This method is used to update the existing process by ProcessId.
	 * 
	 * It will accept the request in @RequestParam with the BPMN Design file and 
	 * processId in the @pathVarilable.
	 * 
	 * <p>
	 * updateProcessDeploy is an interface method of {@link ProcessService} which is
	 * implemented in Implementation class to perform saving of Process.
	 *
	 * @param process   is a type of MultipartFile, holds the updated file of an
	 *                  BPMN design.
	 * @param processId type of int, is Id of an {@link Process} Entity.
	 * 
	 * @param modifiedBy is a type of String,  will holds data modifier information   
	 * 
	 * @return ResponseEntity, 
	 * 				Success, return appropriate String of message with 200 status code(HttpStatus.OK). 
	 * 				Fail, return with catch exception with 400 status code(HttpStatus.BAD_REQUEST).
	 * 
	 *         {@link Logger}, trace logging for error.
	 * @throws Exception 
	 */
	@PutMapping(value = "/process/update/{versionId}/{modifiedBy}")
	public ResponseEntity<String> updateProcess(@RequestParam("process") MultipartFile process, @PathVariable("modifiedBy") String modifiedBy, @PathVariable("versionId") int versionId) throws Exception {
		try {
			processService.updateProcess(versionId, process, modifiedBy);
			return new ResponseEntity<>("Workflow Updated Successfully", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Process updation failed:" + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * This method will perform delete operation on process Entity.
	 * 
	 * This method will receive the list of processId in @RequestBody.
	 * 
	 * deleteByProcessIds interface method of {@link ProcessService} is used delete
	 * all the process based on their process Id's.
	 * 
	 * @param processIds type of List of Integer, is a Id of an {@link Process} Entity.
	 * 
	 * @return ResponseEntity, 
	 * 				Success, return appropriate String of message with 200 status code(HttpStatus.OK).
	 *  			Fail, return with catch exception with 400 status code(HttpStatus.BAD_REQUEST).
	 * 
	 *         {@link Logger}, trace logging for error.
	 */
	@DeleteMapping(value = "/process/delete")
	public ResponseEntity<String> deleteAllById(@RequestBody List<Integer> processIds) {
		try {

			processService.deleteByProcessIds(processIds);
			return new ResponseEntity<>("Processes are deleted successfully",HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Process deletion failed:" + e.getMessage(), e);
			throw e;
		}
	}
	
//	@GetMapping(value = "/process/getAllNodeDetailsForProcess/{processId}")
//	public ResponseEntity<?> getAllNodeDetailsForProcess(@PathVariable("processId") int processId) {
//		try {
//			return new ResponseEntity<>(processService.getAllNodeDetailsForProcess(processId), HttpStatus.OK);
//		} catch (Exception e) {
//			logger.error("getProcessDetails:" + e.getMessage(), e);
//			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
		
	@GetMapping(value = "/process/getProcessInstanceStatus/{processId}/{versoinId}")
	public ResponseEntity<?> getProcessInstanceStatus(@PathVariable("processId") int processId, @PathVariable("versoinId") int versionId) throws Exception {
		try {
			return new ResponseEntity<>(processService.getProcessInstanceStatus(processId, versionId), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Fetching process status details failed:" + e.getMessage(), e);
			throw e;
		}
	}
			
//	@GetMapping(value = "/process/getNodeTransactionDetails1/{processId}/{nodeName}/{nodeId}")
//	public ResponseEntity<?> getSingeNodeTransactionDetails1(@PathVariable("processId") int processId,
//												@PathVariable("nodeName") String nodeName,
//												@PathVariable("nodeId") String nodeId
//	) {
//		try {
//			return new ResponseEntity<>(processService.getSingeNodeTransactionDetails(processId,nodeName,nodeId), HttpStatus.OK);
//		} catch (Exception e) {
//			logger.error("getProcessInstanceStatus:" + e.getMessage(), e);
//			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
	
	@GetMapping(value = "/process/getProcessNames/{onlyActive}")
	public ResponseEntity<?> getProcessNames(@PathVariable("onlyActive") int onlyActive) {
		try {
			return new ResponseEntity<>(processService.getProcessNames(onlyActive), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Fetching active process names failed:" + e.getMessage(), e);
			throw e;
		}
	}
	
	@GetMapping(value = "/process/getProcessVersions/{processId}")
	public ResponseEntity<?> getProcessVersions(@PathVariable("processId") int processId) throws Exception {
		try {
			return new ResponseEntity<>(processService.getProcessVersions(processId), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Fetching all versions for the given processId failed:" + e.getMessage(), e);
			throw e;
		}
	}
	
	@PostMapping(value = "/process/getPossibleActions/")
	public ResponseEntity<?> getPossibleActions(@RequestBody WorkflowRequest workflowrequest) {
		try {
			return new ResponseEntity<>(processService.getPossibleActions(workflowrequest.getProcessInstanceId(), workflowrequest.getVersionId(),workflowrequest.getUserRole()), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Fetching possible actions failed:" + e.getMessage(), e);
			throw e;
		}
	}
}
