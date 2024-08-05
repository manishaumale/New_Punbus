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

import com.idms.base.api.v1.model.dto.DieselIssuedForOtherPurposeDto;
import com.idms.base.api.v1.model.dto.OtherPurposeFormLoadDto;
import com.idms.base.dao.entity.DieselIssuedForOtherPurpose;
import com.idms.base.service.OtherPurposeService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/otherPurpuse")
@Log4j2
public class OtherPurposeController {
	
	@Autowired
	OtherPurposeService service;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@ApiOperation("Returns Object of Other Purpose Data When Load")
	@GetMapping(path = "/otherPurposeDataOnLoad/{dpCode}")
	public OtherPurposeFormLoadDto otherPurposeDataOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into otherPurposeDataOnLoad service");
		OtherPurposeFormLoadDto otherPurposeFormLoadDto = this.service.otherPurposeDataOnLoad(dpCode);
		return otherPurposeFormLoadDto;
	}
	
	@ApiOperation("Creates a new Other Purpose returning status 200 when persisted successfully.")
	@PostMapping("/saveOtherPurpose")
	public ResponseEntity<ResponseStatus> saveOtherPurpose(@RequestBody DieselIssuedForOtherPurpose otherPurpose) {
		log.info("Enter into saveOtherPurpose service");
		return this.service.saveOtherPurpose(otherPurpose);
	}
	
	@ApiOperation("Returns List of Other Purpose")
	@GetMapping(path = "/listOfAllOtherPurpose/{depotCode}")
	public List<DieselIssuedForOtherPurposeDto> listOfAllOtherPurpose(@PathVariable("depotCode") String depotCode) {
		log.info("Enter into listOfAllOtherPurpose service");
		return this.service.listOfAllOtherPurpose(depotCode).stream()
				.map(diesel -> this.mapper.map(diesel, DieselIssuedForOtherPurposeDto.class)).collect(Collectors.toList());
	}

}

