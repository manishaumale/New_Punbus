package com.idms.base.api.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.ETMMasterDto;
import com.idms.base.api.v1.model.dto.IssueEtmObjDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.ResponseMessage;
import com.idms.base.api.v1.model.dto.RouteOnlineBookingDetailsDto;
import com.idms.base.api.v1.model.dto.TicketBoxMasterDto;
import com.idms.base.api.v1.model.dto.TicketManagementBoxParentDto;
//import com.idms.base.master.serviceImpl.ZtestExcel;
import com.idms.base.service.BasicMasterService;
import com.idms.base.service.IssueETMnTicketBoxService;
import com.idms.base.service.impl.etmExcelUploadServiceImpl;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Mynk on 29/01/2022
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/IssueTicketBoxETM")
@Log4j2
public class IssueETMnTicketBoxController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private IssueETMnTicketBoxService issueETMnTicketBoxservice;
	
	@Autowired
	private BasicMasterService basicMasterService;
	
	@Autowired
	etmExcelUploadServiceImpl fileService;
	
	
	/*
	 * Method to return all data from conductor master on bases of depo Id
	 * 
	 * 
	 * */
	@ApiOperation("Returns list of all Conductor Master")
	@GetMapping(path = "/allConductorMaster/{dpCode}")
	public List<ConductorMasterDto> getAllConductorMasterByDpcode(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllConductorMaster service");
		return this.issueETMnTicketBoxservice.getAllConductorMasterByDpcode(dpCode).stream()
				.map(conductorMasterDto -> this.mapper.map(conductorMasterDto, ConductorMasterDto.class)).collect(Collectors.toList());
	}
	
	/*
	 * Method to return all data from ETM master on bases of depo Id
	 * 
	 * 
	 * */
	@ApiOperation("Returns List of All ETM  Master")
	@GetMapping(path = "/listOfAllETMMaster/{dpCode}")
	public List<ETMMasterDto> listOfAllETMMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfAllETMMaster service");
		return this.issueETMnTicketBoxservice.listOfAllETMMaster(dpCode).stream()
				.map(etm -> this.mapper.map(etm, ETMMasterDto.class)).collect(Collectors.toList());
	}
	
	/*
	 * Method to return all data from Ticket Box Master master on bases of depo Id
	 * 
	 * 
	 * */

	@ApiOperation("Returns List of All Ticket Box Master")
	@GetMapping(path = "/listOfAllTicketBoxMaster/{dpCode}")
	public List<TicketBoxMasterDto> listOfAllTicketBoxMaster(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into listOfAllTicketBoxMaster service");
		
		return this.issueETMnTicketBoxservice.listOfAllTicketBoxMaster(dpCode).stream()
				.map(ticketBox -> this.mapper.map(ticketBox, TicketBoxMasterDto.class)).collect(Collectors.toList());
	}

	
	/*
	 * Method to return all data from Route & online booking details by conductor id
	 * 
	 * 
	 * */
	
	@ApiOperation("")
	@GetMapping(path = "/routeOnlineBookingDetails/{id}")
	public ResponseEntity getrouteOnlineBookingDetails(@PathVariable("id") Integer id) {
		
		
		ResponseEntity dto = this.issueETMnTicketBoxservice.findByCondutorId(id);
		
		
		return dto;
	}
	
	/* * Method to return all data from Ticket Box Master master on bases of depo Id
			 * 
			 * Check one
			 * */
	
	

	@ApiOperation(" Returning All TicketBoxManagement Dtls")
	@GetMapping(value = "/getTicketBoxInfo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TicketManagementBoxParentDto getTicketBoxInfo(
			@PathVariable("id") Integer id) {
		TicketManagementBoxParentDto ticketBoxInfoDto = issueETMnTicketBoxservice.findByTicketBoxId(id);
		return ticketBoxInfoDto;
	}
	
	/* * Method to Save Data of Issue ETM Ticket Box
	 * 
	 * Check One
	 * */
	
	@ApiOperation("Save Issue ETM Ticket Details")
	@PostMapping(value = "/addissueEtmTicket", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseStatus> addissueEtmTicket(@RequestBody IssueEtmTicketBoxDto issueEtmTicketBox)
	{
		log.info("Inserting All TicketBoxManagement Service");
		return issueETMnTicketBoxservice.addissueEtmTicket(issueEtmTicketBox);
	}
	
	/* * Method to return all data from IssueEtmTicketBox
	 * 
	 *       
	 * */

	@ApiOperation("Returning All Issue ETM Dtls")
	@GetMapping(value = "/getIssueEtmTicketBoxList/{depocode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IssueEtmTicketBoxDto> getIssueEtmTicketBoxList(@PathVariable ("depocode") String depocode) {
		List<IssueEtmTicketBoxDto> ticketBoxList = issueETMnTicketBoxservice.getIssueEtmTicketBoxList(depocode);
		return ticketBoxList;
}
	
	@GetMapping(value = "/getIssueEtmTicketBoxById/{id}")
	public IssueEtmTicketBoxDto getIssueEtmTicketBoxById(@PathVariable("id") Integer id) {
		return issueETMnTicketBoxservice.getIssueEtmTicketBoxList(id);

	}

	@PostMapping("/uploadExcel")
	public List<ResponseEntity<ResponseMessage>> uploadFile(@RequestParam String type,@RequestParam("file") MultipartFile file,@RequestParam String dpCode) {
		List<ResponseEntity<ResponseMessage>> tutorials = new ArrayList<>();
		
		 if (etmExcelUploadServiceImpl.hasExcelFormat(file)) {
		      try {
		    	if(type.equals("IssueEtm")) {
			    	tutorials = fileService.save(file,type,dpCode);
			    	if(tutorials.size()==0){
				        tutorials.add(new ResponseEntity<>(new ResponseMessage("Issue Etm file is uploaded successfully"), HttpStatus.OK));
			    	}
		  		}
		    	if(type.equals("SubmitEtm")){
		    		tutorials = fileService.fetchDataAndSaveSubmitEtmExcelSheet(file);
		    	}
//		        message = "Uploaded the file successfully: " + file.getOriginalFilename();
		        return tutorials;
		      } catch (Exception e) {
		    	  e.printStackTrace();
		        tutorials.add(new ResponseEntity<>(new ResponseMessage("Could not Upload file! Please upload valid file."), HttpStatus.FORBIDDEN));
		        return tutorials;
		      }
		    }else {
		        tutorials.add(new ResponseEntity<>(new ResponseMessage("Please Upload an Excel file!"), HttpStatus.FORBIDDEN));
		    }
	        
		return tutorials;
	}
	
	@GetMapping("/getExcelObject")
	public List<IssueEtmObjDto> getExcelObj(@RequestParam("file") MultipartFile file){
		try {
			return fileService.returnIssueEtmObj(file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@PostMapping("/assignTicketBox")
	public ResponseEntity<ResponseStatus> assignTicketBox(@RequestParam Integer id,@RequestParam Integer ticketBox,@RequestParam Integer ticketBoxManagement) {
		return issueETMnTicketBoxservice.assignTicketBox(id,ticketBox,ticketBoxManagement);

	}

}
