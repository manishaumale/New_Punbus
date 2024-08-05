package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.DashboardDto;
import com.idms.base.service.DashboardService;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/dashboard")
@Log4j2
public class DashboardController {
	
	@Autowired
	DashboardService service;
	
	@ApiOperation("Returns super admin dashboard")
	@GetMapping(path = "/sa/dashboard/{tpGroups}")
	public DashboardDto getSADashboard(@PathVariable("tpGroups") String tpGroups) {
		log.info("Enter into DashboardController");
		DashboardDto dto = new DashboardDto();
		dto = service.getSADashboard(tpGroups);
		return dto;
	}
	
	@ApiOperation("Returns Depot admin dashboard")
	@GetMapping(path = "/da/dashboard/{dpCode}")
	public DashboardDto getDADashboard(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into DashboardController");
		DashboardDto dto = new DashboardDto();
		dto = service.getDADashboard(dpCode);
		return dto;
	}
	
	@ApiOperation("Returns Total Route Details dashboard")
	@GetMapping(path = "/da/dashboard/totalRouteDetails/{dpCode}")
	public DashboardDto totalRouteDetails(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into DashboardController");
		DashboardDto dto = new DashboardDto();
		dto = service.getTotalRouteDetails(dpCode);
		return dto;
	}
	
	@ApiOperation("Returns Total Bus Details dashboard")
	@GetMapping(path = "/da/dashboard/totalBusDetails/{dpCode}/{type}")
	public DashboardDto totalBusDetails(@PathVariable("dpCode") String dpCode ,@PathVariable("type") String type ) {
		log.info("Enter into DashboardController");
		DashboardDto dto = new DashboardDto();
		dto = service.getTotalBusDetails(dpCode,type);
		return dto;
	}
	
	@ApiOperation("Returns all DriverDetails dashboard")
	@GetMapping(path = "/da/dashboard/driver/{depoCode}/{type}")
	public ResponseEntity<List<DashboardDto>> getdriverDetails(@PathVariable("depoCode") String depoCode,@PathVariable("type") String type) {

		 List<DashboardDto> allDriverDetails = service.getAllDriverDetails(depoCode,type);
		return new ResponseEntity<List<DashboardDto>>(allDriverDetails, HttpStatus.OK);

	}
	
	@ApiOperation("Returns all conductorDetails dashboard")
	@GetMapping(path = "/da/dashboard/conductor/{depoCode}/{type}")
	public ResponseEntity<List<DashboardDto>> getconductorDetails(@PathVariable("depoCode") String depoCode,@PathVariable("type") String type) {
      List<DashboardDto> allConductorDetails = service.getAllConductorDetails(depoCode,type);
		return new ResponseEntity<List<DashboardDto>>(allConductorDetails, HttpStatus.OK);

	}
	
	

}
