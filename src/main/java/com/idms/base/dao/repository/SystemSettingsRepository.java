package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.SystemSettings;
import com.idms.base.support.rest.RestConstants;

public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Integer> {

	@Query(nativeQuery = true, value = "select key_value from " + RestConstants.SCHEMA_NAME + "."
			+ "mst_system_settings where  key_name ='username' or key_name ='password' or key_name ='secure_id' or key_name ='sender_id' order by id")
	public Object[] findCredentials();
}
