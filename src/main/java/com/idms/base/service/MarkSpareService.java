package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareConductorDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareDriverdetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareDto;
import com.idms.base.dao.entity.MarkSpareEntity;
import com.idms.base.support.persist.ResponseStatus;

@Service
public interface MarkSpareService {
	
	ResponseEntity<ResponseStatus> addMarkSpareDetails(MarkSpareEntity markSpareEntity,String depoCode);
	List<MarkSpareDto>getMarkSpareDetails(String depoCode);
	List<MarkSpareBusDetailsDto> getAllBusRegisterNumbers(String depoCode);
	List<MarkSpareDriverdetailsDto> getAllDriverNames(String depoCode);
	List<MarkSpareConductorDetailsDto> getAllConductorNames(String depoCode);
}
