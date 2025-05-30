package com.galaxe.gxworkflow.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.galaxe.gxworkflow.entity.ProcessVariablesDetails;

@Repository
public interface ProcessVariableDetailsRepository extends JpaRepository<ProcessVariablesDetails, Integer> {

	@Query(value = "select value from processvariabledetails where processvariableid in (select id from processvariables where processnodeinfoid=?1 and value =?2)", nativeQuery = true)
	public String getValueforProcessVariable(int processNodeInfoId, String variable);
	
	@Query(value = "Select pvd.value from processvariabledetails pvd\r\n"
			+ "			 inner join processInstancenodeinfo pini on pvd.processinstancenodeinfoid = pini.id\r\n"
			+ "			 inner join processvariables  pv on pv.id = pvd.processvariableid\r\n"
			+ "			where pini.processinstanceid = ?1 and pv.name = ?2 order by pvd.modifiedby desc limit 1", nativeQuery = true)
	public Object getValueforProcessVariableByProcessVariableName(int processinstanceid, String variable);

	@Query(value = "SELECT pv.name, pv.value as pvValue, pd.value as pdValue FROM processvariables pv INNER JOIN processvariabledetails pd on pv.id = pd.processvariableid WHERE pd.processinstanceNodeInfoId =?1", nativeQuery = true)
	public List<Object[]> getProcessVariablesByProcessInstanceId(int processinstanceNodeInfoId);

	@Query(value = "select pv.name, pv.value ,pd.value as pdValue from processvariables pv INNER JOIN processvariabledetails pd on pv.id = pd.processvariableid where pv.variabletypeid in (select id from variabletype where name =?2) and pd.processinstancenodeinfoid=?1", nativeQuery = true)
	public List<Object[]> getProcessVariableDetailsByProcessInstanceId(int processinstanceid, String variableName);
	
//	@Query(value = "select value from processVariableDetails where processvariableid in (select id from processVariables where processid =?1 and name=?2)", nativeQuery = true)
//	public String getvalueForVariable(int processId, String variableName);
	
	@Query(value = "SELECT  pv.name, pv.value as pvValue, pd.value,\r\n"
			+ "pd.id as pdValue FROM processvariables pv INNER JOIN \r\n"
			+ "processvariabledetails pd on pv.id = pd.processvariableid\r\n"
			+ "WHERE pv.variabletypeid \r\n"
			+ "in (select id from variabletype where name=?3 )\r\n"
			+ "and pv.processnodeinfoid=?2 and pd.processinstancenodeinfoid\r\n"
			+ "in (select id from processinstancenodeinfo where processinstanceid=?1)", nativeQuery = true)
	public List<Object[]> getValueforScriptTaskOutputVariable(int processInstanceId, int processnodeinfoid,
			String variableType);

	@Transactional
	@Modifying
	@Query(value = "UPDATE processvariabledetails SET value = ?1 WHERE id =?2", nativeQuery = true)
	void updateProcessVariablesDetailsvalues(Object object, Integer id);
}
