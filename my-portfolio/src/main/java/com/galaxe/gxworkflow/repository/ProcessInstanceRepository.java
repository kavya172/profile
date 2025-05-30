package com.galaxe.gxworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.entity.ProcessVersion;

@Repository(value = "processInstanceRepository")
public interface ProcessInstanceRepository extends JpaRepository<ProcessInstance, Integer> {

//	public ProcessInstance findByProcessId(Process process);
//
//	@Query(value = "SELECT * from processinstance where processid=?1", nativeQuery = true)
//	public ProcessInstance findByProcessId(int processid);

	public List<ProcessInstance> findProcessInstanceByVersionId(ProcessVersion processVersion);
	
	public ProcessInstance findByVersionId(ProcessVersion processVersion);

	@Query(value = "SELECT * from processinstance where versionId in (:processVerIds)", nativeQuery = true)
	public List<ProcessInstance> findAllByVersionIds(List<Integer> processVerIds);

//	@Query(value = "SELECT * from processinstance where versionid=?1", nativeQuery = true)
//	public ProcessInstance findByVersionId(int versionId);

}
