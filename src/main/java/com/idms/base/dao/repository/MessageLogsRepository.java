package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.MessageLogs;
import com.idms.base.support.rest.RestConstants;

public interface MessageLogsRepository extends JpaRepository<MessageLogs , Integer>{
	

	
	@Query(value="select * from "+RestConstants.SCHEMA_NAME+"."+"message_logs mlm where mlm.fuel_tank_id=?1 and mlm.depot_id=?2 "
			+ "and to_date(to_char(mlm.log_date , 'YYYY-MM-DD'), 'YYYY-MM-DD') =?3 ",nativeQuery=true)
	List<Object[]> findByMessageDtls(Integer fueltankId,Integer depotId,Date date);

}
