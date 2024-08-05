package com.idms.base.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.BusStandDto;
import com.idms.base.api.v1.model.dto.BusStandWrapperDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.PermitDetailsDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.RouteComplexityDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteRotationDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.StateWiseRouteKmsDto;
import com.idms.base.api.v1.model.dto.StopageDto;
import com.idms.base.api.v1.model.dto.StopageTypeDto;
import com.idms.base.api.v1.model.dto.StopagesMasterDto;
import com.idms.base.api.v1.model.dto.TollTaxWrapperDto;
import com.idms.base.api.v1.model.dto.TotalNightsDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TripDepartureDaysDto;
import com.idms.base.api.v1.model.dto.TripFormLoadDto;
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.api.v1.model.dto.TripRotationDto;
import com.idms.base.dao.entity.BusStandMaster;
import com.idms.base.dao.entity.BusStandWrapper;
import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.entity.RouteCategoryMaster;
import com.idms.base.dao.entity.RouteComplexityMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.entity.RoutePermitMaster;
import com.idms.base.dao.entity.RouteRotation;
import com.idms.base.dao.entity.RouteTypeMaster;
import com.idms.base.dao.entity.StateMaster;
import com.idms.base.dao.entity.StopageEntity;
import com.idms.base.dao.entity.StopageTypeMaster;
import com.idms.base.dao.entity.StopagesMaster;
import com.idms.base.dao.entity.TollTaxWrapper;
import com.idms.base.dao.entity.TotalNightsMaster;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TripDepartureAndDaysEntity;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.entity.TripRotationEntity;
import com.idms.base.dao.repository.BusStandMasterRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.PermitDetailsMasterRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.RouteRotationRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.StopTypeMasterRepository;
import com.idms.base.dao.repository.StopageEntityRepository;
import com.idms.base.dao.repository.StopagesMasterRepository;
import com.idms.base.dao.repository.TotalNightsRepository;
import com.idms.base.dao.repository.TripDepartureDaysRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.dao.repository.TripRotationReposiotry;
import com.idms.base.service.TripMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TripMasterServiceImpl implements TripMasterService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	TotalNightsRepository totalNightsRepository;
	
	@Autowired
	CityMasterRepository cityMasterRepository;
	
	@Autowired
	StopTypeMasterRepository stopTypeMasterRepository;
	
	@Autowired
	BusStandMasterRepository busStandMasterRepository;
	
	@Autowired
	RouteMasterRepository routeMasterRepository;
	
	@Autowired
	TripMasterRepository tripMasterRepository;
	
	@Autowired
	StopageEntityRepository stopageEntityRepo;
	
	@Autowired
	PermitDetailsMasterRepository permitDetailsMasterRepository;
	
	@Autowired
	RouteRotationRepository routeRotationRepository;
	
	@Autowired
	StopagesMasterRepository stopagesMasterRepository;
	
	@Autowired
	TripRotationReposiotry tripRotationReposiotry;
	
	@Autowired
	StateMasterRepository stateMasterReposiotry;
	
	@Autowired
	TripDepartureDaysRepository tripDepartureDaysRepository;
	

	@Override
	public TripFormLoadDto tripMasterOnLoad(String dpCode) {
		log.info("Entering into tripMasterOnLoad service");
		TripFormLoadDto tripFormLoadDto = new TripFormLoadDto();
		try {

			List<TotalNightsDto> totalNightsList = totalNightsRepository.findAllByStatus(true).stream()
					.map(totalNights -> new TotalNightsDto(totalNights.getId(), totalNights.getNightDetail()))
					.collect(Collectors.toList());
			if (totalNightsList != null && totalNightsList.size() > 0)
				tripFormLoadDto.setTotalNightsList(totalNightsList);
			
			List<CityDto> citiesList = cityMasterRepository.findAllByStatus(true).stream()
					.map(city -> new CityDto(city.getId(), city.getCityName()))
					.collect(Collectors.toList());
			if (citiesList != null && citiesList.size() > 0)
				tripFormLoadDto.setCityList(citiesList);
			
			List<StopageDto> stopagesList = stopageEntityRepo.findAllByStatus(true).stream()
					.map(stopage -> new StopageDto(stopage.getId(), stopage.getStopageName()))
					.collect(Collectors.toList());
			
			if(stopagesList!=null && stopagesList.size() > 0) {
				tripFormLoadDto.setStopageList(stopagesList);
			}
			
			List<StopageTypeDto> stopageList = stopTypeMasterRepository.findAllByStatus(true).stream()
					.map(stop -> new StopageTypeDto(stop.getId(), stop.getStopTypeName()))
					.collect(Collectors.toList());
			if (stopageList != null && stopageList.size() > 0)
				tripFormLoadDto.setStopageTypeList(stopageList);
			
			List<BusStandDto> busStandList = busStandMasterRepository.findAllByStatus(true).stream()
					.map(busStand -> new BusStandDto(busStand.getId(), busStand.getBusStandName()))
					.collect(Collectors.toList());
			if (busStandList != null && busStandList.size() > 0)
				tripFormLoadDto.setBusStandList(busStandList);
			
			List<RouteMasterDto> routeList = routeMasterRepository.getAllRouteMasterByDepotAndStatus(dpCode).stream()
					.map(route -> new RouteMasterDto(route.getId(), route.getRouteName()))
					.collect(Collectors.toList());
			if (routeList != null && routeList.size() > 0)
				tripFormLoadDto.setRouteList(routeList);;
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tripFormLoadDto;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateTripMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateTripMasterStatusFlag service");
		List<TripRotationEntity> rotations = tripRotationReposiotry.findByTripMaster(id);

		try {
			boolean permit = false;
			if (!flag) {
				Map<Integer, Long> uniqueId = rotations.stream()
						.collect(Collectors.groupingBy(p -> p.getPermitMaster().getId(), Collectors.counting()));
				for (Map.Entry<Integer, Long> entry : uniqueId.entrySet()) {
					Optional<PermitDetailsMaster> permitMaster = permitDetailsMasterRepository.findById(entry.getKey());
					if (entry.getValue() <= (permitMaster.get().getTotalTrips() * 2)) {
						int permitCount = tripRotationReposiotry.countOfPermit(entry.getKey(), id);
						int diff = (permitMaster.get().getTotalTrips() * 2) - permitCount;
						if (entry.getValue() <= diff) {
							permit = true;
						} else {
							permit = false;
						}
					} else {
						permit = false;

					}
				}
			}
			if (flag || permit) {
				int i = tripMasterRepository.updateTripMasterStatusFlag(flag, id);
				if (i == 1)
					return new ResponseEntity<>(
							new ResponseStatus("Status has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
				else
					return new ResponseEntity<>(
							new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
			}
			return new ResponseEntity<>(new ResponseStatus("Current Trip Permits have been used in other trips",
					HttpStatus.FORBIDDEN), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<TripMasterDto> getAllTripMaster(String dpCode) {
		log.info("Entering into getAllRouteMaster service");
		List<TripMaster> tripMasterList = null;
		List<TripMasterDto> tmd = new ArrayList<TripMasterDto>();
		
		try {
			
			tripMasterList = tripMasterRepository.findAllByDepot(dpCode);
			for (TripMaster tmle : tripMasterList) {
				TripMasterDto trmd = new TripMasterDto();
				trmd.setId(tmle.getId());
				trmd.setDeadKms(tmle.getDeadKms());
				trmd.setScheduledKms(tmle.getScheduledKms());
				trmd.setServiceId(tmle.getServiceId());
				trmd.setTripStartTime(tmle.getTripStartTime().toString());
				trmd.setTripEndTime(tmle.getTripEndTime().toString());
				trmd.setStatus(tmle.getStatus());
				
				if (null != tmle.getTotalOt()) {
					trmd.setTotalOt(tmle.getTotalOt().toString());
				} else {
					trmd.setTotalOt("-");
				}
				if (tmle.getRouteMaster() !=null) {
					Optional<RouteMaster> rm = routeMasterRepository.findById(tmle.getRouteMaster().getId());
					if(rm.isPresent()){
						trmd.setRouteName(rm.get().getRouteName());
						
						}
				} 
				
				
				
				
				List<TripRotationEntity> treList = tripRotationReposiotry.getReocrdByTripId(tmle.getId());
				if (treList.size() !=0) {
					List<TripRotationDto> trdList = new ArrayList<TripRotationDto>();
					treList.forEach(action->{
						TripRotationDto trd = new TripRotationDto();
						trd.setId(action.getId());
						if (null != action.getStartTime()) {
							trd.setStartTime(action.getStartTime().toString());
						}else{
							trd.setStartTime("-");
						}
						if (null != action.getEndTime()) {
							trd.setEndTime(action.getEndTime().toString());
						}else{
							trd.setEndTime("-");
						}
						if (null != action.getNightHalt()) {
							trd.setNightHalt(action.getNightHalt());
						} 
						
						if (action.getPermitMaster()!=null) {
							PermitDetailsMaster pm = permitDetailsMasterRepository.findById(action.getPermitMaster().getId()).get();
							if (pm !=null) {
								PermitDetailsDto pdd = new PermitDetailsDto();
								BeanUtils.copyProperties(pm, pdd);
								trd.setPermitMaster(pdd);
							}
						}
						if (action.getDays()!=null) {
							Optional<TotalNightsMaster> tnm = totalNightsRepository.findById(action.getDays().getId());
							if (tnm.isPresent()) {
								TotalNightsDto tnd = new TotalNightsDto();
								BeanUtils.copyProperties(tnm.get(), tnd);
								trd.setDays(tnd);
							}
						}
						
						
						if (action.getRouteRotation() != null) {
							RouteRotation rr = routeRotationRepository.findById(action.getRouteRotation().getId()).get();
							if (rr != null) {
								RouteRotationDto rrd = new RouteRotationDto();
								List<StopagesMasterDto> sml = new ArrayList<StopagesMasterDto>();
								rrd.setId(rr.getId());
								
								
								
								List<TripDepartureAndDaysEntity> tddl = tripDepartureDaysRepository.getRecordByTripMasterAndRouteRotation(tmle.getId(),action.getRouteRotation().getId());
								if (tddl.size() !=0) {
									List<TripDepartureDaysDto> list = new ArrayList<TripDepartureDaysDto>();
									tddl.forEach(action2->{
										TripDepartureDaysDto tdd = new TripDepartureDaysDto();
										if (null != action2.getDepartureTime()) {
											tdd.setDepartureTime(action2.getDepartureTime().toString());
											
										}else{
											tdd.setDepartureTime(" ");
										}
										tdd.setId(action2.getId());
										if (null != action2.getDays()) {
											TotalNightsMaster tnm = totalNightsRepository.findById(action2.getDays().getId()).get();
											if (tnm !=null) {
												TotalNightsDto nd = new TotalNightsDto();
												BeanUtils.copyProperties(tnm, nd);
												tdd.setDays(nd);
											}
										}
										if (action2.getStopageMaster() != null) {
											StopagesMaster sm = stopagesMasterRepository.findById(action2.getStopageMaster().getId()).get();
											if (sm!=null) {
												StopagesMasterDto smd = new StopagesMasterDto();
												if (sm.getStopage() != null) {
													StopageEntity se = stopageEntityRepo.findById(sm.getStopage().getId()).get();
													if (se!=null) {
														StopageDto sd = new StopageDto();
														sd.setId(se.getId());
														if (se.getCity() != null) {
															CityMaster cm = cityMasterRepository.findById(se.getCity().getId()).get();
															CityDto cd = new CityDto();
															cd.setId(cm.getId());
															cd.setCityName(cm.getCityName());
															sd.setStopageName(se.getStopageName());
															smd.setId(sm.getId());
															smd.setStopage(sd);
															tdd.setStopageMaster(smd);
														}
													}
												}
											}
										}
										
										
										list.add(tdd);
									});
									rrd.setTriDepDays(list);
								}
								
								
								if (rr.getFromState()!=null) {
									StateMaster sm = stateMasterReposiotry.findById(rr.getFromState().getId()).get();
									if (sm!=null) {
										StateDto sd = new StateDto();
										sd.setId(sm.getId());
										sd.setStateName(sm.getStateName());
										rrd.setFromState(sd);
									}
									
								}
								if (rr.getToState()!=null) {
									StateMaster sm = stateMasterReposiotry.findById(rr.getToState().getId()).get();
									if (sm!=null) {
										StateDto sd = new StateDto();
										sd.setId(sm.getId());
										sd.setStateName(sm.getStateName());
										rrd.setToState(sd);
									}
									
								}
								if (rr.getFromCity()!=null) {
									CityMaster sm = cityMasterRepository.findById(rr.getFromCity().getId()).get();
									if (sm!=null) {
										CityDto sd = new CityDto();
										sd.setId(sm.getId());
										sd.setCityName(sm.getCityName());
										rrd.setFromCity(sd);
									}
									
								}
								if (rr.getToCity()!=null) {
									CityMaster sm = cityMasterRepository.findById(rr.getToCity().getId()).get();
									if (sm!=null) {
										CityDto sd = new CityDto();
										sd.setId(sm.getId());
										sd.setCityName(sm.getCityName());
										rrd.setToCity(sd);
									}
									
								}
								if (rr.getStopagesMasterList().size() != 0) {
									rr.getStopagesMasterList().forEach(action1->{
										StopagesMaster sm = stopagesMasterRepository.findById(action1.getId()).get();
										StopagesMasterDto smd = new StopagesMasterDto();
										smd.setId(sm.getId());
										if (null!= sm.getDepartureTime()) {
											smd.setDepartureTime(sm.getDepartureTime().toString());	
										}
										if (null!= sm.getDays()) {
											Optional<TotalNightsMaster> tnm = totalNightsRepository.findById(sm.getDays().getId());
											if (tnm.isPresent()) {
												TotalNightsDto tnd = new TotalNightsDto();
												tnd.setId(tnm.get().getId());
												tnd.setNightDetail(tnm.get().getNightDetail());
												smd.setDays(tnd);
											}
												
										}
										if (sm.getStopage() != null) {
											StopageEntity stm = stopageEntityRepo.findById(sm.getStopage().getId()).get();
											if (stm !=null) {
												StopageDto std = new StopageDto();
												std.setId(stm.getId());
												std.setStopageName(stm.getStopageName());
												if (stm.getCity()!=null) {
													CityMaster cm = cityMasterRepository.findById(stm.getCity().getId()).get();
													if (cm!=null) {
														CityDto cd = new CityDto();
														cd.setId(cm.getId());
														cd.setCityName(cm.getCityName());
														std.setCity(cd);
													}
												}
												smd.setStopage(std);
											}
										}
										sml.add(smd);
									});
									rrd.setStopagesMasterList(sml);
									//trd.setRouteRotation(rrd);
								}
								trd.setRouteRotation(rrd);
							}
						}
						trdList.add(trd);
					});
					trmd.setTripRotation(trdList);
				}
				
				
				
				
				
				
				
				tmd.add(trmd);
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return tmd;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateTripMasterIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updateTripMasterIsDeletedFlag service");
		try {
			int i = tripMasterRepository.updateTripMasterIsDeletedFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Trip has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
	
//	@Override
//	public ResponseEntity<ResponseStatus> saveTripMaster(TripMaster tripMaster) {
//		log.info("Entering into saveTripMaster service");
//		List<StopagesMaster> stopagesMasterList = new ArrayList<>();
//		List<TollTaxWrapper> tollTaxWrapperList = new ArrayList<>();
//		List<BusStandWrapper> busStandWrapperList = new ArrayList<>();
//		StringBuffer serviceId = new StringBuffer();
//		try {
//			if (tripMaster.getId() == null) {
//				if(tripMaster.getRouteMaster() == null) {
//					return new ResponseEntity<>(new ResponseStatus("Route is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}else if(tripMaster.getScheduledKms() == null || tripMaster.getScheduledKms().equals("")) {
//					return new ResponseEntity<>(new ResponseStatus("Scheduled kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}else if(tripMaster.getDeadKms() == null || tripMaster.getDeadKms().equals("")) {
//					return new ResponseEntity<>(new ResponseStatus("Dead kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}else if(tripMaster.getTripStartTime() == null) {
//					return new ResponseEntity<>(new ResponseStatus("Trip start time is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}else if(tripMaster.getTripEndTime() == null) {
//					return new ResponseEntity<>(new ResponseStatus("Trip end time is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}else if(tripMaster.getStopagesMasterList() == null || tripMaster.getStopagesMasterList().size() == 0) {
//					return new ResponseEntity<>(new ResponseStatus("Stopage is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}
////				else if(tripMaster.getTollTaxWrapperList() == null || tripMaster.getTollTaxWrapperList().size() == 0) {
////					return new ResponseEntity<>(new ResponseStatus("Toll tax is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
////					
////				}else if(tripMaster.getBusStandWrapperList() == null || tripMaster.getBusStandWrapperList().size() == 0) {
////					return new ResponseEntity<>(new ResponseStatus("Adding bus stand is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
////					
////				}
//				for (StopagesMaster stopageObj : tripMaster.getStopagesMasterList()) {
//					stopageObj.setTripMaster(tripMaster);
//					stopagesMasterList.add(stopageObj);
//				}
//				tripMaster.setStopagesMasterList(stopagesMasterList);
//				
//				for (TollTaxWrapper tollWrapperObj : tripMaster.getTollTaxWrapperList()) {
//					tollWrapperObj.setTrip(tripMaster);
//					tollTaxWrapperList.add(tollWrapperObj);
//				}
//				tripMaster.setTollTaxWrapperList(tollTaxWrapperList);
//				
//				for (BusStandWrapper busStandWrapperObj : tripMaster.getBusStandWrapperList()) {
//					busStandWrapperObj.setTripMasterObj(tripMaster);
//					busStandWrapperList.add(busStandWrapperObj);
//				}
//				tripMaster.setBusStandWrapperList(busStandWrapperList);
//				tripMaster.setIsDeleted(false);
//				Optional<RouteMaster>  route = routeMasterRepository.findById(tripMaster.getRouteMaster().getId());
//				if(route.isPresent()) {
//					Optional<CityMaster> fromCity = cityMasterRepository.findById(route.get().getFromCity().getId());
//					Optional<CityMaster> toCity = cityMasterRepository.findById(route.get().getToCity().getId());
//					if(fromCity.get().getCityName().length() > 3)
//					serviceId.append(fromCity.get().getCityName().substring(0, 3));
//					else
//						serviceId.append("");	
//					serviceId.append("/");
//					serviceId.append(toCity.get().getCityName().substring(0, 3));
//				}
//				serviceId.append("/");
//				serviceId.append(route.get().getBusTyperMaster().getBusTypeCode().substring(0, 3));
//				serviceId.append("/");
//				int maxPk = tripMasterRepository.fetchMaxId();
//			    serviceId.append(maxPk);
//				tripMaster.setServiceId(serviceId.toString());
//				tripMasterRepository.save(tripMaster);
//				return new ResponseEntity<>(
//						new ResponseStatus("Trip master has been persisted successfully.", HttpStatus.OK),
//						HttpStatus.OK);
//			} 
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(
//					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
//					HttpStatus.OK);
//		}
//		return null;
//	}

	@Override
	public TripFormLoadDto getAllRouteMaster(String dpCode) {
		TripFormLoadDto tripForm = new TripFormLoadDto();
		tripForm = this.tripMasterOnLoad(dpCode);
//		List<RouteMasterDto> routeMasterList = routeMasterRepository.getAllRouteMasterByDepot(dpCode).stream()
//				.map(routeMasterDto -> this.mapper.map(routeMasterDto, RouteMasterDto.class)).collect(Collectors.toList());
//		tripForm.setRoutesList(routeMasterList);
		return tripForm;
	}

	@Override
	public ResponseEntity<Object> getFormLoadData(Integer routeId) {
		List<RoutePermitMaster> routePermitList = routeMasterRepository.findAllPermitsByRouteId(routeId);
		List<TripMaster> tripMasterList = null;
		boolean flag = false;
		String permitNumber = null;
		try {
			if (routePermitList.size() == 1) {
				for (RoutePermitMaster routePermitObj : routePermitList) {
					tripMasterList = tripMasterRepository.findByRouteId(routePermitObj.getRoute().getId());
					if (tripMasterList.size() < routePermitObj.getPermitDetailsMaster().getTotalTrips()) {
						break;
					} else {
						flag = true;
						permitNumber = routePermitObj.getPermitDetailsMaster().getPermitNumber();
						break;
					}

				}
			} else if (routePermitList.size() > 1) {
				for(RoutePermitMaster routePermit  : routePermitList ) {
					List<Integer> routeList = routeMasterRepository.getAllRoutesByPermit(routePermit.getPermitDetailsMaster().getId());
					if(routeList != null && routeList.size() > 0) {
						tripMasterList = tripMasterRepository.findByAllRouteIdsId(routeList);
						if (tripMasterList.size() < routePermit.getPermitDetailsMaster().getTotalTrips()) {
							continue;
						} else {
							flag = true;
							permitNumber = routePermit.getPermitDetailsMaster().getPermitNumber();
							break;
						}
					}
				}
			}
			if (flag) {
				return new ResponseEntity<>(new ResponseStatus(
						"Trip can't be added because no trip available in Permit : " + permitNumber + "",
						HttpStatus.FORBIDDEN), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ResponseStatus("", HttpStatus.OK), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseEntity<ResponseStatus> saveRouteRotation(RouteMasterDto routeMasterDto) {
		try {
			log.info("Entering into saveRouteRotation service");
			List<StopagesMaster> stopagesMasterList = new ArrayList<>();
			List<TollTaxWrapper> tollTaxWrapperList = new ArrayList<>();
			List<BusStandWrapper> busStandWrapperList = new ArrayList<>();
			
		RouteMaster routeMst = routeMasterRepository.getById(routeMasterDto.getId());
			
				for (RouteRotation routeRotation : routeMst.getRouteRotation()) {
					for (RouteRotationDto routeRotation1 : routeMasterDto.getRouteRotation()) {
						if (routeRotation.getId().equals(routeRotation1.getId())) {
							routeRotation.setNightHalt(routeRotation1.getNightHalt());
							routeRotation.setTripScheduleKm(routeRotation1.getTripScheduleKm());
							routeRotation.setTripStartTime(LocalTime.parse(routeRotation1.getTripStartTime()));
							routeRotation.setTripEndTime(LocalTime.parse(routeRotation1.getTripEndTime()));
							if (null != routeRotation1.getTripDay().getId()) {
								TotalNightsMaster tripMaster = totalNightsRepository.findById(routeRotation1.getTripDay().getId()).get();
								routeRotation.setTripDay(tripMaster);
							}
							if (null != routeRotation1.getPermit().getId()) {
								PermitDetailsMaster permit = permitDetailsMasterRepository.findById(routeRotation1.getPermit().getId()).get();
								routeRotation.setPermit(permit);
							}
							for (StopagesMasterDto stopageObj : routeRotation1.getStopagesMasterList()) {
								StopagesMaster stopage = new StopagesMaster();
								stopage.setRouteRotation(routeRotation);
								stopage.setDepartureTime(LocalTime.parse(stopageObj.getDepartureTime()));
//								if (null != stopageObj.getDays().getId()) {
//									TotalNightsMaster tripMaster = totalNightsRepository.findById(routeRotation1.getTripDay().getId()).get();
//									stopage.setDays(tripMaster);
//								}
								if (null != stopageObj.getStopage().getId()) {
									StopageEntity stopageEntity = stopageEntityRepo.findById(stopageObj.getStopage().getId()).get();
									stopage.setStopage(stopageEntity);
								}
								if (null != stopageObj.getStopageTypeMaster().getId()) {
									StopageTypeMaster stopageType = stopTypeMasterRepository.findById(stopageObj.getStopageTypeMaster().getId()).get();
									stopage.setStopageTypeMaster(stopageType);
								}
								stopagesMasterList.add(stopage);
								}
							routeRotation.setStopagesMasterList(stopagesMasterList);
							for (TollTaxWrapperDto tollWrapperObj : routeRotation1.getTollTaxWrapperList()) {
									TollTaxWrapper tollTax = new TollTaxWrapper();
									tollTax.setRouteRotation(routeRotation);
									tollTax.setTollName(tollWrapperObj.getTollName());
									tollTax.setTollFees(tollWrapperObj.getTollFees());
									tollTaxWrapperList.add(tollTax);
								}
							routeRotation.setTollTaxWrapperList(tollTaxWrapperList);
								
							for (BusStandWrapperDto busStandWrapperObj : routeRotation1.getBusStandWrapperList()) {
								    BusStandWrapper busStand = new BusStandWrapper();
								    busStand.setRouteRotation(routeRotation);
								    if (null != busStandWrapperObj.getBusStandMaster().getId()) {
								    	 BusStandMaster busStandMas = busStandMasterRepository.findById(busStandWrapperObj.getBusStandMaster().getId()).get();
								    	 busStand.setBusStandMaster(busStandMas);
									}
								   
								   
								    busStand.setBusStandFees(busStandWrapperObj.getBusStandFees());
									busStandWrapperList.add(busStand);
								}
								routeRotation.setBusStandWrapperList(busStandWrapperList);
						}
						
						
					}
					
				}			
			routeMasterRepository.save(routeMst);
			return new ResponseEntity<>(
					new ResponseStatus("Route Rotation  has been Saved successfully.", HttpStatus.OK),
					HttpStatus.OK);	
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}
    @Transactional(propagation=Propagation.REQUIRED)
	@Override
	public ResponseEntity<ResponseStatus> saveTripMaster(RouteMasterDto routeMasterDto) {
		try {
			int maxPk = 0; 
			int nextNo = 0;      
			List<TripDepartureAndDaysEntity> tr = new ArrayList<TripDepartureAndDaysEntity>();
			List<Integer> permitId = new ArrayList<Integer>();
			RouteMaster routeMaster = null;
			StringBuffer serviceId = new StringBuffer();
			if (null != routeMasterDto.getId()) {
				int permitKms=0;
				int routeKms = routeMasterRepository.findById(routeMasterDto.getId()).get().getScheduledKms(); 
				for(RouteRotationDto rotation : routeMasterDto.getRouteRotation()){
				Optional<PermitDetailsMaster>permit = 	permitDetailsMasterRepository.findById(rotation.getPermit().getId());
				permitKms= permitKms+ permit.get().getTotalKms();
				}
				if(routeKms<=permitKms){
				routeMaster = routeMasterRepository.findById(routeMasterDto.getId()).get();
						routeMasterDto.getRouteRotation().forEach(action->{
						permitId.add(action.getPermit().getId());
						});
						//Long totalKms = permitDetailsMasterRepository.getToatalKm(permitId);
						//if (totalKms>=routeMaster.getScheduledKms()) {
							if (routeMasterDto.getTripMaster() !=null) {
								TripMaster tripMaster = new TripMaster();
								tripMaster.setScheduledKms(routeMasterDto.getTripMaster().get(0).getScheduledKms());
								tripMaster.setDeadKms(routeMasterDto.getTripMaster().get(0).getDeadKms());
								tripMaster.setTripStartTime(LocalTime.parse(routeMasterDto.getTripMaster().get(0).getTripStartTime()));
								tripMaster.setTripEndTime(LocalTime.parse(routeMasterDto.getTripMaster().get(0).getTripEndTime()));
								if (null != routeMasterDto.getTripMaster().get(0).getTotalOt()) {
									tripMaster.setTotalOt(Double.parseDouble(routeMasterDto.getTripMaster().get(0).getTotalOt()));	
								} 
								tripMaster.setDutyCounts(routeMasterDto.getTripMaster().get(0).getDutyCounts());
								tripMaster.setStatus(true);
								tripMaster.setIsDeleted(false);
								
								if (null != routeMasterDto.getId()) {
									routeMaster = routeMasterRepository.findById(routeMasterDto.getId()).get();
									tripMaster.setRouteMaster(routeMaster);
									if(routeMaster!=null) {
										
										Optional<CityMaster> fromCity = cityMasterRepository.findById(routeMaster.getFromCity().getId());
										Optional<CityMaster> toCity = cityMasterRepository.findById(routeMaster.getToCity().getId());
										if(fromCity.get().getCityName().length() > 3)
										serviceId.append(fromCity.get().getCityName().substring(0, 3));
										else
										serviceId.append("");	
										serviceId.append("/");
										serviceId.append(toCity.get().getCityName().substring(0, 3));
									}
									serviceId.append("/");
									serviceId.append(routeMaster.getBusTyperMaster().getBusTypeCode().substring(0, 3));
									serviceId.append("/");
									try {
										maxPk = tripMasterRepository.fetchMaxId();
										nextNo = maxPk+1;
										
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									if (maxPk !=0 ) {
										serviceId.append(nextNo);
										
									} else {
										nextNo = 0+1;
										serviceId.append(nextNo);
									}
									tripMaster.setServiceId(serviceId.toString());
								}
								
						if (routeMasterDto.getRouteRotation().size() != 0) {
							routeMasterDto.getRouteRotation().forEach(action -> {
								if (null != action.getId()) {
									Optional<RouteRotation> rr = routeRotationRepository.findById(action.getId());
									action.getStopagesMasterList().forEach(action1 -> {
										StopagesMaster sm = stopagesMasterRepository.findById(action1.getId()).get();
										TripDepartureAndDaysEntity tdde = new TripDepartureAndDaysEntity();
										tdde.setStopageMaster(sm);
										if (null != action1.getDays().getId()) {
											Optional<TotalNightsMaster> tnm = totalNightsRepository
													.findById(action1.getDays().getId());
											if (tnm.isPresent()) {
												tdde.setDays(tnm.get());
											}
											tdde.setRouteRotation(rr.get());
											tdde.setTripMaster(tripMaster);
										}
										if (null != action1.getDepartureTime()) {
											tdde.setDepartureTime(LocalTime.parse(action1.getDepartureTime()));
										}
										tr.add(tdde);
									});

									
								}
							});
						}
								
								
								
								
								
								tripMaster.setTripDepartureDays(tr);
								TripMaster tm = tripMasterRepository.save(tripMaster);
								
								Integer i = saveAndUpdateRotationAndTripRotation(tm,routeMaster,routeMasterDto);
								if (i == 1) {
									return new ResponseEntity<>(
											new ResponseStatus("Trip Details  has been Saved successfully.", HttpStatus.OK),
											HttpStatus.OK);
								}
							}
//						} else {
//							return new ResponseEntity<>(
//									new ResponseStatus("Schedule KM is Greater Than Total KM.", HttpStatus.FORBIDDEN),
//									HttpStatus.OK);
//						}
			
			}else{
				return new ResponseEntity<>(
						new ResponseStatus("Route Kms is more than Permit Kms! please check", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			} else {
				
				return new ResponseEntity<>(
						new ResponseStatus("There is no Record Found in Route Master.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info(e + " saveTripMasterServiceImpl");
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private Integer  saveAndUpdateRotationAndTripRotation(TripMaster tm, RouteMaster routeMaster,RouteMasterDto routeMasterDto) {
		try {
			List<TripRotationEntity> tr = new ArrayList<TripRotationEntity>();
			if(routeMasterDto.getRouteRotation().size()!= 0){
				routeMasterDto.getRouteRotation().forEach(action->{
					 if (null != action.getId()) {   
						 Optional<RouteRotation> rr = routeRotationRepository.findById(action.getId());
								 TripRotationEntity tre = new TripRotationEntity();
								 tre.setRouteMaster(routeMaster);
								 tre.setRouteRotation(rr.get());
								 tre.setTripMaster(tm);
								 tre.setNightHalt(action.getNightHalt());
								 if (null != action.getTripStartTime()) {
									 tre.setStartTime(LocalTime.parse(action.getTripStartTime()));
								}
								if (null!= action.getTripEndTime()) {
									tre.setEndTime(LocalTime.parse(action.getTripEndTime()));
									
								}
								 if (null != action.getPermit().getId()) {
									 Optional<PermitDetailsMaster> pdm = permitDetailsMasterRepository.findById( action.getPermit().getId());
									 if (pdm.isPresent()) {
										 tre.setPermitMaster(pdm.get());
									}
								}
								 if (null != action.getDays().getId()) {
									 Optional<TotalNightsMaster> tnm = totalNightsRepository.findById(action.getDays().getId());
										if (tnm.isPresent()) {
											tre.setDays(tnm.get());
										}
								}
								 
								 
								 tr.add(tre);
							}
				});
			}
			
			
			
			
			
			
			
			
//			if (routeMasterDto.getRouteRotation().size()!= 0) {
//				routeMasterDto.getRouteRotation().forEach(rrd->{
//					rrd.getStopagesMasterList().forEach(action->{
//						StopagesMaster sm = stopagesMasterRepository.findById(action.getId()).get();
//						if (null != action.getDepartureTime()) {
//							sm.setDepartureTime(LocalTime.parse(action.getDepartureTime()));		
//						}
//						if (null != action.getDays().getId()) {
//							Optional<TotalNightsMaster> tnm = totalNightsRepository.findById(action.getDays().getId());
//							if (tnm.isPresent()) {
//								sm.setDays(tnm.get());
//							}
//							
//						}
//						
//					});
//				});
//			}
			if (tr.size()!=0) {
				routeMaster.setTripRotation(tr);
			}
		RouteMaster rm	= routeMasterRepository.save(routeMaster);
			if (null != rm.getId() ) {
				return 1;
			} else {
				return 2;
			}
			
		} catch (Exception e) {
			log.info(e + "saveAndUpdateRotationAndTripRotation");
			return 2;
		}
	
		
	}

	@Override
	public RouteMasterDto specificRouteForTrip(Integer id) {
		try {
			Optional<RouteMaster> routeMaster = routeMasterRepository.findById(id);
			Optional<StateMaster> fromStateRotation = null;
			Optional<StateMaster> toStateRotation = null;
			Optional<CityMaster> fromCityRotation = null;
			Optional<CityMaster> toCityRotation = null;
			RouteMasterDto dto = null;
			List<RouteRotationDto> roatationDtoList = new ArrayList<RouteRotationDto>();
			CityDto cityDto = null;
			CityDto toCityDto = null;
			StateDto fromStateDtoRo = null;
			StateDto toStateDtoRo = null;
			CityDto fromCityDtoRo = null;
			CityDto toCityDtoRo = null;
			List<StopagesMasterDto> stopageMasterDto = new ArrayList<StopagesMasterDto>();
			if (routeMaster.get().getRouteRotation().size() != 0) {
				for (RouteRotation rr : routeMaster.get().getRouteRotation()) {
					RouteRotationDto roatationDto = new RouteRotationDto();
					if (rr.getFromState() != null) {
						fromStateRotation = stateMasterReposiotry.findById(rr.getFromState().getId());
						if (fromStateRotation.isPresent()) {
							fromStateDtoRo = new StateDto();
							fromStateDtoRo.setId(fromStateRotation.get().getId());
							if (null != fromStateRotation.get().getStateName()) {
								fromStateDtoRo.setStateName(fromStateRotation.get().getStateName());
							} else {
								fromStateDtoRo.setStateName("-");
							}
						}
					}
					if (rr.getToState() != null) {
						toStateRotation = stateMasterReposiotry.findById(rr.getToState().getId());
						if (toStateRotation.isPresent()) {
							toStateDtoRo = new StateDto();
							toStateDtoRo.setId(toStateRotation.get().getId());
							if (null != toStateRotation.get().getStateName()) {
								toStateDtoRo.setStateName(toStateRotation.get().getStateName());
							} else {
								toStateDtoRo.setStateName("-");
							}
						}
					}
					if (rr.getToCity() != null) {
						toCityRotation = cityMasterRepository.findById(rr.getToCity().getId());
						if (toCityRotation.isPresent()) {
							toCityDtoRo = new CityDto();
							toCityDtoRo.setId(toCityRotation.get().getId());
							if (null != toCityRotation.get().getCityName()) {
								toCityDtoRo.setCityName(toCityRotation.get().getCityName());
							} else {
								toCityDtoRo.setCityName("-");
							}
						}
					}
					if (rr.getFromCity() != null) {
						fromCityRotation = cityMasterRepository.findById(rr.getFromCity().getId());
						if (fromCityRotation.isPresent()) {
							fromCityDtoRo = new CityDto();
							fromCityDtoRo.setId(fromCityRotation.get().getId());
							if (null != fromCityRotation.get().getCityName()) {
								fromCityDtoRo.setCityName(fromCityRotation.get().getCityName());
							} else {
								fromCityDtoRo.setCityName("-");
							}
						}
					}

					if (rr.getStopagesMasterList().size() != 0) {
						for (StopagesMaster stopageObj : rr.getStopagesMasterList()) {

							StopagesMasterDto stm = new StopagesMasterDto();
							BeanUtils.copyProperties(stopageObj, stm);
//							if (null != stopageObj.getDays().getId()) {
//								TotalNightsMaster tripMaster = totalNightsRepository
//										.findById(stopageObj.getDays().getId()).get();
//								TotalNightsDto tndto = new TotalNightsDto();
//								if (tripMaster != null) {
//									tndto.setId(tripMaster.getId());
//									tndto.setNightDetail(tripMaster.getNightDetail());
//								}
//								stm.setDays(tndto);
//							}
							if (null != stopageObj.getStopage().getId()) {

								Optional<StopageEntity> stopageEntity = stopageEntityRepo
										.findById(stopageObj.getStopage().getId());
								if (stopageEntity.isPresent()) {
									StopageDto stopageDto = new StopageDto();
									if (stopageEntity.get().getCity() != null) {
										Optional<CityMaster> fromCity1 = cityMasterRepository
												.findById(stopageEntity.get().getCity().getId());
										if (fromCity1.isPresent()) {
											CityDto fromCityDto1 = new CityDto();
											BeanUtils.copyProperties(fromCity1.get(), fromCityDto1);
											stopageDto.setCity(fromCityDto1);
											stopageDto.setId(stopageEntity.get().getId());
											stopageDto.setStopageName(stopageEntity.get().getStopageName());

										}
									}
									BeanUtils.copyProperties(stopageEntity, stopageDto);
									stm.setStopage(stopageDto);
								}

							}
							if (null != stopageObj.getStopageTypeMaster().getId()) {
								Optional<StopageTypeMaster> stopageType = stopTypeMasterRepository
										.findById(stopageObj.getStopageTypeMaster().getId());
								if (stopageType.isPresent()) {
									StopageTypeDto std = new StopageTypeDto();
									BeanUtils.copyProperties(stopageType.get(), std);
									stm.setStopageTypeMaster(std);
								}

							}
							stopageMasterDto.add(stm);

						}
						List<StopagesMasterDto> stopageMasterDto1 = new ArrayList<StopagesMasterDto>();
						stopageMasterDto1.addAll(stopageMasterDto);
						roatationDto.setStopagesMasterList(stopageMasterDto1);
						stopageMasterDto.clear();
					}

					BeanUtils.copyProperties(rr, roatationDto);
					roatationDto.setFromCity(fromCityDtoRo);
					roatationDto.setFromState(fromStateDtoRo);
					roatationDto.setToCity(toCityDtoRo);
					roatationDto.setToState(toStateDtoRo);
					roatationDtoList.add(roatationDto);
			
							}
						}
						dto = new RouteMasterDto();
						BeanUtils.copyProperties(routeMaster.get(), dto);
						RouteTypeDto routeTypeDto =new  RouteTypeDto();
						routeTypeDto.setId(routeMaster.get().getRouteTypeMaster().getId());
						routeTypeDto.setRouteTypeName(routeMaster.get().getRouteTypeMaster().getRouteTypeName());
						dto.setRouteTypeMaster(routeTypeDto);
						dto.setRouteRotation(roatationDtoList);
						return dto;
		} catch (Exception e) {
			log.info(e + " specificRouteForTrip");
		}
		return null;
	}
	
}
