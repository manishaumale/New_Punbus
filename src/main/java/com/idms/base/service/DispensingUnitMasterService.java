package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.DispensingUnitTypeDto;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface DispensingUnitMasterService {

	List<DispensingUnitTypeDto> dispensingUnitMasterFormOnLoad();

	ResponseEntity<ResponseStatus> saveDispensingUnitMaster(DispensingUnitMaster dispensingUnitMaster);

	List<DispensingUnitMaster> listOfAllDispensingUnitMaster(String depotCode);

	ResponseEntity<ResponseStatus> updateDispensingUnitMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> deleteDispensingUnitMaster(Integer id, Boolean flag);

	void updateCurrentReading(Double currentReading, Integer id);

}
