package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.DriverBlockHistory;
import com.idms.base.dao.entity.RouteBlockHistory;
import com.idms.base.support.rest.RestConstants;

public interface RouteBlockHistoryRepository extends JpaRepository<RouteBlockHistory, Integer> {
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"route_block_history where route_id=?1 and depot_id=?2 order by created_on desc limit 1")
	public RouteBlockHistory findLatestRepord(Integer id,Integer depotId);
	
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"route_block_history where route_id=?1 and depot_id=?2 order by created_on")
	public List<RouteBlockHistory> findHistoryRecords(Integer id,Integer depotId);

}
