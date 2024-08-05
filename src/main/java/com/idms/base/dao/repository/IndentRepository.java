package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.Indent;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface IndentRepository extends JpaRepository<Indent,Integer> 
{

	
@Query(value="select mi.indent_id,mi.ident_number ,mt.transport_name ,md.depot_name ,mws.name , "
		+ "to_date(to_char(mi.created_on ,'YYYY/MM/DD'),'YYYY/MM/DD') as indentraisedby "
		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_indent mi "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md  "
		+ "on md.depot_id = mi.depot  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt  "
		+ "on mt.transport_id =mi.transport_unit  "
		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_workflow_status mws  "
		+ "on mws.status_id = mi.indentstatus  "
		+ "where mi.depot =?1 and mi.is_deleted=false order by mi.indent_id desc",nativeQuery=true)	
     List<Object[]> findbydepoCode(Integer Id);
     
     
     @Query(value="select mci.indentchild_id  ,(case when mitt.name ='Other' then mci.item_supplier else mm.name end) as manufacture, "
            +"(case when mitt.name ='Other' then mci.measurement_unit_name else mm2.name end)  as measurement, "
            +"(case when mitt.name ='Other' then mci.item_specifications else ms.name end) as specification ,mitt.name as itemtype, "
            +"(case when mitt.name ='Other' then mci.item_name else mint.name end) as itemname,"
     		+ "mci.item_quantity ,mci.justification,to_date(to_char(mci.target_date_to_receive , 'YYYY/MM/DD'), 'YYYY/MM/DD') as target_date_to_receive "
     		+",mm.manufacture_id,mm2.measurement_id ,ms.specification_id ,mitt.item_id,mint.itemname_id "
     		+",mci.item_supplier,mci.measurement_unit_name ,mci.item_specifications,mci.item_name "
     		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_child_indent mci  "
     		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_manufacture mm  "
     		+ "on mm.manufacture_id = mci.manufactureid "
     		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_measurement mm2  "
     		+ "on mm2.measurement_id =mci.measurementid "
     		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_specification ms  "
     		+ "on ms.specification_id =mci.specificationid "
     		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_item_type_tbl mitt  "
     		+ "on mitt.item_id =mci .itemtypeid  "
     		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_item_name_tbl mint  "
     		+ "on mint.itemname_id =mci.itemnameid  "
     		+ "where mci.indent_id =?1 and mci.is_deleted =false ",nativeQuery=true)
     List<Object[]> findByIndentChildListItems(Integer Id);
     
    
     @Modifying
     @Query(value="Update Indent set isDeleted=?1 where id=?2")
     int deleteIndentMasterStatusFlag(Boolean flag,Integer id);
     
     @Modifying
     @Query(value="UPDATE "+RestConstants.SCHEMA_NAME+"."+"mst_indent SET  indentstatus =?1 WHERE indent_id=?2 ",nativeQuery=true)
     int updateIndentMasterStatus(Integer statusid,Integer id);

   @Query(value="select to_date(to_char(mn.created_on ,'YYYY/MM/DD'),'YYYY/MM/DD')as createdon,mn.count from "+RestConstants.SCHEMA_NAME+"."+"mst_indent mn  "
   		+ "where indent_id =(select max(indent_id) from "+RestConstants.SCHEMA_NAME+"."+"mst_indent mi)",nativeQuery=true)
   List<Object[]> findbyIndentcount();

   
   @Query(value="select mci.indentchild_id ,mci.item_quantity as previous_demand_quantity , to_date(to_char(mci.created_on ,'YYYY/MM/DD'),'YYYY/MM/DD') as previous_supply_date from "
   		+RestConstants.SCHEMA_NAME+"."+"mst_child_indent mci where mci.indentchild_id in "
   		+ "(select max(mci2.indentchild_id) from "+RestConstants.SCHEMA_NAME+"."+"mst_child_indent mci2 where mci2.manufactureid=?2 and "
   		+ "mci2.measurementid=?3 and mci2.specificationid=?4 and itemnameid=?1)",nativeQuery=true)
   List<Object[]> findbyPrevousDemandQuantityAndPreviousSupplyDate(Integer itemid, Integer manufactureid,
			Integer measurementId, Integer specificationId);
   
   
   /*@Query(value="select mitt.name as type, mitt.item_id ,mint.name as typename,mint.itemname_id ,"
   		+ "mm2.name as measurement ,mm2.measurement_id ,mm.name as Manufacture,mm.manufacture_id , "
   		+ "ms.name as specification,ms.specification_id  "
   		+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_item_type_tbl mitt  "
   		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_item_name_tbl mint  "
   		+ "on mitt.item_id = mint.item_type_id  "
   		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_measurement mm2  "
   		+ "on  mint.itemname_id = mm2.itemnameid  "
   		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_manufacture mm  "
   		+ "on mint.itemname_id = mm.itemnameid  "
   		+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_specification ms  "
   		+ "on mm.manufacture_id = ms.manufactureid  "
   		+ "where mitt.item_id =?1 ",nativeQuery=true)
   List<Object[]> findByItemdetails(Integer id);
   */
   
}
