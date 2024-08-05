package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.AuthorizeRoute;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.QRouteMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.entity.RoutePermitMaster;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface RouteMasterRepository extends JpaRepository<RouteMaster, Integer>,
		QuerydslPredicateExecutor<RouteMaster>, QuerydslBinderCustomizer<QRouteMaster> {

	@Override
	default void customize(QuerydslBindings bindings, QRouteMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}

	List<RouteMaster> findAllByStatus(boolean status);

	List<RouteMaster> findAll();

	List<RouteMaster> findAllByIsDeleted(boolean IsDeleted);

	@Modifying
	@Query(value = "Update RouteMaster  set status=?1 where id=?2 ")
	int updateRouteMasterStatusFlag(Boolean flag, Integer id);

	@Modifying
	@Query(value = "Update RouteMaster  set isDeleted=?1 where id=?2 ")
	int updateRouteMasterIsDeletedFlag(Boolean flag, Integer id);

	List<RouteMaster> findAllByStatusAndIsDeleted(boolean status, boolean flag);

	@Query(value = "SELECT p from RoutePermitMaster p where p.permitDetailsMaster.id = ?1")
	List<RoutePermitMaster> findAllRoutesByPermitId(Integer id);

	@Query(value = "SELECT p from RoutePermitMaster p where p.route.id = ?1")
	List<RoutePermitMaster> findAllPermitsByRouteId(Integer id);

	@Query(value = "select distinct route.id from RouteMaster route inner join RoutePermitMaster as routePermit on route.id = routePermit.route.id where routePermit.permitDetailsMaster.id = ?1")
	List<Integer> getAllRoutesByPermit(Integer permitId);

	@Query(value = "SELECT p from RouteMaster p where p.depotMaster.depotCode = ?1 and p.isDeleted=false ")
	List<RouteMaster> getAllRouteMasterByDepot(String dpCode);
	
	@Query(value="select * from " + RestConstants.SCHEMA_NAME + "." + "mst_route  mr  where mr.status=?2 and mr.is_deleted=?3 and mr.id in (select dr.route_id "
			+ "from " + RestConstants.SCHEMA_NAME + "." + "daily_roaster dr  where dr.driver_id =?1)" , nativeQuery = true)
	List<RouteMaster> getAllRouteMasterByDriverId(Integer driverId,boolean status , boolean isDelete);

	@Query(value = "SELECT p from RouteMaster p where p.depotMaster.depotCode = ?1 and p.isDeleted=false and status=true ")
	List<RouteMaster> getAllRouteMasterByDepotAndStatus(String dpCode);

	@Query(value = "SELECT p from RouteMaster p where p.depotMaster.id = ?1 and p.routeTypeMaster.id=?2 and p.isDeleted=false and status=true ")
	List<RouteMaster> findRoutesByTypeAndDepot(Integer id, Integer typeId);

	@Query(value = "select count(*) as totalCount, (select count(*) from " + RestConstants.SCHEMA_NAME + "."
			+ "mst_route mr2 where status=true and mr2.transport_id in (?1)) as activeRoutes " + " from "
			+ RestConstants.SCHEMA_NAME + "." + "mst_route mr where transport_id in (?1) ", nativeQuery = true)
	List<Object[]> getTotalRoutes(List<Integer> tpIds);

	/*@Query(value = "select count(*) as totalCount, (select count(*) from " + RestConstants.SCHEMA_NAME + "."
			+ "mst_route mr2 where status=true and mr2.depot_id in (?1)) as activeRoutes " + " from "
			+ RestConstants.SCHEMA_NAME + "." + "mst_route mr where depot_id in (?1) ", nativeQuery = true)
	List<Object[]> getTotalRoutesByDepot(Integer id);*/
	
	@Query(value="select count(*) as totalCount,"
+"sum(case when mr.is_deleted =false and mr.status =true then 1 else 0 end )as activeRoutes "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr where depot_id in (?1)",nativeQuery=true)
List<Object[]> getTotalRoutesByDepot(Integer id);

	/*@Query(value = "select route.route_code,route.route_name,route.scheduled_kms as kms,sum(stateWise.total_kms) as total_kms,night.night_detail,"
			+ " sum(stateWise.hill_kms) as hill_kms,sum(stateWise.plain_kms) as plain_kms ," + " route.total_ot from "
			+ RestConstants.SCHEMA_NAME + "." + " mst_route_state_wise_km stateWise" + " join "
			+ RestConstants.SCHEMA_NAME + "." + " mst_route route on route.id = stateWise.route_id" + " join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_depot depo on depo.depot_id = route.depot_id" + " join "
			+ RestConstants.SCHEMA_NAME + "."
			+ "mst_total_nights night on night.total_nights_id = route.total_nights_id"
			+ " where depo.depot_id =?1 and route.created_on >=?2 and route.created_on <=?3"
			+ " GROUP BY route.route_code,route.route_name,route.scheduled_kms,route.total_ot,night.night_detail", nativeQuery = true)
	List<Object[]> routesInOperationReport(Integer depotId, Date startDate, Date endDate);*/
	
	/*@Query(value="select route.route_code, route.route_name, route.scheduled_kms as kms, sum(stateWise.total_kms) as total_kms, night.night_detail," 
+"sum(stateWise.hill_kms) as hill_kms, sum(stateWise.plain_kms) as plain_kms ,route.total_ot "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route_state_wise_km stateWise "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route route on route.id = stateWise.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depo on depo.depot_id = route.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_total_nights night on night.total_nights_id = route.total_nights_id "
+"where depo.depot_id =?1  and to_date(to_char(route.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
+"GROUP BY route.route_code, route.route_name, route.scheduled_kms, route.total_ot, night.night_detail",nativeQuery=true)
*/	
	@Query(value="select mr.route_code, mr.route_name, (coalesce(mr.scheduled_kms,0)) as kms, mt.total_ot as overtime, mtn.night_detail, (coalesce(state.hill_kms,0)) as hill_kms, "
			+ "(coalesce(state.plain_kms,0)) as plain_kms from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id= dr.rota_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.root_id =mr.id and dr.trip_id =mt.trip_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_rotation tr on tr.route_id = mr.id and tr.trip_rotation_id = dr.trip_rotation "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_total_nights mtn on mtn.total_nights_id = tr.total_nights_id "
			+ "left join (select stateWise.route_id, sum(coalesce(stateWise.total_kms, 0)) as statewise_kms, sum(coalesce(stateWise.hill_kms,0)) as hill_kms, "
			+ "sum(coalesce(stateWise.plain_kms,0)) as plain_kms from "+RestConstants.SCHEMA_NAME+"."+"mst_route_state_wise_km stateWise group by stateWise.route_id) state on state.route_id=mr.id "
			+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
			+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
			+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) and mr.depot_id =?1 "
			+ "order by 2",nativeQuery=true)
	List<Object[]> routesInOperationReport(Integer depotId, Date startDate, Date endDate);

	@Query(value = "from RouteMaster p where p.id =?1 and p.isDeleted=false and status=true ")
	RouteMaster getById(Integer id);

	/*@Query(value = "select route.route_id,route.route_name,route.scheduled_kms,route.from_state_id,route.from_city_id,"
			+"route.to_state_id,route.to_city_id, route.id,route.dead_kms,route.status,cat.route_category_name "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route route "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category cat on cat.route_category_id = route.route_category_id "
			+"where route.depot_id=?1 and route.is_deleted =false and route.status=true", nativeQuery = true)
	List<Object[]> getAllRouteMasterByDepoCode(Integer dpCode);*/
	
	@Query(value="select route.route_id,route.route_name,route.scheduled_kms,route.from_state_id,route.from_city_id,route.to_state_id,"
+"route.to_city_id, route.id,route.dead_kms,route.status,cat.route_category_name ,case when t.id is null then 1 else 0 end as routeflag "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route route "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category cat on cat.route_category_id = route.route_category_id "
+"left join (select distinct dr.route_id,route.id from "+RestConstants.SCHEMA_NAME+"."+"mst_route route "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr  on dr.route_id = route.id "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id =dr.rota_id and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') =?2) t on t.id=route.id " 
+"where route.depot_id=?1 and route.is_deleted =false order by route.id desc",nativeQuery=true)
List<Object[]> getAllRouteMasterByDepoCode(Integer dpCode,Date Date);

//	@Query(value = "select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, md.depot_id, mr.route_name, mt.trip_id, mbr.scheduled_kms,"
//			+ "r.rota_date as time, mt.trip_end_date from "+ RestConstants.SCHEMA_NAME + "." + "daily_roaster dr " + "join "
//			+ RestConstants.SCHEMA_NAME + "." + "roaster r on dr.rota_id = r.rota_id " + "join "
//			+ RestConstants.SCHEMA_NAME + "." + "mst_depot md on r.depot_id = md.depot_id " + "join "
//			+ RestConstants.SCHEMA_NAME + "." + "mst_route mr on dr.route_id =int4(mr.route_id) " + "join "
//			+ RestConstants.SCHEMA_NAME + "." + "mst_trip mt on dr.trip_id = mt.trip_id " + "join "
//			+ RestConstants.SCHEMA_NAME + "." + "mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id "
//			+ "where mr.bus_type_id = ?1 and to_date(to_char(mr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ", nativeQuery = true)
	
	@Query(value ="select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, md.depot_name, mr.route_name, tr.start_time as time, "
			+ "ABS((coalesce(mbr.total_actual_kms, 0) - coalesce(mr.scheduled_kms,0))) as Sch_KM, mbr.total_actual_kms , "
			+ "(select max(to_date(to_char(r1.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) from "+RestConstants.SCHEMA_NAME+"."+"roaster r1 join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 on dr2.rota_id =r1.rota_id "
			+ "where dr2.route_id = dr.route_id ) as last_operated , mbd.bus_reg_number "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on dr.rota_id = r.rota_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on r.depot_id = md.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id =mr.id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_rotation tr on tr.trip_rotation_id = dr.trip_rotation "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id "
			+ "and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
			+ "where mbd.bus_type = ?1 and ABS((coalesce(mbr.total_actual_kms, 0) - coalesce(mr.scheduled_kms,0)))>0 "
			+ "and dr.roaster_id = (select min(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id "
			+ "and dr2.bus_id= dr.bus_id and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
			+ "order by 1 desc", nativeQuery=true)
List<Object[]> busWiseKmsMissedReport(Integer busTypeId, Date startDate, Date endDate);


//@Query(value="select sum(mt.scheduled_kms) as expected_kms, sum(btah.kms_done) as achieved_kms,sum(mt.scheduled_kms - btah.kms_done ) as difference "
//+"from "+RestConstants.SCHEMA_NAME+"."+"roaster r " 
//+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on r.rota_id = dr.rota_id " 
//+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id " 
//+"join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on dr.bus_id = btah.bus_id ",nativeQuery=true)

@Query(value="select md.depot_name, sum(coalesce ((mr.scheduled_kms*tt.trip_value),0)) as Expected_kms, sum(coalesce (mbr.total_actual_kms,0)) as achieved_km, "
		+ "sum(coalesce (mbr.vts_kms,0)) as Vts_km, sum(coalesce((mr.scheduled_kms*tt.trip_value ),0)-coalesce(mbr.scheduled_kms,0)) as difference "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_type tt on tt.trip_type_id = mr.trip_type_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mr.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
		+ "where to_date(to_char(dr.rotation_availability_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
		+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
		+ "group by md.depot_name ",nativeQuery=true)
List<Object[]> expectedVersusAchieved(Date startDate,Date endDate);

//@Query(value="select distinct mr.route_code, mr.route_name, rr.trip_start_time as starting_time, mr.scheduled_kms, mt.trip_start_time as trip_time,"
//+"mt.trip_end_date as last_operated, count(mt.trip_id) as no_of_trips "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
//+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mr.id = mt.root_id "
//+"left join "+RestConstants.SCHEMA_NAME+"."+"route_rotation rr on mr.id = rr.route_id "
//+"group by mr.route_code, mr.route_name,rr.trip_start_time, mr.scheduled_kms, mt.trip_start_time, mt.trip_end_date",nativeQuery=true)

@Query(value="select mr.route_code, mr.route_name, (concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time)) as starting_time, "
		+ "(coalesce(mr.scheduled_kms,0))as Scheduled_km, 1 as profit_per_KM, "
		+ "(select max(to_date(to_char(r1.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) from "+RestConstants.SCHEMA_NAME+"."+"roaster r1 join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr3 on dr3.rota_id =r1.rota_id "
		+ "where dr3.route_id = dr.route_id ) as last_operated, count(mt.trip_id) as no_of_trips "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.route_id = mr.id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id "
		+ "where dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 "
		+ "where dr2.rota_id = dr.rota_id and dr2.trip_id = dr.trip_id ) "
		+ "and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "group by mr.route_code, mr.route_name, mr.scheduled_kms, (concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time)), dr.route_id "
		+ "order by 3 desc" , nativeQuery = true)
