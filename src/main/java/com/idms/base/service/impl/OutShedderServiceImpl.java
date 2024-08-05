package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.OutShedderDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.OutShedder;
import com.idms.base.dao.entity.Roaster;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.OutShedderRepository;
import com.idms.base.dao.repository.RoasterRepository;
import com.idms.base.service.OutShedderService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OutShedderServiceImpl implements OutShedderService {

	@Autowired
	OutShedderRepository outShedderRepository;
	
	@Autowired
	BusMasterRepository busMasterRepository;
	
	@Autowired
	DriverMasterRepository driverMasterRepository;
	
	@Autowired
	ConductorMasterRepository conductorMasterRepository;
	
	@Autowired
	DepotMasterRepository depotMasterRepository;
	
	@Autowired
	RoasterRepository roasterRepository;
	
	@Autowired
	DepotMasterRepository depotMasterRepo;
	
	
	@Override
	public List<OutShedderDto> getOutShedder(String depotCode) {
		List<OutShedderDto> list = new ArrayList<>();
		OutShedderDto dto = null;
		BusMasterDto tempBusMaster = null; 
		DriverMasterDto driverMaster = null;
		ConductorMasterDto conductorMaster = null;
		DepotMasterDto depotDto = null;
		try{
			Integer depoMasterId = depotMasterRepo.getIdByDepoName(depotCode);
			List<OutShedder> outList = outShedderRepository.findAllOutByDepot(depoMasterId);
			for(OutShedder outShedderObj : outList){
				dto = new OutShedderDto();
					tempBusMaster = new BusMasterDto();
					tempBusMaster.setId(outShedderObj.getBus().getId());
					tempBusMaster.setBusRegNumber(outShedderObj.getBus().getBusRegNumber());
					dto.setBus(tempBusMaster);
					if(outShedderObj.getDailyRoaster() != null){
						driverMaster = new DriverMasterDto();
						driverMaster.setId(outShedderObj.getDailyRoaster().getDriver().getId());
						driverMaster.setDriverName(outShedderObj.getDailyRoaster().getDriver().getDriverName());
						driverMaster.setDriverCode(outShedderObj.getDailyRoaster().getDriver().getDriverCode());
						dto.setDriver(driverMaster);
						conductorMaster = new ConductorMasterDto();
						conductorMaster.setId(outShedderObj.getDailyRoaster().getConductor().getId());
						conductorMaster.setConductorCode(outShedderObj.getDailyRoaster().getConductor().getConductorCode());
						conductorMaster.setConductorName(outShedderObj.getDailyRoaster().getConductor().getConductorName());
						dto.setConductor(conductorMaster);
						dto.setRouteName(outShedderObj.getDailyRoaster().getRoute().getRouteName());
						dto.setFromState(outShedderObj.getDailyRoaster().getRoute().getFromState().getStateName());
						dto.setToState(outShedderObj.getDailyRoaster().getRoute().getToState().getStateName());
						dto.setDailyId(outShedderObj.getDailyRoaster().getId().toString());
						dto.setRotaId(outShedderObj.getDailyRoaster().getRota().getId().toString());
						dto.setRotaDate(outShedderObj.getDailyRoaster().getRota().getRotaDate().toString());
						if(outShedderObj.getInTime() != null)
						dto.setInTime(outShedderObj.getInTime().toString());
						if(outShedderObj.getOutTime() != null)
						dto.setOutTime(outShedderObj.getOutTime().toString());
						dto.setId(outShedderObj.getId());
						dto.setTripStartTime(outShedderObj.getDailyRoaster().getTrip().getTripStartTime().toString());
						dto.setTripEndTime(outShedderObj.getDailyRoaster().getTrip().getTripEndTime().toString());
						depotDto = new DepotMasterDto();
						depotDto.setId(outShedderObj.getDailyRoaster().getRota().getDepot().getId());
						depotDto.setDepotName(outShedderObj.getDailyRoaster().getRota().getDepot().getDepotName());
						depotDto.setDepotCode(outShedderObj.getDailyRoaster().getRota().getDepot().getDepotName());
						dto.setDepot(depotDto);
					}
					list.add(dto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveOutShedder(OutShedderDto outShedder) {
		log.info("Entering into saveOtherPurpose service");
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		LocalTime outTime = null;
		LocalTime inTime = null;
		OutShedder tempOutShedder = new OutShedder();
		BusMaster busMaster = new BusMaster();
		DailyRoaster dailyRoaster = new DailyRoaster();
		Date date = null;
		DepotMaster depotDto = null;
		try {
			if(outShedder.getId() != null){
				int i = outShedderRepository.updateIntimeInOutShedder(outShedder.getId(),LocalTime.parse(outShedder.getInTime()));
				if(i > 0){
					return new ResponseEntity<>(new ResponseStatus("Record updated successfully.", HttpStatus.OK),
							HttpStatus.OK);
				}
			}
			busMaster.setId(outShedder.getBus().getId());
			tempOutShedder.setBus(busMaster);
			if(outShedder.getRotaId() != null && !outShedder.getDailyId().equals("")){
				Roaster	rotaObj	= roasterRepository.findById(Integer.parseInt(outShedder.getRotaId())).get();
			tempOutShedder.setRota(rotaObj);
			}
			if(outShedder.getDailyId() != null && !outShedder.getDailyId().equals(""))
			dailyRoaster.setId(Integer.parseInt(outShedder.getDailyId()));
			tempOutShedder.setDailyRoaster(dailyRoaster);
			if(outShedder.getRotaDate() != null && !outShedder.getRotaDate().equals("")){
			date = sm.parse(outShedder.getRotaDate());
			tempOutShedder.setRotaDate(date);
			}
			if (outShedder.getOutTime() != null) {
				outTime = LocalTime.parse(outShedder.getOutTime());
				tempOutShedder.setOutTime(outTime);
			}
			if (outShedder.getInTime() != null) {
				inTime = LocalTime.parse(outShedder.getInTime());
				tempOutShedder.setInTime(inTime);
			}
			if(outShedder.getDepot() != null && outShedder.getDepot().getId() != null){
			depotDto = new DepotMaster();
			depotDto.setId(outShedder.getDepot().getId());
			tempOutShedder.setDepot(depotDto);
			}
			outShedderRepository.save(tempOutShedder);

			return new ResponseEntity<>(new ResponseStatus("Record persisted successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<OutShedderDto> getOutShedderByBusId(Date date, String busNo) {
		List<Object[]> getOutShedderList=null;
		BusMasterDto tempBusMaster = null; 
		DriverMasterDto driverMaster = null;
		ConductorMasterDto conductorMaster = null;
		OutShedderDto dto = new OutShedderDto();
		DepotMasterDto depotDto = null;
		List<OutShedderDto> list = new ArrayList<>();
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		String rotaDate = null; 
		try{
			BusMaster busMaster = busMasterRepository.findByBusRegNumber(busNo);
			List<OutShedder> outList = outShedderRepository.findAllOutByDepotDailyAndRotaId(busMaster.getId(),date);
			if(outList != null && outList.size() > 0){
				tempBusMaster = new BusMasterDto();
				tempBusMaster.setId(outList.get(0).getBus().getId());
				tempBusMaster.setBusRegNumber(outList.get(0).getBus().getBusRegNumber());
				dto.setBus(tempBusMaster);
				if(outList.get(0).getDailyRoaster() != null){
					driverMaster = new DriverMasterDto();
					}
							
					driverMaster.setId(outList.get(0).getDailyRoaster().getDriver().getId());
					driverMaster.setDriverName(outList.get(0).getDailyRoaster().getDriver().getDriverName());
					driverMaster.setDriverCode(outList.get(0).getDailyRoaster().getDriver().getDriverCode());
					dto.setDriver(driverMaster);
					conductorMaster = new ConductorMasterDto();
					conductorMaster.setId(outList.get(0).getDailyRoaster().getConductor().getId());
					conductorMaster.setConductorCode(outList.get(0).getDailyRoaster().getConductor().getConductorCode());
					conductorMaster.setConductorName(outList.get(0).getDailyRoaster().getConductor().getConductorName());
					dto.setConductor(conductorMaster);
					dto.setRouteName(outList.get(0).getDailyRoaster().getRoute().getRouteName());
					dto.setFromState(outList.get(0).getDailyRoaster().getRoute().getFromState().getStateName());
					dto.setToState(outList.get(0).getDailyRoaster().getRoute().getToState().getStateName());
					dto.setDailyId(outList.get(0).getDailyRoaster().getId().toString());
					dto.setRotaId(outList.get(0).getDailyRoaster().getRota().getId().toString());
					if(outList.get(0).getDailyRoaster().getRota().getRotaDate() != null){
				         rotaDate = sm.format(outList.get(0).getDailyRoaster().getRota().getRotaDate());
					}
					dto.setRotaDate(rotaDate);
					dto.setOutFlag("false");
					if(outList.get(0).getInTime() != null)
					dto.setInTime(outList.get(0).getInTime().toString());
					if(outList.get(0).getOutTime() != null)
					dto.setOutTime(outList.get(0).getOutTime().toString());
					dto.setId(outList.get(0).getId());
					depotDto = new DepotMasterDto();
					depotDto.setId(outList.get(0).getDailyRoaster().getRota().getDepot().getId());
					depotDto.setDepotName(outList.get(0).getDailyRoaster().getRota().getDepot().getDepotName());
					depotDto.setDepotCode(outList.get(0).getDailyRoaster().getRota().getDepot().getDepotName());
					dto.setDepot(depotDto);
					dto.setTripStartTime(outList.get(0).getDailyRoaster().getTrip().getTripStartTime().toString());
					dto.setTripEndTime(outList.get(0).getDailyRoaster().getTrip().getTripEndTime().toString());
					if(outList.get(0).getInTime() != null && outList.get(0).getOutTime() != null)
						dto.setOutFlag(null);
					list.add(dto);
			}
			else{
		getOutShedderList = outShedderRepository.getOutShedderByBusId(date, busMaster.getId());
		if(getOutShedderList.size() > 0){
			tempBusMaster = new BusMasterDto();
			tempBusMaster.setId(busMaster.getId());
			tempBusMaster.setBusRegNumber(busMaster.getBusRegNumber());
			dto.setBus(tempBusMaster);
		}
		if(getOutShedderList.get(0)[1] != null){
			driverMaster = new DriverMasterDto();
			DriverMaster dMaster = driverMasterRepository.findById(Integer.parseInt(getOutShedderList.get(0)[1].toString())).get();
			driverMaster.setId(dMaster.getId());
			driverMaster.setDriverName(driverMaster.getDriverName());
			driverMaster.setDriverCode(driverMaster.getDriverCode());
			dto.setDriver(driverMaster);
		}
		if(getOutShedderList.get(0)[2] != null){
			conductorMaster = new ConductorMasterDto();
			ConductorMaster cMaster = conductorMasterRepository.findById(Integer.parseInt(getOutShedderList.get(0)[2].toString())).get();
			conductorMaster.setId(cMaster.getId());
			conductorMaster.setConductorCode(cMaster.getConductorCode());
			conductorMaster.setConductorName(cMaster.getConductorName());
			dto.setConductor(conductorMaster);
		}if(getOutShedderList.get(0)[3] != null){
			dto.setRouteName(getOutShedderList.get(0)[3].toString());
		}if(getOutShedderList.get(0)[4] != null){
			dto.setFromState(getOutShedderList.get(0)[4].toString());
		}if(getOutShedderList.get(0)[5] != null){
			dto.setToState(getOutShedderList.get(0)[5].toString());
		}if(getOutShedderList.get(0)[6] != null){
			depotDto = new DepotMasterDto();
			DepotMaster depotMaster = depotMasterRepository.findById(Integer.parseInt(getOutShedderList.get(0)[6].toString())).get();
			depotDto.setId(depotMaster.getId());
			depotDto.setDepotName(depotMaster.getDepotName());
			depotDto.setDepotCode(depotMaster.getDepotCode());
			dto.setDepot(depotDto);
		}if(getOutShedderList.get(0)[7] != null){
			dto.setDailyId(getOutShedderList.get(0)[7].toString());
		}if(getOutShedderList.get(0)[8] != null){
			dto.setRotaId(getOutShedderList.get(0)[8].toString());
		}if(getOutShedderList.get(0)[9] != null){
			dto.setRotaDate(getOutShedderList.get(0)[9].toString());
		}if(getOutShedderList.get(0)[10] != null){
			dto.setTripStartTime(getOutShedderList.get(0)[10].toString());
		}if(getOutShedderList.get(0)[11] != null){
			dto.setTripEndTime(getOutShedderList.get(0)[11].toString());
		}
		dto.setOutFlag("true");
		list.add(dto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	

}
