package com.idms.base.api.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idms.base.api.v1.model.dto.AlertDto;
import com.idms.base.api.v1.model.dto.DipChartReadingsDto;
import com.idms.base.api.v1.model.dto.ExcessShortDto;
import com.idms.base.api.v1.model.dto.FuelTankFormLoadDto;
import com.idms.base.api.v1.model.dto.FuelTankMasterDto;
import com.idms.base.api.v1.model.dto.TankCapacityMasterDto;
import com.idms.base.dao.entity.DipChartReadings;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.TankCapacityMaster;
import com.idms.base.service.FuelTankMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/fuel/tank")
@Log4j2
public class FuelTankMasterController {
	
	@Autowired
	FuelTankMasterService service;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@ApiOperation("Returns Object of  Fuel Tank  Master When Load")
	@GetMapping(path = "/fuelTankMasterFormOnLoad")
	public FuelTankFormLoadDto fuelTankMasterFormOnLoad() {
		log.info("Enter into fuelTankMasterFormOnLoad service");
		FuelTankFormLoadDto fuelTankFormLoadDto = this.service.fuelTankMasterFormOnLoad();
		return fuelTankFormLoadDto;
	}
	
	@ApiOperation("Creates a new Fuel Tank Master returning status 200 when persisted successfully.")
	@PostMapping("/saveFuelTankMaster")
	public ResponseEntity<ResponseStatus> saveFuelTankMaster(@RequestParam ("fuelTankMaster") String fuelTankMaster,@RequestParam("uploadFile") MultipartFile uploadFile) throws JsonMappingException, JsonProcessingException {
		log.info("Enter into saveFuelTankMaster service");
		FuelTankMaster  fuelTank = new ObjectMapper().readValue(fuelTankMaster, FuelTankMaster.class);
		return this.service.saveFuelTankMaster(fuelTank,uploadFile);
	}
	
	@ApiOperation("Update the status of specific Fuel Tank Master by its identifier. ")
	@PutMapping(path = "/updateFuelTankMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateFuelTankMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateFuelTankMasterStatusFlag(id, flag);
	}
	
	@ApiOperation("Returns List of All Fuel Tank Master")
	@GetMapping(path = "/listOfAllFuelTankMaster/{depotCode}")
	public List<FuelTankMasterDto> listOfAllFuelTankMaster(@PathVariable("depotCode") String  depotCode) {
		log.info("Enter into listOfAllFuelTankMaster service");
		List<FuelTankMasterDto> fuelTankList = this.service.listOfAllFuelTankMaster(depotCode).stream().map(fuelTank -> this.mapper.map(fuelTank, FuelTankMasterDto.class))
		.collect(Collectors.toList());
		return fuelTankList;
	}
	
	@PostMapping("/updateExplosiveLicense")
	public ResponseEntity<ResponseStatus> updateExplosiveLicense(@RequestParam("id") Integer id,@RequestParam("explosiveRenewalDate") String explosiveRenewalDate,@RequestParam("uploadFile") MultipartFile uploadFile) throws ParseException {
		SimpleDateFormat sd= new SimpleDateFormat("yyyy-MM-dd");
		Date d=sd.parse(explosiveRenewalDate);
		return service.updateExplosiveLicense(id, d, uploadFile);
	}
	
	@GetMapping("/fuelTankExplosiveAlerts")
	public List<AlertDto> getAlerts() throws ParseException {
		return service.explosiveCertificateExpiry();
	}
	
	@PostMapping("/dipChartReadings")
	public ResponseEntity<ResponseStatus> saveDipChartReadings(@RequestBody DipChartReadingsDto input) {
		return service.saveDipChartReadings(input);
	}
	
	@PostMapping("/addTankCapacityMaster")
	public ResponseEntity<ResponseStatus> addTankCapacity(@RequestBody TankCapacityMasterDto input) {
		return service.addTankCapacity(input);
	}
	
	@GetMapping("/getTankCapacityList")
	public List<TankCapacityMasterDto> getTankCapacityMaster() {
		return service.findAllTankCapacityMaster();
	}
	
	@PostMapping("/updateCleaningDetails")
	public ResponseEntity<ResponseStatus> updateCleaningDetails(@RequestParam("id") Integer id,@RequestParam("cleaningDate") String cleaningRenewDate) throws ParseException{
		SimpleDateFormat sd= new SimpleDateFormat("yyyy-MM-dd");
		Date d=sd.parse(cleaningRenewDate);
		return service.updateCleaningDate(id, d);
	}
	
	@GetMapping("/listOfDipChartReadings/{userName}/{depot}")
	public List<DipChartReadingsDto> getAllDipChartReadings(@PathVariable("userName")String userName,@PathVariable("depot")String depot) {
		return service.getAllDipChartReadings(userName, depot);
	}
	
	@GetMapping("/dipChartExcessVariation")
	public ExcessShortDto calculateExcessShort(@RequestParam("id") Integer tankId,@RequestParam("volume") Double volume) {
		return service.calculateExcessShort(tankId, volume);
	}
	
	@GetMapping("/evengDipChartExcessVariation")
	public ExcessShortDto calculateExcessShort2(@RequestParam("id") Integer tankId,@RequestParam("volume") Double volume) {
		return service.calculateExcessShort2(tankId, volume);
	}
	
	@ApiOperation("Delete the status of specific Fuel Tank Master by its identifier. ")
	@PutMapping(path = "/deleteFuelTankMasterStatusFlag/{id}")
	public ResponseEntity<ResponseStatus> deleteFuelTankMasterStatusFlag(@PathVariable("id") Integer id) {
		return this.service.deleteFuelTankMasterStatusFlag(id);
		
	}

	@GetMapping("/checkTankCode/{tankCode}")
	public FuelTankMasterDto checkTankCode(@PathVariable("tankCode")String tankCode) {
		return service.checkTankCode(tankCode);
	}
	
	@GetMapping("/mixtureCostCalculation/{tankId}")
	public Float mixtureCostCalculation(@PathVariable("tankId")Integer tankId) {
		return service.mixtureCostCalculation(tankId);
	}
	
	@GetMapping("/viewDipChartObjectById/{id}")
	public DipChartReadingsDto viewDipChartObjectById(@PathVariable("id")Integer id) {
		return service.viewDipChartObjectById(id);
	}
	
	@GetMapping("/validateDuplicateCode/{tankCode}")
	public ResponseEntity<ResponseStatus> validateDuplicateCode(@PathVariable("tankCode")String tankCode) {
		return service.validateDuplicateCode(tankCode);
	}

}