List<Object[]> routeAnalysisReport(Date fromDate , Date toDate);

/*@Query(value="select mr.route_code, mr.route_name, mrc.route_category_name, mr.scheduled_kms "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mr.depot_id = md.depot_id " 
+"where md.depot_id =?1  and to_date(to_char(mr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)*/

@Query(value="select mr.route_code, mr.route_name, mrc.route_category_name, (coalesce(mr.scheduled_kms,0))as scheduled_kms "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id and mr.status = true and mr.is_deleted = FALSE "
		+ "where mr.depot_id =?1 and "
		+ "not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "where mr.id = dr.route_id and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "order by 1" , nativeQuery=true)
List<Object[]> nonOperationReport(Integer depotId, Date startDate, Date endDate);

/*@Query(value = "select sum(scheduled_kms*type.trip_value) from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
		+ " join "+RestConstants.SCHEMA_NAME+"."+"trip_type type on type.trip_type_id = mr.trip_type_id "
		+ " where depot_id =?1 and transport_id =?2 and mr.is_deleted=false and mr.status=true", nativeQuery = true)
Integer getAllScheduledKmsByDepo(Integer id,Integer tuId);*/

@Query(value = "select sum(dead_kms) from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr where depot_id =?1 and transport_id =?2", nativeQuery = true)
Integer getAllDeadKmsByDepo(Integer id,Integer tuId);

