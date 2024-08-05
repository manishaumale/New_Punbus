package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.MessageTemplates;
import com.idms.base.support.rest.RestConstants;

public interface MesssageTemplatesRepository extends JpaRepository<MessageTemplates , Integer>{

	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+"."+"message_templates where message_code=?1")
	public MessageTemplates findByCode(String code); 
}
