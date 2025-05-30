package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.ProcessSequenceDetails;
import com.galaxe.gxworkflow.entity.ProcessVersion;

@Repository(value = "processFlowDetailsRepository")
public interface ProcessFlowDetailsRepository extends JpaRepository<ProcessSequenceDetails, Integer> {
	
	//FIXME: REMOVE SCHEMA NAME FROM QUERY
   @Modifying
   @Transactional
   @Query(value = "DELETE FROM Processsequencedetails WHERE versionId in (select versionId from processversion where processId in ?1)", nativeQuery = true)
   public void deleteAllByProcessIds(List<Integer> processId);
	
   @Transactional
   public void deleteByVersionId(ProcessVersion versionId);
//   
//   @Query(value = "select * from  workflowdev.processflowdetails where processflowdetails.processsequenceId=?1", nativeQuery = true) 
//   public ProcessSequenceDetails getTargetNodeBySequenceId(int processSequenceId);
//   
//   @Query(value = "select * from  workflowdev.processflowdetails where sourcenodeid=?1 and processid=?2", nativeQuery = true) 
//   public ProcessSequenceDetails getTargetNodeBySequenceId(int sourceNodeId, int processId);

 }
