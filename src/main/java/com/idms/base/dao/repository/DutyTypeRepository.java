package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DutyType;
import com.idms.base.dao.entity.QDutyType;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface DutyTypeRepository extends JpaRepository<DutyType, Integer>,
		QuerydslPredicateExecutor<DutyType>, QuerydslBinderCustomizer<QDutyType> {

	@Override
	default void customize(QuerydslBindings bindings, QDutyType root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
	
	@Query(value = "Select d from DutyType d where d.status=true ")
	List<DutyType> findAllByStatus();

}
