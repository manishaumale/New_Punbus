package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTyrePosition;
import com.idms.base.dao.entity.TyrePosition;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TyrePositionRepository extends JpaRepository<TyrePosition, Integer> , QuerydslPredicateExecutor<TyrePosition>, QuerydslBinderCustomizer<QTyrePosition> {
	
	@Override
	default void customize(QuerydslBindings bindings, QTyrePosition root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<TyrePosition> findAllByStatus(boolean status);
	
	@Query(value = "SELECT t from TyrePosition t where t.tyreCount.id = ?1 and t.status=true ")
	List<TyrePosition> findAllByTyreCount(Integer id);

	@Query(value="select distinct mtp.tyre_position_id, mtp.name "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp "
			+ " where mtp.tyre_count = ?2 and mtp.tyre_position_id not in "
			+ " (select COALESCE(tyre_position_id,0) from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta where bta.bus_id=?1)", nativeQuery=true)
	List<Object[]> getRemaingPositionList(Integer busId, Integer tyreCount);
	
}
