package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.NodeType;
import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessNodeInfo;
import com.galaxe.gxworkflow.entity.ProcessVersion;

/**
 * The ProcessNodeInfoRepository interface provides data access operations for managing {@link ProcessNodeInfo} entity.
 *
 * <p>
 * This repository interface defines methods to perform CRUD operations on the {@link ProcessNodeInfo} entity stored in the underlying data store, such as a database.
 *
 */
@Repository
public interface ProcessNodeInfoRepository extends JpaRepository<ProcessNodeInfo, Integer> {
	

	/**
	 * To retrieve the {@link ProcessNodeInfo} filtering by processId and nodeTypeId.
	 *
	 * @param processId  type of Integer, is Id of an {@link Process} Entity
	 * @param nodeTypeId type of NodeType, is a Id of an {@link NodeType} Entity
	 * @return  {@link ProcessNodeInfo}
	 */
	public ProcessNodeInfo findByVersionIdAndNodeTypeId(ProcessVersion versionId, NodeType nodeTypeId);

	/**
	 * 
	 * To delete a {@link ProcessNodeInfo} filtering by processId.
	 *
	 *  @param processId  type of Integer, is Id of an {@link Process} Entity
	 */
	@Transactional
//	public void deleteByVersionId(ProcessVersion versionId);

	



	/**
	 * To retrieves the {@link ProcessNodeInfo}, filtering by nodeTypeId and list of processId.
	 * 
	 * In keyword operates bulk delete operation for processIds.
	 *
	 * @param processIds type of List<Integer>, is Id of an {@link Process} Entity
	 * @param nodetype type of NodeType, is a Id of an {@link NodeType} Entity
	 * @return list of {@link ProcessNodeInfo}
	 */
	//List<ProcessNodeInfo> findByProcessIdProcessIdInAndNodeTypeId(List<Integer> processIds, NodeType nodeType);
	
	//FIXME: USE NAMED PARAMETER INSTEAD OF NUMBERS
//	@Modifying
	@Query(value = "INSERT INTO processnodeinfo (nodetypeid, versionId, nodexml, createdon, createdby, modifiedby, modifiedon, nodeid, isactive) VALUES (?1, ?2, CAST(?3 AS XML), ?4, ?5, ?6, ?7, ?8, ?9) returning *", nativeQuery = true)
	ProcessNodeInfo saveProcessNodeInfo(int nodeTypeId, int versionId, String nodexml, java.sql.Timestamp time, String createdBy, String modifiedBy, java.sql.Timestamp time2,String nodeid, boolean active);
	
	//FIXME: REMOVE HARD CODED SCHEMA NAME
	@Query(value = "SELECT pi.nodetypeid, \r\n"
			+ "	nt.name,\r\n"
			+ "	p.id as processid,\r\n"
			+ "	pv.id as versionid,\r\n"
			+ "	p.processname as processname,\r\n"
			+ "	pv.versionnumber,\r\n"
			+ "	\r\n"
			+ "	cast(pi.nodexml as TEXT) AS nodexml,\r\n"
			+ "	pv.createdon,\r\n"
			+ "	pv.createdby,\r\n"
			+ "	pv.modifiedby,\r\n"
			+ "	pv.modifiedon,\r\n"
			+ "(select max(versionnumber) from processversion where processid = p.id)\r\n"
			+ "			FROM processnodeinfo pi \r\n"
			+ "	inner join nodetype nt on nt.id = pi.nodetypeid\r\n"
			+ "			inner join processversion pv on pv.id = pi.versionid\r\n"
			+ "			inner join process p on p.id = pv.processid\r\n"
			+ "			where pv.id = :versionId order by \r\n"
			+ "    nt.order", nativeQuery = true)
	List<Object[]> getProcessByVersionId(int versionId);
	
//	@Query(value = "SELECT nodeid,nodetypeid,processid,cast(nodexml as TEXT) AS nodexml,createdon,createdby,modifiedby,modifiedon FROM processnodeinfo WHERE processid = ?1 and nodetypeid = ?2", nativeQuery = true)
//	List<Object[]> getAllNodesByProcessIdAndNodeTypeId(int processId, int nodeTypeId);
	
	//FIXME: REMOVE HARD CODED SCHEMA NAME
	@Modifying
	@Transactional
	@Query(value = "DELETE  from processnodeinfo where  versionid in\r\n"
			+ "	(Select id from processversion where processid in ?1)", nativeQuery = true)
	void deleteAllByProcessIds(List<Integer> processId);


