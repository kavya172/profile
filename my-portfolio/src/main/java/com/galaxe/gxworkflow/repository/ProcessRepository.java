package com.galaxe.gxworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.Process;

/**
 * The ProcessRepository interface provides data access operations for managing {@link Process} entity.
 *
 * <p>
 * This repository interface defines methods to perform CRUD operations on the {@link Process} entity stored in the underlying data store, such as a database.
 *
 */
@Repository(value = "processRepository")
public interface ProcessRepository extends JpaRepository<Process, Integer> {

	/**
     * To check the existence of process based on the processName.
     *
     * @param processName type of string, is a name of the process.
     * @return the boolean value {@code true} or {@code false}
     */
	boolean existsByProcessName(String processName);

	/**
     * 
     * To delete a list of process by list of processIds.
     * 
     * In keyword operates bulk delete operation for processIds.
     * 
     * @param processIds type of Integer, the list of processIds.
     */
	//FIXME: REMOVE HARD CODED SCHEMA NAME
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM process WHERE id in ?1", nativeQuery = true)
	void deleteByProcessIdIn(List<Integer> processIds);
	
//	@Query(value = "SELECT * FROM process WHERE id IN (SELECT processid FROM processinstance)", nativeQuery = true)
//	public List<Process> findProcessIdInProcessInstance();
	
	@Query(value="select p.processname, pv.versionnumber, pv.id, pv.isactive, pv.modifiedon, pv.modifiedby, p.id as processId, pv.versionname ,\r\n"
			+ "	( select CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END from processInstance where versionid = pv.id) as isLocked\r\n"
			+ "	from process p inner join processversion pv on pv.processId = p.id \r\n"
			+ "	order by pv.modifiedon desc, pv.id desc", nativeQuery = true)
	public List<Object[]> findAllProcessWithVersion();
	
	@Query(value="select * from process where processname=:processName", nativeQuery = true)
	Process findByProcessName(String processName);
	
	@Query(value = "SELECT distinct p.id, p.processname FROM process p inner join processversion pv on pv.processId = p.id WHERE pv.isactive=true order by p.processname", nativeQuery = true)
	public List<Object[]> getActiveProcessNames();
	
	@Query(value = "SELECT distinct p.id, p.processname FROM process p inner join processversion pv on pv.processId = p.id order by p.processname", nativeQuery = true)
	public List<Object[]> getAvailableProcessNames();
	
	@Query(value = "SELECT distinct pv.id, pv.versionname, pv.versionnumber FROM processversion pv inner join process p on p.id = pv.processId where pv.processId=:processId order by pv.versionnumber", nativeQuery = true)
	public List<Object[]> getProcessVersionNames(int processId);
	
	@Query(value = "select * from process where id in ?1", nativeQuery = true)
	public List<Process> getProcessByIds(List<Integer> processVersionIds);
}
