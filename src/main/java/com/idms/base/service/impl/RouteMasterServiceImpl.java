package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import com.idms.base.api.v1.model.dto.AuthorizeRouteDto;
import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusStandDto;
import com.idms.base.api.v1.model.dto.BusStandWrapperDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.PermitDetailsDto;
import com.idms.base.api.v1.model.dto.PermitNumberIdDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.RouteComplexityDto;
import com.idms.base.api.v1.model.dto.RouteFormLoadDto;
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
import com.idms.base.api.v1.model.dto.TripMasterDto;
import com.idms.base.api.v1.model.dto.TripTypeDto;
import com.idms.base.dao.entity.AuthorizeRoute;
import com.idms.base.dao.entity.AuthorizeRouteHistory;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusStandMaster;
import com.idms.base.dao.entity.BusStandWrapper;
import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.entity.RouteCategoryHistory;
import com.idms.base.dao.entity.RouteCategoryMaster;
import com.idms.base.dao.entity.RouteComplexityMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.entity.RoutePermitMaster;
import com.idms.base.dao.entity.RouteRotation;
import com.idms.base.dao.entity.RouteTypeMaster;
import com.idms.base.dao.entity.StateMaster;
import com.idms.base.dao.entity.StateWiseRouteKms;
import com.idms.base.dao.entity.StopageEntity;
import com.idms.base.dao.entity.StopageTypeMaster;
import com.idms.base.dao.entity.StopagesMaster;
import com.idms.base.dao.entity.TollTaxWrapper;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.entity.TripType;
import com.idms.base.dao.repository.AuthorizeRouteHistoryRepository;
import com.idms.base.dao.repository.AuthorizeRouteRepository;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusStandMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.PermitDetailsMasterRepository;
import com.idms.base.dao.repository.RouteCategoryHistoryRepository;
import com.idms.base.dao.repository.RouteCategoryRepository;
import com.idms.base.dao.repository.RouteComplexityRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.RouteRotationRepository;
import com.idms.base.dao.repository.RouteTypeRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.StopTypeMasterRepository;
import com.idms.base.dao.repository.StopageEntityRepository;
import com.idms.base.dao.repository.TotalNightsRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.dao.repository.TripRotationReposiotry;
import com.idms.base.dao.repository.TripTypeRepository;
import com.idms.base.service.RouteMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RouteMasterServiceImpl implements RouteMasterService{
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	PermitDetailsMasterRepository permitDetailsMasterRepository;
	
	@Autowired
	StateMasterRepository stateMasterRepository;
	
	@Autowired
	RouteCategoryRepository routeCategoryRepository;
	
	@Autowired
	RouteComplexityRepository routeComplexityRepository;
	
	@Autowired
	DepotMasterRepository depotMasterRepository;
	
	@Autowired
	RouteTypeRepository routeTypeRepository;
	
	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;
	
	@Autowired
	TotalNightsRepository totalNightsRepository;
	
	@Autowired
	RouteMasterRepository routeMasterRepository;
	
	@Autowired
	CityMasterRepository cityMasterRepository;
	
	@Autowired
	TripMasterRepository tripMasterRepository;
	
	@Autowired
	TransportUnitRepository transportUnitRepository;
	
	@Autowired
	StopTypeMasterRepository stopTypeMasterRepository;
	
	@Autowired
	BusStandMasterRepository busStandMasterRepository;

	@Autowired
	StopageEntityRepository stopageEntityRepo;
	
	@Autowired
	RouteRotationRepository routeRotationRepository;
	
	@Autowired
	TripRotationReposiotry tripRotationReposiotry;
	
	@Autowired
	RouteCategoryHistoryRepository routeCategoryHistoryRepository;
	
	@Autowired
	TripTypeRepository tripTypeRepository;
	
	@Autowired
	BusMasterRepository busMasterRepository;
	
	@Autowired
	DriverMasterRepository driverMasterRepository;
	
	@Autowired
	ConductorMasterRepository conductorMasterRepository;
	
	@Autowired
	AuthorizeRouteRepository authorizeRouteRepository;
	
	@Autowired
	AuthorizeRouteHistoryRepository authorizeRouteHistoryRepository;
	
	@Override
	public RouteFormLoadDto permitDetailsMasterLoad(String dpCode) {
		log.info("Entering into permitDetailsMasterLoad service");
		RouteFormLoadDto routeFormLoadDto = new RouteFormLoadDto();
		Date today = new Date();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		    
			List<StateDto> stateMasterList = stateMasterRepository.findAllByStatus(true).stream()
					.map(state -> new StateDto(state.getId(), state.getStateName())).collect(Collectors.toList());
			
			DepotMaster depotMaster = depotMasterRepository.findByDepotCode(dpCode);
			
			List<TransportDto> transportList = transportUnitRepository.allTransportMasterByDepot(depotMaster.getId()).stream()
					.map(transport -> new TransportDto(transport.getTransportUnitMaster().getId(), transport.getTransportUnitMaster().getTransportName()))
					.collect(Collectors.toList());

			List<BusTypeDto> busTyperMasterList = busTyperMasterRepository.findAllByStatus(true).stream()
					.map(busType -> new BusTypeDto(busType.getId(), busType.getBusTypeName()))
					.collect(Collectors.toList());

			List<PermitDetailsDto> permitMasterList = permitDetailsMasterRepository.findAllByStatusAndIsDeleted(true,false, depotMaster.getId()).stream()
					.map(permitType -> new PermitDetailsDto(permitType.getId(), permitType.getPermitNumber(),permitType.getTotalTrips(),permitType.getValidUpTo()))
					.collect(Collectors.toList());
			List<PermitDetailsDto> permitList = new ArrayList<>();
			for (PermitDetailsDto permitDto : permitMasterList) {
				if (permitDto.getValidUpTo() != null && (permitDto.getValidUpTo().after(today)
						|| formatter.format(permitDto.getValidUpTo()) == (formatter.format(today)))) {
					permitList.add(permitDto);
				} else {
					continue;
				}
			}

			List<RouteCategoryDto> routeCategoryList = routeCategoryRepository.findAllByStatus(true).stream().map(
					routeCategory -> new RouteCategoryDto(routeCategory.getId(), routeCategory.getRouteCategoryName()))
					.collect(Collectors.toList());

			List<RouteComplexityDto> routeComplexityList = routeComplexityRepository.findAllByStatus(true).stream().map(
					routeComplexity -> new RouteComplexityDto(routeComplexity.getId(), routeComplexity.getRcName()))
					.collect(Collectors.toList());

			List<RouteTypeDto> routeTypeList = routeTypeRepository.findAllByStatus(true).stream()
					.map(routeType -> new RouteTypeDto(routeType.getId(), routeType.getRouteTypeName()))
					.collect(Collectors.toList());

			List<TotalNightsDto> totalNightsList = totalNightsRepository.findAllByStatus(true).stream()
					.map(totalNights -> new TotalNightsDto(totalNights.getId(), totalNights.getNightDetail()))
					.collect(Collectors.toList());
			
			// Statrt added by Piyush  for get Data Of busStandList stopageList stopageTypeList while creating route
			
			List<StopageDto> stopagesList = stopageEntityRepo.findAllByStatus(true).stream()
					.map(stopage -> new StopageDto(stopage.getId(), stopage.getStopageName()))
					.collect(Collectors.toList());
			
			if(stopagesList!=null && stopagesList.size() > 0) {
				routeFormLoadDto.setStopageList(stopagesList);
			}
			
			List<StopageTypeDto> stopageList = stopTypeMasterRepository.findAllByStatus(true).stream()
					.map(stop -> new StopageTypeDto(stop.getId(), stop.getStopTypeName()))
					.collect(Collectors.toList());
			if (stopageList != null && stopageList.size() > 0)
				routeFormLoadDto.setStopageTypeList(stopageList);
			
			List<BusStandDto> busStandList = busStandMasterRepository.findAllByStatus(true).stream()
					.map(busStand -> new BusStandDto(busStand.getId(), busStand.getBusStandName()))
					.collect(Collectors.toList());
			
			
			List<TripTypeDto> tripTypeList = tripTypeRepository.findAllByStatus(true).stream()
					.map(type -> new TripTypeDto(type.getId(), type.getTripTypeName()))
					.collect(Collectors.toList());
			
			if (busStandList != null && busStandList.size() > 0)
				routeFormLoadDto.setBusStandList(busStandList);
             // End
			if (stateMasterList != null && stateMasterList.size() > 0)
				routeFormLoadDto.setFromStateList(stateMasterList);

			if (busTyperMasterList != null && busTyperMasterList.size() > 0)
				routeFormLoadDto.setBusTyperMasterList(busTyperMasterList);

			if (depotMaster != null)
				routeFormLoadDto.setDepotMasterList(this.mapper.map(depotMaster, DepotMasterDto.class));

			if (permitList != null && permitList.size() > 0)
				routeFormLoadDto.setPermitDetailList(permitList);

			if (routeCategoryList != null && routeCategoryList.size() > 0)
				routeFormLoadDto.setRouteCategoryList(routeCategoryList);

			if (routeComplexityList != null && routeComplexityList.size() > 0)
				routeFormLoadDto.setRouteComplexityList(routeComplexityList);

			if (routeTypeList != null && routeTypeList.size() > 0)
				routeFormLoadDto.setRouteTypeList(routeTypeList);

			if (totalNightsList != null && totalNightsList.size() > 0)
				routeFormLoadDto.setTotalNightsList(totalNightsList);
			
			if (transportList != null && transportList.size() > 0)
				routeFormLoadDto.setTransportList(transportList);
			
			if (tripTypeList != null && tripTypeList.size() > 0)
				routeFormLoadDto.setTripTypeList(tripTypeList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routeFormLoadDto;
	}


	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseEntity<ResponseStatus> saveRouteMaster(RouteMasterDto routeMaster) {
		log.info("Entering into saveRouteMaster service");
		List<StateWiseRouteKms> stateWiseList = new ArrayList<>();
		List<RoutePermitMaster> routePermitMasterList = new ArrayList<>();
		List<RouteRotation> routeRotationList = new ArrayList<RouteRotation>();
		List<StopagesMaster> stopagesMasterList = new ArrayList<>();
		List<TollTaxWrapper> tollTaxWrapperList = new ArrayList<>();
		List<BusStandWrapper> busStandWrapperList = new ArrayList<>();
		Boolean flag = false;
		Integer busTypeId = null;
		try {
			if (routeMaster.getId() == null) {
				if(routeMaster.getRouteId() == null || routeMaster.getRouteId().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Route Id is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getRouteName() == null || routeMaster.getRouteName().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Route Name is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getTransport() == null || routeMaster.getTransport().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Transport is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getDepotMaster() == null || routeMaster.getDepotMaster().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Depot is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getRouteCode() == null || routeMaster.getRouteCode().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Route Code is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}
//				else if(routeMaster.getTotalOt() == null || routeMaster.getTotalOt().equals("")) {
//					return new ResponseEntity<>(new ResponseStatus("Route Total OT is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}
//				else if(routeMaster.getTotalNightsMaster() == null || routeMaster.getTotalNightsMaster().equals("")) {
//					return new ResponseEntity<>(new ResponseStatus("Total Nights is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}
				else if(routeMaster.getFromState() == null || routeMaster.getFromState().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("From State is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getToState() == null || routeMaster.getToState().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("To State is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getFromCity() == null || routeMaster.getFromCity().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("From City is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getToCity() == null || routeMaster.getToCity().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("To City is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getScheduledKms() == null || routeMaster.getScheduledKms().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Scheduled kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getDeadKms() == null || routeMaster.getDeadKms().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Dead kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getRouteComplexityMaster() == null) {
					return new ResponseEntity<>(new ResponseStatus("Route Complexity is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getRouteCategoryMaster() == null) {
					return new ResponseEntity<>(new ResponseStatus("Route Category is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getTripType() == null) {
					return new ResponseEntity<>(new ResponseStatus("Trip Type is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}
//				else if(routeMaster.getDutyCounts() == null || routeMaster.getDutyCounts().equals("")) {
//					return new ResponseEntity<>(new ResponseStatus("Duty Counts is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
//					
//				}else if(routeMaster.getRoutePermitMasterList() == null || routeMaster.getRoutePermitMasterList().size() == 0) {
//					return new ResponseEntity<>(new ResponseStatus("Permit is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
			//	}
			else if(routeMaster.getBusTyperMaster() == null) {
					return new ResponseEntity<>(new ResponseStatus("Bus type is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getRouteTypeMaster() == null) {
					return new ResponseEntity<>(new ResponseStatus("Route type is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(routeMaster.getStateWiseRouteKms() == null || routeMaster.getStateWiseRouteKms().size() == 0) {
					return new ResponseEntity<>(new ResponseStatus("State wise kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}
				//Added By Piyush
				else if (routeMaster.getRouteRotation() == null || routeMaster.getRouteRotation().size() == 0) {
					return new ResponseEntity<>(new ResponseStatus("Route Rotation is mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
				
				RouteMaster rmEntity = new RouteMaster();
				rmEntity.setRouteId(routeMaster.getRouteId());
				rmEntity.setRouteCode(routeMaster.getRouteCode());
				rmEntity.setRouteName(routeMaster.getRouteName());
				//rmEntity.setTotalOt(routeMaster.getTotalOt());
				rmEntity.setScheduledKms(routeMaster.getScheduledKms());
				rmEntity.setDeadKms(routeMaster.getDeadKms());
				//rmEntity.setDutyCounts(routeMaster.getDutyCounts());
				if (null != routeMaster.getTransport().getId()) {
					TransportUnitMaster tum = transportUnitRepository.findById(routeMaster.getTransport().getId()).get();
					rmEntity.setTransport(tum);
				}
//				if (null != routeMaster.getTotalNightsMaster().getId()) {
//					TotalNightsMaster tnm = totalNightsRepository.findById(routeMaster.getTotalNightsMaster().getId()).get();
//					rmEntity.setTotalNightsMaster(tnm);
//				}
				if (null != routeMaster.getFromState().getId()) {
					StateMaster fromState = stateMasterRepository.findById(routeMaster.getFromState().getId()).get();
					rmEntity.setFromState(fromState);
				}
				if (null != routeMaster.getToState().getId()) {
					StateMaster toState = stateMasterRepository.findById(routeMaster.getToState().getId()).get();
					rmEntity.setToState(toState);
				}
				if (null != routeMaster.getFromCity().getId()) {
					CityMaster fromCity = cityMasterRepository.findById(routeMaster.getFromCity().getId()).get();
					rmEntity.setFromCity(fromCity);
				}
				if (null != routeMaster.getToCity().getId()) {
					CityMaster fromCity = cityMasterRepository.findById(routeMaster.getToCity().getId()).get();
					rmEntity.setToCity(fromCity);
				}
				
				if (null != routeMaster.getRouteComplexityMaster().getId()) {
					RouteComplexityMaster rcm = routeComplexityRepository.findById(routeMaster.getRouteComplexityMaster().getId()).get();
					rmEntity.setRouteComplexityMaster(rcm);
				}
				
				if (null != routeMaster.getRouteCategoryMaster().getId()) {
					RouteCategoryMaster rcm = routeCategoryRepository.findById(routeMaster.getRouteCategoryMaster().getId()).get();
					rmEntity.setRouteCategoryMaster(rcm);
				}
				
				if (null != routeMaster.getRouteTypeMaster().getId()) {
					RouteTypeMaster rtm = routeTypeRepository.findById(routeMaster.getRouteTypeMaster().getId()).get();
					rmEntity.setRouteTypeMaster(rtm);
				}
				
				if (null != routeMaster.getBusTyperMaster().getId()) {
					BusTyperMaster btm = busTyperMasterRepository.findById(routeMaster.getBusTyperMaster().getId()).get();
					rmEntity.setBusTyperMaster(btm);
				}
				
				if (null != routeMaster.getDepotMaster().getId()) {
					DepotMaster dm = depotMasterRepository.findById(routeMaster.getDepotMaster().getId()).get();
					rmEntity.setDepotMaster(dm);
				}
				
				if (null != routeMaster.getTripType().getId()) {
					TripType tripType = tripTypeRepository.findById(routeMaster.getTripType().getId()).get();
					rmEntity.setTripType(tripType);
				}
				
				for (StateWiseRouteKmsDto viaObj : routeMaster.getStateWiseRouteKms()) {
					StateWiseRouteKms st = new StateWiseRouteKms();
					st.setHillKms(viaObj.getHillKms());
					st.setPlainKms(viaObj.getPlainKms());
					st.setTotalKms(viaObj.getTotalKms());
					if (null != viaObj.getState().getId()) {
						StateMaster fromState = stateMasterRepository.findById(viaObj.getState().getId()).get();
						st.setState(fromState);
					}
					st.setRouteMaster(rmEntity);
					stateWiseList.add(st);
				}

					routeMaster.getRouteRotation().forEach(action1->{
						
						RouteRotation rr = new RouteRotation();
						if (null != action1.getFromState().getId()) {
							StateMaster fromState = stateMasterRepository.findById(action1.getFromState().getId()).get();
							rr.setFromState(fromState);
						}
						if (null != action1.getToState().getId()) {
							StateMaster toState = stateMasterRepository.findById(action1.getToState().getId()).get();
							rr.setToState(toState);
						}
						if (null != action1.getFromCity().getId()) {
							CityMaster fromCity = cityMasterRepository.findById(action1.getFromCity().getId()).get();
							rr.setFromCity(fromCity);
						}
						if (null != action1.getToCity().getId()) {
							CityMaster fromCity = cityMasterRepository.findById(action1.getToCity().getId()).get();
							rr.setToCity(fromCity);
						}
						rr.setRouteMaster(rmEntity);
		
						action1.getStopagesMasterList().forEach(action->{
						 StopagesMaster stm = new StopagesMaster();
						 stm.setRouteRotation(rr);
//						if (null != action.getDays().getId()) {
//							TotalNightsMaster tripMaster = totalNightsRepository.findById(action.getDays().getId()).get();
//							stm.setDays(tripMaster);
//						}
						if (null != action.getStopage().getId()) {
							StopageEntity stopageEntity = stopageEntityRepo.findById(action.getStopage().getId()).get();
							stm.setStopage(stopageEntity);
						}
						if (null != action.getStopageTypeMaster().getId()) {
							StopageTypeMaster stopageType = stopTypeMasterRepository.findById(action.getStopageTypeMaster().getId()).get();
							stm.setStopageTypeMaster(stopageType);
						}
						stopagesMasterList.add(stm);
					});
					
					rr.setStopagesMasterList(stopagesMasterList);
					
					
					
					
					
				
					action1.getTollTaxWrapperList().forEach(action->{
						TollTaxWrapper ttwe = new TollTaxWrapper();
						ttwe.setRouteRotation(rr);
						ttwe.setTollFees(action.getTollFees());
						ttwe.setTollName(action.getTollName());
						tollTaxWrapperList.add(ttwe);
					});
					rr.setTollTaxWrapperList(tollTaxWrapperList);
					
					
					rr.setBusStandWrapperList(busStandWrapperList);
					action1.getBusStandWrapperList().forEach(action->{
						BusStandWrapper bswe = new BusStandWrapper();
						bswe.setRouteRotation(rr);
						bswe.setBusStandFees(action.getBusStandFees());
					    if (null != action.getBusStandMaster().getId()) {
					    	 BusStandMaster busStandMas = busStandMasterRepository.findById(action.getBusStandMaster().getId()).get();
					    	 bswe.setBusStandMaster(busStandMas);
						}
						busStandWrapperList.add(bswe);
					});
					
					
					
					routeRotationList.add(rr);
					
					});
				
				
				
				//End By Piyush

				if (flag) {
					return new ResponseEntity<>(new ResponseStatus("Bus type should be same.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				rmEntity.setStateWiseRouteKms(stateWiseList);
//				routeMaster.setRoutePermitMasterList(routePermitMasterList);
				rmEntity.setRouteRotation(routeRotationList);
				rmEntity.setIsDeleted(false);
				routeMasterRepository.save(rmEntity);
				return new ResponseEntity<>(
						new ResponseStatus("Route master has been persisted successfully.", HttpStatus.OK),
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
	public List<RouteMaster> getAllRouteMaster() {
		log.info("Entering into getAllRouteMaster service");
		List<RouteMaster> routeMasterList = null;
		try {
			//routeMasterList = routeMasterRepository.findAll();
			routeMasterList = routeMasterRepository.findAllByStatus(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return routeMasterList;
	}
	
	@Override
	public List<RouteMaster> getAllRouteMasterByDepot(String dpCode) {
		log.info("Entering into getAllRouteMasterByDepot service");
		List<RouteMaster> routeMasterList = null;
		try {
			routeMasterList = routeMasterRepository.getAllRouteMasterByDepot(dpCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return routeMasterList;
	}
	
	@Override
	public List<RouteMaster> getAllRouteMasterByDriverId(Integer driverId,boolean status , boolean isdeleted) {
		log.info("Entering into getAllRouteMasterByDriverId service");
		List<RouteMaster> routeMasterList = null;
		try {
			routeMasterList = routeMasterRepository.getAllRouteMasterByDriverId(driverId,status,isdeleted);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return routeMasterList;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateRouteMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateRouteMasterStatusFlag service");
		try {
			int i = routeMasterRepository.updateRouteMasterStatusFlag(flag,id);
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
	public ResponseEntity<ResponseStatus> updateRouteMasterIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updateRouteMasterIsDeletedFlag service");
		try {
			List<TripMaster> tripMasterList = tripMasterRepository.findByRouteId(id);
			if(tripMasterList != null && tripMasterList.size() > 0) {
				return new ResponseEntity<>(new ResponseStatus("Kindly remove associated trip first, to delete the route.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			else {
			int i = routeMasterRepository.updateRouteMasterIsDeletedFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Route has been removed successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}

	@Override
	public RouteMasterDto findByRouteId(Integer id) {
		Optional<RouteMaster> routeMaster = routeMasterRepository.findById(id);
		Optional<CityMaster> fromCity = null;
		Optional<CityMaster> toCity = null;
		Optional<StateMaster> fromState = null;
		Optional<StateMaster> toState = null;
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
		List<TollTaxWrapperDto> tollTaxList = new ArrayList<TollTaxWrapperDto>();
		List<BusStandWrapperDto> bswd = new ArrayList<BusStandWrapperDto>();
		List<TripMasterDto> tmdl = new ArrayList<TripMasterDto>();

		if (routeMaster.isPresent()) {
			if (routeMaster.get().getFromCity() != null)
				fromCity = cityMasterRepository.findById(routeMaster.get().getFromCity().getId());
			CityDto ct = new CityDto();
			ct.setId(fromCity.get().getId());
			ct.setCityName(fromCity.get().getCityName());
			dto = new RouteMasterDto();
			dto.setFromCity(ct);
			if (routeMaster.get().getToCity() != null)
				toCity = cityMasterRepository.findById(routeMaster.get().getToCity().getId());
			CityDto ct1 = new CityDto();
			ct1.setId(toCity.get().getId());
			ct1.setCityName(toCity.get().getCityName());
			dto.setToCity(ct1);
			if (routeMaster.get().getFromState() != null)
				fromState = stateMasterRepository.findById(routeMaster.get().getFromState().getId());
			StateDto st = new StateDto();
			st.setId(fromState.get().getId());
			st.setStateName(fromState.get().getStateName());
			dto.setFromState(st);
			if (routeMaster.get().getToState() != null)
				toState = stateMasterRepository.findById(routeMaster.get().getToState().getId());
			StateDto st1 = new StateDto();
			st1.setId(toState.get().getId());
			st1.setStateName(toState.get().getStateName());
			dto.setToState(st1);
			if (routeMaster.get().getTransport() != null) {
				TransportUnitMaster tum = transportUnitRepository.findById(routeMaster.get().getTransport().getId())
						.get();
				if (tum != null) {
					TransportDto td = new TransportDto();
					td.setId(tum.getId());
					td.setTransportName(tum.getTransportName());
					dto.setTransport(td);
				}
			}

			if (routeMaster.get().getRouteComplexityMaster() != null) {
				RouteComplexityMaster rcm = routeComplexityRepository
						.findById(routeMaster.get().getRouteComplexityMaster().getId()).get();
				if (rcm != null) {
					RouteComplexityDto rcd = new RouteComplexityDto();
					rcd.setId(rcm.getId());
					rcd.setRcName(rcm.getRcName());
					dto.setRouteComplexityMaster(rcd);

				}
			}

			if (routeMaster.get().getRouteCategoryMaster() != null) {
				RouteCategoryMaster rcm = routeCategoryRepository
						.findById(routeMaster.get().getRouteCategoryMaster().getId()).get();
				if (rcm != null) {
					RouteCategoryDto rcd = new RouteCategoryDto();
					rcd.setId(rcm.getId());
					rcd.setRouteCategoryName(rcm.getRouteCategoryName());
					dto.setRouteCategoryMaster(rcd);
				}

			}

			if (routeMaster.get().getRouteTypeMaster() != null) {
				RouteTypeMaster rtm = routeTypeRepository.findById(routeMaster.get().getRouteTypeMaster().getId())
						.get();
				if (rtm != null) {
					RouteTypeDto rtd = new RouteTypeDto();
					rtd.setId(rtm.getId());
					rtd.setRouteTypeName(rtm.getRouteTypeName());
					dto.setRouteTypeMaster(rtd);
				}

			}

			if (routeMaster.get().getBusTyperMaster() != null) {
				BusTyperMaster btm = busTyperMasterRepository.findById(routeMaster.get().getBusTyperMaster().getId())
						.get();
				if (btm != null) {
					BusTypeDto rtd = new BusTypeDto();
					rtd.setId(btm.getId());
					rtd.setBusTypeName(btm.getBusTypeName());
					dto.setBusTyperMaster(rtd);
				}

			}
			
			if (routeMaster.get().getTripType() != null) {
				TripType tripType = tripTypeRepository.findById(routeMaster.get().getTripType().getId())
						.get();
				if (tripType != null) {
					TripTypeDto rtd = new TripTypeDto();
					rtd.setId(tripType.getId());
					rtd.setTripTypeName(tripType.getTripTypeName());
					dto.setTripType(rtd);
				}

			}

//			if (routeMaster.get().getTotalNightsMaster() != null) {
//				TotalNightsMaster tnm = totalNightsRepository.findById(routeMaster.get().getTotalNightsMaster().getId())
//						.get();
//				if (tnm != null) {
//					TotalNightsDto rtd = new TotalNightsDto();
//					rtd.setId(tnm.getId());
//					rtd.setNightDetail(tnm.getNightDetail());
//					dto.setTotalNightsMaster(rtd);
//				}
//
//			}

			if (routeMaster.get().getStateWiseRouteKms().size() != 0) {
				List<StateWiseRouteKmsDto> swrkdList = new ArrayList<StateWiseRouteKmsDto>();
				routeMaster.get().getStateWiseRouteKms().forEach(action -> {
					StateWiseRouteKmsDto sw = new StateWiseRouteKmsDto();
					sw.setId(action.getId());
					sw.setHillKms(action.getHillKms());
					sw.setPlainKms(action.getPlainKms());
					sw.setTotalKms(action.getTotalKms());
					if (action.getState() != null) {
						StateMaster sm = stateMasterRepository.findById(action.getState().getId()).get();
						if (sm != null) {
							StateDto sd = new StateDto();
							sd.setId(sm.getId());
							sd.setStateName(sm.getStateName());
							sw.setState(sd);
						}
					}
					swrkdList.add(sw);
				});
				dto.setStateWiseRouteKms(swrkdList);
			}

			if (routeMaster.get().getRouteRotation().size() != 0) {
				for (RouteRotation rr : routeMaster.get().getRouteRotation()) {
					RouteRotationDto roatationDto = new RouteRotationDto();
					if (rr.getFromState() != null) {
						fromStateRotation = stateMasterRepository.findById(rr.getFromState().getId());
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
						toStateRotation = stateMasterRepository.findById(rr.getToState().getId());
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

					if (rr.getTollTaxWrapperList().size() != 0) {
						for (TollTaxWrapper tollWrapperObj : rr.getTollTaxWrapperList()) {
							TollTaxWrapperDto ttwe = new TollTaxWrapperDto();
							BeanUtils.copyProperties(tollWrapperObj, ttwe);
							tollTaxList.add(ttwe);
						}

						List<TollTaxWrapperDto> tollTaxList1 = new ArrayList<TollTaxWrapperDto>();
						tollTaxList1.addAll(tollTaxList);
						roatationDto.setTollTaxWrapperList(tollTaxList1);
						tollTaxList.clear();
					}

					if (rr.getBusStandWrapperList().size() != 0) {
						for (BusStandWrapper busStandWrapperObj : rr.getBusStandWrapperList()) {
							BusStandWrapperDto bswe = new BusStandWrapperDto();
							bswe.setId(busStandWrapperObj.getId());
							bswe.setBusStandFees(busStandWrapperObj.getBusStandFees());
							if (null != busStandWrapperObj.getBusStandMaster().getId()) {
								Optional<BusStandMaster> busStandMas = busStandMasterRepository
										.findById(busStandWrapperObj.getBusStandMaster().getId());
								if (busStandMas.isPresent()) {
									BusStandDto bsmd = new BusStandDto();
									BeanUtils.copyProperties(busStandMas.get(), bsmd);
									bswe.setBusStandMaster(bsmd);
								}
							}
							bswd.add(bswe);
						}
						List<BusStandWrapperDto> bswd1 = new ArrayList<BusStandWrapperDto>();
						bswd1.addAll(bswd);
						roatationDto.setBusStandWrapperList(bswd1);
						bswd.clear();
					}

					BeanUtils.copyProperties(rr, roatationDto);
					roatationDto.setFromCity(fromCityDtoRo);
					roatationDto.setFromState(fromStateDtoRo);
					roatationDto.setToCity(toCityDtoRo);
					roatationDto.setToState(toStateDtoRo);
					roatationDtoList.add(roatationDto);
			
							}
						}
						
						BeanUtils.copyProperties(routeMaster.get(), dto);
					}
						if (fromCity.isPresent()) {
							cityDto = new CityDto();
							BeanUtils.copyProperties(fromCity.get(), cityDto);
						}
						if (toCity.isPresent()) {
							toCityDto = new CityDto();
							BeanUtils.copyProperties(toCity.get(), toCityDto);
						}
						dto.setRouteRotation(roatationDtoList);

		
		
		
		return dto;
	}
	
	@Override
	public List<PermitDetailsMaster> getPermitsForTransportAndDepot(Integer dpId, Integer tpId) {
		log.info("Entering into getAllRouteMasterByDepot service");
		List<PermitDetailsMaster> permitList = null;
		try {
			permitList = permitDetailsMasterRepository.getPermitsForTransportAndDepot(dpId, tpId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return permitList;
	}


	@Override
	public List<RouteMaster> getListRouteMaster() {
		log.info("Entering into getAllRouteMasterByDepot service");
		List<RouteMaster> routeMasters = null;
		try {
			routeMasters = routeMasterRepository.findAllByIsDeleted(false);
		} catch (Exception e) {
			log.info("Exception"+e);
		}
		return routeMasters;
	}


	@Override
	public List<PermitNumberIdDto> permitListByTransportAndDepotFilterByRoute(Integer routeId) {
		log.info("permitListByTransportAndDepotFilterByRoute service");
		List<PermitNumberIdDto> pnIdDto = new ArrayList<PermitNumberIdDto>();
		try {
			Optional<RouteMaster> routeMaster = routeMasterRepository.findById(routeId);
			if (routeMaster.isPresent()) {
				List<PermitDetailsMaster>	pdm =  getPermitsForTransportAndDepot(routeMaster.get().getDepotMaster().getId(),routeMaster.get().getTransport().getId());
				pdm.forEach(action->{
					PermitNumberIdDto pnId = new PermitNumberIdDto();
					pnId.setPermitId(action.getId());
					pnId.setPermitNumber(action.getPermitNumber());
					pnIdDto.add(pnId);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pnIdDto;
	}



	@Override
	public List<RouteMasterDto> getAllRouteMasterByDepoCode(String dpCode) {
		try {
			Integer depoId;
			List<RouteMasterDto> routeMasterDtoList = new ArrayList<RouteMasterDto>();
			depoId = depotMasterRepository.getIdByDepoName(dpCode);
			
			 Date date = new Date(); 
			List<Object[]> obj  = routeMasterRepository.getAllRouteMasterByDepoCode(depoId,date);
			for (Object[] objects : obj) {
				RouteMasterDto routeMasterDto = new RouteMasterDto();
	
				if (objects[0] != null) {
					routeMasterDto.setRouteId(objects[0].toString());
					if (objects[1] != null) {
						routeMasterDto.setRouteName(objects[1].toString());
					}
					if (objects[2] != null) {
						routeMasterDto.setScheduledKms(Integer.parseInt(objects[2].toString()));
					}
					if (objects[3] != null) {
						Optional<StateMaster> fs = stateMasterRepository.findById(Integer.parseInt(objects[3].toString()));
						if (fs.isPresent()) {
							StateDto smd = new StateDto();
							smd.setId(fs.get().getId());
							if (null != fs.get().getStateName()) {
								smd.setStateName(fs.get().getStateName());	
							}else{
								smd.setStateName("-");
							}
							routeMasterDto.setFromState(smd);
							
						}
					}
					if(objects[11]!=null)
					{
						routeMasterDto.setRouteflag(objects[11].toString());
					}
					else
					{
						routeMasterDto.setRouteflag("");
					}
					
					
					if (objects[4] != null) {
						Optional<CityMaster> fc = cityMasterRepository.findById(Integer.parseInt(objects[4].toString()));
						if (fc.isPresent()) {
							CityDto cmd = new CityDto();
							cmd.setId(fc.get().getId());
							if (null != fc.get().getCityName()) {
								cmd.setCityName(fc.get().getCityName());	
							}else{
								cmd.setCityName("-");
							}
							
							routeMasterDto.setFromCity(cmd);
							
						}
					}
					if (objects[5] != null) {
						Optional<StateMaster> ts = stateMasterRepository.findById(Integer.parseInt(objects[5].toString()));
						if (ts.isPresent()) {
							StateDto smd = new StateDto();
							smd.setId(ts.get().getId());
							if (null != ts.get().getStateName()) {
								smd.setStateName(ts.get().getStateName());	
							}else{
								smd.setStateName("-");
							}
							routeMasterDto.setToState(smd);
							
						}
					}
					if (objects[6] != null) {
						Optional<CityMaster> tc = cityMasterRepository.findById(Integer.parseInt(objects[6].toString()));
						if (tc.isPresent()) {
							CityDto cmd = new CityDto();
							cmd.setId(tc.get().getId());
							if (null != tc.get().getCityName()) {
								cmd.setCityName(tc.get().getCityName());	
							}else{
								cmd.setCityName("-");
							}
							routeMasterDto.setToCity(cmd);
							
						}
					}
					if (objects[7] != null) {
						routeMasterDto.setId(Integer.parseInt(objects[7].toString()));	
						}
					if (objects[8] != null) {
						routeMasterDto.setDeadKms(Integer.parseInt(objects[8].toString()));	
						}
					
					if (objects[9] != null) {
						routeMasterDto.setStatus(Boolean.parseBoolean(objects[9].toString()));	
						}
					
					if (objects[10] != null) {
						routeMasterDto.setRouteCategory(objects[10].toString());	
						}
				}
					routeMasterDtoList.add(routeMasterDto);
				
			}
			return routeMasterDtoList;
		} catch (Exception e) {
			log.info(e + "getAllRouteMasterByDepoCodeServiceImpl");
		}
		return null;
		
	}



	@Override
	public List<PermitDetailsDto> permitList(String dpCode) {
		List<PermitDetailsDto> permitDtlDtoList = new ArrayList<PermitDetailsDto>();
	try {
		Integer depoId;
		depoId = depotMasterRepository.getIdByDepoName(dpCode);
		Date date = new Date();
		List<PermitDetailsMaster> pdm = permitDetailsMasterRepository.getRecordByStatusAndExp(depoId,date);
		pdm.forEach(action->{
			PermitDetailsDto pdd = new PermitDetailsDto();
			pdd.setId(action.getId());
			pdd.setPermitNumber(action.getPermitNumber());
			permitDtlDtoList.add(pdd);
		});
	} catch (Exception e) {
		log.info(e + "permitList");
	}
		return permitDtlDtoList;
	}
	/**
	 * 1 means true
	 * 2 means false
	 * 3 No Record
	 */
	@Override
	public Integer checkPermitValidation(Integer permitId) {
		Integer i;
		try {
			PermitDetailsMaster pdm = permitDetailsMasterRepository.findById(permitId).get();
			Long tre = tripRotationReposiotry.getReocrdCountBypermitMaster(permitId);
			if (pdm != null) {
				if (null != tre) {
					if (null != pdm.getTotalTrips()) {
						if ((pdm.getTotalTrips()*2)>tre) {
							i = 1;
							return i;
						}else{
							i=2;
							return i;
						}
					}
				}else{
					i = 1;
					return i;
				}
			}else{
				i = 3;
				return i;
			}
		} catch (Exception e) {
			log.info(e + "checkPermitValidation");
		}
		return null;
	}



	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> updateRouteMasterCategoryById(Integer routeId, Integer categoryId) {
		int i = 0;
		RouteCategoryHistory history = new RouteCategoryHistory();
		try {
			i = routeMasterRepository.updateRouteMasterCategoryById(routeId, categoryId);
			if (i > 0) {
				Optional<RouteMaster> route = routeMasterRepository.findById(routeId);
				history.setRouteMaster(route.get());
				history.setRouteCategoryMaster(route.get().getRouteCategoryMaster());
				routeCategoryHistoryRepository.save(history);
				return new ResponseEntity<>(new ResponseStatus("Route category updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}



	@Override
	public List<RouteCategoryDto> routeCategoryMasterOnLoad() {
		List<RouteCategoryDto> routeCategoryList = routeCategoryRepository.findAllByStatus(true).stream().map(
				routeCategory -> new RouteCategoryDto(routeCategory.getId(), routeCategory.getRouteCategoryName()))
				.collect(Collectors.toList());
		return routeCategoryList;
	}



	@Override
	public List<TransportDto> allTransportMasterByDepot(String dpCode) {
		List<TransportDto> list = new ArrayList<>();
		try {
			DepotMaster depot = depotMasterRepository.findByDepotCode(dpCode);
			list = transportUnitRepository.allTransportMasterByDepot(depot.getId()).stream()
					.map(tpu -> new TransportDto(tpu.getTransportUnitMaster().getId(), tpu.getTransportUnitMaster().getTransportName())).collect(Collectors.toList());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}



	@Override
	public List<AuthorizeRouteDto> getDropdownList(Integer transportId) {
		
		List<AuthorizeRouteDto> authorizeRouteDtolist = new ArrayList<AuthorizeRouteDto>();
		
		AuthorizeRouteDto authorizeRouteDtoObj= new AuthorizeRouteDto();
		List<RouteMasterDto> rList=new ArrayList<>();
		List<BusMasterDto> bList=new ArrayList<>();
		List<DriverMasterDto> dList=new ArrayList<>();
		List<ConductorMasterDto> cList=new ArrayList<>();
		
		List<Object[]> routeList = routeMasterRepository.getAllRouteBytransportId(transportId);
		List<Object[]> busList = routeMasterRepository.getAllBusBytransportId(transportId);
		List<Object[]> driverList = routeMasterRepository.getAllDriverBytransportId(transportId);
		List<Object[]> conductorList = routeMasterRepository.getAllConductorBytransportId(transportId);
		
		//List<RouteMasterDto> routeMasterDto=new ArrayList<>();
		//authorizeRouteDtoObj.setBusList(busList);
		RouteMasterDto objRoute =null;
		BusMasterDto objBus =null;
		DriverMasterDto objDriver =null;
		ConductorMasterDto objConductor =null;
		
		for(Object[] o : routeList){
			objRoute = new RouteMasterDto();
			if(o[1]!=null)
			objRoute.setRouteName(o[1].toString());;
			if(o[2]!=null)
			objRoute.setId(Integer.parseInt(o[2].toString()));;
			rList.add(objRoute);
		}	
		
		for(Object[] o : busList) {
			objBus = new BusMasterDto();
			if(o[0]!=null)
				objBus.setId(Integer.parseInt(o[0].toString()));
			if(o[1]!=null)
				objBus.setBusRegNumber(o[1].toString());
			bList.add(objBus);
		}
		
		for(Object[] o : driverList) {
			objDriver = new DriverMasterDto();
			if(o[0]!=null)
				objDriver.setDriverName(o[0].toString());
			if(o[1]!=null)
				objDriver.setId(Integer.parseInt(o[1].toString()));
			dList.add(objDriver);
		}
		for(Object[] o : conductorList) {
			objConductor = new ConductorMasterDto();
			if(o[0]!=null)
				objConductor.setId(Integer.parseInt(o[0].toString()));
				
			if(o[1]!=null)
				objConductor.setConductorName(o[1].toString());
			cList.add(objConductor);
		}	
		authorizeRouteDtoObj.setRouteList(rList);
		authorizeRouteDtoObj.setBusList(bList);
		authorizeRouteDtoObj.setDriverList(dList);
		authorizeRouteDtoObj.setConductorList(cList);
		
		authorizeRouteDtolist.add(authorizeRouteDtoObj);
		
		return authorizeRouteDtolist;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseEntity<ResponseStatus> saveAuthorizeRoute(List<AuthorizeRouteDto> authorizeRouteList) {

		log.info("Entering into saveAuthorizeRoute service");

		//List<AuthorizeRouteDto> authorizeRouteList= new ArrayList<>();
		AuthorizeRoute tempAuthorizeRoute = null;
		
		try {
			for(AuthorizeRouteDto authorizeRouteDto:authorizeRouteList)
			{
				tempAuthorizeRoute = new AuthorizeRoute();
				if (authorizeRouteDto.getId() != null) {
					tempAuthorizeRoute.setId(authorizeRouteDto.getId());
				}

				RouteMaster routeObj = routeMasterRepository.findById(authorizeRouteDto.getRoute().getId()).get();
				//if(routeObj !=null)
				tempAuthorizeRoute.setRoute(routeObj);	
				
				//DepotMaster DepotObj= depotMasterRepository.findById(authorizeRouteDto.getDepot().getId()).get();
				Integer depoMasterId = depotMasterRepository.getIdByDepoName(authorizeRouteDto.getDepot().getDepotCode());
				DepotMaster DepotObj= depotMasterRepository.findById(depoMasterId).get();
				tempAuthorizeRoute.setDepot(DepotObj);			
				
				TransportUnitMaster tranObj=transportUnitRepository.findById(authorizeRouteDto.getTransportUnit().getId()).get();
				tempAuthorizeRoute.setTransport(tranObj);
				
				BusMaster busObj=busMasterRepository.findById(authorizeRouteDto.getBus().getId()).get();
				tempAuthorizeRoute.setBus(busObj);
				
				DriverMaster driverObj=driverMasterRepository.findById(authorizeRouteDto.getDriver().getId()).get();
				tempAuthorizeRoute.setDriver(driverObj);
				
				ConductorMaster conductorObj=conductorMasterRepository.findById(authorizeRouteDto.getConductor().getId()).get();
				tempAuthorizeRoute.setConductor(conductorObj);
				tempAuthorizeRoute.setIsDeleted(false);
				
				TripMaster tripObj =tripMasterRepository.findById(authorizeRouteDto.getTrip().getId()).get();
				tempAuthorizeRoute.setTrip(tripObj);
				
				tempAuthorizeRoute.setReason(authorizeRouteDto.getReason());
				
				authorizeRouteRepository.save(tempAuthorizeRoute);

			}
			
			return new ResponseEntity<>(new ResponseStatus("Record persisted successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}

}



	@Override
	public List<AuthorizeRouteDto> onLoadAuthorizeRoute(String dpCode) {
		
		List<Object[]> objlist=null;
		List<AuthorizeRouteDto> authorizeRouteList=new ArrayList<>();
		
		AuthorizeRouteDto tempAuthorizeRoute = null; 
		try {
			Integer depoMasterId = depotMasterRepository.getIdByDepoName(dpCode);
			objlist = routeMasterRepository.onLoadAuthorizeRoute(depoMasterId);
			for(Object[] o : objlist) {
				tempAuthorizeRoute = new AuthorizeRouteDto();
				if(o[0]!=null)
				tempAuthorizeRoute.setRouteName(o[0].toString());
				if(o[1]!=null)
				tempAuthorizeRoute.setBusRegNumber(o[1].toString());
				if(o[2]!=null)
				tempAuthorizeRoute.setDriverCode(o[2].toString());
				if(o[3]!=null)
				tempAuthorizeRoute.setDriverName(o[3].toString());
				if(o[4]!=null)
				tempAuthorizeRoute.setConductorCode(o[4].toString());
				if(o[5]!=null)
				tempAuthorizeRoute.setConductorName(o[5].toString());
				if(o[6]!=null)
				tempAuthorizeRoute.setDepotName(o[6].toString());				
				if(o[7]!=null)
				tempAuthorizeRoute.setAuthorizeRouteId(Integer.parseInt(o[7].toString()));
				if(o[8]!=null)
				tempAuthorizeRoute.setIsDeleted(Boolean.valueOf(o[8].toString()));
				if(o[9]!=null)
				tempAuthorizeRoute.setServiceId(o[9].toString());
				if(o[10]!=null)
				tempAuthorizeRoute.setTripCode(o[10].toString());
				if(o[11]!=null)
				tempAuthorizeRoute.setReason(o[11].toString());
				
				authorizeRouteList.add(tempAuthorizeRoute);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return authorizeRouteList;
	}



	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseEntity<ResponseStatus> updateAuthorizeRoute(List<AuthorizeRouteDto> authorizeRouteList) {

		log.info("Entering into updateAuthorizeRoute service");

		AuthorizeRouteHistory tempRouteHistory = null;
		AuthorizeRoute tempAuthorizeRoute = null;
		try {
			for(AuthorizeRouteDto authorizeRouteDto:authorizeRouteList)
			{
				tempRouteHistory = new AuthorizeRouteHistory();
				
				AuthorizeRoute authorizeRouteObj=authorizeRouteRepository.findById(authorizeRouteDto.getId()).get();
				
				tempRouteHistory.setAuthorizeRoute(authorizeRouteObj);
				
				RouteMaster routeHistoryObj = routeMasterRepository.findById(authorizeRouteObj.getRoute().getId()).get();
				//if(routeObj !=null)
				tempRouteHistory.setRoute(routeHistoryObj);

				//DepotMaster DepotObj= depotMasterRepository.findById(authorizeRouteDto.getDepot().getId()).get();
				Integer depoId = depotMasterRepository.getIdByDepoName(authorizeRouteObj.getDepot().getDepotCode());
				DepotMaster DepotHistoryObj= depotMasterRepository.findById(depoId).get();
				tempRouteHistory.setDepot(DepotHistoryObj);			
				
				TransportUnitMaster tranHistoryObj=transportUnitRepository.findById(authorizeRouteObj.getTransport().getId()).get();
				tempRouteHistory.setTransport(tranHistoryObj);
				
				BusMaster busHistoryObj=busMasterRepository.findById(authorizeRouteObj.getBus().getId()).get();
				tempRouteHistory.setBus(busHistoryObj);
				
				DriverMaster driverHistoryObj=driverMasterRepository.findById(authorizeRouteObj.getDriver().getId()).get();
				tempRouteHistory.setDriver(driverHistoryObj);
				
				ConductorMaster conductorHistoryObj=conductorMasterRepository.findById(authorizeRouteObj.getConductor().getId()).get();
				tempRouteHistory.setConductor(conductorHistoryObj);
				
				TripMaster tripMasterObj=tripMasterRepository.findById(authorizeRouteObj.getTrip().getId()).get();
				tempRouteHistory.setTrip(tripMasterObj);
				
				tempRouteHistory.setReason(authorizeRouteObj.getReason());
				
				authorizeRouteHistoryRepository.save(tempRouteHistory);
				
				tempAuthorizeRoute = new AuthorizeRoute();
				
				tempAuthorizeRoute.setId(authorizeRouteDto.getId());
				RouteMaster routeObj = routeMasterRepository.findById(authorizeRouteDto.getRoute().getId()).get();
				//if(routeObj !=null)
				tempAuthorizeRoute.setRoute(routeObj);

				//DepotMaster DepotObj= depotMasterRepository.findById(authorizeRouteDto.getDepot().getId()).get();
				Integer depoMasterId = depotMasterRepository.getIdByDepoName(authorizeRouteObj.getDepot().getDepotCode());
				DepotMaster DepotObj= depotMasterRepository.findById(depoMasterId).get();
				tempAuthorizeRoute.setDepot(DepotObj);			
				
				TransportUnitMaster tranObj=transportUnitRepository.findById(authorizeRouteObj.getTransport().getId()).get();
				tempAuthorizeRoute.setTransport(tranObj);
				
				BusMaster busObj=busMasterRepository.findById(authorizeRouteDto.getBus().getId()).get();
				tempAuthorizeRoute.setBus(busObj);
				
				DriverMaster driverObj=driverMasterRepository.findById(authorizeRouteDto.getDriver().getId()).get();
				tempAuthorizeRoute.setDriver(driverObj);
				
				ConductorMaster conductorObj=conductorMasterRepository.findById(authorizeRouteDto.getConductor().getId()).get();
				tempAuthorizeRoute.setConductor(conductorObj);
				
				TripMaster triObj=tripMasterRepository.findById(authorizeRouteDto.getTrip().getId()).get();
				tempAuthorizeRoute.setTrip(triObj);
				
				tempAuthorizeRoute.setReason(authorizeRouteDto.getReason());
				
				//authorizeRouteRepository.save(tempAuthorizeRoute);
								
				authorizeRouteRepository.updateAuthorizeRouteById(authorizeRouteDto.getRoute().getId(),
						authorizeRouteDto.getBus().getId(),authorizeRouteDto.getDriver().getId(),authorizeRouteDto.getConductor().getId(),authorizeRouteDto.getTrip().getId(),authorizeRouteDto.getReason(),
						authorizeRouteDto.getId());
			}
			
			return new ResponseEntity<>(new ResponseStatus("Authorize Route Updated Successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}

}
	@Override
	public List<AuthorizeRouteDto> getAuthorizeRouteOnId(Integer authorizeRouteId) {
		
		List<AuthorizeRouteDto> authorizeRouteDtolist = new ArrayList<AuthorizeRouteDto>();
		List<TripMasterDto> tripMasterDtoList = new ArrayList<TripMasterDto>();
		
		AuthorizeRouteDto authorizeRouteDtoObj= new AuthorizeRouteDto();
		//List<RouteMasterDto> rList=new ArrayList<>();
		List<BusMasterDto> bList=new ArrayList<>();
		List<DriverMasterDto> dList=new ArrayList<>();
		List<ConductorMasterDto> cList=new ArrayList<>();
		
		AuthorizeRoute authorizeRouteObj=authorizeRouteRepository.findById(authorizeRouteId).get();
		
		authorizeRouteDtoObj.setIsDeleted(authorizeRouteObj.getIsDeleted());
		RouteMaster routeMasterObj=routeMasterRepository.findById(authorizeRouteObj.getRoute().getId()).get();
		
		//List<Object[]> routeList = routeMasterRepository.getAllRouteBytransportId();
		List<Object[]> busList = routeMasterRepository.getAllBusBytransportId(authorizeRouteObj.getTransport().getId());
		List<Object[]> driverList = routeMasterRepository.getAllDriverBytransportId(authorizeRouteObj.getTransport().getId());
		List<Object[]> conductorList = routeMasterRepository.getAllConductorBytransportId(authorizeRouteObj.getTransport().getId());
		
		List<Object[]> tripMasterList = routeMasterRepository.findTripByRouteId(authorizeRouteObj.getRoute().getId());
		
		//List<RouteMasterDto> routeMasterDto=new ArrayList<>();
		//authorizeRouteDtoObj.setBusList(busList);
		RouteMasterDto objRoute =null;
		BusMasterDto objBus =null;
		DriverMasterDto objDriver =null;
		ConductorMasterDto objConductor =null;
		
			objRoute = new RouteMasterDto();
			objRoute.setId(routeMasterObj.getId());
			objRoute.setRouteName(routeMasterObj.getRouteName());
			
		/*for(Object[] o : routeList){
			objRoute = new RouteMasterDto();
			if(o[1]!=null)
			objRoute.setRouteName(o[1].toString());;
			if(o[2]!=null)
			objRoute.setId(Integer.parseInt(o[2].toString()));;
			rList.add(objRoute);
		}*/	
		for(Object[] o : busList) {
			objBus = new BusMasterDto();
			if(o[0]!=null)
				objBus.setId(Integer.parseInt(o[0].toString()));
			if(o[1]!=null)
				objBus.setBusRegNumber(o[1].toString());
			bList.add(objBus);
		}
		for(Object[] o : driverList) {
			objDriver = new DriverMasterDto();
			if(o[0]!=null)
				objDriver.setDriverName(o[0].toString());
			if(o[1]!=null)
				objDriver.setId(Integer.parseInt(o[1].toString()));
			dList.add(objDriver);
		}
		for(Object[] o : conductorList) {
			objConductor = new ConductorMasterDto();
			if(o[0]!=null)
				objConductor.setId(Integer.parseInt(o[0].toString()));
			if(o[1]!=null)				
			objConductor.setConductorName(o[1].toString());
			cList.add(objConductor);
		}
		for(Object[] o : tripMasterList){
			TripMasterDto trDto = new TripMasterDto();
			if(o[0]!=null)
				trDto.setId(Integer.parseInt(o[0].toString()));
			if(o[1]!=null)
				trDto.setServiceId(o[1].toString());
			if(o[2]!=null)
				trDto.setTripCode(o[2].toString());	
			tripMasterDtoList.add(trDto);
		}
		//authorizeRouteDtoObj.setRouteList(rList);
		authorizeRouteDtoObj.setRoute(objRoute);
		authorizeRouteDtoObj.setBusList(bList);
		authorizeRouteDtoObj.setDriverList(dList);
		authorizeRouteDtoObj.setConductorList(cList);
		authorizeRouteDtoObj.setReason(authorizeRouteObj.getReason());
		authorizeRouteDtoObj.setTripList(tripMasterDtoList);
		authorizeRouteDtolist.add(authorizeRouteDtoObj);
		
		return authorizeRouteDtolist;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateAuthorizeRouteIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updateAuthorizeRouteIsDeletedFlag service");
		try {
			int i = authorizeRouteRepository.updateAuthorizeRouteIsDeletedFlag(flag, id);
			if (i == 1)
				return new ResponseEntity<>(
						new ResponseStatus("Authorize Route has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}
		
	@Override
	public List<TripMasterDto> getAllTripByRouteId(Integer routeId) {
		
		List<TripMasterDto> tripMasterDtoList = new ArrayList<TripMasterDto>();
		List<Object[]> tripMasterList = routeMasterRepository.findTripByRouteId(routeId);
		
		if(tripMasterList != null && tripMasterList.size() > 0) {
			for(Object[] o : tripMasterList){
				TripMasterDto trDto = new TripMasterDto();
				if(o[0]!=null)
					trDto.setId(Integer.parseInt(o[0].toString()));
				if(o[1]!=null)
					trDto.setServiceId(o[1].toString());
					
				tripMasterDtoList.add(trDto);
			}
		
		}
		
		return tripMasterDtoList;
	}
}