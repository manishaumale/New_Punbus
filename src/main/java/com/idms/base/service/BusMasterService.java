package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.BusFormLoadDto;
import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.TyrePosition;
import com.idms.base.support.persist.ResponseStatus;

public interface BusMasterService {

	BusFormLoadDto busMasterFormOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> saveBusMaster(BusMaster busMaster);

	ResponseEntity<ResponseStatus> updateBusMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateBusMasterIsDeletedFlag(Integer id, Boolean flag);

	List<BusMasterDto> getAllBusMaster(String dpCode);

	List<TyrePosition> getTyrePositionByCount(Integer id);

	List<BusMasterDto> getAllBusMasterByBusType(Integer busType);
	
	
	String getBusMasterbydate(Integer id);
	
	List<BusMasterDto> getBusRegistrationNumbers(Integer busType);
	
	List<BusTypeDto> getBusTypeName();
	
	public List<BusMasterDto> getPrintSlipBuses(String dpCode);

}
