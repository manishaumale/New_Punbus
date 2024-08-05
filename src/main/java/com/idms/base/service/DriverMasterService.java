package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.DriverFormLoadDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface DriverMasterService {

	DriverFormLoadDto driverMasterFormOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> updateDriverMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateDriverMasterIsDeletedFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> saveDriverMaster(DriverMaster driverMaster);

	List<DriverMaster> getAllDriverMaster(String dpCode);

	List<DriverMaster> fetchAllDriverMaster();

	ResponseEntity<ResponseStatus> updateDriverMasterCategoryById(Integer driverId, Integer categoryId);
	
	public List<DriverMaster> findAllunBlockedDrivers(String depotCode);


}
