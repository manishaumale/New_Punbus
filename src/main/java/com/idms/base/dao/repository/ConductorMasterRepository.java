package com.idms.base.dao.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.QConductorMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface ConductorMasterRepository extends JpaRepository<ConductorMaster, Integer> , QuerydslPredicateExecutor<ConductorMaster>, QuerydslBinderCustomizer<QConductorMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QConductorMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<ConductorMaster> findAllByStatus(boolean status);
	
	@Modifying
    @Query(value = "Update ConductorMaster  set status=?1 where id=?2 ")
	int updateConductorMasterStatusFlag(Boolean flag, Integer id);
	
	@Modifying
    @Query(value = "Update ConductorMaster  set isDeleted=?1 where id=?2 ")
	int updateConductorMasterIsDeletedFlag(Boolean flag, Integer id);
	
	List<ConductorMaster> findAllByIsDeleted(boolean IsDeleted);
	
	List<ConductorMaster> findAllByStatusAndIsDeleted(boolean status,boolean flag);
	
	@Query(value = "Select d from ConductorMaster d where d.depot.depotCode = ?1 and d.isDeleted=false order by d.id desc")
	List<ConductorMaster> findAllByDepot(String dpCode);
	
	@Query(value = "Select d from ConductorMaster d where d.depot.depotCode = ?1 and d.isDeleted=false and d.status=true ")
	List<ConductorMaster> findAllByDepotByStatus(String dpCode);

	@Query(value = " select mcd.conductor_id, mcd.conductor_name, case when crh2.return_time is null then '06:00:00' else crh2.return_time  end as return_time, mc.city_id,route.route_category_name "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"conductor_roaster_history crh2 on crh2.conductor_id=mcd.conductor_id and crh2.is_deleted=false "
			+ " and crh2.return_time in (select max(crh.return_time) from "+RestConstants.SCHEMA_NAME+"."+"conductor_roaster_history crh where crh.trip_status='P' and crh.return_date = date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and crh.conductor_id=crh2.conductor_id and crh.is_deleted=false group by crh.conductor_id) "
			+ " and crh2.trip_status='P' and crh2.return_date = date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and crh2.up_down='DOWN' and crh2.return_time <= '19:00:00' "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = crh2.trip_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_city mc on mc.city_id = mt.to_city "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category route on mcd.conductor_category = route.route_category_id "
			+ " where mcd.status=true and mcd.is_deleted=false and mcd.depot_id=?1 and mcd.transport_unit=?2 "
			+ " and mcd.conductor_id not in (select mdu.conductor_id from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mdu where  mdu.from_date <= date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and mdu.to_date >= date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY') "
			+ " and mcd.conductor_id not in (select crh.conductor_id from "+RestConstants.SCHEMA_NAME+"."+"conductor_roaster_history crh where crh.trip_status='P' and crh.return_date > date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and crh.is_deleted=false ) "
			+ " and mcd.conductor_id not in (select crh.conductor_id from "+RestConstants.SCHEMA_NAME+"."+"conductor_roaster_history crh where crh.return_date = date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and crh.return_time > '19:00:00' and crh.is_deleted=false) "
			+ " order by mcd.conductor_id ", nativeQuery=true)
	List<Object[]> getAvailableConductors(Integer id, Integer tpId, Date date);

	@Query(value="select count(*) as totalConductors, (select count(*) from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd2 where status=true and mcd2.transport_unit in (?1)) as activeConductors "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd where transport_unit in (?1) ", nativeQuery=true)
	List<Object[]> getTotalConductors(List<Integer> tpIds);

	/*@Query(value="select count(*) as totalConductors, (select count(*) from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd2 where status=true and mcd2.depot_id in (?1)) as activeConductors "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd where depot_id in (?1) ", nativeQuery=true)
	List<Object[]> getTotalConductorsByDepot(Integer id);*/
	
	@Query(value="select count(*) as totalConductors,"
+"sum(case when mcd.is_deleted =false and mcd.status = true and mcd.conductor_id not in (select conductor_id from "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_unavailable "
+ " where  date_trunc('DAY',to_date) > date_trunc('DAY', cast(CURRENT_DATE as timestamp)) and status_id=2) then 1 else 0 end )as activeConductors "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details  mcd where depot_id in (?1)",nativeQuery=true)
List<Object[]> getTotalConductorsByDepot(Integer id);
	
	
	@Query(value = "select distinct mdd.conductor_id ,mt.trip_end_time availabletime,route.route_category_name "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mdd "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category route on mdd.conductor_category = route.route_category_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster a on a.conductor_id = mdd.conductor_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+" roaster b on a.rota_id =b.rota_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id =a.trip_id and a.trip_id not in "
			+ " (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr where  dr.route_id =a.route_id group by trip_id )t "
			+ " where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') != to_date(to_char(b.generation_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) "
			+ " and b.generation_date = now() and mdd.depot_id=?1 and mdd.transport_unit=?2 and mdd.conductor_id not in "
			+ " (select mdu.conductor_un_availability_id from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mdu where mdu.from_date  >= ?3) ", nativeQuery=true)
	List<Object[]> getAvailableConductorsForDailyRota(Integer id, Integer tpId, Date date);
	
	@Query(value = "with conductorAvailable as(select distinct dr.conductor_id from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"roaster r2 on r2.rota_id = dr.rota_id "
			+ " where to_date(to_char(r2.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 and "
			+ " dr.conductor_id not in (select distinct dr.conductor_id  from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr  "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt  on dr.trip_id = mt.trip_id inner join "
			+ "(select trip_id from (select trip_id ,max(rotation_availability_date) avd "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr group by trip_id )t "
			+ " where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3) "
			+ " a on a.trip_id = dr.trip_id)) "
			+ " select distinct mbd.conductor_id,mt.trip_end_time,route.route_category_name from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mbd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster daily on daily.conductor_id = mbd.conductor_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt on mt.trip_id = daily.trip_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category route on mbd.conductor_category = route.route_category_id "
			+ " where mbd.conductor_id not in (select distinct conductor_id from conductorAvailable) and mbd.depot_id =?1 and mbd.transport_unit =?2 ", nativeQuery=true)
	List<Object[]> getAvailableDriversForRoaster(Integer id, Integer tpId, Date date);
	
	
	@Query(value = " select t.conductor_id,t.trip_end_time,cat.route_category_name,mbd2.conductor_code,mbd2.conductor_name from (select mbd.conductor_id,null trip_end_time from "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mbd "
			+ " where mbd.conductor_id not in (select distinct dr.conductor_id from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr join  "+RestConstants.SCHEMA_NAME+"."+"roaster r2 on r2.rota_id = dr.rota_id "
			+ " where to_date(to_char(r2.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 and dr.conductor_id not in "
			+ " (select distinct dr.conductor_id  from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr inner join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt  on dr.trip_id = mt.trip_id "
			+ " inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr "
			+"	group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3  )a on a.trip_id = dr.trip_id)) "
			+"  and mbd.conductor_id not in (select distinct dr.conductor_id  from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr inner join "
			+" "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt  on dr.trip_id = mt.trip_id inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ " group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 )a on a.trip_id = dr.trip_id) union all " 
			+ " select distinct dr.conductor_id,mt.trip_end_time  from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr inner join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt  on dr.trip_id = mt.trip_id " 
			+ " inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr "
			+ "  group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 )a on a.trip_id = dr.trip_id "
			+ " )t inner join "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mbd2 on mbd2.conductor_id = t.conductor_id inner join  "+RestConstants.SCHEMA_NAME+"."+"mst_route_category cat "
			+ "on cat.route_category_id = mbd2.conductor_category where mbd2.depot_id =?1 and mbd2.transport_unit =?2 "
			+ " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" block_route_off block where block.conductor_id = mbd2.conductor_id and is_blocked = true) "
			/*+ " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" conductor_block_history block where block.conductor_id = mbd2.conductor_id "
            + "  and date_trunc('DAY',block.to_date) > date_trunc('DAY', cast(?3 as timestamp))) "*/
            + " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" authorize_route auRoute where auRoute.conductor_id = mbd2.conductor_id) "
			+ " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_unavailable mbu where mbu.conductor_un_availability_id = mbd2.conductor_id "
            + "  and date_trunc('DAY',mbu.to_date) > date_trunc('DAY', cast(?3 as timestamp))) ", nativeQuery=true)
	List<Object[]> getAvailablilityConductorsForRota(Integer id, Integer tpId, Date date);
	
	@Query(value = "Select d from ConductorMaster d where d.depot.depotCode = ?1 and d.isDeleted=false and d.id not in ?2")
	List<ConductorMaster> getConductorRecordByDepot(String dpCode, List<Integer> conductorIdissueEtmTicketBoxEntity);
	
	@Query(value="select mcd.conductor_id,mcd.conductor_name from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd ",nativeQuery=true)
	List<Object[]> getConductorNames();
	
	
	/*@Query(value="select mcd.conductor_name, mcd.mobile_number as conductor_number,'' order_no,"
+"to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as from_date,"
+"to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as to_date, mcu.detailed_reason as remarks from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu on mcd.conductor_id = mcu.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason mdur on mdur.reason_id = mcu.conductor_unavailability_reason_id where mdur.reason_id = 2 "
+"and ((to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ) "
+"or (to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2))",nativeQuery=true)
*/	
	@Query(value="select mcd.conductor_name, mcd.mobile_number as conductor_number, to_date(to_char(cbh.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as from_date, "
	  		+ "to_date(to_char(cbh.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as to_date, cbh.reason as remarks, to_date(to_char(cbh.order_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') order_date , "
	  		+ "cbh.order_number "
	  		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
	  		+ "join "+RestConstants.SCHEMA_NAME+"."+"conductor_block_history cbh on cbh.conductor_id = mcd.conductor_id "
	  		+ "where ((to_date(to_char(cbh.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2) "
	  		+ "or (to_date(to_char(cbh.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2))" , nativeQuery=true)	
	  List<Object[]> getdisciplinaryActionReportConductorNames(Date startDate, Date endDate);

