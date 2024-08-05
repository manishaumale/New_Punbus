package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.FuelTankCapacityInDepoDto;
import com.idms.base.service.DepotMasterService;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(path = RestConstants.API_BASE + "/v1/basic/depo")
@Log4j2
public class DepotMasterController {
	@Autowired
	private DepotMasterService service;

	@ApiOperation(" Returns total depo fuel capacity")
	@GetMapping(value = { "/fuelcapacity", "/fuelcapacity/{depotCode}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFuelCapacityInDepo(@PathVariable(value = "depotCode", required = false) String depotCode) {
		log.info("Enter into getFuelCapacityInDepo service");

		if (depotCode != null) {
			FuelTankCapacityInDepoDto FuelCapacityInDepo = service.findFuelCapacityInDepo(depotCode);
			if (FuelCapacityInDepo != null)
				return new ResponseEntity<FuelTankCapacityInDepoDto>(FuelCapacityInDepo, HttpStatus.OK);
		} else{
			List<FuelTankCapacityInDepoDto> fuelCapacityInAllDepos = service.findFuelCapacityInAllDepos();
			return new ResponseEntity<List<FuelTankCapacityInDepoDto>>(fuelCapacityInAllDepos, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}