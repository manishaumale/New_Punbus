package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QRouteComplexityMaster;
import com.idms.base.dao.entity.RouteComplexityMaster;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface RouteComplexityRepository extends JpaRepository<RouteComplexityMaster, Integer>, QuerydslPredicateExecutor<RouteComplexityMaster>, QuerydslBinderCustomizer<QRouteComplexityMaster> {//, RevisionRepository<Employee, Long, Integer> {

	@Override
	default void customize(QuerydslBindings bindings, QRouteComplexityMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<RouteComplexityMaster> findAllByStatus(Boolean status);
	
	List<RouteComplexityMaster> findAll();

}
