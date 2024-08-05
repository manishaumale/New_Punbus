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

import com.idms.base.api.v1.model.dto.ReadingMasterDto;
import com.idms.base.api.v1.model.dto.RecieveDieselFormLoadDto;
import com.idms.base.api.v1.model.dto.RecieveDieselSupplyMasterDto;
import com.idms.base.api.v1.model.dto.TankCurrentValAndCapacityDto;
import com.idms.base.dao.entity.RecieveDieselSupplyMaster;
import com.idms.base.service.RecieveDieselSupplyService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE +"/v1/diesel")
@Log4j2
public class RecieveDieselSupplyMasterController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RecieveDieselSupplyService service;
	
	
	@ApiOperation("Creates a new Recieve Diesel Master returning status 200 when persisted successfully.")
	@PostMapping("/saveRecieveDieselMaster")
	public ResponseEntity<ResponseStatus> saveRecieveDieselMaster(@RequestBody RecieveDieselSupplyMaster recieveDieselSupplyMaster) {
		log.info("Enter into saveRecieveDieselMaster service");
		return this.service.saveRecieveDieselMaster(recieveDieselSupplyMaster);
	}
	
	@ApiOperation("Returns List of All Recieve Diesel Master")
	@GetMapping(path = "/listOfAllRecieveDieselMaster")
	public List<RecieveDieselSupplyMasterDto> listOfAllRecieveDieselMaster() {
		log.info("Enter into listOfAllRecieveDieselMaster service");
		return this.service.listOfAllRecieveDieselMaster().stream()
				.map(diesel -> this.mapper.map(diesel, RecieveDieselSupplyMasterDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns Object of Recieve Diesel  Master When Load")
	@GetMapping(path = "/recieveDieselFormOnLoad/{depotCode}")
	public RecieveDieselFormLoadDto recieveDieselFormOnLoad(@PathVariable("depotCode") String depotCode) {
		log.info("Enter into recieveDieselFormOnLoad service");
		RecieveDieselFormLoadDto recieveDieselFormLoadDto = this.service.recieveDieselFormOnLoad(depotCode);
		return recieveDieselFormLoadDto;
	}
	
	@ApiOperation("Delete the specific Receieve Diesel master by its identifier. ")
	@PutMapping(path = "/deleteReceiveDiesel/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deleteReceiveDiesel(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updatedeleteReceiveDieselMasterIsDeletedFlag(id, flag);
	}
	
	@ApiOperation("Update the status of specific Receive Diesel master by its identifier. ")
	@PutMapping(path = "/updateReceiveDieselMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateReceiveDieselMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateReceiveDieselMasterStatusFlag(id, flag);
	}
	
	@ApiOperation("Returns Current Reading And Capacity")
	@GetMapping(path = "/fetchCurrValAndCapacity/{id}")
	public TankCurrentValAndCapacityDto fetchCurrValAndCapacity(@PathVariable("id") Integer tankId) {
		log.info("Enter into fetchCurrValAndCapacity service");
		TankCurrentValAndCapacityDto tankCurrentValAndCapacityDto = this.service.fetchCurrValAndCapacity(tankId);
		return tankCurrentValAndCapacityDto;
	}
	
	@ApiOperation("Returns Readings And Capacity by tank")
	@GetMapping(path = "/fetchReadingByFuelTank/{id}")
	public List<ReadingMasterDto> getReadingsByTankId(@PathVariable("id") Integer tankId) {
		log.info("Enter into fetchCurrValAndCapacity service");
		List<ReadingMasterDto> readings = this.service.readingByFuelTank(tankId);
		return readings;
	}
	
	/*@ApiOperation("Returns List Of Fuel Tank And Du by Transport")
	@GetMapping(path = "/fetchFuelTankListAndDu/{id}")
	public RecieveDieselFormLoadDto fetchFuelTankListAndDu(@PathVariable("id") Integer transportId) {
		log.info("Enter into fetchFuelTankListAndDu service");
		RecieveDieselFormLoadDto dtoObj = this.service.fetchFuelTankListAndDu(transportId);
		return dtoObj;
	}*/
	
	@ApiOperation("Returns status when validating by tank id and reading id")
	@GetMapping(path = "/validateOnchangeOfReadingMaster/{tankId}/{readingId}")
	public ResponseEntity<ResponseStatus> validateOnchangeOfReadingMaster(@PathVariable("tankId") Integer tankId,@PathVariable("readingId") Integer readingId) {
		log.info("Enter into validateOnchangeOfReadingMaster service");
		return this.service.validateOnchangeOfReadingMaster(tankId, readingId);
	}

}
