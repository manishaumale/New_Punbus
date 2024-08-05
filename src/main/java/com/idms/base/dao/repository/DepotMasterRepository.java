package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.QDepotMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface DepotMasterRepository extends JpaRepository<DepotMaster, Integer> , QuerydslPredicateExecutor<DepotMaster>, QuerydslBinderCustomizer<QDepotMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDepotMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<DepotMaster> findAllByStatus(boolean status);
	
	List<DepotMaster> findAll();
	
	@Modifying
    @Query(value = "Update DepotMaster  set status=?1 where id=?2 ")
	int updateDepotMasterStatusFlag(Boolean flag, Integer id);
	
	@Query(value = "SELECT dm from DepotMaster dm where depotCode=?1")
	DepotMaster findByDepotCode(String depotCode);
	
	@Query(value = "SELECT distinct p from DepotMaster p JOIN p.depotTransportList ug where ug.transportUnitMaster.id in (?1) ")
	List<DepotMaster> findAllByTPIds(List<Integer> tpIds);
	
	@Query(value="select d.depot_id,d.depot_name "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_depot d where d.depot_name =?1", nativeQuery=true)
	Integer getIdByDepoName(String dpCode);
	
	@Query(value="select md.depot_id ,md.depot_code from "+RestConstants.SCHEMA_NAME+"."+"mst_depot md where md.status=true",nativeQuery=true)
	List<Object[]> findByDepocodeAndId();
}
