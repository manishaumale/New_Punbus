package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusRefuelingMaster;
import com.idms.base.dao.entity.QBusRefuelingMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface BusRefuelingMasterRepository extends JpaRepository<BusRefuelingMaster, Integer> , QuerydslPredicateExecutor<BusRefuelingMaster>, QuerydslBinderCustomizer<QBusRefuelingMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBusRefuelingMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<BusRefuelingMaster> findAllByStatus(boolean status);
	
	@Query(value = "select mbr.bus_refueling_id, mbd.bus_reg_number, mbr.issued_diesel, mbr.diesel_from_outside, mbr.created_on, (mr.scheduled_kms  + mr.dead_kms ) as gross_kms, "
			+ " mbr.kmpl_as_per_gross_kms, mbr.total_actual_kms, mbr.kmpl_asper_actual_kms, mr.route_name,mbr.vts_kms,mbr.kmpl_as_pervtskms,mbr.scheduled_kms,mbr.kmpl_as_scheduled_kilometer "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit mdu  on mdu.dispensing_unit_id = mbr.dispensing_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master "
			+ " where mbr.depot_id =?1 order by mbr.bus_refueling_id desc", nativeQuery=true)
	List<Object[]> findAllBusRefuelingByDepot(Integer dpId);
	
	@Query(value="select mbrr.reason, mbd.bus_reg_number, mdu.dis_unit_name, mft.tank_name, mbr.issued_diesel, mbr.diesel_from_outside, mr.route_name, "
			+ " mbt.bus_type_name, mdd.driver_name, mbr.scheduled_kms, mbr.dead_kms, mbr.plain_kms , mbr.hill_kms, mbr.kmpl_as_scheduled_kilometer , mbr.kmpl_as_per_gross_kms , "
			+ " mbr.kmpl_asper_actual_kms , mbr.kmpl_as_pervtskms , mbr.reason_for_extra_dead , mbr.remarks, mbr.extra_dead_kms, mbr.total_actual_kms, mbr.gross_kms,  "
			+ " mfto.quantity, mfs.name, mfto.bill_no, mfto.fuel_taken_date, mfto.amount, mfto.bill_document_id, md.document_name, mou.quantity as mou_quantity, "
			+ " abu.quantity as abu_quantity, mbr.vts_kms,drum.name_of_drum as drum, blue.name_of_drum as blueDrum, "
			+ " mou.amount as mobilAmount,mou.is_out_side as mobOutside,abu.amount as addBlueAmount,abu.is_out_side as addOutside,md1.document_id as mobilDocId,md1.document_name as MobilDocName, "
			+ " md2.document_id as addBlueDocId,md2.document_name as addBlueDocName "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_return_reason mbrr on mbrr.reason_id = mbr.reason_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit mdu  on mdu.dispensing_unit_id = mbr.dispensing_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mft.fuel_tank_id = mbr.fuel_tank_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.refueling_id = mbr.bus_refueling_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_taken_out mfto on mfto.fuel_taken_id = mbr.fuel_taken_out_side_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_source mfs on mfs.fuel_source_id = mfto.fuel_source_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mobil_oil_used mou ON mou.mobil_oil_used_id = mbr.mmbil_oil_used_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_mobil_drum drum ON drum.mobil_drum_id = mou.mobil_drum_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"ad_blue_used abu on abu.add_blue_id = mbr.ad_blue_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_add_blue_drum blue ON blue.add_blue_drum_id = abu.add_blue_drum_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_document md on md.document_id = mfto.bill_document_id "
            + " left join "+RestConstants.SCHEMA_NAME+"."+"mst_document md1 on md1.document_id = mou.mobil_oil_document_id "
            + " left join "+RestConstants.SCHEMA_NAME+"."+"mst_document md2 on md2.document_id = abu.add_blue_document_id "
			+ " where mbr.bus_refueling_id =?1 limit 1;", 
			nativeQuery = true)
	List<Object[]> findRefuelingById(Integer id);
	
	@Query(value="select mbrr.reason, mbd.bus_reg_number, mdu.dis_unit_name, mft.tank_name, mbr.issued_diesel, mbr.diesel_from_outside, mr.route_name, "
			+ " mbt.bus_type_name, mdd.driver_name, mbr.scheduled_kms, mbr.dead_kms, mbr.plain_kms , mbr.hill_kms, mbr.kmpl_as_scheduled_kilometer , mbr.kmpl_as_per_gross_kms , "
			+ " mbr.kmpl_asper_actual_kms , mbr.kmpl_as_pervtskms , mbr.reason_for_extra_dead , mbr.remarks, mbr.extra_dead_kms, mbr.total_actual_kms, mbr.gross_kms,  "
			+ " mfto.quantity, mfs.name, mfto.bill_no, mfto.fuel_taken_date, mfto.amount, mfto.bill_document_id, "
			+ " md.document_name, mou.quantity as mou_quantity, abu.quantity as abu_quantity, mbr.vts_kms,drum.name_of_drum as drum, blue.name_of_drum as blueDrum,mou.amount as mobilAmount, "
			+ " mou.is_out_side as mobOutside,abu.amount as addBlueAmount,abu.is_out_side as addOutside,md1.document_id as mobilDocId,md1.document_name as MobilDocName, "
			+ " md2.document_id as addBlueDocId,md1.document_name as addBlueDocName "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_return_reason mbrr on mbrr.reason_id = mbr.reason_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = mbr.bus_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_dispensing_unit mdu  on mdu.dispensing_unit_id = mbr.dispensing_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_tank mft on mft.fuel_tank_id = mbr.fuel_tank_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = mbr.route_master "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.bus_id = mbr.bus_id and mbr.route_master = dr.route_id and dr.trip_id = mbr.trip_id"
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_taken_out mfto on mfto.fuel_taken_id = mbr.fuel_taken_out_side_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_fuel_source mfs on mfs.fuel_source_id = mfto.fuel_source_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mobil_oil_used mou ON mou.mobil_oil_used_id = mbr.mmbil_oil_used_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_mobil_drum drum ON drum.mobil_drum_id = mou.mobil_drum_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"ad_blue_used abu on abu.add_blue_id = mbr.ad_blue_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_add_blue_drum blue ON blue.add_blue_drum_id = abu.add_blue_drum_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_document md on md.document_id = mfto.bill_document_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_document md1 on md1.document_id = mou.mobil_oil_document_id "
	        + " left join "+RestConstants.SCHEMA_NAME+"."+"mst_document md2 on md2.document_id = abu.add_blue_document_id "
			+ " where mbr.bus_refueling_id =?1 limit 1;", 
			nativeQuery = true)
	List<Object[]> findRefuelingByIdMidTrip(Integer id);
	
	@Query(value = "select round(AVG(t.kmpl)),t.dc from (SELECT refueling.kmpl_as_scheduled_kilometer kmpl,driver.driver_code dc,driver.driver_name FROM "+ RestConstants.SCHEMA_NAME + ".mst_bus_refueling refueling "
			+ " join "+ RestConstants.SCHEMA_NAME + ".daily_roaster daily on refueling.bus_refueling_id = daily.refueling_id "
			+ " join "+ RestConstants.SCHEMA_NAME + ".mst_driver_details driver on driver.driver_id = daily.driver_id "
			+ " where   daily.route_id = ?1 and daily.bus_id = ?2 "
			+ " ORDER BY bus_refueling_id DESC LIMIT 15)t group by t.dc ", nativeQuery = true)
	Object[] fetchMaxScheduledKmsByRouteAndBus(Integer routeId,Integer busId);
	
	//@Query(value = "SELECT kmpl_as_scheduled_kilometer FROM "+ RestConstants.SCHEMA_NAME + ".mst_bus_refueling refueling where bus_refueling_id in ?1 ORDER BY bus_refueling_id DESC LIMIT 1 ", nativeQuery = true)
	//Float fetchCurrentMaxKms(List<Integer> refuelingIds);
	
	
	@Query(value = "SELECT avg(kmpl_as_scheduled_kilometer) from ( select kmpl_as_scheduled_kilometer from "+ RestConstants.SCHEMA_NAME + ".mst_bus_refueling refueling where bus_refueling_id in ?1 ORDER BY kmpl_as_scheduled_kilometer DESC LIMIT 15)t ", nativeQuery = true)
	Float fetchCurrentMaxKms(Set<Integer> refuelingIds);

	@Query(nativeQuery = true, value = " select sum(mbr.issued_diesel) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr where mbr.bus_id=?1  and mbr.route_master =?2 and mbr.reason_id = 2")
	Integer findByBusId(Integer busId, Integer id);
	
	@Query(nativeQuery = true, value = " select sum(mbr.issued_diesel) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr where mbr.bus_id=?1  and mbr.route_master =?2 and mbr.trip_id=?3 and mbr.rota_id = ?4")
	Integer findSumByBusDetails(Integer busId, Integer routeId,Integer tripId,Integer rotaId);
	
	@Query(nativeQuery = true , value = " select mbr.* from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr where mbr.bus_id =?1 and mbr.created_on::::text ilike %?2%")
	List<BusRefuelingMaster> findByIdAndRefuelingDate(Integer busId,String refuelingDate);
	
	@Query(nativeQuery = true , value = "select * from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling where diesel_corrected=true")
	List<BusRefuelingMaster> findAllDieselCorectedValues();
	
	@Query(nativeQuery = true, value = " select sum(total_actual_kms) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr where mbr.bus_id in ?1")
	Integer findMileageBusDetails(Integer[] busIds);
	
	@Modifying
    @Query(value = "Update BusRefuelingMaster set isDeleted=?1 where id=?2 ")
	int deleteBusRefuellingByStatusFlag(boolean flag, Integer id);
	
	@Query(nativeQuery = true, value = " select coalesce (sum(case when coalesce (mbr.vts_kms,0)=0  "
			+ " then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ " else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ " end  ),0)  from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr  where mbr.bus_id=?1 and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3")
	Float findVtsKmsByBusIdsAndDates(Integer busId,Date fromDate,Date toDate);
	
	@Query(nativeQuery = true, value = " select coalesce(sum(total_actual_kms) ,0) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr where mbr.bus_id=?1 and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3")
	Integer findTotalKmsByBusIdsAndDates(Integer busId,Date fromDate,Date toDate);
	
	@Query(nativeQuery = true, value = "select coalesce (sum(case when coalesce (mbr.vts_kms,0)=0  "
			+ " then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ " else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "  end  ),0)total ,coalesce (sum(case when coalesce (mbr.vts_kms,0)=0  "
			+ "  then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) - mbr.hill_kms "
			+ " else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) - mbr.hill_kms  end  ) ,0) plain "
			+ " ,sum(mbr.hill_kms) from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr where mbr.bus_id=?1 and to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ " between ?2 and ?3")
	Object[] findTotalKmsAndHillAndPlainByBusIdsAndDates(Integer busId,Date fromDate,Date toDate);
	
}