package com.idms.base.api.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.ConductorAndDriverBlockHistoryDto;
import com.idms.base.api.v1.model.dto.ConductorBlockedHistoryDto;
import com.idms.base.api.v1.model.dto.ConductorFormLoadDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DriverBlockHistoryDto;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.service.ConductorMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/conductor")
@Log4j2
public class ConductorMasterController {

	@Autowired
	ConductorMasterService service;

	@Autowired
	private ModelMapper mapper;

	@ApiOperation("Returns Object of  Conductor Master When Load")
	@GetMapping(path = "/conductorMasterFormOnLoad/{dpCode}")
	public ConductorFormLoadDto conductorMasterFormOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into conductorMasterFormOnLoad service");
		ConductorFormLoadDto conductorFormLoadDto = this.service.conductorMasterFormOnLoad(dpCode);
		return conductorFormLoadDto;
	}

	@ApiOperation("Update the status of specific conductor master by its identifier. ")
	@PutMapping(path = "/updateConductorMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateConductorMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateConductorMasterStatusFlag(id, flag);
	}

	@ApiOperation("Delete the specific driver master by its identifier. ")
	@PutMapping(path = "/deleteConductorMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteConductorrMaster(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateConductorMasterIsDeletedFlag(id, flag);
	}

	@ApiOperation("Creates a new Conductor Master returning status 200 when persisted successfully.")
	@PostMapping("/saveConductorMaster")
	public ResponseEntity<ResponseStatus> saveConductorMaster(@RequestBody ConductorMaster conductorMaster) {
		log.info("Enter into saveConductorMaster service");
		return this.service.saveConductorMaster(conductorMaster);
	}

	@ApiOperation("Returns list of all Conductor Master")
	@GetMapping(path = "/allConductorMaster/{dpCode}")
	public List<ConductorMasterDto> getAllConductorMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllConductorMaster service");
		return this.service.getAllConductorMaster(dpCode).stream()
				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class))
				.collect(Collectors.toList());
	}

	@ApiOperation("Returns list of all Conductor Master")
	@GetMapping(path = "/fetchAllConductorMaster")
	public List<ConductorMasterDto> fetchAllConductorMaster() {
		log.info("Enter into fetchAllConductorMaster service");
		return this.service.fetchAllConductorMaster().stream()
				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class))
				.collect(Collectors.toList());
	}

	@ApiOperation("Returns  Conductor Names")
	@GetMapping(path = "/getconductorNames")
	public ResponseEntity<List<ConductorMaster>> getConductorNames() {
		List<ConductorMaster> conductorNames = service.getConductorNames();
		return new ResponseEntity<List<ConductorMaster>>(conductorNames, HttpStatus.OK);
	}

	@ApiOperation("Update Conductor Master Category By Id.")
	@PutMapping(path = "/updateConductorMasterCategoryById/{conductorId}/{categoryId}")
	public ResponseEntity<ResponseStatus> updateConductorMasterCategoryById(
			@PathVariable("conductorId") Integer conductorId, @PathVariable("categoryId") Integer categoryId) {
		return this.service.updateConductorMasterCategoryById(conductorId, categoryId);
	}

	@ApiOperation("Block Conductor")
	@PutMapping(path = "/blockEntity")
	public ResponseEntity<ResponseStatus> blockConductor(@RequestParam("id") Integer id,
			@RequestParam("reason") String reason, @RequestParam("routeOff") Boolean routeOff,
			@RequestParam("blocked") Boolean blocked,
			@RequestParam("type")String type,@RequestParam("depotCode") String depotCode,@RequestParam("orderNo")String orderNo,@RequestParam("orderDate")String orderDate) throws ParseException {
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
//		return service.blockConductor(id, reason,sdf.parse(fromDate),sdf.parse(toDate),type,depotCode,orderNo,sdf.parse(orderDate));
		return service.newBlockOrRouteOff(reason,id, type, depotCode, routeOff, blocked, orderNo, sdf.parse(orderDate));
	}

	@ApiOperation("unBlock Conductor")
	@PutMapping(path = "/unblockEntity")
	public ResponseEntity<ResponseStatus> unblockConductor(@RequestParam("id") Integer id) {
		return service.unBlock(id);
	}

	@ApiOperation("get Blocked Conductors")
	@GetMapping(path = "/getblockedEntityLists/{depotCode}")
	public List<DriverBlockHistoryDto> getBlockedConductors(@PathVariable String depotCode) {
		return service.findAllrouteOff(depotCode);
	}
	
	@ApiOperation("get Blocked Conductors")
	@GetMapping(path = "/getblockedRouteLists/{depotCode}")
	public List<ConductorAndDriverBlockHistoryDto> getBlockedRoutes(@PathVariable String depotCode) {
		return service.fetchAllBlockedRoutes(depotCode);
	}
	
	@GetMapping(path = "/getUnblockedConductors/{depotCode}")
	public List<ConductorMaster> findUnblockedConductors(@PathVariable String depotCode){
		return service.findUnblockedConductors(depotCode);
	}
}