@Query(value = "select r.id, r.route_id , r.route_name, r.route_code from "+RestConstants.SCHEMA_NAME+"."+"mst_route r where r.depot_id= ?1 and r.transport_id =?2", nativeQuery=true)
List<Object[]> getSpecialRoutesByDeopAndTransUnit(Integer depoMasterId, Integer transPortId);

@Query(value = "select r.id, r.route_id , r.route_name, r.route_code from "+RestConstants.SCHEMA_NAME+"."+"mst_route r where r.depot_id= ?1 and r.transport_id =?2 and r.route_type_id =?3", nativeQuery=true)
List<Object[]> getManualRoutesByDeopAndTransUnit(Integer depoMasterId, Integer transPortId, Integer routeType);


@Modifying
@Query(value = "Update RouteMaster r  set r.routeCategoryMaster.id = ?2 where r.id=?1 ")
int updateRouteMasterCategoryById(Integer routeId, Integer categoryId);

@Query(value="select mr.route_id, mr.route_name, mrc.route_category_name, (coalesce(mr.scheduled_kms,0)) as kms "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mrc.route_category_id = mr.route_category_id  "
		+ "where exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr join "+RestConstants.SCHEMA_NAME+"."+"trip_rotation tr on tr.trip_rotation_id = dr.trip_rotation "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on dr.rota_id = r.rota_id where dr.route_id= mr.id "
		+ "and EXTRACT(EPOCH FROM (TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',tr.end_time) , 'YYYY-MM-DD HH24:MI:SS' ) "
		+ "- TO_TIMESTAMP( concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',tr.start_time), 'YYYY-MM-DD HH24:MI:SS' )))/3600 <8  "
		+ "and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2)" , nativeQuery=true)
