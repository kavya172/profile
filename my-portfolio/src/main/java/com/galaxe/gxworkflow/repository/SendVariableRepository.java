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
import com.galaxe.gxworkflow.entity.SendVariables;


@Repository
public interface SendVariableRepository extends JpaRepository<SendVariables, Integer> {

//	@Transactional
//	int deleteByProcessId(Process process);
	
	SendVariables findByProcessNodeInfoId(ProcessNodeInfo processNodeInfo);
	
	//FIXME
	 @Modifying
	 @Transactional
	 @Query(value = "delete from sendvariables where processnodeinfoid \r\n"
	 		+ "in (select id from processnodeinfo where versionid in\r\n"
	 		+ "	(Select id from processversion where processid in ?1))", nativeQuery = true)
	 public void deleteAllByProcessIds(List<Integer> processId);

	@Modifying
	@Transactional
	@Query(value = "delete from sendvariables where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in ?1)", nativeQuery = true)
	void deleteAllByProcessVersionId(List<Integer> processVersionIds);

	@Modifying
	@Transactional
	@Query(value = "delete from sendvariables where processnodeinfoid =?1", nativeQuery = true)
	void deleteAllByNodeInfoId(int nodeInfoId);
	
//	SendVariables findByProcessId(Process process);
	
	@Query(value = "select * from sendvariables where processnodeinfoid= ?1", nativeQuery = true)
	public List<SendVariables> getSendVariablesByNodeInfoId(int nodeInfoId);
}
	

