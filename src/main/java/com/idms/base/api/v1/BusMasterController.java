package com.idms.base.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.BusFormLoadDto;
import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.TyrePositionDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.BusMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/bus")
@Log4j2
public class BusMasterController {

	@Autowired
	TransportUnitRepository transportUnitRepository;

	@Autowired
	BusMasterService service;

	@Autowired
	private ModelMapper mapper;

	@ApiOperation("Returns Object of  Bus Master When Load")
	@GetMapping(path = "/busMasterFormOnLoad/{dpCode}")
	public BusFormLoadDto busMasterFormOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into busMasterFormOnLoad service");
		BusFormLoadDto busMasterFormOnLoad = this.service.busMasterFormOnLoad(dpCode);
		return busMasterFormOnLoad;
	}

	@ApiOperation("Creates a new Bus Master returning status 200 when persisted successfully.")
	@PostMapping("/saveBusMaster")
	public ResponseEntity<ResponseStatus> saveBusMaster(@RequestBody BusMaster busMaster) {
		log.info("Enter into saveBusMaster service");
		return this.service.saveBusMaster(busMaster);
	}

	@ApiOperation("Update the status of specific bus master by its identifier. ")
	@PutMapping(path = "/updateBusMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateBusMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateBusMasterStatusFlag(id, flag);
	}

	@ApiOperation("Delete the specific bus master by its identifier. ")
	@PutMapping(path = "/deleteBusMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteBusMaster(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateBusMasterIsDeletedFlag(id, flag);
	}

	@ApiOperation("Returns list of all Bus Master")
	@GetMapping(path = "/allBusMaster/{dpCode}")
	public List<BusMasterDto> getAllBusMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllBusMaster service");
		return this.service.getAllBusMaster(dpCode);
	}
	
	@ApiOperation("Returns list of all Bus Master")
	@GetMapping(path = "/allBusMaster1}")
	public List<BusMasterDto> getAllBusMaster1(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllBusMaster service");
		return this.service.getAllBusMaster(dpCode);
	}

	@ApiOperation("Returns list of Tyre Position as per tyre count")
	@GetMapping(path = "/allTyrePositionsByCount/{countId}")
	public List<TyrePositionDto> getTyrePositionByCount(@PathVariable("countId") Integer countId) {
		log.info("Return list of tyre positions as per tyre count");
		return this.service.getTyrePositionByCount(countId).stream()
				.map(tyrePosition -> this.mapper.map(tyrePosition, TyrePositionDto.class)).collect(Collectors.toList());
	}

	@ApiOperation("Returns list of all Bus Master By Bus Type")
	@GetMapping(path = "/allBusMasterByType/{busType}")
	public List<BusMasterDto> getAllBusMasterByBusType(@PathVariable("busType") Integer busType) {
		log.info("Enter into getAllBusMasterByBusType service");
		return this.service.getAllBusMasterByBusType(busType);
	}

	@PutMapping(path = "/updatebusSubType/{id}")
	public ResponseEntity<String> updateBusSubTypeName(@PathVariable("id") Integer id) {
		String busMasterbydate = service.getBusMasterbydate(id);
		return new ResponseEntity<String>(busMasterbydate, HttpStatus.OK);

	}

	@GetMapping(path = "/getbusregistrationNumbers/{busType}")
	public ResponseEntity<List<BusMasterDto>> getBusRegistrationNumbers(@PathVariable("busType") Integer busType) {

		List<BusMasterDto> busRegistrationNumbers = service.getBusRegistrationNumbers(busType);
		return new ResponseEntity<List<BusMasterDto>>(busRegistrationNumbers, HttpStatus.OK);

	}

	@GetMapping(path = "/getbusTypeName")
	public ResponseEntity<List<BusTypeDto>> getBusTypeName() {

		List<BusTypeDto> busTypeName = service.getBusTypeName();
		return new ResponseEntity<List<BusTypeDto>>(busTypeName, HttpStatus.OK);

	}
	
	@GetMapping("/getPrintSlipBuses/{dpCode}")
	public List<BusMasterDto> getAllPrintSlipBuses(@PathVariable("dpCode") String dpCode) {
		return service.getPrintSlipBuses(dpCode);
	}

}
