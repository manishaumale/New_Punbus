package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.PenaltyType;

@Repository
public interface PenaltyTypeRepository extends JpaRepository<PenaltyType, Integer>{
	

	PenaltyType findByName(String string);

	@Query(value = "Select p from PenaltyType p ")
	List<PenaltyType> findAllPenaltyType();
}