List<Object[]> getRoutesLessThanEightReport(Date fromDate,Date toDate);

@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+"."+"mst_route where blocked=true")
List<RouteMaster> findAllBlockedRoutes();


@Query(value="select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')as date, mr.route_name, count(mt.trip_id)as trip , mr.scheduled_kms as sch_km, "
		+ "sum(coalesce (mr.scheduled_kms,0)) as total_km, sum(coalesce (mbr.vts_kms, 0)) as GPS_km "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.route_id = mr.id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
		+ "where mr.id = ?1 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.rota_id = dr.rota_id and dr2.trip_id = dr.trip_id ) "
		+ "and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?2 "
		+ "group by to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') , mr.route_name, mr.scheduled_kms", nativeQuery=true)
List<Object[]> routeWiseSummaryReportList(Integer id , Date startDate , Date endDate);

@Query(value="select r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') rota_date, md.depot_name, sum(coalesce (mbr.scheduled_kms,0)) as roaster_km, "
		+ "sum(coalesce (mbr.vts_kms,0)) as Vts_km , sum(coalesce (mr.scheduled_kms,0)) as alloted_km "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mr.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
		+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
		+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
		+ "group by r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), md.depot_name " , nativeQuery=true)
List<Object[]> dutyRosterTotalKMsReport(Date startDate , Date endDate);

