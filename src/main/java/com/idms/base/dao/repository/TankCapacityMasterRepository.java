package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.QTankCapacityMaster;
import com.idms.base.dao.entity.TankCapacityMaster;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface TankCapacityMasterRepository extends JpaRepository<TankCapacityMaster, Integer> , QuerydslPredicateExecutor<TankCapacityMaster>, QuerydslBinderCustomizer<QTankCapacityMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QTankCapacityMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<TankCapacityMaster> findAllByStatus(boolean status);
	
	@Query(value = "SELECT t from TankCapacityMaster t where t.capacity = ?1 ")
	List<TankCapacityMaster> findTankCapacity(Double capacity);
}
