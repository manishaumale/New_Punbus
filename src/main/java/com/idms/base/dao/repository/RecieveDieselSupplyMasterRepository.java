package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.QRecieveDieselSupplyMaster;
import com.idms.base.dao.entity.RecieveDieselSupplyMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface RecieveDieselSupplyMasterRepository extends JpaRepository<RecieveDieselSupplyMaster, Integer> , QuerydslPredicateExecutor<RecieveDieselSupplyMaster>, QuerydslBinderCustomizer<QRecieveDieselSupplyMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QRecieveDieselSupplyMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<RecieveDieselSupplyMaster> findAllByStatus(boolean status);
	
	@Modifying
    @Query(value = "Update RecieveDieselSupplyMaster  set status=?1 where id=?2 ")
	int updateReceiveDieselMasterStatusFlag(Boolean flag, Integer id);
	
	@Modifying
    @Query(value = "Update RecieveDieselSupplyMaster  set isDeleted=?1 where id=?2 ")
	int updatedeleteReceiveDieselMasterIsDeletedFlag(Boolean flag, Integer id);
	
	@Query(value = "select * from "+RestConstants.SCHEMA_NAME+"."+"mst_recieve_diesel_sup m where m.is_deleted =?1 order by m.diesel_supply_id desc ",nativeQuery=true)
	List<RecieveDieselSupplyMaster> findAllByIsDeleted(boolean IsDeleted);
	
	@Query(value = "Select fuelObj from FuelTankMaster fuelObj where fuelObj.id= ?1")
	FuelTankMaster fetchCurrValAndCapacity(Integer tankId);
	
	@Query(value = "select max(update_supply_id) from "+RestConstants.SCHEMA_NAME+"."+"mst_update_supply where fuel_tank_id = ?1",nativeQuery=true)
	Object[] fetchupdateSupplyId(Integer updateId);
	
	@Query(value = "Select r from RecieveDieselSupplyMaster r where r.updateSupply.id= ?1")
	RecieveDieselSupplyMaster fetchRecieceObject(Integer updateId);
	
	
}