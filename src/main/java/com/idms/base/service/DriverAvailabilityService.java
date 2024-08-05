package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.AllDriverOrConductorsByTPTypeDto;
import com.idms.base.api.v1.model.dto.DriverAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.UnavailableDriversDetailsDto;
import com.idms.base.dao.entity.DriverUnavailabilityMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface DriverAvailabilityService {

	DriverAvailabilityFormLoadDto driverAvailabilityMasterFormOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> saveUnavailableDriverMaster(DriverUnavailabilityMaster driverUnavailabilityMaster);

	List<UnavailableDriversDetailsDto> listOfUnavailableDriversOnClick(Integer routeId,Integer transportId, String dpCode);

	List<AllDriverOrConductorsByTPTypeDto> listOfAllDriversOnBasisOfTransportType(String[] groupIds, String dpCode);

	ResponseEntity<ResponseStatus> updateDriverToDate(Integer id, Date readyDate);

	List<UnavailableDriversDetailsDto> listOfAllDriversAndConductors(String depotCode);

	ResponseEntity<ResponseStatus> approveAttendenceStatus(Integer id, String driverConductorFlg);

	ResponseEntity<ResponseStatus> rejectAttendenceStatus(Integer id, String driverConductorFlg);

}
