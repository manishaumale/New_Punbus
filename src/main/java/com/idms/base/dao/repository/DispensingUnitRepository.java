package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.QDispensingUnitMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface DispensingUnitRepository extends JpaRepository<DispensingUnitMaster, Integer> , QuerydslPredicateExecutor<DispensingUnitMaster>, QuerydslBinderCustomizer<QDispensingUnitMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDispensingUnitMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<DispensingUnitMaster> findAllByStatus(boolean status);
	
	@Query(value = "SELECT du from DispensingUnitMaster du where du.status=?1 and du.depot.id = ?2")
	List<DispensingUnitMaster> findAllByStatusAndDepotId(boolean status,Integer depotId);
	
	@Modifying
    @Query(value = "Update DispensingUnitMaster  set status=?1 where id=?2 ")
	int updateDUMasterStatusFlag(Boolean flag, Integer id);
	
	List<DispensingUnitMaster> findAllByStatusAndIsDeleted(boolean status,boolean flag);
	
	List<DispensingUnitMaster> findAllByIsDeleted(boolean isDeleted);
	
	@Modifying
    @Query(value = "Update DispensingUnitMaster  set isDeleted=?1 where id=?2 ")
	int updateDispensingUnitMasterIsDeletedFlag(Boolean flag, Integer id);
	
	@Modifying
    @Query(value = "Update DispensingUnitMaster  set currentReading=?1 where id=?2 ")
	void updateCurrentReading(double currentReading, Integer id);
	
	@Query(value = "SELECT du from DispensingUnitMaster du where du.isDeleted=?1 and du.depot.id = ?2 order by du.id desc")
	List<DispensingUnitMaster> findAllByDeletedFlagAndDepotId(boolean status,Integer depotId);
	
	@Query(nativeQuery=true,value = "select * from "+RestConstants.SCHEMA_NAME+".mst_dispensing_unit where dis_unit_code ilike %?1% ")
	DispensingUnitMaster findByDispensingCode(String code);
	
	@Query(nativeQuery=true,value = "select max(calibration_date) from "+RestConstants.SCHEMA_NAME+".dispensing_unit_calibration duc where duc.dispensing_id = ?1")
	Object[] findMaxDateByDispensingId(Integer duId);

}
