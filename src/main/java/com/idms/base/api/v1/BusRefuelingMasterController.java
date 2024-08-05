package com.idms.base.api.v1;

import java.text.ParseException;
import java.util.List;

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
import com.idms.base.api.v1.model.dto.BusDieselCorrectionDto;
import com.idms.base.api.v1.model.dto.BusRefuelingListDto;
import com.idms.base.api.v1.model.dto.BusRefuelingListDtoParent;
import com.idms.base.api.v1.model.dto.DailyRosterViewOnlyDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.RefuelingViewDto;
import com.idms.base.dao.entity.BusRefuelingMaster;
import com.idms.base.service.BusRefuelingMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/bus/refueling")
@Log4j2
public class BusRefuelingMasterController {
	
	
	@Autowired
	private BusRefuelingMasterService service;
	
	
	@Autowired
	private ModelMapper mapper;
	
	@ApiOperation("Returns Object of  Bus Refueling Master When Load")
	@GetMapping(path = "/busRefuelingMasterOnLoad/{depotCode}")
	public ResponseEntity<Object> busRefuelingMasterOnLoad(@PathVariable("depotCode") String depotCode) {
		log.info("Enter into busRefuelingMasterOnLoad service");
		ResponseEntity<Object> busRefuelingFormLoadDto = this.service.busRefuelingMasterOnLoad(depotCode);
		return busRefuelingFormLoadDto;

	}
	
	@ApiOperation("Returns object of daily Roaster Master")
	@GetMapping(path = "/fetchRosterDetailsByBusId/{id}/{reason}")
	public DailyRosterViewOnlyDto fetchRosterDetailsByBusId(@PathVariable("id") Integer busId,@PathVariable("reason") Integer reasonId) {
		log.info("Enter into fetchRosterDetailsByBusId service");
		DailyRosterViewOnlyDto dailyRosterViewOnlyDto = this.service.fetchRosterDetailsByBusId(busId,reasonId);
		  return dailyRosterViewOnlyDto;
	}
	
	@ApiOperation("Creates a new Bus Refueling returning status 200 when persisted successfully.")
	@PostMapping("/saveBusRefuelingMaster")
	public ResponseEntity<ResponseStatus> saveBusRefuelingMaster(@RequestParam ("busRefuelingMaster") String busRefuelingMaster,
			@RequestParam(value="uploadFile", required=false) MultipartFile uploadFile,@RequestParam(value="mobilOilBillFile", required=false) MultipartFile mobilOilBillFile,
			@RequestParam(value="redBlueBillFile", required=false) MultipartFile redBlueBillFile) throws JsonMappingException, JsonProcessingException  {
		log.info("Enter into saveBusRefuelingMaster service");
		BusRefuelingMaster  refuelingMaster = new ObjectMapper().readValue(busRefuelingMaster, BusRefuelingMaster.class);
		return this.service.saveBusRefuelingMaster(refuelingMaster,uploadFile,mobilOilBillFile,redBlueBillFile);
	}
	
	@ApiOperation("Returns DU start reading and current reading")
	@GetMapping(path = "/fetchDispensingDataByDuId/{id}")
	public DispensingUnitMasterDto fetchDispensingDataByDuId(@PathVariable("id") Integer dispensingId) {
		log.info("Enter into fetchDispensingDataByDuId service");
		DispensingUnitMasterDto dispensingUnitMasterDto = this.service.fetchDispensingDataByDuId(dispensingId);
		  return dispensingUnitMasterDto;
	}
	
	@ApiOperation("Returns list of all Bus Refueling Master")
	@GetMapping(path = "/allBusRefuelingMaster/{dpCode}")
	public BusRefuelingListDtoParent getAllBusRefuelingMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllBusRefuelingMaster service");
		BusRefuelingListDtoParent refuelingObj = this.service.getBusRefuelingListByDepot(dpCode);
		return refuelingObj;
	}
	
	@ApiOperation("Returns list of all Bus Refueling Master")
	@GetMapping(path = "/busRefuelingById/{id}")
	public RefuelingViewDto getBusRefuelingById(@PathVariable("id") Integer id) {
		log.info("Enter into getBusRefuelingById service");
		RefuelingViewDto busRefuelingDto = this.service.getBusRefuelingById(id);
		return busRefuelingDto;
	}
	
	@ApiOperation("Returns bus diesel correction information")
	@GetMapping(path = "/busDieselCorrection/{busId}/{refuelingDate}")
	public ResponseEntity<BusDieselCorrectionDto> getDieselcorrection(@PathVariable("busId") Integer busId,
			@PathVariable("refuelingDate") String refuelingDate) throws ParseException {
		return service.getBusfuelingCorrectionData(busId, refuelingDate);
	}
	
	@ApiOperation("Accepts bus diesel correction value and returns boolean value")
	@PostMapping(path = "/postBusDieselCorrection")
	public ResponseEntity<Boolean> postDieselCorrection(@RequestBody BusDieselCorrectionDto input) {	
		return service.postBusfuelingCorrectionData(input);
	}
	
	@ApiOperation("Returns All bus diesel correction information")
	@GetMapping(path = "/getAllBusDieselCorrection")
	public List<BusDieselCorrectionDto> getAllDieselCorrection() {	
		return service.getAllBusfuelingCorrectionData();
	}
	
	/*@ApiOperation("Updates or partially update Recieve and Update Supply when transfer")
	@PostMapping(path = "/transferDieselFromOneToAnother")
	public ResponseEntity<ResponseStatus> transferDieselFromOneToAnother(@RequestBody TransferDieselFromOneToAnotherDto transferDto) {
		log.info("Enter into transferDieselFromOneToAnother service");
		  return this.service.transferDieselFromOneToAnother(transferDto);
	}*/
	
	/*@ApiOperation("Returns All Records of transfer")
	@GetMapping(path = "/getAllTransferedRecords")
	public List<TransferDieselFromOneToAnotherDto> getAllTransferedRecords() {	
		return service.getAllTransferedRecords();
	}*/
	
	@ApiOperation("Delete the specific Bus Refuelling by its identifier. ")
	@PutMapping(path = "/deleteBusRefuellingByStatusFlag/{id}")
	public ResponseEntity<ResponseStatus> deleteBusRefuellingByStatusFlag(@PathVariable("id") Integer id) {
		return this.service.deleteBusRefuellingByStatusFlag(id);
	}

}
