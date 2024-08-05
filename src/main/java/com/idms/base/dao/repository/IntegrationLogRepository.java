package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.IntegrationLog;
import com.idms.base.support.rest.RestConstants;


public interface IntegrationLogRepository extends JpaRepository<IntegrationLog, Integer> {

	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+"."+"integration_log il where il.reg_id =?1 and type='VTS-DIESEL' order by il.log_date_time desc")
	public List<IntegrationLog> findByRegIdOrderByDesc(String regId); 
}
