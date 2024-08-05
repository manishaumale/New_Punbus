package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.QCityMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface CityMasterRepository extends JpaRepository<CityMaster, Integer> , QuerydslPredicateExecutor<CityMaster>, QuerydslBinderCustomizer<QCityMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QCityMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<CityMaster> findByStateIdAndStatus(Integer stateId, Boolean status);
	
	List<CityMaster> findAllByStatus(Boolean status);
	
	List<CityMaster> findByStateId(Integer stateId);
	
	List<CityMaster> findAll();
	
	@Modifying
    @Query(value = "Update CityMaster  set status=?1 where id=?2 ")
	int updateCityMasterStatusFlag(Boolean flag, Integer id);

	@Query(value="SELECT t from CityMaster t where t.cityCode=?1 ")
	CityMaster findByCityCode(String fromCityCode);
	
	@Query(value="select ms.state_id, ms.state_name, mc.city_id, mc.city_name, mc.city_code, mc.status from "+RestConstants.SCHEMA_NAME+"."+"mst_city mc left join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on ms.state_id = mc.state_id  order by mc.city_name ", nativeQuery=true)
	List<Object[]> findAllCitiesQuery();
	
}

