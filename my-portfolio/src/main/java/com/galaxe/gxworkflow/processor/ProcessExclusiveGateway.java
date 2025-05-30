package com.galaxe.gxworkflow.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.dto.TransactionInfo;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.repository.ProcessInstanceNodeInfoRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableDetailsRepository;
import com.galaxe.gxworkflow.repository.ProcessVariableRepository;
import com.galaxe.gxworkflow.services.ProcessInstanceService;
import com.galaxe.gxworkflow.util.Constants;

@Component
public class ProcessExclusiveGateway {

	@Autowired
	private ProcessInstanceNodeInfoRepository processNodeInstanceInfoRepository;

	@Autowired
	private ProcessInstanceService processInstanceService;

	@Autowired
	private ProcessVariableRepository processVariableRepository;

	@Autowired
	private ProcessVariableDetailsRepository processVariableDetailsRepository;

	public void createExclusiveGatewayTransaction(ProcessNodeInfo nodeInfo, TransactionInfo transactionInfo) {

		// get the Configuration Details
		String status = "";
		switch ("UserTask") {
		case Constants.USER_TASK:
			status = transactionInfo.getOriginalStatus();
			break;
		}

		// create task and task action
		transactionInfo.setStatus(ProcessStatusConstant.COMPLETED.getCode());
//		ProcessTasks taskDetails = processTasksService.createTask(nodeInfo.getNodeId(), transactionInfo);
//
//		processTasksActionService.createTaskAction(taskDetails.getTaskId(), transactionInfo);

		// node details filter with sequence name
//		ProcessNodeDetails nodeDetails = processNodeDetailsRepo.findByParentNodeIdAndSequenceName(nodeInfo, status);
//		if (!ObjectUtils.isEmpty(nodeDetails)) {

//			List<ProcessNodeDetails> childsInfo = new ArrayList<>();
//			childsInfo.add(nodeDetails);
//			transactionInfo.setStatus("INPROGRESS");
//			executorJob.executor(childsInfo, transactionInfo);
//		}

	}

	public void ExecuteExclusiveGateway(int processNodeInfoId, int processInstanceId, String createdBy,
			ProcessInstanceRequest processInstanceRequest) throws Exception {

		List<Object[]> collectChildNodes = processNodeInstanceInfoRepository
				.findByProcessInstanceIdAndStatusId(processInstanceId);
		if (!CollectionUtils.isEmpty(collectChildNodes)) {
			for (Object[] child : collectChildNodes) {
				String sequenceFlowCondition = processVariableRepository.getExpressionValueforSequence((int) child[2]);
				executeBasedonCondition(sequenceFlowCondition, child, processInstanceId, createdBy,
						processInstanceRequest);
			}
		}
	}

	private void executeBasedonCondition(String sequenceFlowCondition, Object[] child, int processInstanceId,
			String createdBy, ProcessInstanceRequest processInstanceRequest) throws Exception {
		char operator = '\0';
		int index = -1;

		if (sequenceFlowCondition.contains("=")) {
			operator = '=';
			index = sequenceFlowCondition.indexOf("=");
		} else if (sequenceFlowCondition.contains(">")) {
			operator = '>';
			index = sequenceFlowCondition.indexOf(">");
		} else if (sequenceFlowCondition.contains("<")) {
			operator = '<';
			index = sequenceFlowCondition.indexOf("<");
		}

		if (index != -1) {
			String variableToFind = sequenceFlowCondition.substring(0, index) + "}";
			String[] valueAssigned1 = StringUtils.split(sequenceFlowCondition, String.valueOf(operator)); //   {amount     100}	   // '>=', '<='
			String valueAssigned =  valueAssigned1[1].replace("}","");
			String actualValue = processVariableDetailsRepository
					.getValueforProcessVariable((int) child[0], variableToFind);

			switch (operator) {
			case '=':
				if (valueAssigned.equalsIgnoreCase(actualValue)) {
					processInstanceService.executeNextNodeBasedOnNodeType(child, processInstanceRequest);
				}
				break;
			case '>':
				if (Double.parseDouble(actualValue) >= Double.parseDouble(valueAssigned)) {
					processInstanceService.executeNextNodeBasedOnNodeType(child, processInstanceRequest);
				}
				break;
			case '<':
				if (Double.parseDouble(actualValue) <= Double.parseDouble(valueAssigned)) {
					processInstanceService.executeNextNodeBasedOnNodeType(child, processInstanceRequest);
				}
				break;
			default:
				break;
			}
		}

	}

}