@Query(value="select mcd.mobile_number as conductor_number, mcd.conductor_name, (to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) as Absent_from, "
		+ "to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Absent_to, "
		+ "(date_part('day',mcu.to_date - mcu.from_date)+1) Absent_days "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu on mcd.conductor_id = mcu.conductor_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason mdur on mdur.reason_id = mcu.conductor_unavailability_reason_id where mdur.reason_id = 2 "
		+ "and ((to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') >=?1)  "
		+ "and (to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') <=?2)) "
		+ "order by mcd.conductor_name ",nativeQuery=true)
List<Object[]> getabsentReportConductorNames(Date startDate, Date endDate);

@Modifying
@Query(value = "Update ConductorMaster d  set d.conductorCategory.id = ?2 where d.id=?1 ")
int updateDriverMasterCategoryById(Integer conductorId, Integer categoryId);

@Modifying
@Query(value="select mcd.conductor_name, mcd.mobile_number as conductor_number, "
		//+ "(select mr2.route_name from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr2 join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh on mr2.id = drh.old_route_id where mr.id=mr2.id) as old_route_name, "
		+ "dr.override_reason, mr.route_name as new_route, dr.remarks from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on drh.roaster_id= dr.roaster_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = dr.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2", nativeQuery=true)
List<Object[]> dutyInspectorOverrideReport(Date startdate , Date endDate);

