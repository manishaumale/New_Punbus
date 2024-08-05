package com.idms.base.api.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.AllRotaListViewDto;
import com.idms.base.api.v1.model.dto.DailyRoasterAutoDto;
import com.idms.base.api.v1.model.dto.DailyRoasterDto;
import com.idms.base.api.v1.model.dto.FormRotaListDto;
import com.idms.base.api.v1.model.dto.RoasterDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.service.DailyRoasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = RestConstants.API_BASE + "/v1/DailyRoaster")
@Log4j2
public class DailyRoasterController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private DailyRoasterService service;
	
	@ApiOperation("")
	@GetMapping(path = "/getRouteTypes")
	public List<RouteTypeDto> getRouteTypes() {
		log.info("Enter into DailyRoasterController function getRouteTypes");
		List<RouteTypeDto> routeTypes = this.service.getTyreOfRoutes();
		return routeTypes;
	}
	
	@ApiOperation("")
	@GetMapping(path = "/getRouteListWithTypeId/{dpCode}/{typeId}")
	public List<RouteMasterDto> getRouteListWithTypeId(@PathVariable("dpCode") String dpCode, @PathVariable("typeId") Integer typeId) {
		log.info("Enter into DailyRoasterController function getRouteListWithTypeId");
		List<RouteMasterDto> routes = this.service.getRouteListWithTypeId(dpCode, typeId);
		return routes;
	}
	
	@ApiOperation("")
	@GetMapping(path = "/getTripTimeList/{routeId}")
	public List<TripMasterDto> getTripTimeList(@PathVariable("routeId") Integer routeId) {
		log.info("Enter into DailyRoasterController function getTripTimeList");
		List<TripMasterDto> trips = this.service.getTripTimeList(routeId);
		return trips;
	}
	
	@ApiOperation("")
	@GetMapping(path = "/generateRoaster/{dpCode}/{tpId}")
	public FormRotaListDto generateRoasterForm(@PathVariable("dpCode") String dpCode, @PathVariable("tpId") Integer tpId) {
		log.info("Enter into DailyRoasterController function generateRoasterForm");
		return this.service.generateAutoRoaster(dpCode, tpId, this.getDate(1));
		
	}
	
	public Date getDate(Integer i) {
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dt = sdf.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}
	
	@ApiOperation("")
	@GetMapping(path = "/generateAutoRoasterByRotaId/{rotaId}")
	public FormRotaListDto generateRoasterForm( @PathVariable("rotaId") Integer rotaId) {
		log.info("Enter into DailyRoasterController function generateRoasterForm");
		return this.service.generateAutoRoasterByRotaId(rotaId);
		
	}
	
	@ApiOperation("")
	@GetMapping(path = "/getTransportUnits/{dpCode}") 
	public List<TransportDto> getTransportUnits(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into DailyRoasterController function generateRoaster");
		return this.service.getTransportUnits(dpCode);
	}
	
	@ApiOperation("Creates daily roaster returning status 200 when persisted successfully.")
	@PostMapping("/saveDailyRoaster")
	public ResponseEntity<ResponseStatus> saveDailyRoaster(@RequestBody RoasterDto roasterDto) {
		return this.service.saveDailyRoaster(roasterDto);
	}
	
	@ApiOperation("")
	@GetMapping(path = "/getGeneratedRoasterList/{dpCode}") 
	public List<RoasterDto> getGeneratedRoasterList(@PathVariable("dpCode") String dpCode) {
		return this.service.getGeneratedRoasterList(dpCode);
	}
	
	@ApiOperation("")
	@GetMapping(path = "/getGeneratedRoasterDetail/{rotaId}") 
	public AllRotaListViewDto getGeneratedRoasterDetail(@PathVariable("rotaId") Integer rotaId) {
		return this.service.getGeneratedRoasterDetail(rotaId);
	}
	
	@GetMapping(path = "/deleteDailyRoasterId/{dailyRotaId}") 
	public ResponseEntity<ResponseStatus> deleteDailyRoasterId(@PathVariable("dailyRotaId") Integer dailyRotaId) {
		return this.service.deleteDailyRoasterId(dailyRotaId);
	}
	
	@PostMapping("/saveSingleDailyRoaster") 
	public ResponseEntity<ResponseStatus> saveSingleDailyRoaster(@RequestBody RoasterDto roasterDto) {
		return this.service.saveSingleDailyRoaster(roasterDto);
	}

}
