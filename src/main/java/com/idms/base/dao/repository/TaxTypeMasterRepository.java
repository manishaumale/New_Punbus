package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTypeOfTaxMaster;
import com.idms.base.dao.entity.TypeOfTaxMaster;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface TaxTypeMasterRepository extends JpaRepository<TypeOfTaxMaster, Integer> , QuerydslPredicateExecutor<TypeOfTaxMaster>, QuerydslBinderCustomizer<QTypeOfTaxMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QTypeOfTaxMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<TypeOfTaxMaster> findAllByStatus(Boolean status);
	
	List<TypeOfTaxMaster> findAll();
	
}
