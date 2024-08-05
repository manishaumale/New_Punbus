package com.idms.base.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.BookReadingClosingDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.FuelTankMasterDto;
import com.idms.base.api.v1.model.dto.ReadingMasterDto;
import com.idms.base.api.v1.model.dto.RecieveDieselFormLoadDto;
import com.idms.base.api.v1.model.dto.RoleDto;
import com.idms.base.api.v1.model.dto.TankCurrentValAndCapacityDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.DUReadingHistory;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.ReadingMaster;
import com.idms.base.dao.entity.RecieveDieselSupplyMaster;
import com.idms.base.dao.entity.RoleDieselSupply;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.repository.BookreadingClosingRepo;
import com.idms.base.dao.repository.DUReadingHistoryRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.ReadingMasterRepository;
import com.idms.base.dao.repository.RecieveDieselSupplyMasterRepository;
import com.idms.base.dao.repository.RoleDieselSupplyRepository;
import com.idms.base.dao.repository.RoleRepository;
import com.idms.base.dao.repository.TankCapacityMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.RecieveDieselSupplyService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class RecieveDieselSupplyServiceImpl implements  RecieveDieselSupplyService{
	
	
	@Autowired
	RecieveDieselSupplyMasterRepository repository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	TankCapacityMasterRepository tankCapacityMasterRepository;
	
	@Autowired
	FuelTankMasterRepository fuelTankMasterRepository;
	
	@Autowired
	RoleDieselSupplyRepository roleDieselSupRepo;
	
	@Autowired
	DUReadingHistoryRepository historyRepo;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	TransportUnitRepository transportUnitRepository;
	
	@Autowired
	ReadingMasterRepository readingMasterRepository;
	
	@Autowired
	DispensingUnitRepository dispensingUnitRepository;
	
	@Autowired
	BookreadingClosingRepo bookreadingClosingRepo;
	
	
	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveRecieveDieselMaster(RecieveDieselSupplyMaster recieveDieselSupplyMaster) {
		log.info("Entering into saveRecieveDieselMaster service");
		Float newPriceAndVolume = 0.f;
		Float volumeBeforeSupply = 0.f;
		Float oldPrice = 0.f;
		Float currentPrice = 0.f;
		Float oldPriceAndVolume = 0.f;
		Float sumNewAndOld = 0.f;
		Float sumOldVolumeNewVilume = 0.f;
		TransportUnitMaster  transport  = null;
		try {
			FuelTankMaster fuelTank = fuelTankMasterRepository.findById(recieveDieselSupplyMaster.getUpdateSupply().getFuelTankMaster().getId()).get();
			if (recieveDieselSupplyMaster.getId() == null) {
				if(recieveDieselSupplyMaster.getTransportUnitMaster() != null)
			      transport = transportUnitRepository.findById(recieveDieselSupplyMaster.getTransportUnitMaster().getId()).get();
				recieveDieselSupplyMaster.setTransportUnitMaster(transport);
				recieveDieselSupplyMaster.setIsDeleted(false);
				if(recieveDieselSupplyMaster.getQuantityRecieved() != null && recieveDieselSupplyMaster.getDieselRate() != null){
					newPriceAndVolume = recieveDieselSupplyMaster.getQuantityRecieved() * recieveDieselSupplyMaster.getDieselRate();
				}
				if(recieveDieselSupplyMaster.getUpdateSupply().getVolumeBeforeSupply() != null){
					volumeBeforeSupply = recieveDieselSupplyMaster.getUpdateSupply().getVolumeBeforeSupply();
				}
				else{
					volumeBeforeSupply = recieveDieselSupplyMaster.getQuantityRecieved();
				}
				if(fuelTank.getCurrentPrice() != null){
					oldPrice = fuelTank.getCurrentPrice();
					oldPriceAndVolume = volumeBeforeSupply * oldPrice;
					sumNewAndOld = newPriceAndVolume + oldPriceAndVolume;
					sumOldVolumeNewVilume = recieveDieselSupplyMaster.getQuantityRecieved() + volumeBeforeSupply;
					currentPrice = sumNewAndOld / sumOldVolumeNewVilume;
					recieveDieselSupplyMaster.setCurrentPrice(currentPrice);
					recieveDieselSupplyMaster.setOldVolume(volumeBeforeSupply);
					recieveDieselSupplyMaster.setOldPrice(oldPrice);
					fuelTank.setCurrentPrice(currentPrice);
				}
				else{
					if(fuelTank.getCurrentValue() != null && fuelTank.getCurrentValue() > 0){
						Object[] obj = repository.fetchupdateSupplyId(fuelTank.getId());
						if(obj[0] != null){
							RecieveDieselSupplyMaster recieveObj = repository.fetchRecieceObject(Integer.parseInt(obj[0].toString()));
							
							oldPrice = recieveObj.getDieselRate();
							oldPriceAndVolume = volumeBeforeSupply * oldPrice;
							sumNewAndOld = newPriceAndVolume + oldPriceAndVolume;
							sumOldVolumeNewVilume = recieveDieselSupplyMaster.getQuantityRecieved() + volumeBeforeSupply;
							currentPrice = sumNewAndOld / sumOldVolumeNewVilume;
							recieveDieselSupplyMaster.setCurrentPrice(currentPrice);
							recieveDieselSupplyMaster.setOldVolume(volumeBeforeSupply);
							recieveDieselSupplyMaster.setOldPrice(oldPrice);
							fuelTank.setCurrentPrice(currentPrice);
						}
					}
					else{
						oldPrice = recieveDieselSupplyMaster.getDieselRate();
						recieveDieselSupplyMaster.setCurrentPrice(oldPrice);
						recieveDieselSupplyMaster.setOldVolume(volumeBeforeSupply);
						recieveDieselSupplyMaster.setOldPrice(oldPrice);
						fuelTank.setCurrentPrice(oldPrice);
					}
				}
				
				repository.save(recieveDieselSupplyMaster);
				
				for(RoleDieselSupply rds : recieveDieselSupplyMaster.getRoleList()) {
					rds.setRecieveDieselSupplyMaster(recieveDieselSupplyMaster);
					roleDieselSupRepo.save(rds);
				}
				
				DUReadingHistory history = new DUReadingHistory();
				history.setDieselSupply(recieveDieselSupplyMaster);
				history.setFuelTank(recieveDieselSupplyMaster.getUpdateSupply().getFuelTankMaster());
				history.setCurrentDiesel(recieveDieselSupplyMaster.getUpdateSupply().getVolumeAfterSupply());
				historyRepo.save(history);
				
				//FuelTankMaster fuelTank = fuelTankMasterRepository.findById(recieveDieselSupplyMaster.getUpdateSupply().getFuelTankMaster().getId()).get();
				fuelTank.setCurrentValue(recieveDieselSupplyMaster.getUpdateSupply().getVolumeAfterSupply().doubleValue());
				fuelTankMasterRepository.save(fuelTank);				
				
				return new ResponseEntity<>(
						new ResponseStatus("Recieve Diesel Supply master has been persisted successfully.", HttpStatus.OK),
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
	public List<RecieveDieselSupplyMaster> listOfAllRecieveDieselMaster() {
		log.info("Entering into listOfAllRecieveDieselMaster service");
		List<RecieveDieselSupplyMaster> dieselSupplyList = null;
		try {
			//dieselSupplyList = repository.findAll();
			dieselSupplyList = repository.findAllByIsDeleted(false);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dieselSupplyList;
	}
	
	@Override
	public RecieveDieselFormLoadDto recieveDieselFormOnLoad(String depotCode) {
		log.info("Entering into etmMasterFormOnLoad service");
		RecieveDieselFormLoadDto recieveDieselFormLoadDto = new RecieveDieselFormLoadDto();
		try {
			Integer depoMasterId = depotRepo.getIdByDepoName(depotCode);
			List<RoleDto> roleList = roleRepository.findAllByRoleCode().stream()
					.map(role -> new RoleDto(role.getName(), role.getDescription(), role.getId()))
					.collect(Collectors.toList());

			if (roleList != null && roleList.size() > 0)
				recieveDieselFormLoadDto.setUserRoleIst(roleList);

			List<FuelTankMasterDto> fuelTankList = fuelTankMasterRepository.findAllByStatusAndDepotId(true,depoMasterId).stream()
					.map(tank -> new FuelTankMasterDto(tank.getId(), tank.getTankName(),tank.getCapacity().getCapacity())).collect(Collectors.toList());

			if (fuelTankList != null && fuelTankList.size() > 0)
				recieveDieselFormLoadDto.setFuelTankList(fuelTankList);
			
			List<TransportDto> transportList = transportUnitRepository.allTransportMasterByDepot(depoMasterId).stream()
					.map(tpu -> new TransportDto(tpu.getTransportUnitMaster().getId(), tpu.getTransportUnitMaster().getTransportName())).collect(Collectors.toList());
			
			if (transportList != null && transportList.size() > 0)
				recieveDieselFormLoadDto.setTransportList(transportList);
			
			List<DispensingUnitMasterDto> dispensingList = dispensingUnitRepository.findAllByStatusAndDepotId(true,depoMasterId).stream()
					.map(dispense -> new DispensingUnitMasterDto(dispense.getId(), dispense.getDisUnitName())).collect(Collectors.toList());

			if (dispensingList != null && dispensingList.size() > 0)
				recieveDieselFormLoadDto.setDispensingList(dispensingList);
			
			List<BookReadingClosingDto> closingList = bookreadingClosingRepo.findAllByStatus(true).stream()
					.map(book -> new BookReadingClosingDto(book.getId(), book.getName())).collect(Collectors.toList());

			if (closingList != null && closingList.size() > 0)
				recieveDieselFormLoadDto.setClosingList(closingList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return recieveDieselFormLoadDto;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateReceiveDieselMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateReceiveDieselMasterStatusFlag service");
		try {
			int i = repository.updateReceiveDieselMasterStatusFlag(flag,id);
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
	public ResponseEntity<ResponseStatus> updatedeleteReceiveDieselMasterIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updatedeleteReceiveDieselMasterIsDeletedFlag service");
		try {
			int i = repository.updatedeleteReceiveDieselMasterIsDeletedFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Receive Diesel has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}

	@Override
	public TankCurrentValAndCapacityDto fetchCurrValAndCapacity(Integer tankId) {
		log.info("Entering into fetchCurrValAndCapacity service");
		FuelTankMaster fuelTankMaster = null;
		TankCurrentValAndCapacityDto dtoObj = new TankCurrentValAndCapacityDto();
		try {
			fuelTankMaster = repository.fetchCurrValAndCapacity(tankId);
			if(fuelTankMaster != null) {
				dtoObj.setCurrentValue(fuelTankMaster.getCurrentValue());
				dtoObj.setTankCapacity(fuelTankMaster.getCapacity().getCapacity());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dtoObj;
	}
	
	@Override
	public List<ReadingMasterDto> readingByFuelTank(Integer tankId) {
		List<ReadingMasterDto> readings = null;
		try {
			FuelTankMaster tank = fuelTankMasterRepository.findById(tankId).get();
			readings = tank.getDip().getReadings().stream()
					.map(reading -> new ReadingMasterDto(reading.getId(), reading.getReading(), reading.getVolume())).collect(Collectors.toList());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return readings;
	}

	@Override
	public RecieveDieselFormLoadDto fetchFuelTankListAndDu(Integer transportId) {
		log.info("Entering into etmMasterFormOnLoad service");
		RecieveDieselFormLoadDto recieveDieselFormLoadDto = new RecieveDieselFormLoadDto();
		try {
			

			/*List<FuelTankMasterDto> fuelTankList = fuelTankMasterRepository.findAllByStatusAndDepotId(true,depoMasterId).stream()
					.map(tank -> new FuelTankMasterDto(tank.getId(), tank.getTankName())).collect(Collectors.toList());

			if (fuelTankList != null && fuelTankList.size() > 0)
				recieveDieselFormLoadDto.setFuelTankList(fuelTankList);
			
			List<TransportDto> transportList = transportUnitRepository.allTransportMasterByDepot(depoMasterId).stream()
					.map(tpu -> new TransportDto(tpu.getTransportUnitMaster().getId(), tpu.getTransportUnitMaster().getTransportName())).collect(Collectors.toList());
			
			if (transportList != null && transportList.size() > 0)
				recieveDieselFormLoadDto.setTransportList(transportList);*/

		} catch (Exception e) {
			e.printStackTrace();
		}

		return recieveDieselFormLoadDto;
	}

	@Override
	public ResponseEntity<ResponseStatus> validateOnchangeOfReadingMaster(Integer tankId, Integer readingId) {
		float currentValue = 0;
		float volume = 0;
		float difference = 0;
		try {
			FuelTankMaster tankMaster = fuelTankMasterRepository.findById(tankId).get();
			ReadingMaster readingMaster = readingMasterRepository.findById(readingId).get();
			volume = readingMaster.getVolume();
			currentValue = tankMaster.getCurrentValue().floatValue();
			if (currentValue != 0 && volume != 0) {
				difference = currentValue - volume;
				float percentage = difference / currentValue * 100;
				if (percentage != 0 && percentage > 4) {
					return new ResponseEntity<>(
							new ResponseStatus("Please select appropriate dip reading.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} 
			    if(difference < 0 ){
					float diff = Math.abs(difference);
					float percent = diff / currentValue * 100;
					if (percent > 4) {
						return new ResponseEntity<>(
								new ResponseStatus("Please select appropriate dip reading.", HttpStatus.FORBIDDEN),
								HttpStatus.OK);
					} 
				}
				
				else {
					return new ResponseEntity<>(new ResponseStatus("", HttpStatus.OK), HttpStatus.OK);
				}
			}else {
				return new ResponseEntity<>(new ResponseStatus("", HttpStatus.OK), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
		return null;
	}

}
