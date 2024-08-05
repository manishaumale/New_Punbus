package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.TyreChangeFormDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface TyreChangeService {

	List<BusMaster> getAllBusesForTyreChange(String dpCode);

	TyreChangeFormDto getTyreChangeForm(Integer busId);

	ResponseEntity<ResponseStatus> saveTyreChangeInfo(BusMasterDto busObj);
	
	
	

}
