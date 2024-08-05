package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QRouteBlock;
import com.idms.base.dao.entity.RouteBlock;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface RouteBlockRepository extends JpaRepository<RouteBlock, Integer>, QuerydslPredicateExecutor<RouteBlock>,
		QuerydslBinderCustomizer<QRouteBlock> {

	@Override
	default void customize(QuerydslBindings bindings, QRouteBlock root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
	
	@Query(value = "SELECT o from RouteBlock o where o.depot.id = ?1")
	List<RouteBlock> findAllOutByDepot(Integer depotId);
	
	
}
