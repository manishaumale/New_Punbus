package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.TyreAuthenticationEntity;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface TyreAuthenticationRepository extends JpaRepository<TyreAuthenticationEntity,Integer>
{
	
	/*@Query(value="select mbd.bus_reg_number,mbd.bus_id,mt.tyre_id ,mt.tyre_number,mt.expected_life,mt.tyre_make,mt.tyre_condition_id,'' as takenoffmileage,"
	+"mtc.name as tyrecodemn,mtm.name as tyremakename "
+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah " 
+"on  mt.tyre_id =btah.tyre_id left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd " 
+"on btah.bus_id =mbd.bus_id left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc " 
+"on mt.tyre_condition_id = mtc.tyre_condition_id left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm " 
+"on mtm.tyre_maker_id =mt.tyre_make where btah.reason_id is not null and depot_id =?1 ",nativeQuery=true)
List<Object[]> findByTyreAuthenticationdetails(Integer id);*/
	
@Query(value="select mt.tyre_tag,mt.tyre_number,mtm.name as tyremakename, mts.size, mtc.name as conditionname,"
		+ " ta.tyre_recovered_cost,ta.tyre_installation_date,ta.kms_done_bus ,ta.kms_in_condition ,"
		+ " ta.total_kms_done,mtp.name as tyre_position, mt.tyre_id,ta.tyre_auth_id,mtp.tyre_position_id,"
+ " mtc.tyre_condition_id,mtm.tyre_maker_id,mts.tyre_size_id "
+"from "+RestConstants.SCHEMA_NAME+"."+"tyre_authentication ta join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on ta.tyre_id =mt.tyre_id  " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id =ta.tyre_make " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id =ta.tyre_size_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on ta.tyre_condition_id = mtc.tyre_condition_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on ta.tyre_position_id =mtp.tyre_position_id "
+"where ta.depot_id =?1 order by ta.tyre_auth_id desc",nativeQuery=true)
		List<Object[]> findByTyreAuthenticationdetails(Integer id);

/*@Query(value="select mbd.bus_reg_number,mt.tyre_number,mtm.name as tyremake,mtc.name as tyrecodemn,ta.tyre_expectedlife,ta.taken_offmileage,"
+"ta.remarks,ta.tyre_auth_id from "+RestConstants.SCHEMA_NAME+"."+"tyre_authentication ta "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt " 
+"on mt.tyre_id = ta.tyre_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = ta.tyre_make " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc " 
+"on mtc.tyre_condition_id = ta.tyre_condition_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd " 
+"on mbd.bus_id = ta.bus_id where ta.depot_id=?1 ",nativeQuery= true)
List<Object[]> findbydepoId(Integer id);*/
		
@Query(value="select mt.tyre_id,mt.tyre_tag,mt.tyre_number,mtm.name as tyremakename, mts.size as tyresize,mtc.name as conditionname ,mtp.name as tyre_position,mbd.bus_reg_number,mt2.transport_name,bh.install_date as tyreInstalledDate,bh.remarks ,u.username as takenOffBy,mbd.bus_id,mtr.reason_name,mtr.reason_code, "
+ " mr.route_name,mr.id ,mdd.driver_name,mdd.driver_id ,mcd.conductor_id ,mcd.conductor_name  ,mtr.taken_off_reason_id as reason_id ,mtp.tyre_position_id,"
+ " mtc.tyre_condition_id,mtm.tyre_maker_id,mts.tyre_size_id "		
+ "  from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id =mt.tyre_make "	
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id =mt.tyre_size " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mt.tyre_condition_id = mtc.tyre_condition_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_takenoff_reason mtr on mtr.taken_off_reason_id =mt.reason_id "
+"join (select btah.tyre_id,btah.tyre_position_id ,btah.bus_id,btah.install_date,btah.remarks,btah.removal_date,btah.created_by from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt3 on mt3.tyre_id=btah.tyre_id "
+ " and btah.association_history_id =(select max(association_history_id) from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history btah2 where btah2.tyre_id=btah.tyre_id  ))bh on bh.tyre_id=mt.tyre_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on bh.tyre_position_id =mtp.tyre_position_id "
+"inner join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id =bh.bus_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"users u on u.id =bh.created_by "
+"left join (select max(brh.roaster_id)roasterid ,brh.bus_id,brh.created_on "
+ " from "+RestConstants.SCHEMA_NAME+"."+"bus_roaster_history brh group by brh.bus_id ,brh.created_on) ros on ros.bus_id=mbd.bus_id and DATE(ros.created_on)=DATE(bh.removal_date) " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on dr.roaster_id =ros.roasterid " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id =dr.route_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd on mdd.driver_id =dr.driver_id " 
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd on mcd.conductor_id =dr.conductor_id " 
+"join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id =mt.transport_unit " 
+"where mt.depot_id=?1 and mtr.reason_code in('GMAR','GMAC') order by mt.updated_on desc",nativeQuery= true)
				List<Object[]> findbydepoId(Integer id);

}
