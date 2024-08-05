package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusTyreTypeSizeMapping;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface BusTyreTypeSizeMappingRepository extends JpaRepository<BusTyreTypeSizeMapping, Integer> {

	@Query(value="SELECT t from BusTyreTypeSizeMapping t where t.busType.id=?1 and t.tyreType.id=?2 and t.tyreSize.id=?3 ")
	List<BusTyreTypeSizeMapping> findByBusTyreTypeSize(Integer busTypeId, Integer tyreTypeId, Integer tyreSizeId);

	@Query(value="select tyre_size_id  from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_tyre_type_size_mapping mbttsm where mbttsm.bus_type_id=?1", nativeQuery=true)
	List<Integer> getTyreTypeList(Integer busTypeId);
	
	@Modifying
	@Query(value="Update "+RestConstants.SCHEMA_NAME+"."+"mst_bus_tyre_type_size_mapping set status=?1 where id =?2",nativeQuery=true)
	int updateStatusFlag(Boolean flag,Integer id);

}
