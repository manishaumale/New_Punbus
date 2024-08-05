package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.TyreCondition;
import com.idms.base.dao.entity.TyrePosition;

@Repository
public interface TyreConditionRepository extends JpaRepository<TyreCondition, Integer>{
	
	List<TyreCondition> findAllByStatus(boolean status);

	TyreCondition findByName(String string);

}
