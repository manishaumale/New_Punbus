package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ManufactureEntity;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface ManufactureRepository extends JpaRepository<ManufactureEntity, Integer>{
	
	@Query(value="select mm.manufacture_id , mm.name from "+RestConstants.SCHEMA_NAME+"."+"mst_manufacture mm  where mm.item_type_id =?1",nativeQuery=true)
	List<Object[]> findByItemTypeId(Integer id);

}
