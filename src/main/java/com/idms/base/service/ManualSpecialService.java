package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.ManualSpecialRotaFormSaveDto;
import com.idms.base.api.v1.model.dto.ManualSpecialRotaFormSaveParentDto;
import com.idms.base.api.v1.model.dto.ManualSpecialRotaPageLoadDto;
import com.idms.base.api.v1.model.dto.RotaRejectParentDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.support.persist.ResponseStatus;

public interface ManualSpecialService {

	List<RouteMasterDto> getSpecialManualRoutesByDeopAndTransUnit(String depoCode, Integer transPortId, Integer routeType);

	ManualSpecialRotaPageLoadDto getAllTripMasterByRouteId(String depotCode, Integer tuId, Integer[] routeId);

	ResponseEntity<ResponseStatus> saveManualSpecialRota(ManualSpecialRotaFormSaveParentDto parentObj);

	List<RouteTypeDto> routeTypeMasterOnLoad();

	ResponseEntity<ResponseStatus> updateManualSpecialRota(List<ManualSpecialRotaFormSaveDto> childObj);

	ResponseEntity<ResponseStatus> approveManualSpecialRota(Integer rotaId);

	ResponseEntity<ResponseStatus> rejectManualSpecialRota(RotaRejectParentDto parentObj);

}
