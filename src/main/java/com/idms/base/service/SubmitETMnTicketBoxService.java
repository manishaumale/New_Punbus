package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.ConcessionTicketDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.SubmitEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.SubmitEtmTicketViewDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementDto;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface SubmitETMnTicketBoxService {
	
	
	List<ConductorMaster> getAllConductorMasterByDpcode(String dpCode);

	IssueEtmTicketBoxDto getIssueTicketDetails(Integer conductorId);

	List<ConcessionTicketDto> getConsessionTicketList();

	ResponseEntity<ResponseStatus> submitEtmTicketBox(SubmitEtmTicketBoxDto submitEtmTicketBox);

	SubmitEtmTicketBoxDto getSubmitEtmTicketBoxList(Integer id);

	List<SubmitEtmTicketViewDto> getAllSubmitEtmByDepot(Integer id);
	
	public ResponseEntity<ResponseStatus> submitTicketBox(List<TicketBoxManagementDto> ticketBoxDto);

	SubmitEtmTicketBoxDto viewSubmitEtmTicketBoxList(Integer id);


}
