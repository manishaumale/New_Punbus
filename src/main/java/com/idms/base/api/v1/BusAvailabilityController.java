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

import com.idms.base.api.v1.model.dto.AllBusesByTransportTypeDto;
import com.idms.base.api.v1.model.dto.BusAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.UnavailableBusesDetailsDto;
import com.idms.base.dao.entity.BusUnavailabilityMaster;
import com.idms.base.service.BusAvailabilityService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/bus/available")
@Log4j2
public class BusAvailabilityController {
	
	@Autowired
	BusAvailabilityService service;
	
	
	@ApiOperation("Returns Object of  Bus Availability Master When Load")
	@GetMapping(path = "/busAvailabilityMasterFormOnLoad/{dpCode}")
	public BusAvailabilityFormLoadDto busAvailabilityMasterFormOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into busAvailabilityMasterFormOnLoad service");
		BusAvailabilityFormLoadDto busAvailabilityFormLoadDto = this.service.busAvailabilityMasterFormOnLoad(dpCode);
		return busAvailabilityFormLoadDto;
	}
	
	@ApiOperation("Creates a new Bus Unavailable Master returning status 200 when persisted successfully.")
	@PostMapping("/saveUnavailableBusMaster")
	public ResponseEntity<ResponseStatus> saveUnavailableBusMaster(@RequestBody BusUnavailabilityMaster busUnavailabilityMaster) {
		log.info("Enter into saveBusMaster service");
		return this.service.saveUnavailableBusMaster(busUnavailabilityMaster);
	}
	
	@ApiOperation("Returns List of Unavailable Buses On click of count By Bus Type")
	@GetMapping(path = "/listOfUnavailableBusesOnClick/{id}/{transportId}/{dpCode}")
	public List<UnavailableBusesDetailsDto> listOfUnavailableBusesOnClick(@PathVariable("id") Integer BusTypeId,@PathVariable("transportId") Integer transportId, @PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfUnavailableBusesOnClick service");
		List<UnavailableBusesDetailsDto> unavailableBusesDetailsList = this.service.listOfUnavailableBusesOnClick(BusTypeId,transportId,dpCode);
		return unavailableBusesDetailsList;
	}
	
	@ApiOperation("Returns List of All Buses On Basis Of Transport Type")
	@GetMapping(path = "/listOfAllBusesOnBasisOfTransportType")
	public List<AllBusesByTransportTypeDto> listOfAllBusesOnBasisOfTransportType(@RequestParam("group") String[] groupIds, @RequestParam("dpCode") String dpCode) {
		List<AllBusesByTransportTypeDto> allBusesList = this.service.listOfAllBusesOnBasisOfTransportType(groupIds, dpCode);
		return allBusesList;
	}
	
	@ApiOperation("Update the likely to be ready date of specific bus master by its identifier. ")
	@PutMapping(path = "/updateBusLikelyReadyDate")
	public ResponseEntity<ResponseStatus> updateBusLikelyReadyDate(@RequestParam("id") Integer id,
			@RequestParam("likelyToBeReadyDate")  String likelyToBeReadyDate){
		Date readyDate = null ;
		try {
			if(likelyToBeReadyDate != null)
				readyDate = new SimpleDateFormat("dd/MM/yyyy").parse(likelyToBeReadyDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this.service.updateBusLikelyReadyDate(id, readyDate);
	}

}
