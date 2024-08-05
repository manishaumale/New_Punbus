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
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.QDriverMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface DriverMasterRepository extends JpaRepository<DriverMaster, Integer> , QuerydslPredicateExecutor<DriverMaster>, QuerydslBinderCustomizer<QDriverMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDriverMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<DriverMaster> findAllByStatus(boolean status);
	
	@Modifying
    @Query(value = "Update DriverMaster  set status=?1 where id=?2 ")
	int updateDriverMasterStatusFlag(Boolean flag, Integer id);
	
	@Modifying
    @Query(value = "Update DriverMaster  set isDeleted=?1 where id=?2 ")
	int updateDriverMasterIsDeletedFlag(Boolean flag, Integer id);
	
	List<DriverMaster> findAllByIsDeleted(boolean IsDeleted);
	
	List<DriverMaster> findAllByStatusAndIsDeleted(boolean status,boolean flag);
	
	
	@Query(value = "Select d from DriverMaster d where d.depot.depotCode = ?1 and d.isDeleted=false order by d.id desc")
	List<DriverMaster> findAllByDepot(String dpCode);
	
	@Query(value = "Select d from DriverMaster d where d.depot.depotCode = ?1 and d.isDeleted=false and d.status=true ")
	List<DriverMaster> findAllByDepotByStatus(String dpCode);

	@Query(value = "select mdd.driver_id, mdd.driver_name, case when drh2.return_time is null then '06:00:00' else drh2.return_time  end as return_time, mc.city_id,route.route_category_name "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"driver_roaster_history drh2 on drh2.driver_id=mdd.driver_id and drh2.is_deleted=false and drh2.return_time in (select max(drh.return_time) from "+RestConstants.SCHEMA_NAME+"."+"driver_roaster_history drh where drh.trip_status='P' and drh.return_date = date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and drh.driver_id=drh2.driver_id and drh.is_deleted=false group by drh.driver_id) "
			+ " and drh2.trip_status='P' and drh2.return_date = date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY'and drh2.up_down='DOWN' and drh2.return_time <= '19:00:00' "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = drh2.trip_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_city mc on mc.city_id = mt.to_city "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category route on mdd.driver_category = route.route_category_id "
			+ " where mdd.status=true and mdd.is_deleted =false and mdd.depot_id=?1 and mdd.transport_unit=?2 "
			+ " and mdd.driver_id not in (select mdu.driver_id from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where  mdu.from_date <= date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and mdu.to_date >= date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY') "
			+ " and mdd.driver_id not in (select drh.driver_id from "+RestConstants.SCHEMA_NAME+"."+"driver_roaster_history drh where drh.trip_status='P' and drh.return_date > date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and drh.is_deleted=false) "
			+ " and mdd.driver_id not in (select drh.driver_id from "+RestConstants.SCHEMA_NAME+"."+"driver_roaster_history drh where drh.return_date = date_trunc('DAY', cast(?3 as timestamp)) + INTERVAL '1 DAY' and drh.return_time > '19:00:00' and drh.is_deleted=false) "
			+ " order by mdd.driver_id ", nativeQuery=true)
	List<Object[]> getAvailableDrivers(Integer depotId, Integer tpId, Date rotaDate);

	@Query(value="select count(*) as totalDrivers, (select count(*) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd2 where status=true and mdd2.transport_unit in (?1)) as activeDrivers "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd where transport_unit in (?1) ", nativeQuery=true)
	List<Object[]> getTotalDrivers(List<Integer> tpIds);

	@Query(value="select count(*) as totalDrivers,"
+"sum(case when mdd.is_deleted =false and mdd.status =true and mdd.driver_id not in (select driver_id from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable "
+ " where  date_trunc('DAY',to_date) > date_trunc('DAY', cast(CURRENT_DATE as timestamp)) and status_id=2)  then 1 else 0 end )as activeDrivers "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd where depot_id in (?1)", nativeQuery=true)
List<Object[]> getTotalDriversByDepot(Integer id);
	
	@Query(value = "select distinct mdd.driver_id ,mt.trip_end_time availabletime,route.route_category_name "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category route on mdd.driver_category = route.route_category_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster a on a.driver_id = mdd.driver_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+" roaster b on a.rota_id =b.rota_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id =a.trip_id and a.trip_id not in "
			+ " (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr where  dr.route_id =a.route_id group by trip_id )t "
			+ " where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') != to_date(to_char(b.generation_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) "
			+ " and b.generation_date = now() and mdd.depot_id=?1 and mdd.transport_unit=?2 and mdd.driver_id not in "
			+ " (select mdu.driver_un_availability_id from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.from_date  >= ?3) ", nativeQuery=true)
	List<Object[]> getAvailableDriversForDailyRota(Integer id, Integer tpId, Date date);
	
	
	@Query(value = "with driverAvailable as(select distinct dr.driver_id from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"roaster r2 on r2.rota_id = dr.rota_id "
			+ " where to_date(to_char(r2.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 and "
			+ " dr.driver_id not in (select distinct dr.driver_id  from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr  "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt  on dr.trip_id = mt.trip_id inner join "
			+ "(select trip_id from (select trip_id ,max(rotation_availability_date) avd "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr group by trip_id )t "
			+ " where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3) "
			+ " a on a.trip_id = dr.trip_id)) "
			+ " select distinct mbd.driver_id,mt.trip_end_time,route.route_category_name from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mbd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster daily on daily.driver_id = mbd.driver_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt on mt.trip_id = daily.trip_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category route on mbd.driver_category = route.route_category_id "
			+ " where mbd.driver_id not in (select distinct driver_id from driverAvailable) and mbd.depot_id =?1 and mbd.transport_unit =?2 ", nativeQuery=true)
	List<Object[]> getAvailableDriversForRoaster(Integer id, Integer tpId, Date date);
	
	
	
	
	@Query(value = " select t.driver_id,t.trip_end_time,cat.route_category_name,mbd2.driver_code,mbd2.driver_name from (select mbd.driver_id,null trip_end_time from "+RestConstants.SCHEMA_NAME+"."+" mst_driver_details mbd "
			+ " where mbd.driver_id not in (select distinct dr.driver_id from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr join  "+RestConstants.SCHEMA_NAME+"."+"roaster r2 on r2.rota_id = dr.rota_id "
			+ " where to_date(to_char(r2.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 and dr.driver_id not in "
			+ " (select distinct dr.driver_id  from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr inner join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt  on dr.trip_id = mt.trip_id "
			+ " inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr "
			+"	group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3  )a on a.trip_id = dr.trip_id)) "
			+"  and mbd.driver_id not in (select distinct dr.driver_id  from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr inner join "
			+" "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt  on dr.trip_id = mt.trip_id inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
			+ " group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 )a on a.trip_id = dr.trip_id) union all " 
			+ " select distinct dr.driver_id,mt.trip_end_time  from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr inner join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt  on dr.trip_id = mt.trip_id " 
			+ " inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr "
			+ "  group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 )a on a.trip_id = dr.trip_id "
			+ " )t inner join "+RestConstants.SCHEMA_NAME+"."+" mst_driver_details mbd2 on mbd2.driver_id = t.driver_id inner join  "+RestConstants.SCHEMA_NAME+"."+"mst_route_category cat "
			+ "on cat.route_category_id = mbd2.driver_category where mbd2.depot_id =?1 and mbd2.transport_unit =?2 "
			/*+ " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" driver_block_history block where block.driver_id = mbd2.driver_id "
            + "  and date_trunc('DAY',block.to_date) > date_trunc('DAY', cast(?3 as timestamp))) "*/
			 + " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" authorize_route auRoute where auRoute.driver_id = mbd2.driver_id) "
			+ " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" block_route_off block where block.driver_id = mbd2.driver_id and is_blocked = true) "
			+ " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" mst_driver_unavailable mbu where mbu.driver_un_availability_id = mbd2.driver_id "
            + "  and date_trunc('DAY',mbu.to_date) > date_trunc('DAY', cast(?3 as timestamp))) ", nativeQuery=true)
	List<Object[]> getAvailablilityDriversForRota(Integer id, Integer tpId, Date date);
	
	
	/*@Query(value="select mdd.driver_name, mdd.mobile_number as driver_number,'' order_no,"
+ " to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as from_date,"
+"to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as to_date, mdu.detailed_reason as remarks from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu on mdd.driver_id = mdu.driver_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason mdur on mdur.reason_id = mdu.driver_unavailability_reason_id where mdur.reason_id = 2 "
+"and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'),'YYYY/MM/DD') between ?1 and ?2 ) "
+"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ))",nativeQuery=true)*/
	 
  @Query(value="select mdd.driver_name, mdd.mobile_number as driver_number, to_date(to_char(dbh.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as from_date, "
	   		+ "to_date(to_char(dbh.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as to_date, dbh.reason as remarks, to_date(to_char(dbh.order_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') order_date , "
	   		+ "dbh.order_number "
	   		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"driver_block_history dbh on dbh.driver_id = mdd.driver_id "
	   		+ "where ((to_date(to_char(dbh.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2) "
	   		+ "or (to_date(to_char(dbh.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2))" , nativeQuery=true)	
	   List<Object[]> getdisciplinaryActionReportDriverNames(Date startDate, Date endDate);



