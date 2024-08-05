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

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.TyreChangeFormDto;
import com.idms.base.service.TyreChangeService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/TyreChange")
@Log4j2
public class TyreChangeController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	TyreChangeService service;
	
	@ApiOperation("Get List of Bus Tyre Association")
	@GetMapping(path = "/getAllBusesForTyreChange/{dpCode}")
	public List<BusMasterDto> getAllBusesForTyreChange(@PathVariable("dpCode") String dpCode) {
		return this.service.getAllBusesForTyreChange(dpCode).stream()
				.map(bus -> this.mapper.map(bus, BusMasterDto.class)).collect(Collectors.toList());
		
	}
	
	@ApiOperation("Get Form load for Tyre Change")
	@GetMapping(path = "/getTyreChangeForm/{busId}")
	public TyreChangeFormDto getTyreChangeForm(@PathVariable("busId") Integer busId) {
		return this.service.getTyreChangeForm(busId);
	}
	
	@ApiOperation("Save Tyre Change Data")
	@PostMapping(path="/saveTyreChangeInfo")
	public ResponseEntity<ResponseStatus> saveTyreChangeInfo(@RequestBody BusMasterDto busObj) {
		return this.service.saveTyreChangeInfo(busObj);
	}
	
	
}
