package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessVersion;

@Repository
public interface ProcessVersionRepository extends JpaRepository<ProcessVersion, Integer> {

	ProcessVersion findByIdAndProcessId(int versionId, Process process);

	@Query(value="select * from processversion where processId in (:processIds)", nativeQuery = true)
	List<ProcessVersion> findAllByProcessIds(List<Integer> processIds);
	
	@Query(value="select * from processversion pv\r\n"
			+ "inner join process p on p.id = pv.processid\r\n"
			+ "where p.processname = :processName \r\n"
			+ "and pv.versionnumber = :versionNumber ", nativeQuery = true)
	ProcessVersion findByProcessNameAndVersionNumber(String processName, int versionNumber);

	@Modifying
	@Transactional
	@Query(value = "delete from processversion where processid in ?1", nativeQuery = true)
	void deleteAllByProcessIds(List<Integer> processIds);


	@Modifying
	@Transactional
	@Query(value = "delete from processversion where id in ?1", nativeQuery = true)
	void deleteAllByProcessVersionId(List<Integer> processVersionIds);

	boolean findIsactiveById(Integer versionId);

//	ProcessVersion findByVersionId(Integer versionId);

	
}
