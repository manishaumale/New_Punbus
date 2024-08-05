package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusLayout;
import com.idms.base.dao.entity.QBusLayout;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface BusLayoutRepository extends JpaRepository<BusLayout, Integer> , QuerydslPredicateExecutor<BusLayout>, QuerydslBinderCustomizer<QBusLayout> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBusLayout root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<BusLayout> findAllByStatus(boolean status);
	
	
}
