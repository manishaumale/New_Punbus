package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.ManageTicketBoxDto;
import com.idms.base.api.v1.model.dto.MasterStockDto;
import com.idms.base.api.v1.model.dto.MasterStockSerialDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementFormDto;
import com.idms.base.api.v1.model.dto.TicketManagementBoxParentDto;
import com.idms.base.dao.entity.TicketBoxManagementEntity;
import com.idms.base.dao.entity.TicketBoxManagementParentEntity;
import com.idms.base.support.persist.ResponseStatus;

public interface TicketBoxManagementService {

	TicketBoxManagementFormDto getTicketBoxMgmtFormLoad(String dpCode, String dpCode2);
	
	ResponseEntity<ResponseStatus>  addTicketBoxMgmt(List<ManageTicketBoxDto> ticketBoxManagementEntity);

	TicketBoxManagementDto getAllTicketBoxManagementFormLoad(Integer id);

	List<MasterStockDto> getStockTicketDetailsList(Integer id);

	List<MasterStockSerialDto> getTicketBooks(Integer denoId, Integer transportId);

	List<TicketManagementBoxParentDto> getTicketDetailsList();

}
