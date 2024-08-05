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

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.QBusMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface BusMasterRepository extends JpaRepository<BusMaster, Integer> , QuerydslPredicateExecutor<BusMaster>, QuerydslBinderCustomizer<QBusMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBusMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<BusMaster> findAllByStatus(boolean status);
	
	@Modifying
    @Query(value = "Update BusMaster  set status=?1 where id=?2 ")
	int updateBusMasterStatusFlag(Boolean flag, Integer id);
	
	@Modifying
    @Query(value = "Update BusMaster  set isDeleted=?1 where id=?2 ")
	int updateBusMasterIsDeletedFlag(Boolean flag, Integer id);

	List<BusMaster> findAllByIsDeleted(boolean IsDeleted);
	
	List<BusMaster> findAllByStatusAndIsDeleted(boolean status,boolean flag);
	
	@Query(value = "SELECT b from BusMaster b where b.primaryDriver.id = ?1")
	List<BusMaster> findByDriverId(Integer id);
	
	@Query(value = "SELECT b from BusMaster b where b.depot.depotCode = ?1 and b.isDeleted=false order by b.id desc")
	List<BusMaster> findAllBusesByDepot(String dpCode);
	
	@Query(value = "SELECT b from BusMaster b where b.depot.depotCode = ?1 and b.isDeleted=false and b.status=true ")
	List<BusMaster> findAllBusesByDepotAndStatus(String dpCode);
	
	BusMaster findByBusRegNumber(String busNumber);

	@Query(value = "select mbd.bus_id, mbd.bus_reg_number, mbt.bus_type_name, mbtc.count as tc, count(bta.association_id),transport.transport_id,transport_name"
			+" ,(select bta1.association_id "+ " from "+RestConstants.SCHEMA_NAME+"."+" bus_tyre_association bta1"
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id =bta1.tyre_position_id"
			+ " where mtp.name='Spare Tyre' and bta1.bus_id = bta.bus_id) as association_id"
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on bta.bus_id = mbd.bus_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_tyre_count mbtc on mbtc.bus_tyre_count_id = mbd.tyre_count"
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport transport on transport.transport_id = mbd.transport_unit "
			+ " where mbd .depot = ?1 "
			+ " group by mbd.bus_id, mbd.bus_reg_number, mbt.bus_type_id ,bta.bus_id, mbtc.count,transport.transport_id,transport_name ", nativeQuery=true) 
	List<Object[]> getBusTyreAssociationList(Integer depotId);
	
	@Query(value="select  mbd.bus_id , mbd.bus_reg_number,mbt.tyre_count,count(bta.association_id) "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta ON mbd.bus_id = bta.bus_id"
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type"
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id =bta.tyre_position_id and mtp.name !='Spare Tyre'"
			+ " where mbd.depot = ?1"
			+ " group by mbd.bus_id , mbd.bus_reg_number, mbt.tyre_count"
			+ " having count(bta.association_id) >= mbt.tyre_count ", nativeQuery=true)
	List<Object[]> getAllBusesForTyreChange(Integer depotId);
	
	@Query(value = "select bta.association_id, mt.tyre_number,  mtm.name as makerName, mts.size, bta.kms_done, mtc.name as conditionName, bta.install_date, mtp.name as position,  "
			+ " mtm.tyre_maker_id,  mts.tyre_size_id, mtc.tyre_condition_id, mtp.tyre_position_id, mt.tyre_id,mt.tyre_cost,mt.expected_life,mt.tyre_tag "
			+ " from      "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on bta.bus_id = mbd.bus_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on mt.tyre_id =  bta.tyre_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = bta.tyre_condition_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id = bta.tyre_position_id  "
			+ " where mbd.bus_id=?1 ", nativeQuery = true)
	List<Object[]> getBusTyreAssocitionForTyreChange(Integer busId);

	@Query(value = "select mbd.bus_id, mbd.bus_reg_number,case when brh2.return_time is not null then brh2.return_time when mbu2.bus_ready_time is not null then mbu2.bus_ready_time else '06:00:00' end as available_time, mc.city_id,mbd.primary_driver,mbd.secondary_driver,mbd.bus_model "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_tyre_count mbtc on mbtc.bus_tyre_count_id = mbd.tyre_count "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"bus_roaster_history brh2 on brh2.bus_id = mbd.bus_id  and brh2.is_deleted=false "
			+ " and brh2.return_time in (select max(brh.return_time) from "+RestConstants.SCHEMA_NAME+"."+"bus_roaster_history brh where brh.trip_status='P' and brh.return_date = date_trunc('DAY', cast(?3 as timestamp)) and brh.bus_id=brh2.bus_id and brh.is_deleted =false group by brh.bus_id) "
			+ " and brh2.trip_status='P' and brh2.return_date = date_trunc('DAY', cast(?3 as timestamp)) and brh2.up_down='DOWN' and brh2.return_time <= '19:00:00' "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_unavailable mbu2 on mbu2.bus_id=mbd.bus_id and date_trunc('DAY', mbu2.likely_to_ready_date) = date_trunc('DAY', cast(?3 as timestamp)) and mbu2.bus_ready_time <= '19:00:00' "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = brh2.trip_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_city mc on mc.city_id = mt.to_city "
			+ " where mbd.status=true and mbd.is_deleted=false and mbd.depot=?1 and mbd.transport_unit=?2 "
			+ " and mbd.bus_id not in (select mbu.bus_id from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_unavailable mbu where date_trunc('DAY',mbu.detention_date) <= date_trunc('DAY', cast(?3 as timestamp)) and date_trunc('DAY',mbu.likely_to_ready_date) >= date_trunc('DAY', cast(?3 as timestamp)) and mbu.bus_ready_time > '19:00:00') "
			+ " and mbd.bus_id not in (select brh.bus_id from "+RestConstants.SCHEMA_NAME+"."+"bus_roaster_history brh where brh.trip_status='P' and brh.return_date > date_trunc('DAY', cast(?3 as timestamp)) and brh.is_deleted =false) "
			+ " and mbd.bus_id not in (select brh.bus_id from "+RestConstants.SCHEMA_NAME+"."+"bus_roaster_history brh where brh.return_date = date_trunc('DAY', cast(?3 as timestamp)) and brh.return_time > '19:00:00' and brh.is_deleted =false) "
			+ " and mbd.bus_id not in (select bta.bus_id from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id = bta.tyre_position_id where mtp.name!='Spare Tyre' group by bta.bus_id having count(bta.association_id)<mbtc.count) "
			+ " order by mbd.bus_id ", nativeQuery=true)
	List<Object[]> getAvailableBusesForRota(Integer id, Integer tpId, Date date);
	
	
	/*@Query(value = "SELECT depo.depot_name,bus.bus_reg_number,pos.name,pos.tyre_count"
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details bus"  
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association asso"  
			+ " on bus.bus_id = asso.bus_id"
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position pos"  
			+ " on asso.tyre_position_id = pos.tyre_position_id"
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depo"  
			+ " on bus.depot = depo.depot_id where bus.bus_id=?1 and to_date(to_char(bus.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ", nativeQuery=true) 
	List<Object[]> getBusTyreInventoryReport(Integer busId,Date startDate,Date endDate);
	*/
	/*@Query(value="SELECT depo.depot_name,bus.bus_reg_number,pos.name,pos.tyre_count "
	+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details bus "
			+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association asso on bus.bus_id = asso.bus_id "
	+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position pos on asso.tyre_position_id = pos.tyre_position_id "
			+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depo on bus.depot = depo.depot_id "
	+"where bus.bus_id=?1 and to_date(to_char(bus.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery= true)
List<Object[]> getBusTyreInventoryReport(Integer busId,Date startDate,Date endDate);*/
	
	@Query(value="SELECT md.depot_name, mbd.bus_reg_number, "
			+ "(select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id =bta.tyre_id "
			+ "where bta.tyre_position_id =2 and bta.association_id in (select max(bta2.association_id) from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta2 where bta.bus_id =bta2.bus_id "
			+ "and bta2.tyre_position_id = bta .tyre_position_id ) "
			+ "and mbd.bus_id = bta.bus_id and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Front_Right, "
			+ "(select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id =bta.tyre_id "
			+ "where bta.tyre_position_id =1 and mbd.bus_id = bta.bus_id and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Front_Left, "
			+ "(select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id =bta.tyre_id "
			+ "where bta.tyre_position_id =5 and mbd.bus_id = bta.bus_id and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Rear_Right_inner, "
			+ "(select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id =bta.tyre_id "
			+ "where bta.tyre_position_id =6 and mbd.bus_id = bta.bus_id and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Rear_Right_outer, "
			+ "(select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id =bta.tyre_id "
			+ "where bta.tyre_position_id =3 and mbd.bus_id = bta.bus_id and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Rear_Left_inner, "
			+ "(select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id =bta.tyre_id "
			+ "where bta.tyre_position_id =4 and mbd.bus_id = bta.bus_id and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Rear_Left_outer, "
			+ "(select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id =bta.tyre_id "
			+ "where bta.tyre_position_id =7 and mbd.bus_id = bta.bus_id and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Spare "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mbd.depot = md.depot_id "
			+ "where (mbd.bus_id=?1 or 0=?1)",nativeQuery=true)
