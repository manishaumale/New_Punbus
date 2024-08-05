package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QRestAlloactionDriverConductor;
import com.idms.base.dao.entity.RestAlloactionDriverConductor;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface RestAlloactionDriverConductorRepository extends JpaRepository<RestAlloactionDriverConductor, Integer> , QuerydslPredicateExecutor<RestAlloactionDriverConductor>, QuerydslBinderCustomizer<QRestAlloactionDriverConductor> {
	
	@Override
	default void customize(QuerydslBindings bindings, QRestAlloactionDriverConductor root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	//RestAlloactionDriverConductor findbyDriverId(Integer id);
	
	@Query(value = "SELECT radc from RestAlloactionDriverConductor radc where radc.driverMaster.id = ?1 ")
	RestAlloactionDriverConductor findbyDriverId(Integer driverId);
	
	@Query(value = "SELECT radc from RestAlloactionDriverConductor radc where radc.conductorMaster.id = ?1 ")
	RestAlloactionDriverConductor findbyConductorId(Integer conductorId);
	
	
	@Modifying
    @Query(value = "Update RestAlloactionDriverConductor set counter=?1,rest_count=?2 where rest_id=?3 ")
	int updateRestAlloactionDriverConductor(Integer counter, Integer rest,Integer restId);

	
}