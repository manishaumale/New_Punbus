package com.idms.base.service.impl;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.FuelTankCapacityInDepoDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.service.DepotMasterService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DepoMasterServiceImpl implements DepotMasterService {

	@Autowired(required = true)
	private DepotMasterRepository depotMasterRepository;
	@Autowired
	private FuelTankMasterRepository fuelTankMasterRepository;

	@Override
	public FuelTankCapacityInDepoDto findFuelCapacityInDepo(String depotCode) {
		log.info("Entering into findFuelCapacityInDepo service");
		List<FuelTankMaster> fuelMasterList = null;
		DepotMaster depotMaster = null;
		FuelTankCapacityInDepoDto fuelTankCapacityInDepo = null;
		try {

			depotMaster = depotMasterRepository.findByDepotCode(depotCode);
			if (depotMaster== null) {
				log.info("depotCode not found");
				return null;
			}
			fuelMasterList = fuelTankMasterRepository.findAllByDepotId(depotMaster.getId());
			DoubleSummaryStatistics fuelCapacityInDepo = fuelMasterList.stream()
					.mapToDouble(tankcapacity -> tankcapacity.getCapacity().getCapacity()).summaryStatistics();
			fuelTankCapacityInDepo = new FuelTankCapacityInDepoDto(depotMaster.getDepotName(),
					fuelCapacityInDepo.getCount(), fuelCapacityInDepo.getSum());
			return fuelTankCapacityInDepo;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fuelTankCapacityInDepo;
	}
	
	

	@Override
	public List<FuelTankCapacityInDepoDto> findFuelCapacityInAllDepos() {
		log.info("Entered into To findFuelCapacityInAllDepos");
		List<FuelTankCapacityInDepoDto> fuelCapacityInAllDepos = null;
		try {
			List<DepotMaster> allDepoDetails = depotMasterRepository.findAll();
			fuelCapacityInAllDepos = allDepoDetails.stream().map(depo -> {
				DoubleSummaryStatistics fuelCapacityInDepo = fuelTankMasterRepository.findAllByDepotId(depo.getId())
						.stream().mapToDouble(tankCapacity -> tankCapacity.getCapacity().getCapacity())
						.summaryStatistics();
				FuelTankCapacityInDepoDto fuelTankCapacityInDep = new FuelTankCapacityInDepoDto(depo.getDepotName(),
						fuelCapacityInDepo.getCount(), fuelCapacityInDepo.getSum());
				return fuelTankCapacityInDep;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fuelCapacityInAllDepos;
	}
}
