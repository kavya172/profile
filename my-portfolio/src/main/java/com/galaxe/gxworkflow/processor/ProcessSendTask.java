package com.galaxe.gxworkflow.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.dto.WorkflowRequest;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.SendVariableEmail;
import com.galaxe.gxworkflow.entity.SendVariables;
import com.galaxe.gxworkflow.repository.ProcessNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableDetailsRepository;
import com.galaxe.gxworkflow.repository.SendVariableEmailRepository;
import com.galaxe.gxworkflow.repository.SendVariableRepository;
import com.galaxe.gxworkflow.services.impl.ProcessTransactionService;
import com.galaxe.mail.dto.MailConfigDTO;
import com.galaxe.mail.utils.MailUtils;

@Component
public class ProcessSendTask {
	
	@Autowired
	private MailUtils mailUtils;

	@Value("${spring.mail.templates-location}")
	private String templatesLocation;
	
	@Autowired
	private ProcessNodeInfoRepository processNodeInfoRepository;

	@Autowired
	private SendVariableRepository sendVariableRepository;
	
	@Autowired
	private SendVariableEmailRepository sendVariableEmailRepository;
	
	@Autowired
	private ProcessVariableDetailsRepository processVariableDetailsRepository;
	
	@Autowired
	private ProcessTransactionService processTransactionService;

	@Autowired
	private ProcessComplete processComplete;
	
	

	public void ExecuteSendTask(int processNodeInfoId, String createdBy, ProcessInstanceRequest processInstanceRequest) throws Exception {
		
		try {
			
			//FIXME: USE CACHE
			ProcessNodeInfo processNodeInfo = processNodeInfoRepository.findById(processNodeInfoId).get();
			Map<String, Object> dynamicAttributes = processInstanceRequest.getDynamicAttributes();
			
			
			MailConfigDTO mailConfigDTO = new MailConfigDTO();
			mailConfigDTO.setActive(true);
			
			//FIXME: CAN WE USE CACHE HERE?
			SendVariables sendVariables = sendVariableRepository.findByProcessNodeInfoId(processNodeInfo);
			List<SendVariableEmail> sendVariableEmails = sendVariableEmailRepository.findBySendVariableId(sendVariables);
			
			//group all the from,to,cc,bcc mails together
			sendVariableEmails.stream().forEach(sendVariableEmail ->{
				
				switch (sendVariableEmail.getEmailTypeId().getDescriptions().toLowerCase()) {
				case "from":
					if(sendVariableEmail.getEmailId().matches("\\{([^}]*)\\}")) {
						if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
							//check if we have two dynamic from values and through error if not
							String dynamicFrom = getDynamicValue(dynamicAttributes,sendVariableEmail.getEmailId());
							if(null != dynamicFrom && !dynamicFrom.isEmpty()) {
								mailConfigDTO.setMailFrom(dynamicFrom);
							}else {
								String dyFrom =getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),processInstanceRequest.getProcessInstanceId());
								mailConfigDTO.setMailFrom(dyFrom);
							}
						}else {
							String dyFrom=getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),processInstanceRequest.getProcessInstanceId());
							mailConfigDTO.setMailFrom(dyFrom);
						} 
					}else {
						String from = sendVariableEmails.stream()
								.filter(value -> null != value.getEmailId() && "from".equalsIgnoreCase(value.getEmailTypeId().getDescriptions()))
								.map(SendVariableEmail::getEmailId)
								.collect(Collectors.joining(","));
						mailConfigDTO.setMailFrom(from);
					}
					break;
					
