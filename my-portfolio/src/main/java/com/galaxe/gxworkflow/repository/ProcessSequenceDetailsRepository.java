package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.ProcessSequenceDetails;


@Repository
public interface ProcessSequenceDetailsRepository extends JpaRepository<ProcessSequenceDetails, Integer> {

	@Modifying
	@Transactional
	@Query(value = "delete from processsequencedetails where versionid in\r\n"
			+ "	(Select id from processversion where processid in ?1)", nativeQuery = true)
	public void deleteAllByProcessIds(List<Integer> processIds);

	@Modifying
	@Transactional
	@Query(value = "delete from processsequencedetails where versionid in ?1", nativeQuery = true)
	public void deleteAllByProcessVersionId(List<Integer> processVersionIds);
	
	@Query(value = "select * from processsequencedetails where versionid = ?1", nativeQuery = true)
	public List<ProcessSequenceDetails> getProcessSequenceDetailsByVersionid(int processVersionId);
	
}
