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

import com.idms.base.api.v1.model.dto.AssignSubStockToDepoDto;
import com.idms.base.api.v1.model.dto.AssignedSubStockFromLoadDto;
import com.idms.base.api.v1.model.dto.DepotTicketStockDto;
import com.idms.base.api.v1.model.dto.MasterStockSerialDto;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.dao.entity.MasterStockTicketDtls;
import com.idms.base.dao.entity.TicketDepotAssignment;
import com.idms.base.dao.repository.AssignSubStockToDepoRepository;
import com.idms.base.service.AssignSubStockService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/subStockAssignment")
@Log4j2
public class AssignSubStockController {

	@Autowired
	AssignSubStockService service;
	
	@ApiOperation(" Storing All AssignSubStock Dtls")
	@PostMapping(value = "/assinstock",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addSubStockDetails(@RequestBody AssignSubStockToDepoDto assignSubStockDto) {

		log.info("Inserting All AssignSubStock Service");
		String suStockResponse = service.addSubStockDetails(assignSubStockDto);
		return new ResponseEntity<>(suStockResponse, HttpStatus.CREATED);

	}

	@ApiOperation("Returning All AssignSubStock Dtls")
	@GetMapping(value ="/getallsubstockdtls/{depotCode}")
	public ResponseEntity<List<AssignSubStockToDepoDto>> getAssignSubStockDtlsInDepo(@PathVariable("depotCode")String depotCode) {
		log.info("Finding All SubStockService Dtls");
		List<AssignSubStockToDepoDto> allSubStockServices = service.getAllSubStockDetailsBasedOnDepoCode(depotCode);
		return new ResponseEntity<List<AssignSubStockToDepoDto>>(allSubStockServices, HttpStatus.OK);
		

	}

	@ApiOperation("Finding SubStockDtlsInDepo by Id")
	@GetMapping(value = "/substock/{subStockId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AssignSubStockToDepoDto> getAssignSubStockDtlsInDepoById(
			@PathVariable("subStockId") Integer subStockId) {
		if (subStockId != null) {
			AssignSubStockToDepoDto SubStockDtls = service.findSubStockDtls(subStockId);

			return new ResponseEntity<AssignSubStockToDepoDto>(SubStockDtls, HttpStatus.OK);
		}
		return new ResponseEntity<AssignSubStockToDepoDto>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation("Finding SubStockDtls ByDepoName ")
	@GetMapping(value = "/stock/{depotName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AssignSubStockToDepoDto>> getAssignSubStockDtlsInDepoByDepoName(
			@PathVariable("depotName") String depotName) {
		if (depotName != null) {
			List<AssignSubStockToDepoDto> AssignSubStockDtls = service.findSubStockDtlsOfDepo(depotName);
			return new ResponseEntity<List<AssignSubStockToDepoDto>>(AssignSubStockDtls, HttpStatus.OK);
		}
		return new ResponseEntity<List<AssignSubStockToDepoDto>>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(" FormLoadOnDtls")
	@GetMapping(value = "/loadStockForm/{tpList}")
	public AssignedSubStockFromLoadDto formLoadOnDtls(@PathVariable("tpList")String tpList) {
		return service.fromOnLoadDetails(tpList);
	}
	
	@ApiOperation(" getTicketBooks")
	@GetMapping(value = "/getTicketBooks/{denoId}/{tpId}")
	public List<MasterStockSerialDto> getTicketBooks(@PathVariable("denoId")Integer denoId, @PathVariable("tpId")Integer transportId) {
		return service.getTicketBooks(denoId, transportId);
	}
	
	@ApiOperation(" saveTicketDepotAssignment")
	@PostMapping(value = "/saveTicketDepotAssignment")
	public ResponseEntity<ResponseStatus> saveTicketDepotAssignment(@RequestBody List<MasterStock> masterStockList) {
		return service.saveTicketDepotAssignment(masterStockList);
	}
	
	@ApiOperation(" GetDepotTicketStockList")
	@GetMapping(value = "/getDepotTicketStockList/{tpList}")
	public List<DepotTicketStockDto> getDepotTicketStockList(@PathVariable("tpList")String tpList) {
		return service.getDepotTicketStockList(tpList);
	}

	@ApiOperation("GetDepotTicketStockByDeno")
	@GetMapping(value = "/getTicketStockByDenomination/{denomination}/{centralStockId}")
	public List<DepotTicketStockDto> getTicketStockByDenomination(@PathVariable("denomination")Integer denomination,@PathVariable("centralStockId")Integer centralStockId) {
		return service.getTicketStockByDenomination(denomination,centralStockId);
	}
}