				case "to":
					if(sendVariableEmail.getEmailId().matches("\\{([^}]*)\\}")) {
						if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
							String dynamicTo = getDynamicValue(dynamicAttributes,sendVariableEmail.getEmailId());
							if(null != dynamicTo && !dynamicTo.isEmpty() ) {
								mailConfigDTO.setMailTo(dynamicTo);
							}else {
								String dyTo = getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),processInstanceRequest.getProcessInstanceId());
								mailConfigDTO.setMailTo(dyTo);
							}
						}else {
							String dyTo =getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),processInstanceRequest.getProcessInstanceId());
							mailConfigDTO.setMailTo(dyTo);
						}
					}else {
						String to = sendVariableEmails.stream()
								.filter(value -> null != value.getEmailId() && "to".equalsIgnoreCase(value.getEmailTypeId().getDescriptions()))
								.map(SendVariableEmail::getEmailId)
								.collect(Collectors.joining(","));
						System.out.println(to);
						mailConfigDTO.setMailTo(to);
					}
					break;
					
				case "cc":
					if(sendVariableEmail.getEmailId().matches("\\{([^}]*)\\}")) {
						if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
							String dynamicCc = getDynamicValue(dynamicAttributes,sendVariableEmail.getEmailId());
							if(null != dynamicCc && !dynamicCc.isEmpty() ) {
								mailConfigDTO.setMailCC(dynamicCc);
							}else {
								String dyCc = getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),processInstanceRequest.getProcessInstanceId());
								mailConfigDTO.setMailCC(dyCc);
							}
						}else {
							String dyCc= getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),processInstanceRequest.getProcessInstanceId());
							mailConfigDTO.setMailCC(dyCc);
						}
					}else {
						String cc = sendVariableEmails.stream()
								.filter(value -> null != value.getEmailId() && "cc".equalsIgnoreCase(value.getEmailTypeId().getDescriptions()))
								.map(SendVariableEmail::getEmailId)
								.collect(Collectors.joining(","));
						mailConfigDTO.setMailCC(cc);
					}
					break;
					
				case "bcc":
					
					break;
					
				default:
					break;
				}
				
			});
			String body ="";
			if(sendVariables.getBody().matches("\\{([^}]*)\\}")) {
				if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					body = getDynamicValue(dynamicAttributes,sendVariables.getBody());
					body =getProcessVariableValueForGivenSubjectOrBody(processNodeInfo.getId(), mailConfigDTO,body,processInstanceRequest.getProcessInstanceId()); 
				}else {
					body =getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariables.getBody(),processInstanceRequest.getProcessInstanceId()); 
				}
			}else {
				body =sendVariables.getBody();
			}
			
			String subject ="";
			if(sendVariables.getSubject().matches("\\{([^}]*)\\}")) {
				if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					Object object = dynamicAttributes.get(sendVariables.getSubject()
							.replace("{", "")
							.replace("}", ""));
					if(null != object) {
						subject = populateDynamicValues(dynamicAttributes,object.toString());
						subject = getProcessVariableValueForGivenSubjectOrBody(processNodeInfo.getId(), mailConfigDTO,subject,processInstanceRequest.getProcessInstanceId()); 
					}
				}else {
					subject = getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariables.getSubject(),processInstanceRequest.getProcessInstanceId()); 
				}
			}else {
				subject =sendVariables.getSubject();
			}
			if(mailConfigDTO.getMailCC().isBlank()) {
				mailConfigDTO.setMailCC(null);
			}
			
			
			if("HTML".equalsIgnoreCase(sendVariables.getBodyType())) {
				//thing for other scenarios
				sendMailForHtmlTemplate(mailConfigDTO,body,subject,dynamicAttributes);
			}else {
				if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					String bodyWithDynamicValue = populateDynamicValues(dynamicAttributes,body);
					sendMailForText(subject,bodyWithDynamicValue,mailConfigDTO);
				}else {
					sendMailForText(subject,body,mailConfigDTO);
				}
			}
		} catch (Exception e) {
			logFailedSendTask(processNodeInfoId, processInstanceRequest);
			throw e;
		}
	}
	
