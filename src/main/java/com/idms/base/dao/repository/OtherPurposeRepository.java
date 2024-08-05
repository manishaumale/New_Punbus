package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.OtherPurpose;
import com.idms.base.dao.entity.QOtherPurpose;
import com.querydsl.core.types.dsl.StringPath;



@Repository
public interface OtherPurposeRepository extends JpaRepository<OtherPurpose, Integer> , QuerydslPredicateExecutor<OtherPurpose>, QuerydslBinderCustomizer<QOtherPurpose> {
	
	@Override
	default void customize(QuerydslBindings bindings, QOtherPurpose root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query(value = "SELECT op from OtherPurpose op where op.status=?1")
	List<OtherPurpose> findAllByStatus(boolean status);
	
			
			

}
