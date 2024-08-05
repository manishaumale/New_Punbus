package com.idms.base.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.ManualSpecialRotaFormSaveDto;
import com.idms.base.api.v1.model.dto.ManualSpecialRotaFormSaveParentDto;
import com.idms.base.api.v1.model.dto.ManualSpecialRotaPageLoadDto;
import com.idms.base.api.v1.model.dto.RotaRejectChildDto;
import com.idms.base.api.v1.model.dto.RotaRejectParentDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusRotaHistory;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.ConductorRotaHistory;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DailyRoasterAuto;
import com.idms.base.dao.entity.DailyRoasterHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.DriverRotaHistory;
import com.idms.base.dao.entity.MasterStatus;
import com.idms.base.dao.entity.RestAlloactionDriverConductor;
import com.idms.base.dao.entity.Roaster;
import com.idms.base.dao.entity.RoasterAuto;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.entity.TripRotationEntity;
import com.idms.base.dao.entity.WorkFlowHistory;
import com.idms.base.dao.entity.notificationEntity;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusRotaHistoryRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.ConductorRotaHistoryRepository;
import com.idms.base.dao.repository.ConductorUnavailableRepository;
import com.idms.base.dao.repository.DailyRoasterAutoRepository;
import com.idms.base.dao.repository.DailyRoasterHistoryRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.DriverRotaHistoryRepository;
import com.idms.base.dao.repository.DriverUnavailableRepository;
import com.idms.base.dao.repository.FuelNotificationRepository;
import com.idms.base.dao.repository.MasterStatusRepository;
import com.idms.base.dao.repository.RestAlloactionDriverConductorRepository;
import com.idms.base.dao.repository.RoasterAutoRepository;
import com.idms.base.dao.repository.RoasterRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.RouteTypeRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.dao.repository.TripRotationReposiotry;
import com.idms.base.dao.repository.WorkFlowHistoryRepository;
import com.idms.base.service.ManualSpecialService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;
import com.idms.base.util.AlertUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ManualSpecialServiceImpl implements ManualSpecialService{

	@Autowired
	RouteMasterRepository routeMasterRepo;
	
	@Autowired
	DepotMasterRepository depotMasterRepo;
	
	@Autowired
	CityMasterRepository cityMasterRepository;
	
	@Autowired
	TripMasterRepository tripMasterRepository;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	DriverMasterRepository driverRepo;
	
	@Autowired
	ConductorMasterRepository conductorRepo;
	
	@Autowired
	BusMasterRepository busRepo;
	
	@Autowired
	TripRotationReposiotry tripRotationReposiotry;
	
	@Autowired
	RoasterAutoRepository roasterRepo;
	
	@Autowired
	RoasterRepository roasterOldRepo;
	
	@Autowired
	DailyRoasterAutoRepository dailyRoasterAutoRepository;
	
	@Autowired
	DailyRoasterRepository dailyRoasterRepository;
	
	@Autowired
	RouteTypeRepository routeTypeRepository;
	
	@Autowired
	DailyRoasterHistoryRepository dailyRoasterHistoryRepository;
	
	@Autowired
	MasterStatusRepository masterStatusRepository;
	
	@Autowired
	BusRotaHistoryRepository busRotaHistoryRepository;
	
	@Autowired
	DriverRotaHistoryRepository driverRotaHistoryRepository;
	
	@Autowired
	ConductorRotaHistoryRepository conductorRotaHistoryRepository;
	
	@Autowired
	WorkFlowHistoryRepository workFlowHistoryRepository;
	
	
	@Autowired
	RestAlloactionDriverConductorRepository restAllocationRepository;
	
	@Autowired
	SMSTriggerServiceImpl smsTrigger;
	
	@Autowired
	DriverUnavailableRepository driverUnavailableRepository;
	
	@Autowired
	ConductorUnavailableRepository conductorUnavailableRepository;
	
	@Autowired
	AlertUtility alertUtility;
	
	@Autowired
	FuelNotificationRepository fuelNotificationRepo;
	
	String roleIds[]=RestConstants.AlertRoles;
	
	@Override
	public List<RouteMasterDto> getSpecialManualRoutesByDeopAndTransUnit(String depoCode, Integer transPortId, Integer routeType) {
		try {
			List<RouteMasterDto> rmdList = new ArrayList<RouteMasterDto>();
			Integer depoMasterId = depotMasterRepo.getIdByDepoName(depoCode);
				List<Object[]> obj = routeMasterRepo.getManualRoutesByDeopAndTransUnit(depoMasterId,transPortId,routeType);
				if (obj.size() !=0) {
					obj.forEach(action->{
						RouteMasterDto rmd = new RouteMasterDto();
						rmd.setId(Integer.parseInt(action[0].toString()));
						rmd.setRouteId(action[1].toString());
						rmd.setRouteName(action[2].toString());
						rmd.setRouteCode(action[3].toString());
						rmdList.add(rmd);
						
					});
					return  rmdList;
				
			}
		} catch (Exception e) {
			log.info("getSpecialRoutesByDeopAndTransUnitServiceImpl " + e);
		}
		return null;
	}

	@Override
	public ManualSpecialRotaPageLoadDto getAllTripMasterByRouteId(String depotCode, Integer tuId, Integer[] routeIds) {
		List<TripMaster> tripMasterList = new ArrayList<TripMaster>();
		List<TripMaster> tmd = new ArrayList<TripMaster>();
		List<DriverMaster> dmd = new ArrayList<DriverMaster>();
		List<ConductorMaster> cmd = new ArrayList<ConductorMaster>();
		List<BusMaster> bmd = new ArrayList<BusMaster>();
		ManualSpecialRotaPageLoadDto rotaWrapper = new ManualSpecialRotaPageLoadDto();
		try {
			//tripMasterList = tripMasterRepository.findAllByRouteId(routeId);
			
			Date rotaDate = this.getDate(1, new Date());
			DepotMaster depot = depotRepo.findByDepotCode(depotCode);
			
			Roaster roasterTemp = roasterOldRepo.findByDate(depot.getId(), tuId);
			//RoasterAuto roasterAuto = roasterRepo.findByDate(depot.getId(), tuId);
			tripMasterList = tripMasterRepository.findAllTripsBySelectedRoutes(depot.getId(), tuId,routeIds,rotaDate);
			
			if(roasterTemp != null){
				List<DailyRoaster> roasterList = roasterTemp.getDailyRoasterList();
				if(roasterList.size() > 0){
					for(DailyRoaster dailyRotaObj : roasterList){
						if(dailyRotaObj.getTrip() != null){
							if(tripMasterList.contains(dailyRotaObj.getTrip())){
								tripMasterList.remove(dailyRotaObj.getTrip());
							}
						}
					}
				}
			}
			
			for (TripMaster tmle : tripMasterList) {
				TripMaster trmd = new TripMaster();
				trmd.setId(tmle.getId());
				trmd.setTripStartTime(tmle.getTripStartTime());
				trmd.setTripEndTime(tmle.getTripEndTime());
				trmd.setScheduledKms(tmle.getScheduledKms());
				if(tmle.getRouteMaster() != null)
				trmd.setRouteName(tmle.getRouteMaster().getRouteName());
				if(tmle.getRouteMaster() != null)
					trmd.setRouteCode(tmle.getRouteMaster().getRouteCode());
				if(tmle.getRouteMaster() != null && tmle.getRouteMaster().getFromCity() != null)
					trmd.setFromNewCity(tmle.getRouteMaster().getFromCity().getCityName());
				
				if(tmle.getRouteMaster() != null && tmle.getRouteMaster().getToCity() != null)
					trmd.setToNewCity(tmle.getRouteMaster().getToCity().getCityName());
				
				if(tmle.getRouteMaster() != null && tmle.getRouteMaster().getFromState() != null && tmle.getRouteMaster().getFromState().getStateName() != null)
					trmd.setFromState(tmle.getRouteMaster().getFromState().getStateName());
				
				if(tmle.getRouteMaster() != null && tmle.getRouteMaster().getToState() != null && tmle.getRouteMaster().getToState().getStateName() != null)
					trmd.setToState(tmle.getRouteMaster().getToState().getStateName());
				
				//trmd.setRouteMaster(tmle.getRouteMaster());
				//trmd.setFromCity(tmle.getFromCity());
				//trmd.setToCity(tmle.getFromCity());
				tmd.add(trmd);
			}
			rotaWrapper.setTripList(tmd);
			List<Object[]> busList = busRepo.getAvailabilityForRota(depot.getId(), tuId, rotaDate);
			for (Object[] busObj : busList) {
				BusMaster busMaster = new BusMaster();
				DriverMaster primaryDriver = new DriverMaster();
				DriverMaster secondryDriver = new DriverMaster();
				if (busObj[0] != null)
					busMaster.setId(Integer.parseInt((busObj[0]).toString()));
				if (busObj[1] != null)
					busMaster.setBusAvailableTime(LocalTime.parse((busObj[1]).toString()));
				if (busObj[2] != null) {
					primaryDriver.setId(Integer.parseInt((busObj[2]).toString()));
					busMaster.setPrimaryDriver(primaryDriver);
				}
				if (busObj[3] != null) {
					secondryDriver.setId(Integer.parseInt((busObj[3]).toString()));
					busMaster.setSecondaryDriver(secondryDriver);
				}
				if (busObj[5] != null)
					busMaster.setBusRegNumber(busObj[5].toString());
				bmd.add(busMaster);
			}
			rotaWrapper.setBusList(bmd);
			
			List<Object[]> driverList = driverRepo.getAvailablilityDriversForRota(depot.getId(), tuId, rotaDate);
			for (Object[] driverObj : driverList) {
				DriverMaster driver = new DriverMaster();
				if (driverObj[0] != null) {
					driver.setId(Integer.parseInt((driverObj[0]).toString()));
				}
				if (driverObj[1] != null)
					driver.setDriverAvailableTime(LocalTime.parse((driverObj[1]).toString()));
				
				if (driverObj[3] != null)
					driver.setDriverCode(driverObj[3].toString());
				if (driverObj[4] != null)
					driver.setDriverName(driverObj[4].toString());
				dmd.add(driver);
			}
			rotaWrapper.setDriverList(dmd);
			List<Object[]> conductorList = conductorRepo.getAvailablilityConductorsForRota(depot.getId(), tuId, rotaDate);
			for (Object[] conductorObj : conductorList) {
				ConductorMaster conductor = new ConductorMaster();
				if (conductorObj[0] != null)
					conductor.setId(Integer.parseInt((conductorObj[0]).toString()));
				if (conductorObj[1] != null)
					conductor.setConductorAvailableTime(LocalTime.parse((conductorObj[1]).toString()));
				if (conductorObj[3] != null)
					conductor.setConductorCode(conductorObj[3] .toString());
				if (conductorObj[4] != null)
					conductor.setConductorName(conductorObj[4] .toString());
				cmd.add(conductor);
			}
			rotaWrapper.setConductorList(cmd);
			
		} catch (Exception e) {
			log.info(e + "getSpecialRoutesByDeopAndTransUnitServiceImpl ");
		}
		return rotaWrapper;
	}
	
	public Date getDate(Integer i, Date dt) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dt = sdf.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveManualSpecialRota(ManualSpecialRotaFormSaveParentDto parentObj) {
		DailyRoasterAuto dailyObj = null;
		RoasterAuto roaster = new RoasterAuto();
		Roaster roasterObj = new Roaster();
		DailyRoaster dailyOldObj = null;
		List<DailyRoaster> drListOld = new ArrayList<>();
		List<DailyRoasterAuto> drList = new ArrayList<>();
		BusRotaHistory brh = null;
		DriverRotaHistory drh = null;
		ConductorRotaHistory crh = null;
		BusMaster busMaster = null;
		DriverMaster driverMaster = null;
		ConductorMaster conductorMaster = null;
		List<BusRotaHistory> brhList = new ArrayList<>();
		List<DriverRotaHistory> drhList = new ArrayList<>();
		List<ConductorRotaHistory> crhList = new ArrayList<>();
		TripMaster tripObj = null;
		try{
			Date rotaDate = this.getDate(1, new Date());
			Calendar c = Calendar.getInstance();
			TransportUnitMaster tu = new TransportUnitMaster();
			tu.setId(parentObj.getTransportId());
			DepotMaster depot = depotRepo.findByDepotCode(parentObj.getDepotCode());
			Roaster roasterTemp = roasterOldRepo.findByDate(depot.getId(), parentObj.getTransportId());
			RoasterAuto roasterAuto = roasterRepo.findByDate(depot.getId(), parentObj.getTransportId());
			roaster.setDepot(depot);
			roaster.setTransport(tu);
			roaster.setGenerationDate(new Date());
			roaster.setIsNormalRota(true);
			roaster.setIsSpecialRota(false);

			roasterObj.setDepot(depot);
			roasterObj.setTransport(tu);
			roasterObj.setGenerationDate(new Date());
			roasterObj.setIsNormalRota(true);
			roasterObj.setIsSpecialRota(false);
			if(roasterTemp != null && roasterTemp.getId() != null){
				roasterObj.setId(roasterTemp.getId());
			}
			if(roasterAuto != null && roasterAuto.getId() != null){
				roaster.setId(roasterAuto.getId());
			}
			Date dt = new Date();
			c.setTime(dt);
			c.add(Calendar.DATE, 1);
			dt = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			roaster.setRotaDate(this.getDate(1, new Date()));
			roaster.setRoastedCode(depot.getId() + "/" + parentObj.getTransportId() + "/" + sdf.format(dt));

			roasterObj.setRotaDate(this.getDate(1, new Date()));
			roasterObj.setRoastedCode(depot.getId() + "/" + parentObj.getTransportId() + "/" + sdf.format(dt));
			if(parentObj.getRotaDtoList() != null && parentObj.getRotaDtoList().size() > 0){
			for(ManualSpecialRotaFormSaveDto dtoObj : parentObj.getRotaDtoList()){
				brh = new BusRotaHistory();
				drh = new DriverRotaHistory();
				crh = new ConductorRotaHistory();
				tripObj = tripMasterRepository.findById(dtoObj.getTripId()).get();
				List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(dtoObj.getTripId());
				for(TripRotationEntity tempCatARotationObj : rotationsList){
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					busMaster = new BusMaster();
					driverMaster = new DriverMaster();
					conductorMaster = new ConductorMaster();
					if(tempCatARotationObj != null && tempCatARotationObj.getDays() != null && tempCatARotationObj.getDays().getNightDetail() != null){
						if(tempCatARotationObj.getDays().getNightDetail().equals("N + 0")){
							dailyOldObj.setRotationAvailabilityDate(this.getDate(1, new Date()));
							dailyObj.setRotationAvailabilityDate(this.getDate(1, new Date()));
							brh.setReturnDate(this.getDate(1, new Date()));
							crh.setReturnDate(this.getDate(1, new Date()));
							drh.setReturnDate(this.getDate(1, new Date()));
						}else if(tempCatARotationObj.getDays().getNightDetail().equals("N + 1")){
							dailyOldObj.setRotationAvailabilityDate(this.getDate(2, new Date()));
							dailyObj.setRotationAvailabilityDate(this.getDate(2, new Date()));
							brh.setReturnDate(this.getDate(2, new Date()));
							crh.setReturnDate(this.getDate(2, new Date()));
							drh.setReturnDate(this.getDate(2, new Date()));
						}else if(tempCatARotationObj.getDays().getNightDetail().equals("N + 2")){
							dailyOldObj.setRotationAvailabilityDate(this.getDate(3, new Date()));
							dailyObj.setRotationAvailabilityDate(this.getDate(3, new Date()));
							brh.setReturnDate(this.getDate(3, new Date()));
							crh.setReturnDate(this.getDate(3, new Date()));
							drh.setReturnDate(this.getDate(3, new Date()));
						}else if(tempCatARotationObj.getDays().getNightDetail().equals("N + 3")){
							dailyOldObj.setRotationAvailabilityDate(this.getDate(4, new Date()));
							dailyObj.setRotationAvailabilityDate(this.getDate(4, new Date()));
							brh.setReturnDate(this.getDate(4, new Date()));
							crh.setReturnDate(this.getDate(4, new Date()));
							drh.setReturnDate(this.getDate(4, new Date()));
						}
					}	
					
					brh.setReturnTime(tempCatARotationObj.getTripMaster().getTripEndTime());
				    drh.setReturnTime(tempCatARotationObj.getTripMaster().getTripEndTime());
				    crh.setReturnTime(tempCatARotationObj.getTripMaster().getTripEndTime());
				    
				busMaster.setId(dtoObj.getBusId());
				driverMaster.setId(dtoObj.getDriverId());
				conductorMaster.setId(dtoObj.getConductorId());
				
				dailyObj.setBus(busMaster);
				dailyObj.setDriver(driverMaster);
				dailyObj.setConductor(conductorMaster);
				
				dailyObj.setRotaAuto(roaster);
				dailyObj.setTripStatus(false);
				dailyObj.setRoute(tempCatARotationObj.getRouteMaster());
				dailyObj.setIsDeleted(false);
				dailyObj.setRouteType(tempCatARotationObj.getRouteMaster().getRouteTypeMaster());
				dailyObj.setTrip(tempCatARotationObj.getTripMaster());
				dailyObj.setTripRotation(tempCatARotationObj);
				if(tempCatARotationObj.getTripMaster() != null && tempCatARotationObj.getTripMaster().getRouteMaster() != null
						&& tempCatARotationObj.getTripMaster().getRouteMaster().getRouteTypeMaster() != null){
					if(tempCatARotationObj.getTripMaster().getRouteMaster().getRouteTypeMaster().getRouteTypeName().equals("Normal")){
						dailyObj.setRotaTypeFlag("Normal");
					}else if(tempCatARotationObj.getTripMaster().getRouteMaster().getRouteTypeMaster().getRouteTypeName().equals("Special")){
						dailyObj.setRotaTypeFlag("Special");
					}
				}
				drList.add(dailyObj);
				
				dailyOldObj.setBus(busMaster);
				dailyOldObj.setDriver(driverMaster);
				dailyOldObj.setConductor(conductorMaster);
				MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Pending");
				if (masterStatus != null && masterStatus.getId() != null) {
					roasterObj.setMasterStatus(masterStatus);
					roaster.setMasterStatus(masterStatus);
				}
				dailyOldObj.setRota(roasterObj);
				dailyOldObj.setTripStatus(false);
				dailyOldObj.setRoute(tempCatARotationObj.getRouteMaster());
				dailyOldObj.setIsDeleted(false);
				dailyOldObj.setRouteType(tempCatARotationObj.getRouteMaster().getRouteTypeMaster());
				dailyOldObj.setTrip(tempCatARotationObj.getTripMaster());
				dailyOldObj.setTripRotation(tempCatARotationObj);
				
				brh.setBus(busMaster);
				brh.setDepot(depot);
				brh.setIsDeleted(false);
				brh.setRoaster(dailyOldObj);
				brh.setTrip(tempCatARotationObj.getTripMaster());
				
				brhList.add(brh);
				
				drh.setDriver(driverMaster);
				drh.setDepot(depot);
				drh.setIsDeleted(false);
				drh.setRoaster(dailyOldObj);
				drh.setTrip(tempCatARotationObj.getTripMaster());
				drhList.add(drh);
				
				crh.setConductor(conductorMaster);
				crh.setDepot(depot);
				crh.setIsDeleted(false);
				crh.setRoaster(dailyOldObj);
				crh.setTrip(tempCatARotationObj.getTripMaster());
				crhList.add(crh);
				
				//dailyOldObj.setBusRota(brhList);
				//dailyOldObj.setDriverRota(drhList);
				//dailyOldObj.setConductorRota(crhList);
				
				if(tempCatARotationObj.getTripMaster().getRouteMaster().getRouteTypeMaster().getRouteTypeName().equals("Normal")){
					dailyOldObj.setRotaTypeFlag("Normal");
				}else if(tempCatARotationObj.getTripMaster().getRouteMaster().getRouteTypeMaster().getRouteTypeName().equals("Special")){
					dailyOldObj.setRotaTypeFlag("Special");
				}
				
				
				drListOld.add(dailyOldObj);
				
			}	
			if(rotationsList.size()==0)	{
				return new ResponseEntity<>(new ResponseStatus("Rotations are not available for : "+tripObj.getTripCode(), HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
				Integer counter = 0;
				if(dtoObj.getDriverId() != null){
					RestAlloactionDriverConductor driverObj = restAllocationRepository.findbyDriverId(dtoObj.getDriverId());
					if(driverObj != null && driverObj.getId() != null){
						if(driverObj.getCounter().equals(5)){
							Integer updatedRestCount = driverObj.getRestCount() + 1;
							int i = restAllocationRepository.updateRestAlloactionDriverConductor(0, updatedRestCount, driverObj.getId());
							
						}else{
							 counter = driverObj.getCounter() + 1;
							int i = restAllocationRepository.updateRestAlloactionDriverConductor(counter, driverObj.getRestCount(), driverObj.getId());
						}
					}
				
					else{
						RestAlloactionDriverConductor restObj = new RestAlloactionDriverConductor();
						DriverMaster tempDMaster = driverRepo.findById(dtoObj.getDriverId()).get();
						restObj.setDriverMaster(tempDMaster);
						restObj.setCounter(1);
						restObj.setRestCount(0);
						restAllocationRepository.save(restObj);
					}
				}	
				
				if(dtoObj.getConductorId() != null){
				RestAlloactionDriverConductor conductorObj = restAllocationRepository.findbyConductorId(dtoObj.getConductorId());
				if(conductorObj != null && conductorObj.getId() != null){
					if(conductorObj.getCounter().equals(5)){
						Integer updatedRestCount = conductorObj.getRestCount() + 1;
						int i = restAllocationRepository.updateRestAlloactionDriverConductor(0, updatedRestCount, conductorObj.getId());
						
					}else{
						  counter = conductorObj.getCounter() + 1;
						int i = restAllocationRepository.updateRestAlloactionDriverConductor(counter, conductorObj.getRestCount(), conductorObj.getId());
					}
				}
				else{
					RestAlloactionDriverConductor restObj = new RestAlloactionDriverConductor();
					ConductorMaster tempConductor = conductorRepo.findById(dtoObj.getConductorId()).get();
					restObj.setConductorMaster(tempConductor);
					restObj.setCounter(1);
					restObj.setRestCount(0);
					restAllocationRepository.save(restObj);
				}
			
			}	
			}
			roaster.setDailyRoasterList(drList);
			roaster = roasterRepo.save(roaster);

			roasterObj.setDailyRoasterList(drListOld);
			roasterObj = roasterOldRepo.save(roasterObj);
			return new ResponseEntity<>(
					new ResponseStatus("Roster successfully generated for " + roaster.getRotaDate() + " .",
							HttpStatus.OK),
					HttpStatus.OK);
			}
		}catch (Exception e) {
			log.info(e + "saveManualSpecialRota");
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}

	@Override
	public List<RouteTypeDto> routeTypeMasterOnLoad() {
		List<RouteTypeDto> routeTypeList = null;
		try{
		   routeTypeList = routeTypeRepository.findAllByStatus(true).stream()
				.map(routeType -> new RouteTypeDto(routeType.getId(), routeType.getRouteTypeName()))
				.collect(Collectors.toList());
		   
		   for(RouteTypeDto routeDo : routeTypeList){
			   if(routeDo.getRouteTypeName().equals("Normal")){
				   routeDo.setRouteTypeName("Manual");
			   }
		   }
		}catch(Exception e){
			log.info(e + "routeTypeMasterOnLoad");
		}
		return routeTypeList;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> updateManualSpecialRota(List<ManualSpecialRotaFormSaveDto> childObjList) {
		BusMaster busMaster = new BusMaster();
		DriverMaster driverMaster = new DriverMaster();
		ConductorMaster conductorMaster = new ConductorMaster();
		DailyRoasterHistory historyObj = new DailyRoasterHistory();
		Roaster rota = new Roaster();
		WorkFlowHistory workFlowHistoryObj = null; 
		try{
			for(ManualSpecialRotaFormSaveDto childObj : childObjList){
			Optional<DailyRoaster> dailyObj = dailyRoasterRepository.findById(childObj.getRotaId());
			//List<DailyRoaster> roasterList = dailyRoasterRepository.findAllByRotaAndTripId(childObj.getRotaId(),childObj.getTripId());
			rota = dailyObj.get().getRota();
			historyObj.setBus(dailyObj.get().getBus());
			historyObj.setDriver(dailyObj.get().getDriver());
			historyObj.setConductor(dailyObj.get().getConductor());
			//if(roasterList.size() > 0){
				//historyObj.setBus(roasterList.get(0).getBus());
				//historyObj.setDriver(roasterList.get(0).getDriver());
				//historyObj.setConductor(roasterList.get(0).getConductor());
			historyObj.setDailyRoaster(dailyObj.get());
			//historyObj.setDepot(depot);
			historyObj.setIsDeleted(false);
			historyObj.setNightMaster(dailyObj.get().getNightMaster());
			//historyObj.setOldDepot(oldDepot);
			//if(childObj != null && childObj.getOverrideReason() != null)
			historyObj.setOverrideReason(dailyObj.get().getOverrideReason());
			if(dailyObj.get().getRefueling() != null)
			historyObj.setRefueling(historyObj.getRefueling());
			//if(childObj != null && childObj.getRemarks() != null)
			historyObj.setRemarks(dailyObj.get().getRemarks());
			historyObj.setRotationAvailabilityDate(dailyObj.get().getRotationAvailabilityDate());
			historyObj.setRotaTypeFlag(dailyObj.get().getRotaTypeFlag());
			//RouteMaster routeMaster = new RouteMaster();
			//routeMaster.setId(childObj.getRouteId());
			historyObj.setRoute(dailyObj.get().getRoute());
			historyObj.setRouteType(dailyObj.get().getRouteType());
			historyObj.setTrip(dailyObj.get().getTrip());
			//historyObj.setTrip(trip);
			//historyObj.setTripRotation(tripRotation);
			//}
			//for(DailyRoaster dailyObj : roasterList){
				busMaster.setId(childObj.getBusId());
				driverMaster.setId(childObj.getDriverId());
				conductorMaster.setId(childObj.getConductorId());
				dailyObj.get().setBus(busMaster);
				dailyObj.get().setDriver(driverMaster);
				dailyObj.get().setConductor(conductorMaster);
				dailyRoasterRepository.save(dailyObj.get());
			//}
			dailyRoasterHistoryRepository.save(historyObj);
			MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Pending");
			if (masterStatus != null && masterStatus.getId() != null) {
				dailyRoasterAutoRepository.approveOrRejectManualSpecialRota(masterStatus.getId(), rota.getId());
			}
			workFlowHistoryObj = new WorkFlowHistory();
			workFlowHistoryObj.setBus(busMaster);
			workFlowHistoryObj.setDriver(driverMaster);
			workFlowHistoryObj.setConductor(conductorMaster);
			workFlowHistoryObj.setMasterStatus(masterStatus);
			//workFlowHistoryObj.setRemarks(remarks);
			workFlowHistoryObj.setRota(dailyObj.get().getRota());
			workFlowHistoryObj.setRoute(dailyObj.get().getRoute());
			workFlowHistoryObj.setTrip(historyObj.getTrip());
			workFlowHistoryObj.setOverrideReason(dailyObj.get().getOverrideReason());
			workFlowHistoryRepository.save(workFlowHistoryObj);
			
			//roasterOldRepo.save(rota);
		/*List<DailyRoasterAuto> newRoasterList = dailyRoasterAutoRepository.findAllByRotaAndTripId(childObj.getRotaId(),childObj.getTripId());
		for(DailyRoasterAuto dailyObj : newRoasterList){
			busMaster.setId(childObj.getBusId());
			driverMaster.setId(childObj.getDriverId());
			conductorMaster.setId(childObj.getConductorId());
			dailyObj.setBus(busMaster);
			dailyObj.setDriver(driverMaster);
			dailyObj.setConductor(conductorMaster);
			dailyRoasterAutoRepository.save(dailyObj);
		}*/
		}
		
		}catch(Exception e){
			log.info(e + "updateManualSpecialRota");
		}
		return new ResponseEntity<>(
				new ResponseStatus("Roaster updated successfully.",
						HttpStatus.OK),
				HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> approveManualSpecialRota(Integer rotaId) {
		int i = 0;
		BusRotaHistory brh = null;
		DriverRotaHistory drh = null;
		ConductorRotaHistory crh = null;
		BusMaster busMaster = null;
		DriverMaster driverMaster = null;
		ConductorMaster conductorMaster = null;
		DailyRoaster dailyOldObj = null;
		String response = "";
		try {
			MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Approved");
			Optional<Roaster> roasterObjTemp = roasterOldRepo.findById(rotaId);
			List<Object[]> dailyRoasterList = dailyRoasterRepository.fetchRosterDetailsByRotaId(rotaId);
			for (Object[] dailyObj : dailyRoasterList) {
				brh = new BusRotaHistory();
				drh = new DriverRotaHistory();
				crh = new ConductorRotaHistory();
				Integer currentTripId = null;
				Integer busId = null;
				Integer driverId = null;
				Integer conductorId = null;
				Integer dailyId = null;
				if (dailyObj[2] != null) {
					String tripId = dailyObj[2].toString();
					currentTripId = Integer.parseInt(tripId);
				}
				Optional<TripMaster> tripObj = tripMasterRepository.findById(currentTripId);
				String tempDailyId = dailyObj[0].toString();
				dailyId = Integer.parseInt(tempDailyId);
				List<ConductorRotaHistory> historyList = conductorRotaHistoryRepository.findAllByDailyRoasterId(dailyId);
				if(historyList != null && historyList.size() > 0){
					continue;
				}else{
					generateAlert(roasterObjTemp.get().getDepot());
				}
				Optional<DailyRoaster> dailyRoasterObj = dailyRoasterRepository.findById(dailyId);
				brh.setReturnDate(dailyRoasterObj.get().getRotationAvailabilityDate());
				crh.setReturnDate(dailyRoasterObj.get().getRotationAvailabilityDate());
				drh.setReturnDate(dailyRoasterObj.get().getRotationAvailabilityDate());
				/*if (tempCatARotationObj[1] != null) {
					if (tempCatARotationObj[1].toString().equals("N + 0")) {
						brh.setReturnDate(this.getDate(1, new Date()));
						crh.setReturnDate(this.getDate(1, new Date()));
						drh.setReturnDate(this.getDate(1, new Date()));
					} else if (tempCatARotationObj[1].toString().equals("N + 1")) {
						brh.setReturnDate(this.getDate(2, new Date()));
						crh.setReturnDate(this.getDate(2, new Date()));
						drh.setReturnDate(this.getDate(2, new Date()));
					} else if (tempCatARotationObj[1].toString().equals("N + 2")) {
						brh.setReturnDate(this.getDate(3, new Date()));
						crh.setReturnDate(this.getDate(3, new Date()));
						drh.setReturnDate(this.getDate(3, new Date()));
					} else if (tempCatARotationObj[1].toString().equals("N + 3")) {
						brh.setReturnDate(this.getDate(4, new Date()));
						crh.setReturnDate(this.getDate(4, new Date()));
						drh.setReturnDate(this.getDate(4, new Date()));
					}
				}*/

				brh.setReturnTime(tripObj.get().getTripEndTime());
				drh.setReturnTime(tripObj.get().getTripEndTime());
				crh.setReturnTime(tripObj.get().getTripEndTime());

				busMaster = new BusMaster();
				driverMaster = new DriverMaster();
				conductorMaster = new ConductorMaster();
				dailyOldObj = new DailyRoaster();
				
				String tempBusId = dailyObj[3].toString();
				String tempDriverId = dailyObj[4].toString();
				String tempConductorId = dailyObj[5].toString();
				busId = Integer.parseInt(tempBusId);
				driverId = Integer.parseInt(tempDriverId);
				conductorId = Integer.parseInt(tempConductorId);
				
				busMaster.setId(busId);
				driverMaster.setId(driverId);
				conductorMaster.setId(conductorId);
				dailyOldObj.setId(dailyId);
				brh.setBus(busMaster);
				brh.setDepot(roasterObjTemp.get().getDepot());
				brh.setIsDeleted(false);
				brh.setRoaster(dailyOldObj);
				brh.setTrip(tripObj.get());
				busRotaHistoryRepository.save(brh);

				drh.setDriver(driverMaster);
				drh.setDepot(roasterObjTemp.get().getDepot());
				drh.setIsDeleted(false);
				drh.setRoaster(dailyOldObj);
				drh.setTrip(tripObj.get());
				driverRotaHistoryRepository.save(drh);

				crh.setConductor(conductorMaster);
				crh.setDepot(roasterObjTemp.get().getDepot());
				crh.setIsDeleted(false);
				crh.setRoaster(dailyOldObj);
				crh.setTrip(tripObj.get());
				conductorRotaHistoryRepository.save(crh);
				
			}	
			if (masterStatus != null && masterStatus.getId() != null) {
				i = dailyRoasterAutoRepository.approveOrRejectManualSpecialRota(masterStatus.getId(), rotaId);
				if (i > 0) {
					return new ResponseEntity<>(new ResponseStatus("Roster approved successfully.", HttpStatus.OK),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> rejectManualSpecialRota(RotaRejectParentDto parentObj) {
		int i = 0;
		int j = 0;
		WorkFlowHistory workFlowHistoryObj = null;
		try{
			MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Rejected");
			if (masterStatus != null && masterStatus.getId() != null) {
				i = dailyRoasterAutoRepository.approveOrRejectManualSpecialRota(masterStatus.getId(), parentObj.getRotaId());
				if (i > 0) {
					for (RotaRejectChildDto childObj : parentObj.getRotaChildList()) {
						  j = dailyRoasterRepository.updateDailyRoasterById(childObj.getRemarks(),
								childObj.getDailyRoasterId());
						  Optional<DailyRoaster> dailyObj = dailyRoasterRepository.findById(childObj.getDailyRoasterId());
						  workFlowHistoryObj = new WorkFlowHistory();
							workFlowHistoryObj.setBus(dailyObj.get().getBus());
							workFlowHistoryObj.setDriver(dailyObj.get().getDriver());
							workFlowHistoryObj.setConductor(dailyObj.get().getConductor());
							workFlowHistoryObj.setMasterStatus(masterStatus);
							workFlowHistoryObj.setRemarks(dailyObj.get().getRemarks());
							workFlowHistoryObj.setRota(dailyObj.get().getRota());
							workFlowHistoryObj.setRoute(dailyObj.get().getRoute());
							workFlowHistoryObj.setTrip(dailyObj.get().getTrip());
							workFlowHistoryRepository.save(workFlowHistoryObj);
					}
					return new ResponseEntity<>(new ResponseStatus("Roster rejected successfully.", HttpStatus.OK),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
			}
		}catch(Exception e){
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}
	
	private void generateAlert(DepotMaster depot) {
		
		String unavailableConductorAlert = "UNCONA";
		String unavailableDriverAlert = "UNDRIA";
		String response = "";
		try {
			MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Approved");
			SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sm1 = new SimpleDateFormat("dd-MM-yyyy");
			Date date = null;
			String strDate = sm.format(new Date());
			date = sm.parse(strDate);
			Integer depoMasterId = depotMasterRepo.getIdByDepoName(depot.getDepotName());
			List<Object[]> unavailableConductorList = conductorUnavailableRepository
					.unvailableConductorsAlert(depoMasterId);
			for (Object[] unavailableList : unavailableConductorList) {

				String driverConductorFlg = "C";
				Optional<ConductorMaster> conductor = conductorRepo
						.findById(Integer.parseInt(unavailableList[0].toString()));
				response = smsTrigger.sendAbsentSMS(conductor.get().getId(), masterStatus.getShortCode(),
						driverConductorFlg, date);
				String fromDate = sm.format(unavailableList[3]);
				String toDate = sm.format(unavailableList[4]);
				for (String s : roleIds) {
					notificationEntity d = fuelNotificationRepo.checkExistingNotification(conductor.get().getId(),
							unavailableConductorAlert, s);
					if (d == null && s != null && !s.isEmpty()) {
						alertUtility.insertNotification(
								"Conductor:- " + conductor.get().getConductorName() + " ("
										+ conductor.get().getConductorCode() + ")" + " is Absent from "+fromDate+" to "+toDate,
								unavailableConductorAlert, "Conductor", conductor.get().getId(),
								conductor.get().getDepot().getId(), s);
					}
				}

			}

			List<Object[]> unavailableDriverList = driverUnavailableRepository.unvailableDriverAlert(depoMasterId);
			for (Object[] unavailableList : unavailableDriverList) {
				String driverConductorFlg = "D";
				Optional<DriverMaster> driver = driverRepo.findById(Integer.parseInt(unavailableList[0].toString()));
				response = smsTrigger.sendAbsentSMS(driver.get().getId(), masterStatus.getShortCode(),
						driverConductorFlg, date);
				String fromDate = sm.format(unavailableList[3]);
				String toDate = sm.format(unavailableList[4]);
				for (String s : roleIds) {
					notificationEntity d = fuelNotificationRepo.checkExistingNotification(driver.get().getId(),
							unavailableDriverAlert, s);
					if (d == null && s != null && !s.isEmpty()) {
						alertUtility.insertNotification(
								"Driver:- " + driver.get().getDriverName() + " (" + driver.get().getDriverCode() + ")"
										+ " is Absent from "+fromDate+" to "+toDate,
								unavailableDriverAlert, "Driver", driver.get().getId(), driver.get().getDepot().getId(),
								s);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
