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
import com.idms.base.api.v1.model.dto.DriverAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.UnavailableDriversDetailsDto;
import com.idms.base.dao.entity.DriverUnavailabilityMaster;
import com.idms.base.service.DriverAvailabilityService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/driver/available")
@Log4j2
public class DriverAvailableController {
	
	@Autowired
	DriverAvailabilityService service;
	
	
	@ApiOperation("Returns Object of  Driver Availability Master When Load")
	@GetMapping(path = "/driverAvailabilityMasterFormOnLoad/{dpCode}")
	public DriverAvailabilityFormLoadDto driverAvailabilityMasterFormOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into driverAvailabilityMasterFormOnLoad service");
		DriverAvailabilityFormLoadDto driverAvailabilityDto = this.service.driverAvailabilityMasterFormOnLoad(dpCode);
		return driverAvailabilityDto;
	}
	
	@ApiOperation("Creates a new Driver Unavailable Master returning status 200 when persisted successfully.")
	@PostMapping("/saveUnavailableDriverMaster")
	public ResponseEntity<ResponseStatus> saveUnavailableDriverMaster(@RequestBody DriverUnavailabilityMaster driverUnavailabilityMaster) {
		log.info("Enter into saveUnavailableDriverMaster service");
		return this.service.saveUnavailableDriverMaster(driverUnavailabilityMaster);
	}
	
	@ApiOperation("Returns List of Unavailable Drivers On click of count By Driver Type")
	@GetMapping(path = "/listOfUnavailableDriversOnClick/{id}/{transportId}/{dpCode}")
	public List<UnavailableDriversDetailsDto> listOfUnavailableDriversOnClick(@PathVariable("id") Integer employmentId,@PathVariable("transportId") Integer transportId, @PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfUnavailableDriversOnClick service");
		List<UnavailableDriversDetailsDto> unavailableDriversDetailsList = this.service.listOfUnavailableDriversOnClick(employmentId,transportId, dpCode);
		return unavailableDriversDetailsList;
	}
	
	@ApiOperation("Returns List of All Drivers On Basis Of Transport Type And Employment Type")
	@GetMapping(path = "/listOfAllDriversOnBasisOfTransportType")
	public List<AllDriverOrConductorsByTPTypeDto> listOfAllDriversOnBasisOfTransportType(@RequestParam("group") String[] groupIds, @RequestParam("dpCode") String dpCode) {
		log.info("Enter into listOfAllDriversOnBasisOfTransportType service");
		List<AllDriverOrConductorsByTPTypeDto> allDriversList = this.service.listOfAllDriversOnBasisOfTransportType(groupIds, dpCode);
		return allDriversList;
	}
	
	@ApiOperation("Update the to date of specific driver master by its identifier. ")
	@PutMapping(path = "/updateDriverToDate")
	public ResponseEntity<ResponseStatus> updateDriverToDate(@RequestParam("id") Integer id,
			@RequestParam("toDate")  String toDate){
		Date readyDate = null ;
		try {
			if(toDate != null)
				readyDate = new SimpleDateFormat("dd/MM/yyyy").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this.service.updateDriverToDate(id, readyDate);
	}
	
	@ApiOperation("Returns List of All Unavailable Drivers And Conductors By Pending Status")
	@GetMapping(path = "/listOfAllDriversAndConductors/{dpCode}")
	public List<UnavailableDriversDetailsDto> listOfAllDriversAndConductors(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfAllDriversAndConductors service");
		List<UnavailableDriversDetailsDto> allDriversConductorList = this.service.listOfAllDriversAndConductors(dpCode);
		return allDriversConductorList;
	}
	
	@ApiOperation("Update status of driver and conductor By Approve.")
	@PutMapping(path = "/approveAttendenceStatus/{id}/{driverConductorFlg}")
	public ResponseEntity<ResponseStatus> approveAttendenceStatus(@PathVariable("id") Integer id,
			@PathVariable("driverConductorFlg") String driverConductorFlg) {
		return this.service.approveAttendenceStatus(id, driverConductorFlg);
	}
	
	@ApiOperation("Update status of driver and conductor By Reject.")
	@PutMapping(path = "/rejectAttendenceStatus/{id}/{driverConductorFlg}")
	public ResponseEntity<ResponseStatus> rejectAttendenceStatus(@PathVariable("id") Integer id,
			@PathVariable("driverConductorFlg") String driverConductorFlg) {
		return this.service.rejectAttendenceStatus(id, driverConductorFlg);
	}
	

}
