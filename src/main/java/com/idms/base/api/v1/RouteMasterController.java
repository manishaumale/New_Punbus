package com.idms.base.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.AuthorizeRouteDto;
import com.idms.base.api.v1.model.dto.PermitDetailsDto;
import com.idms.base.api.v1.model.dto.PermitNumberIdDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.RouteFormLoadDto;
import com.idms.base.api.v1.model.dto.RouteListDto;
//import com.idms.base.api.v1.model.dto.RouteListDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.dao.repository.BusSubTypeMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.TaxMasterRepository;
import com.idms.base.dao.repository.TaxTypeMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.RouteMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/route")
@Log4j2
public class RouteMasterController {
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private RouteMasterService service;

	@Autowired
	StateMasterRepository stateMasterRepository;

	@Autowired
	CityMasterRepository cityMasterRepository;

	@Autowired
	TransportUnitRepository transportUnitRepository;

	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;

	@Autowired
	BusSubTypeMasterRepository busSubTypeMasterRepository;

	@Autowired
	TaxMasterRepository taxMasterRepository;

	@Autowired
	TaxTypeMasterRepository taxTypeMasterRepository;
	
	
	@ApiOperation("Returns Object of  Route Master When Load")
	@GetMapping(path = "/routeMasterOnLoad/{dpCode}")
	public RouteFormLoadDto routeMasterOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getrouteMasterOnLoad service");
		RouteFormLoadDto routeFormLoadDto = this.service.permitDetailsMasterLoad(dpCode);
		return routeFormLoadDto;

	}
	
	@ApiOperation("Creates a new Route Master returning status 200 when persisted successfully.")
	@PostMapping("/saveRouteMaster")
	public ResponseEntity<ResponseStatus> saveRouteMaster(@RequestBody RouteMasterDto routeMaster) {
		log.info("Enter into saveRouteMaster service");
		return this.service.saveRouteMaster(routeMaster);
	}
	
	@ApiOperation("Returns list of all Route Master")
	@GetMapping(path = "/allRouteMaster/{dpCode}")
	public List<RouteMasterDto> getAllRouteMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllRouteMaster service");
		return this.service.getAllRouteMasterByDepot(dpCode).stream()
				.map(routeMasterDto -> this.mapper.map(routeMasterDto, RouteMasterDto.class)).collect(Collectors.toList());
	}
	
	
	@ApiOperation("Returns list of all Route Master")
	@GetMapping(path = "/allRouteMasterByDriverId/{driverId}/{status}/{isdeleted}")
	public List<RouteMasterDto> getAllRouteMasterByDriverId(@PathVariable("driverId") Integer driverId,@PathVariable("status") boolean status,@PathVariable("isdeleted") boolean isdeleted) {
		log.info("Enter into getAllRouteMaster service");
		return this.service.getAllRouteMasterByDriverId(driverId,status,isdeleted).stream()
				.map(routeMasterDto -> this.mapper.map(routeMasterDto, RouteMasterDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns list of all Route Master")
	@GetMapping(path = "/listRouteMaster")
	public List<RouteListDto> getListRouteMaster() {
		log.info("Enter into getAllRouteMaster service");
		return this.service.getListRouteMaster().stream()
				.map(routeMasterDto -> this.mapper.map(routeMasterDto, RouteListDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Update the status of specific route master by its identifier. ")
	@PutMapping(path = "/updateRouteMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updatePermitMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateRouteMasterStatusFlag(id, flag);
	}
	
	@ApiOperation("Delete the specific route master by its identifier. ")
	@PutMapping(path = "/deleteRouteMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteRouteMaster(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateRouteMasterIsDeletedFlag(id, flag);
	}
	
	@ApiOperation("Returns a specific Route by their identifier. 404 if does not exist.")
	@GetMapping(path = "/specificRoute/{id}")
	public RouteMasterDto getSpecificRoute(@PathVariable("id") Integer id) {
		RouteMasterDto dto = this.service.findByRouteId(id);
		return dto;
	}
	
	@ApiOperation("Returns Object of  Route Master When Load")
	@GetMapping(path = "/permitListByTransportAndDepot/{dpId}/{tpId}")
	public List<PermitDetailsDto> getPermitsForTransportAndDepot(@PathVariable("dpId") Integer dpId, @PathVariable("tpId") Integer tpId) {
		log.info("Enter into getrouteMasterOnLoad service");
		return this.service.getPermitsForTransportAndDepot(dpId, tpId).stream()
				.map(permitDto -> this.mapper.map(permitDto, PermitDetailsDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns permitList By Transport And Depot Filter By Route")
	@GetMapping(path = "/permitListByTransportAndDepotFilterByRoute/{routeId}")
	public List<PermitNumberIdDto> permitListByTransportAndDepotFilterByRoute(@PathVariable("routeId") Integer routeId) {
		log.info("Enter into getrouteMasterOnLoad service");
		return this.service.permitListByTransportAndDepotFilterByRoute(routeId);
	}
	
	//ADDED BY PIYUSH FOR GET ALL ROUTE
	
	@ApiOperation("Returns list of all Route Master By Depo Code")
	@GetMapping(path = "/getAllRouteMasterByDepoCode/{dpCode}")
	public List<RouteMasterDto> getAllRouteMasterByDepoCode(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllRouteMasterByDepoCode service");
		List<RouteMasterDto> dtos = service.getAllRouteMasterByDepoCode(dpCode);	
		return dtos;
	}
	
	@ApiOperation("Returns list of all Permit Details By Status IsDeleted And validUpto")
	@GetMapping(path = "/permitList/{dpCode}")
	public List<PermitDetailsDto> permitList(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllRouteMasterByDepoCode service");
		List<PermitDetailsDto> dtos = service.permitList(dpCode);	
		return dtos;
	}
	
	@ApiOperation("Permit Validatation By ")
	@GetMapping(path = "/checkPermitValidation/{permitId}")
	public Integer checkPermitValidation(@PathVariable("permitId") Integer permitId) {
		log.info("Enter into checkPermitValidation service"); 
		Integer retn = service.checkPermitValidation(permitId);	
		return retn;
	}
	
	@ApiOperation("Update Route Master Category By Id.")
	@PutMapping(path = "/updateRouteMasterCategoryById/{routeId}/{categoryId}")
	public ResponseEntity<ResponseStatus> updateRouteMasterCategoryById(@PathVariable("routeId") Integer routeId,
			@PathVariable("categoryId") Integer categoryId) {
		return this.service.updateRouteMasterCategoryById(routeId, categoryId);
	}
	
	@ApiOperation("Returns List of  Route Category Master When Load")
	@GetMapping(path = "/routeCategoryMasterOnLoad")
	public List<RouteCategoryDto> routeCategoryMasterOnLoad() {
		log.info("Enter into routeCategoryMasterOnLoad service");
		List<RouteCategoryDto> categoryList = this.service.routeCategoryMasterOnLoad();
		return categoryList;

	}
	//changed by Manisha 29/04/2022-> Authorize Route - get transport unit list base on dpcode
	
	@ApiOperation("Returns Tranport Unit list By dpCode")
	@GetMapping(path = "/getTranportUnitList/{dpCode}")
	public List<TransportDto> getTranportUnitList(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getTranportUnitList service");
		List<TransportDto> transportDtoList = service.allTransportMasterByDepot(dpCode);	
		return transportDtoList;
	}
	
	@ApiOperation("Returns Tranport Unit list By dpCode")
	@GetMapping(path = "/getDropdownList/{transportId}")
	public List<AuthorizeRouteDto> getDropdownList(@PathVariable("transportId") Integer transportId) {
		log.info("Enter into getDropdownList service");
		List<AuthorizeRouteDto> dropdownList = service.getDropdownList(transportId);	
		return dropdownList;
	}
	
	@ApiOperation("Create a new Authorize Route returning status 200 when persisted successfully.")
	@PostMapping("/saveAuthorizeRoute")
	public ResponseEntity<ResponseStatus> saveAuthorizeRoute(@RequestBody List<AuthorizeRouteDto> authorizeRoute) {
		log.info("Enter into saveAuthorizeRoute service");
		return this.service.saveAuthorizeRoute(authorizeRoute);
	}
	@ApiOperation("Returns onLoad Authorize Route List By dpCode")
	@GetMapping(path = "/onLoadAuthorizeRoute/{dpCode}")
	public List<AuthorizeRouteDto> onLoadAuthorizeRoute(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into onLoadAuthorizeRoute service");
		List<AuthorizeRouteDto> onLoadAuthRouteList = service.onLoadAuthorizeRoute(dpCode);	
		return onLoadAuthRouteList;
		}
	@ApiOperation("Update Conductor Master Category By Id.")
	@PutMapping(path = "/updateAuthorizeRoute")
	public ResponseEntity<ResponseStatus> updateAuthorizeRoute(@RequestBody List<AuthorizeRouteDto> authorizeRoute) {
		return this.service.updateAuthorizeRoute(authorizeRoute);
	}
	@ApiOperation("Returns onLoad Authorize Route List By authorizeRouteId")
	@GetMapping(path = "/getAuthorizeRouteOnId/{authorizeRouteId}")
	public List<AuthorizeRouteDto> getAuthorizeRouteOnId(@PathVariable("authorizeRouteId") Integer authorizeRouteId) {
		log.info("Enter into getAuthorizeRouteOnId service");
		List<AuthorizeRouteDto> onLoadAuthRouteList = service.getAuthorizeRouteOnId(authorizeRouteId);	
		return onLoadAuthRouteList;
		}
	@ApiOperation("Delete the specific Authorize Route by its identifier. ")
	@PutMapping(path = "/deleteAuthorizeRoute/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteAuthorizeRoute(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateAuthorizeRouteIsDeletedFlag(id, flag);
	}
	@ApiOperation("Returns All Trip By Route Id")
	@GetMapping(path = "/getAllTripByRouteId/{routeId}")
	public List<TripMasterDto> getAllTripByRouteId(@PathVariable("routeId") Integer routeId) {
		log.info("Enter into getAllTripByRouteId service");
		List<TripMasterDto> tripMasterDtoList = service.getAllTripByRouteId(routeId);	
		return tripMasterDtoList;
	}
}