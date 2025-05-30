package com.galaxe.gxworkflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.VariableType;


@Repository
public interface VariableTypeRepository extends JpaRepository<VariableType, Integer> {

	VariableType findByName(String string);
	
}
