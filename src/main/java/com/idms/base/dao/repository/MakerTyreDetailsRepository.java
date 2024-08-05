package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusTyreTypeSizeMapping;
import com.idms.base.dao.entity.MakerTyreDetails;

@Repository
public interface MakerTyreDetailsRepository extends JpaRepository<MakerTyreDetails, Integer>{
	
	@Query(value="SELECT t from MakerTyreDetails t where t.maker.id=?1 and t.tyreType.id=?2 and t.tyreSize.id=?3 ")
	List<BusTyreTypeSizeMapping> findByBusTyreTypeSize(Integer tyreMakerId, Integer tyreTypeId, Integer tyreSizeId);

	@Query(value="SELECT t from MakerTyreDetails t where t.maker.id=?1 and t.tyreType.id=?2 and t.tyreSize.id=?3 ")
	MakerTyreDetails getTyreCostMilage(Integer tyreMakerId, Integer tyreTypeId, Integer tyreSizeId);

}