	@Query(value = "SELECT cast(nodexml as TEXT) AS nodexml FROM processnodeinfo WHERE id = ?1", nativeQuery = true)
	public String getNodeXmlByNodeId(int nodeId);
	
	@Query(value = "SELECT * FROM processnodeinfo WHERE nodeid = ?1", nativeQuery = true)
	public ProcessNodeInfo getNodeInfoByNodeId(int nodeId);

	
	@Query(value = "select pv.isactive \r\n"
			+ "						,btrim((xpath('//gxw:userDetails/@allUsers' 		  	\r\n"
			+ "									, pni.nodexml\r\n"
			+ "								 	, array[array['gxw', 'http://custom.com/ns']]\r\n"
			+ "								  \r\n"
			+ "						))[1]\\:\\:text, '{}') as allUsers\r\n"
			+ "						,btrim((xpath('//gxw:userDetails/@name' 		  	\r\n"
			+ "									, pni.nodexml\r\n"
			+ "								 	, array[array['gxw', 'http://custom.com/ns']]\r\n"
			+ "								  \r\n"
			+ "						))[1]\\:\\:text, '{}') as names\r\n"
			+ "						,btrim((xpath('//gxw:userDetails/@role' 		  	\r\n"
			+ "									, pni.nodexml\r\n"
			+ "								 	, array[array['gxw', 'http://custom.com/ns']]\r\n"
			+ "								  \r\n"
			+ "						))[1]\\:\\:text, '{}') as roles \r\n"
			+ "			from process p\r\n"
			+ "			inner join processversion pv on p.id = pv.processid\r\n"
			+ "			inner join processnodeinfo pni on pni.versionId = pv.id\r\n"
			+ "			where p.processname=:processName \r\n"
			+ "			and pv.versionnumber=:versionNumber \r\n"
			+ "			and pni.nodetypeid=:nodeTypeId ", nativeQuery = true)
	public Object[] getProcessInformationForVersionId(String processName, int versionNumber, int nodeTypeId);
	
	
	@Query(value="SELECT isactive,\r\n"
			+ "			      btrim((xpath('//gxw:userDetails/@allUsers', \r\n"
			+ "			                  pni.nodexml, \r\n"
			+ "			                  ARRAY[ARRAY['gxw', 'http://custom.com/ns']] \r\n"
			+ "			                  ))[1]\\:\\:text, '{}') AS allUsers, \r\n"
			+ "			      btrim((xpath('//gxw:userDetails/@name', \r\n"
			+ "			                  pni.nodexml, \r\n"
			+ "			                  ARRAY[ARRAY['gxw', 'http://custom.com/ns']] \r\n"
			+ "			                  ))[1]\\:\\:text, '{}') AS names, \r\n"
			+ "			      btrim((xpath('//gxw:userDetails/@role', \r\n"
			+ "			                  pni.nodexml, \r\n"
			+ "			                  ARRAY[ARRAY['gxw', 'http://custom.com/ns']] \r\n"
			+ "			                  ))[1]\\:\\:text, '{}') AS roles \r\n"
			+ "			   FROM process p \r\n"
			+ "			   INNER JOIN processversion pv ON p.id = pv.processid \r\n"
			+ "			   INNER JOIN processnodeinfo pni ON pni.versionId = pv.id \r\n"
			+ "			   WHERE  \r\n"
			+ "			    pv.Id = ?1\r\n"
			+ "			   AND pni.nodetypeid = ?2",nativeQuery=true)
	public Object[] getProcessInformationForVersionIdWorkflow(Integer versionId, int nodeTypeId);
	
	@Query(value = "SELECT * FROM processnodeinfo WHERE versionId = ?1 and nodetypeid=?2", nativeQuery=true)
	List<ProcessNodeInfo> findByVersionIdAndNodeTypeId(int versionId, int nodeTypeId);
	
	@Query(value="select pu.emailid , pr.rolename \r\n"
			+ "			from Processnodeinfo pni\r\n"
			+ "			inner join processroles pr on pr.processnodeinfoid =  pni.id\r\n"
			+ "			inner join processusers pu on pu.processnodeinfoid = pni.id\r\n"
			+ "			where pni.id = ?1",nativeQuery = true)
	public  List<Object[]> getUserDetailsByProcessNodeInfoId(int processNodeInfoId);
	
