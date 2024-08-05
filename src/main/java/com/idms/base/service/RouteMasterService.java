package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.AuthorizeRouteDto;
import com.idms.base.api.v1.model.dto.PermitDetailsDto;
import com.idms.base.api.v1.model.dto.PermitNumberIdDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.RouteFormLoadDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface RouteMasterService {

	RouteFormLoadDto permitDetailsMasterLoad(String dpCode);

	ResponseEntity<ResponseStatus> saveRouteMaster(RouteMasterDto routeMaster);

	List<RouteMaster> getAllRouteMaster();

	ResponseEntity<ResponseStatus> updateRouteMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateRouteMasterIsDeletedFlag(Integer id, Boolean flag);

	RouteMasterDto findByRouteId(Integer id);

	List<RouteMaster> getAllRouteMasterByDepot(String dpCode);
	
	List<RouteMaster> getAllRouteMasterByDriverId(Integer dpCode,boolean status , boolean isdeleted);

	List<PermitDetailsMaster> getPermitsForTransportAndDepot(Integer dpId, Integer tpId);

	List<RouteMaster> getListRouteMaster();

	List<PermitNumberIdDto> permitListByTransportAndDepotFilterByRoute(Integer routeId);

	List<RouteMasterDto> getAllRouteMasterByDepoCode(String dpCode);

	List<PermitDetailsDto> permitList(String dpCode);

	Integer checkPermitValidation(Integer permitId);

	ResponseEntity<ResponseStatus> updateRouteMasterCategoryById(Integer routeId, Integer categoryId);

	List<RouteCategoryDto> routeCategoryMasterOnLoad();

	List<TransportDto> allTransportMasterByDepot(String dpCode);

	List<AuthorizeRouteDto> getDropdownList(Integer transportId);

	ResponseEntity<ResponseStatus> saveAuthorizeRoute(List<AuthorizeRouteDto> authorizeRouteDto);

	List<AuthorizeRouteDto> onLoadAuthorizeRoute(String dpCode);

	ResponseEntity<ResponseStatus> updateAuthorizeRoute(List<AuthorizeRouteDto> authorizeRoute);

	List<AuthorizeRouteDto> getAuthorizeRouteOnId(Integer authorizeRouteId);

	ResponseEntity<ResponseStatus> updateAuthorizeRouteIsDeletedFlag(Integer id, Boolean flag);

	List<TripMasterDto> getAllTripByRouteId(Integer routeId);

}
