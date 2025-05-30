package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.NodeStatusColor;
import com.galaxe.gxworkflow.entity.NodeType;
import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessStatus;
import com.galaxe.gxworkflow.entity.ProcessVersion;

/**
 * The ProcessNodeInfoRepository interface provides data access operations for managing {@link ProcessNodeInfo} entity.
 *
 * <p>
 * This repository interface defines methods to perform CRUD operations on the {@link ProcessNodeInfo} entity stored in the underlying data store, such as a database.
 *
 */
@Repository
public interface NodeStatusRepository extends JpaRepository<NodeStatusColor, Integer> {

	@Modifying
	@Transactional
	@Query(value = "delete from nodestatuscolor where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in\r\n"
			+ "	(Select id from processversion where processid in ?1))", nativeQuery = true)
	void deleteAllByProcessIds(List<Integer> processIds);

	@Modifying
	@Transactional
	@Query(value = "DELETE from nodestatuscolor where processnodeinfoid \r\n"
			+ "in (select id from processnodeinfo where versionid in ?1)", nativeQuery = true)
	void deleteAllByProcessVersionId(List<Integer> processVersionIds);

	NodeStatusColor findByProcessNodeInfoAndStatusId(ProcessNodeInfo scriptNode, int statusId);


//	NodeStatusColor findByProcessNodeInfoAndStatusId(ProcessNodeInfo processNodeInfoId, int id);
	
	@Query(value = "Select nsc.processnodeinfoid from nodeStatuscolor nsc\r\n"
			+ "inner join processnodeinfo pni on nsc.processnodeinfoid = pni.id\r\n"
			+ "inner join processinstance pi on pi.versionid = pni.versionid\r\n"
			+ "where pi.id =?1 and nsc.statusid = 4 and nsc.statusname = ?2", nativeQuery = true)
	Integer findProcessNodeIdForGivenStatusName(int processInstanceId, String targetStatusName);
	

	@Query(value = "DELETE from nodestatuscolor where processnodeinfoid= ?1", nativeQuery = true)
	void deleteAllByProcessNodeInfoId(int processNodeInfoId);

	@Query(value = "select * from nodestatuscolor where processnodeinfoid= ?1", nativeQuery = true)
	List<NodeStatusColor> getByProcessNodeInfoId(int processNodeInfoId);
	
}
