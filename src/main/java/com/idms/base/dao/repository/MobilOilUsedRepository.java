package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.MobilOilUsed;
import com.idms.base.dao.entity.QMobilOilUsed;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;



@Repository
public interface MobilOilUsedRepository extends JpaRepository<MobilOilUsed, Integer> , QuerydslPredicateExecutor<MobilOilUsed>, QuerydslBinderCustomizer<QMobilOilUsed> {
	
	@Override
	default void customize(QuerydslBindings bindings, QMobilOilUsed root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query(value = "select sum(quantity) from " + RestConstants.SCHEMA_NAME + "." 
			 + "mobil_oil_used mou  where mou.mobil_drum_id =?1 ", nativeQuery = true)
			Float toalAddBlueUsedFromDrum(Integer drumId);

}