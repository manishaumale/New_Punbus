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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.ConcessionTicketDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.SubmitEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.SubmitEtmTicketViewDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.service.BasicMasterService;
import com.idms.base.service.SubmitETMnTicketBoxService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/SubmitTicketBoxETM")
@Log4j2
public class SubmitETMnTicketBoxController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private SubmitETMnTicketBoxService submitETMnTicketBoxservice;
	
	@Autowired
	private BasicMasterService basicMasterService;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	/*
	 * Method to return all data from conductor master on bases of depo Id
	 * 
	 * 
	 * */
	@ApiOperation("Returns list of all Conductor Master")
	@GetMapping(path = "/allConductorMaster/{dpCode}")
	public List<ConductorMasterDto> getAllConductorMasterByDpcode(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllConductorMasterByDpcode service");
		return this.submitETMnTicketBoxservice.getAllConductorMasterByDpcode(dpCode).stream()
				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	
	}
	
	@ApiOperation("Returns Issue Etm Ticket Details on Conductor Id")
	@GetMapping(path = "/getIssueTicketDetails/{conductorId}")
	public IssueEtmTicketBoxDto getIssueTicketDetails(@PathVariable("conductorId") Integer conductorId) {
		log.info("Enter into getIssueTicketDetails service");
		return this.submitETMnTicketBoxservice.getIssueTicketDetails(conductorId);
//				.stream()
//				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	
	}
	
	@ApiOperation("Returns list of all Consession Ticket")
	@GetMapping(path = "/getConsessionTicketList")
	public List<ConcessionTicketDto> getConsessionTicketList() {
		log.info("Enter into getConsessionTicketList service");
		return this.submitETMnTicketBoxservice.getConsessionTicketList();
//				.stream()
//				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	
	}
	
	@ApiOperation("Submit ETM Ticket Box")
	@PostMapping(path = "/submitEtmTicketBox")
	public ResponseEntity<ResponseStatus> submitEtmTicketBox(@RequestBody SubmitEtmTicketBoxDto submitEtmTicketBox) {
		log.info("Enter into submitEtmTicketBox service");
		return this.submitETMnTicketBoxservice.submitEtmTicketBox(submitEtmTicketBox);
//				.stream()
//				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	
	}
	
	@ApiOperation("Returns list of all Submit Etm Ticket Box List View")
	@GetMapping(path = "/getSubmitEtmTicketBoxList/{id}")
	public SubmitEtmTicketBoxDto getSubmitEtmTicketBoxList(@PathVariable("id") Integer id) {
		log.info("Enter into getSubmitEtmTicketBoxList service");
		return this.submitETMnTicketBoxservice.getSubmitEtmTicketBoxList(id);
//				.stream()
//				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	
	}
	
	@ApiOperation("Returns list of all Submit Etm Ticket Box List")
	@GetMapping(path = "/getSubmitEtmTicketBoxDataByDepot/{dpCode}")
	public List<SubmitEtmTicketViewDto> getSubmitEtmTicketBoxData(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getSubmitEtmTicketBoxList service");
		DepotMaster depotObj = depotRepo.findByDepotCode(dpCode);
		return this.submitETMnTicketBoxservice.getAllSubmitEtmByDepot(depotObj.getId());
//				.stream()
//				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	
	}
	
	@PostMapping("/submitTicketBox")
	public ResponseEntity<ResponseStatus> submitTicketBox(@RequestBody List<TicketBoxManagementDto> ticketBoxDto) {
		return submitETMnTicketBoxservice.submitTicketBox(ticketBoxDto);
	}
	
	@GetMapping(path = "/viewSubmitEtmTicketBoxList/{id}")
	public SubmitEtmTicketBoxDto viewSubmitEtmTicketBoxList(@PathVariable("id") Integer id) {
		log.info("Enter into getSubmitEtmTicketBoxList service");
		return this.submitETMnTicketBoxservice.viewSubmitEtmTicketBoxList(id);
//				.stream()
//				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	
	}
	
}
