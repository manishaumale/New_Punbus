package com.idms.base.api.v1;

import java.util.List;

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
import com.idms.base.api.v1.model.dto.DocketTyreAssociationDto;
import com.idms.base.api.v1.model.dto.RetreadingDocketDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.dao.entity.RetreadingDocket;
import com.idms.base.service.TyreRetradeService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/TyreRetrade")
@Log4j2
public class TyreRetradeController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TyreRetradeService service;
	
	@ApiOperation("Get List of Bus Tyre Association")
	@GetMapping(path = "/getAllTyreForResole/{dpCode}")
	public List<TyreMasterDto> getAllTyreForResole(@PathVariable("dpCode") String dpCode) {
		List<TyreMasterDto> list = service.getAllTyreForResole(dpCode);
		return list;
	}
	
	@ApiOperation("Send Tyres for Retreading")
	@PostMapping(path="/markForRetreading")
	public ResponseEntity<ResponseStatus> markForRetreading(@RequestBody RetreadingDocketDto docketDto) {
		return service.markForRetreading(docketDto);
	}
	
	@ApiOperation("Get List of Docket")
	@GetMapping(path = "/getAllDocket/{dpCode}")
	public List<RetreadingDocketDto> getAllDocket(@PathVariable("dpCode") String dpCode) {
		List<RetreadingDocketDto> list = service.getAllDocket(dpCode);
		return list;
	}
	
	@ApiOperation("Send Tyres for Condemnation")
	@PostMapping(path="/markForCondemnation")
	public ResponseEntity<ResponseStatus> markForCondemnation(@RequestBody RetreadingDocketDto docketDto) {
		return service.markForCondemnation(docketDto);
	}
	
	@ApiOperation("Get List of Docket")
	@GetMapping(path = "/getReceivedDocketList/{dpCode}")
	public List<RetreadingDocketDto> getReceivedDocketList(@PathVariable("dpCode") String dpCode) {
		List<RetreadingDocketDto> list = service.getReceivedDocketList(dpCode);
		return list;
	}
	
	@ApiOperation("Get List of Tyres for a Docket")
	@GetMapping(path = "/getTyreListForDocket/{docketId}")
	public List<DocketTyreAssociationDto> getTyreListForDocket(@PathVariable("docketId") Integer docketId) {
		List<DocketTyreAssociationDto> list = service.getTyreListForDocket(docketId);
		return list;
	}
	
	@ApiOperation("Save Received List")
	@PostMapping(path="/saveReceivedDocketList")
	public ResponseEntity<ResponseStatus> saveReceivedDocketList(@RequestBody RetreadingDocketDto docketDto) {
		return service.saveReceivedDocketList(docketDto);
	}
	
	@ApiOperation("Get Tyre Details By Docket Id")
	@GetMapping(path = "/getTyreObjForDocket/{docketId}")
	public List<TyreMasterDto> getTyreObjForDocket(@PathVariable("docketId") Integer docketId) {
		List<TyreMasterDto> tyreObjList = service.getTyreObjForDocket(docketId);
		return tyreObjList;
	}
	
}
