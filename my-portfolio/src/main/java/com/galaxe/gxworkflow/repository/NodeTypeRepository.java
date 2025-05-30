package com.galaxe.gxworkflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.NodeType;

/**
 * The NodeTypeRepository interface provides data access operations for managing {@link NodeType} entity.
 *
 * <p>
 * This repository interface defines methods to perform CRUD operations on the {@link NodeType} entity stored in the underlying data store, such as a database.
 *
 */
@Repository
public interface NodeTypeRepository extends JpaRepository<NodeType, Integer> {

	/**
	 * To retrieve the nodeType filtering by nodeTypeName
	 *
	 * @param name type of string, is a nodeTypeName of a {@link NodeType}
	 * @return  {@link NodeType}
	 */
	public NodeType findByNameIgnoreCase(String name);

}
