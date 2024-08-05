package com.idms.base.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.FuelTankCapacityInDepoDto;

@Service
public interface DepotMasterService {

	FuelTankCapacityInDepoDto findFuelCapacityInDepo(String depotCode);
	List<FuelTankCapacityInDepoDto> findFuelCapacityInAllDepos();
}