@Query(value="select mdd.mobile_number as driver_number, mdd.driver_name, to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Absent_from, "
		+ "to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Absent_to, "
		+ "(date_part('day',mdu.to_date - mdu.from_date)+1) Absent_days "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu on mdd.driver_id = mdu.driver_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason mdur on mdur.reason_id = mdu.driver_unavailability_reason_id where mdur.reason_id = 2 "
		+ "and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') >= ?1)  "
		+ "and (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') <=?2)) "
		+ "order by mdd.driver_name ",nativeQuery=true)
List<Object[]> getabsentReportDriverNames(Date startDate, Date endDate);

@Modifying
@Query(value = "Update DriverMaster d  set d.driverCategory.id = ?2 where d.id=?1 ")
int updateDriverMasterCategoryById(Integer driverId, Integer categoryId);

@Modifying
@Query(value="select mdd.driver_name, mdd.mobile_number as driver_number, "
		//+ "(select mr2.route_name from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr2 join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh on mr2.id = drh.old_route_id where mr.id=mr2.id) as old_route_name, "
		+ "dr.override_reason, mr.route_name as new_route, dr.remarks from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on drh.roaster_id= dr.roaster_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2", nativeQuery=true)
List<Object[]> dutyInspectorOverrideReport(Date startdate , Date endDate);

