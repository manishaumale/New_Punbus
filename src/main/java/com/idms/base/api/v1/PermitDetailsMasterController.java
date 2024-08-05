package com.idms.base.api.v1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import com.idms.base.api.v1.model.dto.PermitDetailsDto;
import com.idms.base.api.v1.model.dto.PermitDetailsFormDto;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.repository.BusSubTypeMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.TaxMasterRepository;
import com.idms.base.dao.repository.TaxTypeMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.CommonService;
import com.idms.base.service.PermitDetailsMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/permit")
@Log4j2
public class PermitDetailsMasterController {
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PermitDetailsMasterService service;

	@Autowired
	StateMasterRepository stateMasterRepository;

	@Autowired
	CityMasterRepository cityMasterRepository;

	@Autowired
	TransportUnitRepository transportUnitRepository;

	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;

	@Autowired
	BusSubTypeMasterRepository busSubTypeMasterRepository;

	@Autowired
	TaxMasterRepository taxMasterRepository;

	@Autowired
	TaxTypeMasterRepository taxTypeMasterRepository;
	
	@Autowired
	CommonService commonService;
	
	@ApiOperation("Returns list of all Permit Details Master")
	@GetMapping(path = "/allPermitDetailsMaster/{dpCode}")
	public List<PermitDetailsDto> getallPermitDetailsMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getallPermitDetailsMaster service");
		return this.service.getallPermitDetailsMaster(dpCode).stream()
				.map(permitDetailsDto -> this.mapper.map(permitDetailsDto, PermitDetailsDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns Object of  Permit Details Master When Load")
	@GetMapping(path = "/permitDetailsMasterLoad/{dpCode}")
	public PermitDetailsFormDto permitDetailsMasterLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getallPermitDetailsMasterForm service");
		PermitDetailsFormDto permitDetailsFormDto = this.service.permitDetailsMasterLoad(dpCode);
		return permitDetailsFormDto;
		
	}
	
	@ApiOperation("Creates a new Permit Detail Master returning status 200 when persisted successfully.")
	@PostMapping("/savePermitDetailMaster")
	public ResponseEntity<ResponseStatus> savePermitMaster(@RequestParam("PostData") String permitMaster, @RequestParam("uploadFile") MultipartFile uploadFile) throws JsonMappingException, JsonProcessingException {
		log.info("Enter into savePermitMaster service");
		PermitDetailsMaster permitMasterObj = new ObjectMapper().readValue(permitMaster, PermitDetailsMaster.class);
		return this.service.savePermitMaster(permitMasterObj, uploadFile);
	}
	
//	@ApiOperation("Updates or partially updates a Permit Detail Master by its identifier. 404 if does not exist.")
//	@PostMapping("/updatePermitDetailMaster/{id}")
//	public ResponseEntity<ResponseStatus> updatePermitMaster(@PathVariable("id") Integer id , @RequestBody PermitDetailsMaster permitMaster) {
//		log.info("Enter into updatePermitMaster service");
//		return this.service.savePermitMaster(permitMaster);
//	}
	
	@ApiOperation("Updates or partially updates a Permit Detail Master by its identifier In case of Renewal. 404 if does not exist.")
	@PutMapping("/updatePermitDetailRenewal/{id}")
	public ResponseEntity<ResponseStatus> updatePermitDetailRenewal(@PathVariable("id") Integer id , @RequestParam("uploadFile") MultipartFile uploadFile,@RequestParam("issueDate") 
	 String issueDate,@RequestParam("validUpTo") String validUpTo) {
		log.info("Enter into updatePermitDetailRenewal service");
		Date issuedDate = null ;
		Date validDateDate = null;
		
		try {
			if(issueDate != null)
			issuedDate = new SimpleDateFormat("yyyy-MM-dd").parse(issueDate);
			if(validUpTo != null)
			validDateDate = new SimpleDateFormat("yyyy-MM-dd").parse(validUpTo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this.service.updatePermitMaster(id,uploadFile,issuedDate,validDateDate);
	}
	
	@ApiOperation("Update the status of specific permit detail master by its identifier. ")
	@PutMapping(path = "/updatePermitMasterStatusFlag/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updatePermitMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updatePermitMasterStatusFlag(id, flag);
	}
	
	@ApiOperation("Delete the specific permit detail master by its identifier. ")
	@PutMapping(path = "/deletePermitMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> deletePermitMaster(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updatePermitMasterIsDeletedFlag(id, flag);
	}
	
}
