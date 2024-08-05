package com.idms.base.service;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.EtmTBAssignmentDto;
import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.support.persist.ResponseStatus;

public interface ETMIntegrationService {

	ResponseEntity<ResponseStatus> submitETMData(EtmTBAssignment etmTBAssignment);
	
	ResponseEntity<EtmTBAssignmentDto> fetchEtmDetails(String wayBillNo);
	
	ResponseEntity<EtmTBAssignmentDto> fetchPartialEtmDetails(String wayBillNo);


}
