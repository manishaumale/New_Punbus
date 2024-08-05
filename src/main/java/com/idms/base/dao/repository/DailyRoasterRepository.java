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

import com.idms.base.dao.entity.BusBlockHistory;
import com.idms.base.dao.entity.ConductorRotaHistory;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.QDailyRoaster;
import com.idms.base.dao.entity.Roaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface DailyRoasterRepository extends JpaRepository<DailyRoaster, Integer> , QuerydslPredicateExecutor<DailyRoaster>, QuerydslBinderCustomizer<QDailyRoaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDailyRoaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	//@Query(value = "select t from(select daily,row_number () over(partition by class order by id desc) rnum from DailyRoaster daily)t where t.rnum = 1 and t.bus.id= ?1")
	//DailyRoaster fetchRosterDetailsByBusId(Integer busId);
	
	
	@Query("select daily from DailyRoaster daily where daily.bus.id =?1 and daily.tripStatus=false and daily.isDeleted=false and daily.rotationAvailabilityDate <= ?2 order by daily.id desc")
	List<DailyRoaster> fetchRosterDetailsByBusId(Integer busId,Date todayDate);
	
	
	@Query("select daily from DailyRoaster daily where daily.bus.id =?1 and daily.trip.id=?2 and daily.tripStatus=false and daily.isDeleted=false and daily.rota.id=?3 order by daily.id desc")
	List<DailyRoaster> fetchRosterDetailsByBusIdandTripId(Integer busId,Integer tripId,Integer rotaId);
	
	

	/*@Query(value = "select dr.rota_date, sum(mt.dead_kms) as Dead_KMs, sum(mt.scheduled_kms) as Sch_KMs , "

	@Query(value = "select dr.rota_date, sum(mt.dead_kms) as Dead_KMs, sum(mt.scheduled_kms) as Sch_KMs , "

	@Query("select daily from DailyRoaster daily where daily.bus.id =?1 and daily.trip.id=?2 and daily.tripStatus=false and daily.isDeleted=false order by daily.id desc")
	List<DailyRoaster> fetchRosterDetailsByBusIdandTripId(Integer busId,Integer tripId);
	
	@Query(value = "select dr.rota_date, sum(mt.dead_kms) as Dead_KMs, sum(mt.scheduled_kms) as Sch_KMs , "
>>>>>>> .r1090
			+ " sum(mt.scheduled_kms + mt.dead_kms) as Total_KMs,"
			+ "  sum(mbr.extra_dead_kms) as Special_KMs "
			+ " from " + RestConstants.SCHEMA_NAME + "." + "roaster r inner join " + RestConstants.SCHEMA_NAME + "." + "daily_roaster dr on r.rota_id = dr.rota_id"
			+ " inner join " + RestConstants.SCHEMA_NAME + "." + " mst_trip mt on dr.trip_id = mt.trip_id inner join "
			+ " " + RestConstants.SCHEMA_NAME + "."+"mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id"
			+ " where dr.bus_id = ?1 and dr.created_on >=?2  and dr.created_on <=?3"
			+ " group by dr.rota_date ", nativeQuery = true)*/
	
