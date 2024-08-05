package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTaxMaster;
import com.idms.base.dao.entity.TaxMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TaxMasterRepository extends JpaRepository<TaxMaster, Integer> , QuerydslPredicateExecutor<TaxMaster>, QuerydslBinderCustomizer<QTaxMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QTaxMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<TaxMaster> findAllByStatus(Boolean status);
	
	List<TaxMaster> findAll();
	
	@Modifying
    @Query(value = "Update TaxMaster  set status=?1 where id=?2 ")
	int updateTaxMasterStatusFlag(Boolean flag, Integer id);
	
	@Query(value=" select mt.tax_id, ms.state_id, ms.state_name, mtot.tax_type_id, mtot.tax_type_name, mbt.bus_type_id, mbt.bus_type_name, mt.tax_amount, mt.applicable_from, mt.status "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_tax mt "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on mt.state_id =ms.state_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_type_of_tax mtot on mtot.tax_type_id = mt.tax_type_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type mbt on mbt.bus_type_id=mt.bus_type_id ", nativeQuery=true)
	List<Object[]> findAllTaxByQuery();
	
	@Query(value = "SELECT t from TaxMaster t where t.state.id =?1 and t.busTyperMaster.id=?2 and applicableFrom < ?3")
	List<TaxMaster> findAllByStateAndBusTypeId(Integer stateId,Integer busTypeId,Date currentDate);
	
}