	@Query(value="select pv.value from Processnodeinfo pni\r\n"
			+ "					inner join processvariables pv on pv.processnodeinfoid = pni.id\r\n"
			+ "					and pv.name = 'assignAll'\r\n"
			+ "					where pni.id = ?1",nativeQuery = true)
	public String getUserNodeAssignAllDetailsForGivenProcessNodeInfoId(int processNodeInfoId);
	
	@Query(value="Select cast(nodexml as TEXT) from processNodeInfo where versionId = ?1  and nodetypeid in (Select id from nodetype where name = ?2 )",nativeQuery = true)
	String getProcessInfoFromVersionIdAndNodeType(int versionId, String nodeTypeName);

	@Query(value = "SELECT * FROM processnodeinfo where nodeid = ?1", nativeQuery = true)
	ProcessNodeInfo getprocessNodeInfoById(int nodeid);

	@Query(value="Select pv.name, pv.value from process p\r\n"
			+ "inner join processvariables pv on p.id = pv.processid\r\n"
			+ "inner join Processnodeinfo pni on p.id = pni.processid and \r\n"
			+ "								pv.processnodeinfoid = pni.id and \r\n"
			+ "								pv.name  not in  ('assignAll')\r\n"
			+ "where p.id = ?1 and pni.id = ?2",nativeQuery = true)
	public List<Object[]> getAllProcessVariables(Integer processId, int processNodeInfoId);

	@Modifying
	@Transactional
	@Query(value = "DELETE  from processnodeinfo where  versionid in ?1", nativeQuery = true)
	public void deleteAllByProcessVersionId(List<Integer> processVersionIds);

	@Query(value="SELECT pi.nodetypeid,  \r\n"
			+ " nt.name, \r\n"
			+ " p.id as processid, \r\n"
			+ " pv.id as versionid, \r\n"
			+ " p.processname as processname, \r\n"
			+ " pv.versionnumber, \r\n"
			+ "  \r\n"
			+ " cast(pi.nodexml as TEXT) AS nodexml, \r\n"
			+ " pv.createdon, \r\n"
			+ " pv.createdby, \r\n"
			+ " pv.modifiedby, \r\n"
			+ " pv.modifiedon, \r\n"
			+ "			 (select max(versionnumber) from processversion where processid = p.id) ,\r\n"
			+ "			 pi.id\r\n"
			+ " 		FROM processnodeinfo pi  \r\n"
			+ " inner join nodetype nt on nt.id = pi.nodetypeid \r\n"
			+ " 		inner join processversion pv on pv.id = pi.versionid \r\n"
			+ " 		inner join process p on p.id = pv.processid \r\n"
			+ " 		where pv.id = ?1 order by  \r\n"
			+ "			     nt.order",nativeQuery = true)
	public List<Object[]> getProcessByVersionIdForWorkflow(int processVersionId);
	
	
	@Query(value="Select pni.id from processnodeinfo pni\r\n"
			+ "inner join processVersion pv on pv.id = pni.versionid\r\n"
			+ "inner join processInstance pi on pi.versionId = pv.id\r\n"
			+ "inner join nodetype nt on nt.id = pni.nodetypeid \r\n"
			+ "where nt.name = ?2 and pi.id = ?1",nativeQuery = true)
	public Integer getProcessNodeInfoIdByProcessInstancIdAndNodeType(int processInstanceId, String nodeType);
	
	@Query(value = "SELECT id, nodetypeid, cast(nodexml as TEXT), versionid, createdon, createdby, modifiedby, modifiedon, nodeid, isactive FROM processnodeinfo where versionid =?1", nativeQuery = true)
	public List<Object[]> getNodeInfoByVersionId(int processVersionId);
	
//	@Modifying
//	@Query(value = "UPDATE processnodeinfo set nodetypeid=?1, versionid=?2, nodexml=CAST(?3 AS XML), createdon=?4, createdby=?5, modifiedby=?6, modifiedon=?7, nodeid=?7, isactive=?9 where id= ?10", nativeQuery = true)
//	void updateProcessNodeInfo(int nodeTypeId, int versionId, String nodexml, java.sql.Timestamp time, String createdBy, String modifiedBy, java.sql.Timestamp time2,String nodeid, boolean active, int nodeinfoId);

	@Modifying
	@Query(value = "UPDATE processnodeinfo set nodexml=CAST(?1 AS XML), modifiedby=?2, modifiedon=?3, nodeid=?4, isactive=?5 where id= ?6", nativeQuery = true)
	void updateProcessNodeInfo(String nodexml, String modifiedBy, java.sql.Timestamp time2,String nodeid, boolean active, int nodeinfoId);

	
}
;