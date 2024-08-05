package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.OtherDuty;
import com.idms.base.dao.entity.QOtherDuty;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;



@Repository
public interface OtherDutyRepository extends JpaRepository<OtherDuty, Integer>,
		QuerydslPredicateExecutor<OtherDuty>, QuerydslBinderCustomizer<QOtherDuty> {

	@Override
	default void customize(QuerydslBindings bindings, QOtherDuty root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
	
	@Query(value = "Select d from OtherDuty d where d.isDeleted=?1 order by d.updatedOn desc")
	List<OtherDuty> findAllByIsDeleted(boolean IsDeleted);
	
	@Query(nativeQuery = true, value = "select duty.other_duty_id,duty.to_date from "+RestConstants.SCHEMA_NAME+"."+"other_duty duty where duty.to_date >= CURRENT_DATE and duty.driver_id =?1")
	List<Object[]> fetchAvailabilityDriverStatus(Integer driverId);
	
	@Query(nativeQuery = true, value = "select duty.other_duty_id,duty.to_date  from "+RestConstants.SCHEMA_NAME+"."+"other_duty duty where duty.to_date >= CURRENT_DATE and duty.conductor_id =?1")
	List<Object[]> fetchAvailabilityConductorStatus(Integer driverId);

}
