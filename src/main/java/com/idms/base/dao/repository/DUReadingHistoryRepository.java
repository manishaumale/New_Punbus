package com.idms.base.dao.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DUReadingHistory;
import com.idms.base.dao.entity.DipChartReadings;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface DUReadingHistoryRepository extends JpaRepository<DUReadingHistory, Integer> {

	Collection<DUReadingHistory> findByRefuelingId(Integer id);
	
	
	@Query(value = "select mdu.dis_unit_name,coalesce(t.endreading,mdu.initial_reading )current_reading, (select coalesce (max(drh.du_start_reading) ,0)from  "+RestConstants.SCHEMA_NAME+"."
			+ " du_reading_history drh where drh.dispensing_unit_id= mdu.dispensing_unit_id and drh.diesel_supply_id is null and drh.refueling_id is  null and drh.du_end_reading is null)openReading,  "
			+ " mdu.dispensing_unit_id  from  "+RestConstants.SCHEMA_NAME+".mst_dispensing_unit mdu  left join "
			+ " (select max(drh.du_end_reading)endreading,drh.dispensing_unit_id from  "+RestConstants.SCHEMA_NAME+".du_reading_history drh  group by drh.dispensing_unit_id )t  "
			+ " on t.dispensing_unit_id=mdu.dispensing_unit_id   "
			+ " where mdu.status =true and mdu.is_deleted =false  and mdu.depot_id = ?1", nativeQuery=true)
	List<Object[]> findAllDuDataByDepot(Integer dpId);
	
	
	@Query(nativeQuery=true ,value="select * from "+RestConstants.SCHEMA_NAME+".du_reading_history where dispensing_unit_id=?1 and refueling_id is not null  order by created_on desc limit 1 ")
	public DUReadingHistory findMaxRecord(Integer duId);

}
