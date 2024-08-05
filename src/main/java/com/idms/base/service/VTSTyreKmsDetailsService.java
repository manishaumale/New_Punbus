package com.idms.base.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.VTSTyreKmsDetailsDto;

public interface VTSTyreKmsDetailsService {	
	
	List<VTSTyreKmsDetailsDto>  getVTSTyreKmsDetailsDto(String busNumber , String date);
}
