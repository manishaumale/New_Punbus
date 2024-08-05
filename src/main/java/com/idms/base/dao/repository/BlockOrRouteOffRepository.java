package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.BlockOrRouteOffEntity;
import com.idms.base.support.rest.RestConstants;

public interface BlockOrRouteOffRepository extends JpaRepository<BlockOrRouteOffEntity, Integer>{

	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".block_route_off where depot_id=?1 order by updated_on desc")
	List<BlockOrRouteOffEntity> findAllByDepot(Integer depotId);
}