/*	@Query(value="select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, sum(mr.dead_kms) as Dead_KMs, sum(mr.scheduled_kms) as Sch_KMs," 
			+"sum(mr.scheduled_kms + mr.dead_kms) as Total_KMs, sum(mbr.extra_dead_kms) as Special_KMs, sum(mr.scheduled_kms - mbr.scheduled_kms) " 
			+"as Missed_KM, mbu.detailed_reason as spare_less_buses, mdu.detailed_reason as spare_less_drivers," 
			+"mcu.detailed_reason as spare_less_conductors from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+"join "+RestConstants.SCHEMA_NAME+"."+"roaster r  on dr.rota_id= r.rota_id " 
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = int4(mr.route_id) " 
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id " 
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_unavailable mbu on dr.bus_id = mbu.bus_id " 
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu on dr.driver_id = mdu.driver_id " 
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu on dr.conductor_id = mcu.conductor_id " 
			+"where mr.bus_type_id =?1 and to_date(to_char(mr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 group by r.rota_date, mbu.detailed_reason, mdu.detailed_reason, mcu.detailed_reason",nativeQuery=true)
*/	
	@Query(value="select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, coalesce(mr.scheduled_kms, 0)as Sch_KMs, "
			+ "coalesce (mbr.total_actual_kms,0) as Total_KMs, "
			+ "(select mr1.scheduled_kms from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr1 join "+RestConstants.SCHEMA_NAME+"."+"mst_route_type mrt on mr1.route_type_id = mrt.route_type_id and mr1.route_type_id = 2 "
			+ "where mr1.id= mr.id ) as Special_KMs, ABS((coalesce(mbr.total_actual_kms, 0) - coalesce(mr.scheduled_kms,0))) as Missed_KM, "
			+ "(case when mbr.vts_kms =0 then 0 else ABS((coalesce(mbr.vts_kms, 0) - coalesce(mbr.total_actual_kms ,0))) end) as vts_Missed_KM, "
			+ "mbd.bus_reg_number , mbt.bus_type_name, mt.transport_name, mbr.vts_kms "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on dr.rota_id= r.rota_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"trip_type tt on tt.trip_type_id = mr.trip_type_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mbd.transport_unit "
			+ "where mbt.bus_type_id = ?2 and (mbd.bus_id =?1 or 0=?1) and "
			+ "to_date(to_char(mbr.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?3 and ?4 "
			+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id "
			+ "and dr2.bus_id= dr.bus_id and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
			+ "order by 7 desc", nativeQuery=true)
	List<Object[]> busWiseKmsReport(Integer busId,Integer busType,Date startDate, Date endDate);
	
	
	/*@Query(value = "select bus_type_name,bus_reg_number,rota_date  from ("
			+ "SELECT  distinct busType.bus_type_name,bus.bus_reg_number,ros.rota_date ,DENSE_RANK() OVER ( "
			+ "PARTITION BY bus.bus_reg_number ORDER BY ros.rota_date desc) as row_num "
			+ "from " + RestConstants.SCHEMA_NAME + "." + "daily_roaster dailyRoaster"
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_details bus on  bus.bus_id = dailyRoaster.bus_id join "
			+ " "+ RestConstants.SCHEMA_NAME + "." + "mst_bus_type busType on busType.bus_type_id = bus.bus_type join "
		    + " "+ RestConstants.SCHEMA_NAME + "." + "roaster ros on ros.rota_id= dailyRoaster.rota_id where  ros.created_on >=?1 and ros.created_on <=?2"
		    + ")r  where row_num = 1", nativeQuery = true)
	List<Object[]> busNotSentOnRouteReport(Date startDate, Date endDate);*/
	
	/*@Query(value="select bus_type_name,bus_reg_number,rota_date  from ("
+"SELECT  distinct busType.bus_type_name,bus.bus_reg_number,ros.rota_date ,DENSE_RANK() OVER (" 
+"PARTITION BY bus.bus_reg_number ORDER BY ros.rota_date desc) as row_num " 
+"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dailyRoaster "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details bus on  bus.bus_id = dailyRoaster.bus_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type busType on busType.bus_type_id = bus.bus_type " 
+"join "+RestConstants.SCHEMA_NAME+"."+"roaster ros on ros.rota_id= dailyRoaster.rota_id "
+"where to_date(to_char(ros.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 )r where row_num = 1",nativeQuery=true)*/
	
	@Query(value="select mt.transport_name, mbt.bus_type_name, mbd.bus_reg_number, "
			+ "(select max(to_date(to_char(r1.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) from "+RestConstants.SCHEMA_NAME+"."+"roaster r1 join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr1 on dr1.rota_id =r1.rota_id "
			+ "where dr1.bus_id = mbd.bus_id ) date_of_last_route "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mbd.transport_unit "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			+ "where not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
			+ "where mbd.bus_id = dr.bus_id and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2) ", nativeQuery=true)
   List<Object[]> busNotSentOnRouteReport(Date startDate, Date endDate);
	
	/*
	@Query(value = "select driver_code,driver_name,conductor_code,conductor_name,rota_date  from ("
			+ "SELECT  distinct driver.driver_code,driver.driver_name,ros.rota_date ,conductor.conductor_code,conductor.conductor_name ,DENSE_RANK() OVER ( "
			+ "PARTITION BY driver.driver_code ORDER BY ros.rota_date desc) as row_num "
			+ "from " + RestConstants.SCHEMA_NAME + "." +"daily_roaster dailyRoaster "
			+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_driver_details driver on  driver.driver_id = dailyRoaster.driver_id"
			+" join "+ RestConstants.SCHEMA_NAME + "." + "mst_conductor_details conductor on  conductor.conductor_id= dailyRoaster.conductor_id" 
		    + " join "+ RestConstants.SCHEMA_NAME + "."+"roaster ros on ros.rota_id= dailyRoaster.rota_id where driver.driver_id=?1 "
		    		+ " and ros.created_on >=?2 and ros.created_on <=?3 "
		    + ")r  where row_num = 1", nativeQuery = true)
	List<Object[]> driverOrConductorNotSentOnRouteReport(Integer driverId,Date startDate, Date endDate);*/
	
	
