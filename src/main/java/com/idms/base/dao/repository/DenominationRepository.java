package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DenominationEntity;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface DenominationRepository extends JpaRepository<DenominationEntity, Integer>{
	@Query(value="select mtd.denomination_id from "+RestConstants.SCHEMA_NAME+"."+" mst_ticket_denomination mtd  where mtd.denomination=?1 ",nativeQuery=true)
	Integer getDenominationId(Integer denomination);

}