@Query(value="select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, mr.route_name, "
		+ "sum(case when mbt.bus_type_id in (1) then 1 else 0 end) as Ordinary, "
		+ " sum(case when mbt.bus_type_id in (3) then 1 else 0 end) as HVAC, "
		+ "sum(case when mbt.bus_type_id in (4) then 1 else 0 end) as Volvo, "
		+ "sum(case when mbt.bus_type_id in (34) then 1 else 0 end) as Midi_Bus, "
		+ "sum(case when mbt.bus_type_id in (42) then 1 else 0 end) as Integral, "
		+ "sum(case when mbt.bus_type_id in (1,3,4,34,42) then 1 else 0 end) as Total "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on dr.rota_id = r.rota_id and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.rota_id = dr.rota_id "
		+ "and dr2.bus_id= dr.bus_id and to_date(to_char(r.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id =mbd.bus_type "
		+ "where (mr.id= ?1 or 0=?1) group by to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), mr.route_name order by 1", nativeQuery=true)
List<Object[]> routeWiseBusAllocationReport(Integer routeId,Date startDate , Date endDate);



@Query(value="select mr.route_name, (coalesce(mr.scheduled_kms,0))as scheduled_kms "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"users u on u.id = mr.updated_by and mr.status = false "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_role rm on rm.role_id = u.role_id "
		+ "where rm.role_code = 'GM' and to_date(to_char(mr.updated_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "order by 1 " ,nativeQuery=true)
List<Object[]> routeNoToBeOperatedReport(Date startDate , Date endDate);


@Query(value="select mr.route_id,mr.route_name,mr.id "
        +"from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr where mr.transport_id =1 and mr.is_deleted=false and "
	      		+ " not exists (select 1 from "+ RestConstants.SCHEMA_NAME+"."+"route_block_history rbh where rbh.route_id=mr.id and rbh.to_date >=current_timestamp)",nativeQuery=true)
List<Object[]> getAllRouteBytransportId(Integer transportId);

@Query(value="select mbd.bus_id ,mbd.bus_reg_number "
        +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd where "
	      +"not exists (select 1 from "+ RestConstants.SCHEMA_NAME+"."+"mst_bus_unavailable mbu where mbd.bus_id=mbu.bus_id and mbu.likely_to_ready_date < now() )and not exists (select ar.bus_id from "+RestConstants.SCHEMA_NAME+".authorize_route ar where ar.bus_id=mbd.bus_id and ar.transport_id =?1 and ar.is_deleted=false and ar.created_on + interval '30 day' >current_timestamp)"
	      + " and mbd.bus_reg_number is not null and not exists (select 1 from "+ RestConstants.SCHEMA_NAME+"."+"bus_block_history bbh where bbh.bus_id =mbd.bus_id and bbh.to_date <=current_timestamp)",nativeQuery=true)
List<Object[]> getAllBusBytransportId(Integer transportId);

@Query(value="select mdd.driver_name, mdd.driver_id "
        +"from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd where "
	      +"not exists (select 1 from "+ RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdd.driver_id =mdu.driver_id AND mdu.to_date < now() and mdu.status_id=2) and not exists (select ar.driver_id from "+RestConstants.SCHEMA_NAME+".authorize_route ar where ar.driver_id =mdd.driver_id and ar.transport_id =?1 and ar.is_deleted=false and ar.created_on + interval '30 day' >current_timestamp) and"
	      + " not exists (select 1 from "+ RestConstants.SCHEMA_NAME+"."+"driver_block_history dbh where mdd.driver_id=dbh.driver_id and dbh.to_date <=current_timestamp)",nativeQuery=true)
List<Object[]> getAllDriverBytransportId(Integer transportId);

