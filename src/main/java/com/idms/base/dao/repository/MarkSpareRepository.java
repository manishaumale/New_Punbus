package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.MarkSpareEntity;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface MarkSpareRepository extends JpaRepository<MarkSpareEntity,Integer> {
	
	@Query(value="select mbd.bus_reg_number,ms.remarks,mdd.driver_name,mcd.conductor_name,to_date(to_char(ms.from_dt, 'YYYY/MM/DD'), 'YYYY/MM/DD') as fromdate ,to_date(to_char(ms.to_dt, 'YYYY/MM/DD'), 'YYYY/MM/DD') as todate from "+RestConstants.SCHEMA_NAME+"."+"mark_spare ms " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd " 
+"on mbd.bus_id = ms.bus_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd " 
+"on mdd.driver_id = ms.driver_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd " 
+"on mcd.conductor_id = ms.conductor_id " 
+"where ms.depot_id=?1 order by ms.spare_id desc ",nativeQuery=true)
List<Object[]> findbydepoId(Integer id);


@Query(value="select mbd.bus_id ,mbd.bus_reg_number from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd " 
+"where  not exists ("
+"select ms.bus_id from "+RestConstants.SCHEMA_NAME+"."+"mark_spare ms where ms.bus_id  = mbd.bus_id and ms.to_dt >=?2 and ms.bus_id is not null ) "
+"and mbd.depot =?1",nativeQuery=true)
List<Object[]> getAllBusDetails(Integer id,Date date);


@Query(value="select mcd.conductor_id,mcd.conductor_name from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
+"where  not exists ("
+"select ms.conductor_id from "+RestConstants.SCHEMA_NAME+"."+"mark_spare ms where ms.conductor_id  = mcd.conductor_id  and ms.to_dt >=?2 and ms.conductor_id is not null ) "
+"and mcd.depot_id =?1",nativeQuery=true)
List<Object[]>getAllConductorDetails(Integer id,Date date);

@Query(value="select mdd.driver_id,mdd.driver_name from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
+"where  not exists ("
+"select ms.driver_id from "+RestConstants.SCHEMA_NAME+"."+"mark_spare ms where ms.driver_id  = mdd.driver_id  and ms.to_dt >=?2 and ms.driver_id is not null ) "
+"and mdd.depot_id =?1",nativeQuery=true)
List<Object[]>getAllDriverDetails(Integer id,Date date);


}
