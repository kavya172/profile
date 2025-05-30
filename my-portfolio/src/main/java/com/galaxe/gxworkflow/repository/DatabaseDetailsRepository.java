package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.DatabaseDetails;
import com.galaxe.gxworkflow.entity.ProcessRoles;

@Repository
public interface DatabaseDetailsRepository extends JpaRepository<DatabaseDetails, Integer> {

	@Query(value = "SELECT * FROM databasedetails where processnodeinfoid=?1", nativeQuery = true)
	public DatabaseDetails findByProcessNodeInfoId(int processNodeInfoId);

	@Modifying
	@Transactional
	@Query(value = "delete from databasedetails where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in ?1)", nativeQuery = true)
	void deleteAllByProcessVersionId(List<Integer> processVersionIds);

	@Modifying
	@Transactional
	@Query(value = "delete from databasedetails where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in\r\n"
			+ "	(Select id from processversion where processid in ?1))", nativeQuery = true)
	public void deleteAllByProcessIds(List<Integer> processIds);
	
	@Modifying
	@Transactional
	@Query(value = "delete from databasedetails where processnodeinfoid ", nativeQuery = true)
	public void deleteAllByNodeInfoId(int processNodeInfoId);
	
	@Query(value = "select * from databasedetails where processnodeinfoid= ?1", nativeQuery = true)
	public List<DatabaseDetails> getDatabaseDetailsByNodeInfoId(int nodeInfoId);

}
