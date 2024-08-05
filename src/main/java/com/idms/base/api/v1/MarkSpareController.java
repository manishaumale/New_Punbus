package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareConductorDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareDriverdetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareDto;
import com.idms.base.dao.entity.MarkSpareEntity;
import com.idms.base.service.MarkSpareService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/markSpare")
public class MarkSpareController {
	
	@Autowired
	MarkSpareService  service;
	
	@ApiOperation(" Storing MarkSpare Dtls")
	@PostMapping(value = "/addMarkSpareDetails/{depoCode}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseStatus> addMarkSpareDetails(@RequestBody MarkSpareEntity markSpareEntity,@PathVariable("depoCode") String depoCode)
	{
		
		return this.service.addMarkSpareDetails(markSpareEntity,depoCode);
		
		
	}
	
	@ApiOperation(" Fetch the MarkSpare Dtls")
	@GetMapping(value="/getmarkSpareDetails/{depoCode}")
	public ResponseEntity<List<MarkSpareDto>> getMarkSpareDetails(@PathVariable("depoCode") String depoCode)
	
	{
		
		List<MarkSpareDto> markSpareDetails = service.getMarkSpareDetails(depoCode);
		return new ResponseEntity<List<MarkSpareDto>>(markSpareDetails,HttpStatus.OK);
	}
	@ApiOperation("  Fetch the All busNumbers")
	@GetMapping(value="/getmarkSpareBusDetails/{depoCode}")	
	public ResponseEntity<List<MarkSpareBusDetailsDto>>getAllBusDetails(@PathVariable("depoCode") String depoCode)
	{
		List<MarkSpareBusDetailsDto> allBusRegisterNumbers = service.getAllBusRegisterNumbers(depoCode);
		return new ResponseEntity<List<MarkSpareBusDetailsDto>> ( allBusRegisterNumbers ,HttpStatus.OK);
	}
	@ApiOperation("  Fetch the All driverNames")
	@GetMapping(value="/getmarkSpareDriverDetails/{depoCode}")	
	public ResponseEntity<List<MarkSpareDriverdetailsDto>>getAllDriverDetails(@PathVariable("depoCode") String depoCode)
	{
	List<MarkSpareDriverdetailsDto> allDriverNames = service.getAllDriverNames(depoCode);
		return new ResponseEntity<List<MarkSpareDriverdetailsDto>> (allDriverNames ,HttpStatus.OK);
	}
	@ApiOperation("  Fetch the All conductorNames")
	@GetMapping(value="/getmarkSpareConductorDetails/{depoCode}")	
	public ResponseEntity<List<MarkSpareConductorDetailsDto>>getAllConductorDetails(@PathVariable("depoCode") String depoCode)
	{
		 List<MarkSpareConductorDetailsDto> allConductorNames = service.getAllConductorNames(depoCode);
		return new ResponseEntity<List<MarkSpareConductorDetailsDto>>( allConductorNames,HttpStatus.OK);
	
	}	

}
