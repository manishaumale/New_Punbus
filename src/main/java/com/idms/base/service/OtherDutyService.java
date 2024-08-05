package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.OtherDutyOnLoadDto;
import com.idms.base.dao.entity.OtherDuty;
import com.idms.base.support.persist.ResponseStatus;

public interface OtherDutyService {

	OtherDutyOnLoadDto otherDutyOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> saveOtherDuty(OtherDuty otherDuty);

	List<OtherDuty> listOfAllOtherDuty();

	ResponseEntity<ResponseStatus> fetchAvailabilityStatus(String driverFlag, Integer driverOrConductorId);

}
