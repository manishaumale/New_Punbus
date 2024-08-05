package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.AdBlueUsed;
import com.idms.base.dao.entity.QAdBlueUsed;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface AdBlueUsedRepository extends JpaRepository<AdBlueUsed, Integer> , QuerydslPredicateExecutor<AdBlueUsed>, QuerydslBinderCustomizer<QAdBlueUsed> {
	
	@Override
	default void customize(QuerydslBindings bindings, QAdBlueUsed root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
			
	@Query(value = "select sum(quantity) from " + RestConstants.SCHEMA_NAME + "."
			+ "ad_blue_used abu where add_blue_drum_id =?1 ", nativeQuery = true)
	Float toalMobilUsedFromDrum(Integer drumId);
			
			

}