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
import com.idms.base.dao.entity.QFuelTankMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface FuelTankMasterRepository extends JpaRepository<FuelTankMaster, Integer> , QuerydslPredicateExecutor<FuelTankMaster>, QuerydslBinderCustomizer<QFuelTankMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QFuelTankMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Modifying
    @Query(value = "Update FuelTankMaster  set status=?1 where id=?2 ")
	int updateFuelTankMasterStatusFlag(Boolean flag, Integer id);
	
	List<FuelTankMaster> findAllByStatus(boolean status);
	
	@Query(value = "SELECT fm from FuelTankMaster fm where fm.status=?1 and fm.depot.id = ?2 and fm.isDeleted = false")
	List<FuelTankMaster> findAllByStatusAndDepotId(boolean status,Integer depotId);
	
	@Query(value = "SELECT fm from FuelTankMaster fm where fm.depot.id = ?1 and fm.isDeleted = false")
	List<FuelTankMaster> findAllByDepotId(Integer depotId);
	
	@Query(value = "SELECT fm from FuelTankMaster fm where fm.depot.id = ?1 and fm.isDeleted = ?2 order by fuel_tank_id desc ")
	List<FuelTankMaster> findAllByDepotIdAndisDeletedFlag(Integer depotId,Boolean flag);
	
	@Modifying
    @Query(value = "Update FuelTankMaster set isDeleted=?1 where id=?2 ")
	int deleteFuelTankMasterStatusFlag(Boolean flag, Integer id);
	
    @Query(value = "SELECT fm from FuelTankMaster fm where fm.tankCode=?1 ")
	FuelTankMaster checkTankCode(String tankCode);
    	
    @Query(value="SELECT max(ftch.cleaning_date) as last_cleaning_date "
    		+"from "+RestConstants.SCHEMA_NAME+"."+"fuel_tank_cleaning_history ftch "
    		+"where ftch.fuel_tank_id =?1  ",nativeQuery=true)
     String lastCleaningDate(Integer id);
    
    @Query(value = "SELECT fm from FuelTankMaster fm where fm.tankCode=?1 ")
	List<FuelTankMaster> validateTankCode(String tankCode);
	
}

