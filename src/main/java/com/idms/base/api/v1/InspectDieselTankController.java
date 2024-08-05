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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.InspectionTankDto;
import com.idms.base.api.v1.model.dto.TankInspectionDto;
import com.idms.base.dao.entity.TankInspection;
import com.idms.base.service.InspectTankService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/InspectTank")
@Log4j2
public class InspectDieselTankController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private InspectTankService service;
	
	@GetMapping(path = "/getAllInspections/{dpCode}/{username}")
	public List<InspectionTankDto> getAllInspections(@PathVariable("dpCode") String dpCode,@PathVariable("username") String userName) {
		log.info("Enter into getAllInspections InspectDieselTankController ");
		return service.getAllInspections(dpCode,userName).stream().map(ins -> this.mapper.map(ins, InspectionTankDto.class))
				.collect(Collectors.toList());
	}
	
	/*@GetMapping(path = "/getInspectionFormLoad/{dpCode}")
	public List<FuelTankMasterDto> getInspectionFormLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getInspectionFormLoad InspectDieselTankController ");
		return service.getInspectionFormLoad(dpCode).stream().map(tank -> this.mapper.map(tank, FuelTankMasterDto.class))
				.collect(Collectors.toList());
	}*/
	
	@GetMapping(path = "/getInspectionFormLoad/{dpCode}")
	public ResponseEntity<Object> getInspectionFormLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getInspectionFormLoad InspectDieselTankController ");
		return service.getInspectionFormLoad(dpCode);
	}
	
	@PostMapping(path="/saveTankInspection")
	public ResponseEntity<ResponseStatus> saveTankInspection(@RequestBody TankInspection tankInspectionDto) {
		log.info("Enter into saveTankInspection InspectDieselTankController ");
		return service.saveTankInspection(tankInspectionDto);
	}

}
