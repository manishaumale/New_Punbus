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

import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.TripFormLoadDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.TripMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/trip")
@Log4j2
public class TripMasterController {

	@Autowired
	CityMasterRepository cityMasterRepository;

	@Autowired
	TransportUnitRepository transportUnitRepository;

	@Autowired
	TripMasterService service;
	
	@Autowired
	private ModelMapper mapper;

	
	
//	@ApiOperation("Returns Object of  Trip Master When Load")
//	@GetMapping(path = "/tripMasterOnLoad")
//	public TripFormLoadDto tripMasterOnLoad() {
//		log.info("Enter into tripMasterOnLoad service");
//		TripFormLoadDto tripFormLoadDto = this.service.tripMasterOnLoad();
//		return tripFormLoadDto;
//
//	}
	
	@ApiOperation("Update the status of specific trip master by its identifier. ")
	@PutMapping(path = "/updateTripMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateTripMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateTripMasterStatusFlag(id, flag);
	}
	
	@ApiOperation("Returns list of all Trip Master")
	@GetMapping(path = "/allTripMaster/{dpCode}")
	public List<TripMasterDto> getAllTripMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllTripMaster service");
		List<TripMasterDto> tmd = service.getAllTripMaster(dpCode);
//		return this.service.getAllTripMaster(dpCode).stream()
//				.map(tripMasterDto -> this.mapper.map(tripMasterDto, TripMasterDto.class)).collect(Collectors.toList());
		return tmd;
	}
	
	@ApiOperation("Delete the specific trip master by its identifier. ")
	@PutMapping(path = "/deleteTripMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteTripMaster(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateTripMasterIsDeletedFlag(id, flag);
	}
	// Needto be change
//	@ApiOperation("Creates a new Trip Master returning status 200 when persisted successfully.")
//	@PostMapping("/saveTripMaster")
//	public ResponseEntity<ResponseStatus> saveTripMaster(@RequestBody TripMaster tripMaster) {
//		log.info("Enter into saveTripMaster service");
//		return this.service.saveTripMaster(tripMaster);
//	}
	
	@ApiOperation("Returns list of all RouteMaster")
	@GetMapping(path = "/fetchAllRoutes/{dpCode}")
	public TripFormLoadDto getAllRouteMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllRouteMaster service");
		return this.service.getAllRouteMaster(dpCode);
	}
	
	@ApiOperation("Returns list of all RouteMaster")
	@GetMapping(path = "/onRouteChangeById/{id}")
	public ResponseEntity<Object> onRouteChangeById(@PathVariable("id") Integer id) {
		log.info("Enter into onRouteChangeById service");
		ResponseEntity<Object> tripFormLoadDto = this.service.getFormLoadData(id);
		return tripFormLoadDto;
	}
	
	@ApiOperation("Creates a new RouteRotation returning status 200 when persisted successfully.")
	@PostMapping("/saveRouteRotation")
	public ResponseEntity<ResponseStatus> saveRouteRotation(@RequestBody RouteMasterDto routeMasterDto) {
		log.info("Enter into saveRouteRotation service");
		return this.service.saveRouteRotation(routeMasterDto);
	}
	
	@ApiOperation("Creates a new Trip Master returning status 200 when persisted successfully.")
	@PostMapping("/saveTripMaster")
	public ResponseEntity<ResponseStatus> saveTripMaster(@RequestBody RouteMasterDto routeMasterDto) {
		log.info("Enter into saveTripMaster service");
		return this.service.saveTripMaster(routeMasterDto);
	}
	
	@ApiOperation("Returns a specific Route by their identifier for Trip. 404 if does not exist.")
	@GetMapping(path = "/specificRouteForTrip/{id}")
	public RouteMasterDto specificRouteForTrip(@PathVariable("id") Integer id) {
		RouteMasterDto dto = this.service.specificRouteForTrip(id);
		return dto;
	}
	
}