/*	@Query(value="select driver_code, driver_name, conductor_code, conductor_name, rota_date  from ("
+"SELECT  distinct driver.driver_code, driver.driver_name, ros.rota_date , conductor.conductor_code, conductor.conductor_name," 
+"DENSE_RANK() OVER ( PARTITION BY driver.driver_code ORDER BY ros.rota_date desc) as row_num "+"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dailyRoaster " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details driver on  driver.driver_id = dailyRoaster.driver_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details conductor on  conductor.conductor_id= dailyRoaster.conductor_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"roaster ros on ros.rota_id= dailyRoaster.rota_id "
+"where driver.driver_id=74 and to_date(to_char(ros.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between '2022-01-14' and '2022-01-24' )r where row_num = 1",nativeQuery=true)
*/	

	
	
	
	/*@Query(value = "select distinct mt.trip_start_time as Time, mbt.bus_type_name as Bus_Type, mbd.bus_reg_number as Bus_Number, mdd.driver_code as Driver_number,"
			+ "mcd.conductor_code as Conductor_number "
			+ "from " + RestConstants.SCHEMA_NAME + "." + "daily_roaster dr join " + RestConstants.SCHEMA_NAME + "." + " mst_trip mt on dr.trip_id = mt.trip_id"
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_details mbd on dr.bus_id = mbd.bus_id"
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_driver_details mdd on dr.driver_id = mdd.driver_id"
            + " join " + RestConstants.SCHEMA_NAME + "." + "mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id"
            + " join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id"
            + " where mt.up_down = 'UP' and mt.created_on >=?1 and mt.created_on <=?2", nativeQuery = true)
	List<Object[]> busGoingAwayFromDepotReport(Date startDate, Date endDate);*/

	/*@Query(value="select distinct mt.trip_start_time, mbt.bus_type_name as Bus_Type, mbd.bus_reg_number as Bus_Number, mdd.mobile_number as Driver_number,"
+"mcd.mobile_number as Conductor_number "+"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
+"where to_date(to_char(dr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
	List<Object[]> busGoingAwayFromDepotReport(Date startDate, Date endDate);
	*/



/*@Query(value="select distinct mt.trip_start_time, mbt.bus_type_name as Bus_Type, mbd.bus_reg_number as Bus_Number,"
+"mdd.mobile_number as Driver_number,mcd.mobile_number as Conductor_number,"
+"'' link_vts from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
+"where to_date(to_char(dr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
*/
 @Query(value="select distinct concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time) as time, mbt.bus_type_name as Bus_Type, "
          +"mbd.bus_reg_number as Bus_Number, '' link_vts, mdd.driver_code , mdd.driver_name, mcd.conductor_code, mcd.conductor_name "
          +"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
          +"left join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
          +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id "
          +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
          +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
          +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
          +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id "
          +"where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2" , nativeQuery=true)
 List<Object[]> busGoingAwayFromDepotReport(Date startDate, Date endDate);



	/*@Query(value = "select distinct mt.trip_start_time as Time, mbt.bus_type_name as Bus_Type, mbd.bus_reg_number as Bus_Number, mdd.driver_code as Driver_number,"
			+ "mcd.conductor_code as Conductor_number "
			+ " from " + RestConstants.SCHEMA_NAME + "." + "daily_roaster dr join " + RestConstants.SCHEMA_NAME + "." + " mst_trip mt on dr.trip_id = mt.trip_id"
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_details mbd on dr.bus_id = mbd.bus_id"
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_driver_details mdd on dr.driver_id = mdd.driver_id"
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id"
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id"
			+ " where mt.up_down = 'DOWN'  and mt.created_on >=?1 and  mt.created_on <=?2", nativeQuery = true)
	List<Object[]> busComingBackToDepotReport(Date startDate, Date endDate);*/
	
	/*@Query(value="select distinct mt.trip_end_time as Time, mbt.bus_type_name as Bus_Type, mbd.bus_reg_number as Bus_Number, mdd.mobile_number as Driver_number,"
+"mcd.mobile_number as Conductor_number "+"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
+"where to_date(to_char(dr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
	List<Object[]> busComingBackToDepotReport(Date startDate, Date endDate);*/


