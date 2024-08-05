package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.ManualSpecialRotaFormSaveDto;
import com.idms.base.api.v1.model.dto.ManualSpecialRotaFormSaveParentDto;
import com.idms.base.api.v1.model.dto.ManualSpecialRotaPageLoadDto;
import com.idms.base.api.v1.model.dto.RotaRejectParentDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.service.ManualSpecialService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/manualSpecial/Rota")
@Log4j2
public class ManualSpecialRotaController {

	@Autowired
	private ManualSpecialService manualSpecialService;
	
	@ApiOperation("Get Special Routes or Manual Routes By Depot,TransportId and Route Type")
	@GetMapping(value = "/getSpecialManualRoutesByDeopAndTransUnit/{depoCode}/{transPortId}/{routeType}")
	public List<RouteMasterDto> getSpecialManualRoutesByDeopAndTransUnit(@PathVariable("depoCode") String depoCode,
			@PathVariable("transPortId") Integer transPortId, @PathVariable("routeType") Integer routeType) {

		List<RouteMasterDto> rmd = manualSpecialService.getSpecialManualRoutesByDeopAndTransUnit(depoCode, transPortId, routeType);

		return rmd;
	}
	
	@GetMapping(path = "/allTripMasterByRouteId/{depoCode}/{transPortId}/{routeIds}")
	public ManualSpecialRotaPageLoadDto allTripMasterByRouteId(@PathVariable("depoCode") String depoCode,
			@PathVariable("transPortId") Integer transPortId,@PathVariable("routeIds") Integer[] routeIds) {
		ManualSpecialRotaPageLoadDto tmd = manualSpecialService.getAllTripMasterByRouteId(depoCode,transPortId,routeIds);
		return tmd;
	}
	
	@ApiOperation("Saves Manual And Special Rota returning status 200 when persisted successfully.")
	@PostMapping("/saveManualSpecialRota")
	public ResponseEntity<ResponseStatus> saveManualSpecialRota(@RequestBody ManualSpecialRotaFormSaveParentDto parentObj) {
		log.info("Enter into saveManualSpecialRota service");
		return this.manualSpecialService.saveManualSpecialRota(parentObj);
	}
	
	@ApiOperation("Returns Object of  Route Type Master When Load")
	@GetMapping(path = "/routeTypeMasterOnLoad")
	public List<RouteTypeDto> routeTypeMasterOnLoad() {
		log.info("Enter into getrouteMasterOnLoad service");
		List<RouteTypeDto> routeTypeDto = this.manualSpecialService.routeTypeMasterOnLoad();
		return routeTypeDto;

	}
	
	@ApiOperation("Updates Manual And Special Rota returning status 200 when updated successfully.")
	@PostMapping("/updateManualSpecialRota")
	public ResponseEntity<ResponseStatus> updateManualSpecialRota(@RequestBody List<ManualSpecialRotaFormSaveDto> childObjList) {
		log.info("Enter into updateManualSpecialRota service");
		return this.manualSpecialService.updateManualSpecialRota(childObjList);
	}
	
	@ApiOperation("Approve Manual And Special Rota returning status 200 when persisted successfully.")
	@GetMapping("/approveManualSpecialRota/{rotaId}")
	public ResponseEntity<ResponseStatus> approveManualSpecialRota(@PathVariable("rotaId") Integer rotaId) {
		log.info("Enter into approveManualSpecialRota service");
		return this.manualSpecialService.approveManualSpecialRota(rotaId);
	}
	
	@ApiOperation("Reject Manual And Special Rota returning status 200 when persisted successfully.")
	@PostMapping("/rejectManualSpecialRota")
	public ResponseEntity<ResponseStatus> rejectManualSpecialRota(@RequestBody RotaRejectParentDto parentObj) {
		log.info("Enter into rejectManualSpecialRota service");
		return this.manualSpecialService.rejectManualSpecialRota(parentObj);
	}
}