List<Object[]> getBusTyreInventoryReport(Integer busId,Date fromdate,Date endDate);	
	
	
	/*@Query(value = "select depo.depot_name, count(distinct bus.bus_id),"
			+ " sum(case when asso.tyre_condition_id in (1) then 1 else 0 end) as newtyreCount,"
			+ " sum(case when asso.tyre_condition_id in (2) then 1 else 0 end) as resoleOne,"
			+ " sum(case when asso.tyre_condition_id in (3) then 1 else 0 end) as resoleTwo,"
			+ " sum(case when asso.tyre_condition_id in (4) then 1 else 0 end) as resoleThree"
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details bus"
			+" join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depo"
			+ " on bus.depot=depo.depot_id"
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association asso"  
			+ " on asso.bus_id = bus.bus_id"
            +   " where depo.depot_code=?1 and  to_date(to_char(bus.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
            +   " GROUP BY depo.depot_name", nativeQuery=true) 
	List<Object[]> getTotalBusesTyreReport(String depotCode,Date startDate,Date endDate);*/


   /*@Query(value="select depo.depot_name,count(distinct bus.bus_id),sum(case when asso.tyre_condition_id in (1) then 1 else 0 end) as newtyreCount,sum(case when asso.tyre_condition_id in (5) then 1 else 0 end) as resoleOne,sum(case when asso.tyre_condition_id in (6) then 1 else 0 end) as resoleTwo,sum(case when asso.tyre_condition_id in (7) then 1 else 0 end) as resoleThree "
   +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details bus "
		   +"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depo on bus.depot=depo.depot_id "
   +"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association asso on asso.bus_id = bus.bus_id "
		   +"where depo.depot_id=?1 and to_date(to_char(bus.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 GROUP BY depo.depot_name",nativeQuery=true)
  List<Object[]> getTotalBusesTyreReport(Integer depotId,Date startDate,Date endDate);*/
	
	
	
	@Query(value="select md.depot_name ,"
+"(select count(*) "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd where mbd.depot=md.depot_id and "
+"to_date(to_char(md.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Total_Buses,"
+"(select count(*) "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt where mt.tyre_condition_id =1 and mt.depot_id=md.depot_id "
+"and to_date(to_char(md.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ) Total_new_tyres,"
+"(select count(*) "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mt.tyre_position_id = mtp.tyre_position_id "
+"where mt.tyre_condition_id =1 and mt.depot_id=md.depot_id "
+"and to_date(to_char(md.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) New_tyre_position,"
+"(select count(*) "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt inner join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id =btah.tyre_id "
+"where btah.tyre_condition_id =5 and mt.depot_id=md.depot_id "
+"and to_date(to_char(md.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Resole_one_Tyre,"
+"(select count(*) "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt inner join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id =btah.tyre_id "
+"where btah.tyre_condition_id =6 and mt.depot_id=md.depot_id "
+"and to_date(to_char(md.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Resole_two_Tyre,"
+"(select count(*) "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt inner join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id =btah.tyre_id "
+"where btah.tyre_condition_id =7 and mt.depot_id=md.depot_id "
+"and to_date(to_char(md.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3) Resole_three_Tyre "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md where md.depot_id = ?1",nativeQuery=true)
 List<Object[]> getTotalBusesTyreReport(Integer depotId,Date startDate,Date endDate);
	

	@Query(value="select count(*) as totalBuses, (select count(*) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd2 where status=true and mbd2.transport_unit in (?1)) as activeBuses "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd where transport_unit in (?1) ", nativeQuery=true)
	List<Object[]> getTotalBuses(List<Integer> tpIds);

	/*@Query(value="select count(*) as totalBuses, (select count(*) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd2 where status=true and mbd2.depot in (?1)) as activeBuses "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd where depot in (?1) ", nativeQuery=true)
	List<Object[]> getTotalBusesByDepot(Integer id);*/
	
	@Query(value="select count(*) as totalBuses, "
+"sum(case when mbd.is_deleted =false and mbd.status =true and mbd.bus_id not in (select bus_id from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_unavailable "
+ "where  date_trunc('DAY',likely_to_ready_date) > date_trunc('DAY', cast(CURRENT_DATE as timestamp))) then 1 else 0 end )as activeBuses "
+"from "+RestConstants.SCHEMA_NAME+"."+" mst_bus_details mbd where depot in (?1)",nativeQuery=true)
List<Object[]> getTotalBusesByDepot(Integer id);
	
	@Query(value = "SELECT b from BusMaster b where b.busType.id = ?1 and b.isDeleted=false and b.status=true ")
	List<BusMaster> findAllBusesByBusType(Integer busTypeId);
	
	@Query(value = "select t.bus_id,t.trip_end_time,mbd2.primary_driver,mbd2.secondary_driver,mbd2.bus_model,mbd2.bus_reg_number from ( "
			+ "select mbd.bus_id,null trip_end_time from "+RestConstants.SCHEMA_NAME+"."+" mst_bus_details mbd where mbd.bus_id not in "
					+ " (select distinct dr.bus_id from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr  "
					+ " join "+RestConstants.SCHEMA_NAME+"."+" roaster r2 on r2.rota_id = dr.rota_id  "
					+ " where to_date(to_char(r2.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 and "
					+ " dr.bus_id not in (select distinct dr.bus_id  from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr inner join "
					+ " "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt  on dr.trip_id = mt.trip_id inner join "
					+ " (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
					+ " group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 )a "
					+ " on a.trip_id = dr.trip_id)) and mbd.bus_id not in (select distinct dr.bus_id  from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr inner join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt  on dr.trip_id = mt.trip_id "
					+ " inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
					+ " group by trip_id )t where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3 )a on a.trip_id = dr.trip_id) "
					+ " union all select distinct dr.bus_id,mt.trip_end_time  from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr inner join  "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt  on dr.trip_id = mt.trip_id "
			        + " inner join (select trip_id from (select trip_id ,max(rotation_availability_date) avd from "+RestConstants.SCHEMA_NAME+"."+" daily_roaster dr group by trip_id )t "
			        + " where to_date(to_char(t.avd, 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?3)a on "
			        + " a.trip_id = dr.trip_id )t inner join "+RestConstants.SCHEMA_NAME+"."+" mst_bus_details mbd2 on mbd2.bus_id =t.bus_id where  mbd2.depot =?1 and mbd2.transport_unit =?2 "
			       /* + " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" bus_block_history block where block.bus_id = mbd2.bus_id "
		            + "  and date_trunc('DAY',block.to_date) > date_trunc('DAY', cast(?3 as timestamp))) "*/
			        + " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" mark_codemn condemn where condemn.bus_id = mbd2.bus_id) "
			        + " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" authorize_route auRoute where auRoute.bus_id = mbd2.bus_id and auRoute.is_deleted = false) "
			        + " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" mst_bus_unavailable mbu where mbu.bus_id = mbd2.bus_id "
                   + "  and date_trunc('DAY',mbu.likely_to_ready_date) >= date_trunc('DAY', cast(?3 as timestamp))) "
                   + " and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+" mark_spare mark where mark.bus_id = mbd2.bus_id and "
                   + " date_trunc('DAY',mark.to_dt) >= date_trunc('DAY', cast(?3 as timestamp)))", nativeQuery=true)
	List<Object[]> getAvailabilityForRota(Integer id, Integer tpId, Date date);
	
	
	@Query(value="select mbd.bus_type,mbt.bus_type_name ,mbd.bus_reg_number "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt  on mbd.bus_type = mbt.bus_type_id " 
+"where mbd.bus_type =?1 ",nativeQuery=true)
List<Object[]> getBusRegistrationNumbers( Integer busType);
	
	
	@Query(value="select count(bus_id) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details where depot =?1 and transport_unit =?2 and is_deleted = false and status =true ",nativeQuery=true)
	Integer getAllBusesCountByDepotAndTu(Integer depotId, Integer transportId);
	
	
	@Query(value="select md.depot_code, mbd.bus_type, mbt.bus_type_name "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md " 
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on  mbd.depot =md.depot_id "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt  on mbd.bus_type =mbt.bus_type_id "
+"where md.depot_id =?1",nativeQuery=true)
List<Object[]>getBusType(Integer depotId);

