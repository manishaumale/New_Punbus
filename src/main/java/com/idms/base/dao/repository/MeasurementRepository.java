package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.MeasurementEntity;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface MeasurementRepository extends JpaRepository<MeasurementEntity,Integer> {
	
	@Query(value="select mm2.measurement_id ,mm2.name from "+RestConstants.SCHEMA_NAME+"."+"mst_measurement mm2 where mm2.item_type_id =?1",nativeQuery=true)
	List<Object[]> findByItemTypeId(Integer id);

}
