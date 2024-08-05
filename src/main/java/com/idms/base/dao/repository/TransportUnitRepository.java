package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DepotTransportUnit;
import com.idms.base.dao.entity.QTransportUnitMaster;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TransportUnitRepository extends JpaRepository<TransportUnitMaster, Integer> , QuerydslPredicateExecutor<TransportUnitMaster>, QuerydslBinderCustomizer<QTransportUnitMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QTransportUnitMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<TransportUnitMaster> findAllByStatus(Boolean status);
	
	List<TransportUnitMaster> findAll();
	
	@Modifying
    @Query(value = "Update TransportUnitMaster  set status=?1 where id=?2 ")
	int updateTransportMasterStatusFlag(Boolean flag, Integer id);
	
	@Query(value = "Select depot from DepotTransportUnit depot where depotMaster.id=?1")
	List<DepotTransportUnit> allTransportMasterByDepot(Integer depotId);

	@Query(value = "Select id from TransportUnitMaster tp where tp.groupId in ?1")
	List<Integer> findByGroupIds(String[] groupIds);
	
	@Query(value = "Select tp from TransportUnitMaster tp where tp.groupId in ?1")
	List<TransportUnitMaster> findTPUByGroupIds(String[] groupIds);
}