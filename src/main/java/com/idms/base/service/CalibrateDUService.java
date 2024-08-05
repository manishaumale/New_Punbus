package com.idms.base.service;

import java.util.List;

import com.idms.base.dao.entity.DispensingUnitCalibration;
import com.idms.base.dao.entity.DispensingUnitMaster;

public interface CalibrateDUService {

	List<DispensingUnitCalibration> findAllCalibrationsByDepot(String dpCode);

	List<DispensingUnitMaster> getCalibrationFormLoad(String dpCode);

	/*ResponseEntity<ResponseStatus> saveDUCalibration(DUCalbiration duCalibration, MultipartFile uploadFile);*/

}
