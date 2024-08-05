package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ETMMaster;
import com.idms.base.dao.entity.QETMMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface ETMMasterRepository extends JpaRepository<ETMMaster, Integer> , QuerydslPredicateExecutor<ETMMaster>, QuerydslBinderCustomizer<QETMMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QETMMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<ETMMaster> findAllByStatus(boolean status);

	@Query(value="Select d from ETMMaster d where d.depotCode = ?1 and d.status=true order by d.id desc")
	List<ETMMaster> findAllETMbyDpCode(String dpCode);
	
	@Query(value = "Select d from ETMMaster d where d.depotCode = ?1 and d.status=true and d.id not in ?2")
	List<ETMMaster> getEtmRecordByDepot(String dpCode, List<Integer> etmId);

	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".mst_etm where etm_number= ?1 and status=true")
	ETMMaster findByEtmNumber(String etmId);
	
	
}