package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.ProcessVariables;
import com.galaxe.gxworkflow.entity.SendVariableEmail;
import com.galaxe.gxworkflow.entity.SendVariables;


@Repository
public interface SendVariableEmailRepository extends JpaRepository<SendVariableEmail, Integer> {

	@Modifying
	@Transactional
	@Query(value = "delete from sendvariableemails where sendvariableid \r\n"
			+ "in (select id from sendvariables where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in\r\n"
			+ "	(Select id from processversion where processid in ?1)))", nativeQuery = true)
	void deleteAllByProcessIds(List<Integer> processIds);
	
	
	List<SendVariableEmail> findBySendVariableId(SendVariables sendVariableId);

	@Modifying
	@Transactional
	@Query(value = "delete from sendvariableemails where sendvariableid \r\n"
			+ "in (select id from sendvariables where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in ?1))", nativeQuery = true)
	void deleteAllByProcessVersionId(List<Integer> processVersionIds);
	
	@Modifying
	@Transactional
	@Query(value = "delete from sendvariableemails where sendvariableid in (select id from sendvariables where processnodeinfoid =?1)", nativeQuery = true)
	void deleteAllByNodeInfoId(int nodeInfoId);
	
}
