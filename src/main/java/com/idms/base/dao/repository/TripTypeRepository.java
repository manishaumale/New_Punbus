package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTripType;
import com.idms.base.dao.entity.TripType;
import com.querydsl.core.types.dsl.StringPath;



@Repository
public interface TripTypeRepository extends JpaRepository<TripType, Integer>,
		QuerydslPredicateExecutor<TripType>, QuerydslBinderCustomizer<QTripType> {

	@Override
	default void customize(QuerydslBindings bindings, QTripType root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
	
	List<TripType> findAllByStatus(boolean status);

}
