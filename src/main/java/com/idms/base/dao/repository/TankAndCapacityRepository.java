package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.QDepotMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TankAndCapacityRepository extends JpaRepository<DepotMaster, Integer>,
		QuerydslPredicateExecutor<DepotMaster>, QuerydslBinderCustomizer<QDepotMaster> {

	@Override
	default void customize(QuerydslBindings bindings, QDepotMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}

	@Query(value = "select ms.depot_id, ms.depot_name from " + RestConstants.SCHEMA_NAME + "."
			+ "mst_depot mc ", nativeQuery = true)
	List<Object[]> findAllDepot();

	/*
	 * @Query(
	 * value="select md.depot_name, sum(mtc.capacity)as capacity, count(mft.tank_name) "
	 * + "from "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft " +
	 * "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_depot md on md.depot_id = mft.depot_id " +
	 * "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_tank_capacity mtc on mtc.tank_capacity_id = mft.capacity_id " +
	 * "where mft.created_on between ?1 and ?2 and mft.depot_id =?3   group by md.depot_name"
	 * , nativeQuery=true) List<Object[]> listOfTankCapacityReport(Date
	 * fromDate, Date toDate, Integer dpcode);
	 */

	/*@Query(value = "select md.depot_name, mft.tank_name as Total_Tank, (mtc.capacity-sum(coalesce(mbr.issued_diesel, 0))) as Total_capacity "
			+ " from  " + RestConstants.SCHEMA_NAME + "." + "mst_depot md  "
			+ " inner join " + RestConstants.SCHEMA_NAME + "." + "mst_fuel_tank mft on md.depot_id = mft.depot_id and mft .depot_id = ?1 "
			+ " inner join " + RestConstants.SCHEMA_NAME + "." + "mst_tank_capacity mtc on mtc.tank_capacity_id =mft.capacity_id "
			+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_refueling mbr on mbr.fuel_tank_id = mft.fuel_tank_id and  to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') <= ?2 "
			+ " group by md.depot_name, mft.tank_name, mtc.capacity", nativeQuery = true)*/
	
	@Query(value = "select md.depot_name, mft.tank_name as Total_Tank, mtc.capacity as tank_capacity, "
			+ " case when dcr.dip_id is null then coalesce (mft.current_value ,0) "
			+ " else (case when coalesce (dcr.volume_before_supply,0)!=0 then coalesce (dcr.volume_before_supply,0) else coalesce (dcr.eveng_volume_before_supply,0) end) end "
			+ " as Total_capacity "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on md.depot_id = mft.depot_id and mft.depot_id = ?1 and mft.status = TRUE "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_tank_capacity mtc on mtc.tank_capacity_id =mft.capacity_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"dip_chart_readings dcr on dcr.fuel_tank_id = mft.fuel_tank_id "
			+ " and dcr.dip_id in (select max(dcr2.dip_id) from "+RestConstants.SCHEMA_NAME+"."+"dip_chart_readings dcr2 "
			+ " where to_date(to_char(dcr.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') = ?2 group by dcr2.fuel_tank_id) order by mft.tank_name", nativeQuery = true)
	List<Object[]> listOfTankCapacityReport(Integer dpcode, Date date);

	/*
	 * @Query(
	 * value="select md.depot_name, mft.explosive_licence_validity , mft.explosive_licence_id, md2.document_name "
	 * + "from "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft " +
	 * "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_depot md on md.depot_id = mft.depot_id " +
	 * "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_document md2 on md2.document_id = mft.explosive_licence_id " +
	 * "where mft .depot_id =?3 and mft.created_on between ?1 and ?2 ",
	 * nativeQuery=true) List<Object[]> listOfexplosiveReport(Date fromDate,
	 * Date toDate, Integer dpcode);
	 */
	// @Query(value="select md.depot_name, mft.explosive_licence_validity ,
	// mft.explosive_licence_id, md2.document_name,md2.document_path from
	// punbus_dev.mst_fuel_tank mft join punbus_dev.mst_depot md on md.depot_id
	// = mft.depot_id join punbus_dev.mst_document md2 on md2.document_id =
	// mft.explosive_licence_id where mft .depot_id =?1 and
	// to_date(to_char(mft.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2
	// and ?3",nativeQuery=true)
	/*@Query(value = "select  md.depot_name, mft.explosive_licence_validity , mft.explosive_licence_id, md2.document_name as License_details, md2.document_path "
			+ "from " + RestConstants.SCHEMA_NAME + "." + "mst_fuel_tank mft " + "join " + RestConstants.SCHEMA_NAME
			+ "." + "mst_depot md on md.depot_id=mft.depot_id " + "join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_document md2 on md2.document_id = mft.explosive_licence_id "
			+ "where mft .depot_id =?1 and to_date(to_char(mft.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') <=?2 ", nativeQuery = true)*/
	
	@Query(value="select md.depot_name, to_date(to_char(mft.explosive_licence_validity, 'YYYY/MM/DD'), 'YYYY/MM/DD') as explosive_licence_validity, mft.explosive_licence_id, md2.document_name as License_details, md2.document_path "
	+"from "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft "
	+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mft.depot_id "
	+"join "+RestConstants.SCHEMA_NAME+"."+"mst_document md2 on md2.document_id = mft.explosive_licence_id and mft.status = TRUE "
	+"where mft.depot_id =?1 and to_date(to_char(mft.explosive_licence_validity, 'YYYY/MM/DD'), 'YYYY/MM/DD') >=?2 order by to_date(to_char(mft.explosive_licence_validity, 'YYYY/MM/DD'), 'YYYY/MM/DD') desc", nativeQuery = true)
	List<Object[]> listOfexplosiveReport(Integer dpcode, Date date);

	/*
	 * @Query(
	 * value="select md.depot_name, mdu.dis_unit_name, mdut.dis_unit_type_name, mdu .updated_on "
	 * + "from "+RestConstants.SCHEMA_NAME+"."+" mst_dispensing_unit mdu " +
	 * "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_dispensing_unit_type mdut on mdut.dispensing_unit_type_id = mdu.dispensing_unit_type_id "
	 * + "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_depot md on md.depot_id = mdu.depot_id " +
	 * "where mdu .depot_id =?3 and mdu.created_on between ?1 and ?2 ",
	 * nativeQuery=true) List<Object[]> listOfDispensingReport(Date fromDate,
	 * Date toDate, Integer dpcode);
	 */

	/*@Query(value = "select md.depot_name,mdu.dis_unit_name,mdut.dis_unit_type_name ,to_date(to_char(coalesce (his.created_on,mdu.created_on ), 'YYYY/MM/DD'), 'YYYY/MM/DD') as Last_Operated_date " + "from "
			+ RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit mdu "+"join "+ RestConstants.SCHEMA_NAME+"."
			+ "mst_dispensing_unit_type mdut on mdut.dispensing_unit_type_id = mdu.dispensing_unit_type_id " + "join "
			+ RestConstants.SCHEMA_NAME+"."
			+ "mst_depot md on md.depot_id =mdu.depot_id "
			+ " left join "
			+ RestConstants.SCHEMA_NAME+"."+"du_reading_history his on his.dispensing_unit_id = mdu.dispensing_unit_id and his.du_history_id in (select max(drh.du_history_id) "
			+ " from " + RestConstants.SCHEMA_NAME+"."+"du_reading_history drh where drh.dispensing_unit_id = mdu.dispensing_unit_id)where mdu .depot_id =?1 ", nativeQuery = true)*/
	
	
     @Query(value="select md.depot_name,mdu.dis_unit_name,mdut.dis_unit_type_name ,to_date(to_char(coalesce (his.created_on, mdu.created_on), 'YYYY/MM/DD'), 'YYYY/MM/DD') as "
	        +"Last_Operated_date from "+RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit mdu "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit_type mdut on mdut.dispensing_unit_type_id = mdu.dispensing_unit_type_id "
	        +"left join "+RestConstants.SCHEMA_NAME+"."+"du_reading_history his on his.dispensing_unit_id = mdu.dispensing_unit_id "
	        +"and his.du_history_id in (select max(drh.du_history_id) from "+RestConstants.SCHEMA_NAME+"."+"du_reading_history drh where drh.dispensing_unit_id=mdu.dispensing_unit_id) "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id =mdu.depot_id where mdu.depot_id =?1",nativeQuery=true)
	       List<Object[]> listOfDispensingReport(Integer dpcode);

	/*
	 * @Query(
	 * value="select md.depot_name , mdu.dis_unit_code , sum(drh.du_start_reading) as Start_Reading , sum(drh.du_end_reading) as End_Reading,sum(drh.du_end_reading - drh.du_start_reading) as Total "
	 * + "from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md " +
	 * "inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_dispensing_unit mdu on md.depot_id = mdu.depot_id " +
	 * "inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"du_reading_history drh on mdu.dispensing_unit_id = drh.dispensing_unit_id "
	 * +
	 * "where md.depot_id = ?3 and md.created_on between ?1 and ?2 group by md.depot_name, mdu.dis_unit_code "
	 * , nativeQuery=true) List<Object[]> listOfFuelDispenserReport(Date
	 * fromDate, Date toDate, Integer dpcode);
	 */

	/*@Query(value = "select md.depot_name ,mdu.dis_unit_code,drh.du_start_reading as Start_Reading ,  "
			+ " drh.du_end_reading as End_Reading,sum(drh.du_end_reading - drh.du_start_reading) as Total  "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md inner join "+RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit mdu on md.depot_id = mdu.depot_id  "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"du_reading_history drh on mdu.dispensing_unit_id = drh.dispensing_unit_id  "
			+ " where md.depot_id = ?1 and to_date(to_char(drh.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD')  "
			+ " between ?2 and ?3  "
			+ " group by drh.du_start_reading,drh.du_end_reading,md.depot_name, mdu.dis_unit_code order by 3", nativeQuery = true)*/
	
	@Query(value="select md.depot_name , mdu.dis_unit_code, mbd.bus_reg_number, drh.du_start_reading as Start_Reading , drh.du_end_reading as End_Reading, "
              +"to_date(to_char(drh.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as fuel_issuance_date, "
              +"sum(drh.du_end_reading - drh.du_start_reading) as Total_diesel_issuance , mt.transport_name "
              +"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
              +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit mdu on md.depot_id = mdu.depot_id "
              +"inner join "+RestConstants.SCHEMA_NAME+"."+"du_reading_history drh on mdu.dispensing_unit_id = drh.dispensing_unit_id "
              +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = drh.refueling_id "
              +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
              +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mbd.transport_unit "
              +"where md.depot_id = ?1 and to_date(to_char(drh.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
              +"group by md.depot_name, mdu.dis_unit_code, drh.du_start_reading, drh.du_end_reading, to_date(to_char(drh.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD'), "
              +"mbd.bus_reg_number , mt.transport_name order by 5", nativeQuery=true)
	List<Object[]> listOfFuelDispenserReport(Integer depotId, Date fromDate, Date toDate);

	/*
	 * @Query(
	 * value="select mr.route_code, mr.route_name, mrc.route_category_name, mbd.bus_reg_number, mbr.scheduled_kms, mbr.kmpl_as_scheduled_kilometer,mbr.vts_kms, mbr.kmpl_as_pervtskms "
	 * + "from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr " +
	 * "inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
	 * + "inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_bus_refueling mbr on mr.id = mbr.route_master " +
	 * "inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id " +
	 * "where mr.route_code = ?3 and mr.created_on between ?1 and ?2 ",
	 * nativeQuery=true) List<Object[]> listOfKMPLComparisonReport(Date
	 * fromDate, Date toDate, String dpcode);
	 */

	/*@Query(value = "select mr.route_code, mr.route_name, mrc.route_category_name, mbd.bus_reg_number, mbr.scheduled_kms, mbr.kmpl_as_scheduled_kilometer, mbr.vts_kms, mbr.kmpl_as_pervtskms "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mr.id = mbr.route_master "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id =mbr.bus_id "
			+"where mr.route_code =?1 and to_date(to_char(mr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ", nativeQuery = true)
	List<Object[]> listOfKMPLComparisonReport(String route, Date fromDate, Date toDate);*/
	
	/*@Query(value="select mr.route_id, mr.route_name, mrc.route_category_name, mbd.bus_reg_number, mbr.scheduled_kms,"
+"mbr.kmpl_as_scheduled_kilometer, mbr.vts_kms,mbr.kmpl_as_pervtskms from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mr.id = mbr.route_master "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id =mbr.bus_id "
+"where mr.id = ?3 and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"order by 1, 5,6,7,8 desc",nativeQuery=true)
*/	@Query(value="select mr.route_id, mr.route_name, mrc.route_category_name, mbd.bus_reg_number, mbr.scheduled_kms, mbr.kmpl_as_scheduled_kilometer, mbr.vts_kms, "
           	+"mbr.kmpl_as_pervtskms from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr " 
	        +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
	        +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mr.id = mbr.route_master "
	        +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id =mbr.bus_id "
	        +"where mr.id =?3 and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
	        +"order by 1, 5,6,7,8 desc", nativeQuery=true)
        List<Object[]> listOfKMPLComparisonReport(Date fromDate, Date toDate,Integer routeId);

	/*
	 * @Query(
	 * value="select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
	 * +"(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
	 * +"from " +RestConstants.SCHEMA_NAME+"."+ "mst_recieve_diesel_sup mrds "
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id " +"join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_depot md on md.depot_id = mft.depot_id "
	 * +"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 ",
	 * nativeQuery=true) List<Object[]> supplyReceivedReport(Date from, Date to,
	 * Integer depoId);
	 */
	/*@Query(value = "select md.depot_name,to_date(to_char(mrds.entry_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date_of_supply_received,mrds.quantity_recieved, mrds.invoice_number,mrds.diesel_rate as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds join "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mft.depot_id "
			+"where md.depot_id =?1 and to_date(to_char(mrds.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3", nativeQuery = true)
*/	
	@Query(value = "select md.depot_name, to_date(to_char(mrds.entry_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date_of_supply_received, mrds.quantity_recieved, mrds.diesel_rate as Rate_Liter_Received, mrds.invoice_number, SUBSTRING(cast(mrds.entry_time as varchar(19)) FROM 12 FOR 20) as Supply_Received_time "
	+"from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds join "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
	+"join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id and mft.status = TRUE join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mft.depot_id "
	+"where (md.depot_id =?1 or 0 =?1) and to_date(to_char(mrds.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3", nativeQuery = true)
	List<Object[]> supplyReceivedReport(Integer depoId, Date from, Date to);

	/*
	 * @Query(
	 * value="select mbd.bus_reg_number, mdd.mobile_number, mdd.driver_name, mr.route_name, mbr.kmpl_as_scheduled_kilometer "
	 * +"from " +RestConstants.SCHEMA_NAME+"."+ " daily_roaster dr " +"join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * " mst_bus_details mbd on dr.bus_id = mbd.bus_id " +"inner join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * " mst_driver_details mdd on dr.driver_id = mdd.driver_id " +"inner join "
	 * +RestConstants.SCHEMA_NAME+"."+ "mst_route mr on dr.route_id = mr.id "
	 * +"inner join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id "
	 * +"group by mbd.bus_reg_number , mdd.mobile_number, mdd.driver_name,  mr.route_name, mbr.kmpl_as_scheduled_kilometer, dr.created_on "
	 * +"having mbd.bus_reg_number = ?3 and dr.created_on between ?1 and ?2 "
	 * ,nativeQuery=true) List<Object[]> busWiseDriverRouteKmpl(Date startDate,
	 * Date endDate, String busNo);
	 */
	/*@Query(value = "select mbd.bus_reg_number, mdd.mobile_number, "
			+ " mdd.driver_name, mr.route_name, mbr.kmpl_as_scheduled_kilometer from "
			+ " "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master = mr.id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id and dr.route_id =mr.id and dr.bus_id =mbd.bus_id "
			+ " and dr.roaster_id in (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id and dr2.route_id =mr.id and dr2.bus_id =mbd.bus_id ) "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
			+ " where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 and (mbd.bus_reg_number =?3 or '0'=?3)", nativeQuery = true)*/
	
	@Query(value = "select mbd.bus_reg_number,mdd.driver_name, mr.route_name, mbr.kmpl_as_scheduled_kilometer from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
	+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master = mr.id "
	+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id and dr.route_id =mr.id and dr.bus_id =mbd.bus_id "
	+"and dr.roaster_id in (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id and dr2.route_id =mr.id "
	+"and dr2.bus_id =mbd.bus_id ) inner join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
	+"where (mbd.bus_reg_number= ?3 or ''=?3) and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2", nativeQuery = true)
	List<Object[]> busWiseDriverRouteKmpl(Date startDate, Date endDate,String busNo);

	/*
	 * @Query(
	 * value="select  mr.route_name,  mdd.mobile_number, mdd.driver_name, mbd.bus_reg_number, mbr.kmpl_as_scheduled_kilometer "
	 * + "from " +RestConstants.SCHEMA_NAME+"."+ "daily_roaster dr " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * " mst_bus_details mbd on dr.bus_id = mbd.bus_id " + "inner join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * " mst_driver_details mdd on dr.driver_id = mdd.driver_id " +
	 * "inner join " +RestConstants.SCHEMA_NAME+"."+
	 * " mst_route mr on dr.route_id = mr.id " + "inner join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id " +
	 * "where mdd.driver_name = ?3   and dr.created_on between ?1 and ?2 " +
	 * "group by mbd.bus_reg_number , mdd.mobile_number, mdd.driver_name,  mr.route_name, mbr.kmpl_as_scheduled_kilometer "
	 * , nativeQuery=true) List<Object[]> routeWiseBusDriverKmpl(Date startDate,
	 * Date endDate, String driver);
	 */
/*	@Query(value = "select  mr.route_name, mdd.mobile_number, mdd.driver_name, mbd.bus_reg_number, mbr.kmpl_as_scheduled_kilometer from  "
			+ " "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master = mr.id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id and dr.route_id =mr.id and dr.bus_id =mbd.bus_id "
			+ " and dr.roaster_id in (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id and dr2.route_id =mr.id and dr2.bus_id =mbd.bus_id ) "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id where  (mdd.driver_id = ?3 or 0=?3)  "
			+ " and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ", nativeQuery = true)
	*/
	@Query(value = "select mr.route_name, mdd.driver_name, mbd.bus_reg_number, mbr.kmpl_as_scheduled_kilometer "
	       +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
	       +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id "
	       +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master = mr.id "
	       +"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id and dr.route_id =mr.id and dr.bus_id =mbd.bus_id "
	       +"and dr.roaster_id in (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id "
	       +"and dr2.route_id =mr.id  and dr2.bus_id =mbd.bus_id ) "
	       +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
	       +"where (mdd.driver_id =?3 or 0=?3) and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 " , nativeQuery=true)
    List<Object[]> routeWiseBusDriverKmpl( Date startDate, Date endDate,Integer driverId);

	/*
	 * @Query(
	 * value="select  mbd.bus_reg_number, mbt.bus_type_name , mrc. route_category_name, mbr.issued_diesel "
	 * + "from " +RestConstants.SCHEMA_NAME+"."+ "daily_roaster dr " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_details mbd on dr.bus_id= mbd.bus_id " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_type mbt on mbd.bus_type= mbt.bus_type_id " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+ "mst_route mr on dr.route_id = mr.id " +
	 * "join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
	 * + "join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_refueling mbr on dr.refueling_id= mbr.bus_refueling_id " +
	 * "where mbd.bus_reg_number= ?3 and dr.created_on between ?1 and ?2 "
	 * ,nativeQuery=true) List<Object[]> busTypeBusWiseDieselIssuanceReport(Date
	 * startDate, Date endDate, String bus);
	 */
	/*@Query(value = " select mbt.bus_type_name, mbd.bus_reg_number, mrc.route_category_name,  "
			+ " sum(coalesce (mbr.issued_diesel ,0) ) issued_diesel "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbd.bus_id= mbr.bus_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type= mbt.bus_type_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.bus_type_id = mbt. bus_type_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id  "
			+ " where mbt.bus_type_name= ?4 and (mbd.bus_reg_number= ?1 or '0'= ?1)"
			+ " and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3  "
			+ " group by mbd.bus_reg_number, mbt.bus_type_name , mrc. route_category_name "
			+ " order by 3 asc,4 desc", nativeQuery = true)*/
	@Query(value = "select mbt.bus_type_name, mbd.bus_reg_number, mrc.route_category_name, (coalesce(mbr.issued_diesel,0)) as diesal_issued "
                 +",to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') issuance_date from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
                 +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbd.bus_id= mbr.bus_id join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type= mbt.bus_type_id "
                 +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
                 +"where mbt.bus_type_name=?4 and (mbd.bus_reg_number=?1 or ''=?1) and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
                 +"order by 4 desc" , nativeQuery = true)
      List<Object[]> busTypeBusWiseDieselIssuanceReport(String bus, Date startDate, Date endDate,String busType);

	/*
	 * @Query(
	 * value="select mr.route_code, mr.route_name, mbd.bus_reg_number, sum(mbr.issued_diesel) as issued_diesel, sum(mbr.vts_kms) as vts_kms "
	 * + "from " +RestConstants.SCHEMA_NAME+"."+ "mst_route mr " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_details mbd on mr.bus_type_id = mbd.bus_type " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_refueling mbr on mr.id = mbr.route_master " +
	 * "where mr.created_on between ?1 and ?2 group by mr.route_code, mr.route_name, mbd.bus_reg_number "
	 * , nativeQuery=true) List<Object[]> issuanceReceiptStmntReport(Date
	 * startDate, Date endDate);
	 */

	/*@Query(value = "select mr.route_code, mr.route_name, mbd.bus_reg_number, sum(mbr.issued_diesel) as diesel_issued, sum(mbr.issued_diesel * mrds.diesel_rate) as value_in_rs,"
+"sum(mbr.vts_kms) as VTS_KMs "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master =int4(mr.route_id) "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus on mbr.fuel_tank_id = mus.fuel_tank_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds on mrds.update_supply_id = mus.update_supply_id "
+"where to_date(to_char(mbd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between '2022-01-14' and '2022-01-24' " 
+"group by mr.route_code, mr.route_name, mbd.bus_reg_number", nativeQuery = true)*/
	
	@Query(value="select mr.route_id, mr.route_name, mbd.bus_reg_number, (coalesce(mbr.issued_diesel,0)) as diesel_issued, "
			+ " (coalesce(mbr.issued_diesel,0) * "
			+ " (select mrds.diesel_rate from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds "
			+ " where mrds.updated_on = (select max(mrds.updated_on) from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds "
			+ " where to_date(to_char(mrds.updated_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') <= to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD')))) as value_in_rs, "
			+ " to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as issuance_date, "
			+ " (select coalesce((select sum(efe.net_amt_to_deposit) "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"etm_assignment ea "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"earning_from_etm efe on efe.assignment_id = ea.etm_assignment_id where ea.way_bill_no = ietb.waybill_no ),0) "
			+ " + coalesce ((select sum(((setbc.endingserial_no - setbc.startingserial_no)+1) * setbc.denomination) "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb left join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box_child setbc on setbc.submit_etm_ticket_box = setb.set_id "
			+ " where setb.iet_id= ietb.iet_id),0) as receipt from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 on ietb.roaster_id = dr2.roaster_id where dr2.roaster_id=dr.roaster_id) as receipt_rs, "
			+ " mbr.vts_kms as VTS_KMs, mt.transport_name,mr.scheduled_kms from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
			+ " and dr.roaster_id in (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id )  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master =mr.id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mbd.transport_unit"
			+ " where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
			+ " order by 4, 5, 6", nativeQuery=true)
   List<Object[]> issuanceReceiptStmntReport(Date startDate, Date endDate);

	/*
	 * @Query(value="select mr.route_code, mr.route_name, mr.dead_kms " +
	 * "from " +RestConstants.SCHEMA_NAME+"."+ "mst_route mr " +
	 * "where mr.route_code = ?3 and mr.created_on between ?1 and ?2 "
	 * ,nativeQuery=true) List<Object[]> deadKMSReport(Date startDate, Date
	 * endDate, String route);
	 */

	/*@Query(value = "select mr.route_code, mr.route_name, mr.dead_kms "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr "
+"where mr.route_code = ?1", nativeQuery = true)
List<Object[]> deadKMSReport(String route);
*/
/*
@Query(value="select mr.route_id, mr.route_name, sum(coalesce (mbr.dead_kms,0)) as dead_kms from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master "
+"where mr.id = ?1 group by mr.route_id, mr.route_name",nativeQuery=true)
*/    
  /* @Query(value="select mr.route_id, mr.route_name, sum(coalesce (mbr.dead_kms,0) + coalesce(mbr.extra_dead_kms,0)) as dead_kms "
            +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
            +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master where mr.id =?1 group by mr.route_id, mr.route_name ", nativeQuery=true)
   */
   @Query(value="select mr.route_id, mr.route_name, to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date, "
         +"sum(coalesce(mbr.dead_kms,0)) as dead_km, sum(coalesce(mbr.extra_dead_kms,0)) as extra_dead "
         +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
         +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master "
         +"where (mr.id = ?1 or 0=?1) and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD')= ?2 "
         +"group by mr.route_id, mr.route_name, to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') " , nativeQuery=true)
    List<Object[]> deadKMSReport(Integer routeId,Date date);
	/*
	 * @Query(
	 * value="select mdd.mobile_number, mdd.driver_name, mbr.kmpl_as_scheduled_kilometer as KMPL, mr.route_name "
	 * + "from " +RestConstants.SCHEMA_NAME+"."+ "daily_roaster dr " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_driver_details mdd on dr.driver_id = mdd.driver_id " + "join "
	 * +RestConstants.SCHEMA_NAME+"."+
	 * "mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id " +
	 * "join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_route mr on dr.route_id = mr.id " +
	 * "where mr.depot_id =?3 and dr.created_on between ?1 and ?2 ",nativeQuery=
	 * true) List<Object[]> bestWorstDriverReport(Date startDate, Date endDate,
	 * Integer depoId);
	 */
	/*@Query(value = "select mdd.mobile_number, mdd.driver_name, mbr.kmpl_as_scheduled_kilometer as KMPL, mr.route_name "
			+"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id where mr.depot_id =?1 and to_date(to_char(dr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ", nativeQuery = true)
	List<Object[]> bestWorstDriverReport(Integer depoId, Date startDate, Date endDate);*/

/*@Query(value="select mdd.mobile_number, mdd.driver_name, max(coalesce(mbr.kmpl_as_scheduled_kilometer,0)) as KMPL,"
+"mr.route_name from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
+"join "+RestConstants.SCHEMA_NAME+"."+"driver_roaster_history drh on drh.roaster_id =dr.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on drh.driver_id = mdd.driver_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id "
+"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id =dr.rota_id "
+"where mr.depot_id =?3 and to_date(to_char(r.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mdd.mobile_number, mdd.driver_name, mr.route_name order by 3 desc",nativeQuery=true)*/

@Query(value="select  mdd.driver_name, max(coalesce(mbr.kmpl_as_scheduled_kilometer,0)) as KMPL,sum(coalesce(mbr.kmpl_as_scheduled_kilometer,0))/count(dr.route_id) as avg_kmpl "
	 +"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
     +"join "+RestConstants.SCHEMA_NAME+"."+"driver_roaster_history drh on drh.roaster_id =dr.roaster_id "
     +"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on drh.driver_id = mdd.driver_id "
     +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on dr.refueling_id = mbr.bus_refueling_id "
     +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id "
     +"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id =dr.rota_id "
     +"where mr.depot_id =?3 and (coalesce(mbr.kmpl_as_scheduled_kilometer,0)) between ?4 and ?5 and "
     +"to_date(to_char(mbr.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 group by mdd.driver_name "
     +"order by 3 desc" , nativeQuery = true)
   List<Object[]> bestWorstDriverReport(Date startDate, Date endDate,Integer depoId,Double startRange,Double endRange);

	// Query Will Change This query is only for build the the project.

	/*
	 * @Query(
	 * value="select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
	 * +"(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time"
	 * +"from " +RestConstants.SCHEMA_NAME+"."+ "mst_recieve_diesel_sup mrds "
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * " punbus_dev.mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "punbus_dev.mst_depot md on md.depot_id = mft.depot_id"
	 * +"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 "
	 * ,nativeQuery=true) List<Object[]> dueDateCleaningReport(Date startDate,
	 * Date endDate, Integer depoId);
	 */
	// 3
	// @Query(value = "select md.depot_name, mft.tank_name, mft.cleaning_date
	// from punbus_dev.mst_fuel_tank mft join punbus_dev.mst_depot md on
	// mft.depot_id = md.depot_id where mft.depot_id =?1 and
	// to_date(to_char(mft.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2
	// and ?3", nativeQuery = true)
/*	@Query(value = "select md.depot_name, to_date(to_char(mft.cleaning_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as due_date_cleaning, mft.tank_name " + "from " + RestConstants.SCHEMA_NAME + "."
			+ "mst_fuel_tank mft " + "join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_depot md on mft.depot_id = md.depot_id "
			+ " where mft.depot_id =?1 and to_date(to_char(mft.cleaning_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')>= ?2 order by mft.tank_name", nativeQuery = true)*/
	
	@Query(value = "select md.depot_name, to_date(to_char(mft.cleaning_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as due_date_cleaning, mft.tank_name "
	+ " from " + RestConstants.SCHEMA_NAME + "." + " mst_fuel_tank mft join " + RestConstants.SCHEMA_NAME + "." + " mst_depot md on mft.depot_id = md.depot_id and " 
    + " mft.status = TRUE  where mft.depot_id =?1 and to_date(to_char(mft.cleaning_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') >=?2 order by to_date(to_char(mft.cleaning_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')", nativeQuery = true)
	List<Object[]> dueDateCleaningReport(Integer depoId, Date date);

	/*
	 * @Query(
	 * value="select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
	 * +"(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time"
	 * +"from " +RestConstants.SCHEMA_NAME+"."+ "mst_recieve_diesel_sup mrds "
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * " punbus_dev.mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "punbus_dev.mst_depot md on md.depot_id = mft.depot_id"
	 * +"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 "
	 * ,nativeQuery=true) List<Object[]> inspectionDueDateReport(Date startDate,
	 * Date endDate, Integer depoId);
	 */
	/*@Query(value = "select md.depot_name,  to_date(to_char(ti.inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as inspection_date,'' next_due_date "
			+"from "+RestConstants.SCHEMA_NAME+"."+"tank_inspection ti "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on ti.depot_id = md.depot_id "
			+"where ti.depot_id =?1 and to_date(to_char(ti.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD')>=?2 ", nativeQuery = true)*/
	
	@Query(value = "select md.depot_name, to_date(to_char(ti.inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as inspection_date, "
			   +"to_date(to_char(ti.next_inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as next_due_date, ti.remarks as status_t "
	           +"from  " + RestConstants.SCHEMA_NAME + "." + "tank_inspection ti "
	           +"join " + RestConstants.SCHEMA_NAME + "." + "mst_depot md on ti.depot_id = md.depot_id "
	           +"where ti.depot_id =?1 and to_date(to_char(ti.next_inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') >=?2 order by to_date(to_char(ti.next_inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')", nativeQuery=true)
	List<Object[]> inspectionDueDateReport(Integer depoId, Date date);

	/*@Query(value = "select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
			+ "(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
			+"from " +RestConstants.SCHEMA_NAME +"."+"mst_recieve_diesel_sup mrds " +"join "
			+RestConstants.SCHEMA_NAME +"." +"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
			+"join " +RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id " 
			+"join "+RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mft.depot_id "
			+"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 ", nativeQuery = true)
	List<Object[]> depotWiseDieselStockReport(Date startDate, Date endDate, Integer depoId, Integer makeWise);*/
	
	
	
	/*@Query(value = "select md.depot_name, mbr.issued_diesel, (mbr.issued_diesel * mrds.diesel_rate) as value_of_stock, '' available_mobil_oil_stock,"
+"'' value_of_stock_mobil "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mbr.depot_id = md.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus on mbr.fuel_tank_id = mus.fuel_tank_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds on mrds.update_supply_id = mus.update_supply_id "
+"where mbr.depot_id =?1 and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3", nativeQuery = true)
List<Object[]> depotWiseDieselStockReport( Integer depoId,Date startDate, Date endDate);
*/
	
	
	/*@Query(value="select md.depot_name, sum(mtc.capacity-(coalesce(mbr.issued_diesel, 0)))as available_diesel_stock,"
+"sum(mtc.capacity-(coalesce(mbr.issued_diesel, 0))) * max(mrds.diesel_rate) as value_of_stock, '' available_mobil_oil_stock,"
+"'' value_of_mobilestock from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
+"left join (select mbr1.depot_id , mbr1.fuel_tank_id ,sum(mbr1.issued_diesel ) issued_diesel from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr1 "
+"where to_date(to_char(mbr1.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
+"group by mbr1.depot_id , mbr1.fuel_tank_id ) mbr on mbr.depot_id = md.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mft.fuel_tank_id = mbr.fuel_tank_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tank_capacity mtc on mtc.tank_capacity_id = mft.capacity_id "
+"left join "+"(select mus.fuel_tank_id, mus.update_supply_id  from "
+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus where mus.update_supply_id in (select max(mus1.update_supply_id) "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus1 where mus1.fuel_tank_id=mus.fuel_tank_id))mus_new on mus_new.fuel_tank_id = mft.fuel_tank_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds on mrds.update_supply_id = mus_new.update_supply_id "
+"where mbr.depot_id =?1 group by md.depot_name",nativeQuery=true)*/
	
	@Query(value="select md.depot_name , to_date(to_char (coalesce (dcr.created_on, su.date_supp) , 'YYYY/MM/DD'), 'YYYY/MM/DD') as stock_date, "
          +"coalesce ((case when coalesce (dcr.eveng_volume_before_supply,0)!=0 then dcr.eveng_volume_before_supply else dcr.volume_before_supply end),mft.current_value) "
          +"as available_stock_diesel , "
          +"coalesce((case when coalesce (dcr.eveng_volume_before_supply,0)!=0 then dcr.eveng_volume_before_supply else dcr.volume_before_supply end) "
          +"* (dcr.current_diesel_price),(mft.current_value*su.rate)) as value_of_stock_diesel "
          +"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on md.depot_id = mft.depot_id and mft.depot_id =?1 and mft.status = TRUE "
          +"left join "+RestConstants.SCHEMA_NAME+"."+"dip_chart_readings dcr on dcr.fuel_tank_id = mft.fuel_tank_id and dcr.dip_id in(select max(dcr2.dip_id) from "+RestConstants.SCHEMA_NAME+"."+"dip_chart_readings dcr2 "
          +"where dcr2.fuel_tank_id=mft.fuel_tank_id) "
          +"left join (select max(mrds.created_on) as date_supp, mus.fuel_tank_id, max(mus.volume_after_supply) as volume, max(mrds.diesel_rate) as rate "
          +"from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds join "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus "
          +"on mus.update_supply_id=mrds.update_supply_id group by mus.fuel_tank_id)su on su.fuel_tank_id = mft.fuel_tank_id "
          +"where to_date(to_char (coalesce (dcr.created_on, su.date_supp) , 'YYYY/MM/DD'), 'YYYY/MM/DD')between ?2 and ?3 "
          +"group by md.depot_name, to_date(to_char (coalesce (dcr.created_on, su.date_supp) , 'YYYY/MM/DD'), 'YYYY/MM/DD') ,dcr.volume_before_supply "
          +",dcr.eveng_volume_before_supply, dcr.current_diesel_price , mft.current_value , su.rate" , nativeQuery = true)
   List<Object[]> depotWiseDieselStockReport( Integer depoId,Date startDate, Date endDate);
   
   @Query(value= "select md.depot_name, to_date(to_char(coalesce (mu.date_mou, mmd.created_on ) , 'YYYY/MM/DD'), 'YYYY/MM/DD') as stock_date, "
              +"(case when mu.drumid != null then (sum(mmd.total_capacity)-sum(coalesce(mu.mobilused,0))) else sum(mmd.total_capacity) end) as available_stock_mobil, "
              +"(case when mu.drumid != null then (sum(mmd.total_capacity)-sum(coalesce(mu.mobilused,0))) else sum(mmd.total_capacity) end) "
              +"* sum(mmd.total_capacity/mmd.value) as value_of_stock_mobil "
              +"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
              +"join "+RestConstants.SCHEMA_NAME+"."+"mst_mobil_drum mmd on mmd.depot = md.depot_id "
              +"left join ( select max(mou.created_on )as date_mou, sum(mou.quantity) mobilused ,mou.mobil_drum_id drumid from "+RestConstants.SCHEMA_NAME+"."+"mobil_oil_used mou "
              +"group by mou.mobil_drum_id )mu on mu.drumid=mmd.mobil_drum_id "
              +"where md.depot_id =?1 "
              +"and to_date(to_char(coalesce (mu.date_mou, mmd.created_on ) , 'YYYY/MM/DD'), 'YYYY/MM/DD')between ?2 and ?3 "
              +"group by md.depot_name, to_date(to_char(coalesce (mu.date_mou, mmd.created_on ) , 'YYYY/MM/DD'), 'YYYY/MM/DD'), mu.drumid" , nativeQuery = true)
 List<Object[]> depotWiseMobilOilStockReport( Integer depoId,Date startDate, Date endDate);
 
 @Query(value="select md.depot_name, to_date(to_char(coalesce (mu.date_mou, mmd.created_on ) , 'YYYY/MM/DD'), 'YYYY/MM/DD') as stock_date, "
           +"(case when mu.drumid != null then (sum(mmd.total_capacity)-sum(coalesce(mu.mobilused,0))) else sum(mmd.total_capacity) end) as available_stock_mobil, "
           +"(case when mu.drumid != null then (sum(mmd.total_capacity)-sum(coalesce(mu.mobilused,0))) else sum(mmd.total_capacity) end) "
           +"* sum(mmd.total_capacity/mmd.value) as value_of_stock_mobil "
           +"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
           +"join "+RestConstants.SCHEMA_NAME+"."+"mst_add_blue_drum mmd on mmd.depot = md.depot_id "
           +"left join ( select max(abu.created_on )as date_mou, sum(abu.quantity) mobilused ,abu.add_blue_drum_id drumid from "+RestConstants.SCHEMA_NAME+"."+"ad_blue_used abu "
           +"group by abu.add_blue_drum_id )mu on mu.drumid=mmd.add_blue_drum_id "
           +"where md.depot_id = ?1 "
           +"and to_date(to_char(coalesce (mu.date_mou, mmd.created_on ) , 'YYYY/MM/DD'), 'YYYY/MM/DD')between ?2 and ?3 "
           +"group by md.depot_name, to_date(to_char(coalesce (mu.date_mou, mmd.created_on ) , 'YYYY/MM/DD'), 'YYYY/MM/DD'), mu.drumid ",nativeQuery=true)
 List<Object[]> depotWiseAdBlueStockReport( Integer depoId,Date startDate, Date endDate);
	
	/*@Query(value = "select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
			+ "(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
			+ "from " +RestConstants.SCHEMA_NAME +"."+"mst_recieve_diesel_sup mrds "+"join "
			+RestConstants.SCHEMA_NAME +"."+"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
			+"join " +RestConstants.SCHEMA_NAME +"."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id " +"join "
			+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mft.depot_id "
			+"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 ", nativeQuery = true)
	List<Object[]> advancePaymentReport(Date startDate, Date endDate, Integer depoId);*/

	/*@Query(value = "select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
			+ "(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
			+"from "+RestConstants.SCHEMA_NAME +"."+"mst_recieve_diesel_sup mrds " + "join "
			+RestConstants.SCHEMA_NAME + "." +"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id "+"join "
			+ RestConstants.SCHEMA_NAME + "." +"mst_depot md on md.depot_id = mft.depot_id"
			+"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2", nativeQuery = true)*/
	
	@Query(value="select distinct mbd.bus_reg_number, mdd.driver_name, mr.route_name, md.depot_name, mbt.bus_type_name, "
			   +"(case ?4 when 'Date' then 'Date' "
	           +"when 'Week' then  (select concat(to_char(to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'),'W'),"
			   +"' week of ' , to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon'))) "
			   +"when 'Month' then (select  to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon')) " 
	           +"when 'None' then 'None' end) as p_group, "
	       +"(select string_agg( mc.city_name || '-' || mc2.city_name ,'||') fgfdg from "+RestConstants.SCHEMA_NAME +"."+"route_rotation rr "
 	       +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc on mc.city_id = rr.from_city_id "
           +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc2 on mc2.city_id =rr.to_city_id where rr.route_id =mr.id) rotation, "
           +"mbr.kmpl_as_scheduled_kilometer as kmpl, r.rota_date as start_date, dr.rotation_availability_date as end_date,mbr.created_on as refuel_date, mbr.kmpl_as_pervtskms "
	       +"from "+RestConstants.SCHEMA_NAME +"."+"mst_bus_refueling mbr "
     	   +"join "+RestConstants.SCHEMA_NAME +"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
	       +"join "+RestConstants.SCHEMA_NAME +"."+"roaster r on r.rota_id = dr.rota_id "
	       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
	       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
	       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_route mr on mr.id = mbr.route_master "
	       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mbr.depot_id "
	       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
	       +"where md.depot_id =?5 and mbt.bus_type_id=?3 "
     	   +"and case ?4 when 'None' then 2=2 else to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
	       +"case ?4 when 'Week' then to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
	       +"when 'Month' then to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
	       +"when 'Date' then ?1 "
           +"end and "
	       +"case ?4 when 'Week' then to_date(to_char((date_trunc('week', date_trunc('week', now()) - interval '1 week') + interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
	       +"when 'Month' then to_date(to_char((date_trunc('month', date_trunc('month', now()) - interval '1 month') + interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
	       +"when 'Date' then ?2 "
	       +"end "
           +"end "
	       +"order by mbd.bus_reg_number, mdd.driver_name, mr.route_name, r.rota_date, md.depot_name, mbt.bus_type_name" , nativeQuery=true)
	List<Object[]> buswiseDriverwiseRoutewiseKmplReport(Date startDate, Date endDate, Integer busType, String grouping, Integer depoId,String select);
	
	@Query(value="select distinct mdd.driver_name, mbd.bus_reg_number, mr.route_name, md.depot_name, mbt.bus_type_name, "
			   +"(case ?4 when 'Date' then 'Date' "
	           +"when 'Week' then  (select concat(to_char(to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'),'W'),"
			   +"' week of ' , to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon'))) "
			   +"when 'Month' then (select  to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon')) " 
	           +"when 'None' then 'None' end) as p_group, "
		       +"(select string_agg( mc.city_name || '-' || mc2.city_name ,'||') fgfdg from "+RestConstants.SCHEMA_NAME +"."+"route_rotation rr "
	 	       +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc on mc.city_id = rr.from_city_id "
	           +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc2 on mc2.city_id =rr.to_city_id where rr.route_id =mr.id) rotation, "
	           +"mbr.kmpl_as_scheduled_kilometer as kmpl, r.rota_date as start_date, dr.rotation_availability_date as end_date,mbr.created_on as refuel_date, mbr.kmpl_as_pervtskms "
		       +"from "+RestConstants.SCHEMA_NAME +"."+"mst_bus_refueling mbr "
	     	   +"join "+RestConstants.SCHEMA_NAME +"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
		       +"join "+RestConstants.SCHEMA_NAME +"."+"roaster r on r.rota_id = dr.rota_id "
		       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
		       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
		       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_route mr on mr.id = mbr.route_master "
		       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mbr.depot_id "
		       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
		       +"where md.depot_id =?5 and mbt.bus_type_id=?3 "
	     	   +"and case ?4 when 'None' then 2=2 else to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		       +"case ?4 when 'Week' then to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		       +"when 'Month' then to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		       +"when 'Date' then ?1 "
	           +"end and "
		       +"case ?4 when 'Week' then to_date(to_char((date_trunc('week', date_trunc('week', now()) - interval '1 week') + interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		       +"when 'Month' then to_date(to_char((date_trunc('month', date_trunc('month', now()) - interval '1 month') + interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		       +"when 'Date' then ?2 "
		       +"end "
	           +"end "
		       +"order by mdd.driver_name, mbd.bus_reg_number,mr.route_name, r.rota_date, md.depot_name, mbt.bus_type_name" , nativeQuery=true)
		List<Object[]> driverwiseBuswiseRoutewiseKmplReport(Date startDate, Date endDate, Integer busType, String grouping, Integer depoId,String select);
		
		@Query(value="select distinct mr.route_name, mbd.bus_reg_number, mdd.driver_name, md.depot_name, mbt.bus_type_name, "
				+"(case ?4 when 'Date' then 'Date' "
		           +"when 'Week' then  (select concat(to_char(to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'),'W'),"
				   +"' week of ' , to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon'))) "
   			   +"when 'Month' then (select  to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon')) " 
		           +"when 'None' then 'None' end) as p_group, "
			       +"(select string_agg( mc.city_name || '-' || mc2.city_name ,'||') fgfdg from "+RestConstants.SCHEMA_NAME +"."+"route_rotation rr "
		 	       +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc on mc.city_id = rr.from_city_id "
		           +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc2 on mc2.city_id =rr.to_city_id where rr.route_id =mr.id) rotation, "
		           +"mbr.kmpl_as_scheduled_kilometer as kmpl, r.rota_date as start_date, dr.rotation_availability_date as end_date,mbr.created_on as refuel_date, mbr.kmpl_as_pervtskms "
			       +"from "+RestConstants.SCHEMA_NAME +"."+"mst_bus_refueling mbr "
		     	   +"join "+RestConstants.SCHEMA_NAME +"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
			       +"join "+RestConstants.SCHEMA_NAME +"."+"roaster r on r.rota_id = dr.rota_id "
			       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
			       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_route mr on mr.id = mbr.route_master "
			       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mbr.depot_id "
			       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
			       +"where md.depot_id =?5 and mbt.bus_type_id=?3 "
		     	   +"and case ?4 when 'None' then 2=2 else to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			       +"case ?4 when 'Week' then to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			       +"when 'Month' then to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			       +"when 'Date' then ?1 "
		           +"end and "
			       +"case ?4 when 'Week' then to_date(to_char((date_trunc('week', date_trunc('week', now()) - interval '1 week') + interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			       +"when 'Month' then to_date(to_char((date_trunc('month', date_trunc('month', now()) - interval '1 month') + interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			       +"when 'Date' then ?2 "
			       +"end "
		           +"end "
			       +"order by  mr.route_name,mbd.bus_reg_number, mdd.driver_name, r.rota_date, md.depot_name, mbt.bus_type_name" , nativeQuery=true)
			List<Object[]> routewiseBuswiseDriverwiseKmplReport(Date startDate, Date endDate, Integer busType, String grouping, Integer depoId,String select);
			
			@Query(value="select distinct mbd.bus_reg_number,mr.route_name, mdd.driver_name,  md.depot_name, mbt.bus_type_name, "
					   +"(case ?4 when 'Date' then 'Date' "
			           +"when 'Week' then  (select concat(to_char(to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'),'W'),"
    				   +"' week of ' , to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon'))) "
       			       +"when 'Month' then (select  to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon')) " 
			           +"when 'None' then 'None' end) as p_group, "
				       +"(select string_agg( mc.city_name || '-' || mc2.city_name ,'||') fgfdg from "+RestConstants.SCHEMA_NAME +"."+"route_rotation rr "
			 	       +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc on mc.city_id = rr.from_city_id "
			           +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc2 on mc2.city_id =rr.to_city_id where rr.route_id =mr.id) rotation, "
			           +"mbr.kmpl_as_scheduled_kilometer as kmpl, r.rota_date as start_date, dr.rotation_availability_date as end_date,mbr.created_on as refuel_date, mbr.kmpl_as_pervtskms "
				       +"from "+RestConstants.SCHEMA_NAME +"."+"mst_bus_refueling mbr "
			     	   +"join "+RestConstants.SCHEMA_NAME +"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
				       +"join "+RestConstants.SCHEMA_NAME +"."+"roaster r on r.rota_id = dr.rota_id "
				       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
				       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
				       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_route mr on mr.id = mbr.route_master "
				       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mbr.depot_id "
				       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
				       +"where md.depot_id =?5 and mbt.bus_type_id=?3 "
			     	   +"and case ?4 when 'None' then 2=2 else to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
				       +"case ?4 when 'Week' then to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
				       +"when 'Month' then to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
				       +"when 'Date' then ?1 "
			           +"end and "
				       +"case ?4 when 'Week' then to_date(to_char((date_trunc('week', date_trunc('week', now()) - interval '1 week') + interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
				       +"when 'Month' then to_date(to_char((date_trunc('month', date_trunc('month', now()) - interval '1 month') + interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
				       +"when 'Date' then ?2 "
				       +"end "
			           +"end "
				       +"order by mbd.bus_reg_number,mr.route_name, mdd.driver_name,  r.rota_date, md.depot_name, mbt.bus_type_name" , nativeQuery=true)
				List<Object[]> buswiseRoutewiseDriverwisekmplReport(Date startDate, Date endDate, Integer busType, String grouping, Integer depoId,String select);
				
				@Query(value="select distinct mdd.driver_name, mr.route_name, mbd.bus_reg_number,  md.depot_name, mbt.bus_type_name, "
				 	       +"(case ?4 when 'Date' then 'Date' "
				           +"when 'Week' then  (select concat(to_char(to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'),'W'),"
           				   +"' week of ' , to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon'))) "
              			   +"when 'Month' then (select  to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon')) " 
 				           +"when 'None' then 'None' end) as p_group, "
					       +"(select string_agg( mc.city_name || '-' || mc2.city_name ,'||') fgfdg from "+RestConstants.SCHEMA_NAME +"."+"route_rotation rr "
				 	       +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc on mc.city_id = rr.from_city_id "
				           +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc2 on mc2.city_id =rr.to_city_id where rr.route_id =mr.id) rotation, "
				           +"mbr.kmpl_as_scheduled_kilometer as kmpl, r.rota_date as start_date, dr.rotation_availability_date as end_date,mbr.created_on as refuel_date, mbr.kmpl_as_pervtskms "
					       +"from "+RestConstants.SCHEMA_NAME +"."+"mst_bus_refueling mbr "
				     	   +"join "+RestConstants.SCHEMA_NAME +"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
					       +"join "+RestConstants.SCHEMA_NAME +"."+"roaster r on r.rota_id = dr.rota_id "
					       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
					       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
					       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_route mr on mr.id = mbr.route_master "
					       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mbr.depot_id "
					       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
					       +"where md.depot_id =?5 and mbt.bus_type_id=?3 "
				     	   +"and case ?4 when 'None' then 2=2 else to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
					       +"case ?4 when 'Week' then to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
					       +"when 'Month' then to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
					       +"when 'Date' then ?1 "
				           +"end and "
					       +"case ?4 when 'Week' then to_date(to_char((date_trunc('week', date_trunc('week', now()) - interval '1 week') + interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
					       +"when 'Month' then to_date(to_char((date_trunc('month', date_trunc('month', now()) - interval '1 month') + interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
					       +"when 'Date' then ?2 "
					       +"end "
				           +"end "
					       +"order by mdd.driver_name, mr.route_name, mbd.bus_reg_number, r.rota_date, md.depot_name, mbt.bus_type_name" , nativeQuery=true)
				
					List<Object[]> driverwiseRoutewiseBuswiseKmplReport(Date startDate, Date endDate, Integer busType, String grouping, Integer depoId,String select);
					
					@Query(value="select distinct mr.route_name, mdd.driver_name,mbd.bus_reg_number, md.depot_name, mbt.bus_type_name, "
							   +"(case ?4 when 'Date' then 'Date' "
					           +"when 'Week' then  (select concat(to_char(to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'),'W'),"
	           				   +"' week of ' , to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon'))) "
	              			   +"when 'Month' then (select  to_char(to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD'), 'Mon')) " 
	 				           +"when 'None' then 'None' end) as p_group, "
						       +"(select string_agg( mc.city_name || '-' || mc2.city_name ,'||') fgfdg from "+RestConstants.SCHEMA_NAME +"."+"route_rotation rr "
					 	       +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc on mc.city_id = rr.from_city_id "
					           +"inner join "+RestConstants.SCHEMA_NAME +"."+"mst_city mc2 on mc2.city_id =rr.to_city_id where rr.route_id =mr.id) rotation, "
					           +"mbr.kmpl_as_scheduled_kilometer as kmpl, r.rota_date as start_date, dr.rotation_availability_date as end_date,mbr.created_on as refuel_date, mbr.kmpl_as_pervtskms "
						       +"from "+RestConstants.SCHEMA_NAME +"."+"mst_bus_refueling mbr "
					     	   +"join "+RestConstants.SCHEMA_NAME +"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
						       +"join "+RestConstants.SCHEMA_NAME +"."+"roaster r on r.rota_id = dr.rota_id "
						       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
						       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
						       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_route mr on mr.id = mbr.route_master "
						       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mbr.depot_id "
						       +"join "+RestConstants.SCHEMA_NAME +"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
						       +"where md.depot_id =?5 and mbt.bus_type_id=?3 "
					     	   +"and case ?4 when 'None' then 2=2 else to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
						       +"case ?4 when 'Week' then to_date(to_char(date_trunc('week',(date_trunc('week', now()) - interval '1 week')+ interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
						       +"when 'Month' then to_date(to_char(date_trunc('month',(date_trunc('month', now()) - interval '1 month')+ interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
						       +"when 'Date' then ?1 "
					           +"end and "
						       +"case ?4 when 'Week' then to_date(to_char((date_trunc('week', date_trunc('week', now()) - interval '1 week') + interval '1 week' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
						       +"when 'Month' then to_date(to_char((date_trunc('month', date_trunc('month', now()) - interval '1 month') + interval '1 month' - interval '1 day'), 'YYYY/MM/DD'), 'YYYY/MM/DD') "
						       +"when 'Date' then ?2 "
						       +"end "
					           +"end "
						       +"order by mr.route_name, mdd.driver_name,mbd.bus_reg_number, r.rota_date, md.depot_name, mbt.bus_type_name" , nativeQuery=true)
						List<Object[]> routewiseDriverwiseBuswiseKmplReport(Date startDate, Date endDate, Integer busType, String grouping, Integer depoId,String select);

	/*
	 * @Query(
	 * value="select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
	 * +"(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time"
	 * +"from " +RestConstants.SCHEMA_NAME+"."+ "mst_recieve_diesel_sup mrds "
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * " punbus_dev.mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "punbus_dev.mst_depot md on md.depot_id = mft.depot_id"
	 * +"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 "
	 * ,nativeQuery=true) List<Object[]> driverWiseBusRouteKMPL(Date startDate,
	 * Date endDate, String driver);
	 */
/*	@Query(value = "select  mbd.bus_reg_number, mr.route_name,  "
			+ " (mbr.scheduled_kms + mbr.dead_kms) as Total_KMs, (mbr.vts_kms) as VTS_KMs,mbr.issued_diesel,  "
			+ " mbr.kmpl_as_scheduled_kilometer ,mdd.driver_name from "
			+ " "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master = mr.id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id and dr.route_id =mr.id and dr.bus_id =mbd.bus_id "
			+ " and dr.roaster_id in (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id and dr2.route_id =mr.id and dr2.bus_id =mbd.bus_id ) "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
			+ " where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 and mr.id = ?4  and (mdd.driver_id= ?3 or 0= ?3)", nativeQuery = true)*/
	
	@Query(value = "select mdd.driver_name, mbd.bus_reg_number, mr.route_name, (mbr.scheduled_kms + mbr.dead_kms) as Total_KMs, mbr.issued_diesel, mbr.kmpl_as_scheduled_kilometer,to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
	      +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
	      +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbr.bus_id = mbd.bus_id "
	      +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mbr.route_master = mr.id "
	      +"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id and dr.route_id =mr.id and dr.bus_id =mbd.bus_id "
	      +"and dr.roaster_id in (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id and dr2.route_id =mr.id "
	      +"and dr2.bus_id =mbd.bus_id ) "
	      +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on dr.driver_id = mdd.driver_id "
	      +"where mdd.driver_id =?3  and (mr.id =?4 or 0=?4) "
	      +"and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 " , nativeQuery=true)
     List<Object[]> driverWiseBusRouteKMPL( Date startDate, Date endDate,Integer driverId,Integer routeId);

	/*@Query(value = "select mbt.bus_type_name, mbd.bus_reg_number, mrc.route_category_name, mou.quantity "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type= mbt.bus_type_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.bus_type_id = mbt. bus_type_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mobil_oil_used mou on mbd.bus_id = mou.bus_id "
+"where mbd.bus_reg_number= ?3 and to_date(to_char(mbd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mbd.bus_reg_number, mbt.bus_type_name , mrc. route_category_name, mou.quantity ", nativeQuery = true)
	List<Object[]> busTypeBusWiseDieselMobileReport(Date startDate, Date endDate, String busType);*/
	
	
	
	
	/*@Query(value = "select mbt.bus_type_name, mbd.bus_reg_number, mrc.route_category_name,  "
			+ " sum(coalesce (mou.quantity,0)) as mobil_oil_issued, mdd.driver_name,mdd.mobile_number  "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type= mbt.bus_type_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.bus_type_id = mbt.bus_type_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mobil_oil_used mou on mbd.bus_id = mou.bus_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mbd.depot = mdd.depot_id  "
			+ " where mbt.bus_type_name= ?4 and mbd.bus_reg_number=?1 and to_date(to_char(mou.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD')  "
			+ " between ?2 and ?3 group by mbd.bus_reg_number, mbt.bus_type_name ,  "
			+ " mrc. route_category_name, mou.quantity, mdd.driver_name, mdd.mobile_number  "
			+ " order by 3 asc,4 desc ",nativeQuery=true)*/
	
	/*@Query(value = "select mbt.bus_type_name, mbd.bus_reg_number, mrc.route_category_name, mdd.driver_name, mdd.mobile_number as driver_number, "
	          +"(coalesce (mou.quantity,0)) as mobil_oil_issued, to_date(to_char(mou.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') issuance_date "
	          +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
	          +"left join "+RestConstants.SCHEMA_NAME+"."+"mobil_oil_used mou on mbd.bus_id = mou.bus_id "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type= mbt.bus_type_id "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mbd.depot = mdd.depot_id "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.depot_id = mbd.depot "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
	          +"where mbt.bus_type_name=?4 and mbd.bus_reg_number=?1 "
	          +"and to_date(to_char(mou.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
	          +"order by 3 asc, 4 desc",nativeQuery=true)*/
	
	@Query(value="select mbt.bus_type_name, mbd.bus_reg_number, mrc.route_category_name, mdd.driver_name, mdd.mobile_number as driver_number, "
	          +"(coalesce (mou.quantity,0)) as mobil_oil_issued, to_date(to_char(mou.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') issuance_date "
	          +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
       	      +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type= mbt.bus_type_id "
	          +"left join "+RestConstants.SCHEMA_NAME+"."+"mobil_oil_used mou on mbd.bus_id = mou.bus_id "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.mmbil_oil_used_id = mou.mobil_oil_used_id "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route_category mrc on mr.route_category_id = mrc.route_category_id "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
	          +"where mbt.bus_type_id= ?4 and mbd.bus_reg_number= ?1 and "
	          +"dr.roaster_id = (select max(dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id = mbr.bus_refueling_id) "
	          +"and to_date(to_char(mou.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
	          +"order by 3 asc, 4 desc " , nativeQuery= true)
    List<Object[]> busTypeBusWiseDieselMobileOilReport(String busNo,Date startDate, Date endDate, Integer busType);

	/*
	 * @Query(
	 * value="select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
	 * +"(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time"
	 * +"from " +RestConstants.SCHEMA_NAME+"."+ "mst_recieve_diesel_sup mrds "
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * " punbus_dev.mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id"
	 * +"join " +RestConstants.SCHEMA_NAME+"."+
	 * "punbus_dev.mst_depot md on md.depot_id = mft.depot_id"
	 * +"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 "
	 * ,nativeQuery=true) List<Object[]> variationBeyondReport(Date startDate,
	 * Date endDate, Integer depoId);
	 */

	/*@Query(value = "select md.depot_name, mus.variants as variation, mft.tank_name from "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mft.depot_id = md.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus on mft.fuel_tank_id = mus.fuel_tank_id "
+"where mft.depot_id =?1 and to_date(to_char(mft.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') <= ?2 ", nativeQuery = true)*/
	
	  @Query(value = "select md.depot_name, dcr.excess_short, mft.tank_name, dcr.created_on as date "
	  		+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft "
	  		+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mft.depot_id = md.depot_id "
	  		+ " left join "+RestConstants.SCHEMA_NAME+"."+"dip_chart_readings dcr on mft.fuel_tank_id = dcr.fuel_tank_id and mft.status = TRUE "
	  		+ " where mft.depot_id =?1 and dcr.excess_short >4 "
	  		+ " and to_date(to_char(dcr.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ", nativeQuery = true)
      List<Object[]> variationBeyondReport(Integer depoId, Date fromDate, Date toDate);

	@Query(value = "select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
			+ "(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
			+ "from " +RestConstants.SCHEMA_NAME+"." +"mst_recieve_diesel_sup mrds "+ "join "
			+ RestConstants.SCHEMA_NAME + "." +"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
			+ "join " +RestConstants.SCHEMA_NAME + "."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id "+"join "
			+ RestConstants.SCHEMA_NAME +"."+"mst_depot md on md.depot_id = mft.depot_id "
			+"where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 ", nativeQuery = true)
	List<Object[]> depotStateWiseBusTypeComparReport(Date startDate, Date endDate, Integer depoId, Integer busType);

	
	  @Query(value="select md.depot_name, mft.fuel_type_name, coalesce (diop.diesel_issued,0) fuel_issued, p.purpose_name, "
              +"coalesce(diop.diesel_issued,0) *( case when mft.fuel_type_id =1 then d.dr "
              +"when mft.fuel_type_id =2 then m.mr "
              +"when mft.fuel_type_id =3 then a.ar "
              +"else 0 end )as value_in_rs, to_date(to_char(diop.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date_issue, "
              +"diop.remarks from "+RestConstants.SCHEMA_NAME+"."+"diesel_issued_other_purpose diop "
              +"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = diop.depot_id "
              +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_type mft on mft.fuel_type_id = diop.fuel_type_id "
              +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_other_purpose p on p.other_purpose_id=diop.other_purpose_id "
              +"left join (select max(mrds.diesel_rate) dr,max(a.depot_id) depot_id from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds "
              +"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_depot_transport a on a.depot_transport_id =mrds.transport_id "
              +")d on d.depot_id =md.depot_id "
              +"left join (select max(mmd2.value)mr ,max(mmd2.depot) depot from "+RestConstants.SCHEMA_NAME+"."+"mst_mobil_drum mmd2)m on m.depot =md.depot_id "
              +"left join (select max(mabd.value)ar,max(mabd.depot)depot from "+RestConstants.SCHEMA_NAME+"."+"mst_add_blue_drum mabd )a on m.depot =md.depot_id "
              +"where md.depot_id=?3 and to_date(to_char(diop.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true) 
	 List<Object[]> dieselIssueOtherPurposeReport(Date startDate, Date endDate, Integer depoId);
	 

	/*@Query(value = "select md.depot_name, mbr.issued_diesel, (mbr.issued_diesel * mrds.diesel_rate) as value_of_stock, '' available_mobil_oil_stock,'' value_of_stock_mobil"
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mbr.depot_id = md.depot_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus on mbr.fuel_tank_id = mus.fuel_tank_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds on mrds.update_supply_id = mus.update_supply_id "
			+"where mbr.depot_id =?1 and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ", nativeQuery = true)
	List<Object[]> dieselIssueOtherPurposeReport(Integer depoId, Date startDate, Date endDate);*/

	/*@Query(value = "select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
			+ "(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
			+"from " +RestConstants.SCHEMA_NAME +"." +"mst_recieve_diesel_sup mrds "+"join "
			+RestConstants.SCHEMA_NAME +"." +"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
			+"join " +RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id " 
			+"join "+RestConstants.SCHEMA_NAME+"." +"mst_depot md on md.depot_id = mft.depot_id "
			+"where md.depot_id =3 and mrds.created_on between ?1 and ?2 ", nativeQuery = true)
	List<Object[]> dieselNotIssuedReport(Date startDate, Date endDate);
*/
	 
	 
	 
	/* @Query(value="select distinct mbt.bus_type_name, mbd.bus_reg_number, mr.route_name,SUBSTRING(cast(mr.created_on as varchar(19)) FROM 12 FOR 20) as Route_time,"
+"to_date(to_char(r.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') as route_date from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id "
+"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id =dr.rota_id "
+"where dr.refueling_id is null and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"order by 2,5",nativeQuery=true)*/
	 
//	 @Query(value="select distinct mbt.bus_type_name, mbd.bus_reg_number, mr.route_name, SUBSTRING(cast(mr.created_on as varchar(19)) FROM 12 FOR 20) as Route_time, "
//	          +"to_date(to_char(r.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') as route_date from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
//	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
//	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
//	          +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id "
//	          +"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id =dr.rota_id "
//	          +"where dr.refueling_id is null and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
//	          +"order by 2,5", nativeQuery=true)
//	 
	 @Query(value= "select distinct mbt.bus_type_name, mbd.bus_reg_number, mr.route_name, SUBSTRING(cast(mr.created_on as varchar(19)) FROM 12 FOR 20) as Route_time, "
 	 +"to_date(to_char(r.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') as route_date "
 	 +"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
	 +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
	 +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
	 +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id "
	 +"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id =dr.rota_id "
	 +"left join (select count(distinct dr2.rota_id)as count_rota, dr2.bus_id, count(distinct case when dr2.refueling_id is not null then dr2.rota_id end) as count_refueling "
	 +"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 join "+RestConstants.SCHEMA_NAME+"."+"roaster r2 on r2.rota_id = dr2.rota_id "
	 +"where to_date(to_char(r2.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 group by dr2.bus_id) as cnt on cnt.bus_id = dr.bus_id "
	 +"where dr.refueling_id is null "
	 +"and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 and cnt.count_rota> cnt.count_refueling "
	 +"union all "
	 +"select mbt.bus_type_name, mbd.bus_reg_number, 'NA' route_name, 'NA' Route_time, Null Route_date "
	 +"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd "
	 +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbd.bus_type = mbt.bus_type_id "
	 +"where mbd.bus_id not in (select dr.bus_id from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
	 +"where to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2) ",nativeQuery=true)
      List<Object[]> dieselNotIssuedReport(Date startDate, Date endDate);
	 
	 
	/*@Query(value = "select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
			+ "(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup mrds "+"join "
			+RestConstants.SCHEMA_NAME+"."+"mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id "
			+"join " +RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id " 
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mft.depot_id "
			+"where md.depot_id =3 and mrds.created_on between ?1 and ?2 ", nativeQuery = true)*/
	@Query(value = "select mt.transport_name, mbd.bus_reg_number, mr.route_name, to_date(to_char(r.rota_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') as route_date, "
	        +"sum(coalesce(mbr.scheduled_kms,0) + coalesce(mbr.dead_kms,0)) as DPA_kms, '' cashier_kms "
	        +"from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on dr.bus_id = mbd.bus_id "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mbd.transport_unit "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on dr.route_id = mr.id "
       	    +"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id =dr.rota_id "
	        +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
	        +"where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
	        +"group by mt.transport_name, mbd.bus_reg_number, mr.route_name, r.rota_date order by 5, 4 " , nativeQuery=true)
	List<Object[]> grossKMSDepositedCashierVsDpaReport(Date startDate, Date endDate);

	/*@Query(value = "select  md.depot_name , mrds.quantity_recieved, mrds.entry_date as date_of_supply_received, mrds.invoice_number,"
			+ "(mrds.diesel_rate * mrds.quantity_recieved) as Rate_Liter_Received , mrds.entry_time as Supply_Received_time"
			+ "from " + RestConstants.SCHEMA_NAME + "." + "mst_recieve_diesel_sup mrds " + "join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_update_supply mus on mrds.update_supply_id = mus.update_supply_id"
			+ "join " + RestConstants.SCHEMA_NAME + "."
			+ " punbus_dev.mst_fuel_tank mft on mus.fuel_tank_id = mft.fuel_tank_id" + "join "
			+ RestConstants.SCHEMA_NAME + "." + "punbus_dev.mst_depot md on md.depot_id = mft.depot_id"
			+ "where md.depot_id = ?3 and mrds.created_on between ?1 and ?2 ", nativeQuery = true)
	List<Object[]> inspectionCarriedOutReport(Date startDate, Date endDate, Integer depoId);*/
	/*@Query(value="select md.depot_name, ti.inspection_date, ti.status "+"from "+RestConstants.SCHEMA_NAME+"."+"tank_inspection ti "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on ti.depot_id = md.depot_id "
+"where ti.depot_id = ?1 and to_date(to_char(ti.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)*/
	
	/*@Query(value = "select md.depot_name, to_date(to_char(ti.inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as inspection_date, ti.remarks as status_t "
			    +"from "+RestConstants.SCHEMA_NAME+"."+"tank_inspection ti join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on ti.depot_id = md.depot_id "
                +"where ti.depot_id =?1 and to_date(to_char(ti.inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3", nativeQuery= true)
	*/
	@Query(value="select md.depot_name, to_date(to_char(ti.inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as inspection_due_date, ti.remarks as status_t, "
	+"to_date(to_char(ti.next_inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')as next_due_date, u.username as authority_name from "+RestConstants.SCHEMA_NAME+"."+"tank_inspection ti "
	+"join "+RestConstants.SCHEMA_NAME+"."+"users u on ti.created_by = u.id join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on ti.depot_id = md.depot_id "
	+"where ti.depot_id =?1 and to_date(to_char(ti.inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)
	
	List<Object[]> inspectionCarriedOutReport(Integer depoId,Date startDate, Date endDate);
	
	
	@Query(value="select us.username , r.role_names,r.role_code, md.depot_name depot ,SUM(case when inspection.tank_inspection_id is not null then 1 else 0 end)   "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"users us  "
			+ "  join "+RestConstants.SCHEMA_NAME+"."+"mst_role r on us.role_id  = r.role_id AND r.role_code in ('GM','SO','WM') and us.depot_id = ?1 "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id =us.depot_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"tank_inspection inspection on us.id = inspection.created_by "
			+ " and to_date(to_char(inspection.inspection_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3  "
			+ " group by us.username,r.role_names,md.depot_name,r.role_code ",nativeQuery=true)
			List<Object[]> inspectionDoneVersusDueReport(Integer depoId,Date startDate, Date endDate);
			
    @Query(value="select md.depot_name, mbd.bus_reg_number, concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_end_time)as sch_date_time, "
            +"mbr.created_on as refuel_date_time, "
            +"EXTRACT(EPOCH FROM ( TO_TIMESTAMP((SUBSTRING(cast(mbr.created_on as varchar(19)) FROM 1 FOR 20)), 'YYYY-MM-DD HH24:MI:SS' )- "
            +"TO_TIMESTAMP(concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_end_time) , 'YYYY-MM-DD HH24:MI:SS' )))/3600 as duration "
            +"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
            +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on md.depot_id = mbd.depot "
            +"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.bus_id = mbd.bus_id "
            +"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
            +"join  "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id and "
            +"dr.roaster_id in (select max(dr2.roaster_id) from  "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id "
            +" = mbr.bus_refueling_id and dr2.trip_id= mt.trip_id and dr2.route_id= mbr.route_master) "
            +"where md.depot_id=?3 and EXTRACT(EPOCH FROM ( TO_TIMESTAMP((SUBSTRING(cast(mbr.created_on as varchar(19)) FROM 1 FOR 20)), 'YYYY-MM-DD HH24:MI:SS' )- "
            +"TO_TIMESTAMP(concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_end_time) , 'YYYY-MM-DD HH24:MI:SS' )))/3600>1 "
            +"and to_date(to_char(dr.rotation_availability_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
	List<Object[]> busesNotGettingDieselIssueReport(Date startDate, Date endDate, Integer depoId);

}
