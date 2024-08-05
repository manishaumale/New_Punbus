package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.DipChartReadingsDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.FuelTankMasterDto;
import com.idms.base.api.v1.model.dto.InspectionTankDto;
import com.idms.base.api.v1.model.dto.ReadingMasterDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DipChartReadings;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.ReadingMaster;
import com.idms.base.dao.entity.TankInspection;
import com.idms.base.dao.entity.User;
import com.idms.base.dao.repository.DUReadingHistoryRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DipChartReadingsRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.ReadingMasterRepository;
import com.idms.base.dao.repository.TankInspectionRepository;
import com.idms.base.dao.repository.UserRepository;
import com.idms.base.service.InspectTankService;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class InspectTankServiceImpl implements InspectTankService {
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	TankInspectionRepository tankInsRepo;
	
	@Autowired
	FuelTankMasterRepository tankRepo;
	
	@Autowired
	DUReadingHistoryRepository duHisRepo;
	
	@Autowired
	ReadingMasterRepository readingMasterRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DipChartReadingsRepository dipChartReadingRepo;
	
	@Autowired
	DispensingUnitRepository dispensingUnitRepository;
	
	@Autowired
	FuelTankMasterRepository fuelTankMasterRepository;
	
	@Autowired
	DipChartReadingsRepository dipChartReadingsRepository;
	

	@Override
	public List<InspectionTankDto> getAllInspections(String dpCode, String userName) {
		List<TankInspection> list = new ArrayList<TankInspection>();
		FuelTankMasterDto tankMasterOneDto = null;
		FuelTankMasterDto tankMasterSecDto = null;
		DispensingUnitMasterDto dipReadingMasterOneDto = null;
		DispensingUnitMasterDto dipReadingMasterSecDto = null;
		ReadingMasterDto readingMasterOneDto = null;
		ReadingMasterDto readingMasterSecDto = null;
		FuelTankMasterDto tempTankMasterOneDto = null;
		FuelTankMasterDto tempTankMasterSecDto = null;
		ReadingMasterDto tempReadingMasterOneDto = null;
		ReadingMasterDto tempReadingMasterSecDto = null;
		List<InspectionTankDto> output = new ArrayList<>();

		try {
			DepotMaster depot = depotRepo.findByDepotCode(dpCode);
			User user = userRepository.findByUserName(userName);
			list = tankInsRepo.findAllByDepot(depot.getId(), user.getId());
			for (TankInspection input : list) {
				InspectionTankDto dipReading = new InspectionTankDto();
				FuelTankMaster tankMasterOne = fuelTankMasterRepository.findById(input.getFuelTankMasterOne().getId())
						.get();
				tankMasterOneDto = new FuelTankMasterDto();
				tankMasterOneDto.setId(tankMasterOne.getId());
				tankMasterOneDto.setTankName(tankMasterOne.getTankName());
				dipReading.setFuelTankMasterOne(tankMasterOneDto);
				FuelTankMaster tankMasterSec = fuelTankMasterRepository.findById(input.getFuelTankMasterSec().getId())
						.get();
				tankMasterSecDto = new FuelTankMasterDto();
				tankMasterSecDto.setId(tankMasterSec.getId());
				tankMasterSecDto.setTankName(tankMasterSec.getTankName());
				dipReading.setFuelTankMasterSec(tankMasterSecDto);
				dipReading.setFuelVolumeTankOne(input.getFuelVolumeTankOne());
				dipReading.setFuelVolumeTankSecond(input.getFuelVolumeTankSecond());
				ReadingMaster readingMasterOne = readingMasterRepository.findById(input.getReadingMasterOne().getId())
						.get();
				readingMasterOneDto = new ReadingMasterDto();
				readingMasterOneDto.setId(readingMasterOne.getId());
				readingMasterOneDto.setReading(readingMasterOne.getReading());
				dipReading.setReadingMasterOne(readingMasterOneDto);
				ReadingMaster readingMasterSec = readingMasterRepository.findById(input.getReadingMasterSec().getId())
						.get();
				readingMasterSecDto = new ReadingMasterDto();
				readingMasterSecDto.setId(readingMasterSec.getId());
				readingMasterSecDto.setReading(readingMasterSec.getReading());
				dipReading.setReadingMasterSec(readingMasterSecDto);
				dipReading.setVariation(input.getVariation());
				dipReading.setVariationFlag(input.getVariationFlag());
				dipReading.setRemarks(input.getRemarks());
				dipReading.setNextInspectionDate(input.getNextInspectionDate());
				DipChartReadingsDto dtoChartObj = new DipChartReadingsDto();
				dtoChartObj.setId(input.getBookReading().getDip_id());
				FuelTankMaster tankOne = tankRepo.findById(input.getBookReading().getFuelTankMasterOne().getId())
						.get();
				tempTankMasterOneDto = new FuelTankMasterDto();
				tankMasterOneDto.setId(tankOne.getId());
				tankMasterOneDto.setTankName(tankOne.getTankName());
				dtoChartObj.setFuelTankMasterOne(tankMasterOneDto);
				FuelTankMaster tankSec = tankRepo.findById(input.getBookReading().getFuelTankMasterSec().getId())
						.get();
				tempTankMasterSecDto = new FuelTankMasterDto();
				tempTankMasterSecDto.setId(tankSec.getId());
				tempTankMasterSecDto.setTankName(tankSec.getTankName());
				dtoChartObj.setFuelTankMasterSec(tankMasterSecDto);
				dtoChartObj.setFuelVolumeTankOne(input.getBookReading().getFuelVolumeTankOne());
				dtoChartObj.setFuelVolumeTankSecond(input.getBookReading().getFuelVolumeTankSecond());
				
				ReadingMaster readingOne = readingMasterRepository.findById(input.getBookReading().getReadingMasterOne().getId())
						.get();
				readingMasterOneDto = new ReadingMasterDto();
				readingMasterOneDto.setId(readingOne.getId());
				readingMasterOneDto.setReading(readingOne.getReading());
				dtoChartObj.setReadingMasterOne(readingMasterOneDto);
				ReadingMaster readingSec = readingMasterRepository.findById(input.getBookReading().getReadingMasterSec().getId())
						.get();
				readingMasterSecDto = new ReadingMasterDto();
				readingMasterSecDto.setId(readingSec.getId());
				readingMasterSecDto.setReading(readingSec.getReading());
				dtoChartObj.setReadingMasterSec(readingMasterSecDto);
				dtoChartObj.setCloseTotalOpeningFuelVolume(input.getBookReading().getCloseTotalOpeningFuelVolume());
				dipReading.setBookReading(dtoChartObj);
				dipReading.setCreatedOn(input.getCreatedOn());
				dipReading.setCloseTotalOpeningFuelVolume(input.getTotalOpeningFuelVolume());
				output.add(dipReading);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}

	/*@Override
	public List<FuelTankMaster> getInspectionFormLoad(String dpCode) {
		List<FuelTankMaster> list = new ArrayList<>();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(dpCode);
			list = tankRepo.findAllByDepotId(depot.getId());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}*/

	/*@Override
	public ResponseEntity<ResponseStatus> saveTankInspection(TankInspection tankInspection) {
		float currentValue = 0;
		float volume = 0;
		float difference = 0;
		
		try {
			
			FuelTankMaster tank = null;
			
			if(tankInspection.getTank()==null) {
				return new ResponseEntity<>(new ResponseStatus("Fuel Tank is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(tankInspection.getTank().getId()==null) {
				return new ResponseEntity<>(new ResponseStatus("Fuel Tank is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			} else {
				tank = tankRepo.findById(tankInspection.getTank().getId()).get();
				if(tank==null) {
					return new ResponseEntity<>(new ResponseStatus("Invalid fuel tank.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
			}
			
			if(tankInspection.getPrevVolume()==null || tankInspection.getPrevVolume()<0) {
				return new ResponseEntity<>(new ResponseStatus("Fuel Tank auto volume is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(tankInspection.getInspectedReading()==null || tankInspection.getInspectedReading().getId()==null) {
				return new ResponseEntity<>(new ResponseStatus("Reading is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(tankInspection.getInspectedVolume()==null || tankInspection.getInspectedVolume()<0) {
				return new ResponseEntity<>(new ResponseStatus("Inspected volume is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(tankInspection.getInspectionDate() == null) {
				return new ResponseEntity<>(new ResponseStatus("Inspected Date is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(tankInspection.getInspectionTime() == null) {
				return new ResponseEntity<>(new ResponseStatus("Inspected Time is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			tankInspection.setDepot(tank.getDepot());
			tankInsRepo.save(tankInspection);
			FuelTankMaster tankMaster = tankRepo.findById(tankInspection.getTank().getId()).get();
			ReadingMaster readingMaster = readingMasterRepository.findById(tankInspection.getInspectedReading().getId()).get();
			if(readingMaster != null && readingMaster.getVolume() != null)
			volume = readingMaster.getVolume();
			if(tankMaster != null && tankMaster.getCurrentValue() != null)
			currentValue = tankMaster.getCurrentValue().floatValue();
			if (currentValue != 0 && volume != 0) {
				difference = currentValue - volume;
				float percentage = difference / currentValue * 100;
				if (percentage < 4) {
					tank.setCurrentValue(tankInspection.getInspectedVolume());
					tankRepo.save(tank);
				} 
			    if(difference < 0 ){
					float diff = Math.abs(difference);
					float percent = diff / currentValue * 100;
					if (percent < 4) {
						tank.setCurrentValue(tankInspection.getInspectedVolume());
						tankRepo.save(tank);
					} 
				}
			}  
			
			tank.setCurrentValue(tankInspection.getInspectedVolume());
			tankRepo.save(tank);
			
			// The code is commented as there is no need of keeping history in Du*\
			DUReadingHistory duHistory = new DUReadingHistory();
			duHistory.setCurrentDiesel(tankInspection.getInspectedVolume().floatValue());
			duHistory.setTankInspection(tankInspection);
			duHistory.setFuelTank(tank);
			duHisRepo.save(duHistory);
			
			return new ResponseEntity<>(
					new ResponseStatus("Tank Inspection Record has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}*/

	@Override
	public ResponseEntity<Object> getInspectionFormLoad(String dpCode) {
		InspectionTankDto dtoObj = new InspectionTankDto();
		FuelTankMasterDto tankMasterOneDto = null;
		FuelTankMasterDto tankMasterSecDto = null;
		DispensingUnitMasterDto dipReadingMasterOneDto = null;
		DispensingUnitMasterDto dipReadingMasterSecDto = null;
		ReadingMasterDto readingMasterOneDto = null;
		ReadingMasterDto readingMasterSecDto = null;
		try {
			DipChartReadings dipChartObj = dipChartReadingRepo.findMaxRecord();
			if(dipChartObj.getIncomplete() == false){
			FuelTankMaster tankMasterOne = tankRepo.findById(dipChartObj.getFuelTankMasterOne().getId())
					.get();
			tankMasterOneDto = new FuelTankMasterDto();
			tankMasterOneDto.setId(tankMasterOne.getId());
			tankMasterOneDto.setTankName(tankMasterOne.getTankName());
			dtoObj.setFuelTankMasterOne(tankMasterOneDto);
			FuelTankMaster tankMasterSec = tankRepo.findById(dipChartObj.getFuelTankMasterSec().getId())
					.get();
			tankMasterSecDto = new FuelTankMasterDto();
			tankMasterSecDto.setId(tankMasterSec.getId());
			tankMasterSecDto.setTankName(tankMasterSec.getTankName());
			dtoObj.setFuelTankMasterSec(tankMasterSecDto);
			DispensingUnitMaster dipReadingMasterOne = dispensingUnitRepository
					.findById(dipChartObj.getDipReadingMasterOne().getId()).get();
			dipReadingMasterOneDto = new DispensingUnitMasterDto();
			dipReadingMasterOneDto.setId(dipReadingMasterOne.getId());
			dipReadingMasterOneDto.setDisUnitName(dipReadingMasterOne.getDisUnitName());
			dtoObj.setDispensingMasterOne(dipReadingMasterOneDto);
			DispensingUnitMaster dipReadingMasterSec = dispensingUnitRepository
					.findById(dipChartObj.getDipReadingMasterSec().getId()).get();
			dipReadingMasterSecDto = new DispensingUnitMasterDto();
			dipReadingMasterSecDto.setId(dipReadingMasterSec.getId());
			dipReadingMasterSecDto.setDisUnitName(dipReadingMasterSec.getDisUnitName());
			dtoObj.setDispensingMasterSec(dipReadingMasterSecDto);
			dtoObj.setFuelVolumeTankOne(dipChartObj.getFuelVolumeTankOne());
			dtoObj.setFuelVolumeTankSecond(dipChartObj.getFuelVolumeTankSecond());
			dtoObj.setCloseTotalOpeningFuelVolume(dipChartObj.getCloseTotalOpeningFuelVolume());
			dtoObj.setDispensingUnitReadingOne(dipChartObj.getDipReadingOne());
			dtoObj.setDispensingUnitReadingSec(dipChartObj.getDipReadingSec());
			ReadingMaster readingMasterOne = readingMasterRepository.findById(dipChartObj.getReadingMasterOne().getId())
					.get();
			readingMasterOneDto = new ReadingMasterDto();
			readingMasterOneDto.setId(readingMasterOne.getId());
			readingMasterOneDto.setReading(readingMasterOne.getReading());
			dtoObj.setReadingMasterOne(readingMasterOneDto);
			ReadingMaster readingMasterSec = readingMasterRepository.findById(dipChartObj.getReadingMasterSec().getId())
					.get();
			readingMasterSecDto = new ReadingMasterDto();
			readingMasterSecDto.setId(readingMasterSec.getId());
			readingMasterSecDto.setReading(readingMasterSec.getReading());
			dtoObj.setReadingMasterSec(readingMasterSecDto);
			DipChartReadingsDto dtoChartObj = new DipChartReadingsDto();
			dtoChartObj.setId(dipChartObj.getDip_id());
			dtoObj.setBookReading(dtoChartObj);
			dtoObj.setDispenseFuelEvening(dipChartObj.getDispenseFuelEvening());
			return new ResponseEntity<>(dtoObj, HttpStatus.OK);
			}
			else{
				return new ResponseEntity<>(new ResponseStatus("", HttpStatus.FORBIDDEN),HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ResponseEntity<ResponseStatus> saveTankInspection(TankInspection tankInspection) {
		try {
			DepotMaster depot = depotRepo.findByDepotCode(tankInspection.getDepotCode());
			tankInspection.setDepot(depot);
			DipChartReadings dipObj = dipChartReadingsRepository.findById(tankInspection.getBookReading().getDip_id()).get();
			tankInspection.setBookReading(dipObj);
			tankInsRepo.save(tankInspection);
			
			return new ResponseEntity<>(
					new ResponseStatus("Tank Inspection Record has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}
}
