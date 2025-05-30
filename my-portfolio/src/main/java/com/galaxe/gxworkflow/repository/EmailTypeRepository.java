package com.galaxe.gxworkflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galaxe.gxworkflow.entity.EmailType;
import com.galaxe.gxworkflow.entity.NodeType;

@Repository
public interface EmailTypeRepository extends JpaRepository<EmailType, Integer> {

}
