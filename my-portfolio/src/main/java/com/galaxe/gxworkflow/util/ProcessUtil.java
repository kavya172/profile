package com.galaxe.gxworkflow.util;

import java.util.List;

import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.galaxe.gxworkflow.dto.TransactionInfo;
import com.galaxe.gxworkflow.entity.NodeType;

/**
 * The ProcessUtil class provides utility methods for process operations.
 * 
 */
@Component
public class ProcessUtil {

	/**
	 * Retrieve type of node using flowElement and nodeType.
	 *
	 * @param flowElement type of FlowElement, object containing the elements of a BPMN diagram
	 * @param nodeType 	  type of List<NodeType>, is a Id of an Relational entity {@link NodeType} Entity
	 * @return {@link NodeType}
	 */
	public static NodeType getNodeType(FlowElement flowElement, List<NodeType> nodeType) {

		String elementType = flowElement.getElementType().getTypeName();
		return nodeType.stream().filter(node -> node.getName().equalsIgnoreCase(elementType)).findFirst().get();
	}

	/**
	 * To validate the RequestData with the provided request and isAllFields.
	 * 
	 * If isAllFields = true, we need to check for all the request data or Else we need to check notNull condition only for processId and workflowId. 
	 * 
	 *
	 * @param request type of {@link TransactionInfo} object containing the process ID and additional data
	 * @param isAllFields type of boolean
	 * @return {@code true} or {@code false}
	 */
	public static boolean checkRequestData(TransactionInfo request, boolean isAllFields) {
		boolean isValid = false;

		isValid = (request.getProcessId() != 0 && request.getWorkFlowId() != 0 && !StringUtils.isEmpty(request.getTransUser().trim()));

		if (isValid && isAllFields) {
			isValid= (!StringUtils.isEmpty(request.getName().trim()) && !StringUtils.isEmpty(request.getNotes().trim())
					&& !StringUtils.isEmpty(request.getStatus().trim())) ;
		}
		return isValid;
	}
}
