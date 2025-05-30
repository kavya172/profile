package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.ProcessUsers;
import com.galaxe.gxworkflow.entity.ProcessVersion;
import com.galaxe.gxworkflow.entity.SendVariables;


@Repository(value = "processUsersRepository")
public interface ProcessUsersRepository extends JpaRepository<ProcessUsers, Integer> {

//	@Transactional
//	void deleteByVersionId(ProcessVersion version);
	
	@Modifying
	@Transactional
	@Query(value = "delete from processusers where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in\r\n"
			+ "	(Select id from processversion where processid in ?1))", nativeQuery = true)
	void deleteAllByProcessIds(List<Integer> processId);
	
	@Modifying
	@Transactional
	@Query(value = "delete from processusers where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in ?1)", nativeQuery = true)
	void deleteAllByProcessVersionId(List<Integer> processVersionIds);
	
	@Modifying
	@Transactional
	@Query(value = "delete from processusers where processnodeinfoid =?1", nativeQuery = true)
	void deleteAllByNodeInfoId(int nodeInfoId);
	
	@Query(value = "select * from processusers where processnodeinfoid= ?1", nativeQuery = true)
	public List<ProcessUsers> getUsersByNodeInfoId(int nodeInfoId);
}
