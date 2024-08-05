package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DailyRoasterHistory;
import com.idms.base.dao.entity.QDailyRoasterHistory;
import com.querydsl.core.types.dsl.StringPath;

	
@Repository
public interface DailyRoasterHistoryRepository extends JpaRepository<DailyRoasterHistory, Integer> , QuerydslPredicateExecutor<DailyRoasterHistory>, 
QuerydslBinderCustomizer<QDailyRoasterHistory> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDailyRoasterHistory root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
		

}
