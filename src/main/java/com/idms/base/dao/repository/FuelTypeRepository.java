package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.FuelType;
import com.idms.base.dao.entity.QFuelType;
import com.querydsl.core.types.dsl.StringPath;



@Repository
public interface FuelTypeRepository extends JpaRepository<FuelType, Integer> , QuerydslPredicateExecutor<FuelType>, QuerydslBinderCustomizer<QFuelType> {
	
	@Override
	default void customize(QuerydslBindings bindings, QFuelType root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query(value = "SELECT fm from FuelType fm where fm.status=?1")
	List<FuelType> findAllByStatus(boolean status);
	
			
			

}
