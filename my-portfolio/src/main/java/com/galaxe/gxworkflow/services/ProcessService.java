package com.galaxe.gxworkflow.services;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.galaxe.gxworkflow.dto.BpmnDTO;
import com.galaxe.gxworkflow.dto.NodeColorStatusDTO;
import com.galaxe.gxworkflow.dto.ProcessDTO;
import com.galaxe.gxworkflow.dto.ProcessResponseDTO;
import com.galaxe.gxworkflow.entity.Process;

/**
 * The ProcessService has a declaration of methods to perform process operation.
 * 
 */
public interface ProcessService {

	void saveProcessDiagram(String name, MultipartFile file, String createdBy, Boolean isIncrVersoin)
			throws ParserConfigurationException, SAXException, IOException, Exception;

	List<ProcessDTO> getAllProcesses();

	ProcessDTO getProcessNameById(Integer processId);
	
	public void updateProcess(int versionId, MultipartFile file, String modifiedBy) throws Exception;
	
	void deleteByProcessIds(List<Integer> processIds);
	
	BpmnDTO getProcessWorkflow(int processVersionId) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, Exception;
	
	List<NodeColorStatusDTO> getProcessInstanceStatus(int processId, int versionId) throws Exception;
	
	public Process findByProcessId(Integer processId) throws Exception;
	
	List<ProcessResponseDTO> getProcessNames(int onlyActive);
	
	List<ProcessResponseDTO> getProcessVersions(int processId) throws Exception;

	void deleteByProcessVersionIds(List<Integer> processVersionIds);

	List<Object[]> getProcessVersionNamesByProcessId(Integer processId) throws Exception;

	List<Object[]> findAllProcessWithVersion() throws Exception;
	
  BpmnDTO getProcessWorkflowForWorkflow(int processVersionId, int processInstanceId) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, Exception;
  
	List<String> getPossibleActions(int processInstanceId, int processVersionId, String userRole);
}
