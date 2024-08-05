package com.idms.base.dao.repository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.OutShedder;
import com.idms.base.dao.entity.QOutShedder;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface OutShedderRepository extends JpaRepository<OutShedder, Integer>, QuerydslPredicateExecutor<OutShedder>,
		QuerydslBinderCustomizer<QOutShedder> {

	@Override
	default void customize(QuerydslBindings bindings, QOutShedder root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
	
	@Query(value = "select mbd.bus_reg_number,mdd.driver_id,mcd.conductor_id,mr.route_name,ms.state_name as fromState, "
			+ " ms1.state_name as toState,md.depot_id,daily.roaster_id,r.rota_id,r.rota_date,tr.trip_start_time,tr.trip_end_time "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster daily "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = daily.rota_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = daily.bus_id"
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = daily.driver_id"
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = daily.conductor_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = daily.route_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on ms.state_id = mr.from_state_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms1 on ms1.state_id = mr.to_state_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on r.depot_id = md.depot_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_trip tr on tr.trip_id = daily.trip_id "
			+ " where r.rota_date = ?1 and daily.bus_id = ?2", nativeQuery=true) 
		
	List<Object[]> getOutShedderByBusId(Date rotaDate, Integer busId);
	
	@Query(value = "select bus_id, mr.route_name,ms.state_name as fromState,ms1.state_name as toState ,r.rota_date , os.in_time ,os.out_time "
			+ " from      "+RestConstants.SCHEMA_NAME+"."+"out_shedder os "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = os.depot_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.depot_id = os.depot_id"
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on ms.state_id = mr.from_state_id"
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms1 on ms1.state_id = mr.to_state_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = os.rota_id "
			+ " where md.depot_id =1", nativeQuery=true) 
	List<Object[]> getOutShedder(String dpCode);
	
	@Query(value = "SELECT o from OutShedder o where o.bus.id = ?1 and o.rotaDate =?2 ")
	List<OutShedder> findAllOutByDepotDailyAndRotaId(Integer busId,Date rotaDate);
	
	
	@Query(value = "SELECT o from OutShedder o where o.depot.id = ?1")
	List<OutShedder> findAllOutByDepot(Integer depotId);
	
	@Modifying
    @Query(value = "Update OutShedder o  set o.inTime = ?2 where o.id=?1 ")
	int updateIntimeInOutShedder(Integer outId,LocalTime inTime);
	
}
