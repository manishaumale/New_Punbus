package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.AllRotaListViewDto;
import com.idms.base.api.v1.model.dto.FormRotaListDto;
import com.idms.base.api.v1.model.dto.RoasterDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.support.persist.ResponseStatus;

public interface DailyRoasterService {

	List<RouteTypeDto> getTyreOfRoutes();

	List<RouteMasterDto> getRouteListWithTypeId(String dpCode, Integer typeId);

	List<TripMasterDto> getTripTimeList(Integer routeId);

	FormRotaListDto generateAutoRoaster(String dpCode, Integer tpId, Date rotaDate);

	List<TransportDto> getTransportUnits(String dpCode);

	ResponseEntity<ResponseStatus> saveDailyRoaster(RoasterDto roasterDto);

	List<RoasterDto> getGeneratedRoasterList(String dpCode);

	AllRotaListViewDto getGeneratedRoasterDetail(Integer rotaId);

	ResponseEntity<ResponseStatus> deleteDailyRoasterId(Integer dailyRotaId);

	ResponseEntity<ResponseStatus> saveSingleDailyRoaster(RoasterDto roasterDto);

	FormRotaListDto generateAutoRoasterByRotaId(Integer rotaId);

}
