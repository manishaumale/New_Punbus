package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTotalNightsMaster;
import com.idms.base.dao.entity.TotalNightsMaster;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TotalNightsRepository extends JpaRepository<TotalNightsMaster, Integer>, QuerydslPredicateExecutor<TotalNightsMaster>, QuerydslBinderCustomizer<QTotalNightsMaster> {//, RevisionRepository<Employee, Long, Integer> {

	@Override
	default void customize(QuerydslBindings bindings, QTotalNightsMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<TotalNightsMaster> findAllByStatus(Boolean status);
	
	List<TotalNightsMaster> findAll();

}
