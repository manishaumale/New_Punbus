package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.EmploymentType;
import com.idms.base.dao.entity.QEmploymentType;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface EmploymentTypeRepository extends JpaRepository<EmploymentType, Integer> , QuerydslPredicateExecutor<EmploymentType>, QuerydslBinderCustomizer<QEmploymentType> {
	
	@Override
	default void customize(QuerydslBindings bindings, QEmploymentType root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<EmploymentType> findAllByStatus(boolean status);
	
	
}
