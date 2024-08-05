package com.idms.base.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.EtmTBAssignmentDto;
import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.service.ETMIntegrationService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/Integration/ETM")
@Log4j2
public class ETMIntegrationController {
	
	@Autowired
	ETMIntegrationService service;
	
	@ApiOperation("Save ETM Integration data returning status 200 when persisted successfully.")
	@PostMapping("/submitETMData")
	public ResponseEntity<ResponseStatus> submitETMData(@RequestBody EtmTBAssignment etmTBAssignment) {
		log.info("Enter into submitETMData service");
		return this.service.submitETMData(etmTBAssignment);
	}
	
	@ApiOperation("fetch Etm details--- submit etm")
	@GetMapping("/fetchEtmData/{wayBillNo}")
	public ResponseEntity<EtmTBAssignmentDto> fetchEtmData(@PathVariable String wayBillNo){
		return service.fetchEtmDetails(wayBillNo);
	}
	
	@ApiOperation("fetch Partial Etm details-- Issue etm")
	@GetMapping("/fetchPartialEtmData/{wayBillNo}")
	public ResponseEntity<EtmTBAssignmentDto> fetchPartialEtmData(@PathVariable String wayBillNo){
		return service.fetchPartialEtmDetails(wayBillNo);
	}

}
