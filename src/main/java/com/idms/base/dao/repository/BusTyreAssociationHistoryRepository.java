package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusTyreAssociationHistory;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface BusTyreAssociationHistoryRepository extends JpaRepository<BusTyreAssociationHistory, Integer>{

	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".bus_tyre_association_history b join "+RestConstants.SCHEMA_NAME+".mst_takenoff_reason mtr on b.reason_id=mtr.taken_off_reason_id where b.tyre_id =?1 and mtr.reason_code ='RP' order by b.updated_on desc limit 1")
	BusTyreAssociationHistory findTyreRepairRecord(Integer id);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".bus_tyre_association_history where tyre_id =?1 and tyre_condition_id =?2")
	List<BusTyreAssociationHistory> findAllHistoryByTyreAndCondition(Integer tyreId, Integer conditionId);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".bus_tyre_association_history where tyre_id =?1")
	List<BusTyreAssociationHistory> findAllHistoryByTyreId(Integer tyreId);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".bus_tyre_association_history where tyre_id =?1 and bus_id = ?2")
	List<BusTyreAssociationHistory> findAllHistoryByTyreIdAndBusId(Integer tyreId,Integer busId);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".bus_tyre_association_history where bus_id=?1 and Date(removal_date)=Date(current_date)")
	List<BusTyreAssociationHistory> findByBusId(Integer id);
	
	@Query(nativeQuery=true ,value="select created_on from "+RestConstants.SCHEMA_NAME+".bus_tyre_association_history where bus_id =?1 order by updated_on desc limit 1")
	Date findTheBeforeTyreDate(Integer id);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".bus_tyre_association_history where tyre_id =?1 order by tyre_id desc limit 1")
	BusTyreAssociationHistory findConditionByTyreId(Integer tyreId);
}
