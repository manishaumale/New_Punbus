package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.DocketTyreAssociationDto;
import com.idms.base.api.v1.model.dto.RetreadingDocketDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.support.persist.ResponseStatus;

public interface TyreRetradeService {

	List<TyreMasterDto> getAllTyreForResole(String dpCode);

	ResponseEntity<ResponseStatus> markForRetreading(RetreadingDocketDto docketDto);

	ResponseEntity<ResponseStatus> markForCondemnation(RetreadingDocketDto docketDto);

	List<RetreadingDocketDto> getAllDocket(String dpCode);

	List<RetreadingDocketDto> getReceivedDocketList(String dpCode);

	List<DocketTyreAssociationDto> getTyreListForDocket(Integer docketId);

	ResponseEntity<ResponseStatus> saveReceivedDocketList(RetreadingDocketDto docketDto);

	List<TyreMasterDto> getTyreObjForDocket(Integer docketId);

}
