package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.ETMFormLoadDto;
import com.idms.base.dao.entity.ETMMaster;
import com.idms.base.dao.entity.TicketBoxMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface TicketMgmtService {

	ResponseEntity<ResponseStatus> saveTicketBoxMaster(TicketBoxMaster ticketBoxMaster);

	List<TicketBoxMaster> listOfAllTicketBoxMaster(String dpCode);

	ETMFormLoadDto etmMasterFormOnLoad();

	ResponseEntity<ResponseStatus> saveETMMaster(ETMMaster etmMaster);

	List<ETMMaster> listOfAllETMMaster(String dpCode);


}
