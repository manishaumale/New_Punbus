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

import com.idms.base.api.v1.model.dto.ManageTicketBoxDto;
import com.idms.base.api.v1.model.dto.MasterStockDto;
import com.idms.base.api.v1.model.dto.MasterStockSerialDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementFormDto;
import com.idms.base.api.v1.model.dto.TicketManagementBoxParentDto;
import com.idms.base.dao.entity.TicketBoxManagementEntity;
import com.idms.base.dao.entity.TicketBoxManagementParentEntity;
import com.idms.base.dao.repository.DenominationRepository;
import com.idms.base.service.AssignSubStockService;
import com.idms.base.service.TicketBoxManagementService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/TicketBoxManagement")
@Log4j2
public class TicketBoxManagementController {

	@Autowired
	TicketBoxManagementService service;

	@Autowired
	DenominationRepository denoRepo;
	
	@Autowired
	AssignSubStockService assignSubStockService;

	@GetMapping("/getTicketBoxMgmtFormLoad/{tpList}/{dpCode}")
	public TicketBoxManagementFormDto getTicketBoxMgmtFormLoad(@PathVariable("tpList") String tpList,
			@PathVariable("dpCode") String dpCode) {
		return service.getTicketBoxMgmtFormLoad(tpList, dpCode);
	}
//	
//	@ApiOperation("Return List of Start Sr. No. & End Sr. No. by Denomination Id")
//	@GetMapping("/getStockTicketDetails/{id}")
//	public List<MasterStockDto> getStockTicketDetails(@PathVariable("id") Integer id) {
//		return service.getStockTicketDetailsList(id);
//	}
	
	@ApiOperation("Return List of Start Sr. No. & End Sr. No. by Stock Id")
	@GetMapping("/getStockTicketDetails/{id}")
	public List<MasterStockDto> getStockTicketDetails(@PathVariable("id") Integer id) {
		return service.getStockTicketDetailsList(id);
	}
	
	@ApiOperation(" getTicketBooks")
	@GetMapping(value = "/getTicketBooks/{denoId}/{tpId}")
	public List<MasterStockSerialDto> getTicketBooks(@PathVariable("denoId")Integer denoId, @PathVariable("tpId")Integer transportId) {
		return service.getTicketBooks(denoId, transportId);
	}

	@ApiOperation(" Adding All TicketBoxManagement Dtls")
	@PostMapping(value = "/addTicketBoxMgmt", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseStatus> addTicketBoxManagement(@RequestBody List<ManageTicketBoxDto> ticketBoxManagementDto)
	{
		log.info("Inserting All TicketBoxManagement Service");
		return this.service.addTicketBoxMgmt(ticketBoxManagementDto);
	}
	
	@ApiOperation(" Returning All TicketBoxManagement Dtls")
	@GetMapping(value = "/getTicketBoxDtls/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicketBoxManagementDto> getTicketBoxManagementFormLoadDto(@PathVariable("id") Integer id) {
		log.info(" Finding All TicketBoxManagement Dtls ");

		TicketBoxManagementDto allTicketBoxManagementFormLoad = service.getAllTicketBoxManagementFormLoad(id);
		return new ResponseEntity<TicketBoxManagementDto>(allTicketBoxManagementFormLoad,HttpStatus.OK);

	}
	
	@ApiOperation("Get Ticket Details List")
	@GetMapping(value = "/getTicketDetailsList")
	public List<TicketManagementBoxParentDto> getTicketDetailsList() {
		return service.getTicketDetailsList();
	}

}
