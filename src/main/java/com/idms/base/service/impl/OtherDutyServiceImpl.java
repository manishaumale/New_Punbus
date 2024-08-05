package com.idms.base.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.DutyTypeDto;
import com.idms.base.api.v1.model.dto.OtherDutyDto;
import com.idms.base.api.v1.model.dto.OtherDutyOnLoadDto;
import com.idms.base.dao.entity.AdBlueUsed;
import com.idms.base.dao.entity.AddBlueDrumMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DUReadingHistory;
import com.idms.base.dao.entity.DieselIssuedForOtherPurpose;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.DutyType;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.MobilOilDrumMaster;
import com.idms.base.dao.entity.MobilOilUsed;
import com.idms.base.dao.entity.OtherDuty;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.DutyTypeRepository;
import com.idms.base.dao.repository.OtherDutyRepository;
import com.idms.base.service.OtherDutyService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OtherDutyServiceImpl implements OtherDutyService{
	
	@Autowired
	private DriverMasterRepository driverMasterRepository;
	
	
	@Autowired
	private ConductorMasterRepository conductorMasterRepository;
	
	@Autowired
	private DutyTypeRepository dutyTypeRepository;
	
	@Autowired
	private OtherDutyRepository otherDutyRepository;
	
	
	
	@Override
	public OtherDutyOnLoadDto otherDutyOnLoad(String dpCode) {
		log.info("Entering into otherDutyOnLoad service");
		OtherDutyOnLoadDto dto = new OtherDutyOnLoadDto();
		try {
			
			List<DriverMasterDto> driverList = driverMasterRepository.findAllByDepotByStatus(dpCode).stream()
					.map(driver -> new DriverMasterDto(driver.getId(), driver.getDriverName()))
					.collect(Collectors.toList());
			
			List<ConductorMasterDto> conductorList = conductorMasterRepository.findAllByDepotByStatus(dpCode).stream()
					.map(conductor -> new ConductorMasterDto(conductor.getId(), conductor.getConductorName()))
					.collect(Collectors.toList());
			
			List<DutyTypeDto> dutyTypeList = dutyTypeRepository.findAllByStatus().stream()
					.map(duty -> new DutyTypeDto(duty.getId(), duty.getDutyTypeName()))
					.collect(Collectors.toList());
			
			if(driverList!=null && driverList.size() > 0) {
				dto.setDriverList(driverList);
			}
			
			if(conductorList!=null && conductorList.size() > 0) {
				dto.setConductorsList(conductorList);
			}
			
			if(dutyTypeList!=null && dutyTypeList.size() > 0) {
				dto.setDutyTypeList(dutyTypeList);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return dto;
	}



	@Override
	public ResponseEntity<ResponseStatus> saveOtherDuty(OtherDuty otherDuty) {
		log.info("Entering into saveOtherPurpose service");
		try {
			if(null == otherDuty.getDriverMaster().getId()){
				otherDuty.setDriverMaster(null);
			}
			if(null == otherDuty.getConductorMaster().getId()){
				otherDuty.setConductorMaster(null);
			}
			if (otherDuty.getId() == null) {
				otherDuty.setIsDeleted(false);
				otherDutyRepository.save(otherDuty);
				}
				
				return new ResponseEntity<>(
						new ResponseStatus("Record persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}



	@Override
	public List<OtherDuty> listOfAllOtherDuty() {
		log.info("Entering into listOfAllOtherDuty service");
		List<OtherDuty> otherDutyList = null;
		DriverMaster driverMaster = new DriverMaster();
		ConductorMaster conductorMaster = new ConductorMaster();
		DutyType dutyType = new DutyType();
		try {
			otherDutyList = otherDutyRepository.findAllByIsDeleted(false);
			for(OtherDuty otherObj : otherDutyList){
				if(otherObj.getDriverMaster() == null){
					driverMaster.setId(null);
					driverMaster.setDriverName("");
					otherObj.setDriverMaster(driverMaster);
				}if(otherObj.getConductorMaster() == null){
					conductorMaster.setId(null);
					conductorMaster.setConductorName("");
					otherObj.setConductorMaster(conductorMaster);
				}if(otherObj.getDutyType() == null){
					dutyType.setId(null);
					dutyType.setDutyTypeName("");
					otherObj.setDutyType(dutyType);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return otherDutyList;
	}



	@Override
	public ResponseEntity<ResponseStatus> fetchAvailabilityStatus(String driverFlag, Integer driverOrConductorId) {
		if(driverFlag != null && driverFlag.equals("true")){
		List<Object[]> driverList = otherDutyRepository.fetchAvailabilityDriverStatus(driverOrConductorId);
		if(driverList.size() > 0){
			return new ResponseEntity<>(new ResponseStatus("This driver is already allocated till "+driverList.get(0)[1]+"", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		}else{
			List<Object[]> ConductorList = otherDutyRepository.fetchAvailabilityConductorStatus(driverOrConductorId);
			if (ConductorList.size() > 0) {
				return new ResponseEntity<>(new ResponseStatus("This conductor is already allocated till "+ConductorList.get(0)[1]+"", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(
				new ResponseStatus("",HttpStatus.OK),HttpStatus.OK);
	}

}
