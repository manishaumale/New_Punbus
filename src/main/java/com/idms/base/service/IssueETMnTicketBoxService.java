package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.OnlineBookingDetailsDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.TicketManagementBoxParentDto;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.ETMMaster;
import com.idms.base.dao.entity.TicketBoxMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface IssueETMnTicketBoxService {

	List<ConductorMaster> getAllConductorMasterByDpcode(String dpCode);
	List<ETMMaster> listOfAllETMMaster(String dpCode);
	List<TicketBoxMaster> listOfAllTicketBoxMaster(String dpCode);
	RouteMasterDto findByRouteId(Integer id);
	List<OnlineBookingDetailsDto> findById(Integer id);
	ResponseEntity findByCondutorId(Integer id);
	TicketManagementBoxParentDto findByTicketBoxId(Integer id);
	ResponseEntity<ResponseStatus> addissueEtmTicket(IssueEtmTicketBoxDto issueEtmTicketBox);
	List<IssueEtmTicketBoxDto> getIssueEtmTicketBoxList(String depocode);
	IssueEtmTicketBoxDto getIssueEtmTicketBoxList(Integer id);
	public ResponseEntity<ResponseStatus> assignTicketBox(Integer id,Integer ticketBox,Integer ticketBoxManagement);


	
}
