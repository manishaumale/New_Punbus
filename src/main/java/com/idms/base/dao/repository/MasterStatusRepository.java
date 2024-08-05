package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.MasterStatus;
import com.idms.base.dao.entity.QMasterStatus;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface MasterStatusRepository extends JpaRepository<MasterStatus, Integer> , QuerydslPredicateExecutor<MasterStatus>, QuerydslBinderCustomizer<QMasterStatus> {
	
	@Override
	default void customize(QuerydslBindings bindings, QMasterStatus root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query(value = "SELECT status from MasterStatus status where status.name = ?1")
	MasterStatus findAllByStatusName(String statusName);

	
}
