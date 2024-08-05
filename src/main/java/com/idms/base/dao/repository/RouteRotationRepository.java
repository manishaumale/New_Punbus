package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.idms.base.dao.entity.QRouteRotation;
import com.idms.base.dao.entity.RouteRotation;
import com.querydsl.core.types.dsl.StringPath;

public interface RouteRotationRepository extends JpaRepository<RouteRotation, Integer> , QuerydslPredicateExecutor<RouteRotation>, QuerydslBinderCustomizer<QRouteRotation>{
		
		@Override
		default void customize(QuerydslBindings bindings, QRouteRotation rotation) {
			// Exclude the Id from query filter
			bindings.excluding(rotation.id);

			// Perform IgnoreCase & EqualsLike comparison
			bindings.bind(String.class).first(
					(StringPath path, String value) -> path.containsIgnoreCase(value)
			);		
		}

}
