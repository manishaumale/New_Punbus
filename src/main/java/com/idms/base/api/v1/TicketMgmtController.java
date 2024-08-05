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

import com.idms.base.api.v1.model.dto.ETMFormLoadDto;
import com.idms.base.api.v1.model.dto.ETMMasterDto;
import com.idms.base.api.v1.model.dto.TicketBoxMasterDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.ETMMaster;
import com.idms.base.dao.entity.TicketBoxMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.service.BasicMasterService;
import com.idms.base.service.TicketMgmtService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/ticket")
@Log4j2
public class TicketMgmtController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TicketMgmtService service;
	
	@Autowired
	private BasicMasterService basicMasterService;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@ApiOperation("Returns list of all TransportUnit")
	@GetMapping(path = "/allTranport")
	public List<TransportDto> getAllTransportUnit() {
		log.info("Enter into getAllTransportUnit service");
		return this.basicMasterService.findAllTUByActiveStatus().stream()
				.map(transport -> this.mapper.map(transport, TransportDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Creates a new Ticket Box Master returning status 200 when persisted successfully.")
	@PostMapping("/saveTicketBoxMaster")
	public ResponseEntity<ResponseStatus> saveTicketBoxMaster(@RequestBody TicketBoxMaster ticketBoxMaster) {
		log.info("Enter into saveTicketBoxMaster service");
		return this.service.saveTicketBoxMaster(ticketBoxMaster);
	}
	
	@ApiOperation("Returns List of All Ticket Box Master")
	@GetMapping(path = "/listOfAllTicketBoxMaster/{dpCode}")
	public List<TicketBoxMasterDto> listOfAllTicketBoxMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfAllTicketBoxMaster service");
		
		return this.service.listOfAllTicketBoxMaster(dpCode).stream()
				.map(ticketBox -> this.mapper.map(ticketBox, TicketBoxMasterDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns Object of  ETM Master When Load")
	@GetMapping(path = "/etmMasterFormOnLoad")
	public ETMFormLoadDto etmMasterFormOnLoad() {
		log.info("Enter into etmMasterFormOnLoad service");
		ETMFormLoadDto etmFormLoadDto = this.service.etmMasterFormOnLoad();
		return etmFormLoadDto;
	}
	
	@ApiOperation("Creates a new ETM Master returning status 200 when persisted successfully.")
	@PostMapping("/saveETMMaster")
	public ResponseEntity<ResponseStatus> saveETMMaster(@RequestBody ETMMaster etmMaster) {
		log.info("Enter into saveTicketBoxMaster service");
		return this.service.saveETMMaster(etmMaster);
	}
	
	@ApiOperation("Returns List of All ETM  Master")
	@GetMapping(path = "/listOfAllETMMaster/{dpCode}")
	public List<ETMMasterDto> listOfAllETMMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfAllETMMaster service");
		return this.service.listOfAllETMMaster(dpCode).stream()
				.map(etm -> this.mapper.map(etm, ETMMasterDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns list of all TransportUnit")
	@GetMapping(path = "/allTranportByDepot/{dpCode}")
	public List<TransportDto> allTranportByDepot(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into allTranportByDepot service");
		DepotMaster depot = depotRepo.findByDepotCode(dpCode);
		return this.basicMasterService.allTransportMasterByDepot(depot.getId()).stream()
				.map(transport -> this.mapper.map(transport, TransportDto.class)).collect(Collectors.toList());
	}

}
