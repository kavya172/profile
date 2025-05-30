package com.galaxe.gxworkflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessInstanceNodeInfo;
import java.util.List;
import com.galaxe.gxworkflow.entity.ProcessInstance;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;



/**
 * The processInstanceRepository interface provides data access operations for managing {@link Process} entity.
 *
 * <p>
 * This repository interface defines methods to perform CRUD operations on the {@link Process} entity stored in the underlying data store, such as a database.
 *
 */
@Repository(value = "processNodeInstanceInfoRepository")
public interface ProcessInstanceNodeInfoRepository extends JpaRepository<ProcessInstanceNodeInfo, Integer> {

	ProcessInstanceNodeInfo findByProcessInstanceIdAndProcessNodeInfoId (ProcessInstance processInstanceId, ProcessNodeInfo processNodeInfoId);
	
	//FIXME: REMOVE STATUSID = 4 HARD CODED 
	@Query(value = "select targetnodeid,  nt.name, processsequenceid \r\n"
			+ "from processsequencedetails pfd\r\n"
			+ "inner join processnodeinfo pni on pni.id = pfd.targetnodeid\r\n"
			+ "inner join nodetype nt on nt.id = pni.nodetypeid\r\n"
			+ "where sourcenodeid = \r\n"
			+ "(select processnodeinfoid from processinstancenodeinfo\r\n"
			+ "where processinstanceid =?1 and statusid=4 \r\n"
			+ "order by id desc\r\n"
			+ "limit 1)", nativeQuery = true)
	List<Object[]> findByProcessInstanceIdAndStatusId(int processInstanceId);
	
	//FIXME: REMOVE HARD CODED 4 IN THE QUERY.
	@Query(value = "SELECT *\r\n"
			+ "FROM processinstancenodeinfo pin\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT processnodeinfoid, MAX(modifiedon) AS latest_modified\r\n"
			+ "    FROM processinstancenodeinfo\r\n"
			+ "    WHERE processinstanceid = ?1\r\n"
			+ "    GROUP BY processnodeinfoid\r\n"
			+ ") AS sub ON pin.processnodeinfoid = sub.processnodeinfoid AND pin.modifiedon = sub.latest_modified\r\n"
			+ "WHERE pin.processinstanceid = ?1\r\n"
			+ "AND pin.statusid = 4\r\n"
			+ "ORDER BY pin.modifiedon DESC\r\n"
			+ "LIMIT 1",nativeQuery = true)
	List<ProcessInstanceNodeInfo> getProcessInfoFromProcessIdAndLatestExecutedNode(int processinstanceid);
  
	@Query(value = "select * from workflowtest.processinstancenodeinfo \r\n"
			+ "where processnodeinfoid  = ?2 and processinstanceid = ?1\r\n"
			+ "order by createdon desc\r\n"
			+ "limit 1",nativeQuery = true)
	ProcessInstanceNodeInfo findByProcessInstanceIdAndProcessNodeInfoIdForWorkflow(int processinstanceid,int processNodeInfoId);
  
	@Query(value = "select processnodeinfoid from processInstanceNodeinfo where processinstanceId = ?1 order by createdon desc limit 1",nativeQuery = true)
	int getProcessNodeInfoId(int processInstanceId);

	@Query(value = "	select processnodeinfoid from processinstancenodeinfo pini\r\n"
			+ "inner join processnodeinfo pni on pni.id = pini.processnodeinfoid\r\n"
			+ "inner join nodetype nt on pni.nodetypeid = nt.id\r\n"
			+ "where nt.id = 4 and pini.processinstanceid = ?1 and pini.statusid=5 \r\n"
			+ "order by pini.createdon desc limit 1", nativeQuery = true)
	Integer checkLatestRecord(int processInstanceId);

	@Query(value="select * from processinstancenodeinfo where processinstanceid = ?1 and statusId=?2 order by id desc limit 1",nativeQuery= true)
	ProcessInstanceNodeInfo getLatestTransaction(Integer processInstanceId,int statusId);
		// TODO Auto-generated method stub
		
//	@Query(value = "SELECT * FROM processinstancenodeinfo JOIN \r\n"
//			+ "processnodeinfo ON \r\n"
//			+ "processinstancenodeinfo.processnodeinfoid = processnodeinfo.nodeid\r\n"
//			+ "AND processinstancenodeinfo.processinstanceid = ?1\r\n"
//			+ "AND processnodeinfo.nodetypeid = ?2\r\n"
//			+ "AND CAST(processnodeinfo.nodexml AS VARCHAR) LIKE %?3%\r\n"
//			+ "ORDER BY processinstancenodeinfo.modifiedon ASC",nativeQuery = true)
//	List<ProcessInstanceNodeInfo> getNodeInfoTransactionDetails(int processinstanceid,int nodetypeid,String nodeId);
	
	
	
}