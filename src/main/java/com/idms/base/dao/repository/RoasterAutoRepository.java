package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.RoasterAuto;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.support.rest.RestConstants;


@Repository
public interface RoasterAutoRepository extends JpaRepository<RoasterAuto, Integer>{


	
	@Query(value="select * from "+RestConstants.SCHEMA_NAME+"."+"roaster_auto r where date_trunc('DAY',rota_date) = date_trunc('DAY', now()) + INTERVAL '1 DAY' and r.depot_id=?1 and r.transport_id =?2", nativeQuery=true)
	RoasterAuto findByDate(Integer id, Integer transportId);
	
	@Query(value = "SELECT r from RoasterAuto r where r.depot.depotCode = ?1 and r.transport.id = ?2")
	List<RoasterAuto> findAllRotaByDepot(String dpCode,Integer transportId);
	


}

