package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.AllBusesByTransportTypeDto;
import com.idms.base.api.v1.model.dto.BusAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.UnavailableBusesDetailsDto;
import com.idms.base.dao.entity.BusUnavailabilityMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface BusAvailabilityService {

	BusAvailabilityFormLoadDto busAvailabilityMasterFormOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> saveUnavailableBusMaster(BusUnavailabilityMaster busUnavailabilityMaster);

	List<UnavailableBusesDetailsDto> listOfUnavailableBusesOnClick(Integer busTypeId, Integer transportId, String dpCode);

	List<AllBusesByTransportTypeDto> listOfAllBusesOnBasisOfTransportType(String[] groupIds, String dpCode);

	ResponseEntity<ResponseStatus> updateBusLikelyReadyDate(Integer id, Date readyDate);

}
