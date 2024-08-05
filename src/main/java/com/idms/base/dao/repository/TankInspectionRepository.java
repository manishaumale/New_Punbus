package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DipChartReadings;
import com.idms.base.dao.entity.TankInspection;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface TankInspectionRepository extends JpaRepository<TankInspection, Integer>{

	@Query(value="Select tankIns from TankInspection tankIns where tankIns.depot.id=?1 and tankIns.createdBy = ?2 order by tankIns.id desc")
	List<TankInspection> findAllByDepot(Integer id,String userName);
	
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".tank_inspection ti where date(ti.next_inspection_date) =(current_date +1)")
	List<TankInspection> findTomorrowInspections();
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".tank_inspection ti where date(ti.next_inspection_date) =(current_date)")
	List<TankInspection> findTodayInspections();
	
	@Query(nativeQuery=true ,value="select * from "+RestConstants.SCHEMA_NAME+".tank_inspection  order by created_on desc limit 1 ")
	public TankInspection findMaxRecord();
}
