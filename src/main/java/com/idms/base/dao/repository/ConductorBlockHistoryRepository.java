package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.ConductorBlockHistory;
import com.idms.base.support.rest.RestConstants;

public interface ConductorBlockHistoryRepository extends JpaRepository<ConductorBlockHistory, Integer> {

	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"conductor_block_history where conductor_id=?1 and depot_id=?2 order by created_on limit 1")
	public ConductorBlockHistory findLatestRepord(Integer id,Integer depotId);
	
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"conductor_block_history where conductor_id=?1 and depot_id=?2 order by created_on")
	public List<ConductorBlockHistory> findHistoryRecords(Integer id,Integer depotId);
}
