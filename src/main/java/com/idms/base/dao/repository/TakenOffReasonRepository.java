package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.TakenOffReason;

@Repository
public interface TakenOffReasonRepository extends JpaRepository<TakenOffReason, Integer>{

	@Query(value="SELECT fm from TakenOffReason fm where fm.parent is null")
	List<TakenOffReason> findAllWithParentNull();

	@Query(value="SELECT fm from TakenOffReason fm where fm.reasonCode=?1 ")
	TakenOffReason findByCode(String string);

}
