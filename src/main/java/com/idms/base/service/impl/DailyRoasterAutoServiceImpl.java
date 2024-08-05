package com.idms.base.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.DailyRoasterAutoDto;
import com.idms.base.api.v1.model.dto.SameRoutesHelperForRota;
import com.idms.base.dao.entity.AuthorizeRoute;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusRotaHistory;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.ConductorRotaHistory;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DailyRoasterAuto;
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
import com.idms.base.dao.repository.AuthorizeRouteRepository;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusRefuelingMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DailyRoasterAutoRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.MasterStatusRepository;
import com.idms.base.dao.repository.RestAlloactionDriverConductorRepository;
import com.idms.base.dao.repository.RoasterAutoRepository;
import com.idms.base.dao.repository.RoasterRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.dao.repository.TripRotationReposiotry;
import com.idms.base.service.DailyRoasterServiceAuto;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class DailyRoasterAutoServiceImpl implements DailyRoasterServiceAuto {
	
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired 
	TripMasterRepository tripRepo;
	
	@Autowired
	DriverMasterRepository driverRepo;
	
	@Autowired
	ConductorMasterRepository conductorRepo;
	
	@Autowired
	BusMasterRepository busRepo;
	
	@Autowired
	RoasterAutoRepository roasterRepo;
	
	@Autowired
	RoasterRepository roasterOldRepo;
	
	@Autowired
	DailyRoasterAutoRepository dailyRoasterAutoRepository;
	
	@Autowired
	DailyRoasterRepository dailyRoasterRepository;
	
	@Autowired
	BusRefuelingMasterRepository refuellingRepository;
	
	@Autowired
	TripRotationReposiotry tripRotationReposiotry;
	
	@Autowired
	MasterStatusRepository masterStatusRepository;
	
	@Autowired
	RestAlloactionDriverConductorRepository restAllocationRepository;
	
	@Autowired
	AuthorizeRouteRepository authorizeRouteRepository;
	
	
	/*@Override
	public ResponseEntity<ResponseStatus> generateAutoRoaster(String dpCode, Integer tpId, Date date) {
		DepotMaster depot = depotRepo.findByDepotCode(dpCode);
		RoasterAuto roasterTemp = roasterRepo.findByDate(depot.getId(), tpId);
		List<TripMaster> tripList = tripRepo.findByRouteType(depot.getId(),tpId);
		DailyRoasterAuto dailyObj = null;
		Date rotaDate = this.getDate(1, new Date());
		RoasterAuto roaster = new RoasterAuto();
		Roaster roasterObj = new  Roaster();
		DailyRoaster dailyOldObj = null;
		List<DailyRoaster> drListOld = new ArrayList<>();
		List<DailyRoasterAuto> drList = new ArrayList<>();
		int i = 0;
		Calendar c = Calendar.getInstance();
		TransportUnitMaster tu = new TransportUnitMaster();
		tu.setId(1);
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
		Date dt = new Date();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		roaster.setRotaDate(this.getDate(1, new Date()));
		roaster.setRoastedCode(depot.getId() + "/" + tpId + "/" + sdf.format(dt));
		
		roasterObj.setRotaDate(this.getDate(1, new Date()));
		roasterObj.setRoastedCode(depot.getId() + "/" + tpId + "/" + sdf.format(dt));
		try {
			if(tripList.size() == 0){
				return new ResponseEntity<>(
						new ResponseStatus("Roaster could'nt be generated for depot " + depot.getDepotName() + " .", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			if(roasterTemp == null){
			for (TripMaster trip : tripList) {
				
				 * boolean flag = true; for (RoutePermitMaster rpm :
				 * trip.getRouteMaster().getRoutePermitMasterList()) {
				 * PermitDetailsMaster permitDto = rpm.getPermitDetailsMaster();
				 * if (permitDto.getValidUpTo() != null &&
				 * (permitDto.getValidUpTo().after(today) ||
				 * formatter.format(permitDto.getValidUpTo()) ==
				 * (formatter.format(today)))) { flag=true; } else { flag=false;
				 * break; } } if(flag){ tripListWithPermit.add(trip); }
				 
				dailyObj = new DailyRoasterAuto();
				
				dailyOldObj = new  DailyRoaster();

				List<Object[]> driverList = driverRepo.getAvailableDrivers(depot.getId(), tpId, rotaDate);
				//DriverMaster driver = new DriverMaster();
				//driver.setId(Integer.parseInt((driverList.get(i)[0]).toString()));
				//dailyObj.setDriver(driver);
				//dailyOldObj.setDriver(driver);

				List<Object[]> conductorList = conductorRepo.getAvailableConductors(depot.getId(),tpId, rotaDate);
				ConductorMaster conductor = new ConductorMaster();
				conductor.setId(Integer.parseInt((conductorList.get(i)[0].toString())));
				dailyObj.setConductor(conductor);
				dailyOldObj.setConductor(conductor);

				List<Object[]> busList = busRepo.getAvailableBusesForRota(depot.getId(), tpId, rotaDate);
				BusMaster busMaster = new BusMaster();
				if (i<busList.size()) {
					busMaster.setId(Integer.parseInt((busList.get(i)[0].toString())));
					
				}
				dailyObj.setBus(busMaster);
				dailyOldObj.setBus(busMaster);
				
				DriverMaster driver = new DriverMaster();
				if(busList.get(i)[4] != null)
				driver.setId(Integer.parseInt((busList.get(i)[4]).toString()));
				else if(busList.get(i)[5] != null)
				driver.setId(Integer.parseInt((busList.get(i)[5]).toString()));
				else
				driver.setId(Integer.parseInt((driverList.get(i)[0]).toString()));	
				dailyObj.setDriver(driver);
				dailyOldObj.setDriver(driver);
				

				dailyObj.setRotaAuto(roaster);
				dailyObj.setTripStatus(false);
				dailyObj.setRoute(trip.getRouteMaster());
				dailyObj.setIsDeleted(false);
				dailyObj.setRouteType(trip.getRouteMaster().getRouteTypeMaster());
				dailyObj.setTrip(trip);
				drList.add(dailyObj);
				
				dailyOldObj.setRota(roasterObj);
				dailyOldObj.setTripStatus(false);
				dailyOldObj.setRoute(trip.getRouteMaster());
				dailyOldObj.setIsDeleted(false);
				dailyOldObj.setRouteType(trip.getRouteMaster().getRouteTypeMaster());
				dailyOldObj.setTrip(trip);
				drListOld.add(dailyOldObj);
				i++;
			}
			roaster.setDailyRoasterList(drList);
			roaster = roasterRepo.save(roaster);
			
			roasterObj.setDailyRoasterList(drListOld);
			roasterObj = roasterOldRepo.save(roasterObj);
			return new ResponseEntity<>(
					new ResponseStatus("Roaster successfully generated for "+roaster.getRotaDate() + " .", HttpStatus.OK),
					HttpStatus.OK);
			}else{
				return new ResponseEntity<>(
						new ResponseStatus("Roaster already generated for "+roaster.getRotaDate() + " .", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
			//e.printStackTrace();
		}
		
		//return null;
	}*/

	@Override
	public List<DailyRoasterAutoDto> getGeneratedRoasterList(String dpCode, String tuId) {
		Integer transportId = Integer.parseInt(tuId);
		List<DailyRoasterAutoDto> list = new ArrayList<>();
		DailyRoasterAutoDto obj = null;
		List<RoasterAuto> rotaObj = roasterRepo.findAllRotaByDepot(dpCode, transportId);
		for(DailyRoasterAuto tempObj : rotaObj.get(0).getDailyRoasterList()){
			obj = new DailyRoasterAutoDto();
			if(tempObj.getBus() != null)
			obj.setBusModel(tempObj.getBus().getBusModel());
			if(tempObj.getConductor() != null && tempObj.getConductor().getConductorCategory() != null)
			obj.setConductorCategory(tempObj.getConductor().getConductorCategory().getRouteCategoryName());
			if(tempObj.getConductor() != null)
			obj.setConductorCode(tempObj.getConductor().getConductorCode());
			if(tempObj.getConductor() != null)
			obj.setConductorName(tempObj.getConductor().getConductorName());
			if(tempObj.getDriver() != null && tempObj.getDriver().getDriverCategory() != null) 
			obj.setDriverCategory(tempObj.getDriver().getDriverCategory().getRouteCategoryName());
			if(tempObj.getDriver() != null)
			obj.setDriverCode(tempObj.getDriver().getDriverCode());
			if(tempObj.getDriver() != null && tempObj.getDriver().getLicenceValidity() != null)
			obj.setDriverLiscenseValidity(tempObj.getDriver().getLicenceValidity().toString());
			if(tempObj.getBus() != null && tempObj.getBus().getPrimaryDriver() != null)
			obj.setPrimaryDriver(tempObj.getBus().getPrimaryDriver().getDriverCode());
			if(tempObj.getRoute() != null && tempObj.getRoute().getRouteCategoryMaster() != null)
			obj.setRouteCategory(tempObj.getRoute().getRouteCategoryMaster().getRouteCategoryName());
			//if(tempObj.getRoute() != null)
			//obj.setRouteId(tempObj.getRoute().getRouteId());
			//if(tempObj.getRoute() != null)
			//obj.setRouteName(tempObj.getRoute().getRouteName());
			if(tempObj.getBus() != null && tempObj.getBus().getSecondaryDriver() != null)
			obj.setSecondryDriver(tempObj.getBus().getSecondaryDriver().getDriverCode());
			//if(tempObj.getTrip() != null)
			//obj.setTripServiceId(tempObj.getTrip().getServiceId());
			if(tempObj.getTripRotation() != null && tempObj.getTrip().getTripStartTime() != null)
			obj.setTripStartTime(tempObj.getTripRotation().getStartTime().toString());
			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getEndTime() != null)
			obj.setTripEndTime(tempObj.getTripRotation().getEndTime().toString());
			
			list.add(obj);
		}
		return list;
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
	public ResponseEntity<ResponseStatus> generateAutoRoaster(String dpCode, Integer tpId, Date date) {
		List<TripMaster> catATripList = new ArrayList<>();
		List<TripMaster> catBTripList = new ArrayList<>();
		List<TripMaster> catCTripList = new ArrayList<>();
		List<TripMaster> catDTripList = new ArrayList<>();

		List<BusMaster> catABusList = new CopyOnWriteArrayList<>();
		List<BusMaster> catBBusList = new CopyOnWriteArrayList<>();
		List<BusMaster> catCBusList = new CopyOnWriteArrayList<>();
		List<BusMaster> catDBusList = new CopyOnWriteArrayList<>();

		List<DriverMaster> catADriverList = new CopyOnWriteArrayList<>();
		List<DriverMaster> catBDriverList = new CopyOnWriteArrayList<>();
		List<DriverMaster> catCDriverList = new CopyOnWriteArrayList<>();
		List<DriverMaster> catDDriverList = new CopyOnWriteArrayList<>();

		List<ConductorMaster> catAConductorList = new CopyOnWriteArrayList<>();
		List<ConductorMaster> catBConductorList = new CopyOnWriteArrayList<>();
		List<ConductorMaster> catCConductorList = new CopyOnWriteArrayList<>();
		List<ConductorMaster> catDConductorList = new CopyOnWriteArrayList<>();

		Map<Integer, String> availableDriverMap = new HashMap<>();
		Map<Integer, SameRoutesHelperForRota> sameAClassRouteMap = new HashMap<>();
		Map<Integer, SameRoutesHelperForRota> sameBClassRouteMap = new HashMap<>();
		Map<Integer, SameRoutesHelperForRota> sameCClassRouteMap = new HashMap<>();
		Map<Integer, SameRoutesHelperForRota> sameDClassRouteMap = new HashMap<>();
		
		Map<Integer, String> driverRestMap = new HashMap<>();
		Map<Integer, String> conductorRestMap = new HashMap<>();

		DepotMaster depot = depotRepo.findByDepotCode(dpCode);
		Roaster roasterTemp = roasterOldRepo.findByDate(depot.getId(), tpId);
		RoasterAuto roasterAuto = roasterRepo.findByDate(depot.getId(), tpId);
		List<TripMaster> tripList = tripRepo.findAllTripsByAvailableRoutes(depot.getId(), tpId,this.getDate(1, new Date()));
		
		if(roasterTemp != null){
			List<DailyRoaster> roasterList = roasterTemp.getDailyRoasterList();
			if(roasterList.size() > 0){
				for(DailyRoaster dailyRotaObj : roasterList){
					if(dailyRotaObj.getTrip() != null){
						if(tripList.contains(dailyRotaObj.getTrip())){
							tripList.remove(dailyRotaObj.getTrip());
						}
					}
				}
			}
		}
		
		for (TripMaster tripObj : tripList) {
			if (tripObj != null && tripObj.getRouteMaster() != null
					&& tripObj.getRouteMaster().getRouteCategoryMaster() != null) {
				if ((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("A")) {
					catATripList.add(tripObj);
				} else if ((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("B")) {
					catBTripList.add(tripObj);
				} else if ((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("C")) {
					catCTripList.add(tripObj);
				} else if ((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("D")) {
					catDTripList.add(tripObj);
				}

			}
		}
		List<Object[]> driverList = new ArrayList<>();
		Date rotaDate = this.getDate(1, new Date());
	    driverList = driverRepo.getAvailablilityDriversForRota(depot.getId(), tpId, rotaDate);
		for (Object[] driverObj : driverList) {
			DriverMaster driver = new DriverMaster();
			if (driverObj[0] != null) {
				driver.setId(Integer.parseInt((driverObj[0]).toString()));
				availableDriverMap.put(Integer.parseInt((driverObj[0]).toString()), "");
			}
			if (driverObj[1] != null)
				driver.setDriverAvailableTime(LocalTime.parse((driverObj[1]).toString()));
			if (driverObj[2] != null) {
				if (driverObj[2].toString().equals("A")) {
					catADriverList.add(driver);
				} else if (driverObj[2].toString().equals("B")) {
					catBDriverList.add(driver);
				} else if (driverObj[2].toString().equals("C")) {
					catCDriverList.add(driver);
				} else if (driverObj[2].toString().equals("D")) {
					catDDriverList.add(driver);
				}
			}
		}

		List<Object[]> conductorList = conductorRepo.getAvailablilityConductorsForRota(depot.getId(), tpId, rotaDate);
		for (Object[] conductorObj : conductorList) {
			ConductorMaster conductor = new ConductorMaster();
			if (conductorObj[0] != null)
				conductor.setId(Integer.parseInt((conductorObj[0]).toString()));
			if (conductorObj[1] != null)
				conductor.setConductorAvailableTime(LocalTime.parse((conductorObj[1]).toString()));
			if (conductorObj[2] != null) {
				if (conductorObj[2].toString().equals("A")) {
					catAConductorList.add(conductor);
				} else if (conductorObj[2].toString().equals("B")) {
					catBConductorList.add(conductor);
				} else if (conductorObj[2].toString().equals("C")) {
					catCConductorList.add(conductor);
				} else if (conductorObj[2].toString().equals("D")) {
					catDConductorList.add(conductor);
				}
			}
		}

		List<Object[]> busList = busRepo.getAvailabilityForRota(depot.getId(), tpId, rotaDate);
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

			if (busObj[4] != null) {
				if ((Integer.parseInt(busObj[4].toString()) >= 2018)) {
					catABusList.add(busMaster);
				} else if ((Integer.parseInt(busObj[4].toString()) >= 2015
						&& (Integer.parseInt(busObj[4].toString()) < 2018))) {
					catBBusList.add(busMaster);
				} else if ((Integer.parseInt(busObj[4].toString()) >= 2012
						&& (Integer.parseInt(busObj[4].toString()) < 2015))) {
					catCBusList.add(busMaster);
				} else if ((Integer.parseInt(busObj[4].toString()) < 2012)) {
					catDBusList.add(busMaster);
				}
			}
		}

		DailyRoasterAuto dailyObj = null;
		RoasterAuto roaster = new RoasterAuto();
		Roaster roasterObj = new Roaster();
		DailyRoaster dailyOldObj = null;
		List<DailyRoaster> drListOld = new ArrayList<>();
		List<DailyRoasterAuto> drList = new ArrayList<>();
		BusRotaHistory brh = null;
		DriverRotaHistory drh = null;
		ConductorRotaHistory crh = null;
		List<BusRotaHistory> brhList = new ArrayList<>();
		List<DriverRotaHistory> drhList = new ArrayList<>();
		List<ConductorRotaHistory> crhList = new ArrayList<>();
		
		Calendar c = Calendar.getInstance();
		TransportUnitMaster tu = new TransportUnitMaster();
		tu.setId(tpId);
		roaster.setDepot(depot);
		roaster.setTransport(tu);
		roaster.setGenerationDate(new Date());
		//roaster.setIsNormalRota(true);
		//roaster.setIsSpecialRota(false);
		if(roasterTemp != null && roasterTemp.getId() != null){
			for(DailyRoaster rotaobject : roasterTemp.getDailyRoasterList()){
				if(rotaobject.getRotaTypeFlag() != null && rotaobject.getRotaTypeFlag().equals("Auto")){
					return new ResponseEntity<>(
							new ResponseStatus("Roster already generated for "+roasterTemp.getRotaDate() + " .", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
			}
			roasterObj.setId(roasterTemp.getId());
			
		}
		if(roasterAuto != null && roasterAuto.getId() != null){
			roaster.setId(roasterAuto.getId());
		}
		roasterObj.setDepot(depot);
		roasterObj.setTransport(tu);
		roasterObj.setGenerationDate(new Date());
		roasterObj.setIsNormalRota(true);
		roasterObj.setIsSpecialRota(false);
		Date dt = new Date();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		roaster.setRotaDate(this.getDate(1, new Date()));
		roaster.setRoastedCode(depot.getId() + "/" + tpId + "/" + sdf.format(dt));

		roasterObj.setRotaDate(this.getDate(1, new Date()));
		roasterObj.setRoastedCode(depot.getId() + "/" + tpId + "/" + sdf.format(dt));
		TripMaster tripObj = null;
		//String definedDayForTripComplete = null;
		int index = 0;
		int driverIndex = 0;
		SameRoutesHelperForRota helper = null;
		try {
			if (tripList.size() == 0) {
				return new ResponseEntity<>(
						new ResponseStatus("Roster could'nt be generated for depot " + depot.getDepotName() + " .",
								HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			//if(roasterTemp == null){
			Set<Integer> refuelingList = new HashSet<>();
			Float maxCurrentKmplOnDriverBasis = null;
			Float currentKmpl = null;
			String driverCode = null;
			Object[] maxCurrentKmplOnBusBasis = null;
			for (TripMaster tempCatAObj : catATripList) {
				dailyOldObj = new DailyRoaster();
				dailyObj = new DailyRoasterAuto();
				brh = new BusRotaHistory();
				drh = new DriverRotaHistory();
				crh = new ConductorRotaHistory();
				BusMaster busMaster = new BusMaster();
				helper = new SameRoutesHelperForRota();
				DriverMaster driveMaster = new DriverMaster();
				   if(catABusList.size() >  0){
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catABusList) {
							if(tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())){
								flag= true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catABusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
							}
								try{
							if (busListObj.getPrimaryDriver() != null && driverFlag) {
								maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
										tempCatAObj.getRouteMaster().getId(), busListObj.getId());
								if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
									if (tempCatAObj.getRouteMaster() != null
											&& tempCatAObj.getRouteMaster().getId() != null) {
										List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
												.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
														busListObj.getId(), busListObj.getPrimaryDriver().getId());
										if (dailyRoasterList.size() > 0) {
											for (DailyRoaster autoObj : dailyRoasterList) {
												if (autoObj != null && autoObj.getRefueling() != null
														&& autoObj.getRefueling().getId() != null) {
													refuelingList.add(autoObj.getRefueling().getId());
												}
											}
										}
										if (refuelingList.size() > 0) {
											maxCurrentKmplOnDriverBasis = refuellingRepository
													.fetchCurrentMaxKms(refuelingList);
											if (maxCurrentKmplOnBusBasis != null
													&& maxCurrentKmplOnBusBasis[0] != null) {
												Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
												currentKmpl = Float.parseFloat((obj[0]).toString());
												driverCode = obj[1].toString();
											}
											if (maxCurrentKmplOnDriverBasis < currentKmpl) {
												dailyObj.setSuggestion(
														"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
												dailyOldObj.setSuggestion(
														"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

											}
										}
									}

								}

							}}catch(Exception e){
								e.printStackTrace();
							}
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
									helper.setDriverMaster(busListObj.getSecondaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									} else if (catBDriverList.contains(driveMaster)) {
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catBDriverList.indexOf(driveMaster);
										catBDriverList.remove(driverIndex);
									}else if (catCDriverList.contains(driveMaster)) {
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catCDriverList.indexOf(driveMaster);
										catCDriverList.remove(driverIndex);
									}else if (catDDriverList.contains(driveMaster)) {
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
									}
								}
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
									helper.setBusMaster(busListObj);
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catABusList.remove(index);
								//i++;
								break;	
							}
						}
						if(!flag){
						for (BusMaster busListObj : catABusList) {
							     busMaster.setId(busListObj.getId());
							     helper.setBusMaster(busMaster);
								index = catABusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
									helper.setBusMaster(busListObj);
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catABusList.remove(index);
								//i++;
								break;
							}
						
						}
						
		     }else if(catBBusList.size() >  0){
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catBBusList) {
							if(tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime() != null  && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())){
								flag= true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catBBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if(tempCatAObj.getRouteMaster() != null&& sameAClassRouteMap.isEmpty()){
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catBBusList.remove(index);
								//x++;
								break;
							}
						}
						if(!flag){
						for (BusMaster busListObj : catBBusList) {
							  busMaster.setId(busListObj.getId());
							  helper.setBusMaster(busMaster);
								index = catBBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								
								}
								catBBusList.remove(index);
								//x++;
								break;
							}
						}
						
				} else if (catCBusList.size() > 0) {
						boolean flag = false;
						boolean driverFlag = false; 
						for (BusMaster busListObj : catCBusList) {
							if (tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime()!= null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())) {
								flag = true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catCBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									
								}
								catCBusList.remove(index);
								//m++;
								break;
							}
						}
						if (!flag) {
							for (BusMaster busListObj : catCBusList) {
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
									index = catCBusList.indexOf(busListObj);
									if(busListObj.getPrimaryDriver() != null){
										driveMaster.setId(busListObj.getPrimaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										}else if(catBDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catBDriverList.indexOf(driveMaster);
										catBDriverList.remove(driverIndex);
										}else if(catCDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catCDriverList.indexOf(driveMaster);
										catCDriverList.remove(driverIndex);
										}else if(catDDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									try{
									if (busListObj.getPrimaryDriver() != null && driverFlag) {
										maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
												tempCatAObj.getRouteMaster().getId(), busListObj.getId());
										if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
											if (tempCatAObj.getRouteMaster() != null
													&& tempCatAObj.getRouteMaster().getId() != null) {
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
														.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
																busListObj.getId(), busListObj.getPrimaryDriver().getId());
												if (dailyRoasterList.size() > 0) {
													for (DailyRoaster autoObj : dailyRoasterList) {
														if (autoObj != null && autoObj.getRefueling() != null
																&& autoObj.getRefueling().getId() != null) {
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if (refuelingList.size() > 0) {
													maxCurrentKmplOnDriverBasis = refuellingRepository
															.fetchCurrentMaxKms(refuelingList);
													if (maxCurrentKmplOnBusBasis != null
															&& maxCurrentKmplOnBusBasis[0] != null) {
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														currentKmpl = Float.parseFloat((obj[0]).toString());
														driverCode = obj[1].toString();
													}
													if (maxCurrentKmplOnDriverBasis < currentKmpl) {
														dailyObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
														dailyOldObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

													}
												}
											}

										}

									}}catch(Exception e){
										e.printStackTrace();
									}
									
									if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
										driveMaster.setId(busListObj.getSecondaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catADriverList.indexOf(driveMaster);
											catADriverList.remove(driverIndex);
											} else if (catBDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catBDriverList.indexOf(driveMaster);
												catBDriverList.remove(driverIndex);
											}else if (catCDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catCDriverList.indexOf(driveMaster);
												catCDriverList.remove(driverIndex);
											}else if (catDDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catDDriverList.indexOf(driveMaster);
												catDDriverList.remove(driverIndex);
											}
									}
									catCBusList.remove(index);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									//catCBusList.remove(index);
									//m++;
									break;
							}
					}	
				}else if (catDBusList.size() > 0) {
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catDBusList) {
							if (tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())) {
								flag = true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catDBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDBusList.remove(index);
								//n++;
								break;
							}
						}
						if (!flag) {
							for (BusMaster busListObj : catDBusList) {
								   busMaster.setId(busListObj.getId());
								   helper.setBusMaster(busMaster);
									index = catDBusList.indexOf(busListObj);
									if(busListObj.getPrimaryDriver() != null){
										driveMaster.setId(busListObj.getPrimaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										}else if(catBDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catBDriverList.indexOf(driveMaster);
										catBDriverList.remove(driverIndex);
										}else if(catCDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catCDriverList.indexOf(driveMaster);
										catCDriverList.remove(driverIndex);
										}else if(catDDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									try{
									if (busListObj.getPrimaryDriver() != null && driverFlag) {
										maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
												tempCatAObj.getRouteMaster().getId(), busListObj.getId());
										if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
											if (tempCatAObj.getRouteMaster() != null
													&& tempCatAObj.getRouteMaster().getId() != null) {
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
														.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
																busListObj.getId(), busListObj.getPrimaryDriver().getId());
												if (dailyRoasterList.size() > 0) {
													for (DailyRoaster autoObj : dailyRoasterList) {
														if (autoObj != null && autoObj.getRefueling() != null
																&& autoObj.getRefueling().getId() != null) {
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if (refuelingList.size() > 0) {
													maxCurrentKmplOnDriverBasis = refuellingRepository
															.fetchCurrentMaxKms(refuelingList);
													if (maxCurrentKmplOnBusBasis != null
															&& maxCurrentKmplOnBusBasis[0] != null) {
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														currentKmpl = Float.parseFloat((obj[0]).toString());
														driverCode = obj[1].toString();
													}
													if (maxCurrentKmplOnDriverBasis < currentKmpl) {
														dailyObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
														dailyOldObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

													}
												}
											}

										}

									} }catch(Exception e){
										e.printStackTrace();
									}
									
									
									if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
										driveMaster.setId(busListObj.getSecondaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catADriverList.indexOf(driveMaster);
											catADriverList.remove(driverIndex);
											} else if (catBDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catBDriverList.indexOf(driveMaster);
												catBDriverList.remove(driverIndex);
											}else if (catCDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catCDriverList.indexOf(driveMaster);
												catCDriverList.remove(driverIndex);
											}else if (catDDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catDDriverList.indexOf(driveMaster);
												catDDriverList.remove(driverIndex);
											}
									}
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catDBusList.remove(index);
									//n++;
									break;
							}
						}
					
				}else{
					continue;
				}
				   DriverMaster dMaster = new DriverMaster();
				   int objIndex = 0; 
				   if(helper.getDriverMaster() == null){
					   boolean flag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxKmplForDriver(tempCatAObj.getRouteMaster().getId(), helper.getBusMaster().getId());
					   for(Object[] rotaObj :dailyRoasterList){
								String driverId = rotaObj[0].toString();
								dMaster.setId(Integer.parseInt(driverId));
								if(catADriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catADriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catADriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catBDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catBDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catBDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catCDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catCDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catCDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catDDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catDDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catDDriverList.remove(objIndex);
									flag = true;
									break;
								}
								
						}
					}
				   if(!flag && catADriverList.size() > 0){
					   for (DriverMaster driverListObj : catADriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catADriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catADriverList.remove(index);
								//p++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catADriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catADriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catADriverList.remove(index);
									//p++;
									break;
							}
						}
				   }else if(!flag && catBDriverList.size() > 0){
					   for (DriverMaster driverListObj : catBDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catBDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catBDriverList.remove(index);
								//q++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catBDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catBDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catBDriverList.remove(index);
									//q++;
									break;
							}
						}
				   }else if(!flag && catCDriverList.size() > 0){
					   for (DriverMaster driverListObj : catCDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catCDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catCDriverList.remove(index);
								//r++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catCDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catCDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catCDriverList.remove(index);
									//r++;
									break;
							}
						}
				   }else if(!flag && catDDriverList.size() > 0){
					   for (DriverMaster driverListObj : catDDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catDDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDDriverList.remove(index);
								//s++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catDDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catDDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catDDriverList.remove(index);
									//s++;
									break;
							}
						}
				   }/*else{
					   continue;
				   }*/
				 }
				   
				   ConductorMaster cMaster = new ConductorMaster();
				   int obIndex = 0; 
				   if(helper.getConductorMaster() == null){
					   boolean conductorFlag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxEPKMForConductor();
					   for(Object[] rotaObj :dailyRoasterList){
								String conductorId = rotaObj[0].toString();
								cMaster.setId(Integer.parseInt(conductorId));
								if(catAConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catAConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catAConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catBConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catBConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catBConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catCConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catCConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catCConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catDConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catDConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catDConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}
					         	
						}
					}
				   
				   
				   
				   
				   
				   if(!conductorFlag && catAConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catAConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catAConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catAConductorList.remove(conIndex);
								//j++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catAConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catAConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catAConductorList.remove(conIndex);
								//j++;
								break;
							}
						
						}
						
				   }else if(!conductorFlag && catBConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catBConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catBConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catBConductorList.remove(conIndex);
								//k++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catBConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catBConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catBConductorList.remove(conIndex);
								//k++;
								break;
							}
						
						}
						
				   }else if(!conductorFlag && catCConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catCConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catCConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catCConductorList.remove(conIndex);
								//l++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catCConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catCConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catCConductorList.remove(conIndex);
								//l++;
								break;
							}
						
						}
						
				   }else if(!conductorFlag && catDConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catDConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catDConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catDConductorList.remove(conIndex);
								//y++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catDConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catDConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDConductorList.remove(conIndex);
								//y++;
								break;
							}
						
						}
						
				   }/*else{
					   continue;
				   }*/
				}
				   
				   if((!sameAClassRouteMap.isEmpty()) && sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
						SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
						drh = new DriverRotaHistory();
						brh = new BusRotaHistory();
						crh = new ConductorRotaHistory();		
						if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
							List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
							for(TripRotationEntity tempCatARotationObj : rotationsList){
								dailyOldObj = new DailyRoaster();
								dailyObj = new DailyRoasterAuto();
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
								
								brh.setReturnTime(tempCatAObj.getTripEndTime());
							    drh.setReturnTime(tempCatAObj.getTripEndTime());
							    crh.setReturnTime(tempCatAObj.getTripEndTime());
							    dailyOldObj.setTripRotation(tempCatARotationObj);
							    
							dailyObj.setBus(helperObj.getBusMaster());
							dailyOldObj.setBus(helperObj.getBusMaster());
							dailyObj.setDriver(helperObj.getDriverMaster());
							dailyOldObj.setDriver(helperObj.getDriverMaster());
							dailyObj.setConductor(helperObj.getConductorMaster());
							dailyOldObj.setConductor(helperObj.getConductorMaster());
							dailyObj.setRotaAuto(roaster);
							dailyObj.setTripStatus(false);
							dailyObj.setRoute(tempCatAObj.getRouteMaster());
							dailyObj.setIsDeleted(false);
							dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyObj.setTrip(tempCatAObj);
							dailyObj.setTripRotation(tempCatARotationObj);
							dailyObj.setRotaTypeFlag("Auto");
							drList.add(dailyObj);
							
							dailyOldObj.setRota(roasterObj);
							dailyOldObj.setTripStatus(false);
							dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
							dailyOldObj.setIsDeleted(false);
							dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyOldObj.setTrip(tempCatAObj);
							
							brh.setBus(helperObj.getBusMaster());
							brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							brh.setIsDeleted(false);
							brh.setRoaster(dailyOldObj);
							brh.setTrip(tempCatAObj);
							
							brhList.add(brh);
							
							drh.setDriver(helperObj.getDriverMaster());
							drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							drh.setIsDeleted(false);
							drh.setRoaster(dailyOldObj);
							drh.setTrip(tempCatAObj);
							drhList.add(drh);
							
							crh.setConductor(helperObj.getConductorMaster());
							crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							crh.setIsDeleted(false);
							crh.setRoaster(dailyOldObj);
							crh.setTrip(tempCatAObj);
							crhList.add(crh);
							
							//dailyOldObj.setBusRota(brhList);
							//dailyOldObj.setDriverRota(drhList);
							//dailyOldObj.setConductorRota(crhList);
							dailyOldObj.setRotaTypeFlag("Auto");
							drListOld.add(dailyOldObj);
							helper.setTripEndTime(tempCatAObj.getTripEndTime());
							helper.setTripStartTime(tempCatAObj.getTripEndTime());
							sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							}	
							continue;
						}
					}
				   
				   if(tempCatAObj.getRouteMaster() != null && (!(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())))){
					   helper.setTripStartTime(tempCatAObj.getTripStartTime());
						helper.setTripEndTime(tempCatAObj.getTripEndTime());
						sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
						}
				   
				List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
				if (rotationsList.size() == 0) {
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
				}
				for(TripRotationEntity tempCatARotationObj : rotationsList){
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					brh = new BusRotaHistory();
					drh = new DriverRotaHistory();
					crh = new ConductorRotaHistory();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
					
				
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
					
					brh.setReturnTime(tempCatAObj.getTripEndTime());
				    drh.setReturnTime(tempCatAObj.getTripEndTime());
				    crh.setReturnTime(tempCatAObj.getTripEndTime());
				
				dailyObj.setRotaAuto(roaster);
				dailyObj.setTripStatus(false);
				dailyObj.setRoute(tempCatAObj.getRouteMaster());
				dailyObj.setIsDeleted(false);
				dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyObj.setTrip(tempCatAObj);
				dailyObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyObj.setRotaTypeFlag("Auto");
				drList.add(dailyObj);

				dailyOldObj.setRota(roasterObj);
				dailyOldObj.setTripStatus(false);
				dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
				dailyOldObj.setIsDeleted(false);
				dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTrip(tempCatAObj);
				brh.setBus(helper.getBusMaster());
				brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				brh.setIsDeleted(false);
				brh.setRoaster(dailyOldObj);
				brh.setTrip(tempCatAObj);
				brhList.add(brh);
				
				drh.setDriver(helper.getDriverMaster());
				drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				drh.setIsDeleted(false);
				drh.setRoaster(dailyOldObj);
				drh.setTrip(tempCatAObj);
				drhList.add(drh);
				
				crh.setConductor(helper.getConductorMaster());
				crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				crh.setIsDeleted(false);
				crh.setRoaster(dailyOldObj);
				crh.setTrip(tempCatAObj);
				crhList.add(crh);
				
				//dailyOldObj.setBusRota(brhList);
				//dailyOldObj.setDriverRota(drhList);
				//dailyOldObj.setConductorRota(crhList);
				dailyOldObj.setRotaTypeFlag("Auto");
				drListOld.add(dailyOldObj);
				
				}
			}
			
			
			sameAClassRouteMap = new HashMap<>();
			for (TripMaster tempCatAObj : catBTripList) {
				dailyOldObj = new DailyRoaster();
				dailyObj = new DailyRoasterAuto();
				brh = new BusRotaHistory();
				drh = new DriverRotaHistory();
				crh = new ConductorRotaHistory();
				BusMaster busMaster = new BusMaster();
			    helper = new SameRoutesHelperForRota();
			    DriverMaster driveMaster = new DriverMaster();
				   if(catBBusList.size() >  0){
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catBBusList) {
							if(tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime() != null  && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())){
								flag= true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catBBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								if(!(driverFlag) &&  busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if(tempCatAObj.getRouteMaster() != null&& sameAClassRouteMap.isEmpty()){
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catBBusList.remove(index);
								//x++;
								break;
							}
						}
						if(!flag){
						for (BusMaster busListObj : catBBusList) {
							  busMaster.setId(busListObj.getId());
							  helper.setBusMaster(busMaster);
								index = catBBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								} 
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								
								
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								
								}
								catBBusList.remove(index);
								//x++;
								break;
							}
						}
						
				} else if (catCBusList.size() > 0) {
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catCBusList) {
							if (tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime()!= null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())) {
								flag = true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catCBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									
								}
								catCBusList.remove(index);
								//m++;
								break;
							}
						}
						if (!flag) {
							for (BusMaster busListObj : catCBusList) {
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
									index = catCBusList.indexOf(busListObj);
									if(busListObj.getPrimaryDriver() != null){
										driveMaster.setId(busListObj.getPrimaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										}else if(catBDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catBDriverList.indexOf(driveMaster);
										catBDriverList.remove(driverIndex);
										}else if(catCDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catCDriverList.indexOf(driveMaster);
										catCDriverList.remove(driverIndex);
										}else if(catDDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									try{
									if (busListObj.getPrimaryDriver() != null && driverFlag) {
										maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
												tempCatAObj.getRouteMaster().getId(), busListObj.getId());
										if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
											if (tempCatAObj.getRouteMaster() != null
													&& tempCatAObj.getRouteMaster().getId() != null) {
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
														.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
																busListObj.getId(), busListObj.getPrimaryDriver().getId());
												if (dailyRoasterList.size() > 0) {
													for (DailyRoaster autoObj : dailyRoasterList) {
														if (autoObj != null && autoObj.getRefueling() != null
																&& autoObj.getRefueling().getId() != null) {
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if (refuelingList.size() > 0) {
													maxCurrentKmplOnDriverBasis = refuellingRepository
															.fetchCurrentMaxKms(refuelingList);
													if (maxCurrentKmplOnBusBasis != null
															&& maxCurrentKmplOnBusBasis[0] != null) {
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														currentKmpl = Float.parseFloat((obj[0]).toString());
														driverCode = obj[1].toString();
													}
													if (maxCurrentKmplOnDriverBasis < currentKmpl) {
														dailyObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
														dailyOldObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

													}
												}
											}

										}

									}}catch(Exception e){
										e.printStackTrace();
									}
									if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
										driveMaster.setId(busListObj.getSecondaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catADriverList.indexOf(driveMaster);
											catADriverList.remove(driverIndex);
											} else if (catBDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catBDriverList.indexOf(driveMaster);
												catBDriverList.remove(driverIndex);
											}else if (catCDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catCDriverList.indexOf(driveMaster);
												catCDriverList.remove(driverIndex);
											}else if (catDDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catDDriverList.indexOf(driveMaster);
												catDDriverList.remove(driverIndex);
											}
									}
									catCBusList.remove(index);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									//catCBusList.remove(index);
									//m++;
									break;
							}
					}	
				}else if (catDBusList.size() > 0) {
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catDBusList) {
							if (tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())) {
								flag = true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catDBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDBusList.remove(index);
								//n++;
								break;
							}
						}
						if (!flag) {
							for (BusMaster busListObj : catDBusList) {
								   busMaster.setId(busListObj.getId());
								   helper.setBusMaster(busMaster);
									index = catDBusList.indexOf(busListObj);
									if(busListObj.getPrimaryDriver() != null){
										driveMaster.setId(busListObj.getPrimaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										}else if(catBDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catBDriverList.indexOf(driveMaster);
										catBDriverList.remove(driverIndex);
										}else if(catCDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catCDriverList.indexOf(driveMaster);
										catCDriverList.remove(driverIndex);
										}else if(catDDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									try{
									if (busListObj.getPrimaryDriver() != null && driverFlag) {
										maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
												tempCatAObj.getRouteMaster().getId(), busListObj.getId());
										if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
											if (tempCatAObj.getRouteMaster() != null
													&& tempCatAObj.getRouteMaster().getId() != null) {
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
														.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
																busListObj.getId(), busListObj.getPrimaryDriver().getId());
												if (dailyRoasterList.size() > 0) {
													for (DailyRoaster autoObj : dailyRoasterList) {
														if (autoObj != null && autoObj.getRefueling() != null
																&& autoObj.getRefueling().getId() != null) {
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if (refuelingList.size() > 0) {
													maxCurrentKmplOnDriverBasis = refuellingRepository
															.fetchCurrentMaxKms(refuelingList);
													if (maxCurrentKmplOnBusBasis != null
															&& maxCurrentKmplOnBusBasis[0] != null) {
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														currentKmpl = Float.parseFloat((obj[0]).toString());
														driverCode = obj[1].toString();
													}
													if (maxCurrentKmplOnDriverBasis < currentKmpl) {
														dailyObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
														dailyOldObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

													}
												}
											}

										}

									}}catch(Exception e){
										e.printStackTrace();
									}
									if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
										driveMaster.setId(busListObj.getSecondaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catADriverList.indexOf(driveMaster);
											catADriverList.remove(driverIndex);
											} else if (catBDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catBDriverList.indexOf(driveMaster);
												catBDriverList.remove(driverIndex);
											}else if (catCDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catCDriverList.indexOf(driveMaster);
												catCDriverList.remove(driverIndex);
											}else if (catDDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catDDriverList.indexOf(driveMaster);
												catDDriverList.remove(driverIndex);
											}
									}
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catDBusList.remove(index);
									//n++;
									break;
							}
						}
					
				}else{
					continue;
				}
				   DriverMaster dMaster = new DriverMaster();
				   int objIndex = 0; 
				   if(helper.getDriverMaster() == null){
					   boolean flag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxKmplForDriver(tempCatAObj.getRouteMaster().getId(), helper.getBusMaster().getId());
					   for(Object[] rotaObj :dailyRoasterList){
								String driverId = rotaObj[0].toString();
								dMaster.setId(Integer.parseInt(driverId));
								if(catADriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catADriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catADriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catBDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catBDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catBDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catCDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catCDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catCDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catDDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catDDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catDDriverList.remove(objIndex);
									flag = true;
									break;
								}
						}
					}
				    if(!flag && catBDriverList.size() > 0){
					   for (DriverMaster driverListObj : catBDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catBDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catBDriverList.remove(index);
								//q++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catBDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catBDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catBDriverList.remove(index);
									//q++;
									break;
							}
						}
				   }else if(!flag && catCDriverList.size() > 0){
					   for (DriverMaster driverListObj : catCDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catCDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catCDriverList.remove(index);
								//r++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catCDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catCDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catCDriverList.remove(index);
									//r++;
									break;
							}
						}
				   }else if(!flag && catDDriverList.size() > 0){
					   for (DriverMaster driverListObj : catDDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catDDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDDriverList.remove(index);
								//s++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catDDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catDDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catDDriverList.remove(index);
									//s++;
									break;
							}
						}
				   }/*else{
					   continue;
				   }*/
				 }
				   
				   ConductorMaster cMaster = new ConductorMaster();
				   int obIndex = 0; 
				   if(helper.getConductorMaster() == null){
					   boolean conductorFlag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxEPKMForConductor();
					   for(Object[] rotaObj :dailyRoasterList){
								String conductorId = rotaObj[0].toString();
								cMaster.setId(Integer.parseInt(conductorId));
								if(catAConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catAConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catAConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catBConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catBConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catBConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catCConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catCConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catCConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catDConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catDConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catDConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}
					         	
						}
					}
				   
				    if(!conductorFlag && catBConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catBConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catBConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catBConductorList.remove(conIndex);
								//k++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catBConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catBConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catBConductorList.remove(conIndex);
								//k++;
								break;
							}
						
						}
						
				   }else if(!conductorFlag && catCConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catCConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catCConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catCConductorList.remove(conIndex);
								//l++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catCConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catCConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catCConductorList.remove(conIndex);
								//l++;
								break;
							}
						
						}
						
				   }else if(!conductorFlag && catDConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catDConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catDConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catDConductorList.remove(conIndex);
								//y++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catDConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catDConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDConductorList.remove(conIndex);
								//y++;
								break;
							}
						
						}
						
				   }/*else{
					   continue;
				   }*/
				 }
				   
				   if((!sameAClassRouteMap.isEmpty()) && sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
						SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
						drh = new DriverRotaHistory();
						brh = new BusRotaHistory();
						crh = new ConductorRotaHistory();
						if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
							List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
							for(TripRotationEntity tempCatARotationObj : rotationsList){
							
								if(tempCatARotationObj != null && tempCatARotationObj.getDays() != null && tempCatARotationObj.getDays().getNightDetail() != null){
									dailyOldObj = new DailyRoaster();
									dailyObj = new DailyRoasterAuto();
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
								
								brh.setReturnTime(tempCatAObj.getTripEndTime());
							    drh.setReturnTime(tempCatAObj.getTripEndTime());
							    crh.setReturnTime(tempCatAObj.getTripEndTime());
							    
							dailyObj.setBus(helperObj.getBusMaster());
							dailyOldObj.setBus(helperObj.getBusMaster());
							dailyObj.setDriver(helperObj.getDriverMaster());
							dailyOldObj.setDriver(helperObj.getDriverMaster());
							dailyObj.setConductor(helperObj.getConductorMaster());
							dailyOldObj.setConductor(helperObj.getConductorMaster());
							dailyObj.setRotaAuto(roaster);
							dailyObj.setTripStatus(false);
							dailyObj.setRoute(tempCatAObj.getRouteMaster());
							dailyObj.setIsDeleted(false);
							dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyObj.setTrip(tempCatAObj);
							dailyObj.setRotaTypeFlag("Auto");
							drList.add(dailyObj);
							
							dailyOldObj.setRota(roasterObj);
							dailyOldObj.setTripStatus(false);
							dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
							dailyOldObj.setIsDeleted(false);
							dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyOldObj.setTrip(tempCatAObj);
							dailyOldObj.setTripRotation(tempCatARotationObj);
							brh.setBus(helperObj.getBusMaster());
							brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							brh.setIsDeleted(false);
							brh.setRoaster(dailyOldObj);
							brh.setTrip(tempCatAObj);
							brhList.add(brh);
							
							drh.setDriver(helperObj.getDriverMaster());
							drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							drh.setIsDeleted(false);
							drh.setRoaster(dailyOldObj);
							drh.setTrip(tempCatAObj);
							drhList.add(drh);
							
							crh.setConductor(helperObj.getConductorMaster());
							crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							crh.setIsDeleted(false);
							crh.setRoaster(dailyOldObj);
							crh.setTrip(tempCatAObj);
							crhList.add(crh);
							
							//dailyOldObj.setBusRota(brhList);
							//dailyOldObj.setDriverRota(drhList);
							//dailyOldObj.setConductorRota(crhList);
							dailyOldObj.setRotaTypeFlag("Auto");
							drListOld.add(dailyOldObj);
							helper.setTripEndTime(tempCatAObj.getTripEndTime());
							helper.setTripStartTime(tempCatAObj.getTripEndTime());
							sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							
							}
							continue;
						}
					}
				   
				   if(tempCatAObj.getRouteMaster() != null && (!(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())))){
					   helper.setTripStartTime(tempCatAObj.getTripStartTime());
						helper.setTripEndTime(tempCatAObj.getTripEndTime());
						sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
						}
				   
				List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
				if (rotationsList.size() == 0) {
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
				}
				for(TripRotationEntity tempCatARotationObj : rotationsList){
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					brh = new BusRotaHistory();
					drh = new DriverRotaHistory();
					crh = new ConductorRotaHistory();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
					
				
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
					
					brh.setReturnTime(tempCatAObj.getTripEndTime());
				    drh.setReturnTime(tempCatAObj.getTripEndTime());
				    crh.setReturnTime(tempCatAObj.getTripEndTime());
				
				dailyObj.setRotaAuto(roaster);
				dailyObj.setTripStatus(false);
				dailyObj.setRoute(tempCatAObj.getRouteMaster());
				dailyObj.setIsDeleted(false);
				dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyObj.setTrip(tempCatAObj);
				dailyObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyObj.setRotaTypeFlag("Auto");
				drList.add(dailyObj);

				dailyOldObj.setRota(roasterObj);
				dailyOldObj.setTripStatus(false);
				dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
				dailyOldObj.setIsDeleted(false);
				dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTrip(tempCatAObj);
				brh.setBus(helper.getBusMaster());
				brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				brh.setIsDeleted(false);
				brh.setRoaster(dailyOldObj);
				brh.setTrip(tempCatAObj);
				brhList.add(brh);
				
				drh.setDriver(helper.getDriverMaster());
				drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				drh.setIsDeleted(false);
				drh.setRoaster(dailyOldObj);
				drh.setTrip(tempCatAObj);
				drhList.add(drh);
				
				crh.setConductor(helper.getConductorMaster());
				crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				crh.setIsDeleted(false);
				crh.setRoaster(dailyOldObj);
				crh.setTrip(tempCatAObj);
				crhList.add(crh);
				
				//dailyOldObj.setBusRota(brhList);
				//dailyOldObj.setDriverRota(drhList);
				//dailyOldObj.setConductorRota(crhList);
				dailyOldObj.setRotaTypeFlag("Auto");
				drListOld.add(dailyOldObj);
				
				
				}
			}
			
			sameAClassRouteMap = new HashMap<>();
			for (TripMaster tempCatAObj : catCTripList) {
				dailyOldObj = new DailyRoaster();
				dailyObj = new DailyRoasterAuto();
				brh = new BusRotaHistory();
				drh = new DriverRotaHistory();
				crh = new ConductorRotaHistory();
				BusMaster busMaster = new BusMaster();
				 helper = new SameRoutesHelperForRota();
				 DriverMaster driveMaster = new DriverMaster();
				   if (catCBusList.size() > 0) {
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catCBusList) {
							if (tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime()!= null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())) {
								flag = true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catCBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									
								}
								catCBusList.remove(index);
								//m++;
								break;
							}
						}
						if (!flag) {
							for (BusMaster busListObj : catCBusList) {
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
									index = catCBusList.indexOf(busListObj);
									if(busListObj.getPrimaryDriver() != null){
										driveMaster.setId(busListObj.getPrimaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										}else if(catBDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catBDriverList.indexOf(driveMaster);
										catBDriverList.remove(driverIndex);
										}else if(catCDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catCDriverList.indexOf(driveMaster);
										catCDriverList.remove(driverIndex);
										}else if(catDDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									try{
									if (busListObj.getPrimaryDriver() != null && driverFlag) {
										maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
												tempCatAObj.getRouteMaster().getId(), busListObj.getId());
										if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
											if (tempCatAObj.getRouteMaster() != null
													&& tempCatAObj.getRouteMaster().getId() != null) {
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
														.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
																busListObj.getId(), busListObj.getPrimaryDriver().getId());
												if (dailyRoasterList.size() > 0) {
													for (DailyRoaster autoObj : dailyRoasterList) {
														if (autoObj != null && autoObj.getRefueling() != null
																&& autoObj.getRefueling().getId() != null) {
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if (refuelingList.size() > 0) {
													maxCurrentKmplOnDriverBasis = refuellingRepository
															.fetchCurrentMaxKms(refuelingList);
													if (maxCurrentKmplOnBusBasis != null
															&& maxCurrentKmplOnBusBasis[0] != null) {
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														currentKmpl = Float.parseFloat((obj[0]).toString());
														driverCode = obj[1].toString();
													}
													if (maxCurrentKmplOnDriverBasis < currentKmpl) {
														dailyObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
														dailyOldObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

													}
												}
											}

										}

									}}catch(Exception e){
										e.printStackTrace();
									}
									if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
										driveMaster.setId(busListObj.getSecondaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catADriverList.indexOf(driveMaster);
											catADriverList.remove(driverIndex);
											} else if (catBDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catBDriverList.indexOf(driveMaster);
												catBDriverList.remove(driverIndex);
											}else if (catCDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catCDriverList.indexOf(driveMaster);
												catCDriverList.remove(driverIndex);
											}else if (catDDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catDDriverList.indexOf(driveMaster);
												catDDriverList.remove(driverIndex);
											}
									}
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catCBusList.remove(index);
									//m++;
									break;
							}
					}	
				}else if (catDBusList.size() > 0) {
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catDBusList) {
							if (tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())) {
								flag = true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catDBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catADriverList.indexOf(driveMaster);
									catADriverList.remove(driverIndex);
									}else if(catBDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catBDriverList.indexOf(driveMaster);
									catBDriverList.remove(driverIndex);
									}else if(catCDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catCDriverList.indexOf(driveMaster);
									catCDriverList.remove(driverIndex);
									}else if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}}catch(Exception e){
									e.printStackTrace();
								}
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catADriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										} else if (catBDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catBDriverList.indexOf(driveMaster);
											catBDriverList.remove(driverIndex);
										}else if (catCDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catCDriverList.indexOf(driveMaster);
											catCDriverList.remove(driverIndex);
										}else if (catDDriverList.contains(driveMaster)) {
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catDDriverList.indexOf(driveMaster);
											catDDriverList.remove(driverIndex);
										}
								}
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDBusList.remove(index);
								//n++;
								break;
							}
						}
						if (!flag) {
							for (BusMaster busListObj : catDBusList) {
								   busMaster.setId(busListObj.getId());
								   helper.setBusMaster(busMaster);
									index = catDBusList.indexOf(busListObj);
									if(busListObj.getPrimaryDriver() != null){
										driveMaster.setId(busListObj.getPrimaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catADriverList.indexOf(driveMaster);
										catADriverList.remove(driverIndex);
										}else if(catBDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catBDriverList.indexOf(driveMaster);
										catBDriverList.remove(driverIndex);
										}else if(catCDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catCDriverList.indexOf(driveMaster);
										catCDriverList.remove(driverIndex);
										}else if(catDDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									try{
									if (busListObj.getPrimaryDriver() != null && driverFlag) {
										maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
												tempCatAObj.getRouteMaster().getId(), busListObj.getId());
										if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
											if (tempCatAObj.getRouteMaster() != null
													&& tempCatAObj.getRouteMaster().getId() != null) {
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
														.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
																busListObj.getId(), busListObj.getPrimaryDriver().getId());
												if (dailyRoasterList.size() > 0) {
													for (DailyRoaster autoObj : dailyRoasterList) {
														if (autoObj != null && autoObj.getRefueling() != null
																&& autoObj.getRefueling().getId() != null) {
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if (refuelingList.size() > 0) {
													maxCurrentKmplOnDriverBasis = refuellingRepository
															.fetchCurrentMaxKms(refuelingList);
													if (maxCurrentKmplOnBusBasis != null
															&& maxCurrentKmplOnBusBasis[0] != null) {
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														currentKmpl = Float.parseFloat((obj[0]).toString());
														driverCode = obj[1].toString();
													}
													if (maxCurrentKmplOnDriverBasis < currentKmpl) {
														dailyObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
														dailyOldObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

													}
												}
											}

										}

									}}catch(Exception e){
										e.printStackTrace();
									}
									if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
										driveMaster.setId(busListObj.getSecondaryDriver().getId());
										if(catADriverList.contains(driveMaster)){
											helper.setDriverMaster(busListObj.getSecondaryDriver());
											driverIndex = catADriverList.indexOf(driveMaster);
											catADriverList.remove(driverIndex);
											} else if (catBDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catBDriverList.indexOf(driveMaster);
												catBDriverList.remove(driverIndex);
											}else if (catCDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catCDriverList.indexOf(driveMaster);
												catCDriverList.remove(driverIndex);
											}else if (catDDriverList.contains(driveMaster)) {
												helper.setDriverMaster(busListObj.getSecondaryDriver());
												driverIndex = catDDriverList.indexOf(driveMaster);
												catDDriverList.remove(driverIndex);
											}
									}
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catDBusList.remove(index);
									//n++;
									break;
							}
						}
					
				}else{
					continue;
				}
				   DriverMaster dMaster = new DriverMaster();
				   int objIndex = 0; 
				   if(helper.getDriverMaster() == null){
					   boolean flag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxKmplForDriver(tempCatAObj.getRouteMaster().getId(), helper.getBusMaster().getId());
					   for(Object[] rotaObj :dailyRoasterList){
								String driverId = rotaObj[0].toString();
								dMaster.setId(Integer.parseInt(driverId));
								if(catADriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catADriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catADriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catBDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catBDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catBDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catCDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catCDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catCDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catDDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catDDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catDDriverList.remove(objIndex);
									flag = true;
									break;
								}
						}
					}
				    if(!flag && catCDriverList.size() > 0){
					   for (DriverMaster driverListObj : catCDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catCDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catCDriverList.remove(index);
								//r++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catCDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catCDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catCDriverList.remove(index);
									//r++;
									break;
							}
						}
				   }else if(!flag && catDDriverList.size() > 0){
					   for (DriverMaster driverListObj : catDDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catDDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDDriverList.remove(index);
								//s++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catDDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catDDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catDDriverList.remove(index);
									//s++;
									break;
							}
						}
				   }/*else{
					   continue;
				   }*/
				 }
				   
				   ConductorMaster cMaster = new ConductorMaster();
				   int obIndex = 0; 
				   if(helper.getConductorMaster() == null){
					   boolean conductorFlag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxEPKMForConductor();
					   for(Object[] rotaObj :dailyRoasterList){
								String conductorId = rotaObj[0].toString();
								cMaster.setId(Integer.parseInt(conductorId));
								if(catAConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catAConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catAConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catBConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catBConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catBConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catCConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catCConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catCConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catDConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catDConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catDConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}
					         	
						}
					}
				   
				     if(!conductorFlag && catCConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catCConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catCConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catCConductorList.remove(conIndex);
								//l++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catCConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catCConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catCConductorList.remove(conIndex);
								//l++;
								break;
							}
						
						}
						
				   }else if(!conductorFlag && catDConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catDConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catDConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catDConductorList.remove(conIndex);
								//y++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catDConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catDConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDConductorList.remove(conIndex);
								//y++;
								break;
							}
						
						}
						
				   }/*else{
					   continue;
				   }*/
				   }
				   if((!sameAClassRouteMap.isEmpty()) && sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
						SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
						drh = new DriverRotaHistory();
						brh = new BusRotaHistory();
						crh = new ConductorRotaHistory();
						if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
							List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
							for(TripRotationEntity tempCatARotationObj : rotationsList){
								dailyOldObj = new DailyRoaster();
								dailyObj = new DailyRoasterAuto();
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
								
								brh.setReturnTime(tempCatAObj.getTripEndTime());
							    drh.setReturnTime(tempCatAObj.getTripEndTime());
							    crh.setReturnTime(tempCatAObj.getTripEndTime());
							    
							dailyObj.setBus(helperObj.getBusMaster());
							dailyOldObj.setBus(helperObj.getBusMaster());
							dailyObj.setDriver(helperObj.getDriverMaster());
							dailyOldObj.setDriver(helperObj.getDriverMaster());
							dailyObj.setConductor(helperObj.getConductorMaster());
							dailyOldObj.setConductor(helperObj.getConductorMaster());
							dailyObj.setRotaAuto(roaster);
							dailyObj.setTripStatus(false);
							dailyObj.setRoute(tempCatAObj.getRouteMaster());
							dailyObj.setIsDeleted(false);
							dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyObj.setTrip(tempCatAObj);
							dailyObj.setRotaTypeFlag("Auto");
							drList.add(dailyObj);
							
							dailyOldObj.setRota(roasterObj);
							dailyOldObj.setTripStatus(false);
							dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
							dailyOldObj.setIsDeleted(false);
							dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyOldObj.setTrip(tempCatAObj);
							dailyOldObj.setTripRotation(tempCatARotationObj);
							brh.setBus(helperObj.getBusMaster());
							brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							brh.setIsDeleted(false);
							brh.setRoaster(dailyOldObj);
							brh.setTrip(tempCatAObj);
							brhList.add(brh);
							
							drh.setDriver(helperObj.getDriverMaster());
							drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							drh.setIsDeleted(false);
							drh.setRoaster(dailyOldObj);
							drh.setTrip(tempCatAObj);
							drhList.add(drh);
							
							crh.setConductor(helperObj.getConductorMaster());
							crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							crh.setIsDeleted(false);
							crh.setRoaster(dailyOldObj);
							crh.setTrip(tempCatAObj);
							crhList.add(crh);
							
							//dailyOldObj.setBusRota(brhList);
							//dailyOldObj.setDriverRota(drhList);
							//dailyOldObj.setConductorRota(crhList);
							dailyOldObj.setRotaTypeFlag("Auto");
							drListOld.add(dailyOldObj);
							helper.setTripEndTime(tempCatAObj.getTripEndTime());
							helper.setTripStartTime(tempCatAObj.getTripEndTime());
							sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							
						}
							continue;
					  }		
					}
				   
				   if(tempCatAObj.getRouteMaster() != null && (!(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())))){
					   helper.setTripStartTime(tempCatAObj.getTripStartTime());
						helper.setTripEndTime(tempCatAObj.getTripEndTime());
						sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
						}
				   
				List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
				if (rotationsList.size() == 0) {
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					brh = new BusRotaHistory();
					drh = new DriverRotaHistory();
					crh = new ConductorRotaHistory();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
				}
				for(TripRotationEntity tempCatARotationObj : rotationsList){
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					brh = new BusRotaHistory();
					drh = new DriverRotaHistory();
					crh = new ConductorRotaHistory();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
					
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
					
					brh.setReturnTime(tempCatAObj.getTripEndTime());
				    drh.setReturnTime(tempCatAObj.getTripEndTime());
				    crh.setReturnTime(tempCatAObj.getTripEndTime());
				
				dailyObj.setRotaAuto(roaster);
				dailyObj.setTripStatus(false);
				dailyObj.setRoute(tempCatAObj.getRouteMaster());
				dailyObj.setIsDeleted(false);
				dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyObj.setTrip(tempCatAObj);
				dailyObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyObj.setRotaTypeFlag("Auto");
				drList.add(dailyObj);

				dailyOldObj.setRota(roasterObj);
				dailyOldObj.setTripStatus(false);
				dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
				dailyOldObj.setIsDeleted(false);
				dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTrip(tempCatAObj);
				brh.setBus(helper.getBusMaster());
				brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				brh.setIsDeleted(false);
				brh.setRoaster(dailyOldObj);
				brh.setTrip(tempCatAObj);
				brhList.add(brh);
				
				drh.setDriver(helper.getDriverMaster());
				drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				drh.setIsDeleted(false);
				drh.setRoaster(dailyOldObj);
				drh.setTrip(tempCatAObj);
				drhList.add(drh);
				
				crh.setConductor(helper.getConductorMaster());
				crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				crh.setIsDeleted(false);
				crh.setRoaster(dailyOldObj);
				crh.setTrip(tempCatAObj);
				crhList.add(crh);
				
				//dailyOldObj.setBusRota(brhList);
				//dailyOldObj.setDriverRota(drhList);
				//dailyOldObj.setConductorRota(crhList);
				dailyOldObj.setRotaTypeFlag("Auto");
				drListOld.add(dailyOldObj);
				
				}
			}
			
			sameAClassRouteMap = new HashMap<>();
			for (TripMaster tempCatAObj : catDTripList) {
				dailyOldObj = new DailyRoaster();
				dailyObj = new DailyRoasterAuto();
				BusMaster busMaster = new BusMaster();
				DriverMaster driveMaster = new DriverMaster();
				 helper = new SameRoutesHelperForRota();
				    if (catDBusList.size() > 0) {
						boolean flag = false;
						boolean driverFlag = false;
						for (BusMaster busListObj : catDBusList) {
							if (tempCatAObj.getTripEndTime() != null && busListObj.getBusAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(busListObj.getBusAvailableTime())) {
								flag = true;
								busMaster.setId(busListObj.getId());
								helper.setBusMaster(busMaster);
								index = catDBusList.indexOf(busListObj);
								if(busListObj.getPrimaryDriver() != null){
									driveMaster.setId(busListObj.getPrimaryDriver().getId());
									if(catDDriverList.contains(driveMaster)){
										driverFlag = true;
									helper.setDriverMaster(busListObj.getPrimaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								try{
								if (busListObj.getPrimaryDriver() != null && driverFlag) {
									maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
											tempCatAObj.getRouteMaster().getId(), busListObj.getId());
									if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
										if (tempCatAObj.getRouteMaster() != null
												&& tempCatAObj.getRouteMaster().getId() != null) {
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
													.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
															busListObj.getId(), busListObj.getPrimaryDriver().getId());
											if (dailyRoasterList.size() > 0) {
												for (DailyRoaster autoObj : dailyRoasterList) {
													if (autoObj != null && autoObj.getRefueling() != null
															&& autoObj.getRefueling().getId() != null) {
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if (refuelingList.size() > 0) {
												maxCurrentKmplOnDriverBasis = refuellingRepository
														.fetchCurrentMaxKms(refuelingList);
												if (maxCurrentKmplOnBusBasis != null
														&& maxCurrentKmplOnBusBasis[0] != null) {
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													currentKmpl = Float.parseFloat((obj[0]).toString());
													driverCode = obj[1].toString();
												}
												if (maxCurrentKmplOnDriverBasis < currentKmpl) {
													dailyObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
													dailyOldObj.setSuggestion(
															"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

												}
											}
										}

									}

								}
							}catch(Exception e){
								e.printStackTrace();
							}
								if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
									driveMaster.setId(busListObj.getSecondaryDriver().getId());
									if(catDDriverList.contains(driveMaster)){
									helper.setDriverMaster(busListObj.getSecondaryDriver());
									driverIndex = catDDriverList.indexOf(driveMaster);
									catDDriverList.remove(driverIndex);
									}
								}
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDBusList.remove(index);
								//n++;
								break;
							}
						}
						if (!flag) {
							for (BusMaster busListObj : catDBusList) {
								   busMaster.setId(busListObj.getId());
								   helper.setBusMaster(busMaster);
									index = catDBusList.indexOf(busListObj);
									if(busListObj.getPrimaryDriver() != null){
										driveMaster.setId(busListObj.getPrimaryDriver().getId());
										if(catDDriverList.contains(driveMaster)){
											driverFlag = true;
										helper.setDriverMaster(busListObj.getPrimaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									try{
									if (busListObj.getPrimaryDriver() != null && driverFlag) {
										maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(
												tempCatAObj.getRouteMaster().getId(), busListObj.getId());
										if (maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis.length > 0) {
											if (tempCatAObj.getRouteMaster() != null
													&& tempCatAObj.getRouteMaster().getId() != null) {
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository
														.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),
																busListObj.getId(), busListObj.getPrimaryDriver().getId());
												if (dailyRoasterList.size() > 0) {
													for (DailyRoaster autoObj : dailyRoasterList) {
														if (autoObj != null && autoObj.getRefueling() != null
																&& autoObj.getRefueling().getId() != null) {
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if (refuelingList.size() > 0) {
													maxCurrentKmplOnDriverBasis = refuellingRepository
															.fetchCurrentMaxKms(refuelingList);
													if (maxCurrentKmplOnBusBasis != null
															&& maxCurrentKmplOnBusBasis[0] != null) {
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														currentKmpl = Float.parseFloat((obj[0]).toString());
														driverCode = obj[1].toString();
													}
													if (maxCurrentKmplOnDriverBasis < currentKmpl) {
														dailyObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl :" + currentKmpl);
														dailyOldObj.setSuggestion(
																"This driver no : " + driverCode + " having better kmpl:" + currentKmpl);

													}
												}
											}

										}
									 }			
									}catch(Exception e){
										e.printStackTrace();
									}
									if(!(driverFlag) && busListObj.getSecondaryDriver() != null){
										driveMaster.setId(busListObj.getSecondaryDriver().getId());
										if(catDDriverList.contains(driveMaster)){
										helper.setDriverMaster(busListObj.getSecondaryDriver());
										driverIndex = catDDriverList.indexOf(driveMaster);
										catDDriverList.remove(driverIndex);
										}
									}
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catDBusList.remove(index);
									//n++;
									break;
							}
						}
					
				}else{
					continue;
				}
				   DriverMaster dMaster = new DriverMaster();
				   int objIndex = 0; 
				   if(helper.getDriverMaster() == null){
					   boolean flag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxKmplForDriver(tempCatAObj.getRouteMaster().getId(), helper.getBusMaster().getId());
					   for(Object[] rotaObj :dailyRoasterList){
								String driverId = rotaObj[0].toString();
								dMaster.setId(Integer.parseInt(driverId));
								if(catADriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catADriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catADriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catBDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catBDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catBDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catCDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catCDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catCDriverList.remove(objIndex);
									flag = true;
									break;
								}else if(catDDriverList.contains(dMaster) && rotaObj[1] != null){
									objIndex = catDDriverList.indexOf(dMaster);
									helper.setDriverMaster(dMaster);
									catDDriverList.remove(objIndex);
									flag = true;
									break;
								}
						}
					}
				    if(!flag && catDDriverList.size() > 0){
					   for (DriverMaster driverListObj : catDDriverList) {
							if (tempCatAObj.getTripEndTime() != null && driverListObj.getDriverAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(driverListObj.getDriverAvailableTime())) {
								flag = true;
								helper.setDriverMaster(driverListObj);
								index = catDDriverList.indexOf(driverListObj);
								if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDDriverList.remove(index);
								//s++;
								break;
							}
						}
						if (!flag) {
							for (DriverMaster driverListObj : catDDriverList) {
								    helper.setDriverMaster(driverListObj);
								    index = catDDriverList.indexOf(driverListObj);
									if (tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()) {
										helper.setTripStartTime(tempCatAObj.getTripStartTime());
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									catADriverList.remove(index);
									//s++;
									break;
							}
						}
				   }/*else{
					   continue;
				   }*/
				 }
				   
				   ConductorMaster cMaster = new ConductorMaster();
				   int obIndex = 0; 
				   if(helper.getConductorMaster() == null){
					   boolean conductorFlag = false;
					   if(helper.getBusMaster() != null && helper.getBusMaster().getId() != null){
					   List<Object[]> dailyRoasterList = dailyRoasterRepository.findMaxEPKMForConductor();
					   for(Object[] rotaObj :dailyRoasterList){
								String conductorId = rotaObj[0].toString();
								cMaster.setId(Integer.parseInt(conductorId));
								if(catAConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catAConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catAConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catBConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catBConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catBConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catCConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catCConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catCConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}else if(catDConductorList.contains(cMaster) && rotaObj[1] != null){
									obIndex = catDConductorList.indexOf(cMaster);
									helper.setConductorMaster(cMaster);
									catDConductorList.remove(obIndex);
									conductorFlag = true;
									break;
								}
					         	
						}
					}
				   
				      if(!conductorFlag && catDConductorList.size() > 0){
					   boolean flag = false;
					   int conIndex = 0;
						for (ConductorMaster conductorMasterObj : catDConductorList) {
							if(tempCatAObj.getTripEndTime() != null && conductorMasterObj.getConductorAvailableTime() != null && tempCatAObj.getTripEndTime().isBefore(conductorMasterObj.getConductorAvailableTime())){
								flag= true;
								conIndex = catDConductorList.indexOf(conductorMasterObj);
								helper.setConductorMaster(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							 }
								catDConductorList.remove(conIndex);
								//y++;
								break;	
							}
						}
						if(!flag){
							for (ConductorMaster conductorMasterObj : catDConductorList) {
							    helper.setConductorMaster(conductorMasterObj);
								index = catDConductorList.indexOf(conductorMasterObj);
								if(tempCatAObj.getRouteMaster() != null && sameAClassRouteMap.isEmpty()){
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								catDConductorList.remove(conIndex);
								//y++;
								break;
							}
						
						}
						
				   }/*else{
					   continue;
				   }*/
				   }
				   
				   if((!sameAClassRouteMap.isEmpty()) && sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
						SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
						drh = new DriverRotaHistory();
						brh = new BusRotaHistory();
						crh = new ConductorRotaHistory();
						if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
							List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
							for(TripRotationEntity tempCatARotationObj : rotationsList){
								dailyOldObj = new DailyRoaster();
								dailyObj = new DailyRoasterAuto();
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
								
								brh.setReturnTime(tempCatAObj.getTripEndTime());
							    drh.setReturnTime(tempCatAObj.getTripEndTime());
							    crh.setReturnTime(tempCatAObj.getTripEndTime());
							dailyObj.setBus(helperObj.getBusMaster());
							dailyOldObj.setBus(helperObj.getBusMaster());
							dailyObj.setDriver(helperObj.getDriverMaster());
							dailyOldObj.setDriver(helperObj.getDriverMaster());
							dailyObj.setConductor(helperObj.getConductorMaster());
							dailyOldObj.setConductor(helperObj.getConductorMaster());
							dailyObj.setRotaAuto(roaster);
							dailyObj.setTripStatus(false);
							dailyObj.setRoute(tempCatAObj.getRouteMaster());
							dailyObj.setIsDeleted(false);
							dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyObj.setTrip(tempCatAObj);
							dailyObj.setRotaTypeFlag("Auto");
							drList.add(dailyObj);
							
							dailyOldObj.setRota(roasterObj);
							dailyOldObj.setTripStatus(false);
							dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
							dailyOldObj.setIsDeleted(false);
							dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
							dailyOldObj.setTrip(tempCatAObj);
							dailyOldObj.setTripRotation(tempCatARotationObj);
							
							brh.setBus(helperObj.getBusMaster());
							brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							brh.setIsDeleted(false);
							brh.setRoaster(dailyOldObj);
							brh.setTrip(tempCatAObj);
							brhList.add(brh);
							
							drh.setDriver(helperObj.getDriverMaster());
							drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							drh.setIsDeleted(false);
							drh.setRoaster(dailyOldObj);
							drh.setTrip(tempCatAObj);
							drhList.add(drh);
							
							crh.setConductor(helperObj.getConductorMaster());
							crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
							crh.setIsDeleted(false);
							crh.setRoaster(dailyOldObj);
							crh.setTrip(tempCatAObj);
							crhList.add(crh);
							
							//dailyOldObj.setBusRota(brhList);
							//dailyOldObj.setDriverRota(drhList);
							//dailyOldObj.setConductorRota(crhList);
							dailyOldObj.setRotaTypeFlag("Auto");
							drListOld.add(dailyOldObj);
							helper.setTripEndTime(tempCatAObj.getTripEndTime());
							helper.setTripStartTime(tempCatAObj.getTripEndTime());
							sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							
						  }
							continue;
						}
					}
				   
				   if(tempCatAObj.getRouteMaster() != null && (!(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())))){
					   helper.setTripStartTime(tempCatAObj.getTripStartTime());
						helper.setTripEndTime(tempCatAObj.getTripEndTime());
						sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
						}
				   
				List<TripRotationEntity> rotationsList = tripRotationReposiotry.getReocrdByTripId(tempCatAObj.getId());
				if (rotationsList.size() == 0) {
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
				}
				for(TripRotationEntity tempCatARotationObj : rotationsList){
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new DailyRoaster();
					if (helper.getBusMaster() != null) {
						dailyObj.setBus(helper.getBusMaster());
						dailyOldObj.setBus(helper.getBusMaster());
					}
					if (helper.getDriverMaster() != null) {
						dailyObj.setDriver(helper.getDriverMaster());
						dailyOldObj.setDriver(helper.getDriverMaster());
					}
					if (helper.getConductorMaster() != null) {
						dailyObj.setConductor(helper.getConductorMaster());
						dailyOldObj.setConductor(helper.getConductorMaster());
					}
					
				
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
					
					brh.setReturnTime(tempCatAObj.getTripEndTime());
				    drh.setReturnTime(tempCatAObj.getTripEndTime());
				    crh.setReturnTime(tempCatAObj.getTripEndTime());
				
				dailyObj.setRotaAuto(roaster);
				dailyObj.setTripStatus(false);
				dailyObj.setRoute(tempCatAObj.getRouteMaster());
				dailyObj.setIsDeleted(false);
				dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyObj.setTrip(tempCatAObj);
				dailyObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyObj.setRotaTypeFlag("Auto");
				drList.add(dailyObj);

				dailyOldObj.setRota(roasterObj);
				dailyOldObj.setTripStatus(false);
				dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
				dailyOldObj.setIsDeleted(false);
				dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
				dailyOldObj.setTripRotation(tempCatARotationObj);
				dailyOldObj.setTrip(tempCatAObj);
				
				brh.setBus(helper.getBusMaster());
				brh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				brh.setIsDeleted(false);
				brh.setRoaster(dailyOldObj);
				brh.setTrip(tempCatAObj);
				brhList.add(brh);
				
				drh.setDriver(helper.getDriverMaster());
				drh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				drh.setIsDeleted(false);
				drh.setRoaster(dailyOldObj);
				drh.setTrip(tempCatAObj);
				drhList.add(drh);
				
				crh.setConductor(helper.getConductorMaster());
				crh.setDepot(tempCatAObj.getRouteMaster().getDepotMaster());
				crh.setIsDeleted(false);
				crh.setRoaster(dailyOldObj);
				crh.setTrip(tempCatAObj);
				crhList.add(crh);
				
				//dailyOldObj.setBusRota(brhList);
				//dailyOldObj.setDriverRota(drhList);
				//dailyOldObj.setConductorRota(crhList);
				dailyOldObj.setRotaTypeFlag("Auto");
				drListOld.add(dailyOldObj);
				
				
				}
			}
			
			List<DailyRoaster> dailyList = authorizeRouteData(depot.getId(),roasterObj);
			if(dailyList.size() > 0 )
			drListOld.addAll(dailyList);
			MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Pending");
			if (masterStatus != null && masterStatus.getId() != null) {
				roasterObj.setMasterStatus(masterStatus);
				roaster.setMasterStatus(masterStatus);
			}
			roaster.setDailyRoasterList(drList);
			roasterObj.setDailyRoasterList(drListOld);
			for(DailyRoaster dailyRoaster : roasterObj.getDailyRoasterList()){
				if(dailyRoaster.getDriver() != null)
				driverRestMap.put(dailyRoaster.getDriver().getId(), "");
				if(dailyRoaster.getConductor() != null)
				conductorRestMap.put(dailyRoaster.getConductor().getId(), "");
			}
			Integer counter = 0;
			if(drList.size() > 0){
				for (Map.Entry<Integer,String> entry : driverRestMap.entrySet()){
					RestAlloactionDriverConductor driverObj = restAllocationRepository.findbyDriverId(entry.getKey());
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
						DriverMaster driverMaster = driverRepo.findById(entry.getKey()).get();
						restObj.setDriverMaster(driverMaster);
						restObj.setCounter(1);
						restObj.setRestCount(0);
						restAllocationRepository.save(restObj);
					}
		    }
				
				for (Map.Entry<Integer,String> entry : conductorRestMap.entrySet()){
					RestAlloactionDriverConductor conductorObj = restAllocationRepository.findbyConductorId(entry.getKey());
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
						ConductorMaster conductorMaster = conductorRepo.findById(entry.getKey()).get();
						restObj.setConductorMaster(conductorMaster);
						restObj.setCounter(1);
						restObj.setRestCount(0);
						restAllocationRepository.save(restObj);
					}
		    }
				
				roaster = roasterRepo.save(roaster);
				roasterObj = roasterOldRepo.save(roasterObj);
			return new ResponseEntity<>(
					new ResponseStatus("Roster successfully generated for " + roaster.getRotaDate() + " .",
							HttpStatus.OK),
					HttpStatus.OK);
			}
			//}
			else{
			return new ResponseEntity<>(
			new ResponseStatus("Roster could'nt generated, as trips are not available for "+roaster.getRotaDate() + " .", HttpStatus.FORBIDDEN),HttpStatus.OK);
		 }
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);

		}
		//return null;

	}
	
	
	private List<DailyRoaster> authorizeRouteData(Integer depotId,Roaster roasterObj) {
		List<DailyRoaster> drListOld = new ArrayList<>();
		DailyRoaster dailyOldObj = null;
		List<TripRotationEntity> rotationsList = new ArrayList<>();
		try {
			List<AuthorizeRoute> authRouteList = authorizeRouteRepository.findAllAuthorizeRoutesByDepot(depotId);
			for (AuthorizeRoute authObj : authRouteList) {
				//List<TripMaster> tripList = tripRepo.findByRouteId(authObj.getRoute().getId());
				//for (TripMaster tripObj : authObj.getTrip()) {
					DailyRoaster dailyObj = dailyRoasterRepository.findDailyByBusId(authObj.getBus().getId());
					if (dailyObj != null && dailyObj.getRotationAvailabilityDate().after(roasterObj.getRotaDate())) {
						continue;
					}
					if (dailyObj != null && dailyObj.getRotationAvailabilityDate().equals(roasterObj.getRotaDate())) {
						if (authObj != null && dailyObj.getTripRotation().getEndTime().isBefore(authObj.getTrip().getTripStartTime())) {
							rotationsList = tripRotationReposiotry.getReocrdByTripId(authObj.getTrip().getId());
							for (TripRotationEntity tempCatARotationObj : rotationsList) {
								dailyOldObj = new DailyRoaster();
								if (tempCatARotationObj != null && tempCatARotationObj.getDays() != null
										&& tempCatARotationObj.getDays().getNightDetail() != null) {
									if (tempCatARotationObj.getDays().getNightDetail().equals("N + 0")) {
										dailyOldObj.setRotationAvailabilityDate(this.getDate(1, new Date()));
									} else if (tempCatARotationObj.getDays().getNightDetail().equals("N + 1")) {
										dailyOldObj.setRotationAvailabilityDate(this.getDate(2, new Date()));
									} else if (tempCatARotationObj.getDays().getNightDetail().equals("N + 2")) {
										dailyOldObj.setRotationAvailabilityDate(this.getDate(3, new Date()));
									} else if (tempCatARotationObj.getDays().getNightDetail().equals("N + 3")) {
										dailyOldObj.setRotationAvailabilityDate(this.getDate(4, new Date()));
									}
								}

								dailyOldObj.setTripRotation(tempCatARotationObj);
								dailyOldObj.setBus(authObj.getBus());
								dailyOldObj.setDriver(authObj.getDriver());
								dailyOldObj.setConductor(authObj.getConductor());
								dailyOldObj.setRota(roasterObj);
								dailyOldObj.setTripStatus(false);
								dailyOldObj.setRoute(authObj.getRoute());
								dailyOldObj.setIsDeleted(false);
								dailyOldObj.setRouteType(authObj.getRoute().getRouteTypeMaster());
								dailyOldObj.setTrip(tempCatARotationObj.getTripMaster());
								dailyOldObj.setRotaTypeFlag("Auto");
								drListOld.add(dailyOldObj);
							}
						}
					} else if (dailyObj != null && dailyObj.getRotationAvailabilityDate().before(roasterObj.getRotaDate())) {
						rotationsList = tripRotationReposiotry.getReocrdByTripId(authObj.getTrip().getId());
						for (TripRotationEntity tempCatARotationObj : rotationsList) {
							dailyOldObj = new DailyRoaster();
							if (tempCatARotationObj != null && tempCatARotationObj.getDays() != null
									&& tempCatARotationObj.getDays().getNightDetail() != null) {
								if (tempCatARotationObj.getDays().getNightDetail().equals("N + 0")) {
									dailyOldObj.setRotationAvailabilityDate(this.getDate(1, new Date()));
								} else if (tempCatARotationObj.getDays().getNightDetail().equals("N + 1")) {
									dailyOldObj.setRotationAvailabilityDate(this.getDate(2, new Date()));
								} else if (tempCatARotationObj.getDays().getNightDetail().equals("N + 2")) {
									dailyOldObj.setRotationAvailabilityDate(this.getDate(3, new Date()));
								} else if (tempCatARotationObj.getDays().getNightDetail().equals("N + 3")) {
									dailyOldObj.setRotationAvailabilityDate(this.getDate(4, new Date()));
								}
							}

							dailyOldObj.setTripRotation(tempCatARotationObj);
							dailyOldObj.setBus(authObj.getBus());
							dailyOldObj.setDriver(authObj.getDriver());
							dailyOldObj.setConductor(authObj.getConductor());
							dailyOldObj.setRota(roasterObj);
							dailyOldObj.setTripStatus(false);
							dailyOldObj.setRoute(authObj.getRoute());
							dailyOldObj.setIsDeleted(false);
							dailyOldObj.setRouteType(authObj.getRoute().getRouteTypeMaster());
							dailyOldObj.setTrip(tempCatARotationObj.getTripMaster());
							dailyOldObj.setRotaTypeFlag("Auto");
							drListOld.add(dailyOldObj);
						}
					}
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drListOld;
	}
	
	/*@Override
	public ResponseEntity<ResponseStatus> generateAutoRoaster(String dpCode, Integer tpId, Date date) {
		List<TripMaster> catATripList = new ArrayList<>();
		List<TripMaster> catBTripList = new ArrayList<>();
		List<TripMaster> catCTripList = new ArrayList<>();
		List<TripMaster> catDTripList = new ArrayList<>();
		
		List<BusMaster> catABusList = new ArrayList<>();
		List<BusMaster> catBBusList = new ArrayList<>();
		List<BusMaster> catCBusList = new ArrayList<>();
		List<BusMaster> catDBusList = new ArrayList<>();
		
		List<DriverMaster> catADriverList = new ArrayList<>();
		List<DriverMaster> catBDriverList = new ArrayList<>();
		List<DriverMaster> catCDriverList = new ArrayList<>();
		List<DriverMaster> catDDriverList = new ArrayList<>();
		
		List<ConductorMaster> catAConductorList = new ArrayList<>();
		List<ConductorMaster> catBConductorList = new ArrayList<>();
		List<ConductorMaster> catCConductorList = new ArrayList<>();
		List<ConductorMaster> catDConductorList = new ArrayList<>();
		
		Map<Integer,String> availableDriverMap = new HashMap<>();
		Map<Integer,SameRoutesHelperForRota> sameAClassRouteMap = new HashMap<>();
		Map<Integer,SameRoutesHelperForRota> sameBClassRouteMap = new HashMap<>();
		Map<Integer,SameRoutesHelperForRota> sameCClassRouteMap = new HashMap<>();
		Map<Integer,SameRoutesHelperForRota> sameDClassRouteMap = new HashMap<>();
		
		DepotMaster depot = depotRepo.findByDepotCode(dpCode);
		RoasterAuto roasterTemp = roasterRepo.findByDate(depot.getId(), tpId);
		List<TripMaster> tripList = tripRepo.findAllTripsByAvailableRoutes(depot.getId(),tpId);
		
		for(TripMaster tripObj : tripList){
			if(tripObj!= null && tripObj.getRouteMaster() != null && tripObj.getRouteMaster().getRouteCategoryMaster() != null){
				if((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("A")){
					catATripList.add(tripObj);
				}else if((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("B")){
					catBTripList.add(tripObj);
				}else if((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("C")){
					catCTripList.add(tripObj);
				}else if((tripObj.getRouteMaster().getRouteCategoryMaster().getRouteCategoryName()).equals("D")){
					catDTripList.add(tripObj);
				}
				
			}
		}
		Date rotaDate = this.getDate(1, new Date());
		List<Object[]> driverList = driverRepo.getAvailableDrivers(depot.getId(), tpId, rotaDate);
		for(Object[] driverObj : driverList){
			DriverMaster driver = new DriverMaster();
			if(driverObj[0] != null){
			driver.setId(Integer.parseInt((driverObj[0]).toString()));
			availableDriverMap.put(Integer.parseInt((driverObj[0]).toString()), "");
			}
			if(driverObj[4] != null){
				if(driverObj[4].toString().equals("A")){
					catADriverList.add(driver);
				}else if(driverObj[4].toString().equals("B")){
					catBDriverList.add(driver);
				}else if(driverObj[4].toString().equals("C")){
					catCDriverList.add(driver);
				}else if(driverObj[4].toString().equals("D")){
					catDDriverList.add(driver);
				}
			}
		}
		
		List<Object[]> conductorList = conductorRepo.getAvailableConductors(depot.getId(),tpId, rotaDate);
		for(Object[] conductorObj : conductorList){
			ConductorMaster conductor = new ConductorMaster();
			if(conductorObj[0] != null)
				conductor.setId(Integer.parseInt((conductorObj[0]).toString()));
			if(conductorObj[4] != null){
				if(conductorObj[4].toString().equals("A")){
					catAConductorList.add(conductor);
				}else if(conductorObj[4].toString().equals("B")){
					catBConductorList.add(conductor);
				}else if(conductorObj[4].toString().equals("C")){
					catCConductorList.add(conductor);
				}else if(conductorObj[4].toString().equals("D")){
					catDConductorList.add(conductor);
				}
			}
		}
		
		List<Object[]> busList = busRepo.getAvailableBusesForRota(depot.getId(), tpId, rotaDate);
		for(Object[] busObj : busList){
			BusMaster busMaster = new BusMaster();
			DriverMaster primaryDriver = new DriverMaster();
			DriverMaster secondryDriver = new DriverMaster();
			if(busObj[0] != null)
				busMaster.setId(Integer.parseInt((busObj[0]).toString()));
			if(busObj[4] != null){
				primaryDriver.setId(Integer.parseInt((busObj[4]).toString()));
				busMaster.setPrimaryDriver(primaryDriver);
			}
			if(busObj[5] != null){
				secondryDriver.setId(Integer.parseInt((busObj[5]).toString()));
				busMaster.setSecondaryDriver(secondryDriver);
			}
			    
			if(busObj[6] != null){
				if((Integer.parseInt(busObj[6].toString()) >= 2018)){
					catABusList.add(busMaster);
				}else if((Integer.parseInt(busObj[6].toString()) >= 2015 && (Integer.parseInt(busObj[6].toString()) < 2018))){
					catBBusList.add(busMaster);
				}else if((Integer.parseInt(busObj[6].toString()) >= 2012 && (Integer.parseInt(busObj[6].toString()) < 2015))){
					catCBusList.add(busMaster);
				}else if((Integer.parseInt(busObj[6].toString()) < 2012)){
					catDBusList.add(busMaster);
				}
			}
		}
		
		
		DailyRoasterAuto dailyObj = null;
		RoasterAuto roaster = new RoasterAuto();
		Roaster roasterObj = new  Roaster();
		DailyRoaster dailyOldObj = null;
		List<DailyRoaster> drListOld = new ArrayList<>();
		List<DailyRoasterAuto> drList = new ArrayList<>();
		int i = 0;
		int j = 0;
		int k = 0;
		int m = 0;
		int n = 0;
		int l = 0;
		int x = 0;
		int y = 0;
		int p = 0;
		int q = 0;
		int r = 0;
		int s = 0;
		int z = 0;
		Calendar c = Calendar.getInstance();
		TransportUnitMaster tu = new TransportUnitMaster();
		tu.setId(tpId);
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
		Date dt = new Date();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		roaster.setRotaDate(this.getDate(1, new Date()));
		roaster.setRoastedCode(depot.getId() + "/" + tpId + "/" + sdf.format(dt));
		
		roasterObj.setRotaDate(this.getDate(1, new Date()));
		roasterObj.setRoastedCode(depot.getId() + "/" + tpId + "/" + sdf.format(dt));
		try {
			if(tripList.size() == 0){
				return new ResponseEntity<>(
						new ResponseStatus("Roaster could'nt be generated for depot " + depot.getDepotName() + " .", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			//if(roasterTemp == null){
				for(TripMaster tempCatAObj :  catATripList){
					DriverMaster driverMaster = new DriverMaster();
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new  DailyRoaster();
					SameRoutesHelperForRota helper = new SameRoutesHelperForRota();
					if(catABusList.size() > i){
					if(catABusList.get(i) != null && catABusList.get(i).getId() != null){
						if(sameAClassRouteMap.isEmpty()){
							if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
								helper.setBusMaster(catABusList.get(i));
								helper.setTripStartTime(tempCatAObj.getTripStartTime());
								helper.setTripEndTime(tempCatAObj.getTripEndTime());
							sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							}
						}else{
							if(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
								SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
								if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
									dailyObj.setBus(helperObj.getBusMaster());
									dailyOldObj.setBus(helperObj.getBusMaster());
									dailyObj.setDriver(helperObj.getDriverMaster());
									dailyOldObj.setDriver(helperObj.getDriverMaster());
									dailyObj.setConductor(helperObj.getConductorMaster());
									dailyOldObj.setConductor(helperObj.getConductorMaster());
									dailyObj.setRotaAuto(roaster);
									dailyObj.setTripStatus(false);
									dailyObj.setRoute(tempCatAObj.getRouteMaster());
									dailyObj.setIsDeleted(false);
									dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
									dailyObj.setTrip(tempCatAObj);
									drList.add(dailyObj);
									
									dailyOldObj.setRota(roasterObj);
									dailyOldObj.setTripStatus(false);
									dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
									dailyOldObj.setIsDeleted(false);
									dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
									dailyOldObj.setTrip(tempCatAObj);
									drListOld.add(dailyOldObj);
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
									helper.setTripStartTime(tempCatAObj.getTripEndTime());
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									continue;
								}
							}else{
								helper.setBusMaster(catABusList.get(i));
								helper.setTripStartTime(tempCatAObj.getTripStartTime());
								helper.setTripEndTime(tempCatAObj.getTripEndTime());
							}
						}
					dailyObj.setBus(catABusList.get(i));
					dailyOldObj.setBus(catABusList.get(i));
					}
					}else if(catBBusList.size() > x){
						if(catBBusList.get(x) != null && catBBusList.get(x).getId() != null){
							if(sameAClassRouteMap.isEmpty()){
								if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catABusList.get(i));
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatAObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatAObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatAObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										helper.setTripStartTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catABusList.get(x));
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catBBusList.get(x));
							dailyOldObj.setBus(catBBusList.get(x));
							catBBusList.remove(catBBusList.get(x));
							}
					}else if(catCBusList.size() > m){
						if(catCBusList.get(m) != null && catCBusList.get(m).getId() != null){
							if(sameAClassRouteMap.isEmpty()){
								if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catABusList.get(m));
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatAObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatAObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatAObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										helper.setTripStartTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catCBusList.get(m));
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catCBusList.get(m));
							dailyOldObj.setBus(catCBusList.get(m));
							catCBusList.remove(catCBusList.get(m));
							}
					}
					
					else if(catDBusList.size() > n){
						if(catDBusList.get(n) != null && catDBusList.get(n).getId() != null){
							if(sameAClassRouteMap.isEmpty()){
								if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catABusList.get(n));
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameAClassRouteMap.get(tempCatAObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatAObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatAObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatAObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatAObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatAObj.getTripEndTime());
										helper.setTripStartTime(tempCatAObj.getTripEndTime());
										sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catDBusList.get(n));
									helper.setTripStartTime(tempCatAObj.getTripStartTime());
									helper.setTripEndTime(tempCatAObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catDBusList.get(n));
							dailyOldObj.setBus(catDBusList.get(n));
							catDBusList.remove(catDBusList.get(n));
							}
					}else{
						continue;
					}
					Set<Integer> refuelingList = new HashSet<>();
					Float maxCurrentKmplOnDriverBasis = null;
					Float currentKmpl = null;
					String driverCode = null;
					Object[] maxCurrentKmplOnBusBasis = null;
			        maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(tempCatAObj.getRouteMaster().getId(), dailyObj.getBus().getId());
					if(catABusList.size() > i){
						if(catABusList.get(i).getPrimaryDriver() != null && catABusList.get(i).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catABusList.get(i).getPrimaryDriver().getId());
								if(maxCurrentKmplOnBusBasis != null){
								if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
									List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(i).getPrimaryDriver().getId());
									if(dailyRoasterList.size() > 0){
										for(DailyRoaster autoObj : dailyRoasterList){
											if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
												refuelingList.add(autoObj.getRefueling().getId());
											}
										}
									}
									if(refuelingList.size() > 0){
										maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
										if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
											Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
											 currentKmpl = Float.parseFloat((obj[0]).toString());
											 driverCode = obj[1].toString();
										}
									if(maxCurrentKmplOnDriverBasis < currentKmpl){
										dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										
									}
									}
									}
						}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
								helper.setDriverMaster(driverMaster);
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								availableDriverMap.remove(catABusList.get(i).getPrimaryDriver().getId());
								catADriverList.remove(i);
						}
								
						}else if(catABusList.get(i).getSecondaryDriver() != null && catABusList.get(i).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catABusList.get(i).getSecondaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(i).getSecondaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
								helper.setDriverMaster(driverMaster);
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								availableDriverMap.remove(catABusList.get(i).getSecondaryDriver().getId());
								catADriverList.remove(i);
							}else if(catADriverList.size() > i){
								if(catADriverList.get(i) != null && catADriverList.get(i).getId() != null){
								driverMaster.setId(catADriverList.get(i).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
								helper.setDriverMaster(driverMaster);
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								availableDriverMap.remove(catADriverList.get(i).getId());
								catADriverList.remove(i);
							}
						}
					    }else if(catBBusList.size() > x){
						if(catBBusList.get(x).getPrimaryDriver() != null && catBBusList.get(x).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catBBusList.get(x).getPrimaryDriver().getId());
								if(maxCurrentKmplOnBusBasis != null){
									if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
										List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(x).getPrimaryDriver().getId());
										if(dailyRoasterList.size() > 0){
											for(DailyRoaster autoObj : dailyRoasterList){
												if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
													refuelingList.add(autoObj.getRefueling().getId());
												}
											}
										}
										if(refuelingList.size() > 0){
											maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
											if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
												Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
												 currentKmpl = Float.parseFloat((obj[0]).toString());
												 driverCode = obj[1].toString();
											}
										if(maxCurrentKmplOnDriverBasis < currentKmpl){
											dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										}
										}
										}
							}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
								helper.setDriverMaster(driverMaster);
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								availableDriverMap.remove(catBBusList.get(x).getPrimaryDriver().getId());
								catBBusList.remove(x);
						}
								
							else if(catBBusList.get(x).getSecondaryDriver() != null && catBBusList.get(x).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catBBusList.get(x).getSecondaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(x).getSecondaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
								helper.setDriverMaster(driverMaster);
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								availableDriverMap.remove(catBBusList.get(x).getSecondaryDriver().getId());
								catBDriverList.remove(x);
							}else if(catBDriverList.size() > x){
								if(catBDriverList.get(x) != null && catBDriverList.get(x).getId() != null)
								driverMaster.setId(catBDriverList.get(x).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
								helper.setDriverMaster(driverMaster);
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								availableDriverMap.remove(catBDriverList.get(x).getId());
								catBDriverList.remove(x);
							}
						}else if(catCBusList.size() > m){
							if(catCBusList.get(m).getPrimaryDriver() != null && catCBusList.get(m).getPrimaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
									driverMaster.setId(catCBusList.get(m).getPrimaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(m).getPrimaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
									dailyObj.setDriver(driverMaster);
									dailyOldObj.setDriver(driverMaster);
									if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									availableDriverMap.remove(catCBusList.get(m).getPrimaryDriver().getId());
									catCBusList.remove(m);
							}
									
								else if(catCBusList.get(m).getSecondaryDriver() != null && catCBusList.get(m).getSecondaryDriver().getId() != null){
										//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
										driverMaster.setId(catCBusList.get(m).getSecondaryDriver().getId());
										if(maxCurrentKmplOnBusBasis != null){
											if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(m).getSecondaryDriver().getId());
												if(dailyRoasterList.size() > 0){
													for(DailyRoaster autoObj : dailyRoasterList){
														if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if(refuelingList.size() > 0){
													maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
													if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														 currentKmpl = Float.parseFloat((obj[0]).toString());
														 driverCode = obj[1].toString();
													}
												if(maxCurrentKmplOnDriverBasis < currentKmpl){
													dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
													dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												}
												}
												}
									}
									dailyObj.setDriver(driverMaster);
									dailyOldObj.setDriver(driverMaster);
									if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									availableDriverMap.remove(catCBusList.get(m).getSecondaryDriver().getId());
									catBDriverList.remove(m);
								}else if(catCDriverList.size() > m){
									if(catCDriverList.get(m) != null && catCDriverList.get(m).getId() != null)
									driverMaster.setId(catCDriverList.get(m).getId());
									dailyObj.setDriver(driverMaster);
									dailyOldObj.setDriver(driverMaster);
									if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									availableDriverMap.remove(catCDriverList.get(m).getId());
									catCDriverList.remove(m);
								}
							}else if(catDBusList.size() > n){
							if(catDBusList.get(n).getPrimaryDriver() != null && catDBusList.get(n).getPrimaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
									driverMaster.setId(catDBusList.get(n).getPrimaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(n).getPrimaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
									dailyObj.setDriver(driverMaster);
									dailyOldObj.setDriver(driverMaster);
									if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									availableDriverMap.remove(catDBusList.get(n).getPrimaryDriver().getId());
									catDBusList.remove(n);
							}
									
								else if(catDBusList.get(n).getSecondaryDriver() != null && catDBusList.get(n).getSecondaryDriver().getId() != null){
										//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
										driverMaster.setId(catDBusList.get(n).getSecondaryDriver().getId());
										if(maxCurrentKmplOnBusBasis != null){
											if(tempCatAObj.getRouteMaster() != null && tempCatAObj.getRouteMaster().getId() != null){
												List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatAObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(n).getSecondaryDriver().getId());
												if(dailyRoasterList.size() > 0){
													for(DailyRoaster autoObj : dailyRoasterList){
														if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
															refuelingList.add(autoObj.getRefueling().getId());
														}
													}
												}
												if(refuelingList.size() > 0){
													maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
													if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
														Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
														 currentKmpl = Float.parseFloat((obj[0]).toString());
														 driverCode = obj[1].toString();
													}
												if(maxCurrentKmplOnDriverBasis < currentKmpl){
													dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
													dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												}
												}
												}
									}
									dailyObj.setDriver(driverMaster);
									dailyOldObj.setDriver(driverMaster);
									if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									availableDriverMap.remove(catDBusList.get(n).getSecondaryDriver().getId());
									catBDriverList.remove(p);
								}else if(catDDriverList.size() > n){
									if(catDDriverList.get(n) != null && catDDriverList.get(n).getId() != null)
									driverMaster.setId(catDDriverList.get(n).getId());
									dailyObj.setDriver(driverMaster);
									dailyOldObj.setDriver(driverMaster);
									if(sameAClassRouteMap.size() == 1 || !(sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
									}
									availableDriverMap.remove(catDDriverList.get(n).getId());
									catDDriverList.remove(n);
								}
							}else{
								continue;
							}
					
					if(catAConductorList.size() > i){
						if(catAConductorList.get(i) != null && catAConductorList.get(i).getId() != null){
							if(sameAClassRouteMap.size() == 1 || !sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
							helper.setConductorMaster(catAConductorList.get(i));
							sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							}
						dailyObj.setConductor(catAConductorList.get(i));
						dailyOldObj.setConductor(catAConductorList.get(i));
						}
					}else if(catBConductorList.size() > x){
							if(catBConductorList.get(x) != null && catBConductorList.get(x).getId() != null){
								//j++;
								if(sameAClassRouteMap.size() == 1 || !sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
								helper.setConductorMaster(catBConductorList.get(x));
								sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
								}
								dailyObj.setConductor(catBConductorList.get(x));
								dailyOldObj.setConductor(catBConductorList.get(x));
								catBConductorList.remove(catBConductorList.get(x));
							}
					}else if(catCConductorList.size() > m){
						if(catCConductorList.get(m) != null && catCConductorList.get(m).getId() != null){
							//r++;
							if(sameAClassRouteMap.size() == 1 || !sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
							helper.setConductorMaster(catCConductorList.get(m));
							sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
							}
							dailyObj.setConductor(catCConductorList.get(m));
							dailyOldObj.setConductor(catCConductorList.get(m));
							catCConductorList.remove(catCConductorList.get(m));
						}
				}else if(catDConductorList.size() > n){
					if(catDConductorList.get(n) != null && catDConductorList.get(n).getId() != null){
						//j++;
						if(sameAClassRouteMap.size() == 1 || !sameAClassRouteMap.containsKey(tempCatAObj.getRouteMaster().getId())){
						helper.setConductorMaster(catDConductorList.get(n));
						sameAClassRouteMap.put(tempCatAObj.getRouteMaster().getId(), helper);
						}
						dailyObj.setConductor(catDConductorList.get(n));
						dailyOldObj.setConductor(catDConductorList.get(n));
						catDConductorList.remove(catDConductorList.get(n));
					}
			}else{
				continue;
			}
					
					dailyObj.setRotaAuto(roaster);
					dailyObj.setTripStatus(false);
					dailyObj.setRoute(tempCatAObj.getRouteMaster());
					dailyObj.setIsDeleted(false);
					dailyObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
					dailyObj.setTrip(tempCatAObj);
					drList.add(dailyObj);
					
					dailyOldObj.setRota(roasterObj);
					dailyOldObj.setTripStatus(false);
					dailyOldObj.setRoute(tempCatAObj.getRouteMaster());
					dailyOldObj.setIsDeleted(false);
					dailyOldObj.setRouteType(tempCatAObj.getRouteMaster().getRouteTypeMaster());
					dailyOldObj.setTrip(tempCatAObj);
					drListOld.add(dailyOldObj);
					i++;
					x++;
					m++;
					n++;
					
				}
				for(TripMaster tempCatBObj :  catBTripList){
					DriverMaster driverMaster = new DriverMaster();
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new  DailyRoaster();
					SameRoutesHelperForRota helper = new SameRoutesHelperForRota();
					if(catBBusList.size() > k){
					if(catBBusList.get(k) != null && catBBusList.get(k).getId() != null){
						if(sameBClassRouteMap.isEmpty()){
							if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
								helper.setBusMaster(catBBusList.get(k));
								helper.setTripStartTime(tempCatBObj.getTripStartTime());
								helper.setTripEndTime(tempCatBObj.getTripEndTime());
							sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
							}
						}else{
							if(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId())){
								SameRoutesHelperForRota helperObj = sameBClassRouteMap.get(tempCatBObj.getRouteMaster().getId());
								if(helperObj.getTripEndTime().isBefore(tempCatBObj.getTripStartTime())){
									dailyObj.setBus(helperObj.getBusMaster());
									dailyOldObj.setBus(helperObj.getBusMaster());
									dailyObj.setDriver(helperObj.getDriverMaster());
									dailyOldObj.setDriver(helperObj.getDriverMaster());
									dailyObj.setConductor(helperObj.getConductorMaster());
									dailyOldObj.setConductor(helperObj.getConductorMaster());
									dailyObj.setRotaAuto(roaster);
									dailyObj.setTripStatus(false);
									dailyObj.setRoute(tempCatBObj.getRouteMaster());
									dailyObj.setIsDeleted(false);
									dailyObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
									dailyObj.setTrip(tempCatBObj);
									drList.add(dailyObj);
									
									dailyOldObj.setRota(roasterObj);
									dailyOldObj.setTripStatus(false);
									dailyOldObj.setRoute(tempCatBObj.getRouteMaster());
									dailyOldObj.setIsDeleted(false);
									dailyOldObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
									dailyOldObj.setTrip(tempCatBObj);
									drListOld.add(dailyOldObj);
									helper.setTripEndTime(tempCatBObj.getTripEndTime());
									helper.setTripStartTime(tempCatBObj.getTripEndTime());
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									continue;
								}
							}else{
								helper.setBusMaster(catBBusList.get(k));
								helper.setTripStartTime(tempCatBObj.getTripStartTime());
								helper.setTripEndTime(tempCatBObj.getTripEndTime());
							}
						}
					dailyObj.setBus(catBBusList.get(k));
					dailyOldObj.setBus(catBBusList.get(k));
					catBBusList.remove(catBBusList.get(k));
					}
					}else if(catCBusList.size() > y){
						if(catCBusList.get(y) != null && catCBusList.get(y).getId() != null){
							if(sameBClassRouteMap.isEmpty()){
								if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catCBusList.get(y));
									helper.setTripStartTime(tempCatBObj.getTripStartTime());
									helper.setTripEndTime(tempCatBObj.getTripEndTime());
								sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameBClassRouteMap.get(tempCatBObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatBObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatBObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatBObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatBObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatBObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatBObj.getTripEndTime());
										helper.setTripStartTime(tempCatBObj.getTripEndTime());
										sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catCBusList.get(y));
									helper.setTripStartTime(tempCatBObj.getTripStartTime());
									helper.setTripEndTime(tempCatBObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catCBusList.get(y));
							dailyOldObj.setBus(catCBusList.get(y));
							catCBusList.remove(catCBusList.get(y));
							}
					}else if(catDBusList.size() > z){
						if(catDBusList.get(z) != null && catDBusList.get(z).getId() != null){
							if(sameBClassRouteMap.isEmpty()){
								if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catDBusList.get(z));
									helper.setTripStartTime(tempCatBObj.getTripStartTime());
									helper.setTripEndTime(tempCatBObj.getTripEndTime());
								sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameBClassRouteMap.get(tempCatBObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatBObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatBObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatBObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatBObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatBObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatBObj.getTripEndTime());
										helper.setTripStartTime(tempCatBObj.getTripEndTime());
										sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catDBusList.get(z));
									helper.setTripStartTime(tempCatBObj.getTripStartTime());
									helper.setTripEndTime(tempCatBObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catDBusList.get(z));
							dailyOldObj.setBus(catDBusList.get(z));
							catDBusList.remove(catDBusList.get(z));
							}
					}else{
						continue;
					}
					
					
					Set<Integer> refuelingList = new HashSet<>();
					Float maxCurrentKmplOnDriverBasis = null;
					Float currentKmpl = null;
					String driverCode = null;
					Object[] maxCurrentKmplOnBusBasis = null;
			        maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(tempCatBObj.getRouteMaster().getId(), dailyObj.getBus().getId());
					
					if(catBBusList.size() > k){
						if(catBBusList.get(k).getPrimaryDriver() != null && catBBusList.get(k).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catBBusList.get(k).getPrimaryDriver().getId());
								if(maxCurrentKmplOnBusBasis != null){
									if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
										List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatBObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(k).getPrimaryDriver().getId());
										if(dailyRoasterList.size() > 0){
											for(DailyRoaster autoObj : dailyRoasterList){
												if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
													refuelingList.add(autoObj.getRefueling().getId());
												}
											}
										}
										if(refuelingList.size() > 0){
											maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
											if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
												Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
												 currentKmpl = Float.parseFloat((obj[0]).toString());
												 driverCode = obj[1].toString();
											}
										if(maxCurrentKmplOnDriverBasis < currentKmpl){
											dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										}
										}
										}
							}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catBBusList.get(k).getPrimaryDriver().getId());
									catBDriverList.remove(k);
							}
								
						}
								
							else if(catBBusList.get(k).getSecondaryDriver() != null && catBBusList.get(k).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catBBusList.get(k).getSecondaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatBObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(k).getSecondaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catBBusList.get(k).getSecondaryDriver().getId());
									catBDriverList.remove(k);
							}
								
							}else if(catBDriverList.size() > k){
								if(catBDriverList.get(k) != null && catBDriverList.get(k).getId() != null)
								driverMaster.setId(catBDriverList.get(k).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catBDriverList.get(k).getId());
									catBDriverList.remove(k);
							}
								
							}
						}else if(catCBusList.size() > y){
						if(catCBusList.get(y).getPrimaryDriver() != null && catCBusList.get(y).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catCBusList.get(y).getPrimaryDriver().getId());
								if(maxCurrentKmplOnBusBasis != null){
									if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
										List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatBObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(y).getPrimaryDriver().getId());
										if(dailyRoasterList.size() > 0){
											for(DailyRoaster autoObj : dailyRoasterList){
												if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
													refuelingList.add(autoObj.getRefueling().getId());
												}
											}
										}
										if(refuelingList.size() > 0){
											maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
											if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
												Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
												 currentKmpl = Float.parseFloat((obj[0]).toString());
												 driverCode = obj[1].toString();
											}
										if(maxCurrentKmplOnDriverBasis < currentKmpl){
											dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										}
										}
										}
							}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catCBusList.get(y).getPrimaryDriver().getId());
									catCDriverList.remove(y);
							}
								
						}
								
							else if(catCBusList.get(y).getSecondaryDriver() != null && catCBusList.get(y).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catCBusList.get(y).getSecondaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatBObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(y).getSecondaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catCBusList.get(y).getSecondaryDriver().getId());
									catCDriverList.remove(y);
							}
								
							}else if(catCDriverList.size() > y){
								if(catCDriverList.get(y) != null && catCDriverList.get(y).getId() != null)
								driverMaster.setId(catCDriverList.get(y).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catCDriverList.get(y).getId());
									catCDriverList.remove(y);
							}
								
							}
						}else if(catDBusList.size() > z){
						if(catDBusList.get(z).getPrimaryDriver() != null && catDBusList.get(z).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catDBusList.get(z).getPrimaryDriver().getId());
								if(maxCurrentKmplOnBusBasis != null){
									if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
										List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatBObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(z).getPrimaryDriver().getId());
										if(dailyRoasterList.size() > 0){
											for(DailyRoaster autoObj : dailyRoasterList){
												if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
													refuelingList.add(autoObj.getRefueling().getId());
												}
											}
										}
										if(refuelingList.size() > 0){
											maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
											if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
												Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
												 currentKmpl = Float.parseFloat((obj[0]).toString());
												 driverCode = obj[1].toString();
											}
										if(maxCurrentKmplOnDriverBasis < currentKmpl){
											dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										}
										}
										}
							}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDBusList.get(z).getPrimaryDriver().getId());
									catCDriverList.remove(z);
							}
								
						}
								
							else if(catDBusList.get(z).getSecondaryDriver() != null && catDBusList.get(z).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catDBusList.get(z).getSecondaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatBObj.getRouteMaster() != null && tempCatBObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatBObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(z).getSecondaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDBusList.get(z).getSecondaryDriver().getId());
									catCDriverList.remove(z);
							}
								
							}else if(catDDriverList.size() > z){
								if(catDDriverList.get(z) != null && catDDriverList.get(z).getId() != null)
								driverMaster.setId(catDDriverList.get(z).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameBClassRouteMap.size() == 1 || !(sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameBClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDDriverList.get(z).getId());
									catDDriverList.remove(z);
							}
								
							}
						}
					
					if(catBConductorList.size() > k){
						if(catBConductorList.get(k) != null && catBConductorList.get(k).getId() != null){
							if(sameBClassRouteMap.size() == 1 || !sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId())){
								helper.setConductorMaster(catBConductorList.get(k));
								sameAClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
								}
						dailyObj.setConductor(catBConductorList.get(k));
						dailyOldObj.setConductor(catBConductorList.get(k));
						}
					}else if(catCConductorList.size() > y){
							if(catCConductorList.get(y) != null && catCConductorList.get(y).getId() != null){
								//l++;
								if(sameBClassRouteMap.size() == 1 || !sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId())){
									helper.setConductorMaster(catCConductorList.get(y));
									sameAClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
									}
								dailyObj.setConductor(catCConductorList.get(y));
								dailyOldObj.setConductor(catCConductorList.get(y));
							}
					}else if(catDConductorList.size() > z){
						if(catDConductorList.get(z) != null && catDConductorList.get(z).getId() != null){
							//l++;
							if(sameBClassRouteMap.size() == 1 || !sameBClassRouteMap.containsKey(tempCatBObj.getRouteMaster().getId())){
								helper.setConductorMaster(catDConductorList.get(z));
								sameAClassRouteMap.put(tempCatBObj.getRouteMaster().getId(), helper);
								}
							dailyObj.setConductor(catDConductorList.get(z));
							dailyOldObj.setConductor(catDConductorList.get(z));
						}
				}
					
					dailyObj.setRotaAuto(roaster);
					dailyObj.setTripStatus(false);
					dailyObj.setRoute(tempCatBObj.getRouteMaster());
					dailyObj.setIsDeleted(false);
					dailyObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
					dailyObj.setTrip(tempCatBObj);
					drList.add(dailyObj);
					
					dailyOldObj.setRota(roasterObj);
					dailyOldObj.setTripStatus(false);
					dailyOldObj.setRoute(tempCatBObj.getRouteMaster());
					dailyOldObj.setIsDeleted(false);
					dailyOldObj.setRouteType(tempCatBObj.getRouteMaster().getRouteTypeMaster());
					dailyOldObj.setTrip(tempCatBObj);
					drListOld.add(dailyOldObj);
					k++;
					y++;
					z++;
				}
				
				for(TripMaster tempCatCObj :  catCTripList){
					DriverMaster driverMaster = new DriverMaster();
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new  DailyRoaster();
					SameRoutesHelperForRota helper = new SameRoutesHelperForRota();
					 if(catCBusList.size() > q){
						if(catCBusList.get(q) != null && catCBusList.get(q).getId() != null){
							if(sameCClassRouteMap.isEmpty()){
								if(tempCatCObj.getRouteMaster() != null && tempCatCObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catCBusList.get(q));
									helper.setTripStartTime(tempCatCObj.getTripStartTime());
									helper.setTripEndTime(tempCatCObj.getTripEndTime());
								sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameCClassRouteMap.get(tempCatCObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatCObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatCObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatCObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatCObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatCObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatCObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatCObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatCObj.getTripEndTime());
										helper.setTripStartTime(tempCatCObj.getTripEndTime());
										sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catCBusList.get(q));
									helper.setTripStartTime(tempCatCObj.getTripStartTime());
									helper.setTripEndTime(tempCatCObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catCBusList.get(q));
							dailyOldObj.setBus(catCBusList.get(q));
							catCBusList.remove(catCBusList.get(q));
							}
					}else if(catDBusList.size() > r){
						if(catDBusList.get(r) != null && catDBusList.get(r).getId() != null){
							if(sameCClassRouteMap.isEmpty()){
								if(tempCatCObj.getRouteMaster() != null && tempCatCObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catDBusList.get(r));
									helper.setTripStartTime(tempCatCObj.getTripStartTime());
									helper.setTripEndTime(tempCatCObj.getTripEndTime());
								sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameCClassRouteMap.get(tempCatCObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatCObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatCObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatCObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatCObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatCObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatCObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatCObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatCObj.getTripEndTime());
										helper.setTripStartTime(tempCatCObj.getTripEndTime());
										sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catDBusList.get(r));
									helper.setTripStartTime(tempCatCObj.getTripStartTime());
									helper.setTripEndTime(tempCatCObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catDBusList.get(r));
							dailyOldObj.setBus(catDBusList.get(r));
							catDBusList.remove(catDBusList.get(r));
							}
					}else{
						continue;
					}
					
					 Set<Integer> refuelingList = new HashSet<>();
						Float maxCurrentKmplOnDriverBasis = null;
						Float currentKmpl = null;
						String driverCode = null;
						Object[] maxCurrentKmplOnBusBasis = null;
				        maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(tempCatCObj.getRouteMaster().getId(), dailyObj.getBus().getId());
					
					 if(catCBusList.size() > q){
						if(catCBusList.get(q).getPrimaryDriver() != null && catCBusList.get(q).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catCBusList.get(q).getPrimaryDriver().getId());
								if(maxCurrentKmplOnBusBasis != null){
									if(tempCatCObj.getRouteMaster() != null && tempCatCObj.getRouteMaster().getId() != null){
										List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatCObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(q).getPrimaryDriver().getId());
										if(dailyRoasterList.size() > 0){
											for(DailyRoaster autoObj : dailyRoasterList){
												if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
													refuelingList.add(autoObj.getRefueling().getId());
												}
											}
										}
										if(refuelingList.size() > 0){
											maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
											if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
												Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
												 currentKmpl = Float.parseFloat((obj[0]).toString());
												 driverCode = obj[1].toString();
											}
										if(maxCurrentKmplOnDriverBasis < currentKmpl){
											dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										}
										}
										}
							}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameCClassRouteMap.size() == 1 || !(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catCBusList.get(q).getPrimaryDriver().getId());
									catCDriverList.remove(q);
							}
								
						}
								
							else if(catCBusList.get(q).getSecondaryDriver() != null && catCBusList.get(q).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catCBusList.get(q).getSecondaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatCObj.getRouteMaster() != null && tempCatCObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatCObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(q).getSecondaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameCClassRouteMap.size() == 1 || !(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catCBusList.get(q).getSecondaryDriver().getId());
									catCDriverList.remove(q);
							}
								
							}else if(catCDriverList.size() > q){
								if(catCDriverList.get(q) != null && catCDriverList.get(q).getId() != null)
								driverMaster.setId(catCDriverList.get(q).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameCClassRouteMap.size() == 1 || !(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catCDriverList.get(q).getId());
									catCDriverList.remove(q);
							}
								
							}
						}else if(catDBusList.size() > r){
						if(catDBusList.get(r).getPrimaryDriver() != null && catDBusList.get(r).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catDBusList.get(r).getPrimaryDriver().getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameCClassRouteMap.size() == 1 || !(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDBusList.get(r).getPrimaryDriver().getId());
									catCDriverList.remove(r);
							}
								
						}
								
							else if(catDBusList.get(r).getSecondaryDriver() != null && catDBusList.get(r).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catDBusList.get(r).getSecondaryDriver().getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameCClassRouteMap.size() == 1 || !(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDBusList.get(r).getSecondaryDriver().getId());
									catCDriverList.remove(r);
							}
								
							}else if(catDDriverList.size() > r){
								if(catDDriverList.get(r) != null && catDDriverList.get(r).getId() != null)
								driverMaster.setId(catDDriverList.get(r).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameCClassRouteMap.size() == 1 || !(sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameCClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDDriverList.get(r).getId());
									catDDriverList.remove(r);
							}
								
							}
						}else{
							continue;
						}
					
					 if(catCConductorList.size() > q){
							if(catCConductorList.get(q) != null && catCConductorList.get(q).getId() != null){
								//l++;
								if(sameCClassRouteMap.size() == 1 || !sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId())){
									helper.setConductorMaster(catCConductorList.get(q));
									sameAClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
									}
								dailyObj.setConductor(catCConductorList.get(q));
								dailyOldObj.setConductor(catCConductorList.get(q));
							}
					}else if(catDConductorList.size() > r){
						if(catDConductorList.get(r) != null && catDConductorList.get(r).getId() != null){
							//l++;
							if(sameCClassRouteMap.size() == 1 || !sameCClassRouteMap.containsKey(tempCatCObj.getRouteMaster().getId())){
								helper.setConductorMaster(catDConductorList.get(r));
								sameAClassRouteMap.put(tempCatCObj.getRouteMaster().getId(), helper);
								}
							dailyObj.setConductor(catDConductorList.get(r));
							dailyOldObj.setConductor(catDConductorList.get(r));
						}
				}else{
					continue;
				}
					
					dailyObj.setRotaAuto(roaster);
					dailyObj.setTripStatus(false);
					dailyObj.setRoute(tempCatCObj.getRouteMaster());
					dailyObj.setIsDeleted(false);
					dailyObj.setRouteType(tempCatCObj.getRouteMaster().getRouteTypeMaster());
					dailyObj.setTrip(tempCatCObj);
					drList.add(dailyObj);
					
					dailyOldObj.setRota(roasterObj);
					dailyOldObj.setTripStatus(false);
					dailyOldObj.setRoute(tempCatCObj.getRouteMaster());
					dailyOldObj.setIsDeleted(false);
					dailyOldObj.setRouteType(tempCatCObj.getRouteMaster().getRouteTypeMaster());
					dailyOldObj.setTrip(tempCatCObj);
					drListOld.add(dailyOldObj);
					q++;
					r++;
				}
				
				for(TripMaster tempCatDObj :  catDTripList){
					DriverMaster driverMaster = new DriverMaster();
					dailyObj = new DailyRoasterAuto();
					dailyOldObj = new  DailyRoaster();
					SameRoutesHelperForRota helper = new SameRoutesHelperForRota();
					  if(catDBusList.size() > s){
						if(catDBusList.get(s) != null && catDBusList.get(s).getId() != null){
							if(sameDClassRouteMap.isEmpty()){
								if(tempCatDObj.getRouteMaster() != null && tempCatDObj.getRouteMaster().getId() != null){
									helper.setBusMaster(catDBusList.get(s));
									helper.setTripStartTime(tempCatDObj.getTripStartTime());
									helper.setTripEndTime(tempCatDObj.getTripEndTime());
								sameDClassRouteMap.put(tempCatDObj.getRouteMaster().getId(), helper);
								}
							}else{
								if(sameDClassRouteMap.containsKey(tempCatDObj.getRouteMaster().getId())){
									SameRoutesHelperForRota helperObj = sameDClassRouteMap.get(tempCatDObj.getRouteMaster().getId());
									if(helperObj.getTripEndTime().isBefore(tempCatDObj.getTripStartTime())){
										dailyObj.setBus(helperObj.getBusMaster());
										dailyOldObj.setBus(helperObj.getBusMaster());
										dailyObj.setDriver(helperObj.getDriverMaster());
										dailyOldObj.setDriver(helperObj.getDriverMaster());
										dailyObj.setConductor(helperObj.getConductorMaster());
										dailyOldObj.setConductor(helperObj.getConductorMaster());
										dailyObj.setRotaAuto(roaster);
										dailyObj.setTripStatus(false);
										dailyObj.setRoute(tempCatDObj.getRouteMaster());
										dailyObj.setIsDeleted(false);
										dailyObj.setRouteType(tempCatDObj.getRouteMaster().getRouteTypeMaster());
										dailyObj.setTrip(tempCatDObj);
										drList.add(dailyObj);
										
										dailyOldObj.setRota(roasterObj);
										dailyOldObj.setTripStatus(false);
										dailyOldObj.setRoute(tempCatDObj.getRouteMaster());
										dailyOldObj.setIsDeleted(false);
										dailyOldObj.setRouteType(tempCatDObj.getRouteMaster().getRouteTypeMaster());
										dailyOldObj.setTrip(tempCatDObj);
										drListOld.add(dailyOldObj);
										
										helper.setTripEndTime(tempCatDObj.getTripEndTime());
										helper.setTripStartTime(tempCatDObj.getTripEndTime());
										sameDClassRouteMap.put(tempCatDObj.getRouteMaster().getId(), helper);
										continue;
									}
								}else{
									helper.setBusMaster(catDBusList.get(s));
									helper.setTripStartTime(tempCatDObj.getTripStartTime());
									helper.setTripEndTime(tempCatDObj.getTripEndTime());
								}
							}
							dailyObj.setBus(catDBusList.get(s));
							dailyOldObj.setBus(catDBusList.get(s));
							catDBusList.remove(catDBusList.get(s));
							}
					}else{
						continue;
					}
					
					  Set<Integer> refuelingList = new HashSet<>();
						Float maxCurrentKmplOnDriverBasis = null;
						Float currentKmpl = null;
						String driverCode = null;
						Object[] maxCurrentKmplOnBusBasis = null;
				        maxCurrentKmplOnBusBasis = refuellingRepository.fetchMaxScheduledKmsByRouteAndBus(tempCatDObj.getRouteMaster().getId(), dailyObj.getBus().getId());
					
					 if(catDBusList.size() > s){
						if(catDBusList.get(s).getPrimaryDriver() != null && catDBusList.get(s).getPrimaryDriver().getId() != null){
								//availableDriverMap.containsKey(catABusList.get(i).getPrimaryDriver().getId())){
								driverMaster.setId(catDBusList.get(s).getPrimaryDriver().getId());
								if(maxCurrentKmplOnBusBasis != null){
									if(tempCatDObj.getRouteMaster() != null && tempCatDObj.getRouteMaster().getId() != null){
										List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatDObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(s).getPrimaryDriver().getId());
										if(dailyRoasterList.size() > 0){
											for(DailyRoaster autoObj : dailyRoasterList){
												if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
													refuelingList.add(autoObj.getRefueling().getId());
												}
											}
										}
										if(refuelingList.size() > 0){
											maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
											if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
												Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
												 currentKmpl = Float.parseFloat((obj[0]).toString());
												 driverCode = obj[1].toString();
											}
										if(maxCurrentKmplOnDriverBasis < currentKmpl){
											dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
										}
										}
										}
							}
								
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameDClassRouteMap.size() == 1 || !(sameDClassRouteMap.containsKey(tempCatDObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameDClassRouteMap.put(tempCatDObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDBusList.get(s).getPrimaryDriver().getId());
									catDDriverList.remove(s);
							}
								
						}
								
							else if(catDBusList.get(s).getSecondaryDriver() != null && catDBusList.get(s).getSecondaryDriver().getId() != null){
									//availableDriverMap.containsKey(catABusList.get(i).getSecondaryDriver().getId())){
									driverMaster.setId(catDBusList.get(s).getSecondaryDriver().getId());
									if(maxCurrentKmplOnBusBasis != null){
										if(tempCatDObj.getRouteMaster() != null && tempCatDObj.getRouteMaster().getId() != null){
											List<DailyRoaster> dailyRoasterList = dailyRoasterRepository.findByRouteBusAndDriver(tempCatDObj.getRouteMaster().getId(),dailyObj.getBus().getId(),catABusList.get(s).getSecondaryDriver().getId());
											if(dailyRoasterList.size() > 0){
												for(DailyRoaster autoObj : dailyRoasterList){
													if(autoObj != null && autoObj.getRefueling() != null && autoObj.getRefueling().getId() != null){
														refuelingList.add(autoObj.getRefueling().getId());
													}
												}
											}
											if(refuelingList.size() > 0){
												maxCurrentKmplOnDriverBasis = refuellingRepository.fetchCurrentMaxKms(refuelingList);
												if(maxCurrentKmplOnBusBasis != null && maxCurrentKmplOnBusBasis[0] != null){
													Object[] obj = (Object[]) maxCurrentKmplOnBusBasis[0];
													 currentKmpl = Float.parseFloat((obj[0]).toString());
													 driverCode = obj[1].toString();
												}
											if(maxCurrentKmplOnDriverBasis < currentKmpl){
												dailyObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
												dailyOldObj.setSuggestion("This driver no : "+driverCode+" having better kmpl");
											}
											}
											}
								}
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameDClassRouteMap.size() == 1 || !(sameDClassRouteMap.containsKey(tempCatDObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameDClassRouteMap.put(tempCatDObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDBusList.get(s).getSecondaryDriver().getId());
									catDDriverList.remove(s);
							}
								
							}else if(catDDriverList.size() > s){
								if(catDDriverList.get(s) != null && catDDriverList.get(s).getId() != null)
								driverMaster.setId(catDDriverList.get(s).getId());
								dailyObj.setDriver(driverMaster);
								dailyOldObj.setDriver(driverMaster);
								if(sameDClassRouteMap.size() == 1 || !(sameDClassRouteMap.containsKey(tempCatDObj.getRouteMaster().getId()))){
									helper.setDriverMaster(driverMaster);
									sameDClassRouteMap.put(tempCatDObj.getRouteMaster().getId(), helper);
									availableDriverMap.remove(catDDriverList.get(s).getId());
									catDDriverList.remove(s);
							}
								
							}
						}else{
							continue;
						}
					
					 if(catDConductorList.size() > s){
						if(catDConductorList.get(s) != null && catDConductorList.get(s).getId() != null){
							//l++;
							if(sameDClassRouteMap.size() == 1 || !sameDClassRouteMap.containsKey(tempCatDObj.getRouteMaster().getId())){
								helper.setConductorMaster(catDConductorList.get(s));
								sameAClassRouteMap.put(tempCatDObj.getRouteMaster().getId(), helper);
								}
							dailyObj.setConductor(catDConductorList.get(s));
							dailyOldObj.setConductor(catDConductorList.get(s));
						}
				}else{
					continue;
				}
					
					dailyObj.setRotaAuto(roaster);
					dailyObj.setTripStatus(false);
					dailyObj.setRoute(tempCatDObj.getRouteMaster());
					dailyObj.setIsDeleted(false);
					dailyObj.setRouteType(tempCatDObj.getRouteMaster().getRouteTypeMaster());
					dailyObj.setTrip(tempCatDObj);
					drList.add(dailyObj);
					
					dailyOldObj.setRota(roasterObj);
					dailyOldObj.setTripStatus(false);
					dailyOldObj.setRoute(tempCatDObj.getRouteMaster());
					dailyOldObj.setIsDeleted(false);
					dailyOldObj.setRouteType(tempCatDObj.getRouteMaster().getRouteTypeMaster());
					dailyOldObj.setTrip(tempCatDObj);
					drListOld.add(dailyOldObj);
					s++;
				}
				
				
			roaster.setDailyRoasterList(drList);
			roaster = roasterRepo.save(roaster);
			
			roasterObj.setDailyRoasterList(drListOld);
			roasterObj = roasterOldRepo.save(roasterObj);
			return new ResponseEntity<>(
					new ResponseStatus("Roaster successfully generated for "+roaster.getRotaDate() + " .", HttpStatus.OK),
					HttpStatus.OK);
		//	}
				//else{
				//return new ResponseEntity<>(
					//new ResponseStatus("Roaster already generated for "+roaster.getRotaDate() + " .", HttpStatus.FORBIDDEN),
					//	HttpStatus.OK);
			//}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
			
		}
		
		//return null;
	}*/

}