@Query(value="select mcd.conductor_id ,mcd.conductor_name "
        +"from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd where "
	      +"not exists (select 1 from "+ RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu where  mcd.conductor_id =mcu.conductor_id and mcu.to_date < now() and mcu.status_id=2) and not exists (select ar.conductor_id from "+RestConstants.SCHEMA_NAME+".authorize_route ar where ar.conductor_id =mcd.conductor_id and ar.transport_id =?1 and ar.is_deleted=false and ar.created_on + interval '30 day' >current_timestamp) and"
	      + " not exists (select 1 from "+ RestConstants.SCHEMA_NAME+"."+"conductor_block_history cbh where cbh.conductor_id=mcd.conductor_id and cbh.to_date<=current_timestamp)",nativeQuery=true)
List<Object[]> getAllConductorBytransportId(Integer transportId);

@Query(value="SELECT mt.transport_name, mbd.bus_reg_number, concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt2.trip_start_time) as bus_up_time,  "
		+ "concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt2.trip_end_time) as bus_down_time, "
		+ "mbt.bus_type_name, mr.route_name, mdd.driver_code, mdd.driver_name, mcd.conductor_code, mcd.conductor_name, ''GPS_Link "
		+ "from  "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id= dr.rota_id  "
		+ "join  "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mbd.transport_unit   "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt2 on mt2.trip_id = dr.trip_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = dr.conductor_id  "
		+ "where mbd.bus_id =?1 and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.rota_id =dr.rota_id  and dr2.driver_id =dr.driver_id  "
		+ "and dr2.conductor_id= dr.conductor_id ) and "
		+ "to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')= to_date(to_char(now() , 'YYYY/MM/DD'), 'YYYY/MM/DD')",nativeQuery=true)
List<Object[]> getEnrouteBusdetails(Integer busId);

@Query(value="SELECT mr.route_name,mbd.bus_reg_number,mdd.driver_code,mdd.driver_name,mcd.conductor_code ,mcd.conductor_name,md.depot_name,ar.authorize_route_id,ar.is_deleted,mt.service_id,mt.trip_code,ar.reason "
        +"from "+RestConstants.SCHEMA_NAME+"."+"authorize_route ar "
	      +"JOIN "+RestConstants.SCHEMA_NAME+"."+"mst_route mr ON ar.route_id =mr.id "
	      +"JOIN "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd ON ar.bus_id =mbd.bus_id "
	      +"JOIN "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd ON ar.driver_id =mdd.driver_id "
	      +"JOIN "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd ON ar.conductor_id =mcd.conductor_id "
	      +"JOIN "+RestConstants.SCHEMA_NAME+"."+"mst_depot md ON ar.depot_id =md.depot_id "
	      +"JOIN "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt ON ar.trip_id =mt.trip_id "
	      +"where ar.depot_id =?1 and ar.is_deleted=false",nativeQuery=true)
List<Object[]> onLoadAuthorizeRoute(Integer depotId);


@Query(value="select mr.id, mr.route_name , mt.service_id, md.depot_name, mr.scheduled_kms, "
		+ "(select max(to_date(to_char(r1.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) from "+RestConstants.SCHEMA_NAME+"."+"roaster r1 join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr1 on dr1.rota_id =r1.rota_id "
		+ "where dr1.trip_id = mt.trip_id ) as last_operated_date "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mt.root_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mr.depot_id "
		+ "where not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id where mt.trip_id = dr.trip_id and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2)",nativeQuery=true)
List<Object[]> tripsNotBeingOperatedReport(Date startDate , Date endDate);