/*@Query(value="select distinct mt.trip_end_time as Time, mbt.bus_type_name as Bus_Type,"
+"mbd.bus_reg_number as Bus_Number, mdd.mobile_number as Driver_number,"
+"mcd.mobile_number as Conductor_number, '' link_vts from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
+"where to_date(to_char(dr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)*/

 @Query(value="select distinct concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_end_time) as time, mbt.bus_type_name as Bus_Type, "
         +"mbd.bus_reg_number as Bus_Number, '' link_vts, mdd.driver_code , mdd.driver_name, mcd.conductor_code, mcd.conductor_name "
         +"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
         +"left join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
         +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on dr.trip_id = mt.trip_id "
         +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
         +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
         +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
         +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id "
         +"where to_date(to_char(dr.rotation_availability_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2" , nativeQuery=true) 
List<Object[]> busComingBackToDepotReport(Date startDate, Date endDate);

	
	@Query(value = "SELECT r from DailyRoaster r where r.rota.id = ?1 order by r.route.id asc,r.trip.id asc,r.tripRotation.id asc ")
	List<DailyRoaster> findAllByRotaId(Integer rotaId);
	
	/*
	 *Roaster Details By pass Conductor Id 
	 *where trip false with refueling assuming conductor has not came back for refueling and trip is yet to start
	 * */
