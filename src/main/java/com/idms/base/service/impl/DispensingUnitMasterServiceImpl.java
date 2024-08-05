package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.DispensingUnitTypeDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.DispensingUnitTypeRepository;
import com.idms.base.service.DispensingUnitMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DispensingUnitMasterServiceImpl  implements DispensingUnitMasterService{
	
	@Autowired
	DispensingUnitTypeRepository dispensingUnitTypeRepository;
	
	@Autowired
	DispensingUnitRepository dispensingUnitRepository;
	
	@Autowired
	DepotMasterRepository depotRepository;
	
	@Autowired
	DispensingUnitRepository dispensingRepository;
	
	
	@Override
	public List<DispensingUnitTypeDto> dispensingUnitMasterFormOnLoad() {
	log.info("Entering into dispensingUnitMasterFormOnLoad service");
	List<DispensingUnitTypeDto> dispensingListList = new ArrayList<>();
	try {
		  dispensingListList = dispensingUnitTypeRepository.findAllByStatus(true).stream()
				.map(dispensingUnitTypeDto -> new DispensingUnitTypeDto(dispensingUnitTypeDto.getId(), dispensingUnitTypeDto.getDisUnitTypeName()))
				.collect(Collectors.toList());


	} catch (Exception e) {
		e.printStackTrace();
	}

	return dispensingListList;
}

	@Override
	public ResponseEntity<ResponseStatus> saveDispensingUnitMaster(DispensingUnitMaster dispensingUnitMaster) {
		log.info("Entering into saveDispensingUnitMaster service");
		try {
			if (dispensingUnitMaster.getId() == null) {
				dispensingUnitMaster.setIsDeleted(false);
				if(dispensingUnitMaster.getDepotCode() != null) {
					DepotMaster depotMaster = depotRepository.findByDepotCode(dispensingUnitMaster.getDepotCode());
					dispensingUnitMaster.setDepot(depotMaster);
				}
				dispensingUnitMaster.setCurrentReading(dispensingUnitMaster.getInitialReading());
				dispensingUnitRepository.save(dispensingUnitMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Dispensing Unit master has been persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}

	@Override
	public List<DispensingUnitMaster> listOfAllDispensingUnitMaster(String depotCode){
		log.info("Entering into listOfAllDispensingUnitMaster service");
		DepotMaster depotMaster = null;
		List<DispensingUnitMaster> dispeseMasterList = null;
		try {
			depotMaster = depotRepository.findByDepotCode(depotCode);
			dispeseMasterList = dispensingUnitRepository.findAllByDeletedFlagAndDepotId(false,depotMaster.getId());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dispeseMasterList;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateDispensingUnitMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateDispensingUnitMasterStatusFlag service");
		try {
			int i = dispensingUnitRepository.updateDUMasterStatusFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Status has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> deleteDispensingUnitMaster(Integer id, Boolean flag) {
		log.info("Entering into updateTripMasterIsDeletedFlag service");
		try {
			int i = dispensingUnitRepository.updateDispensingUnitMasterIsDeletedFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Dispense Unit Master has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
	
	@Transactional()
	@Override
	public void updateCurrentReading(Double currentReading, Integer id) {
		log.info("Entering into updateCurrentReading service");
		try{
			dispensingRepository.updateCurrentReading(currentReading,id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
