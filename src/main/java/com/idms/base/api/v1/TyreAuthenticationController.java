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

import com.idms.base.api.v1.model.dto.TyreAuthenticationDto;
import com.idms.base.dao.entity.TyreAuthenticationEntity;
import com.idms.base.service.TyreAuthenticationService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/tyreAuthentication")
@Log4j2
public class TyreAuthenticationController {
	
	@Autowired
	TyreAuthenticationService service;
	
	@ApiOperation(" Adding the TyreAuthentication service  details")
	@PostMapping(value="/saveTyreAuthenticationDtls/{depoCode}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseStatus> saveTyreAuthentication(@PathVariable("depoCode")String depoCode,@RequestBody TyreAuthenticationEntity tyreAuthenticationEntity )
	{
		log.info("Entering into TyreAuthentication service");
		return this.service.saveTyreAuthentication(depoCode,tyreAuthenticationEntity);
	}
	
	@ApiOperation(" getting the TyreAuthentication details based on tyretakenoff condition")
	@GetMapping(value="/getTyreAuthentication/{depoCode}")
	public ResponseEntity<List<TyreAuthenticationDto>> getTyreAuthentication(@PathVariable("depoCode") String depoCode)
	{
		log.info("Entering into TyreAuthentication details based on tyretakenoff condition details");
		List<TyreAuthenticationDto> tyreAuthenticationDetails = service.getTyreAuthenticationDetails(depoCode);
		return new ResponseEntity<List<TyreAuthenticationDto>>(tyreAuthenticationDetails,HttpStatus.OK);
		
	}
	
	
	@ApiOperation(" Fetching the TyreAuthenticationtable details")
	@GetMapping(value="/tyreAuthenticationDetails/{depoCode}")
	public ResponseEntity<List<TyreAuthenticationDto>> tyreAuthenticationDetails(@PathVariable("depoCode") String depoCode)
	{
		log.info(" fetching TyreAuthenticationtable  service details");
		 List<TyreAuthenticationDto> fetchTyreAuthenticationDetails = service.fetchTyreAuthenticationDetails(depoCode);
		return new ResponseEntity<List<TyreAuthenticationDto>>(fetchTyreAuthenticationDetails,HttpStatus.OK);
		
	}


}
