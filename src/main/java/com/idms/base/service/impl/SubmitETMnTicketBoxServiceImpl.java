package com.idms.base.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.ConcessionTicketDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DailyRoasterDto;
import com.idms.base.api.v1.model.dto.ETMMasterDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxEntityChildDto;
import com.idms.base.api.v1.model.dto.OnlineBookingDetailsDto;
import com.idms.base.api.v1.model.dto.RouteOnlineBookingDetailsDto;
import com.idms.base.api.v1.model.dto.SubmitConcessionTicketDto;
import com.idms.base.api.v1.model.dto.SubmitEtmTicketBoxDto;
import com.idms.base.api.v1.model.dto.SubmitEtmTicketViewDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementDto;
import com.idms.base.api.v1.model.dto.TicketBoxMasterDto;
import com.idms.base.api.v1.model.dto.TicketManagementBoxParentDto;
import com.idms.base.dao.entity.CentralTicketStock;
import com.idms.base.dao.entity.ConcessionTicketEntity;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.ConductorRotaHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntity;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntityChild;
import com.idms.base.dao.entity.OnlineBookingDetails;
import com.idms.base.dao.entity.SubmitConcessionTicketEntity;
import com.idms.base.dao.entity.SubmitEtmTicketBoxChild;
import com.idms.base.dao.entity.SubmitEtmTicketBoxEntity;
import com.idms.base.dao.entity.TicketBoxManagementEntity;
import com.idms.base.dao.entity.TicketBoxManagementEntityHistory;
import com.idms.base.dao.entity.TicketBoxManagementParentEntity;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.notificationEntity;
import com.idms.base.dao.repository.CentralTicketStockRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.ConductorRotaHistoryRepository;
import com.idms.base.dao.repository.DenominationRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.EtmTBAssignmentRepository;
import com.idms.base.dao.repository.FuelNotificationRepository;
import com.idms.base.dao.repository.IssueEtmTicketBoxRepository;
import com.idms.base.dao.repository.OnlineBookingDetailsRepository;
import com.idms.base.dao.repository.SubmitEtmTicketBoxRepository;
import com.idms.base.dao.repository.TicketBoxManagementEntityHistoryRepo;
import com.idms.base.dao.repository.TicketBoxManagementParentRepository;
import com.idms.base.dao.repository.TicketBoxManagementRepository;
import com.idms.base.service.SubmitETMnTicketBoxService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;
import com.idms.base.util.AlertUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SubmitETMnTicketBoxServiceImpl implements SubmitETMnTicketBoxService  {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ConductorMasterRepository conductorMasterRepository;

	@Autowired
	private IssueEtmTicketBoxRepository issueEtmTicketBoxRepo;

	@Autowired
	private OnlineBookingDetailsRepository bookingRepo;

	@Autowired
	ConcessionTicketRepository concessionTicketRepo;
	
	@Autowired
	private SubmitEtmTicketBoxRepository submitEtmTicketBoxRepo;
	
	@Autowired
	TicketBoxManagementParentRepository tbmpRepo;
	
	@Autowired
	TicketBoxManagementEntityHistoryRepo history;
	
	@Autowired
	TicketBoxManagementRepository ticketBoxMasterRepository;
	
	@Autowired
	ConductorRotaHistoryRepository conductorRotaHistoryRepository;
	
	@Autowired
	EtmTBAssignmentRepository etmRepository;

	@Autowired
	private CentralTicketStockRepository centralTicketStockRepository;
	@Autowired
	DepotMasterRepository depoRepo;
	@Autowired
	DenominationRepository denominationRepo;
	@Autowired
	AlertUtility alert;
	@Autowired
	FuelNotificationRepository fuelNotificationRepo;
	
	@Override
	public List<ConductorMaster> getAllConductorMasterByDpcode(String dpCode) {
		
		log.info("Entering into getAllConductorMaster service");
		List<ConductorMaster> conductorsMasterList = null;
		try {
			conductorsMasterList = conductorMasterRepository.findAllByDepot(dpCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conductorsMasterList;
	}

	@Override
	public IssueEtmTicketBoxDto getIssueTicketDetails(Integer conductorId) {
		IssueEtmTicketBoxDto dto = new IssueEtmTicketBoxDto();
		ConductorMasterDto conductorDto = new ConductorMasterDto();
		TicketBoxMasterDto ticketBoxDto = new TicketBoxMasterDto();
		ETMMasterDto etmDto = new ETMMasterDto();
		DailyRoasterDto dailyDto = new DailyRoasterDto();
		RouteOnlineBookingDetailsDto routeOnlineBookingDetailsDto = new RouteOnlineBookingDetailsDto();
		List<OnlineBookingDetailsDto> onlineBookingList = new ArrayList<>();
		TicketManagementBoxParentDto tmbprDto = new TicketManagementBoxParentDto();
		try {
			IssueEtmTicketBoxEntity entity = issueEtmTicketBoxRepo.getConductorId(conductorId);
			EtmTBAssignment etmData = etmRepository.findByWaybillNo(entity.getWayBillNo());
			dto.setId(entity.getId());
			dto.setWayBillNo(entity.getWayBillNo());
			//conductorDto.
			ticketBoxDto.setId(entity.getTicketBoxNumber().getId());
			ticketBoxDto.setTicketBoxNumber(entity.getTicketBoxNumber().getTicketBoxNumber());
			etmDto.setId(entity.getEtmMaster().getId());
			etmDto.setEtmNumber(entity.getEtmMaster().getEtmNumber());
			dailyDto.setId(entity.getDailyRoaster().getId());
			Object[] sum = issueEtmTicketBoxRepo.fetchAmountSumByIssueEtmId(entity.getId());
			if(sum[0] != null && sum.length > 0){
				dto.setAmountSum(sum[0].toString());
			}
			//dailyDto.setRoute(entity.get);

			//Setter for Route Information and Online Booking Information

			routeOnlineBookingDetailsDto.setId(entity.getDailyRoaster().getId());
			routeOnlineBookingDetailsDto.setRouteId(entity.getDailyRoaster().getRoute().getRouteId());
			routeOnlineBookingDetailsDto.setRouteCode(entity.getDailyRoaster().getRoute().getRouteCode());
			routeOnlineBookingDetailsDto.setRouteName(entity.getDailyRoaster().getRoute().getRouteName());
			routeOnlineBookingDetailsDto.setScheduledKms(entity.getDailyRoaster().getRoute().getScheduledKms());
			routeOnlineBookingDetailsDto.setBusRegNumber(entity.getDailyRoaster().getBus().getBusRegNumber());
			//		busTypeDto.setBusTypeName(entity.getDailyRoaster().getBus().getBusType().getBusTypeName());
			//		routeOnlineBookingDetailsDto.setBusType(busTypeDto);
			routeOnlineBookingDetailsDto.setDriverId(entity.getDailyRoaster().getDriver().getId());
			routeOnlineBookingDetailsDto.setDriverCode(entity.getDailyRoaster().getDriver().getDriverCode());
			routeOnlineBookingDetailsDto.setDriverName(entity.getDailyRoaster().getDriver().getDriverName());
			routeOnlineBookingDetailsDto.setConductorId(entity.getDailyRoaster().getConductor().getId());
			routeOnlineBookingDetailsDto.setConductorName(entity.getDailyRoaster().getConductor().getConductorName());
			List<OnlineBookingDetails> ltOnlineBookingDetails =bookingRepo.findByTripId(entity.getDailyRoaster().getTrip());
			if(ltOnlineBookingDetails.size()>0){
				for (int j=0; j<ltOnlineBookingDetails.size(); j++){
					OnlineBookingDetailsDto onlineBookingDetailsDto = new OnlineBookingDetailsDto();
					onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
					onlineBookingDetailsDto.setAmount(ltOnlineBookingDetails.get(j).getAmount());
					onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
					onlineBookingDetailsDto.setFromCityCode(ltOnlineBookingDetails.get(j).getFromCity().getCityName());
					onlineBookingDetailsDto.setToCityCode(ltOnlineBookingDetails.get(j).getToCity().getCityName());
					onlineBookingDetailsDto.setSeatsCount(ltOnlineBookingDetails.get(j).getSeatsCount());
					onlineBookingList.add(onlineBookingDetailsDto);
				}
			}

			//Setter for Ticket Box Data Information

			List<TicketBoxManagementDto> boxManagementDtos = new ArrayList<>();
			/*for(TicketBoxManagementEntity entity2: entity.getTicketBoxManagementParentEntity().getTicketBoxManagementList()){
				TicketBoxManagementDto boxManagementDto = new TicketBoxManagementDto();
				boxManagementDto.setId(entity2.getId());
				boxManagementDto.setAmount(entity2.getAmount());
				boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
				boxManagementDto.setEndingSerialNo(entity2.getEndingSerialNo());
				boxManagementDto.setDeno(entity2.getDenomination().getDenomination());
				boxManagementDto.setTransportUnitMaster(entity2.getTransportUnitMaster().getTransportName());
				//boxManagementDto.setTicketBox(entity2.getTicketBoxNumber().getTicketBoxNumber());
				//totalValue = totalValue + entity2.getAmount();
				boxManagementDtos.add(boxManagementDto);
			}*/
			for(IssueEtmTicketBoxEntityChild entity2 : entity.getIssueEtmChildList()){
				TicketBoxManagementDto boxManagementDto = new TicketBoxManagementDto();
				boxManagementDto.setId(entity2.getId());
				boxManagementDto.setAmount(entity2.getAmount());
				if(entity2.getCurrentLastNo() != null)
				boxManagementDto.setStartingSerialNo(entity2.getCurrentLastNo());
				else
					boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
				boxManagementDto.setEndingSerialNo(entity2.getEndingSerialNo());
				boxManagementDto.setDeno(entity2.getDenomination());
				boxManagementDto.setTransportUnitMaster(entity2.getTransportUnitMaster().getTransportName());
				boxManagementDto.setTransportId(entity2.getTransportUnitMaster().getId());
				//boxManagementDto.setTicketBox(entity2.getTicketBoxNumber().getTicketBoxNumber());
				//totalValue = totalValue + entity2.getAmount();
				boxManagementDtos.add(boxManagementDto);
			}
			
			
			tmbprDto.setId(entity.getId());
			tmbprDto.setTicketBoxManagementList(boxManagementDtos);
			//tmbprDto.setTicketBox(boxManagementDtos.get(0).getTicketBox());
			tmbprDto.setTransportUnitMaster(boxManagementDtos.get(0).getTransportUnitMaster());
			//tmbprDto.setTotalValue(totalValue);

			dto.setTicketManagementBoxParentDto(tmbprDto);
			dto.setRouteOnlineBookingDetailsDto(routeOnlineBookingDetailsDto);
			dto.setTicketBoxMasterDto(ticketBoxDto);
			dto.setEtmMasterDto(etmDto);
			if(etmData.getEtmTripTotalDtls() !=null && etmData.getEtmTripTotalDtls().getTotalTripAmount() !=null)
			dto.setTotalAmount(etmData.getEtmTripTotalDtls().getTotalTripAmount().toString());
			else
				dto.setTotalAmount("0");
			if(etmData.getEtmTripTotalDtls() !=null && etmData.getEtmTripTotalDtls().getTotalDistance() !=null)
			dto.setTotalDist(etmData.getEtmTripTotalDtls().getTotalDistance());
			else
				dto.setTotalDist(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public List<ConcessionTicketDto> getConsessionTicketList() {
		try{

			List<ConcessionTicketDto> dto = concessionTicketRepo.findAll().stream()
					.map(ct -> modelMapper.map(ct, ConcessionTicketDto.class)).collect(Collectors.toList());
			return dto;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public ResponseEntity<ResponseStatus> submitEtmTicketBox(SubmitEtmTicketBoxDto submitEtmTicketBox) {
		// TODO Auto-generated method stub
		SubmitEtmTicketBoxEntity entity = new SubmitEtmTicketBoxEntity();
		List<SubmitConcessionTicketEntity> concessionTicketList = new ArrayList<>();
		//IssueEtmTicketBoxEntity issueEntity = new IssueEtmTicketBoxEntity();
		TicketBoxManagementParentEntity managementParentEntity = new TicketBoxManagementParentEntity();
		List<TicketBoxManagementEntity> managementEntitieList = new ArrayList<>();
		
		SubmitEtmTicketBoxChild childObj = null;
		List<SubmitEtmTicketBoxChild> childObjList = new ArrayList<>();
		try {
			Optional<IssueEtmTicketBoxEntity> issueEntity = issueEtmTicketBoxRepo.findById(submitEtmTicketBox.getIssueEtmTicketBoxDto().getId());
			issueEntity.get().setCondutorStatus(true);
			entity.setIssueEtmTicketBoxEntity(issueEntity.get());
			SubmitEtmTicketBoxEntity entity2 = submitEtmTicketBoxRepo.save(entity);
			
			List<TicketBoxManagementEntity> ticketBoxList = ticketBoxMasterRepository.findByParentId(issueEntity.get().getTicketBoxManagementParentEntity().getId());
			
			List<TicketBoxManagementEntityHistory> historyList = new ArrayList<>();
			//DenominationEntity denEntity = null;
			//for(TicketBoxManagementDto managementEntity: submitEtmTicketBox.getIssueEtmTicketBoxDto().getTicketManagementBoxParentDto().getTicketBoxManagementList()){
				for(TicketBoxManagementEntity managementEntity: ticketBoxList){
					//if(managementDto.getId() == managementEntity.getId()){
				        //denEntity = new DenominationEntity();
				        //denEntity.setId(managementEntity.getDeno());
						TicketBoxManagementEntityHistory his = new TicketBoxManagementEntityHistory();
						his.setTicketBoxMngId(managementEntity.getId());
						his.setAmount(managementEntity.getAmount());
						his.setCurrentLastNo(managementEntity.getCurrentLastNo());
						his.setDenomination(managementEntity.getDenomination());
						his.setEndingSerialNo(managementEntity.getEndingSerialNo());
						his.setStartingSerialNo(managementEntity.getStartingSerialNo());
						//his.setStatus(managementEntity.getStatus());
						his.setSubmitEtmTicketBoxEntity(entity);
						his.setTicketBoxManagementParent(entity.getIssueEtmTicketBoxEntity().getTicketBoxManagementParentEntity());
						his.setTicketBoxNumber(entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber());
						//his.setTransportUnitMaster(entity.getIssueEtmTicketBoxEntity().get);
						historyList.add(his);
					//}
				//}
				
			}
			entity.setTicketBoxHistoryList(historyList);
			
			/*for(TicketBoxManagementDto managementDto: submitEtmTicketBox.getIssueEtmTicketBoxDto().getTicketManagementBoxParentDto().getTicketBoxManagementList()){
				for(IssueEtmTicketBoxEntityChild managementEntity: issueEntity.get().getIssueEtmChildList() ){
					if(managementDto.getDeno() != null && managementEntity.getDenomination() != null && managementDto.getEndingSerialNo() != null && 
							managementEntity.getEndingSerialNo() != null && managementDto.getDeno() == managementEntity.getDenomination() &&
							managementDto.getEndingSerialNo()!= null &&  managementDto.getEndingSerialNo() == managementEntity.getEndingSerialNo() ){
						
						managementEntity.setCurrentLastNo(managementDto.getCurrentLastNo());
						managementEntity.setAmount(managementDto.getAmount());
						//managementEntitieList.add(managementEntity);
					}
				}
				
			}*/
			//managementParentEntity.setTicketBoxManagementList(managementEntitieList);
			//managementParentEntity.setId(issueEntity.get().getTicketBoxManagementParentEntity().getId());
			//TicketBoxManagementParentEntity boxManagementParentEntity = tbmpRepo.save(managementParentEntity);
			
			//code by hemant
			TransportUnitMaster tu = null;
			for(TicketBoxManagementDto managementDto: submitEtmTicketBox.getIssueEtmTicketBoxDto().getTicketManagementBoxParentDto().getTicketBoxManagementList()){
				childObj = new SubmitEtmTicketBoxChild();
				childObj.setAmount(managementDto.getAmount());
				childObj.setCurrentLastNo(managementDto.getCurrentLastNo());
				childObj.setDenomination(managementDto.getDeno());
				childObj.setEndingSerialNo(managementDto.getEndingSerialNo());
				childObj.setStartingSerialNo(managementDto.getStartingSerialNo());
				//childObj.setCurrentAmount(managementDto.getCurrentamount());
				childObj.setSubmitEtmTicketBox(entity);
				tu = new TransportUnitMaster();
				tu.setId(managementDto.getTransportId());
				childObj.setTransportUnitMaster(tu);
				
				if(managementDto.getIsBookletChecked()==true){
					//long longValue=0;
					childObj.setCurrentAmount((long) 0);
					childObj.setCurrentLastNo((long) 0);
					childObj.setIsBookletChecked(managementDto.getIsBookletChecked());
					}
				
				childObjList.add(childObj);
				
				
				/*for(TicketBoxManagementEntity managementEntity: issueEntity.get().getTicketBoxManagementParentEntity().getTicketBoxManagementList() ){
					if(managementDto.getId() == managementEntity.getId()){
						managementEntity.setCurrentLastNo(managementDto.getCurrentLastNo());
						managementEntity.setAmount(managementDto.getAmount());
						managementEntitieList.add(managementEntity);
					}
				}*/
				
			}
			
			entity.setSubmitEtmChildList(childObjList);
			
			//issueEntity.get().setTicketBoxManagementParentEntity(boxManagementParentEntity);
			//above line commented by hemant
			//entity.setIssueEtmTicketBoxEntity(issueEntity);
			for(SubmitConcessionTicketDto concessionTicketDto: submitEtmTicketBox.getConcessionTicketDto()){
				SubmitConcessionTicketEntity concessionTicketEntity = new SubmitConcessionTicketEntity();
				concessionTicketEntity.setTotalTickets(concessionTicketDto.getTotalTickets());
				concessionTicketEntity.setNetAmount(concessionTicketDto.getNetAmount());
				
				Optional<ConcessionTicketEntity> concessionTicketEntity2 = concessionTicketRepo.findById(concessionTicketDto.getId());
				concessionTicketEntity.setConcessionTicketEntity(concessionTicketEntity2.get());
				concessionTicketEntity.setSubmitEtmTicketBoxEntity(entity2);
				
				concessionTicketList.add(concessionTicketEntity);
			}
			entity2.setIssueEtmTicketBoxEntity(issueEntity.get());
			entity2.setConcessionTicketList(concessionTicketList);
			
			SubmitEtmTicketBoxEntity savedSubmit = submitEtmTicketBoxRepo.save(entity);
			
			List<ConductorRotaHistory> conductorHistoryList = conductorRotaHistoryRepository.findAllByDailyRoasterId(issueEntity.get().getDailyRoaster().getId());
			for(ConductorRotaHistory conductorObj : conductorHistoryList){
				conductorObj.setSubmitEtmTicketBoxEntity(savedSubmit);
				conductorRotaHistoryRepository.save(conductorObj);
			}
			String updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
			Timestamp updatedOn = new Timestamp(System.currentTimeMillis());
			for(TicketBoxManagementDto managementDto: submitEtmTicketBox.getIssueEtmTicketBoxDto().getTicketManagementBoxParentDto().getTicketBoxManagementList()){
				//for(SubmitEtmTicketBoxChild managementEntity: savedSubmit.getSubmitEtmChildList() ){
				for(IssueEtmTicketBoxEntityChild managementEntity: issueEntity.get().getIssueEtmChildList()){
					if(managementDto.getId() != null && managementEntity.getId() != null && managementDto.getId() == managementEntity.getId()){
							//managementEntity.getEndingSerialNo() != null && managementEntity.getStartingSerialNo() != null && managementDto.getDeno() == managementEntity.getDenomination() &&
						   // managementDto.getEndingSerialNo() == managementEntity.getEndingSerialNo() && managementDto.getStartingSerialNo() == managementEntity.getStartingSerialNo()){
						
						if(managementDto.getIsBookletChecked()==true){
							//long longValue=0;
							managementDto.setCurrentamount((long) 0);
							managementDto.setCurrentLastNo((long) 0);
							managementDto.setIsBookletChecked(managementDto.getIsBookletChecked());
							}
						ticketBoxMasterRepository.updateTicketBoxData(managementDto.getCurrentamount(),managementDto.getCurrentLastNo(),updatedBy,updatedOn,managementDto.getIsBookletChecked(),
								managementEntity.getTicketBoxManagementEntity().getId());
						//managementEntity.setCurrentLastNo(managementDto.getCurrentLastNo());
						//managementEntity.setAmount(managementDto.getAmount());
						//managementEntitieList.add(managementEntity);
						break;
					}
				}
				
			}return new ResponseEntity<>(
					new ResponseStatus("Record saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong.", HttpStatus.OK),
					HttpStatus.OK);
		}
		
		//return null;
	}

	@Override
	public SubmitEtmTicketBoxDto getSubmitEtmTicketBoxList(Integer id) {
		List<SubmitEtmTicketBoxDto> dtoList = new ArrayList<>();
//		IssueEtmTicketBoxDto issueDto = new IssueEtmTicketBoxDto();
		List<SubmitConcessionTicketDto> concessionList = new ArrayList<>();
		IssueEtmTicketBoxEntityChildDto childDto = null;
		List<IssueEtmTicketBoxEntityChildDto> childDtoList = new ArrayList<>();
		try {
			Optional<SubmitEtmTicketBoxEntity> entityObj = submitEtmTicketBoxRepo.findById(id);
			SubmitEtmTicketBoxEntity entity = entityObj.get();
			//for(SubmitEtmTicketBoxEntity entity: entityList){
				SubmitEtmTicketBoxDto dto = new SubmitEtmTicketBoxDto();
				IssueEtmTicketBoxDto issueDto = new IssueEtmTicketBoxDto();
				ConductorMasterDto conductorDto = new ConductorMasterDto();
				TicketBoxMasterDto ticketBoxDto = new TicketBoxMasterDto();
				ETMMasterDto etmDto = new ETMMasterDto();
				DailyRoasterDto dailyDto = new DailyRoasterDto();
				RouteOnlineBookingDetailsDto routeOnlineBookingDetailsDto = new RouteOnlineBookingDetailsDto();
				List<OnlineBookingDetailsDto> onlineBookingList = new ArrayList<>();
				TicketManagementBoxParentDto tmbprDto = new TicketManagementBoxParentDto();
	
				dto.setId(entity.getId());
				
				conductorDto.setId(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getId());
				conductorDto.setConductorName(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getConductorName());
				conductorDto.setConductorCode(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getConductorCode());
				
				issueDto.setId(entity.getIssueEtmTicketBoxEntity().getId());
				issueDto.setWayBillNo(entity.getIssueEtmTicketBoxEntity().getWayBillNo());
				//conductorDto.
				ticketBoxDto.setId(entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber().getId());
				ticketBoxDto.setTicketBoxNumber(entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber().getTicketBoxNumber());
				etmDto.setId(entity.getIssueEtmTicketBoxEntity().getEtmMaster().getId());
				etmDto.setEtmNumber(entity.getIssueEtmTicketBoxEntity().getEtmMaster().getEtmNumber());
				dailyDto.setId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getId());
				//dailyDto.setRoute(entity.get);

				//Setter for Route Information and Online Booking Information

				routeOnlineBookingDetailsDto.setId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getId());
				routeOnlineBookingDetailsDto.setRouteId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteId());
				routeOnlineBookingDetailsDto.setRouteCode(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteCode());
				routeOnlineBookingDetailsDto.setRouteName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteName());
				routeOnlineBookingDetailsDto.setScheduledKms(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getScheduledKms());
				routeOnlineBookingDetailsDto.setBusRegNumber(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus().getBusRegNumber());
				//		busTypeDto.setBusTypeName(entity.getDailyRoaster().getBus().getBusType().getBusTypeName());
				//		routeOnlineBookingDetailsDto.setBusType(busTypeDto);
				routeOnlineBookingDetailsDto.setDriverId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getId());
				routeOnlineBookingDetailsDto.setDriverCode(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getDriverCode());
				routeOnlineBookingDetailsDto.setDriverName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getDriverName());
				routeOnlineBookingDetailsDto.setConductorId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getConductor().getId());
				routeOnlineBookingDetailsDto.setConductorName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getConductor().getConductorName());
				List<OnlineBookingDetails> ltOnlineBookingDetails =bookingRepo.findByTripId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getTrip());
				if(ltOnlineBookingDetails.size()>0){
					for (int j=0; j<ltOnlineBookingDetails.size(); j++){
						OnlineBookingDetailsDto onlineBookingDetailsDto = new OnlineBookingDetailsDto();
						onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
						onlineBookingDetailsDto.setAmount(ltOnlineBookingDetails.get(j).getAmount());
						onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
						onlineBookingDetailsDto.setFromCityCode(ltOnlineBookingDetails.get(j).getFromCity().getCityName());
						onlineBookingDetailsDto.setToCityCode(ltOnlineBookingDetails.get(j).getToCity().getCityName());
						onlineBookingDetailsDto.setSeatsCount(ltOnlineBookingDetails.get(j).getSeatsCount());
						onlineBookingList.add(onlineBookingDetailsDto);
					}
				}

				//Setter for Ticket Box Data Information

				List<TicketBoxManagementDto> boxManagementDtos = new ArrayList<>();
				for(TicketBoxManagementEntity entity2: entity.getIssueEtmTicketBoxEntity().getTicketBoxManagementParentEntity().getTicketBoxManagementList()){
					TicketBoxManagementDto boxManagementDto = new TicketBoxManagementDto();
					if(entity2.getIsBookletChecked()==false){
					boxManagementDto.setId(entity2.getId());
					//boxManagementDto.setAmount(entity2.getAmount());
					//boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
					boxManagementDto.setEndingSerialNo(entity2.getEndingSerialNo());
					boxManagementDto.setDeno(entity2.getDenomination().getDenomination());
					boxManagementDto.setTransportUnitMaster(entity2.getTransportUnitMaster().getTransportName());
					boxManagementDto.setTransportId(entity2.getTransportUnitMaster().getId());
					if(entity2.getTicketBoxNumber() != null)
					boxManagementDto.setTicketBox(entity2.getTicketBoxNumber().getTicketBoxNumber());
					boxManagementDto.setCurrentLastNo(entity2.getCurrentLastNo());
					boxManagementDto.setBookSeries(entity2.getBookSeries());
					//if(entity2.getCurrentLastNo() == null)
						boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
					//else
						//boxManagementDto.setStartingSerialNo(entity2.getCurrentLastNo());
					//if(entity2.getCurrentamount() == null)
						boxManagementDto.setAmount(entity2.getAmount());
					//else
						boxManagementDto.setCurrentamount(entity2.getCurrentamount());
					//totalValue = totalValue + entity2.getAmount();
					boxManagementDtos.add(boxManagementDto);
					}
				}
				tmbprDto.setId(entity.getId());
				if(boxManagementDtos.size() != 0){
					tmbprDto.setTicketBoxManagementList(boxManagementDtos);
					//tmbprDto.setTicketBox(boxManagementDtos.get(0).getTicketBox());
					tmbprDto.setTransportUnitMaster(boxManagementDtos.get(0).getTransportUnitMaster());	
				}
				
				//tmbprDto.setTotalValue(totalValue);

				issueDto.setTicketManagementBoxParentDto(tmbprDto);
				issueDto.setRouteOnlineBookingDetailsDto(routeOnlineBookingDetailsDto);
				issueDto.setTicketBoxMasterDto(ticketBoxDto);
				issueDto.setEtmMasterDto(etmDto);
				issueDto.setConductorMasterDto(conductorDto);
				dto.setIssueEtmTicketBoxDto(issueDto);
				
				for(SubmitConcessionTicketEntity concessionTicketEntity: entity.getConcessionTicketList()){
					SubmitConcessionTicketDto concessionTicketDto = new SubmitConcessionTicketDto();
					ConcessionTicketDto ticketDto = new ConcessionTicketDto();
					
					concessionTicketDto.setId(concessionTicketEntity.getId());
					concessionTicketDto.setNetAmount(concessionTicketEntity.getNetAmount());
					concessionTicketDto.setTotalTickets(concessionTicketEntity.getTotalTickets());
					
					ticketDto.setId(concessionTicketEntity.getConcessionTicketEntity().getId());
					ticketDto.setPassType(concessionTicketEntity.getConcessionTicketEntity().getPassType());
					ticketDto.setDiscount(concessionTicketEntity.getConcessionTicketEntity().getDiscount());
					
					concessionTicketDto.setConcessionTicketDto(ticketDto);
					
					concessionList.add(concessionTicketDto);
				}
				dto.setConcessionTicketDto(concessionList);
				
				for(IssueEtmTicketBoxEntityChild chilObj : entity.getIssueEtmTicketBoxEntity().getIssueEtmChildList()){
					
					childDto = new IssueEtmTicketBoxEntityChildDto();
					/*if(chilObj.getAmount() == null)
						childDto.setAmount(chilObj.getCurrentAmount());
					else
					childDto.setAmount(chilObj.getAmount());*/
					
					childDto.setTicketBoxManagementEntity(chilObj.getTicketBoxManagementEntity().getId());
					Optional<TicketBoxManagementEntity> ticketBoxManagementEntity =ticketBoxMasterRepository.findById(chilObj.getTicketBoxManagementEntity().getId());
					Optional<CentralTicketStock> centralTicketStock=centralTicketStockRepository.findById(ticketBoxManagementEntity.get().getCentralTicket().getId());
					childDto.setIsBookletChecked(ticketBoxManagementEntity.get().getIsBookletChecked());
					
					if(childDto.getIsBookletChecked()==false){
						
					childDto.setSeriesNumber(centralTicketStock.get().getSeriesNumber());
					
					if(chilObj.getAmount() == null)
						childDto.setCurrentamount(chilObj.getAmount());
					else
						childDto.setCurrentamount(chilObj.getAmount());
					childDto.setAmount(chilObj.getAmount());
					childDto.setCurrentLastNo(chilObj.getCurrentLastNo());
					childDto.setDenomination(chilObj.getDenomination());
					childDto.setEndingSerialNo(chilObj.getEndingSerialNo());
					childDto.setId(chilObj.getId());
					if(chilObj.getCurrentLastNo() == null)
					childDto.setStartingSerialNo(chilObj.getStartingSerialNo());
					else
						childDto.setStartingSerialNo(chilObj.getCurrentLastNo());	
					//childDto.setCurrentamount(chilObj.getCurrentAmount());
					if(chilObj.getTransportUnitMaster() != null)
					childDto.setTransportUnitMasterName(chilObj.getTransportUnitMaster().getTransportName());
					childDto.setTransportId(chilObj.getTransportUnitMaster().getId());
					childDtoList.add(childDto);
					}
				}
				dto.setChildSubmitTicketBoxEntityList(childDtoList);
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getConductorMaster() != null);
				dto.setConductorName(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getConductorName());
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber() != null)
				dto.setTicketBoxNo(entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber().getTicketBoxNumber());
				if(entity.getIssueEtmTicketBoxEntity() != null)
				dto.setWayBillNo(entity.getIssueEtmTicketBoxEntity().getWayBillNo());
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getEtmMaster() != null)
				dto.setEtmNo(entity.getIssueEtmTicketBoxEntity().getEtmMaster().getEtmNumber());
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getDailyRoaster() != null){ 
					if(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute() != null)
						dto.setRoutename(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteName());
					if(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus() != null){
						dto.setBusRegistrationNo(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus().getBusRegNumber());
                        dto.setBusType(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus().getBusType().getBusTypeName());
					}
					if(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver() != null)
						dto.setDriverName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getDriverName());
				}
				
				Object[] sum = submitEtmTicketBoxRepo.fetchAmountSumBySubmitEtmId(id);
				if(sum[0] != null && sum.length > 0){
					dto.setAmountSum(sum[0].toString());
				}
				
				//dtoList.add(dto);
			//}
			return dto;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public List<SubmitEtmTicketViewDto> getAllSubmitEtmByDepot(Integer id) {
		SubmitEtmTicketViewDto viewObj = null;
		List<SubmitEtmTicketViewDto> viewList = new ArrayList<>();
		try{
			List<SubmitEtmTicketBoxEntity> submitList =  submitEtmTicketBoxRepo.getAllSubmitEtmByDepot(id);
			for(SubmitEtmTicketBoxEntity submitObj : submitList){
				viewObj = new SubmitEtmTicketViewDto();
				if(submitObj.getIssueEtmTicketBoxEntity().getConductorMaster() != null)
				viewObj.setConductorName(submitObj.getIssueEtmTicketBoxEntity().getConductorMaster().getConductorName());
				if(submitObj.getIssueEtmTicketBoxEntity().getEtmMaster() != null)
				viewObj.setEtmNo(submitObj.getIssueEtmTicketBoxEntity().getEtmMaster().getEtmNumber());
				viewObj.setSubmitid(submitObj.getId());
				viewObj.setWayBillNo(submitObj.getIssueEtmTicketBoxEntity().getWayBillNo());
				if(submitObj.getIssueEtmTicketBoxEntity().getTicketBoxNumber() != null)
				viewObj.setTicketBoxNo(submitObj.getIssueEtmTicketBoxEntity().getTicketBoxNumber().getTicketBoxNumber());
				if(submitObj.getIssueEtmTicketBoxEntity()!=null && submitObj.getIssueEtmTicketBoxEntity().getDailyRoaster() != null
						&& submitObj.getIssueEtmTicketBoxEntity().getDailyRoaster()!=null 
						&& submitObj.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute()!=null)
				viewObj.setRouteName(submitObj.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteName());		
				long amount=0;
				long currentAmount=0;
				for(IssueEtmTicketBoxEntityChild e : submitObj.getIssueEtmTicketBoxEntity().getIssueEtmChildList()) {
					amount=amount+e.getAmount();
				}
				for(SubmitEtmTicketBoxChild e: submitObj.getSubmitEtmChildList()) {
					currentAmount=currentAmount+e.getCurrentAmount();
				}
//				Object[] sum = submitEtmTicketBoxRepo.fetchAmountSumBySubmitEtmId(submitObj.getId());
//				if(sum[0] != null && sum.length > 0){
					viewObj.setAmountSum(String.valueOf(amount));
//				}
				viewObj.setManualTicketAmount(currentAmount);
				viewObj.setManualEntry(submitObj.getManualEntry());
				viewList.add(viewObj);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return viewList;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseEntity<ResponseStatus> submitTicketBox(List<TicketBoxManagementDto> ticketBoxDto) {
		SubmitEtmTicketBoxChild childObj = null;
		List<SubmitEtmTicketBoxChild> childObjList = new ArrayList<>();
		Optional<SubmitEtmTicketBoxEntity> record = submitEtmTicketBoxRepo.findById(ticketBoxDto.get(0).getRowId());
		try {
			if (record.isPresent()) {
				ticketAlertsOnSubmit();
				List<TicketBoxManagementEntity> ticketBoxList = ticketBoxMasterRepository.findByParentId(
						record.get().getIssueEtmTicketBoxEntity().getTicketBoxManagementParentEntity().getId());
				List<TicketBoxManagementEntityHistory> historyList = new ArrayList<>();
				for (TicketBoxManagementEntity managementEntity : ticketBoxList) {
					TicketBoxManagementEntityHistory his = new TicketBoxManagementEntityHistory();
					his.setTicketBoxMngId(managementEntity.getId());
					his.setAmount(managementEntity.getAmount());
					his.setCurrentLastNo(managementEntity.getCurrentLastNo());
					his.setDenomination(managementEntity.getDenomination());
					his.setEndingSerialNo(managementEntity.getEndingSerialNo());
					his.setStartingSerialNo(managementEntity.getStartingSerialNo());
					his.setSubmitEtmTicketBoxEntity(record.get());
					his.setTicketBoxManagementParent(
							record.get().getIssueEtmTicketBoxEntity().getTicketBoxManagementParentEntity());
					his.setTicketBoxNumber(record.get().getIssueEtmTicketBoxEntity().getTicketBoxNumber());
					historyList.add(his);
				}
				record.get().setTicketBoxHistoryList(historyList);
				TransportUnitMaster tu = null;
				TicketBoxManagementEntity ticketBoxManage=null;
				for (TicketBoxManagementDto managementDto : ticketBoxDto) {
					childObj = new SubmitEtmTicketBoxChild();
					childObj.setAmount(managementDto.getAmount());
					childObj.setCurrentLastNo(managementDto.getCurrentLastNo());
					childObj.setDenomination(managementDto.getDeno());
					childObj.setEndingSerialNo(managementDto.getEndingSerialNo());
					childObj.setStartingSerialNo(managementDto.getStartingSerialNo());
					childObj.setCurrentAmount(managementDto.getCurrentamount());
					childObj.setSubmitEtmTicketBox(record.get());
					childObj.setTotalTicketPunched(managementDto.getTotalTicketPunched());
					tu = new TransportUnitMaster();
					tu.setId(managementDto.getTransportId());
					childObj.setTransportUnitMaster(tu);
					
					ticketBoxManage = new TicketBoxManagementEntity();
					ticketBoxManage.setId(managementDto.getTicketBoxManagementEntity());
					childObj.setTicketBoxManagementEntity(ticketBoxManage);
					//childObj.setIsBookletChecked(managementDto.getCurrentLastNo()== null ? managementDto.getIsBookletChecked():false);
					if(managementDto.getIsBookletChecked()==true){
					//long longValue=0;
					childObj.setCurrentAmount((long) 0);
					childObj.setCurrentLastNo((long) 0);
					childObj.setIsBookletChecked(managementDto.getIsBookletChecked());
					}
					childObjList.add(childObj);

				}

				record.get().setSubmitEtmChildList(childObjList);
				String updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
				Timestamp updatedOn = new Timestamp(System.currentTimeMillis());
				for (TicketBoxManagementDto managementDto : ticketBoxDto) {
					// for(SubmitEtmTicketBoxChild managementEntity:
					// savedSubmit.getSubmitEtmChildList() ){
					for (IssueEtmTicketBoxEntityChild managementEntity : record.get().getIssueEtmTicketBoxEntity()
							.getIssueEtmChildList()) {
						if (managementDto.getId() != null && managementEntity.getId() != null
								&& managementDto.getId().equals(managementEntity.getId())) {
							// managementEntity.getEndingSerialNo() != null &&
							// managementEntity.getStartingSerialNo() != null &&
							// managementDto.getDeno() ==
							// managementEntity.getDenomination() &&
							// managementDto.getEndingSerialNo() ==
							// managementEntity.getEndingSerialNo() &&
							// managementDto.getStartingSerialNo() ==
							// managementEntity.getStartingSerialNo()){
							/*if(managementDto.getCurrentamount().equals("")){
								managementDto.setCurrentamount(null);
							}
							if(managementDto.getCurrentLastNo().equals("")){
								managementDto.setCurrentLastNo(null);
							}*/
							if(managementDto.getIsBookletChecked()==true){
								//long longValue=0;
								managementDto.setCurrentamount((long) 0);
								managementDto.setCurrentLastNo((long) 0);
								managementDto.setIsBookletChecked(managementDto.getIsBookletChecked());
								}
							ticketBoxMasterRepository.updateTicketBoxData(managementDto.getCurrentamount(),
									managementDto.getCurrentLastNo(),updatedBy,updatedOn,managementDto.getIsBookletChecked(),
									managementEntity.getTicketBoxManagementEntity().getId());
							// managementEntity.setCurrentLastNo(managementDto.getCurrentLastNo());
							// managementEntity.setAmount(managementDto.getAmount());
							// managementEntitieList.add(managementEntity);
							break;
						}
					}
				}
				record.get().setManualEntry(true);
				Optional<IssueEtmTicketBoxEntity> issueTicket = issueEtmTicketBoxRepo.findById(record.get().getIssueEtmTicketBoxEntity().getId());
				issueTicket.get().setCondutorStatus(true);
				issueEtmTicketBoxRepo.save(issueTicket.get());
				submitEtmTicketBoxRepo.save(record.get());
				
				return new ResponseEntity<>(new ResponseStatus("Record saved successfully.", HttpStatus.OK), HttpStatus.OK);
				
			} else {
				return new ResponseEntity<>(new ResponseStatus("Exception occured else", HttpStatus.OK), HttpStatus.OK);
			}
				} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>(new ResponseStatus("Exception occured", HttpStatus.OK), HttpStatus.OK);
				}
	}

	public SubmitEtmTicketBoxDto viewSubmitEtmTicketBoxList(Integer id) {
		List<SubmitEtmTicketBoxDto> dtoList = new ArrayList<>();
//		IssueEtmTicketBoxDto issueDto = new IssueEtmTicketBoxDto();
		List<SubmitConcessionTicketDto> concessionList = new ArrayList<>();
		IssueEtmTicketBoxEntityChildDto childDto = null;
		List<IssueEtmTicketBoxEntityChildDto> childDtoList = new ArrayList<>();
		try {
			Optional<SubmitEtmTicketBoxEntity> entityObj = submitEtmTicketBoxRepo.findById(id);
			SubmitEtmTicketBoxEntity entity = entityObj.get();
			//for(SubmitEtmTicketBoxEntity entity: entityList){
				SubmitEtmTicketBoxDto dto = new SubmitEtmTicketBoxDto();
				IssueEtmTicketBoxDto issueDto = new IssueEtmTicketBoxDto();
				ConductorMasterDto conductorDto = new ConductorMasterDto();
				TicketBoxMasterDto ticketBoxDto = new TicketBoxMasterDto();
				ETMMasterDto etmDto = new ETMMasterDto();
				DailyRoasterDto dailyDto = new DailyRoasterDto();
				RouteOnlineBookingDetailsDto routeOnlineBookingDetailsDto = new RouteOnlineBookingDetailsDto();
				List<OnlineBookingDetailsDto> onlineBookingList = new ArrayList<>();
				TicketManagementBoxParentDto tmbprDto = new TicketManagementBoxParentDto();
	
				dto.setId(entity.getId());
				
				conductorDto.setId(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getId());
				conductorDto.setConductorName(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getConductorName());
				conductorDto.setConductorCode(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getConductorCode());
				
				issueDto.setId(entity.getIssueEtmTicketBoxEntity().getId());
				issueDto.setWayBillNo(entity.getIssueEtmTicketBoxEntity().getWayBillNo());
				//conductorDto.
				ticketBoxDto.setId(entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber().getId());
				ticketBoxDto.setTicketBoxNumber(entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber().getTicketBoxNumber());
				etmDto.setId(entity.getIssueEtmTicketBoxEntity().getEtmMaster().getId());
				etmDto.setEtmNumber(entity.getIssueEtmTicketBoxEntity().getEtmMaster().getEtmNumber());
				dailyDto.setId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getId());
				//dailyDto.setRoute(entity.get);

				//Setter for Route Information and Online Booking Information

				routeOnlineBookingDetailsDto.setId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getId());
				routeOnlineBookingDetailsDto.setRouteId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteId());
				routeOnlineBookingDetailsDto.setRouteCode(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteCode());
				routeOnlineBookingDetailsDto.setRouteName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteName());
				routeOnlineBookingDetailsDto.setScheduledKms(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getScheduledKms());
				routeOnlineBookingDetailsDto.setBusRegNumber(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus().getBusRegNumber());
				//		busTypeDto.setBusTypeName(entity.getDailyRoaster().getBus().getBusType().getBusTypeName());
				//		routeOnlineBookingDetailsDto.setBusType(busTypeDto);
				routeOnlineBookingDetailsDto.setDriverId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getId());
				routeOnlineBookingDetailsDto.setDriverCode(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getDriverCode());
				routeOnlineBookingDetailsDto.setDriverName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getDriverName());
				routeOnlineBookingDetailsDto.setConductorId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getConductor().getId());
				routeOnlineBookingDetailsDto.setConductorName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getConductor().getConductorName());
				List<OnlineBookingDetails> ltOnlineBookingDetails =bookingRepo.findByTripId(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getTrip());
				if(ltOnlineBookingDetails.size()>0){
					for (int j=0; j<ltOnlineBookingDetails.size(); j++){
						OnlineBookingDetailsDto onlineBookingDetailsDto = new OnlineBookingDetailsDto();
						onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
						onlineBookingDetailsDto.setAmount(ltOnlineBookingDetails.get(j).getAmount());
						onlineBookingDetailsDto.setBookingId(ltOnlineBookingDetails.get(j).getBookingId());
						onlineBookingDetailsDto.setFromCityCode(ltOnlineBookingDetails.get(j).getFromCity().getCityName());
						onlineBookingDetailsDto.setToCityCode(ltOnlineBookingDetails.get(j).getToCity().getCityName());
						onlineBookingDetailsDto.setSeatsCount(ltOnlineBookingDetails.get(j).getSeatsCount());
						onlineBookingList.add(onlineBookingDetailsDto);
					}
				}

				//Setter for Ticket Box Data Information

				List<TicketBoxManagementDto> boxManagementDtos = new ArrayList<>();
				for(TicketBoxManagementEntity entity2: entity.getIssueEtmTicketBoxEntity().getTicketBoxManagementParentEntity().getTicketBoxManagementList()){
					TicketBoxManagementDto boxManagementDto = new TicketBoxManagementDto();
					
					if(entity2.getIsBookletChecked()==false){
					boxManagementDto.setId(entity2.getId());
					//boxManagementDto.setAmount(entity2.getAmount());
					//boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
					boxManagementDto.setEndingSerialNo(entity2.getEndingSerialNo());
					boxManagementDto.setDeno(entity2.getDenomination().getDenomination());
					boxManagementDto.setTransportUnitMaster(entity2.getTransportUnitMaster().getTransportName());
					boxManagementDto.setTransportId(entity2.getTransportUnitMaster().getId());
					if(entity2.getTicketBoxNumber() != null)
					boxManagementDto.setTicketBox(entity2.getTicketBoxNumber().getTicketBoxNumber());
					boxManagementDto.setCurrentLastNo(entity2.getCurrentLastNo());
					boxManagementDto.setBookSeries(entity2.getBookSeries());
					//if(entity2.getCurrentLastNo() == null)
						boxManagementDto.setStartingSerialNo(entity2.getStartingSerialNo());
					//else
						//boxManagementDto.setStartingSerialNo(entity2.getCurrentLastNo());
					//if(entity2.getCurrentamount() == null)
						boxManagementDto.setAmount(entity2.getAmount());
					//else
						boxManagementDto.setCurrentamount(entity2.getCurrentamount());
					//totalValue = totalValue + entity2.getAmount();
					boxManagementDtos.add(boxManagementDto);
					}
				}
				tmbprDto.setId(entity.getId());
				if(boxManagementDtos.size() != 0){
					tmbprDto.setTicketBoxManagementList(boxManagementDtos);
					//tmbprDto.setTicketBox(boxManagementDtos.get(0).getTicketBox());
					tmbprDto.setTransportUnitMaster(boxManagementDtos.get(0).getTransportUnitMaster());	
				}
				
				//tmbprDto.setTotalValue(totalValue);

				issueDto.setTicketManagementBoxParentDto(tmbprDto);
				issueDto.setRouteOnlineBookingDetailsDto(routeOnlineBookingDetailsDto);
				issueDto.setTicketBoxMasterDto(ticketBoxDto);
				issueDto.setEtmMasterDto(etmDto);
				issueDto.setConductorMasterDto(conductorDto);
				dto.setIssueEtmTicketBoxDto(issueDto);
				
				for(SubmitConcessionTicketEntity concessionTicketEntity: entity.getConcessionTicketList()){
					SubmitConcessionTicketDto concessionTicketDto = new SubmitConcessionTicketDto();
					ConcessionTicketDto ticketDto = new ConcessionTicketDto();
					
					concessionTicketDto.setId(concessionTicketEntity.getId());
					concessionTicketDto.setNetAmount(concessionTicketEntity.getNetAmount());
					concessionTicketDto.setTotalTickets(concessionTicketEntity.getTotalTickets());
					
					ticketDto.setId(concessionTicketEntity.getConcessionTicketEntity().getId());
					ticketDto.setPassType(concessionTicketEntity.getConcessionTicketEntity().getPassType());
					ticketDto.setDiscount(concessionTicketEntity.getConcessionTicketEntity().getDiscount());
					
					concessionTicketDto.setConcessionTicketDto(ticketDto);
					
					concessionList.add(concessionTicketDto);
				}
				dto.setConcessionTicketDto(concessionList);
				
				for(SubmitEtmTicketBoxChild chilObj : entity.getSubmitEtmChildList()){
					childDto = new IssueEtmTicketBoxEntityChildDto();
					/*if(chilObj.getAmount() == null)
						childDto.setAmount(chilObj.getCurrentAmount());
					else
					childDto.setAmount(chilObj.getAmount());*/
					if(chilObj.getCurrentLastNo() == null)
						childDto.setCurrentamount(chilObj.getAmount());
					else
						childDto.setCurrentamount(chilObj.getCurrentAmount());
					childDto.setAmount(chilObj.getAmount());
					childDto.setCurrentLastNo(chilObj.getCurrentLastNo());
					childDto.setDenomination(chilObj.getDenomination());
					childDto.setEndingSerialNo(chilObj.getEndingSerialNo());
					childDto.setId(chilObj.getId());
					if(chilObj.getCurrentLastNo() == null)
					childDto.setStartingSerialNo(chilObj.getStartingSerialNo());
					else if(chilObj.getCurrentLastNo() == 0)
					childDto.setStartingSerialNo(chilObj.getStartingSerialNo());
					else
					childDto.setStartingSerialNo(chilObj.getCurrentLastNo());	
					//childDto.setCurrentamount(chilObj.getCurrentAmount());
					if(chilObj.getTransportUnitMaster() != null)
					childDto.setTransportUnitMasterName(chilObj.getTransportUnitMaster().getTransportName());
					childDto.setTransportId(chilObj.getTransportUnitMaster().getId());
					
					childDto.setTicketBoxManagementEntity(chilObj.getTicketBoxManagementEntity().getId());
					Optional<TicketBoxManagementEntity> ticketBoxManagementEntity =ticketBoxMasterRepository.findById(chilObj.getTicketBoxManagementEntity().getId());
					Optional<CentralTicketStock> centralTicketStock=centralTicketStockRepository.findById(ticketBoxManagementEntity.get().getCentralTicket().getId());
					childDto.setSeriesNumber(centralTicketStock.get().getSeriesNumber());
					childDto.setIsBookletChecked(chilObj.getIsBookletChecked());
					childDtoList.add(childDto);
				}
				dto.setChildSubmitTicketBoxEntityList(childDtoList);
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getConductorMaster() != null);
				dto.setConductorName(entity.getIssueEtmTicketBoxEntity().getConductorMaster().getConductorName());
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber() != null)
				dto.setTicketBoxNo(entity.getIssueEtmTicketBoxEntity().getTicketBoxNumber().getTicketBoxNumber());
				if(entity.getIssueEtmTicketBoxEntity() != null)
				dto.setWayBillNo(entity.getIssueEtmTicketBoxEntity().getWayBillNo());
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getEtmMaster() != null)
				dto.setEtmNo(entity.getIssueEtmTicketBoxEntity().getEtmMaster().getEtmNumber());
				if(entity.getIssueEtmTicketBoxEntity() != null && entity.getIssueEtmTicketBoxEntity().getDailyRoaster() != null){ 
					if(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute() != null)
						dto.setRoutename(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getRoute().getRouteName());
					if(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus() != null){
						dto.setBusRegistrationNo(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus().getBusRegNumber());
                        dto.setBusType(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getBus().getBusType().getBusTypeName());
					}
					if(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver() != null)
						dto.setDriverName(entity.getIssueEtmTicketBoxEntity().getDailyRoaster().getDriver().getDriverName());
				}
				
				Object[] sum = submitEtmTicketBoxRepo.fetchAmountSumBySubmitEtmId(id);
				if(sum[0] != null && sum.length > 0){
					dto.setAmountSum(sum[0].toString());
				}
				
				//dtoList.add(dto);
			//}
			return dto;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void ticketAlertsOnSubmit() {
		String roleIds[] = RestConstants.AlertRoles;
		String TicketReorderAlert = "TRA";
		List<Object[]> allDepoName = submitEtmTicketBoxRepo.getAllDepoName();
		for (Object[] ob : allDepoName) {
			DepotMaster depoDtls = depoRepo.findByDepotCode(ob[0].toString());
			List<Object[]> allDenoValue = submitEtmTicketBoxRepo.getAllDenoName(ob[0].toString());
			for (Object[] obj : allDenoValue) {

				Integer denominationId = denominationRepo.getDenominationId(Integer.parseInt(obj[0].toString()));
				
				Integer depoId = depoDtls.getId();
				String depotCode = depoDtls.getDepotCode();
				Integer assignSubStockId = submitEtmTicketBoxRepo.getAssignSubStockId(depotCode,Integer.parseInt(obj[0].toString()));
				
				Long totalAmountBydenomination = submitEtmTicketBoxRepo.getTotalAmountBydenomination(depoId,
						denominationId);
				Long minValueOfTicket = submitEtmTicketBoxRepo.getMinValueOfTicket(depoId, denominationId);
				if (totalAmountBydenomination != null && minValueOfTicket != null) {

					if (totalAmountBydenomination < minValueOfTicket) {
						for (String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(assignSubStockId,TicketReorderAlert,s);
							if (d == null && s!=null && !s.isEmpty()) {
							/*alert.insertNotification(
									" In " + ob[0].toString() + " depo based on " + Integer.parseInt(obj[0].toString())
											+ "denomination  totalticketvalue is lessthen minvalue, we need to Reorder The Tickets",
									TicketReorderAlert, "Ticket", Integer.parseInt(ob[1].toString()), depoId, s);*/
								
								
								alert.insertNotification(ob[0].toString() +" depot, Denomination- " +Integer.parseInt(obj[0].toString()) + " tickets is less then required minimum value." +" Please order tickets of Denomination- "+Integer.parseInt(obj[0].toString()),
										TicketReorderAlert, "Ticket" ,assignSubStockId,depoId,s);

							}
						}
					}
				}

			}

		}
	}

}
