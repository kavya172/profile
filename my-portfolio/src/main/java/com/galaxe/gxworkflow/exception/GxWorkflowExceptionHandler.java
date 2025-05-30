/*******************************************************************************
 * GalaxE.Healthcare Solutions Inc. ©2019, Confidential and Proprietary - All Rights Reserved.
 * No unauthorized use permitted. The content contained herein may not be reproduced,
 * adapted/modified, published, performed or displayed without the express written 
 * authorization of GalaxE.Healthcare Solutions, Inc..
 ******************************************************************************/
package com.galaxe.gxworkflow.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import com.galaxe.mail.dto.MailConfigDTO;
import com.galaxe.mail.service.MailService;
import com.galaxe.mail.utils.MailUtils;

@ControllerAdvice("com.galaxe")
public class GxWorkflowExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GxWorkflowExceptionHandler.class);

//	@Value("${mail.error.notification.from}")
//	private String errorFrom;
//	
//	@Value("${mail.error.notification.to}")
//	private String errorTo;
	
	@Value("${mail.error.notification.subject}")
	private String errorSubject;
	
	@Autowired
	MailConfigDTO mailConfigDTO;

	@Autowired
	private MailUtils mailUtils;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<GxWorkflowErrorMessage> handleException(Exception e) throws Exception {
		GxWorkflowErrorMessage message = new GxWorkflowErrorMessage();
		message.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		message.setErrorMessage(e.getMessage());
		message.setDetails(e.toString());
		mailUtils.sendMailWithoutTemplate(errorSubject, e.getMessage(), mailConfigDTO);
		return new ResponseEntity<GxWorkflowErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(WorkflowException.class)
	public ResponseEntity<GxWorkflowErrorMessage> handleWorkflowException(Exception e) throws Exception {
		GxWorkflowErrorMessage message = new GxWorkflowErrorMessage();
		message.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		message.setErrorMessage(e.getMessage());
		message.setDetails(e.toString());
		return new ResponseEntity<GxWorkflowErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = {DataNotFoundException.class})
	public ResponseEntity<GxWorkflowErrorMessage> handleDataNotFoundException(DataNotFoundException e) throws Exception {
		GxWorkflowErrorMessage message = new GxWorkflowErrorMessage();
		message.setErrorCode(HttpStatus.NOT_FOUND.value());
		message.setErrorMessage(e.getMessage());
		message.setDetails(String.valueOf(e.getCause()));
		return new ResponseEntity<GxWorkflowErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@ExceptionHandler(DuplicateNameException.class)
	public ResponseEntity<GxWorkflowErrorMessage> handleDuplicateNameException(DuplicateNameException e) throws Exception {
		GxWorkflowErrorMessage message = new GxWorkflowErrorMessage();
		message.setErrorCode(1062);
		message.setErrorMessage(e.getMessage());
		message.setDetails(String.valueOf(e.getCause()));
		return new ResponseEntity<GxWorkflowErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InValidDataException.class)
	public ResponseEntity<GxWorkflowErrorMessage> handleInValidDataExceptionException(InValidDataException e) throws Exception {
		GxWorkflowErrorMessage message = new GxWorkflowErrorMessage();
		message.setErrorCode(1065);
		message.setErrorMessage(e.getMessage());
		message.setDetails(String.valueOf(e.getCause()));
		return new ResponseEntity<GxWorkflowErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(RequestDataNotFoundException.class)
	public ResponseEntity<GxWorkflowErrorMessage> handleRequestDataNotFoundExceptionException(RequestDataNotFoundException e) throws Exception {
		GxWorkflowErrorMessage message = new GxWorkflowErrorMessage();
		message.setErrorCode(1099);
		message.setErrorMessage(e.getMessage());
		message.setDetails(String.valueOf(e.getCause())); 
		return new ResponseEntity<GxWorkflowErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
