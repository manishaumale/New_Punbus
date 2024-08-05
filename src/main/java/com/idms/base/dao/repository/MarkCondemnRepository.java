package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.idms.base.dao.entity.MarkCondemn;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface MarkCondemnRepository extends JpaRepository<MarkCondemn, Integer> {
	
	@Query(value="select mbd.bus_reg_number ,mc.remarks,mc.bus_id,to_date(to_char(mc.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') as createdOn, "
			+ "  to_date(to_char(mc.condemn_date , 'YYYY/MM/DD'), 'YYYY/MM/DD') as conDate from "+RestConstants.SCHEMA_NAME+"."+"mark_codemn mc "
			+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mc.bus_id = mbd.bus_id " 
              +"where mc.depot_id=?1 order by mc.mark_id desc ",nativeQuery=true)
   List<Object[]> findBydepoId(Integer id);
   
   
   @Query(value="select mbd.bus_id,mbd.bus_reg_number from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd " 
+"where  not exists ("
+"select mc.bus_id from "+RestConstants.SCHEMA_NAME+"."+"mark_codemn mc where mc.bus_id = mbd.bus_id and mc.bus_id is not null ) "
+"and mbd.depot =?1 ",nativeQuery=true)
 List<Object[]> getBusDetails(Integer id);

}
