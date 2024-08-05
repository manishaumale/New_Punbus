package com.idms.base.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.DUCalbirationDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.service.CalibrateDUService;
import com.idms.base.support.rest.RestConstants;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/CalibrateDU")
@Log4j2
public class CalibrateDUController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CalibrateDUService calibrateDUService;
	
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