@Modifying
@Query(value="select mdd.mobile_number as driver_number, mdd.driver_name, ''Suspended_since from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
	    +"where to_date(to_char(mdd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2" , nativeQuery=true)
List<Object[]> getSuspensionReport(Date startDate, Date endDate);

@Query(value="select mobile_number, driver_name, driver_code, rota_date as off_route_since from ( "
		+ "SELECT driver.mobile_number, driver.driver_name, driver.driver_code, max(to_date(to_char(ros.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) as rota_date, "
		+ "DENSE_RANK() OVER ( PARTITION BY driver.driver_code ORDER BY ros.rota_date desc) as row_num from  "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dailyRoaster  "
		+ "join  "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details driver on  driver.driver_id = dailyRoaster.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster ros on ros.rota_id= dailyRoaster.rota_id "
		+ "where to_date(to_char(ros.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "group by driver.mobile_number, driver.driver_name,ros.rota_date, driver.driver_code)r "
		+ "where row_num = 1  order by 2" , nativeQuery=true)
List<Object[]> driverOrConductorNotSentOnRouteReport(Date startDate, Date endDate);

/*@Query(value="select mdd.driver_name, mdd.mobile_number as driver_number, "
          +"((date_part('day', timestamp ?2 - timestamp ?3)+1)-coalesce((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where (mdu.driver_unavailability_reason_id !=2 "
          +"or mdu.driver_unavailability_reason_id = 2 ) and mdu.driver_id = mdd.driver_id "
          +"and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
          +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0)) total_present, "
          +"coalesce((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id = 2 "
          +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
          +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) Total_Absent, "
          +"coalesce ((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id != 2 "
          +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
          +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) Total_on_leave, "
          +"coalesce ((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id = 1 "
          +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
          +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) rest, "
          +"coalesce ((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id= 3 "
          +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
          +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) CL "
          +"from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd where mdd.employment_type =?1" , nativeQuery=true)*/

     @Query(value= "select mdd.driver_name, mdd.mobile_number as driver_number,((date_part('day',cast(?3 as timestamp) - cast(?2 as timestamp))+1)-coalesce((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) " 
        +"from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where (mdu.driver_unavailability_reason_id !=2 or mdu.driver_unavailability_reason_id = 2 ) "
        +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
        +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0)) total_present, "
        +"coalesce((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id = 2 "
        +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
        +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) Total_Absent, "
        +"coalesce ((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id != 2 "
        +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
        +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) Total_on_leave, "
        +"coalesce ((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id = 1 "
        +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
        +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) rest, "
        +"coalesce ((select sum((date_part('day',mdu.to_date - mdu.from_date)+1)) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu where mdu.driver_unavailability_reason_id= 3 "
        +"and mdu.driver_id = mdd.driver_id and ((to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
        +"or (to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))),0) CL "
        +"from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd where (mdd.employment_type =?1 or 0=?1)", nativeQuery=true)

List<Object[]> getDriverOrConductorLeaveReport(Integer driverType,Date startDate, Date endDate);
	
@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details where blocked=true")
List<DriverMaster> findAllBlockedDrivers();

@Query(value="select mdd.driver_code, mdd.driver_name, mdd.mobile_number as driver_number, od.order_no , to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as from_date ,  "
		+ "to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') , mdt.duty_type_name, od.remarks "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"other_duty od "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = od.driver_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_duty_type mdt on mdt.duty_type_id = od.duty_type_id "
		+ "where ((to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2) "
		+ "or (to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2))" , nativeQuery=true)
List<Object[]> driverPerformingOtherDuty(Date startDate, Date endDate);


