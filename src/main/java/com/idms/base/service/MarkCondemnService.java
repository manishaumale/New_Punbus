package com.idms.base.service;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.MarkCondemndto;
import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.dao.entity.MarkCondemn;
import com.idms.base.support.persist.ResponseStatus;

@Service
public interface MarkCondemnService 
{
	
	ResponseEntity<ResponseStatus> addmarkCondemnDtls(MarkCondemn markCondemn,String depoCode);
	 List<MarkCondemndto> getMarkCondemnDtls(String depoCode);
	 List<MarkSpareBusDetailsDto> getAllBusDetails(String depoCode);

}