@Query(value = "select sum(mr.scheduled_kms*tt.trip_value ) from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr  "
		+ " inner join "+RestConstants.SCHEMA_NAME+"."+"trip_type tt on tt.trip_type_id =mr.trip_type_id  "
		+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mr.id =mt.root_id and mt.trip_id in( "
		+ " select distinct trip_id from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr where rota_id = ?1)", nativeQuery = true)
Integer getAllScheduledKmsByRotaId(Integer rotaId);


@Query(value = "select sum(((mpd.total_trips*2))*mpd.total_kms) from "+RestConstants.SCHEMA_NAME+"."+"mst_permit_details mpd  "
		+ " where mpd.depot_id = ?1 and mpd.transport_id =?2 and mpd.is_deleted=false and mpd.status=true and "
		+ " to_date(to_char(mpd.valid_up_to, 'YYYY/MM/DD'), 'YYYY/MM/DD')>= to_date(to_char(now() , 'YYYY/MM/DD'), 'YYYY/MM/DD')", nativeQuery = true)
        Integer getAllScheduledKmsByDepo(Integer id,Integer tuId);

/*@Query(value = "Select a from authorize_route a where a.authorize_route_id= ?1")
List<ConductorMaster> getAuthorizeRouteById(Integer id);*/

@Query(value = "SELECT p.trip_id ,p.service_id ,p.trip_code "
		+"from "+RestConstants.SCHEMA_NAME+"."+"mst_trip p where not exists (select ar.trip_id from "+RestConstants.SCHEMA_NAME+".authorize_route ar where ar.trip_id =p.trip_id  and ar.route_id =?1 and ar.is_deleted=false and ar.created_on + interval '30 day' >current_timestamp) and p.root_id =?1",nativeQuery=true)
List<Object[]> findTripByRouteId(Integer routeId);

@Query(value="select r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') rota_date, sum(coalesce((mr.scheduled_kms*tt.trip_value), 0)) as alloted_kms, "
		+ "sum(case when tr.total_nights_id =1 then coalesce((mr.scheduled_kms*tt.trip_value), 0) end) To_Be_Received_Kms_n_0 "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id= dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_type tt on tt.trip_type_id = mr.trip_type_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_rotation tr on tr.route_id = mr.id and tr.trip_rotation_id = dr.trip_rotation "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_total_nights mtn on mtn.total_nights_id = tr.total_nights_id "
		+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
		+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
		+ "group by r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')",nativeQuery=true)
List<Object[]> kmsLikelyToBeReceivedReport( Date startDate, Date endDate);

@Query(value="select r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') rota_date, sum(coalesce((mr.scheduled_kms*tt.trip_value), 0)) as alloted_kms, "
		+ "sum(case when tr.total_nights_id =2 then coalesce((mr.scheduled_kms*tt.trip_value), 0) end) To_Be_Received_Kms_n_1 "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id= dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_type tt on tt.trip_type_id = mr.trip_type_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_rotation tr on tr.route_id = mr.id and tr.trip_rotation_id = dr.trip_rotation "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_total_nights mtn on mtn.total_nights_id = tr.total_nights_id "
		+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
		+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
		+ "group by r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')",nativeQuery=true)
List<Object[]> kmsLikelyToBeReceivedReport1( Date startDate, Date endDate);

@Query(value="select r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') rota_date, sum(coalesce((mr.scheduled_kms*tt.trip_value), 0)) as alloted_kms, "
		+ "sum(case when tr.total_nights_id =3 then coalesce((mr.scheduled_kms*tt.trip_value), 0) end) To_Be_Received_Kms_n_2 "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id= dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_type tt on tt.trip_type_id = mr.trip_type_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_rotation tr on tr.route_id = mr.id and tr.trip_rotation_id = dr.trip_rotation "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_total_nights mtn on mtn.total_nights_id = tr.total_nights_id "
		+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
		+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
		+ "group by r.roasted_code , to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')",nativeQuery=true)
List<Object[]> kmsLikelyToBeReceivedReport2(Date startDate, Date endDate);

@Query(value="select mr.route_code, mr.route_name ,to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, max(coalesce (mr.scheduled_kms,0)) Sch_kms, "
		+ "min(coalesce (mt.total_ot,0) ) total_ot , min(mtn.night_detail) nght "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_rotation tr on tr.route_id = mr.id and tr.trip_rotation_id = dr.trip_rotation "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_total_nights mtn on mtn.total_nights_id = tr.total_nights_id "
		+ "where (mr.id=?1 or 0=?1) and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
		+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
		+ "group by to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), mr.route_name, mr.route_code "
		+ "order by 4 desc",nativeQuery=true)
List<Object[]> maxKMsReport(Integer routeId,Date startDate, Date endDate);

@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".mst_route where route_code= ?1 and status=true and is_deleted=false")
RouteMaster findByRouteCode(String routeName);

@Query(value="select mt.transport_name ,md.depot_name ,mr.route_code , mr.route_id ,mr.route_name ,mrt.route_type_name ,mrc.route_category_name  "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id =mr.transport_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id =mr.depot_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_route_type mrt on mrt.route_type_id =mr.route_type_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mrc.route_category_id = mr.route_category_id  "
		+ "where mr.depot_id in (?1)",nativeQuery=true)
List<Object[]> getTotalRouteDetails(Integer depoCode);

}






