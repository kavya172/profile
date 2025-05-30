package com.galaxe.gxworkflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.Process;
import com.galaxe.gxworkflow.entity.ProcessStatus;


/**
 * The processInstanceRepository interface provides data access operations for managing {@link Process} entity.
 *
 * <p>
 * This repository interface defines methods to perform CRUD operations on the {@link Process} entity stored in the underlying data store, such as a database.
 *
 */
@Repository(value = "processStatusRepository")
public interface ProcessStatusRepository extends JpaRepository<ProcessStatus, Integer> {

	
	public ProcessStatus findByCode(String code);
	
}