public void ExecuteSendTaskForWorkflow(int processNodeInfoId, String createdBy, WorkflowRequest workflowRequest) throws Exception {
		
		try {
			
			//FIXME: USE CACHE
			ProcessNodeInfo processNodeInfo = processNodeInfoRepository.findById(processNodeInfoId).get();
			Map<String, Object> dynamicAttributes = workflowRequest.getDynamicAttributes();
			
			
			MailConfigDTO mailConfigDTO = new MailConfigDTO();
			mailConfigDTO.setActive(true);
			
			//FIXME: CAN WE USE CACHE HERE?
			SendVariables sendVariables = sendVariableRepository.findByProcessNodeInfoId(processNodeInfo);
			List<SendVariableEmail> sendVariableEmails = sendVariableEmailRepository.findBySendVariableId(sendVariables);
			
			//group all the from,to,cc,bcc mails together
			sendVariableEmails.stream().forEach(sendVariableEmail ->{
				
				switch (sendVariableEmail.getEmailTypeId().getDescriptions().toLowerCase()) {
				case "from":
					if(sendVariableEmail.getEmailId().matches("\\{([^}]*)\\}")) {
						if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
							//check if we have two dynamic from values and through error if not
							String dynamicFrom = getDynamicValue(dynamicAttributes,sendVariableEmail.getEmailId());
							if(null != dynamicFrom && !dynamicFrom.isEmpty()) {
								mailConfigDTO.setMailFrom(dynamicFrom);
							}else {
								String dyFrom =getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),workflowRequest.getProcessInstanceId());
								mailConfigDTO.setMailFrom(dyFrom);
							}
						}else {
							String dyFrom=getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),workflowRequest.getProcessInstanceId());
							mailConfigDTO.setMailFrom(dyFrom);
						} 
					}else {
						String from = sendVariableEmails.stream()
								.filter(value -> null != value.getEmailId() && "from".equalsIgnoreCase(value.getEmailTypeId().getDescriptions()))
								.map(SendVariableEmail::getEmailId)
								.collect(Collectors.joining(","));
						mailConfigDTO.setMailFrom(from);
					}
					break;
					
				case "to":
					if(sendVariableEmail.getEmailId().matches("\\{([^}]*)\\}")) {
						if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
							String dynamicTo = getDynamicValue(dynamicAttributes,sendVariableEmail.getEmailId());
							if(null != dynamicTo && !dynamicTo.isEmpty() ) {
								mailConfigDTO.setMailTo(dynamicTo);
							}else {
								String dyTo = getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),workflowRequest.getProcessInstanceId());
								mailConfigDTO.setMailTo(dyTo);
							}
						}else {
							String dyTo =getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),workflowRequest.getProcessInstanceId());
							mailConfigDTO.setMailTo(dyTo);
						}
					}else {
						String to = sendVariableEmails.stream()
								.filter(value -> null != value.getEmailId() && "to".equalsIgnoreCase(value.getEmailTypeId().getDescriptions()))
								.map(SendVariableEmail::getEmailId)
								.collect(Collectors.joining(","));
						System.out.println(to);
						mailConfigDTO.setMailTo(to);
					}
					break;
					
				case "cc":
					if(sendVariableEmail.getEmailId().matches("\\{([^}]*)\\}")) {
						if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
							String dynamicCc = getDynamicValue(dynamicAttributes,sendVariableEmail.getEmailId());
							if(null != dynamicCc && !dynamicCc.isEmpty() ) {
								mailConfigDTO.setMailCC(dynamicCc);
							}else {
								String dyCc = getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),workflowRequest.getProcessInstanceId());
								mailConfigDTO.setMailCC(dyCc);
							}
						}else {
							String dyCc= getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariableEmail.getEmailId(),workflowRequest.getProcessInstanceId());
							mailConfigDTO.setMailCC(dyCc);
						}
					}else {
						String cc = sendVariableEmails.stream()
								.filter(value -> null != value.getEmailId() && "cc".equalsIgnoreCase(value.getEmailTypeId().getDescriptions()))
								.map(SendVariableEmail::getEmailId)
								.collect(Collectors.joining(","));
						mailConfigDTO.setMailCC(cc);
					}
					break;
					
				case "bcc":
					
					break;
					
				default:
					break;
				}
				
			});
			String body ="";
			if(sendVariables.getBody().matches("\\{([^}]*)\\}")) {
				if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					body = getDynamicValue(dynamicAttributes,sendVariables.getBody());
					body =getProcessVariableValueForGivenSubjectOrBody(processNodeInfo.getId(), mailConfigDTO,body,workflowRequest.getProcessInstanceId()); 
				}else {
					body =getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariables.getBody(),workflowRequest.getProcessInstanceId()); 
				}
			}else {
				body =sendVariables.getBody();
			}
			
			String subject ="";
			if(sendVariables.getSubject().matches("\\{([^}]*)\\}")) {
				if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					Object object = dynamicAttributes.get(sendVariables.getSubject()
							.replace("{", "")
							.replace("}", ""));
					if(null != object) {
						subject = populateDynamicValues(dynamicAttributes,object.toString());
						subject = getProcessVariableValueForGivenSubjectOrBody(processNodeInfo.getId(), mailConfigDTO,subject,workflowRequest.getProcessInstanceId()); 
					}
				}else {
					subject = getProcessVariableValueForGivenValue(processNodeInfo.getId(), mailConfigDTO, sendVariables.getSubject(),workflowRequest.getProcessInstanceId()); 
				}
			}else {
				subject =sendVariables.getSubject();
			}
			if(mailConfigDTO.getMailCC().isBlank()) {
				mailConfigDTO.setMailCC(null);
			}
			
			
			if("HTML".equalsIgnoreCase(sendVariables.getBodyType())) {
				//thing for other scenarios
				sendMailForHtmlTemplate(mailConfigDTO,body,subject,dynamicAttributes);
			}else {
				if(null != dynamicAttributes && !dynamicAttributes.isEmpty()) {
					String bodyWithDynamicValue = populateDynamicValues(dynamicAttributes,body);
					sendMailForText(subject,bodyWithDynamicValue,mailConfigDTO);
				}else {
					sendMailForText(subject,body,mailConfigDTO);
				}
			}
		} catch (Exception e) {
			logFailedSendTaskForWorkflow(processNodeInfoId, workflowRequest);
			throw e;
		}
	}



	private String getProcessVariableValueForGivenValue(int processNodeInfoId, MailConfigDTO mailConfigDTO,
			String inputValue, Integer processInstanceId) {
		String globalValue = inputValue.replace("{", "").replace("}", "");
		Object ObjValue = processVariableDetailsRepository.getValueforProcessVariableByProcessVariableName(processInstanceId, globalValue);
		if(null != ObjValue) {
			return ObjValue.toString();
		}
		else return null;
	}
	
	private String getProcessVariableValueForGivenSubjectOrBody(int processNodeInfoId, MailConfigDTO mailConfigDTO,
			String inputValue,Integer processInstanceId) {
		
		 StringBuilder resultString = new StringBuilder();
	        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
	        Matcher matcher = pattern.matcher(inputValue);
	        
	        while (matcher.find()) {
	            String placeholderKey = matcher.group(1); // Extract the placeholder key
//	            String globalValue = inputValue.replace("{", "").replace("}", "");
	    		Object ObjValue = processVariableDetailsRepository
	    				.getValueforProcessVariableByProcessVariableName(processInstanceId, placeholderKey);
	            if (ObjValue != null) {
	            	String value = ObjValue.toString();
	                matcher.appendReplacement(resultString, value.toString());
	            }
	        }
	        matcher.appendTail(resultString);

	        return resultString.toString();
	}
	
	
	private void sendMailForText(String subject, String body, MailConfigDTO mailConfigDTO) throws MessagingException {
		mailUtils.sendMailWithoutTemplate(subject, body, mailConfigDTO);
	}
	
	private void sendMailForHtmlTemplate(MailConfigDTO mailConfigDTO, String body, String subject,Map<String, Object> dynamicAttributes) throws MessagingException, IOException {
	    // Create temporary HTML file
	    String fileName = "tempFile.html";
	    String filePath = templatesLocation + File.separator + fileName;
	    File htmlFile = new File(filePath);
	    htmlFile.createNewFile();
	    
	    // Write email body to the HTML file
	    FileWriter fileWriter = new FileWriter(htmlFile);
	    fileWriter.write(body);
	    fileWriter.close();
	    
	    Map<String, Object> placeHolderMap = new HashMap<String, Object>();
	    populatePlaceHolderValues(placeHolderMap, dynamicAttributes, body);
	    // Set the template name in mailConfigDTO
	    mailConfigDTO.setTemplateName(fileName);
	    
	    if(null != placeHolderMap && placeHolderMap.size() >0) {
	    	mailConfigDTO.setPlaceHolderMap(placeHolderMap);
	    }
	    
	    // Send mail using the template
	    mailUtils.sendMailFromTemplate(subject, mailConfigDTO);
	    
	    // Delete the temporary HTML file
	    htmlFile.delete();
	}

	
	private String getDynamicValue(Map<String, Object> dynamicAttributes,String value) {
		if(value.matches("\\{([^}]*)\\}")) {
			Object obj = dynamicAttributes.get(value.replace("{", "").replace("}", ""));
			if(null!= obj) {
				return obj.toString();
			}
			else {
				return null;
			}
		}else {
			return value;
		}
	}
	
	 public String populateDynamicValues(Map<String, Object> dynamicAttributes, String input) {
	        StringBuilder resultString = new StringBuilder();
	        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
	        Matcher matcher = pattern.matcher(input);
	        
	        while (matcher.find()) {
	            String placeholderKey = matcher.group(1); // Extract the placeholder key
	            Object replacement = dynamicAttributes.get(placeholderKey);
	            if (replacement != null) {
	                matcher.appendReplacement(resultString, replacement.toString());
	            }
	        }
	        matcher.appendTail(resultString);

	        return resultString.toString();
	    }
	 
	 public void populatePlaceHolderValues(Map<String,Object> placeHolderMap,Map<String, Object> dynamicAttributes, String input) {
		 Pattern pattern = Pattern.compile("\\$\\{(?:'([^']*)'|([^'}]*))\\}");

         // Match the pattern against the HTML content
         Matcher matcher = pattern.matcher(input);

         // Store matched content in a list
         while (matcher.find()) {
             String content;
             if (matcher.group(1) != null) {
                 content = matcher.group(1).replace("{", "").replace("}", "").replace("$",""); // Content enclosed in single quotes
             } else {
                 content = matcher.group(2).replace("{", "").replace("}", "").replace("$","");; // Content without single quotes
             }
             Object replacement = dynamicAttributes.get(content);
             //check for null
             placeHolderMap.put(content, replacement);
         }
		 
	 }
	 private void logFailedSendTask(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest) throws Exception {
			processTransactionService.postTransactionFailed(processInstanceRequest.getProcessInstanceId(),
					processNodeInfoId, processInstanceRequest.getUserName(),ProcessStatusConstant.FAILED.getCode());
			processComplete.updateToFailed(processNodeInfoId, processInstanceRequest.getProcessInstanceId(),
					processInstanceRequest.getUserName());
		}
	 
	 
		private void logFailedSendTaskForWorkflow(int processNodeInfoId, WorkflowRequest workflowRequest) throws Exception {
			processTransactionService.postTransactionFailed(workflowRequest.getProcessInstanceId(),
					processNodeInfoId, workflowRequest.getUserName(), ProcessStatusConstant.FAILED.getCode());
			processComplete.updateToFailed(processNodeInfoId, workflowRequest.getProcessInstanceId(),
					workflowRequest.getUserName());
			
		}
}
