package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ETMMakerMaster;
import com.idms.base.dao.entity.QETMMakerMaster;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface ETMMakerMasterRepository extends JpaRepository<ETMMakerMaster, Integer> , QuerydslPredicateExecutor<ETMMakerMaster>, QuerydslBinderCustomizer<QETMMakerMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QETMMakerMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<ETMMakerMaster> findAllByStatus(boolean status);
	
	
}
