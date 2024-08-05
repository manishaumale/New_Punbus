package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.DipChartReadings;
import com.idms.base.support.rest.RestConstants;

public interface DipChartReadingsRepository extends JpaRepository<DipChartReadings, Integer> {

	@Query(nativeQuery=true ,value="select * from "+RestConstants.SCHEMA_NAME+".dip_chart_readings where fuel_tank_id =?1 order by created_on desc limit 1 ")
	public DipChartReadings orderByDate(Integer id);
	
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".dip_chart_readings where depot_id=?1 and created_by=?2 order by created_on desc")
	public List<DipChartReadings> findByDepotAndUserName(Integer depot,String userName);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".dip_chart_readings where fuel_tank_id =?1 order  by created_on desc limit 1")
	public DipChartReadings findExistingRecord(Integer id);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".dip_chart_readings where fuel_tank_id =?1 and created_by =?2 order  by created_on desc limit 1")
	public DipChartReadings findByDailyuserRecords(Integer id, String d);
	
	@Query(nativeQuery=true ,value="select * from "+RestConstants.SCHEMA_NAME+".dip_chart_readings  order by created_on desc limit 1 ")
	public DipChartReadings findMaxRecord();
}
