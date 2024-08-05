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

import com.idms.base.api.v1.model.dto.MarkCondemndto;
import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.dao.entity.MarkCondemn;
import com.idms.base.service.MarkCondemnService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/markCodemn")
public class MarkCondemnController {
	@Autowired
	MarkCondemnService service;

	@ApiOperation(" Storing MarkCondemn Dtls")
	@PostMapping(value = "/addMarkCodemndtls/{depoCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseStatus> addMarkCodemndtls(@RequestBody MarkCondemn markCondemn,
			@PathVariable("depoCode") String depoCode) {

		return this.service.addmarkCondemnDtls(markCondemn, depoCode);

	}

	@ApiOperation(" Fetch MarkCondemn Dtls")
	@GetMapping(value = "/getmarkcodemndtls/{depoCode}")
	public ResponseEntity<List<MarkCondemndto>> getMarkCodemnDtls(@PathVariable("depoCode") String depoCode) {

		List<MarkCondemndto> markCondemnDtls = service.getMarkCondemnDtls(depoCode);
		return new ResponseEntity<List<MarkCondemndto>>(markCondemnDtls, HttpStatus.OK);

	}

	@ApiOperation(" Fetch All Bus Dtls")
	@GetMapping(value="/getbusdetails/{depoCode}")
	public ResponseEntity<List<MarkSpareBusDetailsDto>> getBusDetails(@PathVariable("depoCode") String depoCode) {

		List<MarkSpareBusDetailsDto> allBusDetails = service.getAllBusDetails(depoCode);
		return new ResponseEntity<List<MarkSpareBusDetailsDto>>(allBusDetails, HttpStatus.OK);

	}

}
