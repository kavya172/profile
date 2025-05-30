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
import com.galaxe.gxworkflow.dto.ProcessDTO;
import com.galaxe.gxworkflow.entity.Process;

/**
 * The ProcessMapper class is responsible for converting ProcessDTO into Process
 * entity class and Vice versa.
 *
 */
@Component
public class ProcessMapper {

	/**
	 * Transforming the List of entity data of type {@link Process} into List of DTO of type {@link ProcessDTO}. 
	 * 
	 * @param processes, type of List of {@link Process}.
	 * 
	 * @return  List of {@link ProcessDTO}. 
	 */
	public List<ProcessDTO> toListDto(List<Process> processes) {
		List<Process> sortedProcesses = processes.stream()
		        .sorted(Comparator.comparing(Process::getModifiedOn).reversed())
		        .collect(Collectors.toList());
		List<ProcessDTO> processDTODtos = new ArrayList<>();
		for (Process process : sortedProcesses) {
			ProcessDTO processDTO = new ProcessDTO();
			processDTO.setId(process.getId());
			processDTO.setProcessName(process.getProcessName());
			processDTO.setModifiedBy(process.getModifiedBy());
			processDTO.setModifiedOn(getFormattedDateString(process.getModifiedOn()));
			//FIXME:
			//processDTO.setIsActive(process.getIsActive());
			//processDTO.setVersion(process.getVersion());
			processDTODtos.add(processDTO);
		}
		return processDTODtos;
	}

	/**
	 * Transforming the entity data of type {@link Process} into DTO of type {@link ProcessDTO}. 
	 * 
	 * @param process, type of {@link Process}.
	 * 
	 * @return {@link ProcessDTO}. 
	 */
	public ProcessDTO toDto(Process process) {

		ProcessDTO processDTO = new ProcessDTO();
		processDTO.setProcessName(process.getProcessName());
		return processDTO;
	}
	
//	method to convert the timestamp date to String 	
	private String getFormattedDateString(Timestamp effectiveStartDateTimestamp) {
	    Date effectiveStartDate = new Date(effectiveStartDateTimestamp.getTime());
	    DateFormat dateTimeFormat = new SimpleDateFormat(WorkflowConstant.TIMESTAMP_FORMAT_MMDDYYYY_HHMM_A);
	    return dateTimeFormat.format(effectiveStartDate);
	}

}
