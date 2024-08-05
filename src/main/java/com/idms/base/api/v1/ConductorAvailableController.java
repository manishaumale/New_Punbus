package com.idms.base.api.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import com.idms.base.api.v1.model.dto.AllDriverOrConductorsByTPTypeDto;
import com.idms.base.api.v1.model.dto.ConductorAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.UnavailableConductorsDetailsDto;
import com.idms.base.dao.entity.ConductorUnavailabilityMaster;
import com.idms.base.service.ConductorAvailabilityService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/conductor/available")
@Log4j2
public class ConductorAvailableController {
	
	@Autowired
	ConductorAvailabilityService service;
	
	
	@ApiOperation("Returns Object of  Conductor Availability Master When Load")
	@GetMapping(path = "/conductorAvailabilityMasterFormOnLoad/{dpCode}")
	public ConductorAvailabilityFormLoadDto conductorAvailabilityMasterFormOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into conductorAvailabilityMasterFormOnLoad service");
		ConductorAvailabilityFormLoadDto conductorAvailabilityDto = this.service.conductorAvailabilityMasterFormOnLoad(dpCode);
		return conductorAvailabilityDto;
	}
	
	@ApiOperation("Creates a new Conductor Unavailable Master returning status 200 when persisted successfully.")
	@PostMapping("/saveUnavailableConductorMaster")
	public ResponseEntity<ResponseStatus> saveUnavailableConductorMaster(@RequestBody ConductorUnavailabilityMaster conductorUnavailabilityMaster) {
		log.info("Enter into saveUnavailableConductorMaster service");
		return this.service.saveUnavailableConductorMaster(conductorUnavailabilityMaster);
	}
	
	@ApiOperation("Returns List of Unavailable Conductor On click of count By Driver Type")
	@GetMapping(path = "/listOfUnavailableConductorsOnClick/{id}/{transportId}/{dpCode}")
	public List<UnavailableConductorsDetailsDto> listOfUnavailableConductorOnClick(@PathVariable("id") Integer employmentId,@PathVariable("transportId") Integer transportId, @PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfUnavailableConductorOnClick service");
		List<UnavailableConductorsDetailsDto> unavailableConductorsDetailsList = this.service.listOfUnavailableConductorsOnClick(employmentId,transportId, dpCode);
		return unavailableConductorsDetailsList;
	}
	
	@ApiOperation("Returns List of All Conductors On Basis Of Transport Type And Employment Type")
	@GetMapping(path = "/listOfAllConductorsOnBasisOfTransportType")
	public List<AllDriverOrConductorsByTPTypeDto> listOfAllConductorsOnBasisOfTransportType(@RequestParam("group") String[] groupIds, @RequestParam("dpCode") String dpCode) {
		log.info("Enter into listOfAllConductorsOnBasisOfTransportType service");
		List<AllDriverOrConductorsByTPTypeDto> allConductorsList = this.service.listOfAllConductorsOnBasisOfTransportType(groupIds, dpCode);
		return allConductorsList;
	}
	
	@ApiOperation("Update the likely to be ready date of specific conductor master by its identifier. ")
	@PutMapping(path = "/updateConductorToDate")
	public ResponseEntity<ResponseStatus> updateConductorToDate(@RequestParam("id") Integer id,
			@RequestParam("toDate")  String toDate){
		Date readyDate = null ;
		try {
			if(toDate != null)
				readyDate = new SimpleDateFormat("dd/MM/yyyy").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this.service.updateConductorToDate(id, readyDate);
	}
	

}
