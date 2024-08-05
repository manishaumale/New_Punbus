package com.idms.base.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idms.base.api.v1.model.dto.CalibrationDispensingUnitDto;
import com.idms.base.api.v1.model.dto.DUCalbirationDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.calibrationExcessShortDto;
import com.idms.base.dao.entity.DispensingUnitCalibration;
import com.idms.base.service.CalibrateDUService;
import com.idms.base.service.InspectStockService;
import com.idms.base.support.rest.RestConstants;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
//@ConditionalOnExpression("false")
@RequestMapping(path = RestConstants.API_BASE + "/v1/calibration")
@Log4j2
public class calibrationController {

	@Autowired
	InspectStockService service;
	
	@Autowired
	private CalibrateDUService calibrateDUService;
	
	@Autowired
	private ModelMapper mapper;
	
	
//	@GetMapping(path = "/getInspectionList")
//	public ResponseEntity<List<InspectStockDto>> getInspectionsList() {
//		return service.getInspectStockList();
//	}
//	
//	
//	@GetMapping(path = "/getInspectionData/{id}")
//	public ResponseEntity<InspectStockDto> getInspectStockData(@PathVariable Integer id) {
//		return service.getInspectStockData(id);
//	}
//	
//	@PostMapping(path = "/saveInspectionData")
//	public ResponseEntity<Boolean> saveInspectStockData(@RequestBody InspectStockDto input) {
//		return service.saveInspectStockData(input);
//	}
	
	@GetMapping(path = "/getCalibrationData/{id}")
	public ResponseEntity<String> getCalibrationDispensingUnitInfo(@PathVariable Integer id) {
		//return dispensing unit code from mst_dispensing_unit
		return service.getCalibrationDispensingUnitInfo(id);
	}
	
	@PostMapping(path = "/postCalibrationData",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> postCalibrationDispensingUnitInfo(@RequestParam ("calibrationData") String busRefuelingMaster,@RequestParam(value="uploadFile") MultipartFile uploadFile) throws JsonMappingException, JsonProcessingException {
		//save the data into dispensing_unit_calibration and return boolean value
		CalibrationDispensingUnitDto input = new ObjectMapper().readValue(busRefuelingMaster, CalibrationDispensingUnitDto.class);
		return service.postCalibrationDispensingUnitInfo(input,uploadFile);
	}
	
	@GetMapping(path="/getAllCalibrations") 
	public List<DispensingUnitCalibration> findAllCalibrations() {
		return service.findAll();
	}
	
	@GetMapping("/calculateExcessShort")
	public  Double excessShort(@RequestParam ("tankId") Integer tankId,@RequestParam ("discharge")Double discharge) {
		return service.excessShort(tankId, discharge);
	}
	
	@GetMapping("/getExcessShortList")
	public List<calibrationExcessShortDto> getExcessShortList() {
		return service.getExcessShortList();
	}
	
	@GetMapping(path = "/getAllCalibrations/{dpCode}")
	public List<DUCalbirationDto> getAllCalibrations(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllCalibrations CalibrateDUController ");
		return calibrateDUService.findAllCalibrationsByDepot(dpCode).stream().map(calib -> this.mapper.map(calib, DUCalbirationDto.class))
				.collect(Collectors.toList());
	}
	
	@GetMapping(path = "/getCalibrationFormLoad/{dpCode}")
	public List<DispensingUnitMasterDto> getCalibrationFormLoad(@PathVariable("dpCode") String dpCode) {
		return calibrateDUService.getCalibrationFormLoad(dpCode).stream().map(du -> this.mapper.map(du, DispensingUnitMasterDto.class))
				.collect(Collectors.toList());
	}
	
	/*@PostMapping(path="/saveCalibration")
	public ResponseEntity<ResponseStatus> saveCalibration(@RequestParam("duCalbirationDto") String duCalbirationDto, @RequestParam("uploadFile") MultipartFile uploadFile) throws JsonMappingException, JsonProcessingException {
		log.info("Enter into getAllCalibrations CalibrateDUController ");
		DUCalbiration duCalibration = new ObjectMapper().readValue(duCalbirationDto, DUCalbiration.class);
		return this.calibrateDUService.saveDUCalibration(duCalibration, uploadFile);
	}*/
}