@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details where blocked=true")
List<BusMaster> findAllBlockedRoutes();

@Query(value = "SELECT b from BusMaster b where b.transportUnit.id = ?1")
List<BusMaster> findAllByTransportId(Integer id);


@Query(value="select mbd2.bus_id,mbd2.bus_reg_number from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd2 where mbd2.transport_unit =?1 and not exists ( "
		+ " select bta.bus_id from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta where mbd2.bus_id=bta.bus_id  "
		+ " group by bta.bus_id  "
		+ " having count(*)>(select mbtc.count from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
		+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_tyre_count mbtc on mbtc.bus_tyre_count_id = mbd.tyre_count and bta.bus_id=mbd.bus_id))",nativeQuery=true)
List<Object[]>getAllBusesByTransportId(Integer transportId);

@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details a where exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history b where b.bus_id =a.bus_id and Date(b.removal_date)=Date(current_date)) and a.depot =?1")
List<BusMaster> findPrintSlipBuses(Integer depotId);

@Query(nativeQuery=true,value="select total_seats from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details where bus_type =?1 limit 1")
Object[] fetchTotalSeatsByBusType(Integer busTypeId);

@Query(value="select md.depot_name ,mbd.bus_reg_number ,mbd.bus_passing_date , mbd.bus_model ,mbt.bus_type_name ,mbst.bus_sub_type_name , mbm.name "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mbd.depot  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_sub_type mbst on mbst.bus_sub_type_id = mbd.bus_sub_type  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_maker mbm on mbm.bus_maker_id = mbd.maker  "
		+ "where depot in (?1) order by 3 desc",nativeQuery=true)
     List<Object[]> getTotalBusDetails(Integer depotId);

@Query(value="select md.depot_name ,mbd.bus_reg_number ,mbd.bus_passing_date , mbd.bus_model ,mbt.bus_type_name ,mbst.bus_sub_type_name , mbm.name "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mbd.depot  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_sub_type mbst on mbst.bus_sub_type_id = mbd.bus_sub_type  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_maker mbm on mbm.bus_maker_id = mbd.maker  "
		+ "where depot in (?1) and mbd.is_deleted =false and mbd.status =true and mbd.bus_id not in (select bus_id from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_unavailable "
		+ "where  date_trunc('DAY',likely_to_ready_date) > date_trunc('DAY', cast(CURRENT_DATE as timestamp))) order by 3 desc",nativeQuery=true)
    List<Object[]> getActiveTotalBusDetails(Integer depotId);


}
