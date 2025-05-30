package com.galaxe.gxworkflow.mapper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.galaxe.gxworkflow.constants.WorkflowConstant;
import com.galaxe.gxworkflow.dto.NodeInstanceTransactionDTO;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;

@Component
public class ProcessInstanceNodeInfoMapper {

	public List<NodeInstanceTransactionDTO> toTransactionListDto(List<ProcessInstanceNodeInfo> processNodeInstanceInfos) {
		List<NodeInstanceTransactionDTO> nodeInstanceTransactionDTOs = new ArrayList<>();
		for (ProcessInstanceNodeInfo processNodeInstanceInfo : processNodeInstanceInfos) {
			NodeInstanceTransactionDTO nodeInstanceTransactionDTO = new NodeInstanceTransactionDTO();
			nodeInstanceTransactionDTO.setId(processNodeInstanceInfo.getId());
			nodeInstanceTransactionDTO.setDescription(processNodeInstanceInfo.getStatusId().getDescription());
			nodeInstanceTransactionDTO.setCreatedBy(processNodeInstanceInfo.getProcessInstanceId().getCreatedBy());
			nodeInstanceTransactionDTO.setCreatedOn(getFormattedDateString(processNodeInstanceInfo.getProcessInstanceId().getCreatedOn()));
			nodeInstanceTransactionDTOs.add(nodeInstanceTransactionDTO);
		}
		return nodeInstanceTransactionDTOs;
	}
	
	private String getFormattedDateString(Timestamp effectiveStartDateTimestamp) {
	    Date effectiveStartDate = new Date(effectiveStartDateTimestamp.getTime());
	    DateFormat dateTimeFormat = new SimpleDateFormat(WorkflowConstant.TIMESTAMP_FORMAT_MMDDYYYY_HHMM_A);
	    return dateTimeFormat.format(effectiveStartDate);
	}
}
