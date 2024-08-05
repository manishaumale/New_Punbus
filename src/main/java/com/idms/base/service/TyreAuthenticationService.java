package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.TyreAuthenticationDto;
import com.idms.base.dao.entity.TyreAuthenticationEntity;
import com.idms.base.support.persist.ResponseStatus;

@Service
public interface TyreAuthenticationService {
	
	ResponseEntity<ResponseStatus> saveTyreAuthentication (String depoCode,TyreAuthenticationEntity tyreAuthenticationEntity);
	List<TyreAuthenticationDto> getTyreAuthenticationDetails(String depoCode);
	List<TyreAuthenticationDto> fetchTyreAuthenticationDetails(String depoCode);
	
	

}
