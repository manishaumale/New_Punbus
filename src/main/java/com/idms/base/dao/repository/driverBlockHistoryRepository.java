package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.DriverBlockHistory;
import com.idms.base.support.rest.RestConstants;

public interface driverBlockHistoryRepository extends JpaRepository<DriverBlockHistory, Integer>{
	
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"driver_block_history where driver_id=?1 and depot_id=?2 order by created_on desc limit 1")
	public DriverBlockHistory findLatestRepord(Integer id,Integer depotId);
	
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"driver_block_history where driver_id=?1 and depot_id=?2 order by created_on")
	public List<DriverBlockHistory> findHistoryRecords(Integer id,Integer depotId);

}