@Modifying
@Query(value="select mcd.mobile_number as conductor_number, mcd.conductor_name, ''Suspended_since from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
	+"where to_date(to_char(mcd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2" , nativeQuery=true)
List<Object[]> getSuspensionReport(Date startDate, Date endDate);

@Query(value="select mobile_number, conductor_name, conductor_code, rota_date as off_route_since  from ( "
		+ "SELECT conductor.mobile_number, conductor.conductor_name, conductor.conductor_code, max(to_date(to_char(ros.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) as rota_date,   "
		+ "DENSE_RANK() OVER ( PARTITION BY conductor.conductor_code ORDER BY ros.rota_date desc) as row_num from  "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dailyRoaster  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details conductor on  conductor.conductor_id= dailyRoaster.conductor_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster ros on ros.rota_id= dailyRoaster.rota_id "
		+ "where  to_date(to_char(ros.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "group by conductor.mobile_number, conductor.conductor_name,ros.rota_date,conductor.conductor_code)r "
		+ "where row_num = 1 order by 2" , nativeQuery=true)
List<Object[]> driverOrConductorNotSentOnRouteReport(Date startDate, Date endDate);

@Query(value="select mcd.conductor_name, mcd.mobile_number as conductor_number, "
		+ "((date_part('day',cast(?3 as timestamp) - cast(?2 as timestamp))+1)-coalesce((select sum((date_part('day',mcu.to_date - mcu.from_date)+1))  "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu where (mcu.conductor_unavailability_reason_id !=2 or mcu.conductor_unavailability_reason_id = 2 ) "
		+ "and mcu.conductor_id = mcd.conductor_id "
		+ "and ((to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "or (to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0)) total_present, "
		+ "coalesce((select sum((date_part('day',mcu.to_date - mcu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu where mcu.conductor_unavailability_reason_id = 2 "
		+ "and mcu.conductor_id = mcd.conductor_id "
		+ "and ((to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "or (to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) Total_Absent, "
		+ "coalesce ((select sum((date_part('day',mcu.to_date - mcu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu where mcu.conductor_unavailability_reason_id != 2 "
		+ "and mcu.conductor_id = mcd.conductor_id "
		+ "and ((to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "or (to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) Total_on_leave, "
		+ "coalesce ((select sum((date_part('day',mcu.to_date - mcu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu where mcu.conductor_unavailability_reason_id = 1 "
		+ "and mcu.conductor_id = mcd.conductor_id "
		+ "and ((to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "or (to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) rest, "
		+ "coalesce ((select sum((date_part('day',mcu.to_date - mcu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu where mcu.conductor_unavailability_reason_id= 3 "
		+ "and mcu.conductor_id = mcd.conductor_id "
		+ "and ((to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "or (to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) CL "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
		+ "where (mcd.employment_type = ?1 or 0=?1)" , nativeQuery=true)
	List<Object[]> getDriverOrConductorLeaveReport(Integer driverType,Date startDate, Date endDate);

	
	@Query(nativeQuery=true, value="select mcd.conductor_id,mcd.conductor_code,mcd.conductor_name,mcd.badge_number,mcd.blocked from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd where blocked=true")
	List<Object[]> findAllBlockedConductors();
	
	@Query(value="select mcd.conductor_name, mcd.mobile_number as conductor_number, od.order_no , to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as startDate,"
	        +"to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as endDate, mdt.duty_type_name "
	        +"from "+RestConstants.SCHEMA_NAME+"."+"other_duty od "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = od.conductor_id "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_duty_type mdt on mdt.duty_type_id = od.duty_type_id "
	       +"where ((to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2) "
	       +"or (to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2))" , nativeQuery=true)
	
	List<Object[]> conductorPerformingOtherDuty(Date startDate, Date endDate);
		
	@Query(value="select mcd.conductor_code, mcd.conductor_name, mcd.mobile_number as conductor_number, to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')as from_date , "
			+ "to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') , mdt.duty_type_name, od.remarks "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"other_duty od "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = od.conductor_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_duty_type mdt on mdt.duty_type_id = od.duty_type_id "
			+ "where  (mcd.employment_type =?1 or 0=?1) "
			+ "and ((to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
			+ "or (to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))" , nativeQuery=true)
	
	List<Object[]> totalConductorsOnotherdutyreport(Integer type,Date startDate, Date endDate);
	
	@Query(value="select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')as date, mcd.conductor_code, (coalesce(mr.scheduled_kms ,0)) as KMS, "
			+ "mcd.conductor_name , mr.route_name, ''epkm "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on dr.conductor_id = mcd.conductor_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
			+ "where dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
			+ "and dr2.trip_id=dr.trip_id and dr2.conductor_id = dr.conductor_id) "
			+ "and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
			+ "order by 1 desc",nativeQuery=true)
    List<Object[]> dateWiseConductorProductiveReport(Date startDate , Date endDate);
    
    @Query(value="select mcd.conductor_id, mcd.conductor_name, to_date(to_char(mbr.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, mr.route_name, ''EPKM  "
    		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
    		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
    		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = dr.conductor_id "
    		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
    		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
    		+ "where to_date(to_char(mbr.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
    		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.conductor_id =dr.conductor_id and dr2.refueling_id  = dr.refueling_id ) "
            +"order by 3 desc" , nativeQuery=true)
		List<Object[]> detailBusDriverConductorReport(Date startDate , Date endDate);
		
		@Query(value="select md.depot_name, mcd.mobile_number as conductor_number, mcd.conductor_name, to_date(to_char(drh.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date_of_Inclusion, "
				+ "mr.route_name as Deputed_Route, 1 as KMPL_per_Receipt_1_Month, dr.override_reason "
				+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on drh.roaster_id= dr.roaster_id "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = dr.driver_id "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mr.depot_id = md.depot_id "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
				+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
		List<Object[]> staffPerformanceReport( Date fromDate , Date endDate);
		
		@Query(value="select md.depot_name, mcd.conductor_code, mcd.conductor_name, mcd.mobile_number as conductor_number, ra.rest_count rests_due "
				+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mcd.depot_id "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"rest_allocation ra on ra.conductor_id = mcd.conductor_id "
				+ "where  to_date(to_char(ra.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
		List<Object[]> restDueReport( Date fromDate , Date endDate);
		
		@Query(value="select mcd.conductor_code, mcd.conductor_name, mcd.mobile_number as conductor_number, mr.route_name, to_date(to_char(ar.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') from_date, "
				+ "(to_date(to_char(ar.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD')+30) to_date, ar.reason "
				+ "from "+RestConstants.SCHEMA_NAME+"."+"authorize_route ar "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ar.conductor_id "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = ar.route_id "
				+ "where to_date(to_char(ar.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
		List<Object[]> authorizedRouteReport( Date fromDate , Date endDate);
		
		@Query(value="select mcd.conductor_code, mcd.conductor_name, to_date(to_char(mcu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') from_date, "
				+ "to_date(to_char(mcu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') to_date, mdur.reason "
				+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu on mcu.conductor_id = mcd.conductor_id "
				+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason mdur on mdur.reason_id = mcu.conductor_unavailability_reason_id "
				+ "where (date_part('month', mcu.from_date)= ?1 or date_part('month', mcu.to_date)= ?1)" , nativeQuery=true)
		List<Object[]> attendanceReportDetails(Integer month);

		@Query(nativeQuery = true, value = "select mdd.conductor_id,mdd.conductor_name from " + RestConstants.SCHEMA_NAME
				+ "."
				+ "mst_conductor_details mdd where not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"block_route_off a where a.conductor_id =mdd.conductor_id and a.is_blocked=true) and depot_id=?1 order by conductor_id")
		List<Object[]> findUnblockedConductors(Integer depot);
		
		@Query(nativeQuery=true,value="select * from  "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details where conductor_code =?1 and status=true")
		ConductorMaster findByConductorName(String name);
		
		 @Query(value="select mcd.conductor_code ,mcd.conductor_name ,mcd.mobile_number,met.enrolment_name,md.depot_name ,mt.transport_name,mrc.route_category_name "
					+"from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details  mcd "
					+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mcd.depot_id "
					+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id =mcd.transport_unit "
					+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type met on met.enrolment_id = mcd.employment_type "
					+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mrc.route_category_id =mcd.conductor_category "
					+"where mcd.depot_id in (?1)",nativeQuery=true)
					List<Object[]> getAllConductorList(Integer depoId);

					@Query(value="select mcd.conductor_code ,mcd.conductor_name ,mcd.mobile_number,met.enrolment_name,md.depot_name ,mt.transport_name,mrc.route_category_name "
							+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details  mcd "
							+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mcd.depot_id "
							+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id =mcd.transport_unit "
							+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type met on met.enrolment_id = mcd.employment_type "
							+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mrc.route_category_id =mcd.conductor_category "
							+ "where mcd.depot_id in (?1) and mcd.is_deleted =false and mcd.status = true and mcd.conductor_id not in (select conductor_id from "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_unavailable "
							+ "where  date_trunc('DAY',to_date) > date_trunc('DAY', cast(CURRENT_DATE as timestamp)) and status_id=2)",nativeQuery=true)
					List<Object[]>getAllActiveConductorList(Integer DepoId);
}
