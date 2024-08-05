package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.DailyRoasterAutoDto;
import com.idms.base.support.persist.ResponseStatus;

public interface DailyRoasterServiceAuto {

	ResponseEntity<ResponseStatus> generateAutoRoaster(String dpCode, Integer tpId, Date date);

	List<DailyRoasterAutoDto> getGeneratedRoasterList(String dpCode, String tuId);

}
