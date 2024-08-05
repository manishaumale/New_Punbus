package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.TyreRepairHistory;
import com.idms.base.support.rest.RestConstants;

public interface TyreRepairHistoryRepo extends JpaRepository<TyreRepairHistory, Integer>{

	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".tyre_repair where depot_id=?1")
	public List<TyreRepairHistory> findAllByDepot(Integer id);
}
