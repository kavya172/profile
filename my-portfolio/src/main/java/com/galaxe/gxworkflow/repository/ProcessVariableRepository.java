package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessVariables;
import com.galaxe.gxworkflow.entity.ProcessVersion;


@Repository
public interface ProcessVariableRepository extends JpaRepository<ProcessVariables, Integer> {

	@Transactional
	void deleteById(ProcessVersion versionId);
	
	List<ProcessVariables> findByProcessNodeInfoId(ProcessNodeInfo processNodeInfo);
	
	@Query(value = "select value from processVariables where processnodeinfoid=?1", nativeQuery = true)
	String getExpressionValueforSequence(int processnodeinfoid);
	
	@Modifying
	@Transactional
	@Query(value = "delete from processvariables where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in\r\n"
			+ "	(Select id from processversion where processid in ?1))", nativeQuery = true)
	public void deleteAllByProcessIds(List<Integer> processIds);

	@Modifying
	@Transactional
	@Query(value = "delete from processvariables where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in ?1)", nativeQuery = true)
	void deleteAllByProcessVersionId(List<Integer> processVersionIds);

	ProcessVariables findByProcessNodeInfoIdIdAndName(int processNodeInfoId, String name);
	
	@Query(value = "select * from processVariables where processnodeinfoid in (select id from workflowtest.processnodeinfo where versionId = ?1) "
			+ "and variabletypeid in (select id from workflowtest.variableType where name = ?2)",nativeQuery = true)
	List<ProcessVariables> getProcessVariablesByNodeIdAndVariableType(int processNodeId,String variableType);
	
	@Modifying
	@Query(value = "UPDATE processvariables SET value=?2,modifiedby=?3, modifiedon=?4 where id= ?1", nativeQuery = true)
	void updateProcessVariable(int id, String Value,String modifiedBy, java.sql.Timestamp time);
	
}
