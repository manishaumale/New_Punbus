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

import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.QBusTyperMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface BusTyperMasterRepository extends JpaRepository<BusTyperMaster, Integer> , QuerydslPredicateExecutor<BusTyperMaster>, QuerydslBinderCustomizer<QBusTyperMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBusTyperMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<BusTyperMaster> findAllByStatus(Boolean status);
	
	List<BusTyperMaster> findAll();
	
	@Modifying
    @Query(value = "Update BusTyperMaster  set status=?1 where id=?2 ")
	int updateBusTypeStatusFlag(Boolean flag, Integer id);
	
	@Query(value="select  bus_type_id ,bus_type_name "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type",nativeQuery=true)
	List<Object[]> getBusTypeName();
	
	@Modifying
	@Query(value="select mbt.bus_type_name, mbd.bus_reg_number, "
			//+ "(select mr2.route_name from "+RestConstants.SCHEMA_NAME+"."+"mst_route mr2 join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh on mr2.id = drh.old_route_id where mr.id=mr2.id) as old_route_name, "
			+ "dr.override_reason, mr.route_name as new_route, dr.remarks from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster_history drh "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr on drh.roaster_id= dr.roaster_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id = mbd.bus_type "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
			+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
			+ "where dr.bus_id =75 and to_date(to_char(r.rota_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2", nativeQuery=true)
	List<Object[]> dutyInspectorOverrideReport(Date startdate , Date endDate);
	
	  @Query(value="select mbd.bus_reg_number, to_date(to_char(mbr.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') as Date, mr.route_name, coalesce (mbr.kmpl_as_scheduled_kilometer,0) as KMPL, ''EPKM  "
	    		+ "from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr "
	    		+ "join "+RestConstants.SCHEMA_NAME+"."+"roaster r on r.rota_id = dr.rota_id "
	    		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = dr.bus_id "
	    		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_route mr on mr.id = dr.route_id "
	    		+ "join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_refueling mbr on mbr.bus_refueling_id = dr.refueling_id "
	    		+ "where to_date(to_char(mbr.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?1 and ?2 "
	    		+ "and dr.roaster_id = (select max (dr2.roaster_id) from "+RestConstants.SCHEMA_NAME+"."+"daily_roaster dr2 where dr2.refueling_id =dr.refueling_id) "
	    		+ "order by 3 desc" , nativeQuery=true)
			List<Object[]> detailBusDriverConductorReport(Date startDate , Date endDate);
	
}
