package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.OutShedderDto;
import com.idms.base.dao.entity.OutShedder;
import com.idms.base.support.persist.ResponseStatus;

public interface OutShedderService {

	
	List<OutShedderDto> getOutShedder(String dpCode);
	
	ResponseEntity<ResponseStatus> saveOutShedder(OutShedderDto outShedder);
	
	List<OutShedderDto> getOutShedderByBusId(Date date, String busNo);
	
	
}