//	@Query("select daily from DailyRoaster daily where daily.conductor.id =?1 and daily.tripStatus=false and daily.isDeleted=false and daily.refueling is null order by daily.id desc")
	@Query("select daily from ConductorRotaHistory daily where daily.conductor.id =?1 and daily.submitEtmTicketBoxEntity is null order by daily.id asc")
	List<ConductorRotaHistory> fetchRosterDetailsByCondutorId(Integer conductorId);
	
	@Query(value = "SELECT auto from DailyRoaster auto where auto.route.id = ?1 and auto.bus.id = ?2 and auto.driver.id = ?3")
	List<DailyRoaster> findByRouteBusAndDriver(Integer routeId, Integer busId,Integer driverId);
	
	
	@Query(value="select roaster_id,bus_id,driver_id,conductor_id,rotation_availability_date from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster r where date_trunc('DAY',created_on) = date_trunc('DAY', now()) + INTERVAL '-1 DAY' and r.trip_rotation=?1", nativeQuery=true)
	Object[] findByRotationAndDate(Integer id);
	
	
	@Query(value="select distinct dr.driver_id,mbr.kmpl_as_scheduled_kilometer,mbr.bus_refueling_id from " + RestConstants.SCHEMA_NAME + "." + " daily_roaster dr  "
			+ "inner join " + RestConstants.SCHEMA_NAME + "." + " mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id and dr.bus_id = mbr.bus_id and "
					+ " mbr.route_master = dr.route_id "
			+ " where mbr.route_master =?1 and mbr.bus_id =?2 order by mbr.kmpl_as_scheduled_kilometer desc limit 15", nativeQuery=true)
	List<Object[]> findMaxKmplForDriver(Integer routeId,Integer busId);
	
	
	
	@Query(value="select  assign.conductor_id,earning.earning_perkm from " + RestConstants.SCHEMA_NAME + "." + " conductor_roaster_history his "
			+ " join " + RestConstants.SCHEMA_NAME + "." + " etm_assignment assign on his.conductor_id = assign.conductor_id "
					+ " join " + RestConstants.SCHEMA_NAME + "." + " earning_from_etm earning on earning.assignment_id = assign.etm_assignment_id "
					+ " order by earning.earning_perkm desc,his.conductor_roaster_id desc ", nativeQuery=true)
	List<Object[]> findMaxEPKMForConductor();
	
	@Query(value = "SELECT r from DailyRoaster r where r.rota.id != ?1 and r.rotationAvailabilityDate =?2 order by r.route.id asc,r.trip.id asc,r.tripRotation.id asc ")
	List<DailyRoaster> findAllCompletingSameDay(Integer rotaId,Date todayDate);
	
	@Query(value = "SELECT r from DailyRoaster r where r.rota.id = ?1 and r.trip.id =?2")
	List<DailyRoaster> findAllByRotaAndTripId(Integer rotaId,Integer tripId);
	
	@Query(value="select min(roaster_id) , route_id, trip_id ,bus_id ,driver_id ,conductor_id  from " + RestConstants.SCHEMA_NAME + "." + " daily_roaster dr where rota_id =?1 "
			+ " group by  route_id, trip_id ,bus_id ,driver_id ,conductor_id   "
			+ " order by route_id ,trip_id", nativeQuery=true)
	List<Object[]> fetchRosterDetailsByRotaId(Integer rotaId);
	
	@Modifying
    @Query(value = "Update DailyRoaster r  set r.remarks = ?1 where r.id=?2 ")
	int updateDailyRoasterById(String remarks,Integer dailyRoasterId);
	
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr where (driver_id =?1 or conductor_id =?2 or bus_id =?3 or route_id =?4 ) and created_on between ?5 and ?6")
	List<DailyRoaster> findLastThirtyDaysRecord(Integer driverId,Integer conductorId,Integer busId,Integer routeId,Date fromDate, Date toDate); 
	
	@Query(value = "SELECT r from DailyRoaster r where r.refueling.id = ?1 ")
	List<DailyRoaster> findAllByRefuellingId(Integer refuellingId);
 		
	@Query(value = "select t.sparename,t.sparetype,t.remarks,t.from_dt,t.to_dt from ("
			+ "select mbd.bus_reg_number sparename,'BUS' sparetype,ms.remarks,ms.from_dt,ms.to_dt from "+RestConstants.SCHEMA_NAME+"."+" mark_spare ms"					
					+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd  on mbd.bus_id = ms.bus_id "
					+ " union all select mdd.driver_name sparename,'Driver' sparetype,ms.remarks,ms.from_dt,ms.to_dt from "+RestConstants.SCHEMA_NAME+"."+"mark_spare ms join  "+RestConstants.SCHEMA_NAME+"."+" mst_driver_details mdd on ms.driver_id = mdd.driver_id"
					+ " union all select mcd.conductor_name sparename,'Conductor' sparetype,ms.remarks,ms.from_dt,ms.to_dt from "+RestConstants.SCHEMA_NAME+"."+"mark_spare ms join  "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mcd on ms.conductor_id = mcd.conductor_id"
					+ ")t where to_date(?1, 'YYYY/MM/DD') between t.from_dt and t.to_dt", nativeQuery=true)
	List<Object[]> spareList(Date rotaDate);
	
	@Query(value = "SELECT b from Roaster b where b.id = ?1")
	Roaster findByRotaId(Integer id);
	
	/*@Query(value = "SELECT  daily from DailyRoaster daily where daily.bus.id = ?1 order by daily.rotationAvailabilityDate desc limit 0,1")
	DailyRoaster findDailyByBusId(Integer id);*/
	
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster where bus_id=?1 order by rotation_availability_date desc limit 1")
	public DailyRoaster findDailyByBusId(Integer id);
	
	@Query("select daily from ConductorRotaHistory daily where daily.conductor.id =?1  and daily.trip.id=?2 order by daily.id desc")
	List<ConductorRotaHistory> fetchRosterDetailsByCondutorIdAndTrip(Integer conductorId,Integer tripId);
	
}
