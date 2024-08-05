package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.AllDriverOrConductorsByTPTypeDto;
import com.idms.base.api.v1.model.dto.ConductorAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.UnavailableConductorsDetailsDto;
import com.idms.base.dao.entity.ConductorUnavailabilityMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface ConductorAvailabilityService {

	ConductorAvailabilityFormLoadDto conductorAvailabilityMasterFormOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> saveUnavailableConductorMaster(
			ConductorUnavailabilityMaster conductorUnavailabilityMaster);

	List<UnavailableConductorsDetailsDto> listOfUnavailableConductorsOnClick(Integer employmentId,Integer transportId, String dpCode);

	List<AllDriverOrConductorsByTPTypeDto> listOfAllConductorsOnBasisOfTransportType(String[] groupIds, String dpCode);

	ResponseEntity<ResponseStatus> updateConductorToDate(Integer id, Date readyDate);

}
