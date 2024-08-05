package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.InspectionTankDto;
import com.idms.base.dao.entity.TankInspection;
import com.idms.base.support.persist.ResponseStatus;

public interface InspectTankService {

	List<InspectionTankDto> getAllInspections(String dpCode,String userName);

	/*List<FuelTankMaster> getInspectionFormLoad(String dpCode);*/

	ResponseEntity<ResponseStatus> saveTankInspection(TankInspection tankInspection);

	ResponseEntity<Object> getInspectionFormLoad(String dpCode);

}
