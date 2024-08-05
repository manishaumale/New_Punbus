package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DailyRoasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.ETMMasterDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxEntityChildDto;
import com.idms.base.api.v1.model.dto.OnlineBookingDetailsDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.RouteOnlineBookingDetailsDto;
import com.idms.base.api.v1.model.dto.RouteRotationDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementDto;
import com.idms.base.api.v1.model.dto.TicketBoxMasterDto;
import com.idms.base.api.v1.model.dto.TicketManagementBoxParentDto;
import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.CentralTicketStock;
import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.ConductorRotaHistory;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.ETMMaster;
import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntity;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntityChild;
import com.idms.base.dao.entity.OnlineBookingDetails;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.entity.RouteRotation;
import com.idms.base.dao.entity.StateMaster;
import com.idms.base.dao.entity.TicketBoxManagementEntity;
import com.idms.base.dao.entity.TicketBoxManagementParentEntity;
import com.idms.base.dao.entity.TicketBoxMaster;
import com.idms.base.dao.repository.BusStandMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.CentralTicketStockRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.ETMMasterRepository;
import com.idms.base.dao.repository.EtmTBAssignmentRepository;
import com.idms.base.dao.repository.IssueEtmTicketBoxRepository;
import com.idms.base.dao.repository.OnlineBookingDetailsRepository;
import com.idms.base.dao.repository.RouteCategoryRepository;
import com.idms.base.dao.repository.RouteComplexityRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.RouteTypeRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.StopTypeMasterRepository;
import com.idms.base.dao.repository.StopageEntityRepository;
import com.idms.base.dao.repository.TicketBoxManagementParentRepository;
import com.idms.base.dao.repository.TicketBoxManagementRepository;
import com.idms.base.dao.repository.TicketBoxRepository;
import com.idms.base.dao.repository.TotalNightsRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.service.IssueETMnTicketBoxService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class IssueETMnTicketBoxServiceImpl implements IssueETMnTicketBoxService{

	
	@Autowired
	ConductorMasterRepository conductorMasterRepository;
	
	@Autowired
	TicketBoxRepository tbRepo;
	
	@Autowired
	ETMMasterRepository etmMasterRepository;
	
	@Autowired
	TicketBoxRepository ticketBoxRepository;
	
	@Autowired
	TicketBoxManagementRepository ticketBoxMasterRepository;
	
	@Autowired
	TicketBoxManagementParentRepository tbmpRepo;
	
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
	OnlineBookingDetailsRepository OnlineBookingDetailsRepo;
	
	@Autowired
	DailyRoasterRepository dailyRoasterRepo;
	
	@Autowired
	private OnlineBookingDetailsRepository bookingRepo;
	
	@Autowired
	private TicketBoxManagementParentRepository ticketBoxManagementRepo;
	
	@Autowired
	private IssueEtmTicketBoxRepository issueEtmTicketBoxRepo;
	
	@Autowired
	private EtmTBAssignmentRepository etmRepository;
	
	@Autowired
	private CentralTicketStockRepository centralTicketStockRepository;
	
	@Override
	public List<ConductorMaster> getAllConductorMasterByDpcode(String dpCode) {
		log.info("Entering into getAllConductorMaster service");
		List<ConductorMaster> conductorsMasterList = null;
		List<Integer> conductorIdissueEtmTicketBoxEntity =  issueEtmTicketBoxRepo.getRecordForAvailableConductor();
		try {
			if (conductorIdissueEtmTicketBoxEntity.size()!= 0) {
				conductorsMasterList = conductorMasterRepository.getConductorRecordByDepot(dpCode,conductorIdissueEtmTicketBoxEntity);
			}else{
				conductorsMasterList = conductorMasterRepository.findAllByDepot(dpCode);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conductorsMasterList;
	}
	
	@Override
	public List<ETMMaster> listOfAllETMMaster(String dpCode) {
		log.info("Entering into listOfAllETMMaster service");
		List<ETMMaster> etmList = null;
		List<Integer> etmId =  issueEtmTicketBoxRepo.getRecordForAvailableEtm();
		try {
			
			if (etmId.size() != 0) {
				etmList = etmMasterRepository.getEtmRecordByDepot(dpCode,etmId);
			} else {
				etmList = etmMasterRepository.findAllETMbyDpCode(dpCode);

			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return etmList;
	}
	
	
	@Override
	public List<TicketBoxMaster> listOfAllTicketBoxMaster(String dpCode) {
		log.info("Entering into listOfAllTicketBoxMaster service");
		
		List<TicketBoxMaster> ticketBoxList = null;
		List<TicketBoxMaster> newTicketBoxList = new ArrayList<>();
		List<Integer> ticketBoxId =  issueEtmTicketBoxRepo.getRecordForAvailableTicket();
		try {
			if (ticketBoxId.size()!=0) {
				ticketBoxList = ticketBoxRepository.findAllBoxListWithDepotCode(dpCode,ticketBoxId);
			} else {
				ticketBoxList = ticketBoxRepository.findAllBoxWithDepotCode(dpCode);

			}
			
			for(TicketBoxMaster ticketBoxObj : ticketBoxList){
				TicketBoxManagementParentEntity parent = tbmpRepo.findByTicketBoxId(ticketBoxObj.getId());
				if(parent != null && parent.getId() != null){
					ticketBoxObj.setParentId( parent.getId());
					newTicketBoxList.add(ticketBoxObj);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return newTicketBoxList;
	}

	@Override
	public RouteMasterDto findByRouteId(Integer id) {
		Optional<RouteMaster> routeMaster = routeMasterRepository.findById(id);
		Optional<CityMaster> fromCity = null;
		Optional<CityMaster> toCity = null;
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
		
		if(routeMaster.isPresent()) {
			if(routeMaster.get().getFromCity() != null)
			fromCity = cityMasterRepository.findById(routeMaster.get().getFromCity().getId());
			if(routeMaster.get().getToCity() != null)
			toCity   = cityMasterRepository.findById(routeMaster.get().getToCity().getId());
			if (routeMaster.get().getRouteRotation().size() !=0) {
				for (RouteRotation rr: routeMaster.get().getRouteRotation()) {
					RouteRotationDto roatationDto = new RouteRotationDto();
					if (rr.getFromState() !=null) {
						fromStateRotation = stateMasterRepository.findById(rr.getFromState().getId());
						if (fromStateRotation.isPresent()) {
							fromStateDtoRo= new StateDto();
							BeanUtils.copyProperties(fromStateRotation.get(),fromStateDtoRo);
						}
					}
					if (rr.getToState() !=null) {
						toStateRotation = stateMasterRepository.findById(rr.getToState().getId());
						if (toStateRotation.isPresent()) {
							toStateDtoRo= new StateDto();
							BeanUtils.copyProperties(toStateRotation.get(),toStateDtoRo);
						}
					}
					if (rr.getToCity() !=null) {
						toCityRotation = cityMasterRepository.findById(rr.getToCity().getId());
						if (toCityRotation.isPresent()) {
							toCityDtoRo= new CityDto();
							BeanUtils.copyProperties(toCityRotation.get(),toCityDtoRo);
						}
					}
					if (rr.getFromCity() !=null) {
						fromCityRotation = cityMasterRepository.findById(rr.getFromCity().getId());
						if (fromCityRotation.isPresent()) {
							fromCityDtoRo= new CityDto();
							BeanUtils.copyProperties(fromCityRotation.get(),fromCityDtoRo);
						}
					}
				     BeanUtils.copyProperties(rr,roatationDto);
				     roatationDto.setFromCity(fromCityDtoRo);
				     roatationDto.setFromState(fromStateDtoRo);
				     roatationDto.setToCity(toCityDtoRo);
				     roatationDto.setToState(toStateDtoRo);
				     roatationDtoList.add(roatationDto);
				     
				}				
			}
			dto = new RouteMasterDto();
			BeanUtils.copyProperties(routeMaster.get(),dto);
		}
		if(fromCity.isPresent()) {
			cityDto = new CityDto();
			BeanUtils.copyProperties(fromCity.get(),cityDto);
		}
		if(toCity.isPresent()) {
			toCityDto = new CityDto();
			BeanUtils.copyProperties(toCity.get(),toCityDto);
		}
		dto.setFromCity(cityDto);
		dto.setToCity(toCityDto);
		dto.setRouteRotation(roatationDtoList);
		return dto;
	}

	@Override
	public List<OnlineBookingDetailsDto> findById(Integer id) {
		
		
		log.info("Entering into listOfAllETMMaster service");
		List<OnlineBookingDetailsDto> OnlineBookingDetailsDtolist = null;
		try {
			 //onlineBookingDetailsDto = OnlineBookingDetailsRepo.findById(id);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return OnlineBookingDetailsDtolist;
	}

	@Override
	public ResponseEntity findByCondutorId(Integer id) {

		RouteOnlineBookingDetailsDto routeOnlineBookingDetailsDto = new RouteOnlineBookingDetailsDto();

		try {
			List<ConductorRotaHistory> DailyRoaster = dailyRoasterRepo.fetchRosterDetailsByCondutorId(id);
			List<OnlineBookingDetailsDto> onlineBookingList = new ArrayList<>();
			long timeDiff = ChronoUnit.MINUTES.between(DailyRoaster.get(0).getRoaster().getTripRotation().getStartTime(),
					LocalTime.now());
			// routeOnlineBookingDetailsDto.setStatus("Trip time is greater than
			// 2 hours");
			String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String roatdate= new SimpleDateFormat("yyyy-MM-dd").format(DailyRoaster.get(0).getRoaster().getRota().getRotaDate());			
			if (timeDiff < 120 &&  roatdate.compareTo(modifiedDate) == 0) {
				if (DailyRoaster.size() > 0) {
//					for (int i = 0; i < DailyRoaster.size(); i++) {
						log.info("Bus ::::" + DailyRoaster.get(0).getRoaster().getBus().getBusRegNumber());
						BusTypeDto busTypeDto = new BusTypeDto();
						List<OnlineBookingDetails> ltOnlineBookingDetails = new ArrayList<>();
						routeOnlineBookingDetailsDto.setId(DailyRoaster.get(0).getRoaster().getId());
						routeOnlineBookingDetailsDto.setRouteId(DailyRoaster.get(0).getRoaster().getRoute().getRouteId());
						routeOnlineBookingDetailsDto.setRouteCode(DailyRoaster.get(0).getRoaster().getRoute().getRouteCode());
						routeOnlineBookingDetailsDto.setRouteName(DailyRoaster.get(0).getRoaster().getRoute().getRouteName());
						routeOnlineBookingDetailsDto.setScheduledKms(DailyRoaster.get(0).getRoaster().getRoute().getScheduledKms() * DailyRoaster.get(0).getRoaster().getRoute().getTripType().getTripValue() );
						routeOnlineBookingDetailsDto.setBusRegNumber(DailyRoaster.get(0).getRoaster().getBus().getBusRegNumber());
						busTypeDto.setBusTypeName(DailyRoaster.get(0).getRoaster().getBus().getBusType().getBusTypeName());
						routeOnlineBookingDetailsDto.setBusType(busTypeDto);
						routeOnlineBookingDetailsDto.setDriverId(DailyRoaster.get(0).getRoaster().getDriver().getId());
						routeOnlineBookingDetailsDto.setDriverCode(DailyRoaster.get(0).getRoaster().getDriver().getDriverCode());
						routeOnlineBookingDetailsDto.setDriverName(DailyRoaster.get(0).getRoaster().getDriver().getDriverName());
						routeOnlineBookingDetailsDto.setConductorId(DailyRoaster.get(0).getConductor().getId());
						routeOnlineBookingDetailsDto
								.setConductorName(DailyRoaster.get(0).getConductor().getConductorName());
						ltOnlineBookingDetails = bookingRepo.findByTripId(DailyRoaster.get(0).getTrip());
						if (ltOnlineBookingDetails.size() > 0) {
							for (int j = 0; j < ltOnlineBookingDetails.size(); j++) {
								OnlineBookingDetailsDto onlineBookingDetailsDto = new OnlineBookingDetailsDto();
								onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
								onlineBookingDetailsDto.setAmount(ltOnlineBookingDetails.get(j).getAmount());
								onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
								onlineBookingDetailsDto
										.setFromCityCode(ltOnlineBookingDetails.get(j).getFromCity().getCityName());
								onlineBookingDetailsDto
										.setToCityCode(ltOnlineBookingDetails.get(j).getToCity().getCityName());
								onlineBookingDetailsDto.setSeatsCount(ltOnlineBookingDetails.get(j).getSeatsCount());
								onlineBookingList.add(onlineBookingDetailsDto);
							}
						}
						routeOnlineBookingDetailsDto.setStatus("Trip time is lesser than 2 hours");
						routeOnlineBookingDetailsDto.setOnlineBookingDetails(onlineBookingList);
//					}
				}
				return new ResponseEntity<>(routeOnlineBookingDetailsDto, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ResponseStatus("Trip time is greater than 2 hours", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(
					"Exception while fetching Data from Daily roaster :IssueETMnTicketBoxServiceImpl:Method findByCondutorId",
					e);
		}

		return new ResponseEntity<>(routeOnlineBookingDetailsDto, HttpStatus.OK);
	}

	@Override
	public TicketManagementBoxParentDto findByTicketBoxId(Integer id) {
		TicketManagementBoxParentDto dto = new TicketManagementBoxParentDto();
		try {
			TicketBoxManagementParentEntity list = tbmpRepo.findByTicketBoxId(id);
			Long totalValue = (long) 0;
			List<TicketBoxManagementDto> boxManagementDtos = new ArrayList<TicketBoxManagementDto>();
			if (list !=null) {
				
				if (list.getTicketBoxManagementList().size() != 0) {
					for (TicketBoxManagementEntity entity2 : list.getTicketBoxManagementList()) {
						TicketBoxManagementDto boxManagementDto = new TicketBoxManagementDto();
						boxManagementDto.setId(entity2.getId());
						boxManagementDto.setAmount(entity2.getAmount());
						if(entity2.getCurrentLastNo() != null)
						boxManagementDto.setStartingSerialNo(entity2.getCurrentLastNo());
						else
							boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
						boxManagementDto.setEndingSerialNo(entity2.getEndingSerialNo());
						boxManagementDto.setDeno(entity2.getDenomination().getDenomination());
						boxManagementDto.setTransportUnitMaster(entity2.getTransportUnitMaster().getTransportName());
						//boxManagementDto.setTicketBox(entity2.getTicketBoxNumber());
						totalValue = totalValue + entity2.getAmount();
						boxManagementDtos.add(boxManagementDto);
					}
					Object[] sum = ticketBoxMasterRepository.fetchAmountSumByIssueEtmParentId(list.getId());
					if(sum[0] != null && sum.length > 0){
						dto.setAmountSum(sum[0].toString());
					}
					dto.setId(list.getId());
					dto.setTicketBoxManagementList(boxManagementDtos);
					TicketBoxMaster tbm = tbRepo.findById(list.getTicketBoxNumber().getId()).get();
					if (tbm!=null) {
						TicketBoxMasterDto dtoo = new TicketBoxMasterDto();
						dtoo.setTicketBoxNumber(tbm.getTicketBoxNumber());
						dto.setTicketBox(dtoo);	
					}
					dto.setTransportUnitMaster(boxManagementDtos.get(0).getTransportUnitMaster());
					dto.setTotalValue(totalValue);
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public ResponseEntity<ResponseStatus> addissueEtmTicket(IssueEtmTicketBoxDto issueEtmTicketBox) {
		
		IssueEtmTicketBoxEntityChild childObj = null;
		List<IssueEtmTicketBoxEntityChild> issueEtmChildList = new ArrayList<>();
		List<TicketBoxManagementEntity> ticketBoxManagementEntity = new ArrayList<>();
		
		try {
			IssueEtmTicketBoxEntity entity = new IssueEtmTicketBoxEntity();
			entity.setWayBillNo(issueEtmTicketBox.getWayBillNo());
			
			Optional<ConductorMaster> conductorMaster = conductorMasterRepository.findById(issueEtmTicketBox.getConductorMaster());
			entity.setConductorMaster(conductorMaster.get());
			
			Optional<TicketBoxMaster> ticketBoxMaster = ticketBoxRepository.findById(issueEtmTicketBox.getTicketBoxNumber());
			entity.setTicketBoxNumber(ticketBoxMaster.get());
			
			Optional<ETMMaster> etmMaster = etmMasterRepository.findById(issueEtmTicketBox.getEtmMaster());
			entity.setEtmMaster(etmMaster.get());
			
			Optional<DailyRoaster> dailyRoaster = dailyRoasterRepo.findById(issueEtmTicketBox.getDailyRoaster());
			entity.setDailyRoaster(dailyRoaster.get());
			
			DepotMaster dpMaster = depotMasterRepository.findByDepotCode(issueEtmTicketBox.getDpCode());
			if (null != dpMaster.getId()) {
				entity.setDepoMaster(dpMaster);
			}
			
			if(issueEtmTicketBox.getOnlineBookingDetails() != null){
				Optional<OnlineBookingDetails> onlineBookingDetails = OnlineBookingDetailsRepo.findById(issueEtmTicketBox.getOnlineBookingDetails());
				entity.setOnlineBookingDetails(onlineBookingDetails.get());	
			}
			
			Optional<TicketBoxManagementParentEntity> ticketBoxManagementParentEntity = ticketBoxManagementRepo.findById(issueEtmTicketBox.getTicketBoxManagementParentEntity());
			entity.setTicketBoxManagementParentEntity(ticketBoxManagementParentEntity.get());
			
			if(ticketBoxManagementParentEntity.isPresent()){
			 ticketBoxManagementEntity = ticketBoxMasterRepository.findByParentId(ticketBoxManagementParentEntity.get().getId());
			}
			
			if(ticketBoxManagementEntity.size() > 0)
				for(TicketBoxManagementEntity entityObj : ticketBoxManagementEntity){
					childObj = new IssueEtmTicketBoxEntityChild();
					childObj.setIssueEtmTicketBoxEntity(entity);
					childObj.setAmount(entityObj.getAmount());
					childObj.setCurrentLastNo(entityObj.getCurrentLastNo());
					if(entityObj.getDenomination() != null)
					childObj.setDenomination(entityObj.getDenomination().getDenomination());
					childObj.setEndingSerialNo(entityObj.getEndingSerialNo());
					childObj.setTransportUnitMaster(entityObj.getTransportUnitMaster());
					childObj.setStartingSerialNo(entityObj.getStartingSerialNo());
					childObj.setTicketBoxManagementEntity(entityObj);
					issueEtmChildList.add(childObj);
				}
			
			entity.setIssueEtmChildList(issueEtmChildList);
			
			issueEtmTicketBoxRepo.save(entity);
			EtmTBAssignment etmAssignment = new EtmTBAssignment();
			etmAssignment.setConductorId(entity.getConductorMaster());
			etmAssignment.setRouteId(entity.getDailyRoaster().getRoute());
			etmAssignment.setWayBillNo(entity.getWayBillNo());
			etmAssignment.setTicketBoxId(entity.getTicketBoxNumber());
			etmAssignment.setEtmId(entity.getEtmMaster());
			etmAssignment.setIssueEtmId(entity);
			etmAssignment.setEtmSubmitStatus(false);
			etmAssignment.setTbSubmitStatus(false);
			etmRepository.save(etmAssignment);
			return new ResponseEntity<>(
					new ResponseStatus("Added Issue ETM Ticket Box sucessfully", HttpStatus.OK),
					HttpStatus.OK);
                 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<IssueEtmTicketBoxDto> getIssueEtmTicketBoxList(String depocode) {
		List<IssueEtmTicketBoxDto> dtosList = new ArrayList<>();
		try {
			DepotMaster dpMaster = depotMasterRepository.findByDepotCode(depocode);
			List<IssueEtmTicketBoxEntity> entitiList = issueEtmTicketBoxRepo.getRecordByDepotId(dpMaster.getId());
			for(IssueEtmTicketBoxEntity entity: entitiList){
				IssueEtmTicketBoxDto dto = new IssueEtmTicketBoxDto();
				ConductorMasterDto conductorDto = new ConductorMasterDto();
				TicketBoxMasterDto ticketBoxDto = new TicketBoxMasterDto();
				ETMMasterDto etmDto = new ETMMasterDto();
				Object[] sum = issueEtmTicketBoxRepo.fetchAmountSumByIssueEtmId(entity.getId());
				if(sum[0] != null && sum.length > 0){
					dto.setAmountSum(sum[0].toString());
				}
				DailyRoasterDto dailyDto = new DailyRoasterDto();
				RouteOnlineBookingDetailsDto routeOnlineBookingDetailsDto = new RouteOnlineBookingDetailsDto();
				List<OnlineBookingDetailsDto> onlineBookingList = new ArrayList<>();
				TicketManagementBoxParentDto tmbprDto = new TicketManagementBoxParentDto();
				BusTypeDto busTypeDto = new BusTypeDto();
				dto.setId(entity.getId());
				dto.setWayBillNo(entity.getWayBillNo());
				conductorDto.setId(entity.getConductorMaster().getId());
				conductorDto.setConductorName(entity.getConductorMaster().getConductorName());
				conductorDto.setConductorCode(entity.getConductorMaster().getConductorCode());	
				ticketBoxDto.setId(entity.getTicketBoxNumber()!=null ?entity.getTicketBoxNumber().getId():null);
				ticketBoxDto.setTicketBoxNumber(entity.getTicketBoxNumber()!=null ? entity.getTicketBoxNumber().getTicketBoxNumber(): null);
				
				etmDto.setId(entity.getEtmMaster().getId());
				etmDto.setEtmNumber(entity.getEtmMaster().getEtmNumber());
				RouteMasterDto routeDto = new RouteMasterDto();
				routeDto.setRouteName(entity.getDailyRoaster()!=null && entity.getDailyRoaster().getRoute()!=null ? entity.getDailyRoaster().getRoute().getRouteName(): " ");
				routeDto.setId(entity.getDailyRoaster()!=null && entity.getDailyRoaster().getRoute()!=null ? entity.getDailyRoaster().getRoute().getId() : null);
				dailyDto.setRoute(routeDto);
				BusMasterDto busDto =  new BusMasterDto();
				busDto.setBusRegNumber(entity.getDailyRoaster()!=null && entity.getDailyRoaster().getBus()!=null  ? entity.getDailyRoaster().getBus().getBusRegNumber() : " ");
				busDto.setId(entity.getDailyRoaster()!=null && entity.getDailyRoaster().getBus()!=null  ? entity.getDailyRoaster().getBus().getId() : null);
				busTypeDto.setBusTypeName(entity.getDailyRoaster()!=null && entity.getDailyRoaster().getBus() !=null && entity.getDailyRoaster().getBus().getBusType()!=null  ?entity.getDailyRoaster().getBus().getBusType().getBusTypeName() : " ");
				busDto.setBusType(busTypeDto);
				DriverMasterDto driverDto =new DriverMasterDto();
				driverDto.setDriverName(entity.getDailyRoaster()!=null ? entity.getDailyRoaster().getDriver().getDriverName() : " ");
				dailyDto.setDriver(driverDto); 
				dailyDto.setBus(busDto);
//				
//				dailyDto.setId(entity.getDailyRoaster().getId());
				
				//Setter for Route Information and Online Booking Information

//				routeOnlineBookingDetailsDto.setId(entity.getDailyRoaster().getId());
//				routeOnlineBookingDetailsDto.setRouteId(entity.getDailyRoaster().getRoute().getRouteId());
//				routeOnlineBookingDetailsDto.setRouteCode(entity.getDailyRoaster().getRoute().getRouteCode());
//				routeOnlineBookingDetailsDto.setRouteName(entity.getDailyRoaster().getRoute().getRouteName());
//				routeOnlineBookingDetailsDto.setScheduledKms(entity.getDailyRoaster().getRoute().getScheduledKms());
//				routeOnlineBookingDetailsDto.setBusRegNumber(entity.getDailyRoaster().getBus().getBusRegNumber());
//				
//				busTypeDto.setId(entity.getDailyRoaster().getBus().getBusType().getId());
//				busTypeDto.setBusTypeName(entity.getDailyRoaster().getBus().getBusType().getBusTypeName());
//				routeOnlineBookingDetailsDto.setBusType(busTypeDto);
//				routeOnlineBookingDetailsDto.setDriverId(entity.getDailyRoaster().getDriver().getId());
//				routeOnlineBookingDetailsDto.setDriverCode(entity.getDailyRoaster().getDriver().getDriverCode());
//				routeOnlineBookingDetailsDto.setDriverName(entity.getDailyRoaster().getDriver().getDriverName());
//				routeOnlineBookingDetailsDto.setConductorId(entity.getDailyRoaster().getConductor().getId());
//				routeOnlineBookingDetailsDto.setConductorName(entity.getDailyRoaster().getConductor().getConductorName());
//				List<OnlineBookingDetails> ltOnlineBookingDetails =bookingRepo.findByTripId(entity.getDailyRoaster().getTrip());
//				if(ltOnlineBookingDetails.size()>0){
//					for (int j=0; j<ltOnlineBookingDetails.size(); j++){
//						OnlineBookingDetailsDto onlineBookingDetailsDto = new OnlineBookingDetailsDto();
//						onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
//						onlineBookingDetailsDto.setAmount(ltOnlineBookingDetails.get(j).getAmount());
//						onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
//						onlineBookingDetailsDto.setFromCityCode(ltOnlineBookingDetails.get(j).getFromCity().getCityName());
//						onlineBookingDetailsDto.setToCityCode(ltOnlineBookingDetails.get(j).getToCity().getCityName());
//						onlineBookingDetailsDto.setSeatsCount(ltOnlineBookingDetails.get(j).getSeatsCount());
//						onlineBookingList.add(onlineBookingDetailsDto);
//					}
//				}
				
				//Setter for Ticket Box Data Information
				
//				List<TicketBoxManagementDto> boxManagementDtos = new ArrayList<>();
//				for(TicketBoxManagementEntity entity2: entity.getTicketBoxManagementParentEntity().getTicketBoxManagementList()){
//					TicketBoxManagementDto boxManagementDto = new TicketBoxManagementDto();
//					boxManagementDto.setId(entity2.getId());
//					boxManagementDto.setAmount(entity2.getAmount());
//					boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
//					boxManagementDto.setEndingSerialNo(entity2.getEndingSerialNo());
//					boxManagementDto.setDeno(entity2.getDenomination().getDenomination());
//					boxManagementDto.setTransportUnitMaster(entity2.getTransportUnitMaster().getTransportName());
//					boxManagementDto.setTicketBox(entity2.getTicketBoxNumber().getTicketBoxNumber());
//					//totalValue = totalValue + entity2.getAmount();
//					boxManagementDtos.add(boxManagementDto);
//				}
//				tmbprDto.setId(entity.getId());
//				tmbprDto.setTicketBoxManagementList(boxManagementDtos);
//				//tmbprDto.setTicketBox(boxManagementDtos.get(0).getTicketBox());
//				tmbprDto.setTransportUnitMaster(boxManagementDtos.get(0).getTransportUnitMaster());
				//tmbprDto.setTotalValue(totalValue);

				//dto.setTicketManagementBoxParentDto(tmbprDto);
				//dto.setRouteOnlineBookingDetailsDto(routeOnlineBookingDetailsDto);
				dto.setTicketBoxMasterDto(ticketBoxDto);
				dto.setEtmMasterDto(etmDto);
				dto.setConductorMasterDto(conductorDto);
				dto.setManualEntry(entity.getManualEntry());
				dto.setDailyRoasterDto(dailyDto);
				dtosList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtosList;
	}

	@Override
	public IssueEtmTicketBoxDto getIssueEtmTicketBoxList(Integer id) {
		IssueEtmTicketBoxEntityChildDto childDto = null;
		List<IssueEtmTicketBoxEntityChildDto> childDtoList = new ArrayList<>();
		try {
			Optional<IssueEtmTicketBoxEntity> issueEtmTkt = issueEtmTicketBoxRepo.findById(id);
			if (issueEtmTkt.isPresent()) {
				IssueEtmTicketBoxDto issueEtmTktBoxDto = new IssueEtmTicketBoxDto();
				issueEtmTktBoxDto.setId(issueEtmTkt.get().getId());
				issueEtmTktBoxDto.setWayBillNo(issueEtmTkt.get().getWayBillNo());
				issueEtmTktBoxDto.setConductorMaster(issueEtmTkt.get().getConductorMaster().getId());	
				TicketBoxMasterDto ticketBoxDto = new TicketBoxMasterDto();
				ticketBoxDto.setId(issueEtmTkt.get().getTicketBoxNumber().getId());
				ticketBoxDto.setTicketBoxNumber(issueEtmTkt.get().getTicketBoxNumber().getTicketBoxNumber());
				ConductorMasterDto conductorDto = new ConductorMasterDto();
				conductorDto.setId(issueEtmTkt.get().getConductorMaster().getId());
				conductorDto.setConductorName(issueEtmTkt.get().getConductorMaster().getConductorName());
				conductorDto.setConductorCode(issueEtmTkt.get().getConductorMaster().getConductorCode());
				ETMMasterDto etmDto = new ETMMasterDto();
				etmDto.setId(issueEtmTkt.get().getEtmMaster().getId());
				etmDto.setEtmNumber(issueEtmTkt.get().getEtmMaster().getEtmNumber());
				Object[] sum = issueEtmTicketBoxRepo.fetchAmountSumByIssueEtmId(issueEtmTkt.get().getId());
				if(sum[0] != null && sum.length > 0){
					issueEtmTktBoxDto.setAmountSum(sum[0].toString());
				}
				Optional<DepotMaster> depoMaster = depotMasterRepository.findById(issueEtmTkt.get().getId());
				if (depoMaster.isPresent()) {
					issueEtmTktBoxDto.setDpCode(depoMaster.get().getDepotCode());
				}
//				Optional<DailyRoaster> dailyRoaster = dailyRoasterRepo.findById(issueEtmTkt.get().getDailyRoaster().getId());
				RouteOnlineBookingDetailsDto rmd = new RouteOnlineBookingDetailsDto();
				if (issueEtmTkt.isPresent()) {
					Optional<RouteMaster> rm = routeMasterRepository.findById(issueEtmTkt.get().getDailyRoaster().getRoute().getId());
					rmd.setId(rm.get().getId());
					rmd.setRouteCode(rm.get().getRouteCode());
					rmd.setRouteId(rm.get().getRouteId());
					rmd.setRouteName(rm.get().getRouteName());
					rmd.setScheduledKms(rm.get().getScheduledKms());
					rmd.setBusRegNumber(issueEtmTkt.get().getDailyRoaster().getBus().getBusRegNumber());
					BusTyperMaster btm = busTyperMasterRepository.findById(issueEtmTkt.get().getDailyRoaster().getBus().getBusType().getId()).get();
					BusTypeDto btd = new BusTypeDto();
					BeanUtils.copyProperties(btm, btd);
					rmd.setBusType(btd);
					rmd.setDriverCode(issueEtmTkt.get().getDailyRoaster().getDriver().getDriverCode());
					rmd.setDriverId(issueEtmTkt.get().getDailyRoaster().getDriver().getId());
					rmd.setDriverName(issueEtmTkt.get().getDailyRoaster().getDriver().getDriverName());		
				}
				for(IssueEtmTicketBoxEntityChild chilObj : issueEtmTkt.get().getIssueEtmChildList()){
					childDto = new IssueEtmTicketBoxEntityChildDto();
					
					Optional<TicketBoxManagementEntity> ticketBoxManagementEntity =ticketBoxMasterRepository.findById(chilObj.getTicketBoxManagementEntity().getId());
					Optional<CentralTicketStock> centralTicketStock=centralTicketStockRepository.findById(ticketBoxManagementEntity.get().getCentralTicket().getId());
					childDto.setSeriesNumber(centralTicketStock.get().getSeriesNumber());
					
					if(ticketBoxManagementEntity.get().getIsBookletChecked()==false){
					childDto.setAmount(chilObj.getAmount());
					childDto.setCurrentLastNo(chilObj.getCurrentLastNo());
					childDto.setDenomination(chilObj.getDenomination());
					childDto.setEndingSerialNo(chilObj.getEndingSerialNo());
					childDto.setId(chilObj.getId());
					if(chilObj.getCurrentLastNo() != null)
					childDto.setStartingSerialNo(chilObj.getCurrentLastNo());
					else
						childDto.setStartingSerialNo(chilObj.getStartingSerialNo());
					childDto.setTransportUnitMasterName(chilObj.getTransportUnitMaster().getTransportName());
					
					childDtoList.add(childDto);
					}
				}
				issueEtmTktBoxDto.setDailyRoaster(issueEtmTkt.get().getDailyRoaster().getId());
				issueEtmTktBoxDto.setTicketBoxMasterDto(ticketBoxDto);
				issueEtmTktBoxDto.setEtmMasterDto(etmDto);
				issueEtmTktBoxDto.setConductorMasterDto(conductorDto);
				issueEtmTktBoxDto.setRouteOnlineBookingDetailsDto(rmd);
				issueEtmTktBoxDto.setChildTicketBoxEntityList(childDtoList);
				
				return issueEtmTktBoxDto;
			}
			
			
		} catch (Exception e) {
			log.info(e + "getIssueEtmTicketBoxList"); 
		}
		return null;
	}
	
	public ResponseEntity<ResponseStatus> assignTicketBox(Integer id, Integer ticketBox,Integer ticketBoxManagement) {
		List<TicketBoxManagementEntity> ticketBoxManagementEntity = new ArrayList<>();
		List<IssueEtmTicketBoxEntityChild> issueEtmChildList = new ArrayList<>();
		IssueEtmTicketBoxEntityChild childObj = null;
		

		Optional<TicketBoxManagementParentEntity> ticketBoxManagementParentEntity = ticketBoxManagementRepo
				.findById(ticketBoxManagement);
		Optional<IssueEtmTicketBoxEntity> getRecord = issueEtmTicketBoxRepo.findById(id);
		Optional<TicketBoxMaster> ticketBoxRecord = ticketBoxRepository.findById(ticketBox);

		if (ticketBoxManagementParentEntity.isPresent()) {
			ticketBoxManagementEntity = ticketBoxMasterRepository
					.findByParentId(ticketBoxManagementParentEntity.get().getId());
		}
		try {
			if (getRecord.isPresent()) {
				getRecord.get().setTicketBoxNumber(ticketBoxRecord.get());
				getRecord.get().setManualEntry(true);
				getRecord.get().setTicketBoxManagementParentEntity(ticketBoxManagementParentEntity.get());  
				if (ticketBoxManagementEntity.size() > 0)
					for (TicketBoxManagementEntity entityObj : ticketBoxManagementEntity) {
						childObj = new IssueEtmTicketBoxEntityChild();
						childObj.setIssueEtmTicketBoxEntity(getRecord.get());
						childObj.setAmount(entityObj.getCurrentLastNo()!= null ? entityObj.getCurrentamount():entityObj.getAmount());
						childObj.setStartingSerialNo(entityObj.getCurrentLastNo()!=null ? entityObj.getCurrentLastNo() :entityObj.getStartingSerialNo());
						if (entityObj.getDenomination() != null)
							childObj.setDenomination(entityObj.getDenomination().getDenomination());
						childObj.setEndingSerialNo(entityObj.getEndingSerialNo());
						childObj.setTransportUnitMaster(entityObj.getTransportUnitMaster());
//						childObj.setStartingSerialNo(entityObj.getStartingSerialNo());
						childObj.setTicketBoxManagementEntity(entityObj);
						issueEtmChildList.add(childObj);
					}
				getRecord.get().setIssueEtmChildList(issueEtmChildList);
				issueEtmTicketBoxRepo.save(getRecord.get());
				Optional<TicketBoxMaster> ticketBoxMaster = ticketBoxRepository.findById(ticketBox);

				return new ResponseEntity<>(new ResponseStatus("Ticket box is added successfully", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ResponseStatus("Some thing went wrong", HttpStatus.OK), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Exception occured.", HttpStatus.OK), HttpStatus.OK);
		}
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
