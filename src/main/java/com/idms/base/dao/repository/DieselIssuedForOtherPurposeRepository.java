package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DieselIssuedForOtherPurpose;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.QDieselIssuedForOtherPurpose;
import com.idms.base.dao.entity.RecieveDieselSupplyMaster;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface DieselIssuedForOtherPurposeRepository extends JpaRepository<DieselIssuedForOtherPurpose, Integer> , QuerydslPredicateExecutor<DieselIssuedForOtherPurpose>, QuerydslBinderCustomizer<QDieselIssuedForOtherPurpose> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDieselIssuedForOtherPurpose root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<DieselIssuedForOtherPurpose> findAllByIsDeleted(boolean IsDeleted);
	
	@Query(value = "SELECT di from DieselIssuedForOtherPurpose di where di.isDeleted=?1 and di.depot.id = ?2 order by di.id desc")
	List<DieselIssuedForOtherPurpose> findAllByStatusAndDepotId(boolean flag,Integer depotId);
	
			
			

}