@Query(value="select mdd.driver_code, mdd.driver_name,  mdd.mobile_number as driver_number, to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')as from_date , "
		+ "to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') , mdt.duty_type_name, od.remarks "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"other_duty od "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = od.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_duty_type mdt on mdt.duty_type_id = od.duty_type_id "
		+ "where  (mdd.employment_type =?1 or 0=?1) "
		+ "and ((to_date(to_char(od.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) "
		+ "or (to_date(to_char(od.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3))" , nativeQuery=true)
List<Object[]> totalDriversOnotherdutyreport(Integer type,Date startDate, Date endDate);

@Query(value="select to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')as date, mdd.driver_code, (coalesce(mr.scheduled_kms ,0)) as KMS, mdd.driver_name , mr.route_name "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "where dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id and dr2.bus_id= dr.bus_id "
		+ "and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) "
		+ "and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "order by 1 desc",nativeQuery=true)
List<Object[]> dateWiseDriverProductiveReport(Date startDate , Date endDate);

@Query(value="select mdd.driver_code, mdd.driver_name, to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, mr.route_name, coalesce (mbr.kmpl_as_scheduled_kilometer,0) as KMPL "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.driver_id =dr.driver_id and dr2.refueling_id  = dr.refueling_id) "
		+ "order by 3 desc " , nativeQuery=true)
List<Object[]> detailBusDriverConductorReport(Date startDate , Date endDate);

@Query(value="select md.depot_name, mdd.mobile_number as driver_number, mdd.driver_name, to_date(to_char(drh.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date_of_Inclusion, "
		+ "mr.route_name as Deputed_Route, 1 as KMPL_per_Receipt_1_Month, dr.override_reason "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on drh.roaster_id= dr.roaster_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mr.depot_id = md.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
		+ "where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
List<Object[]> staffPerformanceReport( Date fromDate , Date endDate);

@Query(value="select md.depot_name, mdd.driver_code, mdd.driver_name, mdd.mobile_number as driver_number, ra.rest_count rests_due "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mdd.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"rest_allocation ra on ra.driver_id = mdd.driver_id "
		+ "where to_date(to_char(ra.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
List<Object[]> restDueReport( Date fromDate , Date endDate);

@Query(value="select mdd.driver_code, mdd.driver_name, mdd.mobile_number as driver_number, mr.route_name, to_date(to_char(ar.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') from_date, "
		+ "(to_date(to_char(ar.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD')+30) to_date, ar.reason "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"authorize_route ar "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = ar.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = ar.route_id "
		+ "where to_date(to_char(ar.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
List<Object[]> authorizedRouteReport( Date fromDate , Date endDate);


@Query(value="select mdd.driver_code, mdd.driver_name, to_date(to_char(mdu.from_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') from_date, "
		+ "to_date(to_char(mdu.to_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') to_date, mdur.reason "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu on mdu.driver_id = mdd.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason mdur on mdur.reason_id = mdu.driver_unavailability_reason_id "
		+ "where (date_part('month', mdu.from_date)= ?1 or date_part('month', mdu.to_date)= ?1)" , nativeQuery=true)
List<Object[]> attendanceReportDetails(Integer month);


@Query(nativeQuery=true,value="select mdd.driver_id , mdd.driver_name from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd where not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"block_route_off a where a.driver_id =mdd.driver_id and a.is_blocked=true) and depot_id=?1 order by driver_id")
List<Object[]> findUnblockedDrivers(Integer depot);

@Query(nativeQuery=true,value="select * from  "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details where driver_code =?1 and status=true")
DriverMaster findByDrivercode(String name);

@Query(value="select mdd.driver_code ,mdd.driver_name,mdd.mobile_number,met.enrolment_name,md.depot_name ,mt.transport_name,mrc.route_category_name "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mdd.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id  = mdd.transport_unit "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type met on met.enrolment_id =mdd.employment_type  "
		+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mrc.route_category_id=mdd.driver_category "
		+ "where mdd.depot_id in (?1)",nativeQuery=true)
List<Object[]> getAllDriverDetails(Integer depoId);

@Query(value="select mdd.driver_code ,mdd.driver_name ,mdd.mobile_number,met.enrolment_name,md.depot_name ,mt.transport_name ,mrc.route_category_name "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mdd.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id  = mdd.transport_unit "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type met on met.enrolment_id =mdd.employment_type  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mrc.route_category_id=mdd.driver_category "
		+ "where mdd.depot_id in (?1) and  "
		+ "mdd.is_deleted =false and mdd.status =true and mdd.driver_id not in (select driver_id from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable "
		+ "where date_trunc('DAY',to_date) > date_trunc('DAY', cast(CURRENT_DATE as timestamp)) and status_id=2)",nativeQuery=true)
List<Object[]> getAllActiveDriverDetails(Integer depoId);

}
