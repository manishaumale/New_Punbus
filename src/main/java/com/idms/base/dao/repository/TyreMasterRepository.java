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

import com.idms.base.dao.entity.QTyreMaster;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TyreMasterRepository extends JpaRepository<TyreMaster, Integer>, QuerydslPredicateExecutor<TyreMaster>,
		QuerydslBinderCustomizer<QTyreMaster> {

	@Override
	default void customize(QuerydslBindings bindings, QTyreMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}

	List<TyreMaster> findAllByStatus(boolean status);
	
	
	

	@Query(value = " select mt.tyre_id, mt2.transport_name,mtt.name as ttype, mts.size, mt.tyre_number, mtm.name as makername, "
			+ " case when mtc.name ='New' and docket.dta_id is null then mt.expected_life when mtc.name ='Condemn' and docket.dta_id is null then mt.expected_life else cast(docket.expected_life as varchar(20)) end,case when mtc.name ='New' "
			+ " then mt.tyre_cost  else docket.tyre_cost end,mt.old_mileage,pos.name, "
			+ " bus.bus_reg_number, mt.tyre_tag,mt.invoice_number,mt.invoice_date,mt.tyre_purchase_date,mt.source_of_origin_id,mtc.name as conditionName "
			+ " from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " left join " + RestConstants.SCHEMA_NAME
			+ "." + "mst_transport mt2 on mt2.transport_id = mt.transport_unit " + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make" + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size" + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id"
			+ "  join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type  "
			+ " left  join " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association bta on bta.tyre_id = mt.tyre_id  "
			+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_details bus on bus.bus_id = bta.bus_id  "
			+ "  left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_position pos on pos.tyre_position_id = bta.tyre_position_id  "
			+ "  left join " + RestConstants.SCHEMA_NAME + "." + "docket_tyre_association docket on docket.tyre_id = mt.tyre_id  "
			+ " where mt.depot_id=?1 and mt.old_tyre = false order by mt.tyre_id desc", nativeQuery = true)
	List<Object[]> findAllByDepot(Integer integer);

	@Query(value = "select distinct mt.tyre_id, mt.tyre_number, mts.size, mtc.name as condition, mtm.name as makerName, mts.tyre_size_id, mtc.tyre_condition_id, mtm.tyre_maker_id, bta.association_id "
			+"from "+RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " left join " + RestConstants.SCHEMA_NAME
			+ "." + "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "bus_tyre_association bta on bta.tyre_id = mt.tyre_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size " + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
			+ "	where mt.depot_id = ?1 and mtc.name!='Condemn' and bta.tyre_id is null and mts.tyre_size_id in (?2)", nativeQuery = true)
	List<Object[]> getTyreListNotAssignedByDepot(Integer depotId, List<Integer> typeList);

	@Query(value = " select mt.tyre_id, mt.tyre_number, mtm.name as makername, mts.size, sum(btah.kms_done), mtc.name as condition "
			+ " from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " left join " + RestConstants.SCHEMA_NAME
			+ "." + "mst_takenoff_reason mtr on mtr.taken_off_reason_id = mt.reason_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
			+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
			+ " left join " + RestConstants.SCHEMA_NAME + "."
			+ "bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
			+ " where mt.available = false and mtr.reason_code='RS' and mt.depot_id = ?1 "
			+ " group by mt.tyre_id, mtm.name, btah.tyre_id, mt.tyre_number, mts.size, mtc.name order by mt.updated_on desc", nativeQuery = true)
	List<Object[]> getAllTyreForResole(Integer id);

	/*@Query(value = " select mt.tyre_id, mt.tyre_number, mtm.name as makername, mts.size, sum(btah.kms_done), mtc.name as condition "
			+ " from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " left join " + RestConstants.SCHEMA_NAME
			+ "." + "mst_takenoff_reason mtr on mtr.taken_off_reason_id = mt.reason_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
			+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
			+ " left join " + RestConstants.SCHEMA_NAME + "."
			+ "bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
			+ " where mt.transport_unit=?1 and mts.tyre_size_id=?2 and mtr.reason_code='CN' and mt.depot_id=?3 "
			+ " group by mt.tyre_id, mtm.name, btah.tyre_id, mt.tyre_number, mts.size, mtc.name ", nativeQuery = true)
	List<Object[]> getAllTyreForCondemnAvail(Integer tpId, Integer sizeId, Integer depotId);*/
	
	
	
	@Query(value="select mt.tyre_id, mt.tyre_number,mt.tyre_tag ,mt2.transport_name,mtm.name as manufacture,mts.size,sum(btah.kms_done),"
+"mtc.name as condition,md.depot_code,mt.tyre_condition_id from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr " 
+"on mtr.taken_off_reason_id = mt.reason_id "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm " 
+"on mtm.tyre_maker_id = mt.tyre_make "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts "
+"on mts.tyre_size_id = mt.tyre_size "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah " 
+"on btah.tyre_id = mt.tyre_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc " 
+"on mtc.tyre_condition_id = mt.tyre_condition_id "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 " 
+"on mt.transport_unit = mt2.transport_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md " 
+"on mt.depot_id = md.depot_id " 
+"where mt.transport_unit=?1 and mts.tyre_size_id=?2 "
+"and mtr.reason_code='CN' and mt.depot_id=?3 "
+"group by mt.tyre_id, mtm.name,btah.tyre_id, mt.tyre_number, mts.size, mtc.name,mt2.transport_name,md.depot_code order by mt.updated_on desc",nativeQuery= true)
List<Object[]> getAllTyreForCondemnAvail(Integer tpId, Integer sizeId, Integer depotId);

	/*@Query(value = " select mt.tyre_id, mt.tyre_number, mtm.name as makername, mts.size, sum(btah.kms_done), mtc.name as condition "
			+ " from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " left join " + RestConstants.SCHEMA_NAME
			+ "." + "mst_takenoff_reason mtr on mtr.taken_off_reason_id = mt.reason_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
			+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
			+ " left join " + RestConstants.SCHEMA_NAME + "."
			+ "bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id " + " left join "
			+ RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
			+ " where mt.transport_unit=?1 and mts.tyre_size_id=?2 and mtr.reason_code='CNM' and mt.depot_id=?3 "
			+ " group by mt.tyre_id, mtm.name, btah.tyre_id, mt.tyre_number, mts.size, mtc.name ", nativeQuery = true)
	List<Object[]> getCondemnAvailTyreList(Integer tpId, Integer sizeId, Integer id);*/
	
	
	
	@Query(value="select mt.tyre_id, mt.tyre_number,mt.tyre_tag ,mt2.transport_name,mtm.name as manufacture,mts.size,sum(btah.kms_done),"
+"mtc.name as condition,md.depot_code,mt.tyre_condition_id from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mtr.taken_off_reason_id = mt.reason_id "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah  on btah.tyre_id = mt.tyre_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "  
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt.transport_unit = mt2.transport_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id " 
+"where mt.transport_unit=?1 and mts.tyre_size_id=?2 and mtr.reason_code='CNM' and mt.depot_id=?3 group by mt.tyre_id, mtm.name," 
+"btah.tyre_id, mt.tyre_number, mts.size, mtc.name ,mt2.transport_name,md.depot_code order by mt.tyre_id desc",nativeQuery=true)
List<Object[]>getCondemnAvailTyreList(Integer tpId, Integer sizeId, Integer id);




	

	@Query(value = "SELECT t from TyreMaster t where t.depot.depotCode=?1 and t.status=?2")
	List<TyreMaster> findAllTyreByDepotAndStatus(String depotCode, boolean status);
	
	
	@Query(value="select mt.tyre_number, mtc.name as Tyre_Status, to_date(to_char(rdd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date,"
+"mtr.reason_name as Reason "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
+"join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id = bta.tyre_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"retreading_docket_details rdd on mt.depot_id = rdd.depot_id "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id and mtr.reason_name in ('Resole','ForResole') "
+"where mt.depot_id =?1 and mt.tyre_condition_id in (1,5,6,7) "
+"and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery= true)
List<Object[]>findAllTyreByDepotAndStatus(Integer depotId,Date startDate, Date endDate);

	/*@Query(value = "select tyre.tyre_number,takenOff.created_on,pos.name,takenOff.reason_name " + " from "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre tyre" + " join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_takenoff_reason  takenOff on takenOff.taken_off_reason_id = tyre.reason_id" + " left join "
			+ RestConstants.SCHEMA_NAME + "." + "bus_tyre_association asso  on asso.tyre_id= tyre.tyre_id"
			+ " left join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_position pos  on pos.tyre_position_id = asso.tyre_position_id"
			+ " where reason_id is not null and tyre.depot_id = ?1 and tyre.created_on >=?2 and tyre.created_on <=?3", nativeQuery = true)
	List<Object[]> tyreTakenOFFNotFittedReport(Integer depotId, Date startDate, Date endDate);*/
	
	
	@Query(value="select mt.tyre_number, to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as taken_off_date, mtp.name as t_position, mtr.reason_name as Reason ,mt.tyre_tag "
			+ ",md.depot_name from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on btah.tyre_id = mt.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on btah.tyre_position_id = mtp.tyre_position_id "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on btah.reason_id = mtr.taken_off_reason_id and mtr.reason_name in "
			+ "('Resole','Repair','ForResole','DoneResole','ForRepair','DoneRepair') "
			+ "where mt.depot_id =?1 and btah.tyre_condition_id in (1,5,6,7) "
			+ "and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta where btah.tyre_id = bta.tyre_id ) "
			+ "and to_date(to_char(btah.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
			+ "order by 1",nativeQuery=true)
List<Object[]> tyreTakenOFFNotFittedReport(Integer depotId, Date startDate, Date endDate);

@Query(value="select tyre.tyre_number,pos.name as Position_Name, (case when tyre.tyre_tag is not null then concat(tyre.tyre_tag, '-' ,con.name) else tyre.tyre_tag end)tyre_tag, "
		+ "mts.size as tyre_size, mtm.name as tyre_manufacturer, md.depot_name,mbd.bus_reg_number "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre tyre "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = tyre.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association asso on asso.tyre_id = tyre.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = asso.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition con on con.tyre_condition_id = tyre.tyre_condition_id and con.name='New' "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position pos on pos.tyre_position_id = asso.tyre_position_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = tyre.tyre_size "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = tyre.tyre_make "
		+ "where tyre.depot_id= ?1 and to_date(to_char(tyre.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
	List<Object[]> newTyreIssuedReportReport(Integer depotId, Date startDate, Date endDate);

	/*@Query(value = "select tyre.tyre_number, con.name as conditionName,pos.name as PositionName " + " from "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre tyre " + " join " + RestConstants.SCHEMA_NAME + "."
			+ "bus_tyre_association asso  on asso.tyre_id = tyre.tyre_id" + " join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_condition con  on con.tyre_condition_id = tyre.tyre_condition_id and con.name !='New' and con.name !='Condemn'"
			+ " left join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_position pos   on pos.tyre_position_id = asso.tyre_position_id"
			+ " where tyre.depot_id=?1 and tyre.created_on >=?2 and tyre.created_on <=?3", nativeQuery = true)
	List<Object[]> ressoleTyreIssuedReportReport(Integer depotId, Date startDate, Date endDate);*/
	
	/*@Query(value="select tyre.tyre_number, con.name as conditionName,pos.name as Position_Name "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre tyre "
			+"join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association asso on asso.tyre_id = tyre.tyre_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition con on con.tyre_condition_id = tyre.tyre_condition_id and con.name='New' "
					+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position pos  on pos.tyre_position_id = asso.tyre_position_id "
							+"where depot_id=?1 and to_date(to_char(tyre.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
	List<Object[]> ressoleTyreIssuedReportReport(Integer depotId, Date startDate, Date endDate);*/
	
	
	
	
	/*@Query(value="select tyre.tyre_number,con.name as conditionName,pos.name as PositionName "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre tyre "
			+"join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association asso  on asso.tyre_id = tyre.tyre_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition con  on con.tyre_condition_id = tyre.tyre_condition_id and con.name !='New' and con.name !='Condemn' "
					+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position pos on pos.tyre_position_id = asso.tyre_position_id "
			+"where tyre.depot_id=?1 and to_date(to_char(tyre.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
	List<Object[]> resoleTyreIssuedReportReport(Integer depotId, Date startDate, Date endDate);
	*/


@Query(value="select tyre.tyre_number, con.name as conditionName,pos.name as PositionName ,(case when tyre.tyre_tag is not null then concat(tyre.tyre_tag, '-' ,con.name) else tyre.tyre_tag end)tyre_tag, "
		+ "mts.size as tyre_size, mtm.name as tyre_manufacturer, md.depot_name,mbd.bus_reg_number "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre tyre "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = tyre.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history asso on asso.tyre_id = tyre.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = asso.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition con on con.tyre_condition_id = tyre.tyre_condition_id and con.name !='New' and con.name !='Condemn' "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position pos on pos.tyre_position_id = asso.tyre_position_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = tyre.tyre_size "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = tyre.tyre_make "
		+ "where tyre.depot_id=?1 and to_date(to_char(tyre.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
List<Object[]> resoleTyreIssuedReportReport(Integer depotId, Date startDate, Date endDate);

	/*@Query(value = "select md.depot_name,mt.tyre_number,mt.expected_life,btah.kms_done as mileage_at_taken_off,btah.removal_date as taken_off_date"
			+ " from " + RestConstants.SCHEMA_NAME + "." + "mst_depot md  inner join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre mt on md.depot_id = mt.depot_id" + " inner join " + RestConstants.SCHEMA_NAME + "."
			+ "bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id"
			+ " where mt.tyre_condition_id =1 and mt.created_on >=?1 and mt.created_on <=?2", nativeQuery = true)
	List<Object[]> mileageAchievedReport(Date startDate, Date endDate);*/
	/*@Query(value="select md.depot_name , mt.tyre_number , mt.expected_life , btah.kms_done as mileage_at_taken_off, btah.removal_date as taken_off_date "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on md.depot_id = mt.depot_id "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
			+"where mt.tyre_condition_id =1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
	List<Object[]> mileageAchievedReport(Date startDate, Date endDate);*/
	
@Query(value="select md.depot_name, mt.tyre_number, mt.expected_life, btah.kms_done as mileage_at_taken_off,"
+"to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as taken_off_date "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
+"where mt.tyre_condition_id =1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> mileageAchievedReport(Date startDate, Date endDate);

	/*@Query(value="select md.depot_name , mt.tyre_number, mtm.name , mt.expected_life , sum(btah.kms_done) as mileage_at_taken_off,to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as taken_off_date "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
					+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on md.depot_id = mt.depot_id "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
					+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mt.tyre_make = mtm.tyre_maker_id "
			+"where md.depot_id =?1 and mt.tyre_condition_id in (1, 2,3,4,5) and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 group by md.depot_name , mt.tyre_number, mt.expected_life, mt.tyre_type, btah.removal_date, mtm.name, mt.tyre_condition_id ",nativeQuery=true)
	List<Object[]> condemnTyreReport(Integer depotId,Date startDate,Date endDate);*/



@Query(value="select md.depot_name, mt.tyre_number, mtm.name as make, mt.expected_life, sum(btah.kms_done) as taken_off_mileage, sum(mbr.hill_kms) as hill, "
		+ "sum(mbr.plain_kms) as plain from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mt.tyre_make = mtm.tyre_maker_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.depot_id = mt.depot_id "
		+ "where mt.depot_id =?1 and btah.tyre_condition_id=8 "
		+ "and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
		+ "group by md.depot_name, mt.tyre_number, mtm.name, mt.expected_life ",nativeQuery=true)
List<Object[]> condemnTyreReport(Integer depotId,Date startDate,Date endDate);

	@Query(value = "select md.depot_name , mt.tyre_number, mtm.name , mt.expected_life , sum(btah.kms_done) as mileage_at_taken_off,btah.removal_date as taken_off_date "
			+ "from " + RestConstants.SCHEMA_NAME + "." + "mst_depot md inner join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre mt on md.depot_id = mt.depot_id" + "inner join " + RestConstants.SCHEMA_NAME + "."
			+ "bus_tyre_association_history btah  on mt.tyre_id = btah.tyre_id" + "inner join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mt.tyre_make = mtm.tyre_maker_id"
			+ "where md.depot_id =?1 and mt.tyre_condition_id in (1, 2,3,4,5) and mt.created_on >=?2 and mt.created_on <=?3"
			+ "group by md.depot_name , mt.tyre_number, mt.expected_life, mt.tyre_type, btah.removal_date, mtm.name, mt.tyre_condition_id", nativeQuery = true)
	List<Object[]> condemnTyreReportReport(Integer depotId, Date startDate, Date endDate);

	/*
	 * @Query(
	 * value="select md.depot_name , mt.tyre_number , mt.expected_life , mt.tyre_condition_id"
	 * +" from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md"
	 * +" inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_tyre mt on md.depot_id = mt.depot_id"
	 * +" where md.depot_id =?1 and mt.tyre_condition_id in (1, 2,3,4) and md.created_on >=?2 and md.created_on <=?3"
	 * +" GROUP BY md.depot_name , mt.tyre_number, mt.expected_life, mt.tyre_condition_id"
	 * , nativeQuery=true) List<Object[]> expectedLifeReport(Integer
	 * depotId,Date startDate, Date endDate);
	 */
	/*@Query(value = "select md.depot_name , mt.tyre_number , mt.expected_life , mt.tyre_condition_id "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on md.depot_id = mt.depot_id "
			+"where md.depot_id =?1 and mt.tyre_condition_id in (1, 2,3,4) and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 group by md.depot_name , mt.tyre_number, mt.expected_life, mt.tyre_condition_id", nativeQuery = true)
	List<Object[]> expectedLifeReport(Integer depotId, Date startDate, Date endDate);*/
	
	@Query(value="select md.depot_name, (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc2.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, "
			+ "(select (cast(mt.expected_life as float)) from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id= bta.tyre_condition_id "
			+ "where mtc.name= 'New' and mt.tyre_id = bta.tyre_id) New_Tyre_Expected_Life, "
			+ "(select dta.expected_life from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id= dta.new_conditon_id "
			+ "where mtc.name= 'R1' and mt.tyre_id = dta.tyre_id) R1_Tyre_Expected_Life, "
			+ "(select dta.expected_life from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id= dta.new_conditon_id "
			+ "where mtc.name= 'R2' and mt.tyre_id = dta.tyre_id ) R2_Tyre_Expected_Life, "
			+ "(select dta.expected_life from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id= dta.new_conditon_id "
			+ "where mtc.name= 'R3' and mt.tyre_id = dta.tyre_id ) R3_Tyre_Expected_Life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = mt.tyre_condition_id "
			+ "where md.depot_id =?1 "
			+ "and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
List<Object[]> expectedLifeReport(Integer depotId, Date startDate, Date endDate);

	/*@Query(value = "select mtc.name as New_Resole_Tyre , sum(mt.tyre_cost) as Value_in_Rs " + " from "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id"
			+ " where mt.tyre_condition_id =?1 and mt.created_on >=?2 and mt.created_on <=?3    group by mtc.name", nativeQuery = true)
	List<Object[]> tyreReportInStock(Integer tyreConditionId, Date startDate, Date endDate);*/
	
	/*@Query(value="select mtc.name as New_Resole_Tyre , sum(mt.tyre_cost) as Value_in_Rs "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
			+"where mt.tyre_condition_id in (1,5,6,7) and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 group by mtc.name",nativeQuery=true)
	List<Object[]> tyreReportInStock(Date startDate, Date endDate);
	*/


@Query(value="select mtc.name as New_Resole_Tyre, count(mt.tyre_id), sum(mt.tyre_cost) as Value_in_Rs "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
		+ "where mt.tyre_condition_id in (1,5,6,7) and mt.bus_fitted = 'True' "
		+ "and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "group by mtc.name",nativeQuery=true)
List<Object[]> tyreReportInStock(Date startDate, Date endDate);

	/*
	 * @Query(
	 * value="select md.depot_name , mt.tyre_number, mt.expected_life , sum(btah.kms_done) as mileage_at_taken_off, (float8 (mt.expected_life) - btah.kms_done ) as difference "
	 * + "from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md " +
	 * "inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_tyre mt on md.depot_id = mt.depot_id " +
	 * "inner join "+RestConstants.SCHEMA_NAME+
	 * "."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id  " +
	 * "where md.depot_id =?3  and mt.tyre_condition_id in (1, 2,3,4,5)  and md.created_on between ?1 and ?2 "
	 * +
	 * "group by md.depot_name , mt.tyre_number, mt.expected_life, mt.tyre_type, btah.removal_date, btah.kms_done , mt.tyre_condition_id"
	 * ,nativeQuery=true) List<Object[]> tyreTakenOffBeforeExpectedLife(Integer
	 * depotId,Date fromDate, Date toDate);
	 */

	/*@Query(value = "select md.depot_name , mt.tyre_number, mt.expected_life , sum(btah.kms_done) as mileage_at_taken_off,(float8 (mt.expected_life) - btah.kms_done ) as difference "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on md.depot_id = mt.depot_id "
			+"inner join " +RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
					+"where md.depot_id =?1 and mt.tyre_condition_id in (1, 2,3,4,5)  and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 group by md.depot_name , mt.tyre_number, mt.expected_life, mt.tyre_type, btah.removal_date, btah.kms_done , mt.tyre_condition_id ", nativeQuery = true)
	List<Object[]> tyreTakenOffBeforeExpectedLife(Integer depotId, Date fromDate, Date toDate);
*/
	
	
	
	@Query(value="select md.depot_name ,(case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, "
			+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce ((cast( mbr1.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce((cast(mbr2.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce ((cast(mbr3.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce ((cast(mbr4.expected_life as float)),0) end) expected_life, "
			+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0)+ coalesce (mbr3.kms2,0)+ coalesce (mbr4.kms2,0)) total_kms, "
			+ "ABS((case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce ((cast( mbr1.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce((cast(mbr2.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce ((cast(mbr3.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce ((cast(mbr4.expected_life as float)),0) end) "
			+ "-(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0)+ coalesce (mbr3.kms2,0)+ coalesce (mbr4.kms2,0))) as difference "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
			+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms, mbr.bus_id, btah.tyre_id, btah.tyre_condition_id, "
			+ "(cast(mt3.expected_life as float))expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt3 "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt3.tyre_id = btah.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
			+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?2 end and "
			+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?3 end and mtc2.name='New' "
			+ "group by btah.tyre_id, btah.tyre_condition_id, mbr.bus_id, mt3.expected_life )mbr1 on mbr1.tyre_id = mt.tyre_id "
			+ "and coalesce (mbr1.kms,0)< cast( mt.expected_life as float) "
			+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
			+ "dta.expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
			+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?2 end and "
			+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?3 end and mtc2.name='R1' "
			+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life "
			+ "having coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)< dta.expected_life) mbr2 on mbr2.tyre_id = mt.tyre_id "
			+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
			+ "dta.expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
			+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?2 end and "
			+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?3 end and mtc2.name='R2' "
			+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life "
			+ "having coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)< dta.expected_life) mbr3 on mbr3.tyre_id = mt.tyre_id "
			+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
			+ "dta.expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
			+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?2 end and "
			+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?3 end and mtc2.name='R3' "
			+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life "
			+ "having coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)< dta.expected_life) mbr4 on mbr4.tyre_id = mt.tyre_id "
			+ "where md.depot_id =?1 and (coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0))> 0",nativeQuery=true)
List<Object[]> tyreTakenOffBeforeExpectedLife(Integer depotId, Date fromDate, Date toDate);
	
	/*
	 * @Query(
	 * value="select md.depot_name, mt.tyre_number, mtp.name as Tyre_position , mtr.reason_name as Reason "
	 * + "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt " +
	 * "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_depot md on mt.depot_id = md.depot_id  " +
	 * "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_tyre_position mtp on mt.tyre_position_id = mtp.tyre_position_id  "
	 * + "join "+RestConstants.SCHEMA_NAME+
	 * "."+"mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id  "
	 * +
	 * "where mt.depot_id = ?3 and mtp.tyre_position_id  in (3,4,5,6,10,11)  and mt.tyre_condition_id = 1 and mt.created_on between ?1 and ?2"
	 * ,nativeQuery=true) List<Object[]> newTyreIssuedForRearReport(Date
	 * startDate, Date endDate, Integer depotId);
	 */

	/*@Query(value = "SELECT md.depot_name, mt.tyre_number, mtp.name as Tyre_position, to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date_of_Installation, mtr.reason_name as Reason "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id = bta.tyre_id "
			+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on bta.tyre_position_id = mtp.tyre_position_id "
			+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
			+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id "
			+"where mt.depot_id = ?1 and mtp.tyre_position_id  in (3,4,5,6)  and mt.tyre_condition_id = 1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ", nativeQuery = true)
List<Object[]> newTyreIssuedForRearReport(Integer depotId, Date startDate, Date endDate);*/





@Query(value="SELECT md.depot_name, mt.tyre_number, mtp.name as Tyre_position, to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date_of_Installation,"
+"mtr.reason_name as Reason,mt.tyre_tag "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id = bta.tyre_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on bta.tyre_position_id = mtp.tyre_position_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id "
+"where mt.depot_id = ?1 and mtp.tyre_position_id in (3,4,5,6) and mt.tyre_condition_id = 1 "
+"and to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)
List<Object[]> newTyreIssuedForRearReport(Integer depotId, Date startDate, Date endDate);


	/*@Query(value = "select mt.tyre_number, mtc.name as Tyre_Status , mt.created_on, mtr.reason_name as Reason "
			+ "from  " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + "join  " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id " + "inner join  "
			+ RestConstants.SCHEMA_NAME + "." + "mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id "
			+ "where mt.depot_id =?3 and mt.tyre_condition_id=5 and mt.created_on between ?1 and ?2", nativeQuery = true)
	List<Object[]> tyreCondemnReport(Date startDate, Date endDate, Integer depotId);
*/


/*@Query(value="select mt.tyre_number, mtc.name as Tyre_Status, to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date, mtr.reason_name as Reason "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+"join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id = bta.tyre_id "
				+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id and mtc.name='Condemn' "
				+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id "
				+"where mt.depot_id =?1 and mt.tyre_condition_id=8 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)
List<Object[]> tyreCondemnReport(Integer depotId, Date startDate, Date endDate);*/

@Query(value="select mt.tyre_number, mtc.name as Tyre_Status, to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date, mtr.reason_name as Reason,mt.tyre_tag "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id and mtr.reason_name = 'Condemn' "
		+ "where mt.depot_id =?1 and btah.tyre_condition_id=8 "
		+ "and to_date(to_char(btah.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
		+ "group by mt.tyre_number, mtc.name, to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD'), mtr.reason_name,mt.tyre_tag",nativeQuery=true)
List<Object[]> tyreCondemnReport(Integer depotId, Date startDate, Date endDate);

	/*@Query(value = "select md.depot_name , mt.tyre_number , mt.tyre_condition_id as Resole, mt.expected_life,  mbr.kmpl_as_pervtskms as GPS_Mileage "
			+ "from " + RestConstants.SCHEMA_NAME + "." + "mst_depot md " + "inner join " + RestConstants.SCHEMA_NAME
			+ "." + "mst_tyre mt on md.depot_id = mt.depot_id " + "inner join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_bus_refueling mbr  on md.depot_id = mbr.depot_id "
			+ "where md.depot_id =?1 and mt.tyre_condition_id in (2,3,4) and md.created_on between ?2 and ?3 "
			+ "group by md.depot_name , mt.tyre_number, mt.expected_life, mt.tyre_condition_id, mbr.kmpl_as_pervtskms", nativeQuery = true)
	List<Object[]> resoleTyreMileageReport(Integer tyreId, Date startDate, Date endDate);*/
@Query(value="select md.depot_name ,(case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, "
		+ "(case when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce((cast(mbr2.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce ((cast(mbr3.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce ((cast(mbr4.expected_life as float)),0) end) expected_life, "
		+ "(coalesce (mbr2.kms2,0)+ coalesce (mbr3.kms2,0)+ coalesce (mbr4.kms2,0)) total_kms "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "dta.expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R1' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life ) mbr2 on mbr2.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "dta.expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R2' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life) mbr3 on mbr3.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "dta.expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R3' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life) mbr4 on mbr4.tyre_id = mt.tyre_id "
		+ "where md.depot_id =?1 ",nativeQuery=true)	
List<Object[]> resoleTyreMileageReport(Integer depotId, Date startDate, Date endDate);
	

	/*@Query(value = "select md.depot_name , mt.tyre_number , btah.removal_date as taken_off_date " + "from "
			+ RestConstants.SCHEMA_NAME + "." + "mst_depot md " + "inner join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre mt on md.depot_id = mt.depot_id " + "inner join " + RestConstants.SCHEMA_NAME + "."
			+ "bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
			+ "where md.depot_id =?1 and mt.tyre_condition_id in (2,3,4,5) and md.created_on between ?2 and ?3", nativeQuery = true)
	List<Object[]> tyreSentForRetreadingReport(Integer depotId, Date startDate, Date endDate);*/
	
	/*@Query(value="select md.depot_name,mt.tyre_number,to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as taken_off_date "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on md.depot_id = mt.depot_id "
			+"inner join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
			+"where md.depot_id =?1 and mt.tyre_condition_id in (2,3,4,5)  and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)
	List<Object[]> tyreSentForRetreadingReport(Integer depotId, Date startDate, Date endDate);*/
	
 @Query(value="select md.depot_name, mt.tyre_number, max(to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) as taken_off_date, "
 		+ "to_date(to_char(rdd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') sent_date, (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag "
 		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
 		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
 		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on mt.tyre_id = btah.tyre_id "
 		+ "left join "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta on dta.tyre_id = mt.tyre_id "
 		+ "join "+RestConstants.SCHEMA_NAME+"."+"retreading_docket_details rdd on dta.docket_id = rdd.docket_id "
 		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
 		+ "where md.depot_id =?1 and mtc.name in ('New', 'R1', 'R2') "
 		+ "and to_date(to_char(dta.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
 		+ "group by md.depot_name, mt.tyre_number, to_date(to_char(rdd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD'), "
 		+ "(case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)",nativeQuery=true) 
List<Object[]> tyreSentForRetreadingReport(Integer depotId, Date startDate, Date endDate);

	/*@Query(value = "select md.depot_name , sum(mbt.tyre_count) as total_tyre, mt.tyre_condition_id " + "from "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + "inner join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_depot md on mt.depot_id = md.depot_id " + "inner join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_bus_type mbt on mt.bus_type = mbt.bus_type_id "
			+ "where mt.depot_id =?1 and mt.tyre_condition_id in (2,3,4) and mt.created_on between ?2 and ?3 "
			+ "group by md.depot_name , mbt.tyre_count, mt.tyre_condition_id", nativeQuery = true)
	List<Object[]> countOfRetreadingTyresReport(Integer depotId, Date fromDate, Date toDate);*/



@Query(value="select md.depot_name , count(distinct mt.tyre_id) as total_tyre, "
		+ "sum(case when btah.tyre_condition_id=5 then 1 else 0 end) as resoleOne, "
		+ "sum(case when btah.tyre_condition_id =6 then 1 else 0 end) as resoleTwo, "
		+ "sum(case when btah.tyre_condition_id =7 then 1 else 0 end) as resoleThree from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id "
		+ "where md.depot_id =?1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
		+ "GROUP BY md.depot_name",nativeQuery=true)
List<Object[]> countOfRetreadingTyresReport(Integer depotId, Date fromDate, Date toDate);

	/*@Query(value = "select md.depot_name, mbt.tyre_count, mt.tyre_number, mtm.name as make, mtc.created_on as condemn_date "
			+ "from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + "join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_depot md on mt.depot_id = md.depot_id " + "join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_bus_type mbt on mt.bus_type = mbt.bus_type_id " + "join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_maker mtm on mt.tyre_make = mtm.tyre_maker_id " + "join " + RestConstants.SCHEMA_NAME + "."
			+ "mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
			+ "where mt.depot_id =?1 and mt.tyre_condition_id =5 and mt.created_on between ?2 and ?3", nativeQuery = true)
	List<Object[]> tyresLyingForAuctionReport(Integer depotId, Date fromDate, Date toDate);
*/
	
	/*@Query(value="select md.depot_name,mbt.tyre_count,mt.tyre_number, mtm.name as make, to_date(to_char(mtc.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as condemn_date "
			+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mt.bus_type = mbt.bus_type_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mt.tyre_make = mtm.tyre_maker_id "
			+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
			+"where mt.depot_id =?1 and mt.tyre_condition_id =1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)
	List<Object[]> tyresLyingForAuctionReport(Integer depotId, Date fromDate, Date toDate);
	*/


@Query(value="select distinct md.depot_name, mt.tyre_number, mtm.name as make, to_date(to_char(btah.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as condemn_date , mt.tyre_id "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mt.tyre_make = mtm.tyre_maker_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id and mtc.name ='Condemn' "
		+ "where mt.depot_id =?1 and not exists (select 1 from "+RestConstants.SCHEMA_NAME+"."+"auc_docket_tyre_association adta where adta.tyre_id = mt.tyre_id ) "
		+ "and to_date(to_char(btah.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
List<Object[]> tyresLyingForAuctionReport(Integer depotId, Date fromDate, Date toDate);

	/*
	@Query(value="select mt.tyre_id, mt2.transport_name,mtt.name as ttype, mts.size, mt.tyre_number, mtm.name as makername, mt.expected_life,mt.tyre_cost from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mt.transport_unit left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type where mt.depot_id=?1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 ",nativeQuery=true)
	List<Object[]>depotWiseTyreSummaryReport(Integer depotId, String fromDate, String toDate);*/
	
	@Query(value="select md.depot_name, mt.tyre_number, ''Due_date_for_inspection, mt.expected_life, ''status "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
			+ "where mt.depot_id=?1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') >= ?2",nativeQuery=true)
	List<Object[]>tyreDueForInspection(Integer depotId, Date date);
	
	
	
	@Query(value="select mt2.transport_name, md.depot_name, mbd.bus_reg_number, (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag,mt.tyre_number, "
			+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce ((cast( mbr1.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce((cast(mbr2.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce ((cast( mbr3.expected_life as float)),0) "
			+ "when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce ((cast( mbr4.expected_life as float)),0) end) expected_life, "
			+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0)+ coalesce (mbr3.kms2,0)+ coalesce (mbr4.kms2,0)) total_kms, "
			+ "(case when bta2.association_id is null then 'taken-off' else 'Running' end) Status "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mt.transport_unit "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta2 on bta2.tyre_id = mt.tyre_id "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = bta2.bus_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms, mbr.bus_id, bta.tyre_id, bta.tyre_condition_id, "
			+ "(cast( mt3.expected_life as float))expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt3 "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt3.tyre_id = bta.tyre_id "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = bta.bus_id "
			+ "group by bta.tyre_id, bta.tyre_condition_id, mbr.bus_id, mt3.expected_life )mbr1 on mbr1.tyre_id = mt.tyre_id and mbr1.kms> cast( mt.expected_life as float)left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
			+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?2 end "
			+ "and "
			+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < '2022-05-01' then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else '2022-05-01' end "
			+ "and mtc2.name='R1' "
			+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life "
			+ "having coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)> dta.expected_life ) mbr2 on mbr2.tyre_id = mt.tyre_id left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
			+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?2 end "
			+ "and "
			+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < '2022-05-01' then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else '2022-05-01' end "
			+ "and mtc2.name = 'R2' "
			+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life "
			+ "having coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)> dta.expected_life) mbr3 on mbr3.tyre_id = mt.tyre_id left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
			+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
			+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else ?2 end "
			+ "and "
			+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < '2022-05-01' then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ "else '2022-05-01' end "
			+ "and mtc2.name = 'R3' "
			+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life "
			+ "having coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
			+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)> dta.expected_life ) mbr4 on mbr4.tyre_id = mt.tyre_id "
			+ "where mtc.name in('New','R1','R2','R3') and (coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0)+ coalesce (mbr3.kms2,0)+ coalesce (mbr4.kms2,0))> 0 ",nativeQuery=true)
	List<Object[]> completionOfExpectedTyreReport(Integer depotId, Date startDate, Date endDate);
	
	
	
	@Query(value="select md.depot_name, mt.tyre_number, to_date(to_char(rdd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as sent_date, dta.remarks, dta.received_date , "
			+ "(case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag , rdd.docket_number "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on dta.tyre_id = mt.tyre_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"retreading_docket_details rdd on dta.docket_id = rdd.docket_id "
			+ "where mt.depot_id =?1 and dta.is_received = 'False' "
			+ "and to_date(to_char(dta.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
List<Object[]> tyreNotSentForRetreadingReport(Integer depotId, Date startDate, Date endDate);

    @Modifying
    @Query(value="update "+RestConstants.SCHEMA_NAME+"."+"mst_tyre set from_date=?1 , to_date =?2 , old_mileage =?3 where tyre_id =?4",nativeQuery=true)
	Integer update(String fromDate, String toDate, Float oldMileage, Integer id);
	
	
	@Query(value="select t.tyre_id,sum(coalesce (t.cm1,0)) from ("
+"select mt.tyre_id,coalesce (sum(mbr.hill_kms*5 +mbr.plain_kms+mbr.dead_kms+mbr.total_actual_kms),0) as cm1 from " 
+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta "  
+"on mt.tyre_id = bta.tyre_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling  mbr "
+"on bta.bus_id = mbr.bus_id "
+"where mt.depot_id =?1  group by mt.tyre_id "
+"union "
+"select mt.tyre_id,coalesce (sum(mbr.hill_kms*5 +mbr.plain_kms+mbr.dead_kms+mbr.total_actual_kms),0) as cm1 from " 
+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah " 
+"on mt.tyre_id = btah.tyre_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling  mbr "

+"on btah.bus_id = mbr.bus_id "
+"where mt.depot_id =?1  group by mt.tyre_id )t group by t.tyre_id order by 1",nativeQuery=true)
List<Object[]> getTyreCurrentMileageDtls(Integer id);

@Query(value="select mt.tyre_number from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
+"where mt.depot_id =?1 and mtc.tyre_condition_id =?2 ",nativeQuery=true)
List<Object[]> getTyreNumber(Integer dpCode, Integer tyreConditionId);

@Query(value="select distinct mt.tyre_id,mbr.hill_kms,mbr.plain_kms from " 
+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt.tyre_id = bta.tyre_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling  mbr "
+"on bta.bus_id = mbr.bus_id where mt.depot_id =?1  order by 1",nativeQuery=true)
List<Object[]> getPlainAndHillKms(Integer id);


@Query(value="select md.depot_name, (case when bta.association_id is null then 'taken-off' else 'Running' end) Status, mtc.name as tyre_condition,count( mt.tyre_id) "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on bta.tyre_id = mt.tyre_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ "where (mt.depot_id=?1 or 0=?1) and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
		+ "group by md.depot_name, mtc.name, (case when bta.association_id is null then 'taken-off' else 'Running' end)",nativeQuery=true)
List<Object[]> depotWiseTyreSummaryReport(Integer id , Date fromDate , Date endDate); 

	

@Query(value="select max(mt.tyre_id) from " 
+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt ",nativeQuery=true)
Integer getMaxTyreId();	

@Query(value="select mt.tyre_number, mtc.name as Tyre_Status, to_date(to_char(rdd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date, mtm.name, mts.size, mtr.reason_name as Reason,mt.tyre_tag "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta on dta.tyre_id = mt.tyre_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"retreading_docket_details rdd on rdd.docket_id = dta.docket_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
		+ "inner join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id and mtr.reason_name in ('Resole','ForResole') "
		+ "where mt.depot_id =?1 and mt.tyre_condition_id in (1,5,6) "
		+ "and to_date(to_char(rdd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3",nativeQuery=true)
List<Object[]> depotWiseTakenOffReport(Integer id , Date fromDate , Date endDate); 

@Query(value="select mtc.name as New_Resole_Tyre, ''from_date, ''to_date, count(mt.tyre_id), sum(mt.tyre_cost) as Value_in_Rs "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id "
		+ "where to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "group by mtc.name order by mtc.name",nativeQuery=true)
List<Object[]> newTyreAndResoleTyreQuantityReport(Date fromDate , Date endDate);  

@Query(value="select mt.tyre_number, mts.size, addt.bid_amount  "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"auc_docket_tyre_association adta on adta.tyre_id = mt.tyre_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"auctioned_docket_details addt on addt.docket_id = adta.docket_id  "
		+ "and to_date(to_char(addt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
List<Object[]> tyreAuctionReport(Date fromDate , Date endDate); 


@Query(value = " select mt.tyre_id,mt2.transport_name,mts.size, mtm.name as makername, mtt.name as ttype,  mt.tyre_number,mt.invoice_number,mt.invoice_date,mt.tyre_cost,mt.tyre_purchase_date ,mt.expected_life,mt.tyre_tag ,mt.source_of_origin_tyre ,mt.kms_run_till_date,mt.old_tyre,mtr.reason_name ,mtp.name as tyre_position "
		+ " from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " left join " + RestConstants.SCHEMA_NAME
		+ "." + "mst_transport mt2 on mt2.transport_id = mt.transport_unit " + " left join "
		+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make" + " left join "
		+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size" + " left join "
		+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id"
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_takenoff_reason mtr on mtr.taken_off_reason_id = btah.reason_id  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_position mtp on mtp.tyre_position_id =btah.tyre_position_id  "
		+ " where mt.old_tyre =true and mt.depot_id=?1 order by mt.tyre_id DESC", nativeQuery = true)
List<Object[]> findAllOldTyreByDepot(Integer integer); 


@Query(value="select mtm.name as make, mtt.name as tyre_type, mts.size, mt.tyre_number, max(to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')) taken_off_date, "
		+ "mtr.reason_name from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt  "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id and btah.tyre_condition_id =8 "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mt.tyre_make = mtm.tyre_maker_id "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type  "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size  "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id  "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id "
		+ "where mt.depot_id =?1 and not exists (select 1 from " + RestConstants.SCHEMA_NAME + "." + "auc_docket_tyre_association adta where adta.tyre_id = mt.tyre_id and mt.tyre_condition_id=8) "
		+ "and to_date(to_char(btah.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
		+ "group by mtm.name, mtt.name, mts.size, mt.tyre_number, mtr.reason_name ",nativeQuery=true)
List<Object[]> tyreNotAuctionReport(Integer id , Date fromDate , Date endDate); 

@Query(value="select (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, mtc.name as tyre_condition, "
		+ "''from_date, ''to_date, "
		+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0)+ coalesce (mbr3.kms2,0)) total_kms "
		+ "from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms, mbr.bus_id, bta.tyre_id, bta.tyre_condition_id "
		+ "from " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association bta "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_refueling mbr on mbr.bus_id = bta.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?1 then to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?1 end and case when now() < ?2 then now() else ?2 end "
		+ "group by bta.tyre_id, bta.tyre_condition_id, mbr.bus_id)mbr1 on mbr1.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id "
		+ "from " + RestConstants.SCHEMA_NAME + "." + "docket_tyre_association dta "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?1 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?1 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?2 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and mtc2.name='R1' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id) mbr2 on mbr2.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id "
		+ "from " + RestConstants.SCHEMA_NAME + "." + "docket_tyre_association dta "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?1 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?1 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?2 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and mtc2.name='R2' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id) mbr3 on mbr3.tyre_id = mt.tyre_id "
		+ "where mtc.name in ('New','R1','R2' ) "
		+ "order by mt.tyre_tag desc",nativeQuery=true)
List<Object[]> newTyreAndResoleTyreMileageReport(Date fromDate , Date endDate); 

@Query(value="select depot_name,size, tyre_manufacture, tyre_type, avg_kms, cost_per_km from( "
		+ "select md.depot_name,mts.size, mtm.name as tyre_manufacture, mtt.name as tyre_type, "
		+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0))/4 avg_kms , "
		+ "(coalesce (mbr1.kms,0)*coalesce((case when coalesce (cast( mt.expected_life as float),0)=0 then 0 else mt.tyre_cost / coalesce (cast( mt.expected_life as float),0) end),0)+ "
		+ "coalesce (mbr2.kms2,0)*coalesce (mbr2.cst,0)+ coalesce (mbr3.kms2,0)*coalesce (mbr3.cst,0)+ coalesce (mbr4.kms2,0)*coalesce (mbr4.cst,0))cost_per_km, "
		+ "DENSE_RANK() OVER ( PARTITION BY mts.size ORDER BY (coalesce (mbr1.kms,0)*coalesce((case when coalesce (cast( mt.expected_life as float),0)=0 then 0 "
		+ "else mt.tyre_cost / coalesce (cast( mt.expected_life as float),0) end),0)+ coalesce (mbr2.kms2,0)*coalesce (mbr2.cst,0)+ coalesce (mbr3.kms2,0)*coalesce (mbr3.cst,0)+ "
		+ "coalesce (mbr4.kms2,0)*coalesce (mbr4.cst,0)) desc) as row_num "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)/count(mt3.tyre_id) as kms, mbr.bus_id, bta.tyre_id, bta.tyre_condition_id "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = bta.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt3 on mt3.tyre_id = bta.tyre_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?3 then to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and case when now() < ?4 then now() else ?4 end "
		+ "group by bta.tyre_id, bta.tyre_condition_id, mbr.bus_id ) mbr1 on mbr1.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)/count(dta.tyre_id) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?3 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?4 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?4 end and mtc2.name='R1' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.tyre_cost, dta.expected_life) mbr2 on mbr2.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)/count(dta.tyre_id) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?3 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?4 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?4 end and mtc2.name='R2' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.tyre_cost, dta.expected_life) mbr3 on mbr3.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)/count(dta.tyre_id) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?3 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?4 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?4 end and mtc2.name='R3' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.tyre_cost, dta.expected_life) mbr4 on mbr4.tyre_id = mt.tyre_id "
		+ "where mt.depot_id =?1 and mtm.tyre_maker_id =?5 and (mts.tyre_size_id =?2 or 0=?2) )r "
		+ "where row_num = 1",nativeQuery=true)
List<Object[]> makeWiseComparisonReport(Integer depotId,Integer tyreSizeId,Date startDate, Date endDate,Integer makeId); 

@Query(value="select (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, mtp.name as tyre_position,mtc.name as tyre_condition, "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce ((cast( mbr1.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce((cast(mbr2.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce ((cast( mbr3.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce ((cast( mbr4.expected_life as float)),0) end) expected_life, "
		+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0)+ coalesce (mbr3.kms2,0)+ coalesce (mbr4.kms2,0)) total_kms, mbd.bus_reg_number, ''achieved_km_date "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id = mt.tyre_position_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta2 on bta2.tyre_id = mt.tyre_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = bta2.bus_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms, mbr.bus_id, bta.tyre_id, bta.tyre_condition_id, "
		+ "(cast( mt3.expected_life as float))expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt3 "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt3.tyre_id = bta.tyre_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = bta.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?1 then to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?1 end and case when now()< ?2 then now() else ?2 end "
		+ "group by bta.tyre_id, bta.tyre_condition_id, mbr.bus_id, mt3.expected_life )mbr1 on mbr1.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?1 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?1 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?2 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and mtc2.name='R1' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life) mbr2 on mbr2.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?1 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?1 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?2 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and mtc2.name='R2' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life) mbr3 on mbr3.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?1 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?1 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?2 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and mtc2.name='R3' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life) mbr4 on mbr4.tyre_id = mt.tyre_id "
		+ "where mtc.name in ('New','R1','R2','R3')",nativeQuery=true)
List<Object[]> tyreCurrentMileageReport(Date fromDate , Date endDate); 


@Query(value = " select mt.tyre_id, mt.tyre_number,mt.tyre_tag,sum(coalesce (btah.total_mileage_taken_off,0)),mtc.name from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id  "
		+ " join " + RestConstants.SCHEMA_NAME + "." + " mst_tyre_condition mtc  on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ " where mt.tyre_size = ?1 and mt.tyre_make = ?2 and mt.tyre_type= ?3 and mt.available=true and mtc.name !='Condemn'"
		+ " and not exists(select 1 from " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association bta where bta.tyre_id = mt.tyre_id) group by mt.tyre_id, mt.tyre_number,mtc.name order by mt.tyre_id DESC", nativeQuery = true)
List<Object[]> getTyreListBySizeMakeAndType(Integer sizeId, Integer makeId, Integer typeId);

@Query(value = " select mt.tyre_id,mt2.transport_name,mts.size, mtm.name as makername, mtt.name as ttype,  mt.tyre_number,mt.invoice_number,mt.invoice_date,mt.tyre_cost,mt.tyre_purchase_date ,mt.expected_life,mt.tyre_tag ,md.depot_name as source_of_origin_tyre ,mt.kms_run_till_date, "
		+ "mbd.bus_reg_number,mtc2.name, btah.removal_date as taken_off_date,btah.install_date,btah.kms_taken_off,btah.kms_installed,btah.total_mileage_taken_off,mtr.reason_name,mt.old_tyre ,mtp.name as tyre_position"
		+ " from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt " + " left join " + RestConstants.SCHEMA_NAME
		+ "." + "mst_transport mt2 on mt2.transport_id = mt.transport_unit " + " left join "
		+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make" + " left join "
		+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size" + " left join "
		+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id"
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "bus_tyre_association_history btah on btah.tyre_id = mt.tyre_id  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_takenoff_reason mtr on mtr.taken_off_reason_id = btah.reason_id  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_bus_details mbd on btah.bus_id = mbd.bus_id  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_condition mtc2 on btah.tyre_condition_id =mtc2.tyre_condition_id  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_position mtp on mtp.tyre_position_id =btah.tyre_position_id  "
		+ " left join " + RestConstants.SCHEMA_NAME + "." + "mst_depot md on mt.depot_id =md.depot_id "
		+ " where mt.old_tyre =true and mt.tyre_id =?1 order by mt.tyre_id DESC", nativeQuery = true)
List<Object[]> getOldTyreByTyreId(Integer integer);

@Modifying
@Query(value="update "+RestConstants.SCHEMA_NAME+"."+"mst_tyre set tyre_position_id=?1 where tyre_id =?2",nativeQuery=true)
int updateTyrePosition(Integer positionId,Integer tyreId);

@Query(value="select mt.tyre_id,mt.tyre_number,mt.tyre_tag,mt.available,mt.tyre_condition_id,mt.expected_life from " + RestConstants.SCHEMA_NAME + "." + "mst_tyre mt join " + RestConstants.SCHEMA_NAME + "." + "mst_takenoff_reason mtr on mt.reason_id = mtr.taken_off_reason_id where mt.available =false and mtr.reason_code ='RP' and mt.depot_id =?1 order by mt.updated_on desc",nativeQuery=true)
List<Object[]> findAllRepairTyres(Integer depotId);

@Query(value="select count(mt.tyre_id) from " 
+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt ",nativeQuery=true)
Integer getCounterTyreId();	

@Query(value="select md.depot_name, mt.tyre_number, mtp.name as Tyre_Position ,case when dta2.dta_id is null then 'No' else 'Yes' end Taken_off_for_Retreading "
		+ ",case when btah2.association_history_id is null then 'No' else 'Yes' end Condemnation "
		+ ",case when bta2.association_id is null then 'No' else 'Yes' end Refitting  "
		+ ",case when bta2.association_id is null then 'Yes' else 'No' end At_plant "
		+ ",add2.auction_date from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id = mt.tyre_position_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta2 on bta2.tyre_id = mt.tyre_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah2 on btah2.tyre_id = mt.tyre_id and btah2.tyre_condition_id=5 and btah2.reason_id=3  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta2 on dta2.tyre_id= mt.tyre_id and dta2.is_received = false "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"auc_docket_tyre_association adta on adta.tyre_id = mt.tyre_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"auctioned_docket_details add2 on add2.docket_id = adta.docket_id  "
		+ "where (mt.tyre_id =?1 or 0=?1) and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 order by 2",nativeQuery=true)
List<Object[]> currentTyreStatusReport(Integer id , Date fromDate , Date endDate); 

@Query(value="select mt2.transport_name, md.depot_name, (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, "
		+ "mtm.name as tyre_manufacture, mts.size, mt.tyre_cost, "
		+ "(coalesce (mbr1.kms,0)*coalesce((case when coalesce (cast( mt.expected_life as float),0)=0 then 0 else mt.tyre_cost / coalesce (cast( mt.expected_life as float),0) end),0)+ "
		+ "coalesce (mbr2.kms2,0)*coalesce (mbr2.cst,0)+ coalesce (mbr3.kms2,0)*coalesce (mbr3.cst,0)+ coalesce (mbr4.kms2,0)*coalesce (mbr4.cst,0))tyre_recovered_cost, "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then mbr1.install_date when mt.tyre_condition_id = mbr2.tyre_condition_id then mbr2.install_date "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then mbr3.install_date when mt.tyre_condition_id = mbr4.tyre_condition_id then mbr4.install_date end) install_date, "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce (mbr1.kms,0) when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce (mbr2.kms2,0) "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce(mbr3.kms2,0) when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce(mbr4.kms2,0) end) conditinl_kms, "
		+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0)) total_kms "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mt.transport_unit "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms, mbr.bus_id, bta.tyre_id, bta.tyre_condition_id, "
		+ "to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') install_date "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = bta.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between --to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "case when to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and case when now() < ?3 then now() else ?3 end "
		+ "group by bta.tyre_id, bta.tyre_condition_id, mbr.bus_id, to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'))mbr1 on mbr1.tyre_id = mt.tyre_id "
		+ "and coalesce (mbr1.kms,0)> cast( mt.expected_life as float) "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce(dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R1' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), dta.expected_life, dta.tyre_cost "
		+ "having coalesce (sum(mbr.scheduled_kms),0)> dta.expected_life) mbr2 on mbr2.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between  "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R2' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), dta.expected_life, dta.tyre_cost "
		+ "having coalesce (sum(mbr.scheduled_kms),0)> dta.expected_life) mbr3 on mbr3.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between  "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R3' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), dta.expected_life, dta.tyre_cost "
		+ "having coalesce (sum(mbr.scheduled_kms),0)> dta.expected_life) mbr4 on mbr4.tyre_id = mt.tyre_id "
		+ "where md.depot_id =?1 and (coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0))> 0",nativeQuery=true)
List<Object[]> tyreNotTakenOffAfterCompletedMileageReport(Integer id , Date fromDate , Date endDate);

@Query(value="select mt2.transport_name, md.depot_name, (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, "
		+ "mtm.name as tyre_manufacture, mts.size, mt.tyre_cost, "
		+ "(coalesce (mbr1.kms,0)*coalesce((case when coalesce (cast( mt.expected_life as float),0)=0 then 0 else mt.tyre_cost / coalesce (cast( mt.expected_life as float),0) end),0)+ "
		+ "coalesce (mbr2.kms2,0)*coalesce (mbr2.cst,0)+ coalesce (mbr3.kms2,0)*coalesce (mbr3.cst,0)+ coalesce (mbr4.kms2,0)*coalesce (mbr4.cst,0))tyre_recovered_cost, "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then mbr1.install_date when mt.tyre_condition_id = mbr2.tyre_condition_id then mbr2.install_date "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then mbr3.install_date when mt.tyre_condition_id = mbr4.tyre_condition_id then mbr4.install_date end) install_date, "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce (mbr1.kms,0) when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce (mbr2.kms2,0) "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce(mbr3.kms2,0) when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce(mbr4.kms2,0) end) conditinl_kms, "
		+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0)) total_kms "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mt.transport_unit "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms, mbr.bus_id, bta.tyre_id, bta.tyre_condition_id, "
		+ "to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = bta.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when now() < ?3 then now() else ?3 end "
		+ "group by bta.tyre_id, bta.tyre_condition_id, mbr.bus_id, to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'))mbr1 on mbr1.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R1' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), dta.expected_life, dta.tyre_cost) mbr2 "
		+ "on mbr2.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R2' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'),dta.expected_life, dta.tyre_cost) mbr3 "
		+ "on mbr3.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0)as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and "
		+ "case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R3' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), dta.expected_life, dta.tyre_cost) mbr4 "
		+ "on mbr4.tyre_id = mt.tyre_id "
		+ "where md.depot_id =?1 and mtc.name = 'Condemn'",nativeQuery=true)
List<Object[]> condemnMileageReport(Integer id , Date fromDate , Date endDate);

@Query(value="select md.depot_name, mt.tyre_number, mtp.name as Tyre_Position ,case when dta2.dta_id is null then 'No' else 'Yes' end Taken_off_for_Retreading "
		+ ",case when btah2.association_history_id is null then 'No' else 'Yes' end Condemnation "
		+ ",case when bta2.association_id is null then 'No' else 'Yes' end Refitting  "
		+ ",case when bta2.association_id is null then 'Yes' else 'No' end At_plant "
		+ ",add2.auction_date from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on mt.depot_id = md.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id = mt.tyre_position_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta2 on bta2.tyre_id = mt.tyre_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah2 on btah2.tyre_id = mt.tyre_id and btah2.tyre_condition_id=5 and btah2.reason_id=3  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta2 on dta2.tyre_id= mt.tyre_id and dta2.is_received = false "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"auc_docket_tyre_association adta on adta.tyre_id = mt.tyre_id  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"auctioned_docket_details add2 on add2.docket_id = adta.docket_id  "
		+ "where mt.tyre_id =?1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 order by 2",nativeQuery=true)
List<Object[]> depotWiseTyreConsumptionReport(Integer id , Date fromDate , Date endDate);

@Query(value="select mt2.transport_name, md.depot_name, (case when mt.tyre_tag is not null then concat(mt.tyre_tag, '-' ,mtc.name) else mt.tyre_tag end)tyre_tag, mt.tyre_number, "
		+ "mtm.name as tyre_manufacture, mts.size, mtp.name as tyre_position, "
		+ "(coalesce (mbr1.kms,0)*coalesce((case when coalesce (cast( mt.expected_life as float),0)=0 then 0 else mt.tyre_cost / coalesce (cast( mt.expected_life as float),0) end),0)+ "
		+ "coalesce (mbr2.kms2,0)*coalesce (mbr2.cst,0)+ coalesce (mbr3.kms2,0)*coalesce (mbr3.cst,0)+ coalesce (mbr4.kms2,0)*coalesce (mbr4.cst,0))tyre_recovered_cost, "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then mbr1.install_date when mt.tyre_condition_id = mbr2.tyre_condition_id then mbr2.install_date "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then mbr3.install_date when mt.tyre_condition_id = mbr4.tyre_condition_id then mbr4.install_date end) install_date, "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce ((cast( mbr1.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce((cast(mbr2.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce ((cast(mbr3.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce ((cast(mbr4.expected_life as float)),0) end) expected_life, "
		+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0)) total_kms, "
		+ "((coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0)) - "
		+ "(case when mt.tyre_condition_id = mbr1.tyre_condition_id then coalesce ((cast( mbr1.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr2.tyre_condition_id then coalesce((cast(mbr2.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr3.tyre_condition_id then coalesce ((cast(mbr3.expected_life as float)),0) "
		+ "when mt.tyre_condition_id = mbr4.tyre_condition_id then coalesce ((cast(mbr4.expected_life as float)),0) end)) as extra_km_done "
		+ ",mdd.driver_name, mbd.bus_reg_number, mr.route_name, ''retreading_due_date "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mt.transport_unit "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id = mt.tyre_position_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta2 on bta2.tyre_id = mt.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = bta2.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.bus_id = mbd.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id= dr.route_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms, mbr.bus_id, bta.tyre_id, bta.tyre_condition_id, "
		+ "to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') install_date, "
		+ "(cast(mt3.expected_life as float))expected_life "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt3 "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on mt3.tyre_id = bta.tyre_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = bta.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and case when now() < ?3 then now() else ?3 end "
		+ "group by bta.tyre_id, bta.tyre_condition_id, mbr.bus_id, to_date(to_char(bta.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD'), mt3.expected_life) mbr1 on mbr1.tyre_id = mt.tyre_id "
		+ "and mbr1.kms> cast( mt.expected_life as float) "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R1' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life, dta.tyre_cost, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "having coalesce (sum(mbr.scheduled_kms),0)> dta.expected_life) mbr2 on mbr2.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life , "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R2' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life, dta.tyre_cost, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "having coalesce (sum(mbr.scheduled_kms),0)> dta.expected_life) mbr3 on mbr3.tyre_id = mt.tyre_id "
		+ "left join (select coalesce(sum(case when coalesce (mbr.vts_kms,0)=0 then ((mbr.total_actual_kms -coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) "
		+ "else ((mbr.vts_kms-coalesce (mbr.hill_kms,0)) + (coalesce (mbr.hill_kms,0) * 5)) end ),0) as kms2, mbr.bus_id, dta.tyre_id, btah.tyre_condition_id, dta.expected_life, "
		+ "to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')install_date, "
		+ "case when coalesce (dta.expected_life,0) =0 then 0 else dta.tyre_cost / dta.expected_life end cst from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah on btah.tyre_id = dta.tyre_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc2 on mtc2.tyre_condition_id = btah.tyre_condition_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_id = btah.bus_id "
		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between "
		+ "case when to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') > ?2 then to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?2 end and case when to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') < ?3 then to_date(to_char(btah.removal_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "else ?3 end and mtc2.name='R3' "
		+ "group by dta.tyre_id, mbr.bus_id ,btah.tyre_condition_id, dta.expected_life, dta.tyre_cost, to_date(to_char(btah.install_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
		+ "having coalesce (sum(mbr.scheduled_kms),0)> dta.expected_life) mbr4 on mbr4.tyre_id = mt.tyre_id "
		+ "where md.depot_id =?1 and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.route_id = dr.route_id and dr2.rota_id= dr.rota_id "
		+ "and dr2.bus_id= dr.bus_id and dr2.trip_id=dr.trip_id and dr2.driver_id= dr.driver_id) and "
		+ "(coalesce (mbr1.kms,0)+ coalesce (mbr2.kms2,0) +coalesce (mbr3.kms2,0) +coalesce (mbr4.kms2,0))> 0",nativeQuery=true)
List<Object[]> tyreDueForRetredingReport(Integer id , Date fromDate , Date endDate);

@Query(value = "SELECT t from TyreMaster t where t.tyreNumber=?1 ")
List<TyreMaster> validateByTyreNo(String tyreNo);

}
