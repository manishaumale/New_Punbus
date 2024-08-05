package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QStopageEntity;
import com.idms.base.dao.entity.StopageEntity;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface StopageEntityRepository extends JpaRepository<StopageEntity, Integer> , QuerydslPredicateExecutor<StopageEntity>, QuerydslBinderCustomizer<QStopageEntity>{

	@Override
	default void customize(QuerydslBindings bindings, QStopageEntity root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<StopageEntity> findAllByStatus(Boolean status);
	
}
