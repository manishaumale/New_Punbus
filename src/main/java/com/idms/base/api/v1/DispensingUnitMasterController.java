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

import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.DispensingUnitTypeDto;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.service.DispensingUnitMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/dis/unit")
@Log4j2
public class DispensingUnitMasterController {
	
	@Autowired
	DispensingUnitMasterService service;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@ApiOperation("Returns Object of  Dispensing Unit Master When Load")
	@GetMapping(path = "/dispensingUnitMasterFormOnLoad")
	public List<DispensingUnitTypeDto> dispensingUnitMasterFormOnLoad() {
		log.info("Enter into fuelTankMasterFormOnLoad service");
		List<DispensingUnitTypeDto> dtoObjList = this.service.dispensingUnitMasterFormOnLoad();
		return dtoObjList;
	}
	
	@ApiOperation("Creates a new Dispensing Unit Master returning status 200 when persisted successfully.")
	@PostMapping("/saveDispensingUnitMaster")
	public ResponseEntity<ResponseStatus> saveDispensingUnitMaster(@RequestBody DispensingUnitMaster dispensingUnitMaster){
		log.info("Enter into saveDispensingUnitMaster service");
		return this.service.saveDispensingUnitMaster(dispensingUnitMaster);
	}
	
	@ApiOperation("Returns List of All Dispensing Unit Master")
	@GetMapping(path = "/listOfAllDispensingUnitMaster/{depotCode}")
	public List<DispensingUnitMasterDto> listOfAllDispensingUnitMaster(@PathVariable("depotCode") String depotCode) {
		log.info("Enter into listOfAllDispensingUnitMaster service");
		List<DispensingUnitMasterDto> readingList = this.service.listOfAllDispensingUnitMaster(depotCode).stream().map(dispense -> this.mapper.map(dispense, DispensingUnitMasterDto.class))
		.collect(Collectors.toList());
		return readingList;
	}
	
	@ApiOperation("Update the status of specific Dispensing Unit Master by its identifier. ")
	@PutMapping(path = "/updateDispensingUnitMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateDispensingUnitMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateDispensingUnitMasterStatusFlag(id, flag);
	}
	
	@ApiOperation("Delete the specific Dispensing Unit master by its identifier. ")
	@PutMapping(path = "/deleteDispensingUnitMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteDispensingUnitMaster(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.deleteDispensingUnitMaster(id, flag);
	}

}
