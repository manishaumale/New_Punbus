package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.SpecificationEntity;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface SpecificationRepository extends JpaRepository<SpecificationEntity,Integer> {
	
	@Query(value="select ms.specification_id ,ms.name from "+RestConstants.SCHEMA_NAME+"."+"mst_specification ms  where ms.item_type_id =?1",nativeQuery=true)
	List<Object[]> findByItemTypeId(Integer id);

}
