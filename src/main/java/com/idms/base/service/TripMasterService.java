package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.TripFormLoadDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface TripMasterService {

	TripFormLoadDto tripMasterOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> updateTripMasterStatusFlag(Integer id, Boolean flag);

	List<TripMasterDto> getAllTripMaster(String dpCode);

	ResponseEntity<ResponseStatus> updateTripMasterIsDeletedFlag(Integer id, Boolean flag);

	//ResponseEntity<ResponseStatus> saveTripMaster(TripMaster tripMaster);

	TripFormLoadDto getAllRouteMaster(String dpCode);

	ResponseEntity<Object> getFormLoadData(Integer routeId);

	ResponseEntity<ResponseStatus> saveRouteRotation(RouteMasterDto routeMasterDto);

	ResponseEntity<ResponseStatus> saveTripMaster(RouteMasterDto routeMasterDto);

	RouteMasterDto specificRouteForTrip(Integer id);

}
