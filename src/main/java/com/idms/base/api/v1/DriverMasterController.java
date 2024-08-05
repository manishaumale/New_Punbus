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

import com.idms.base.api.v1.model.dto.DriverFormLoadDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.service.DriverMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/driver")
@Log4j2
public class DriverMasterController {
	
	@Autowired
	DriverMasterService service;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@ApiOperation("Returns Object of  Driver Master When Load")
	@GetMapping(path = "/driverMasterFormOnLoad/{dpCode}")
	public DriverFormLoadDto driverMasterFormOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into driverMasterFormOnLoad service");
		DriverFormLoadDto driverMasterFormOnLoad = this.service.driverMasterFormOnLoad(dpCode);
		return driverMasterFormOnLoad;
	}
	
	@ApiOperation("Update the status of specific driver master by its identifier. ")
	@PutMapping(path = "/updateDriverMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateDriverMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateDriverMasterStatusFlag(id, flag);
	}
	
	@ApiOperation("Delete the specific driver master by its identifier. ")
	@PutMapping(path = "/deleteDriverMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteDriverMaster(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateDriverMasterIsDeletedFlag(id, flag);
	}
	
	@ApiOperation("Creates a new Driver Master returning status 200 when persisted successfully.")
	@PostMapping("/saveDriverMaster")
	public ResponseEntity<ResponseStatus> saveDriverMaster(@RequestBody DriverMaster driverMaster) {
		log.info("Enter into saveDriverMaster service");
		return this.service.saveDriverMaster(driverMaster);
	}
	
	@ApiOperation("Returns list of all Driver Master")
	@GetMapping(path = "/allDriverMaster/{dpCode}")
	public List<DriverMasterDto> getAllDriverMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllDriverMaster service");
		return this.service.getAllDriverMaster(dpCode).stream()
				.map(driverMasterDto -> this.mapper.map(driverMasterDto, DriverMasterDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns list of all Driver Master")
	@GetMapping(path = "/fetchAllDriverMaster")
	public List<DriverMasterDto> fetchAllDriverMaster() {
		log.info("Enter into fetchAllDriverMaster service");
		return this.service.fetchAllDriverMaster().stream()
				.map(driverMasterDto -> this.mapper.map(driverMasterDto, DriverMasterDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Update Driver Master Category By Id.")
	@PutMapping(path = "/updateDriverMasterCategoryById/{driverId}/{categoryId}")
	public ResponseEntity<ResponseStatus> updateDriverMasterCategoryById(@PathVariable("driverId") Integer driverId,
			@PathVariable("categoryId") Integer categoryId) {
		return this.service.updateDriverMasterCategoryById(driverId, categoryId);
		
	}
	
	@GetMapping(path = "/unBlockedDrivers/{dpCode}")
	public List<DriverMaster> findAllUnblockedDrivers(@PathVariable("dpCode") String dpCode) {
		return service.findAllunBlockedDrivers(dpCode);
	}

}
