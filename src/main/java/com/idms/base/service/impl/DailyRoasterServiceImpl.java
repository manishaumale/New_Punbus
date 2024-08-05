package com.idms.base.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AllRotaListViewDto;
import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DailyRoasterAutoDto;
import com.idms.base.api.v1.model.dto.DailyRoasterDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.FormRotaListDto;
import com.idms.base.api.v1.model.dto.MarkSpareListDto;
import com.idms.base.api.v1.model.dto.RoasterDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusRotaHistory;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.ConductorRotaHistory;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.DriverRotaHistory;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.entity.Roaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.entity.RoutePermitMaster;
import com.idms.base.dao.entity.RouteTypeMaster;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.RoasterRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.RouteTypeRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.service.DailyRoasterService;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class DailyRoasterServiceImpl implements DailyRoasterService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	RouteTypeRepository rtRepo;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	RouteMasterRepository rmRepo;
	
	@Autowired 
	TripMasterRepository tripRepo;
	
	@Autowired
	DriverMasterRepository driverRepo;
	
	@Autowired
	ConductorMasterRepository conductorRepo;
	
	@Autowired
	BusMasterRepository busRepo;
	
	@Autowired
	TransportUnitRepository tpuRepo;
	
	@Autowired
	RoasterRepository roasterRepo;
	
	@Autowired
	DailyRoasterRepository dailyRotaRepo;

	@Override
	public List<RouteTypeDto> getTyreOfRoutes() {
		return rtRepo.findAll().stream()
		.map(rType -> new RouteTypeDto(rType.getId(), rType.getRouteTypeName())).collect(Collectors.toList());
	}

	@Override
	public List<RouteMasterDto> getRouteListWithTypeId(String dpCode, Integer typeId) {
		List<RouteMasterDto> list = new ArrayList<>();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(dpCode);
			list = rmRepo.findRoutesByTypeAndDepot(depot.getId(), typeId).stream()
					.map(route -> new RouteMasterDto(route.getId(), route.getRouteName())).collect(Collectors.toList());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<TripMasterDto> getTripTimeList(Integer routeId) {
		List<TripMasterDto> list = new ArrayList<>();
		try { 
			list = tripRepo.findByRouteId(routeId).stream()
					.map(trip -> new TripMasterDto(trip.getId(), trip.getTripStartTime().toString())).collect(Collectors.toList());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public FormRotaListDto generateAutoRoasterByRotaId(Integer rotaId) {
		FormRotaListDto form = new FormRotaListDto();
		Roaster roaster = null;
		try {
			roaster = roasterRepo.findById(rotaId).get();
			if(roaster!=null) {
				form = this.generateAutoRoaster(roaster.getDepot().getDepotCode(), roaster.getTransport().getId(),roaster.getRotaDate());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return form;
	}

	@Override
	public FormRotaListDto generateAutoRoaster(String dpCode, Integer tpId, Date rotaDate) {
		DepotMaster depot = depotRepo.findByDepotCode(dpCode);
		FormRotaListDto form = new FormRotaListDto();
		List<TripMasterDto> tripDtoList = new ArrayList<>();
		List<TripMaster> tripList = tripRepo.findByRouteType(depot.getId(), tpId);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		for(TripMaster trip : tripList) {
			boolean flag = true;
			for (RoutePermitMaster rpm : trip.getRouteMaster().getRoutePermitMasterList()) {
				PermitDetailsMaster permitDto = rpm.getPermitDetailsMaster(); 
				if (permitDto.getValidUpTo() != null && (permitDto.getValidUpTo().after(today)
						|| formatter.format(permitDto.getValidUpTo()) == (formatter.format(today)))) {
					flag=true;
				} else {
					flag=false;
					break;
				}
			}
			if(flag) {
				TripMasterDto dto = new TripMasterDto();
				dto.setId(trip.getId());
				dto.setUpDown(trip.getUpDown());
				dto.setTripStartTime(trip.getTripStartTime().toString());
				RouteMasterDto route = new RouteMasterDto();
				route.setId(trip.getRouteMaster().getId());
				route.setRouteName(trip.getRouteMaster().getRouteName());
				RouteTypeDto type = new RouteTypeDto();
				type.setId(trip.getRouteMaster().getRouteTypeMaster().getId());
				type.setRouteTypeName(trip.getRouteMaster().getRouteTypeMaster().getRouteTypeName());
				route.setRouteTypeMaster(type);
				DepotMasterDto depotDto = new DepotMasterDto();
				depotDto.setId(depot.getId());
				route.setDepotMaster(depotDto);
				dto.setRouteMaster(route);
				dto.setScheduledKms(trip.getScheduledKms());
				CityDto startCity = new CityDto(trip.getFromCity().getId(), trip.getFromCity().getCityName());
				dto.setFromCity(startCity);
				CityDto endCity = new CityDto(trip.getToCity().getId(), trip.getToCity().getCityName());
				dto.setToCity(endCity);
				dto.setTripEndTime(trip.getTripEndTime().toString());
				tripDtoList.add(dto);
			}
		}
		form.setTrip(tripDtoList);
		
		List<Object[]> driverList = driverRepo.getAvailableDrivers(depot.getId(), tpId, rotaDate);
		List<DriverMasterDto> driverDtoList = new ArrayList<>();
		for(Object[] o : driverList) {
			DriverMasterDto dto = new DriverMasterDto();
			dto.setId(Integer.parseInt(o[0].toString()));
			dto.setDriverName(o[1].toString());
			dto.setAvailableTime(LocalTime.parse(o[2].toString()));
			CityDto city = new CityDto();
			if(o[3]!=null) {
				city.setId(Integer.parseInt(o[3].toString()));
			}
			dto.setAvailableCity(city);
			driverDtoList.add(dto);
		}
		form.setDriverList(driverDtoList);
		
		List<Object[]> conductorList = conductorRepo.getAvailableConductors(depot.getId(), tpId, rotaDate);
		List<ConductorMasterDto> conductorDtoList = new ArrayList<>();
		for(Object[] o : conductorList) {
			ConductorMasterDto dto = new ConductorMasterDto();
			dto.setId(Integer.parseInt(o[0].toString()));
			dto.setConductorName(o[1].toString());
			dto.setAvailableTime(LocalTime.parse(o[2].toString()));
			CityDto city = new CityDto();
			if(o[3]!=null) {
				city.setId(Integer.parseInt(o[3].toString()));
			}
			dto.setAvailableCity(city);
			conductorDtoList.add(dto);
		}
		form.setConductorList(conductorDtoList);
		
		List<Object[]> busList = busRepo.getAvailableBusesForRota(depot.getId(), tpId, rotaDate);
		List<BusMasterDto> busDtoList = new ArrayList<>();
		for(Object[] o : busList) {
			BusMasterDto dto = new BusMasterDto();
			dto.setId(Integer.parseInt(o[0].toString()));
			dto.setBusRegNumber(o[1].toString());
			dto.setAvailableTime(LocalTime.parse(o[2].toString()));
			CityDto city = new CityDto();
			if(o[3]!=null) {
				city.setId(Integer.parseInt(o[3].toString()));
			}
			dto.setAvailableCity(city);
			busDtoList.add(dto);
		}
		form.setBusList(busDtoList);
		return form;
	}

	@Override
	public List<TransportDto> getTransportUnits(String dpCode) {
		List<TransportDto> list = new ArrayList<>();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(dpCode);
			list = tpuRepo.allTransportMasterByDepot(depot.getId()).stream()
					.map(tpu -> new TransportDto(tpu.getTransportUnitMaster().getId(), tpu.getTransportUnitMaster().getTransportName())).collect(Collectors.toList());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveDailyRoaster(RoasterDto roasterDto) {
		try {
			Roaster roaster = roasterRepo.findByDate(roasterDto.getDepot().getId(), roasterDto.getTransport().getId());
			DepotMaster depot = depotRepo.findById(roasterDto.getDepot().getId()).get();
			Calendar c = Calendar.getInstance(); 
			if(roaster==null) {
				roaster = new Roaster();
				TransportUnitMaster tu = new TransportUnitMaster();
				tu.setId(roasterDto.getTransport().getId());
				roaster.setDepot(depot);
				roaster.setTransport(tu);
				roaster.setGenerationDate(new Date());
				roaster.setIsNormalRota(roasterDto.getIsNormalRota());
				roaster.setIsSpecialRota(roasterDto.getIsSpecialRota());
				Date dt = new Date();
				c.setTime(dt); 
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				roaster.setRotaDate(this.getDate(1, new Date()));
				roaster.setRoastedCode(roasterDto.getDepot().getId()+"/"+roasterDto.getTransport().getId()+"/"+sdf.format(dt));
//				roaster = roasterRepo.save(roaster);
			} else {
					return new ResponseEntity<>(
							new ResponseStatus("Roaster already generated for "+roaster.getRotaDate() + " .", HttpStatus.OK),
							HttpStatus.OK);
			}
			if(roasterDto.getDailyRoasterList()==null)
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			
			List<DailyRoaster> drList = new ArrayList<>();
			for(DailyRoasterDto drDto : roasterDto.getDailyRoasterList()) {
				
				TripMaster trip = tripRepo.findById(drDto.getTrip().getId()).get();
				DailyRoaster rota = new DailyRoaster();
				
				if(drDto.getBus()!=null && drDto.getBus().getId()!=0) {
					BusMaster bus = new BusMaster();
					bus.setId(drDto.getBus().getId());
					rota.setBus(bus);
					
					BusRotaHistory brh = new BusRotaHistory();
					brh.setBus(bus);
					brh.setDepot(depot);
					brh.setUpDown(trip.getUpDown());
					brh.setTripStatus("P");
					brh.setTrip(trip);
					brh.setRoaster(rota);
					brh.setIsDeleted(false);
//					if(trip.getUpDown().equals("DOWN")) {
//						brh.setReturnTime(trip.getTripEndTime());
//						if(trip.getTotalNightsMaster().getNightDetail().equals("N + 0")) {
//							brh.setReturnDate(this.getDate(1, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 1")) {
//							brh.setReturnDate(this.getDate(2, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 2")) {
//							brh.setReturnDate(this.getDate(3, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 3")) {
//							brh.setReturnDate(this.getDate(4, new Date()));
//						}
//					} 
					List<BusRotaHistory> brhList = new ArrayList<>();
					brhList.add(brh);
					rota.setBusRota(brhList);
				}
				
				
				if(drDto.getDriver()!=null && drDto.getDriver().getId()!=0) {
					DriverMaster driver = new DriverMaster();
					driver.setId(drDto.getDriver().getId());
					rota.setDriver(driver);
					
					DriverRotaHistory drh = new DriverRotaHistory();
					drh.setDriver(driver);
					drh.setDepot(depot);
					drh.setUpDown(trip.getUpDown());
					drh.setTripStatus("P");
					drh.setTrip(trip);
					drh.setRoaster(rota);
					drh.setIsDeleted(false);
//					if(trip.getUpDown().equals("DOWN")) {
//						drh.setReturnTime(trip.getTripEndTime());
//						if(trip.getTotalNightsMaster().getNightDetail().equals("N + 0")) {
//							drh.setReturnDate(this.getDate(1, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 1")) {
//							drh.setReturnDate(this.getDate(2, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 2")) {
//							drh.setReturnDate(this.getDate(3, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 3")) {
//							drh.setReturnDate(this.getDate(4, new Date()));
//						}
//					} 
					
					List<DriverRotaHistory> drhList = new ArrayList<>();
					drhList.add(drh);
					rota.setDriverRota(drhList);
				}
				
				if(drDto.getConductor()!=null && drDto.getConductor().getId()!=0) {
					ConductorMaster conductor = new ConductorMaster();
					conductor.setId(drDto.getConductor().getId());
					rota.setConductor(conductor);
					
					ConductorRotaHistory crh = new ConductorRotaHistory();
					crh.setConductor(conductor);
					crh.setDepot(depot);
					crh.setUpDown(trip.getUpDown());
					crh.setTripStatus("P");
					crh.setTrip(trip);
					crh.setRoaster(rota);
					crh.setIsDeleted(false);
//					if(trip.getUpDown().equals("DOWN")) {
//						crh.setReturnTime(trip.getTripEndTime());
//						if(trip.getTotalNightsMaster().getNightDetail().equals("N + 0")) {
//							crh.setReturnDate(this.getDate(1, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 1")) {
//							crh.setReturnDate(this.getDate(2, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 2")) {
//							crh.setReturnDate(this.getDate(3, new Date()));
//						} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 3")) {
//							crh.setReturnDate(this.getDate(4, new Date()));
//						}
//					} 
					
					List<ConductorRotaHistory> crhList = new ArrayList<>();
					crhList.add(crh);
					rota.setConductorRota(crhList);
				}
				
				rota.setRota(roaster);
				rota.setTripStatus(false);
				RouteMaster route = new RouteMaster();
				route.setId(drDto.getRoute().getId());
				rota.setRoute(route);
				rota.setIsDeleted(false);
				RouteTypeMaster routeType = new RouteTypeMaster();
				routeType.setId(drDto.getRouteType().getId());

				rota.setRouteType(routeType);
				rota.setTrip(trip);
				rota.setRota(roaster);
				drList.add(rota);
				
			}
			roaster.setDailyRoasterList(drList);
			roaster = roasterRepo.save(roaster);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(
				new ResponseStatus("Roaster Data has been persisted successfully.", HttpStatus.OK),
				HttpStatus.OK);
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
	public List<RoasterDto> getGeneratedRoasterList(String dpCode) {
		List<RoasterDto> rlist = new ArrayList<RoasterDto>();
		try {
			List<Object[]> list = roasterRepo.getGeneratedRoasterList(dpCode);
			Object[] transportArray = list.get(0);
			String id = transportArray[6].toString();
			Integer transportId = Integer.parseInt(id);
			DepotMaster depotObj = depotRepo.findByDepotCode(dpCode);
			Integer scheduledKms = rmRepo.getAllScheduledKmsByDepo(depotObj.getId(),transportId);
			Integer deadKms = rmRepo.getAllDeadKmsByDepo(depotObj.getId(),transportId);
			Integer fleetStrength = busRepo.getAllBusesCountByDepotAndTu(depotObj.getId(),transportId);
			for(Object[] o : list) {
				RoasterDto dto = new RoasterDto();
				dto.setId(Integer.parseInt(o[0].toString()));
				dto.setRotaDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o[1].toString()));
				dto.setRoastedCode(o[2].toString());
				
				DepotMasterDto depot = new DepotMasterDto();
				depot.setDepotName(o[3].toString());
				dto.setDepot(depot);
				
				if(o[7] != null){
					dto.setApprovalStatus(o[7].toString());
				}
				
				TransportDto tp = new TransportDto();
				tp.setTransportName(o[4].toString());
				dto.setTransport(tp);
				
				dto.setIsEdit(Boolean.parseBoolean(o[5].toString()));
				dto.setScheduledKms(scheduledKms);
				dto.setFleetStrength(fleetStrength);
				dto.setDeadKms(deadKms);
				rlist.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return rlist;
	}

	/*@Override
	public List<DailyRoasterDto> getGeneratedRoasterDetail(Integer rotaId) {
		//RoasterDto roaster = new RoasterDto();
		List<Object[]> list = null;
		List<DailyRoasterDto> rList = new ArrayList<>();
		try {
			Roaster rota = roasterRepo.findById(rotaId).get();
			if(rota.getId()==null) {
				return roaster;
			}
			roaster.setId(rota.getId());
			
			DepotMasterDto depot = new DepotMasterDto();
			//depot.setDepotName(rota.getDepot().getDepotName());
			//roaster.setDepot(depot);
			
			TransportDto tp = new TransportDto();
			//tp.setId(rota.getTransport().getId());
			//tp.setTransportName(rota.getTransport().getTransportName());
			//roaster.setTransport(tp);
			
			//roaster.setRotaDate(rota.getRotaDate());
			//roaster.setRoastedCode(rota.getRoastedCode());
			
			list = roasterRepo.getGeneratedRoasterDetail(rotaId);
			if(list.size()>0) {
				for(Object[] o : list) {
					DailyRoasterDto dto = new DailyRoasterDto();
					
					BusMasterDto bus = new BusMasterDto();
					if(o[0]!=null) {
						bus.setId(Integer.parseInt(o[9].toString()));
						bus.setBusRegNumber(o[0].toString());
					}
					else {
						bus.setId(0);
						bus.setBusRegNumber("----");
					}
					dto.setBus(bus);
					
					DriverMasterDto driver = new DriverMasterDto();
					if(o[1]!=null) {
						driver.setDriverName(o[1].toString());
						driver.setId(Integer.parseInt(o[10].toString()));
					}
					else {
						driver.setId(0);
						driver.setDriverName("----");
					}
					dto.setDriver(driver);
					
					ConductorMasterDto conductor = new ConductorMasterDto();
					if(o[2]!=null) {
						conductor.setConductorName(o[2].toString());
						conductor.setId(Integer.parseInt(o[11].toString()));
					}
					else {
						conductor.setId(0);
						conductor.setConductorName("----");
					}
					dto.setConductor(conductor);
					
					TripMasterDto trip = new TripMasterDto();
					
					CityDto fromCity = new CityDto();
					fromCity.setId(Integer.parseInt(o[14].toString()));
					fromCity.setCityName(o[3].toString());
					trip.setFromCity(fromCity);
					
					CityDto toCity = new CityDto();
					toCity.setId(Integer.parseInt(o[15].toString()));
					toCity.setCityName(o[4].toString());
					trip.setToCity(toCity);
					trip.setId(Integer.parseInt(o[12].toString()));
					trip.setUpDown(o[13].toString());
					trip.setTripStartTime(o[5].toString());
					trip.setTripEndTime(o[6].toString());
					trip.setScheduledKms(Integer.parseInt(o[7].toString()));
					dto.setTrip(trip);
					dto.setId(Integer.parseInt(o[8].toString()));
					rList.add(dto);
				}
			}
			//roaster.setDailyRoasterList(rList);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return rList;
	}*/

	@Override
	public ResponseEntity<ResponseStatus> deleteDailyRoasterId(Integer dailyRotaId) {
		try {
			DailyRoaster dr = dailyRotaRepo.findById(dailyRotaId).get();
			if(dr!=null) {
				dr.setIsDeleted(true);
				for(BusRotaHistory brh : dr.getBusRota()) {
					brh.setIsDeleted(true);
				}
				for(DriverRotaHistory drh : dr.getDriverRota()) {
					drh.setIsDeleted(true);
				}
				for(ConductorRotaHistory crh : dr.getConductorRota()) {
					crh.setIsDeleted(true);
				}
				dailyRotaRepo.save(dr);
				DailyRoaster dr1 = new DailyRoaster();
				dr1.setTrip(dr.getTrip());
				dr1.setRota(dr.getRota());
				dr1.setRoute(dr.getRoute());
				dr1.setIsDeleted(false);
				dr1.setRouteType(dr.getRouteType());
				dr1.setTripStatus(false);
				dailyRotaRepo.save(dr1);
			} else {
				return new ResponseEntity<>(
						new ResponseStatus("Wrong roaster id.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(
				new ResponseStatus("Roaster Data has been deleted successfully.", HttpStatus.OK),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStatus> saveSingleDailyRoaster(RoasterDto roasterDto) {
		Roaster roaster = null;
		try {
			if(roasterDto!=null && roasterDto.getId()!=null) {
				roaster = roasterRepo.findById(roasterDto.getId()).get();
				if(roaster==null) {
					return new ResponseEntity<>(
							new ResponseStatus("Provide valid roaster Id.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				
				List<DailyRoaster> drList = new ArrayList<>();
				for(DailyRoasterDto drDto : roasterDto.getDailyRoasterList()) {
					
					DailyRoaster rota = dailyRotaRepo.findById(drDto.getId()).get();
					TripMaster trip = tripRepo.findById(rota.getTrip().getId()).get();
					if(drDto.getBus()!=null && drDto.getBus().getId()!=0) {
						BusMaster bus = new BusMaster();
						bus.setId(drDto.getBus().getId());
						rota.setBus(bus);					
						BusRotaHistory brh = new BusRotaHistory();
						brh.setBus(bus);
						brh.setDepot(roaster.getDepot());
						brh.setUpDown(trip.getUpDown());
						brh.setTripStatus("P");
						brh.setTrip(trip);
						brh.setRoaster(rota);
						brh.setIsDeleted(false);
//						if(trip.getUpDown().equals("DOWN")) {
//							brh.setReturnTime(trip.getTripEndTime());
//							if(trip.getTotalNightsMaster().getNightDetail().equals("N + 0")) {
//								brh.setReturnDate(this.getDate(0, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 1")) {
//								brh.setReturnDate(this.getDate(1, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 2")) {
//								brh.setReturnDate(this.getDate(2, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 3")) {
//								brh.setReturnDate(this.getDate(3, roaster.getRotaDate()));
//							}
//						} 
						List<BusRotaHistory> brhList = new ArrayList<>();
						brhList.add(brh);
						rota.setBusRota(brhList);
					}
					if(drDto.getDriver()!=null && drDto.getDriver().getId()!=0) {
						DriverMaster driver = new DriverMaster();
						driver.setId(drDto.getDriver().getId());
						rota.setDriver(driver);
						
						DriverRotaHistory drh = new DriverRotaHistory();
						drh.setDriver(driver);
						drh.setDepot(roaster.getDepot());
						drh.setUpDown(trip.getUpDown());
						drh.setTripStatus("P");
						drh.setTrip(trip);
						drh.setRoaster(rota);
						drh.setIsDeleted(false);
//						if(trip.getUpDown().equals("DOWN")) {
//							drh.setReturnTime(trip.getTripEndTime());
//							if(trip.getTotalNightsMaster().getNightDetail().equals("N + 0")) {
//								drh.setReturnDate(this.getDate(1, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 1")) {
//								drh.setReturnDate(this.getDate(2, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 2")) {
//								drh.setReturnDate(this.getDate(3, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 3")) {
//								drh.setReturnDate(this.getDate(4, roaster.getRotaDate()));
//							}
//						} 
						
						List<DriverRotaHistory> drhList = new ArrayList<>();
						drhList.add(drh);
						rota.setDriverRota(drhList);
					}
					
					if(drDto.getConductor()!=null && drDto.getConductor().getId()!=0) {
						ConductorMaster conductor = new ConductorMaster();
						conductor.setId(drDto.getConductor().getId());
						rota.setConductor(conductor);
						
						ConductorRotaHistory crh = new ConductorRotaHistory();
						crh.setConductor(conductor);
						crh.setDepot(roaster.getDepot());
						crh.setUpDown(trip.getUpDown());
						crh.setTripStatus("P");
						crh.setTrip(trip);
						crh.setRoaster(rota);
						crh.setIsDeleted(false);
//						if(trip.getUpDown().equals("DOWN")) {
//							crh.setReturnTime(trip.getTripEndTime());
//							if(trip.getTotalNightsMaster().getNightDetail().equals("N + 0")) {
//								crh.setReturnDate(this.getDate(1, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 1")) {
//								crh.setReturnDate(this.getDate(2, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 2")) {
//								crh.setReturnDate(this.getDate(3, roaster.getRotaDate()));
//							} else if(trip.getTotalNightsMaster().getNightDetail().equals("N + 3")) {
//								crh.setReturnDate(this.getDate(4, roaster.getRotaDate()));
//							}
//						} 
						
						List<ConductorRotaHistory> crhList = new ArrayList<>();
						crhList.add(crh);
						rota.setConductorRota(crhList);
						rota.setIsDeleted(false);
					}
					dailyRotaRepo.save(rota);
				}
				
			} else {
				return new ResponseEntity<>(
						new ResponseStatus("Id is mandatory.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(
				new ResponseStatus("Roaster Data has been persisted successfully.", HttpStatus.OK),
				HttpStatus.OK);
	}
	
	@Override
//	public AllRotaListViewDto getGeneratedRoasterDetail(Integer rotaId) {
//		List<DailyRoasterAutoDto> list = new ArrayList<>();
//		List<DailyRoasterAutoDto> specialList = new ArrayList<>();
//		List<DailyRoasterAutoDto> manualList = new ArrayList<>();
//		DailyRoasterAutoDto obj = null;
//		Map<Integer,String> hashMap = new HashMap<>();
//		AllRotaListViewDto allRotaListObj = new AllRotaListViewDto();
//		List<DailyRoaster> rotaList = dailyRotaRepo.findAllByRotaId(rotaId);
//		//Integer totalKms = 0;
//		for(DailyRoaster tempObj : rotaList){
//			obj = new DailyRoasterAutoDto();
//			if(tempObj.getTrip() != null && (!hashMap.isEmpty()) && (!hashMap.containsKey(tempObj.getTrip().getId()))){
//				hashMap = new HashMap<>();
//			}
//			if(tempObj.getTrip() != null && hashMap.isEmpty()){
//			hashMap.put(tempObj.getTrip().getId(), "");
//			//parentObj = new DailyRoasterParentDto();
//			if(tempObj.getRoute() != null)
//			obj.setRouteId(tempObj.getRoute().getRouteId());
//			if(tempObj.getRoute() != null)
//			obj.setRouteName(tempObj.getRoute().getRouteName());
//			if(tempObj.getTrip() != null)
//			obj.setTripServiceId(tempObj.getTrip().getServiceId());
//			if(tempObj.getTrip() != null && tempObj.getTrip().getScheduledKms() != null){
//				obj.setScheduledKms(tempObj.getTrip().getScheduledKms());
//			}
//			if(tempObj != null && tempObj.getTrip() != null && tempObj.getTrip().getServiceId() != null)
//				obj.setServiceId(tempObj.getTrip().getServiceId());
//			}
//			obj.setTripId(tempObj.getTrip().getId());
//			obj.setRotaId(tempObj.getId());
//			if(tempObj.getBus() != null)
//			obj.setBusModel(tempObj.getBus().getBusModel());
//			if(tempObj.getConductor() != null && tempObj.getConductor().getConductorCategory() != null)
//			obj.setConductorCategory(tempObj.getConductor().getConductorCategory().getRouteCategoryName());
//			if(tempObj.getConductor() != null)
//			obj.setConductorCode(tempObj.getConductor().getConductorCode());
//			if(tempObj.getConductor() != null)
//			obj.setConductorName(tempObj.getConductor().getConductorName());
//			if(tempObj.getDriver() != null && tempObj.getDriver().getDriverCategory() != null) 
//			obj.setDriverCategory(tempObj.getDriver().getDriverCategory().getRouteCategoryName());
//			if(tempObj.getDriver() != null)
//			obj.setDriverCode(tempObj.getDriver().getDriverCode());
//			if(tempObj.getDriver() != null && tempObj.getDriver().getLicenceValidity() != null)
//			obj.setDriverLiscenseValidity(tempObj.getDriver().getLicenceValidity().toString());
//			if(tempObj.getBus() != null && tempObj.getBus().getPrimaryDriver() != null)
//			obj.setPrimaryDriver(tempObj.getBus().getPrimaryDriver().getDriverCode());
//			if(tempObj.getRoute() != null && tempObj.getRoute().getRouteCategoryMaster() != null)
//			obj.setRouteCategory(tempObj.getRoute().getRouteCategoryMaster().getRouteCategoryName());
//			//if(tempObj.getRoute() != null)
//			//obj.setRouteId(tempObj.getRoute().getRouteId());
//			//if(tempObj.getRoute() != null)
//			//obj.setRouteName(tempObj.getRoute().getRouteName());
//			if(tempObj.getBus() != null && tempObj.getBus().getSecondaryDriver() != null)
//			obj.setSecondryDriver(tempObj.getBus().getSecondaryDriver().getDriverCode());
//			//if(tempObj.getTrip() != null)
//			//obj.setTripServiceId(tempObj.getTrip().getServiceId());
//			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getStartTime() != null)
//				obj.setTripStartTime(tempObj.getTripRotation().getStartTime().toString());
//				if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getEndTime() != null)
//				obj.setTripEndTime(tempObj.getTripRotation().getEndTime().toString());
//			if(tempObj.getDriver() != null)
//			obj.setDriverName(tempObj.getDriver().getDriverName());
//			if(tempObj.getBus() != null)
//			obj.setBusNumber(tempObj.getBus().getBusRegNumber());
//			//if(tempObj.getTrip() != null && tempObj.getTrip().getScheduledKms() != null){
//				//obj.setScheduledKms(tempObj.getTrip().getScheduledKms());
//				//totalKms = totalKms + tempObj.getTrip().getScheduledKms();
//			//}
//			if(tempObj.getSuggestion() != null){
//				obj.setSuggestion(tempObj.getSuggestion());
//			}
//			if(tempObj.getRotationAvailabilityDate() != null){
//				obj.setAvailabilityDate(tempObj.getRotationAvailabilityDate().toString());
//			}
//			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getFromState() != null){
//				obj.setFromState(tempObj.getTripRotation().getRouteRotation().getFromState().getStateName());
//			}
//			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getToState() != null){
//				obj.setToState(tempObj.getTripRotation().getRouteRotation().getToState().getStateName());
//			}
//			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getFromCity() != null){
//				obj.setFromCity(tempObj.getTripRotation().getRouteRotation().getFromCity().getCityName());
//			}
//			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getToCity() != null){
//				obj.setToCity(tempObj.getTripRotation().getRouteRotation().getToCity().getCityName());
//			}
//			
//			if(tempObj != null && tempObj.getDriver() != null && tempObj.getDriver().getMobileNumber() != null)
//				obj.setDriverContactNo(tempObj.getDriver().getMobileNumber());
//			
//			if(tempObj != null && tempObj.getConductor() != null && tempObj.getConductor().getMobileNumber() != null)
//				obj.setConductorContactNo(tempObj.getConductor().getMobileNumber());
//			//if(tempObj != null && tempObj.getTrip() != null && tempObj.getTrip().getServiceId() != null)
//				//obj.setServiceId(tempObj.getTrip().getServiceId());
//			if(tempObj != null && tempObj.getTripRotation() != null && tempObj.getTripRotation().getDays() != null)
//				obj.setDays(tempObj.getTripRotation().getDays().getNightDetail());
//			if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Auto")){
//				list.add(obj);
//			}if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Special")){
//				specialList.add(obj);
//			}if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Normal")){
//				manualList.add(obj);
//			}
//	
//		}
//			allRotaListObj.setAutoList(list);
//			allRotaListObj.setSpecialList(specialList);
//			allRotaListObj.setManualList(manualList);
//		
//			//obj.setTotalScheduledKms(totalKms);
//		return allRotaListObj;
//	}
	public AllRotaListViewDto getGeneratedRoasterDetail(Integer rotaId) {
		List<DailyRoasterAutoDto> list = new ArrayList<>();
		List<DailyRoasterAutoDto> specialList = new ArrayList<>();
		List<DailyRoasterAutoDto> manualList = new ArrayList<>();			
		DailyRoasterAutoDto obj = null;
		AllRotaListViewDto allRotaListObj = new AllRotaListViewDto();
		Integer totalKms = 0;
		Integer totalKmsByRota = 0;
		//List<DailyRoaster> rotaList = new ArrayList<>();
		totalKmsByRota = rmRepo.getAllScheduledKmsByRotaId(rotaId);
		List<DailyRoaster> rotaList = dailyRotaRepo.findAllByRotaId(rotaId);
		//List<DailyRoaster> rotaListNew = dailyRotaRepo.findAllByRotaId(rotaId);
		Calendar c = Calendar.getInstance(); 
		Date today = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    String s = df.format(today);
	    String result = s;
	    try {
	    	today = df.parse(result);
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		//List<DailyRoaster> rotaCompletionList = dailyRotaRepo.findAllCompletingSameDay(rotaId,today);
		//rotaList.addAll(rotaListNew);
		//rotaList.addAll(rotaCompletionList);
		
		int i =0;
		int j = 0; 
		int k = 0;
		Map<Integer,String> hashMap = new HashMap<>();
		for(DailyRoaster tempObj : rotaList){
			obj = new DailyRoasterAutoDto();
			if(tempObj.getTrip() != null && (!hashMap.isEmpty()) && (!hashMap.containsKey(tempObj.getTrip().getId()))){
				hashMap = new HashMap<>();
			}
			if(tempObj.getTrip() != null && hashMap.isEmpty()){
			hashMap.put(tempObj.getTrip().getId(), "");
			if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Auto")){
				i++;
				obj.setCounter(i);
			}if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Special")){
				j++;
				obj.setSpecialCounter(j);
			}if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Normal")){
				k++;
				obj.setManualCounter(k);
				
			}
			}
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
			if(tempObj.getRoute() != null)
			obj.setRouteId(tempObj.getRoute().getRouteCode());
			if(tempObj.getRoute() != null)
			obj.setRouteName(tempObj.getRoute().getRouteName());
			if(tempObj.getBus() != null && tempObj.getBus().getSecondaryDriver() != null)
			obj.setSecondryDriver(tempObj.getBus().getSecondaryDriver().getDriverCode());
			if(tempObj.getTrip() != null)
			obj.setTripServiceId(tempObj.getTrip().getServiceId());
			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getStartTime() != null)
				obj.setTripStartTime(tempObj.getTripRotation().getStartTime().toString());
				if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getEndTime() != null)
				obj.setTripEndTime(tempObj.getTripRotation().getEndTime().toString());
			if(tempObj.getDriver() != null)
			obj.setDriverName(tempObj.getDriver().getDriverName());
			if(tempObj.getBus() != null)
			obj.setBusNumber(tempObj.getBus().getBusRegNumber());
			if(tempObj.getRoute() != null && tempObj.getRoute().getScheduledKms() != null && tempObj.getRoute().getTripType() != null){
				totalKms = tempObj.getRoute().getScheduledKms() * tempObj.getRoute().getTripType().getTripValue();
				obj.setScheduledKms(totalKms);
			}
			//obj.setScheduledKms(totalKms);
			if(tempObj.getSuggestion() != null){
				obj.setSuggestion(tempObj.getSuggestion());
			}
			if(tempObj.getRotationAvailabilityDate() != null){
				obj.setAvailabilityDate(tempObj.getRotationAvailabilityDate().toString());
			}
			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getFromState() != null){
				obj.setFromState(tempObj.getTripRotation().getRouteRotation().getFromState().getStateName());
			}
			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getToState() != null){
				obj.setToState(tempObj.getTripRotation().getRouteRotation().getToState().getStateName());
			}
			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getFromCity() != null){
				obj.setFromCity(tempObj.getTripRotation().getRouteRotation().getFromCity().getCityName());
			}
			if(tempObj.getTripRotation() != null && tempObj.getTripRotation().getRouteRotation() != null && tempObj.getTripRotation().getRouteRotation().getToCity() != null){
				obj.setToCity(tempObj.getTripRotation().getRouteRotation().getToCity().getCityName());
			}
			
			if(tempObj != null && tempObj.getDriver() != null && tempObj.getDriver().getMobileNumber() != null)
				obj.setDriverContactNo(tempObj.getDriver().getMobileNumber());
			
			if(tempObj != null && tempObj.getConductor() != null && tempObj.getConductor().getMobileNumber() != null)
				obj.setConductorContactNo(tempObj.getConductor().getMobileNumber());
			if(tempObj != null && tempObj.getTrip() != null && tempObj.getTrip().getServiceId() != null)
				obj.setServiceId(tempObj.getTrip().getServiceId());
			if(tempObj != null && tempObj.getTripRotation() != null && tempObj.getTripRotation().getDays() != null)
				obj.setDays(tempObj.getTripRotation().getDays().getNightDetail());
			if(tempObj != null){
				obj.setRotaId(tempObj.getId());
			}
			if(tempObj != null && tempObj.getRemarks() != null){
				obj.setRemarks(tempObj.getRemarks());
			}
			
			if(tempObj.getRoute() != null)
				obj.setRoutePk(tempObj.getRoute().getId());
			if(tempObj.getRoute().getTransport() != null)
				obj.setTransportId(tempObj.getRoute().getTransport().getId());
			
			if(tempObj.getTrip() != null)
				obj.setTripId(tempObj.getTrip().getId());
			
			if(tempObj.getOverrideReason() != null)
				obj.setOverrideReason(tempObj.getOverrideReason());
			
			if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Auto")){
				list.add(obj);
			}if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Special")){
				specialList.add(obj);
			}if(tempObj != null && tempObj.getRotaTypeFlag() != null && tempObj.getRotaTypeFlag().equals("Normal")){
				manualList.add(obj);
			}
			allRotaListObj.setScheduledKms(totalKmsByRota);
			allRotaListObj.setAutoList(list);
			allRotaListObj.setSpecialList(specialList);
			allRotaListObj.setManualList(manualList);
			
		}
		//added by manisha 
		List<MarkSpareListDto> spareList = new ArrayList<>();		
		MarkSpareListDto obj2 = new MarkSpareListDto();
		Roaster roasterObj = dailyRotaRepo.findByRotaId(rotaId);				
		List<Object[]> objList = dailyRotaRepo.spareList(roasterObj.getRotaDate());
			for(Object[] temp : objList){
				obj2=new MarkSpareListDto();
				if(temp[0]!=null)
				obj2.setSpareName(temp[0].toString());					
				if(temp[1]!=null)
				obj2.setSparetype(temp[1].toString());
				if(temp[2]!=null)
				obj2.setRemarks(temp[2].toString());
				spareList.add(obj2);
			}	
			allRotaListObj.setSpareList(spareList);
			//obj.setTotalScheduledKms(totalKms);
		return allRotaListObj;
	}

}
