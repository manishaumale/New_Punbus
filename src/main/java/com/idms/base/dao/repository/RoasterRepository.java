package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.Roaster;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface RoasterRepository extends JpaRepository<Roaster, Integer>{

	@Query(value="select * from "+RestConstants.SCHEMA_NAME+"."+"roaster r where date_trunc('DAY',rota_date) = date_trunc('DAY', now()) + INTERVAL '1 DAY' and r.depot_id=?1 and r.transport_id =?2", nativeQuery=true)
	Roaster findByDate(Integer depotId, Integer tpId );

	@Query(value=" select r.rota_id, r.rota_date, r.roasted_code, md.depot_name, mt.transport_name, case when r.rota_date >= date_trunc('DAY', now()) then true else false end as isedit,r.transport_id,status.name from "+RestConstants.SCHEMA_NAME+"."+"roaster r  "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id=r.depot_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id=r.transport_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_workflow_status status on status.status_id =r.status_id "
			+ " where md.depot_code=?1 order by r.rota_id desc ", nativeQuery=true)
	List<Object[]> getGeneratedRoasterList(String dpCode);
	
	@Query(value=" select mbd.bus_reg_number , mdd.driver_name , mcd.conductor_name, mc.city_name as from_city, mc1.city_name as to_city, mt.trip_start_time, mt.trip_end_time , mt.scheduled_kms, dr.roaster_id, mbd.bus_id, mdd.driver_id, mcd.conductor_id, dr.trip_id, mt.up_down,  "
			+ " mc.city_id as from_id, mc1.city_id as to_id"
			+ " from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = dr.conductor_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id  "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id  "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_city mc on mc.city_id = mt.from_city "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_city mc1 on mc1.city_id = mt.to_city "
			+ " where rota_id =?1 and dr.is_deleted=false order by mt.trip_start_time "
			+ "", nativeQuery=true)
	List<Object[]> getGeneratedRoasterDetail(Integer rotaId);

}
