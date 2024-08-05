package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.idms.base.dao.entity.QStopagesMaster;
import com.idms.base.dao.entity.RouteRotation;
import com.idms.base.dao.entity.StopagesMaster;
import com.querydsl.core.types.dsl.StringPath;

public interface StopagesMasterRepository extends JpaRepository<StopagesMaster, Integer> , QuerydslPredicateExecutor<RouteRotation>, QuerydslBinderCustomizer<QStopagesMaster>{	
		@Override
		default void customize(QuerydslBindings bindings, QStopagesMaster stopage) {
			// Exclude the Id from query filter
			bindings.excluding(stopage.id);

			// Perform IgnoreCase & EqualsLike comparison
			bindings.bind(String.class).first(
					(StringPath path, String value) -> path.containsIgnoreCase(value)
			);		
		}
}
