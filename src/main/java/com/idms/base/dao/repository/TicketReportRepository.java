package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

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
public interface TicketReportRepository extends JpaRepository<DepotMaster, Integer> , QuerydslPredicateExecutor<DepotMaster>, QuerydslBinderCustomizer<QDepotMaster>{

	@Override
	default void customize(QuerydslBindings bindings, QDepotMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	// ALL QUERY ARE DEMO QUERY NEED TO WRITE FOR ALL 
//	
//	@Query(value="select md.depot_name,etm.etm_number,etm.etm_ser_number,mem.name  from "+RestConstants.SCHEMA_NAME+"."+"mst_etm etm"
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_depot md  on etm.depot_id = md.depot_id"
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm_maker mem on mem.etm_maker_id = etm.etm_maker_id where to_date(to_char(etm.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//			+ " between ?1 and ?2 and etm.depot_id =?3", nativeQuery=true)
//	List<Object[]> getEtmStockReport(Date startDate, Date endDate, Long depoId);
//
//	@Query(value="select mcd.conductor_name,etm.etm_number,issue.created_on,mr.route_name,mt.trip_start_time "
//			+ " from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mcd on  mcd.conductor_id  = issue.conductor_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm etm on etm.etm_id = issue.etm_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" daily_roaster roaster on roaster.roaster_id = issue.roaster_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_route mr on mr.id = roaster.route_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt on mt.trip_id = roaster.trip_id where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//			+ " between ?1 and ?2 and issue.depot_id =?3 and issue.etm_id =?4", nativeQuery=true)
//	List<Object[]> getIssuanceReport(Date startDate, Date endDate, Integer depoId, Integer etmNo);
//
//	@Query(value="select mcd.conductor_name,etm.etm_number,issue.created_on,mt.trip_start_time from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+"  mst_etm etm on etm.etm_id = issue.etm_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" daily_roaster roaster on roaster.roaster_id = issue.roaster_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_route mr on mr.id = roaster.route_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt on mt.trip_id = roaster.trip_id "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mcd on  mcd.conductor_id  = issue.conductor_id "
//			+ " where issue.condutor_status = true and to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//			+ " between ?1 and ?2 and issue .depot_id =?3 and issue.iet_id in (select max(ietb.iet_id) from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box ietb "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm me on me.etm_id = ietb.etm_id "
//			+ " where ietb.condutor_status = true and ietb.etm_id = issue.etm_id) order by issue.iet_id desc ", nativeQuery=true)
//	List<Object[]> getNonIssuanceOfETMReport(Date startDate, Date endDate, Integer depoId);
//
//	@Query(value="select md.depot_name,ticket.ticket_box_number,etm.etm_number,mcd.conductor_name,mcd.conductor_code  from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mcd on  mcd.conductor_id  = issue.conductor_id    "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm etm on etm.etm_id = issue.etm_id "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_depot md  on issue.depot_id = md.depot_id "
//			+ " join  "+RestConstants.SCHEMA_NAME+"."+" mst_ticket_box ticket  on ticket.ticket_box_id = issue.ticket_box_id "
//			+ " where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//			+ " between ?1 and ?2 and issue.depot_id =?3", nativeQuery=true)
//	List<Object[]> ticketBoxAndMachineIssuanceReport(Date startDate, Date endDate, Long depoId);

//	@Query(value="select md.depot_name,etm.etm_number,etm.etm_ser_number,mem.name  from "+RestConstants.SCHEMA_NAME+"."+"mst_etm etm"
//	+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_depot md  on etm.depot_id = md.depot_id"
//	+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm_maker mem on mem.etm_maker_id = etm.etm_maker_id where to_date(to_char(etm.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//	+ " between ?1 and ?2 and etm.depot_id =?3", nativeQuery=true)
	
	@Query(value="select md.depot_name, me.etm_number as etm_code, me.etm_ser_number as etm_number, ''date_of_purchase, mem.name as Company_name, ''rate, "
			+ "mgg.value as machine_features,mt.transport_name "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_etm me "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = me.transport_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_etm_maker mem on me.etm_maker_id = mem.etm_maker_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_gsm_gps mgg on mgg.gsm_gps_id = me.gsm_andgpsid "
			+ "where (md.depot_id=?3 or 0=?3) and to_date(to_char(me.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2", nativeQuery=true)
	List<Object[]> getEtmStockReport(Date startDate, Date endDate, Long depoId);
	
	@Query(value="select mt.transport_name, md.depot_name, count(me.etm_ser_number) as etm_count, ''from_date, ''to_date, mem.name as Manufacturer, mgg.value as machine_features "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_etm me "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = me.transport_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_etm_maker mem on me.etm_maker_id = mem.etm_maker_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_gsm_gps mgg on mgg.gsm_gps_id = me.gsm_andgpsid "
			+ "where (md.depot_id=?3 or 0=?3) and to_date(to_char(me.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
			+ "group by mt.transport_name, md.depot_name, mem.name, mgg.value", nativeQuery=true)
	List<Object[]> etmStocSummarykReport(Date startDate, Date endDate, Long depoId);

//	@Query(value="select mcd.conductor_name,etm.etm_number,issue.created_on,mr.route_name,mt.trip_start_time "
//			+ " from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mcd on  mcd.conductor_id  = issue.conductor_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm etm on etm.etm_id = issue.etm_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" daily_roaster roaster on roaster.roaster_id = issue.roaster_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_route mr on mr.id = roaster.route_id  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt on mt.trip_id = roaster.trip_id where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//			+ " between ?1 and ?2 and issue.depot_id =?3 and issue.etm_id =?4", nativeQuery=true)
	/*@Query(value="select md.depot_name, mcd.conductor_name, mcd.mobile_number, me.etm_ser_number as Machine_Number, ietb.created_on as ETM_Issue_Time, "
			+ "(concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time)) as Route_Start_Time "
			+ ",EXTRACT(EPOCH FROM (TO_TIMESTAMP( concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time) , 'YYYY-MM-DD HH24:MI:SS' ) "
			+ "- ietb.created_on))/3600 as How_Early_Issued "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
			+ "where md.depot_id=?3 and me.etm_ser_number =?4 and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
	List<Object[]> getIssuanceReport(Date startDate, Date endDate, Integer depoId, Integer etmNo);*/
	
	
	
	
	
	
	
	
	
	
	
	
	@Query(value="select md.depot_name, mcd.conductor_name, mcd.mobile_number, me.etm_ser_number as Machine_Number, ietb.created_on as ETM_Issue_Time,"
+"(concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time)) as Route_Start_Time "
+",EXTRACT(EPOCH FROM (TO_TIMESTAMP( concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time) , 'YYYY-MM-DD HH24:MI:SS' ) "
+"- ietb.created_on))/3600 as How_Early_Issued, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"where (md.depot_id=?3 or 0=?3) and (me.etm_id =?4 or 0=?4) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> getIssuanceReport(Date startDate, Date endDate, Integer depoId, Integer etmNo);
	
	

@Query(value="select md.depot_name, ''from_date, ''to_date, count(me.etm_ser_number) as ETM_count, count(mr.id) as Route_count, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"where (md.depot_id=?3 or 0=?3) and (me.etm_id =?4 or 0=?4) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mt2.transport_name, md.depot_name",nativeQuery=true)
List<Object[]> getIssuanceSummaryReport(Date startDate, Date endDate, Integer depoId, Integer etmNo);
	

	/*@Query(value="select mcd.conductor_name,etm.etm_number,issue.created_on,mt.trip_start_time from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"  mst_etm etm on etm.etm_id = issue.etm_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" daily_roaster roaster on roaster.roaster_id = issue.roaster_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_route mr on mr.id = roaster.route_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt on mt.trip_id = roaster.trip_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mcd on  mcd.conductor_id  = issue.conductor_id "
			+ " where issue.condutor_status = true and to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ " between ?1 and ?2 and issue .depot_id =?3 and issue.iet_id in (select max(ietb.iet_id) from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box ietb "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm me on me.etm_id = ietb.etm_id "
			+ " where ietb.condutor_status = true and ietb.etm_id = issue.etm_id) order by issue.iet_id desc ", nativeQuery=true)
	List<Object[]> getNonIssuanceOfETMReport(Date startDate, Date endDate, Integer depoId);*/




@Query(value="select md.depot_name, mcd.conductor_name, mcd.mobile_number, me.etm_ser_number as Machine_Number, ietb.created_on as ETM_Issue_Time,"
+"(concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time)) as Route_Start_Time ,max(ietb.created_on) as Last_issue_date,"
+"(to_date(to_char(NOW(), 'YYYY/MM/DD'), 'YYYY/MM/DD') - max (to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD'))) as number_of_days, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"where (md.depot_id=?1 or 0=?1) group by mt2.transport_name, md.depot_name, mcd.conductor_name, mcd.mobile_number, me.etm_ser_number, ietb.created_on,"
+"(concat(SUBSTRING(cast(r.rota_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time))",nativeQuery=true)
List<Object[]> getNonIssuanceOfETMReport(Integer depoId);




@Query(value="select mt2.transport_name, md.depot_name, ''from_date, ''to_date, count(me.etm_ser_number) as ETM_count, count(mr.id) as Route_count "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"where (md.depot_id=?3 or 0=?3) and (me.etm_id =?4 or 0=?4) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mt2.transport_name, md.depot_name",nativeQuery=true)
List<Object[]> getNonIssuanceOfETMSummaryReport(Date startDate, Date endDate, Integer depoId,Integer etmNo);


//	@Query(value="select md.depot_name,ticket.ticket_box_number,etm.etm_number,mcd.conductor_name,mcd.conductor_code  from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue  "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_conductor_details mcd on  mcd.conductor_id  = issue.conductor_id    "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm etm on etm.etm_id = issue.etm_id "
//			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_depot md  on issue.depot_id = md.depot_id "
//			+ " join  "+RestConstants.SCHEMA_NAME+"."+" mst_ticket_box ticket  on ticket.ticket_box_id = issue.ticket_box_id "
//			+ " where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//			+ " between ?1 and ?2 and issue.depot_id =?3", nativeQuery=true)
	
	/*@Query(value="select md.depot_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date, mtb.ticket_box_number, me.etm_ser_number as ETM_Number, mcd.conductor_name, "
			+ "mcd.mobile_number "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_box mtb on ietb.ticket_box_id = mtb.ticket_box_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on ietb.etm_id = me.etm_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on ietb.conductor_id = mcd.conductor_id "
			+ "where me.depot_id=?3 and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
	List<Object[]> ticketBoxAndMachineIssuanceReport(Date startDate, Date endDate, Long depoId);*/



@Query(value="select md.depot_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') as date, mtb.ticket_box_number, me.etm_ser_number as ETM_Number,"
+"mcd.conductor_name, mcd.mobile_number, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_box mtb on ietb.ticket_box_id = mtb.ticket_box_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on ietb.etm_id = me.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on ietb.conductor_id = mcd.conductor_id "
+"where (me.depot_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> ticketBoxAndMachineIssuanceReport(Date startDate, Date endDate, Long depoId);


@Query(value="select mt2.transport_name, md.depot_name, ''from_date, ''to_date, count(mtb.ticket_box_number) as Ticket_box_count, count(me.etm_ser_number) as ETM_count "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_box mtb on ietb.ticket_box_id = mtb.ticket_box_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on ietb.etm_id = me.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
+"where (me.depot_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mt2.transport_name, md.depot_name",nativeQuery=true)
List<Object[]> ticketBoxAndMachineIssuanceSummaryReport(Date startDate, Date endDate, Long depoId);








//	@Query(value="select distinct conductor_id from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
//			+ " between ?1 and ?2 and issue.depot_id =?3", nativeQuery=true)
	
	/*@Query(value="select md.depot_name, count(mtb.ticket_box_number) as ticket_box_number, count(me.etm_ser_number) as ETM_Number, mcd.conductor_name, "
			+ "mcd.mobile_number "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_box mtb on ietb.ticket_box_id = mtb.ticket_box_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on ietb.etm_id = me.etm_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on ietb.conductor_id = mcd.conductor_id "
			+ "where me.depot_id=?3 and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
			+ "group by md.depot_name, mcd.conductor_name, mcd.mobile_number",nativeQuery=true)
	List<Object []> issuanceSameTicketBoxMachSameCondRep(Date startDate, Date endDate, Integer depoId);*/


/*@Query(value="select md.depot_name, ''from_date, ''to_date, mtb.ticket_box_number, count(mtb.ticket_box_number) as ticket_box_cnt, me.etm_ser_number as ETM_number,"
+"count(me.etm_ser_number) as ETM_Number_cnt, mcd.conductor_name, mcd.mobile_number, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_box mtb on ietb.ticket_box_id = mtb.ticket_box_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on ietb.etm_id = me.etm_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on ietb.conductor_id = mcd.conductor_id "
+"where (me.depot_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mt2.transport_name, md.depot_name, mtb.ticket_box_number, me.etm_ser_number, mcd.conductor_name, mcd.mobile_number",nativeQuery=true)
List<Object[]> issuanceSameTicketBoxMachSameCondRep(Date startDate, Date endDate, Integer depoId);*/


@Query(value="select md.depot_name, ''from_date, ''to_date, mtb.ticket_box_number, count(mtb.ticket_box_number) as ticket_box_cnt, me.etm_ser_number as ETM_number,"
+"count(me.etm_ser_number) as ETM_Number_cnt, mcd.conductor_name, mcd.mobile_number, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_box mtb on ietb.ticket_box_id = mtb.ticket_box_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on ietb.etm_id = me.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on me.depot_id = md.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on ietb.conductor_id = mcd.conductor_id "
+"where (me.depot_id=?3 or 0=?3 )and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mt2.transport_name, md.depot_name, mtb.ticket_box_number, me.etm_ser_number, mcd.conductor_name, mcd.mobile_number "
+"having count(mtb.ticket_box_number)>5 and count(me.etm_ser_number)>5 ",nativeQuery=true)
List<Object[]> issuanceSameTicketBoxMachSameCondRep(Date startDate, Date endDate, Integer depoId);




	@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> fuelDispenserWiseFuelIssuanceReport(Date startDate, Date endDate, Long stateId);

	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> depotWisePaperTicketStockReport(Date startDate, Date endDate, Long depotId);
	
	*/
	
	
	/*@Query(value="select md.depot_name, ms.state_name, mtd.denomination, mtbc.tb_count as Number_Books, ''Value_in_Rs, ''Received_Date "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms on mtms.centeral_stock_id = mcts.central_stock_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mtms.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on ms.state_id = mcts.state_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id = mcts.denomination_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_book_count mtbc on mtbc.master_stock_id = mcts.tb_count_id "
+"where md.depot_id=?3 and to_date(to_char(mcts.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> depotWisePaperTicketStockReport(Date startDate, Date endDate, Long depotId);*/
	
	@Query(value="select md.depot_name, ms.state_name, mtd.denomination, mtbc.tb_count as Number_Books, "
			+ "(mtbc.tb_count*count(*)*mtd.denomination)Value_in_Rs, count(*) number_of_book,mtms.updated_on as Received_Date, "
			+ "mt2.transport_name, mcts.series_number "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms on mtms.centeral_stock_id = mcts.central_stock_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls mstd on mstd.stock_id = mtms.master_stock_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mcts.transport_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mtms.depot_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on ms.state_id = mcts.state_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id = mcts.denomination_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_book_count mtbc on mtbc.master_stock_id = mcts.tb_count_id "
			+ "where (md.depot_id= ?3 or 0=?3) "
			+ "and to_date(to_char(mcts.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
			+ "group by md.depot_name, ms.state_name, mtd.denomination, mtbc.tb_count, mtms.updated_on, mt2.transport_name, "
			+ "mcts.series_number ",nativeQuery=true)
List<Object[]> depotWisePaperTicketStockReport(Date startDate, Date endDate, Long depotId);	
	
	
	
	
	
	
	
	
	
	
	

	/*@Query(value="select md.depot_name,etm.etm_number,setb.created_on,mt.trip_end_time,mt.trip_end_date  "
			+ " from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_etm etm on etm.etm_id = issue.etm_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_depot md  on issue.depot_id = md.depot_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" daily_roaster roaster on roaster.roaster_id = issue.roaster_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_route mr on mr.id = roaster.route_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" mst_trip mt on mt.trip_id = roaster.trip_id  "
			+ " join "+RestConstants.SCHEMA_NAME+"."+" submit_etm_ticket_box setb on setb.iet_id =  issue.iet_id  "
			+ " where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ " between ?1 and ?2 and issue.depot_id =?3 ", nativeQuery=true)
	List<Object[]> machineNotReceivedScheduledReport(Date startDate, Date endDate, Integer depotId);
*/
	



/*@Query(value="select md.depot_name, me.etm_ser_number as Machine_Number, "
+"concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_end_time) as Schedule_arrival, setb.created_on as Actual_Arrived, "
+"EXTRACT(EPOCH FROM (setb.created_on - TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time) , 'YYYY-MM-DD HH24:MI:SS' ) "
+"))/3600 as Late_in_Hours "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where md.depot_id=?3 and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> machineNotReceivedScheduledReport(Date startDate, Date endDate, Integer depotId);*/



@Query(value="select md.depot_name, me.etm_ser_number as Machine_Number,"
+"concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_end_time) as Schedule_arrival, setb.created_on as Actual_Arrived,"
+"EXTRACT(EPOCH FROM (setb.created_on - TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time), 'YYYY-MM-DD HH24:MI:SS') "
+"))/3600 as Late_in_Hours, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where (md.depot_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
List<Object[]> machineNotReceivedScheduledReport(Date startDate, Date endDate, Integer depotId);




@Query(value="select mt2.transport_name, md.depot_name, ''from_date, ''to_date, count(me.etm_ser_number) as ETM_count "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where (md.depot_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mt2.transport_name, md.depot_name",nativeQuery=true)
List<Object[]> machineNotReceivedScheduledSummaryReport(Date startDate, Date endDate, Integer depotId);

	@Query(value="select distinct etm_id from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box issue where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ " between ?1 and ?2 and issue.depot_id =?3", nativeQuery=true)
	List<Integer> frequentlyIssuanceMachineWithTicketBoxReport(Date startDate, Date endDate, Integer depoId);

	/*@Query(value="select mcd.conductor_name,mcd.conductor_code,mr.route_name,sum(child.amount)-sum(child.current_amount) as amount  "
			+ " from issue_etm_ticket_box issue "
			+ " join submit_etm_ticket_box setb on setb.iet_id =  issue.iet_id "
			+ " join mst_conductor_details mcd on  mcd.conductor_id  = issue.conductor_id "
			+ " join submit_etm_ticket_box_child child on child.submit_etm_ticket_box = setb.set_id "
			+ " join daily_roaster roaster on roaster.roaster_id = issue.roaster_id  "
			+ " join mst_route mr on mr.id = roaster.route_id  "
			+ " where to_date(to_char(issue.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ " between ?1 and ?2 and issue.conductor_id =?3 "
			+ " group by  mcd.conductor_name,mcd.conductor_code,mr.route_name ", nativeQuery=true)
	List<Object[]> kmplReport(Date startDate, Date endDate, Integer conductor);*/
	
	
	
	/*@Query(value="select md.depot_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') date, mcd.conductor_name, mcd.mobile_number,"
			+ "mr.route_name,(sum(ietbc.amount) - sum(setbc.current_amount)) Amount_of_Paper_Tickets_Used "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box_child ietbc on ietbc.issue_etm_ticket_box_entity = ietb.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box_child setbc on setbc.submit_etm_ticket_box = setb.set_id "
+"where mcd.conductor_id=?3 and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by md.depot_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD'), mcd.conductor_name, mcd.mobile_number, mr.route_name",nativeQuery=true)
List<Object[]> kmplReport(Date startDate, Date endDate, Integer conductor);
	*/
	
	
	
	/*@Query(value="select md.depot_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') date,"
			+"mcd.conductor_name, mcd.mobile_number, mr.route_name, "
+"(sum(ietbc.amount) - sum(setbc.current_amount)) Amount_of_Paper_Tickets_Used, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box_child ietbc on ietbc.issue_etm_ticket_box_entity = ietb.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = ietbc.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box_child setbc on setbc.submit_etm_ticket_box = setb.set_id "
+"where (mcd.conductor_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by md.depot_name, mt2.transport_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD'),"
+"mcd.conductor_name, mcd.mobile_number, mr.route_name",nativeQuery=true)
List<Object[]> kmplReport(Date startDate, Date endDate, Integer conductor);
	*/
	
	
	@Query(value="select md.depot_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') date, mcd.conductor_name, mcd.mobile_number, mr.route_name," 
+"(coalesce(sum(ietbc.amount),0) - coalesce(sum(setbc.current_amount),0)) Amount_of_Paper_Tickets_Used, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb " 
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box_child ietbc on ietbc.issue_etm_ticket_box_entity = ietb.iet_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = ietbc.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box_child setbc on setbc.submit_etm_ticket_box = setb.set_id " 
+"where (mcd.conductor_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by md.depot_name, mt2.transport_name, to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD'), mcd.conductor_name, mcd.mobile_number, mr.route_name",nativeQuery=true)
List<Object[]> kmplReport(Date startDate, Date endDate, Integer conductor);
	

	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> lateCashDepositReport(Date startDate, Date endDate, Long conductor);*/
	
	
	/*@Query(value="select mcd.conductor_name, mcd.mobile_number, mr.route_name, "
+" EXTRACT(EPOCH FROM (setb.created_on - TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time), 'YYYY-MM-DD HH24:MI:SS') "
+"))/3600 as Late_in_Hours "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where mcd.conductor_id=?3 and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery= true)
List<Object[]> lateCashDepositReport(Date startDate, Date endDate, Long conductor);
	*/

/*@Query(value="select md.depot_name, mcd.conductor_name, mcd.mobile_number, mr.route_name,"
+"EXTRACT(EPOCH FROM (setb.created_on - TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time), 'YYYY-MM-DD HH24:MI:SS') "
+"))/3600 as Late_in_Hours, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where (mcd.conductor_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> lateCashDepositReport(Date startDate, Date endDate, Long conductor);
	*/
	


@Query(value="select md.depot_name, mcd.conductor_name, mcd.mobile_number, mr.route_name, "
		+ "EXTRACT(EPOCH FROM (setb.created_on - TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time), 'YYYY-MM-DD HH24:MI:SS')  "
		+ "))/3600 as Late_in_Hours, mt2.transport_name,setb.created_on as actualtime,TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',mt.trip_start_time), 'YYYY-MM-DD HH24:MI:SS')  "
		+ "as scheduled_time  "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = ietb.conductor_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id  "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
		+ "where (mcd.conductor_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> lateCashDepositReport(Date startDate, Date endDate, Long conductor);




	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> addaFeesAndTollFeeReport(Date startDate, Date endDate, Long depot);*/



    @Query(value="select mt2.transport_name, md.depot_name, to_date(to_char(ea.etm_submit_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') date, "
+"mr.route_name, ''Adda_Fees, ''Toll_Fees, ''Total_Fees "
+"from "+RestConstants.SCHEMA_NAME+"."+"etm_assignment ea "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.way_bill_no = ea.way_bill_no "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box_child setbc on setbc.submit_etm_ticket_box = setb.set_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = setbc.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = setb.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"where (md.depot_id=?3 or 0=?3) and to_date(to_char(ea.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
  List<Object[]> addaFeesAndTollFeeReport(Date startDate, Date endDate, Long depot);




	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> kMReceivedLessthanSchduledKMsReport(Date startDate, Date endDate, Long route);*/

	
	
	@Query(value="select mr.route_name, mr.scheduled_kms, sum(etd.distance) Received_KMs, mbr.vts_kms, "
+ "(mr.scheduled_kms - sum(etd.distance) ) Difference, ''Reason "
+"from "+RestConstants.SCHEMA_NAME+"."+"etm_trip_dtls etd "
+"join "+RestConstants.SCHEMA_NAME+"."+"etm_assignment ea on ea.etm_assignment_id = etd.assignment_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = ea.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
+"where (mr.id=?3 or 0=?3) and etd.distance < mr.scheduled_kms "
+"and to_date(to_char(etd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mr.route_name, mr.scheduled_kms, mbr.vts_kms",nativeQuery=true)
List<Object[]> kMReceivedLessthanSchduledKMsReport(Date startDate, Date endDate, Long route);
	
	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> nonScheduleKMsReport(Date startDate, Date endDate, Long route);*/






@Query(value="select mr.route_name, mr.scheduled_kms, sum(etd.distance) Received_KMs, mbr.vts_kms,"
		+ " (mr.scheduled_kms - sum(etd.distance) ) Difference, ''Reason "
+"from "+RestConstants.SCHEMA_NAME+"."+"etm_trip_dtls etd "
+"join "+RestConstants.SCHEMA_NAME+"."+"etm_assignment ea on ea.etm_assignment_id = etd.assignment_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = ea.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
+"where (mr.id=?3 or 0=?3) and etd.distance > mr.scheduled_kms "
+"and to_date(to_char(etd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mr.route_name, mr.scheduled_kms, mbr.vts_kms",nativeQuery=true)
List<Object[]> nonScheduleKMsReport(Date startDate, Date endDate, Long route);



	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> comparisonReportDutyRosterKMs(Date startDate, Date endDate, Long depot);
*/

@Query(value="select mt2.transport_name, md.depot_Name, mr.route_name, mr.scheduled_kms as route_kms, mbr.scheduled_kms as DPA_KMs, mbd.bus_reg_number, mbr.vts_kms as Bus_vts_KMs,"
		+ "count(mt.trip_id) trip_cnt, mdd.driver_name, mcd.conductor_name "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mr.depot_id "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mbd.transport_unit "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id = dr.driver_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = dr.conductor_id "
		+ "where md.depot_id=?3 and to_date(to_char(dr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "group by mt2.transport_name, md.depot_Name, mr.route_name, mr.scheduled_kms, mbr.scheduled_kms, mbd.bus_reg_number, mbr.vts_kms,"
		+ "mdd.driver_name, mcd.conductor_name ",nativeQuery=true)
List<Object[]> comparisonReportDutyRosterKMs(Date startDate, Date endDate, Long depot);


	@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> repairBillReport(Date startDate, Date endDate, Long depot);


   @Query(value="select md.depot_name, mr.route_name, ''Bus_Stand_Name, bsbd.adv_booking_amt_byebtm as Advance_Booking_Amount, "
	   		+ " efe.net_amt_to_deposit as Cash_Deposited_Amount, "
	   		+ "mcd.conductor_name, mcd.conductor_code "
	   		+ "from "+RestConstants.SCHEMA_NAME+"."+"etm_assignment ea "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.way_bill_no = ea.way_bill_no "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = setb.iet_id "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"bus_stand_booking_dtls bsbd on bsbd.assignment_id = ea.etm_assignment_id "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"earning_from_etm efe on efe.assignment_id = ea.etm_assignment_id "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
	   		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id = dr.conductor_id "
	   		+ "where md.depot_id=?3 and to_date(to_char(bsbd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
	List<Object[]> busStandWiseAdvanceBookingReport(Date startDate, Date endDate, Long depot);

	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> freeTravelingConcessionTravelingReport(Date startDate, Date endDate);*/
	
	
	/*@Query(value="select md.depot_name, mr.route_name, sct.net_amount "
+"from "+RestConstants.SCHEMA_NAME+"."+"submit_concession_ticket sct "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.set_id = sct.set_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = setb.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"where to_date(to_char(sct.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> freeTravelingConcessionTravelingReport(Date startDate, Date endDate);
	*/
	
	
	/*@Query(value="select md.depot_name, mr.route_name, sum(sct.net_amount), mt2.transport_name, mct.pass_type, mct.discount "
+"from "+RestConstants.SCHEMA_NAME+"."+"submit_concession_ticket sct "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_concession_ticket mct on mct.concession_ticket_id = sct.concession_ticket_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.set_id = sct.set_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box_child setbc on setbc.submit_etm_ticket_box = setb.set_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = setbc.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = setb.iet_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+"where to_date(to_char(sct.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by md.depot_name, mr.route_name, mt2.transport_name, mct.pass_type, mct.discount ",nativeQuery=true)
List<Object[]> freeTravelingConcessionTravelingReport(Date startDate, Date endDate);
	*/
	
	@Query(value="select md.depot_name, mr.route_name, coalesce(sum(sct.net_amount),0) amount, mt2.transport_name, mct.pass_type, mct.discount "  
+"from "+RestConstants.SCHEMA_NAME+"."+"submit_concession_ticket sct " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_concession_ticket mct on mct.concession_ticket_id = sct.concession_ticket_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.set_id = sct.set_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box_child setbc on setbc.submit_etm_ticket_box = setb.set_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = setbc.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = setb.iet_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "  
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id " 
+"where to_date(to_char(sct.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by md.depot_name, mr.route_name, mt2.transport_name, mct.pass_type, mct.discount",nativeQuery=true)
List<Object[]> freeTravelingConcessionTravelingReport(Date startDate, Date endDate);

	/*@Query(value="select ms.depot_id, ms.depot_name from "+RestConstants.SCHEMA_NAME+"."+"mst_depot mc ", nativeQuery=true)
	List<Object[]> busTypeSubTypeCashDepositReport(Date startDate, Date endDate, Long busType, Long subType);
	*/







@Query(value="select mt2.transport_name, md.depot_name, mbt.bus_type_name, ''from_date, ''to_date, mr.route_name, count(distinct mbd.bus_id) as bus_count, "
		+ "coalesce(sum(etd.net_trip_amt),0) as Total_Cash "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"etm_trip_dtls etd "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"etm_assignment ea on ea.etm_assignment_id = etd.assignment_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.way_bill_no = ea.way_bill_no "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = setb.iet_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mbd.transport_unit "
		+ "where (mbt.bus_type_id=?3 or 0=?3) and (md.depot_id=?4 or 0=?4) and to_date(to_char(etd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
		+ "group by mt2.transport_name, md.depot_name, mbt.bus_type_name, mr.route_name ",nativeQuery=true)
List<Object[]> busTypeSubTypeCashDepositSummaryReport(Date startDate, Date endDate, Long busType,Integer depotId);



@Query(value="select mt2.transport_name, md.depot_name, mbt.bus_type_name, ''from_date, ''to_date, mr.route_name, mbd.bus_reg_number, "
+"coalesce(sum(etd.net_trip_amt),0) as Total_Cash "
+ "from "+RestConstants.SCHEMA_NAME+"."+"etm_trip_dtls etd "
+ "join "+RestConstants.SCHEMA_NAME+"."+"etm_assignment ea on ea.etm_assignment_id = etd.assignment_id "
+ "join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.way_bill_no = ea.way_bill_no "
+ "join "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb on ietb.iet_id = setb.iet_id "
+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mbd.transport_unit "
+ "where (mbt.bus_type_id=?3 or 0=?3) and (md.depot_id=?4 or 0=?4) and  "
+ "to_date(to_char(etd.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
+"group by mt2.transport_name, md.depot_name, mbt.bus_type_name, mr.route_name, mbd.bus_reg_number,ietb.roaster_id ",nativeQuery=true)
List<Object[]> busTypeSubTypeCashDepositReport(Date startDate, Date endDate, Long busType,Integer depotId);





	
	/*@Query(value="select md.depot_name, me.etm_ser_number as Machine_Number, "
+"concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',( mt.trip_end_time + interval '1 hour')) as Schedule_arrival,"
+"setb.created_on as Actual_Arrived, "
+"EXTRACT(EPOCH FROM (setb.created_on -TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),"
+"' ',( mt.trip_end_time + interval '1 hour')), 'YYYY-MM-DD HH24:MI:SS' ) "
+"))/3600 as Late_in_Hours " 
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where md.depot_id=?3 and "
+"TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',( mt.trip_end_time + interval '1 hour')), 'YYYY-MM-DD HH24:MI:SS' )"
+"< setb.created_on and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2",nativeQuery=true)
List<Object[]> machineNotReceivedAsperScheduledAndDelayInhrsReport(Date startDate, Date endDate,Integer depoId);*/
	
	
	@Query(value="select md.depot_name, me.etm_ser_number as Machine_Number,"
+"concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',( mt.trip_end_time + interval '1 hour')) as Schedule_arrival, "
+"setb.created_on as Actual_Arrived, "
+"EXTRACT(EPOCH FROM (setb.created_on - "
+" TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',( mt.trip_end_time + interval '1 hour')), 'YYYY-MM-DD HH24:MI:SS' ) "
+"))/3600 as Late_in_Hours, mt2.transport_name "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id = ietb.roaster_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_trip mt on mt.trip_id = dr.trip_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where (md.depot_id=?3 or 0=?3) and "
+"TO_TIMESTAMP( concat(SUBSTRING(cast(dr.rotation_availability_date as varchar(19)) FROM 1 FOR 10),' ',( mt.trip_end_time + interval '1 hour')), 'YYYY-MM-DD HH24:MI:SS' ) "
+"< setb.created_on "
+"and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 ",nativeQuery=true)
List<Object[]> machineNotReceivedAsperScheduledAndDelayInhrsReport(Date startDate, Date endDate,Integer depoId);	




@Query(value="select mt2.transport_name, md.depot_name, ''from_date, ''to_date, count(me.etm_ser_number) as ETM_count "
+"from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box ietb "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = ietb.depot_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_etm me on me.etm_id = ietb.etm_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = me.transport_id "
+"join "+RestConstants.SCHEMA_NAME+"."+"submit_etm_ticket_box setb on setb.iet_id = ietb.iet_id "
+"where (md.depot_id=?3 or 0=?3) and to_date(to_char(ietb.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 " 
+"group by mt2.transport_name, md.depot_name ",nativeQuery=true)
List<Object[]> machineNotReceivedAsperScheduledAndDelayInhrsSummaryReport(Date startDate, Date endDate,Integer depoId);	





	
}
