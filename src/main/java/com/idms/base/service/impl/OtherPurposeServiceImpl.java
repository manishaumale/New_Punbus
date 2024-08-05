package com.idms.base.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.AddBlueDrumMasterDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.FuelTankMasterDto;
import com.idms.base.api.v1.model.dto.FuelTypeDto;
import com.idms.base.api.v1.model.dto.MobilOilDrumMasterDto;
import com.idms.base.api.v1.model.dto.OtherPurposeDto;
import com.idms.base.api.v1.model.dto.OtherPurposeFormLoadDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.AdBlueUsed;
import com.idms.base.dao.entity.AddBlueDrumMaster;
import com.idms.base.dao.entity.DUReadingHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DieselIssuedForOtherPurpose;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.MobilOilDrumMaster;
import com.idms.base.dao.entity.MobilOilUsed;
import com.idms.base.dao.repository.AdBlueUsedRepository;
import com.idms.base.dao.repository.AddBlueDrumMasterRepository;
import com.idms.base.dao.repository.DUReadingHistoryRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DieselIssuedForOtherPurposeRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.FuelTypeRepository;
import com.idms.base.dao.repository.MobilOilDrumMasterRepository;
import com.idms.base.dao.repository.MobilOilUsedRepository;
import com.idms.base.dao.repository.OtherPurposeRepository;
import com.idms.base.service.OtherPurposeService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OtherPurposeServiceImpl implements OtherPurposeService{
	
	@Autowired
	OtherPurposeRepository otherPurposeRepository;
	
	@Autowired
	FuelTypeRepository fuelTypeRepository;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	MobilOilDrumMasterRepository mobilOilDrumMasterRepository;
	
	@Autowired
	AddBlueDrumMasterRepository addBlueDrumMasterRepository;
	
	@Autowired
	FuelTankMasterRepository fuelTankMasterRepository;
	
	@Autowired
	DispensingUnitRepository dispensingUnitRepository;
	
	@Autowired
	DieselIssuedForOtherPurposeRepository dieselIssuedForOtherPurposeRepository;
	
	@Autowired
	AdBlueUsedRepository adBlueUsedRepository;
	
	@Autowired
	MobilOilUsedRepository mobilOilUsedRepository;
	
	@Autowired
	DUReadingHistoryRepository duReadingHistoryRepo;
	
	
	@Override
	public OtherPurposeFormLoadDto otherPurposeDataOnLoad(String depotCode) {
		log.info("Entering into otherPurposeDataOnLoad service");
		OtherPurposeFormLoadDto otherPurposeFormLoadDto = new OtherPurposeFormLoadDto();
		try {
			Integer depoMasterId = depotRepo.getIdByDepoName(depotCode);

			List<OtherPurposeDto> otherPurposeList = otherPurposeRepository.findAllByStatus(true).stream()
					.map(other -> new OtherPurposeDto(other.getId(), other.getPurposeName())).collect(Collectors.toList());

			if (otherPurposeList != null && otherPurposeList.size() > 0)
				otherPurposeFormLoadDto.setOtherpurposeList(otherPurposeList);
			
			List<FuelTypeDto> fuelTypeList = fuelTypeRepository.findAllByStatus(true).stream()
					.map(fuelType -> new FuelTypeDto(fuelType.getId(), fuelType.getFuelTypeName())).collect(Collectors.toList());

			if (fuelTypeList != null && fuelTypeList.size() > 0)
				otherPurposeFormLoadDto.setFuelTypeList(fuelTypeList);
			
			List<MobilOilDrumMasterDto> mobilList =  mobilOilDrumMasterRepository.findAllDrumsByDepot(true,false,depotCode).stream()
					.map(mobil -> new MobilOilDrumMasterDto(mobil.getId(), mobil.getNameOfDrum(),mobil.getTotalCapacity())).collect(Collectors.toList());

			if (mobilList != null && mobilList.size() > 0)
				otherPurposeFormLoadDto.setMobilList(mobilList);
			
			
			 List<AddBlueDrumMasterDto> addBlueList =  addBlueDrumMasterRepository.findAllDrumsByDepot(true,false,depotCode).stream()
					.map(addBlue -> new AddBlueDrumMasterDto(addBlue.getId(), addBlue.getNameOfDrum(),addBlue.getTotalCapacity())).collect(Collectors.toList());

			if (addBlueList != null && addBlueList.size() > 0)
				otherPurposeFormLoadDto.setAddBlueList(addBlueList);
			
			List<FuelTankMasterDto> fuelTankList = fuelTankMasterRepository.findAllByStatusAndDepotId(true,depoMasterId).stream()
					.map(tank -> new FuelTankMasterDto(tank.getId(), tank.getTankName(),tank.getCurrentValue()!= null ? tank.getCurrentValue() : 0 )).collect(Collectors.toList());

			if (fuelTankList != null && fuelTankList.size() > 0)
				otherPurposeFormLoadDto.setFuelTankList(fuelTankList);
			
			List<DispensingUnitMasterDto> dispensingList = dispensingUnitRepository.findAllByStatusAndDepotId(true,depoMasterId).stream()
					.map(dispense -> new DispensingUnitMasterDto(dispense.getId(), dispense.getDisUnitName())).collect(Collectors.toList());

			if (dispensingList != null && dispensingList.size() > 0)
				otherPurposeFormLoadDto.setDispensingList(dispensingList);
			
			for (MobilOilDrumMasterDto mobilDrumObj : mobilList) {
				Float mobilOilUsed = mobilOilUsedRepository.toalAddBlueUsedFromDrum(mobilDrumObj.getId());
				if (mobilOilUsed != null && mobilDrumObj.getTotalCapacity() != null) {
					Float availableStock = mobilDrumObj.getTotalCapacity() - mobilOilUsed;
					String avialStock = availableStock.toString();
					mobilDrumObj.setAvailableStock(avialStock);
				} else if (mobilOilUsed == null && mobilDrumObj.getTotalCapacity() != null) {
					mobilDrumObj.setAvailableStock(mobilDrumObj.getTotalCapacity().toString());
				}
			}
			
			if (mobilList != null && mobilList.size() > 0)
				otherPurposeFormLoadDto.setMobilList(mobilList);
			for (AddBlueDrumMasterDto mobilDrumObj : addBlueList) {

				Float addBlueUsed = adBlueUsedRepository.toalMobilUsedFromDrum(mobilDrumObj.getId());
				if (addBlueUsed != null && mobilDrumObj.getTotalCapacity() != null) {
					Float availableStock = mobilDrumObj.getTotalCapacity() - addBlueUsed;
					String avialStock = availableStock.toString();
					mobilDrumObj.setAvailableStock(avialStock);
				} else if (addBlueUsed == null && mobilDrumObj.getTotalCapacity() != null) {
					mobilDrumObj.setAvailableStock(mobilDrumObj.getTotalCapacity().toString());
				}
			}	
			
			if (addBlueList != null && addBlueList.size() > 0)
				otherPurposeFormLoadDto.setAddBlueList(addBlueList);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return otherPurposeFormLoadDto;
	}

	
	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveOtherPurpose(DieselIssuedForOtherPurpose otherPurpose) {
		log.info("Entering into saveOtherPurpose service");
		Double fuelLeft = null;
		try {
			
			DepotMaster depotMaster = depotRepo.findByDepotCode(otherPurpose.getDepotCode());
			if (otherPurpose.getId() == null) {
				otherPurpose.setIsDeleted(false);
				otherPurpose.setDepot(depotMaster);
				dieselIssuedForOtherPurposeRepository.save(otherPurpose);
				}
			
			if(otherPurpose.getFuelTankMaster() != null && otherPurpose.getFuelTankMaster().getId() != null){
				FuelTankMaster fuelTank = fuelTankMasterRepository.findById(otherPurpose.getFuelTankMaster().getId()).get();
				if(fuelTank.getCurrentValue() != null && otherPurpose.getDieselIssued() != null){
					fuelLeft =  fuelTank.getCurrentValue() - otherPurpose.getDieselIssued().doubleValue();
					fuelTank.setCurrentValue(fuelLeft);
					fuelTankMasterRepository.save(fuelTank);
					DUReadingHistory duHistory = new DUReadingHistory();
					duHistory.setDispensingUnit(otherPurpose.getDispensingUnitMaster());
					duHistory.setDuStartReading(otherPurpose.getDuStartReading());
					duHistory.setDuEndReading(otherPurpose.getDuEndReading());
					duHistory.setIssuedDiesel(otherPurpose.getDieselIssued());
					duHistory.setFuelTank(otherPurpose.getFuelTankMaster());
					duReadingHistoryRepo.save(duHistory);
				}
			}else if(otherPurpose.getMobilOilDrumMaster() != null && otherPurpose.getMobilOilDrumMaster().getId() != null){
				MobilOilDrumMaster mobilDrumMaster = mobilOilDrumMasterRepository.findById(otherPurpose.getMobilOilDrumMaster().getId()).get();
				if(otherPurpose.getDieselIssued() != null){
					MobilOilUsed mobilUsed = new MobilOilUsed();
					mobilUsed.setIsOutSide(false);
					mobilUsed.setMobilOilDrumMaster(mobilDrumMaster);
					mobilUsed.setQuantity(otherPurpose.getDieselIssued());
					mobilOilUsedRepository.save(mobilUsed);
				}
			}else if(otherPurpose.getAddBlueDrumMaster() != null && otherPurpose.getAddBlueDrumMaster().getId() != null){
				AddBlueDrumMaster addBlueDrumMaster = addBlueDrumMasterRepository.findById(otherPurpose.getAddBlueDrumMaster().getId()).get();
				if( otherPurpose.getDieselIssued() != null){
					AdBlueUsed addBlueUsed = new AdBlueUsed();
					addBlueUsed.setAddBlueDrumMaster(addBlueDrumMaster);
					addBlueUsed.setIsOutSide(false);
					addBlueUsed.setQuantity(otherPurpose.getDieselIssued());
					adBlueUsedRepository.save(addBlueUsed);
				}
			}
				
				return new ResponseEntity<>(
						new ResponseStatus("Fuel issued successfully.", HttpStatus.OK),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}


	@Override
	public List<DieselIssuedForOtherPurpose> listOfAllOtherPurpose(String depotCode) {
		log.info("Entering into listOfAllOtherPurpose service");
		List<DieselIssuedForOtherPurpose> fuelOtherPurposeList = null;
		MobilOilDrumMaster mobilMaster = new MobilOilDrumMaster();
		AddBlueDrumMaster addBlue = new AddBlueDrumMaster();
		FuelTankMaster tankMaster = new FuelTankMaster();
		DispensingUnitMaster dispenseObj = new DispensingUnitMaster();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(depotCode);
			fuelOtherPurposeList = dieselIssuedForOtherPurposeRepository.findAllByStatusAndDepotId(false,depotMaster.getId());
			for(DieselIssuedForOtherPurpose otherObj : fuelOtherPurposeList){
				if(otherObj.getAddBlueDrumMaster() == null){
					addBlue.setId(null);
					addBlue.setNameOfDrum("");
					otherObj.setAddBlueDrumMaster(addBlue);
				}if(otherObj.getFuelTankMaster() == null){
					tankMaster.setId(null);
					tankMaster.setTankName("");
					otherObj.setFuelTankMaster(tankMaster);
				}if(otherObj.getMobilOilDrumMaster() == null){
					mobilMaster.setId(null);
					mobilMaster.setNameOfDrum("");
					otherObj.setMobilOilDrumMaster(mobilMaster);
				}if(otherObj.getDispensingUnitMaster() == null){
					dispenseObj.setId(null);
					dispenseObj.setDisUnitName("");
					otherObj.setDispensingUnitMaster(dispenseObj);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return fuelOtherPurposeList;
	}

}
