package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.BusStandBookingDtlsDto;
import com.idms.base.api.v1.model.dto.ConcessionDtlsDto;
import com.idms.base.api.v1.model.dto.ConductorEtmBookingDetailsDto;
import com.idms.base.api.v1.model.dto.ETMStatewiseCollectionDto;
import com.idms.base.api.v1.model.dto.EarningFromETMDto;
import com.idms.base.api.v1.model.dto.EtmTBAssignmentDto;
import com.idms.base.api.v1.model.dto.EtmTripDtlsDto;
import com.idms.base.api.v1.model.dto.EtmTripTotalDtlsDto;
import com.idms.base.api.v1.model.dto.IssueEtmTicketBoxEntityChildDto;
import com.idms.base.api.v1.model.dto.PassengerClassificationDto;
import com.idms.base.api.v1.model.dto.ResponseMessage;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.TicketBoxMasterDto;
import com.idms.base.api.v1.model.dto.TripExpensesDto;
import com.idms.base.dao.entity.ConcessionDtls;
import com.idms.base.dao.entity.ETMStatewiseCollection;
import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.dao.entity.EtmTripDtls;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntity;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntityChild;
import com.idms.base.dao.entity.SubmitEtmTicketBoxEntity;
import com.idms.base.dao.repository.EtmTBAssignmentRepository;
import com.idms.base.dao.repository.IssueEtmTicketBoxRepository;
import com.idms.base.dao.repository.SubmitEtmTicketBoxRepository;
import com.idms.base.service.ETMIntegrationService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ETMIntegrationServiceImpl implements ETMIntegrationService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	EtmTBAssignmentRepository etmAsgnRepo;
	
	@Autowired
	IssueEtmTicketBoxRepository issueTicketRepo;
	
	@Autowired
	SubmitEtmTicketBoxRepository submitEtmEntityRepo;

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public ResponseEntity<ResponseStatus> submitETMData(EtmTBAssignment etmTBAssignment) {
		try {
			
			String validateString = validateETMData(etmTBAssignment);
			
			if(!validateString.equals("success")) {
				return new ResponseEntity<>(new ResponseStatus(validateString, HttpStatus.FORBIDDEN), HttpStatus.OK);
			}

			EtmTBAssignment obj = etmAsgnRepo.findByWaybillNo(etmTBAssignment.getWayBillNo());
			if(obj==null) {
				return new ResponseEntity<>(new ResponseStatus(" detail not found.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			} else {
				List<SubmitEtmTicketBoxEntity> submitEtm = submitEtmEntityRepo.findByWaybillNo(etmTBAssignment.getWayBillNo());
				if(submitEtm.size()!=0) {
					return new ResponseEntity<>(new ResponseStatus(" is already entered! Please Upload Correct File.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				}else{
					
					etmTBAssignment.getConductorBookingDetails().setAssignment(obj);
					obj.setConductorBookingDetails(etmTBAssignment.getConductorBookingDetails());
					obj.setEtmSubmitDate(new Date());
					obj.setEtmSubmitStatus(true);
					
					etmTBAssignment.getBusStandBookingDtls().setAssignment(obj);
					obj.setBusStandBookingDtls(etmTBAssignment.getBusStandBookingDtls());
					
					etmTBAssignment.getTripExpenses().setAssignment(obj);
					obj.setTripExpenses(etmTBAssignment.getTripExpenses());
					
					etmTBAssignment.getPassengerClassification().setAssignment(obj);
					obj.setPassengerClassification(etmTBAssignment.getPassengerClassification());
					
					etmTBAssignment.getEarning().setAssignment(obj);
					obj.setEarning(etmTBAssignment.getEarning());
					
					for(ETMStatewiseCollection col : etmTBAssignment.getEtmStatewiseDtls()) {
						col.setAssignment(obj);
					}
					obj.setEtmStatewiseDtls(etmTBAssignment.getEtmStatewiseDtls());
					
					for(EtmTripDtls dtls : etmTBAssignment.getEtmTripDtls()) {
						dtls.setAssignment(obj);
					}
					obj.setEtmTripDtls(etmTBAssignment.getEtmTripDtls());
					
					
					etmTBAssignment.getEtmTripTotalDtls().setAssignment(obj);
					obj.setEtmTripTotalDtls(etmTBAssignment.getEtmTripTotalDtls());
					
					for(ConcessionDtls con : etmTBAssignment.getConcessionDtls()) {
						con.setAssignment(obj);
					}
					obj.setConcessionDtls(etmTBAssignment.getConcessionDtls());
					
					etmAsgnRepo.save(obj);
					SubmitEtmTicketBoxEntity a = new SubmitEtmTicketBoxEntity();
					a.setWayBillNo(obj.getWayBillNo());
					a.setConcessionTicketList(null);
					a.setIssueEtmTicketBoxEntity(null);
					a.setManualEntry(false);
					a.setSubmitEtmChildList(null);
					a.setTicketBoxHistoryList(null);
					a.setEtmAssignmentId(obj);
					a.setIssueEtmTicketBoxEntity(obj.getIssueEtmId());
					a.setIsFaulty(obj.getIsFaulty());
					submitEtmEntityRepo.save(a);
					return new ResponseEntity<>(
							new ResponseStatus(" details are saved successfully.", HttpStatus.OK),
							HttpStatus.OK);
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	private String validateETMData(EtmTBAssignment etmTBAssignment) {
		
		if(etmTBAssignment==null) {
			return "Please provide valid data.";
		}
		
		if(etmTBAssignment.getWayBillNo()==null || etmTBAssignment.getWayBillNo().trim().equals("")) {
			return "Please provide valid waybill number.";
		}
		
		if(etmTBAssignment.getConductorBookingDetails()==null) {
			return "Conductor booking details is mandatory";
		} else {
			if(etmTBAssignment.getConductorBookingDetails().getPassengerAmt()==null) {
				return "Passenger amount in Conductor booking detail should not be null.";
			}
			if(etmTBAssignment.getConductorBookingDetails().getLuggageAmt()==null) {
				return "Luggage amount in Conductor booking detail should not be null.";
			}
			if(etmTBAssignment.getConductorBookingDetails().getConcessionAmt()==null) {
				return "Concession amount in Conductor booking detail should not be null.";
			}
			if(etmTBAssignment.getConductorBookingDetails().getTollAmt()==null) {
				return "Toll amount in Conductor booking detail should not be null.";
			}
			if(etmTBAssignment.getConductorBookingDetails().getBoxAmt()==null) {
				return "Box amount in Conductor booking detail should not be null.";
			}
			if(etmTBAssignment.getConductorBookingDetails().getMiscAmt()==null) {
				return "Misc amount in Conductor booking detail should not be null.";
			}
			if(etmTBAssignment.getConductorBookingDetails().getTotalAmt()==null) {
				return "Total amount in Conductor booking detail should not be null.";
			}
		}
		
		if(etmTBAssignment.getBusStandBookingDtls()==null) {
			return "Bus Stand Booking details is mandatory";
		} else {
			if(etmTBAssignment.getBusStandBookingDtls().getPassengerCount()==null) {
				return "Passenger count in Bus Stand Booking details is mandatory";
			}
			if(etmTBAssignment.getBusStandBookingDtls().getAddaBookingAmt()==null) {
				return "Adda Booking amount in Bus Stand Booking details is mandatory";
			}
			if(etmTBAssignment.getBusStandBookingDtls().getAdvBookingAmtByEBTM()==null) {
				return "Advance Booking by EBTMt in Bus Stand Booking details is mandatory";
			}
			if(etmTBAssignment.getBusStandBookingDtls().getTotalAmt()==null) {
				return "Total amount in Bus Stand Booking details is mandatory";
			}
		}
		
		if(etmTBAssignment.getTripExpenses()==null) {
			return "Trip Expenses details is mandatory";
		} else {
			if(etmTBAssignment.getTripExpenses().getTollTax()==null) {
				return "Toll tax amount in Trip Expenses Details is mandatory";
			}
			if(etmTBAssignment.getTripExpenses().getTicketRefund()==null) {
				return "Ticket Refund amount in Trip Expenses Details is mandatory";
			}
			if(etmTBAssignment.getTripExpenses().getBusStandFee()==null) {
				return "Bus Stand Fee in Trip Expenses Details is mandatory";
			}
			if(etmTBAssignment.getTripExpenses().getDiesel()==null) {
				return "Diesel in Trip Expenses Details is mandatory";
			}
			if(etmTBAssignment.getTripExpenses().getRepairBill()==null) {
				return "Repair Bill in Trip Expenses Details is mandatory";
			}
			if(etmTBAssignment.getTripExpenses().getMiscFee()==null) {
				return "Misc fee in Trip Expenses Details is mandatory";
			}
			if(etmTBAssignment.getTripExpenses().getTotal()==null) {
				return "Total amount in Trip Expenses Details is mandatory";
			}
		}
		
		if(etmTBAssignment.getPassengerClassification()==null) {
			return "Passenger Classification details is mandatory.";
		} else {
			if(etmTBAssignment.getPassengerClassification().getPassengerCounterBooking()==null) {
				return "Passenger counter booking in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getAdvBooking()==null) {
				return "Advance booking in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getLuggageTicket()==null) {
				return "Luggage Ticket booking in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getPolicWarrent()==null) {
				return "Police Warrrent booking in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getConcessionTicket()==null) {
				return "Concession Ticket booking in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getTrafficInspection()==null) {
				return "Traffic Inspection booking in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getTicketAmount()==null) {
				return "Ticket amount in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getKmpl()==null) {
				return "KMPL in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getTotalAdults()==null) {
				return "Total Adults count in Passenger Classification details is mandatory.";
			}
			if(etmTBAssignment.getPassengerClassification().getTotalChilds()==null) {
				return "Total Child count in Passenger Classification details is mandatory.";
			}
		}
		
		if(etmTBAssignment.getConcessionDtls()==null) {
			return "Concession Details is mandatory.";
		} else {
			if(etmTBAssignment.getConcessionDtls().size()==0){
				return "Concession Details is mandatory.";
			} else {
				for(ConcessionDtls dtls : etmTBAssignment.getConcessionDtls()) {
					if(dtls.getPassType()==null) {
						return "Pass Type is mandatory in Concession Detials.";
					}
					if(dtls.getDiscPercentage()==null) {
						return "Discount Percentage is mandatory in Concession Detials.";
					}
					if(dtls.getTickets()==null) {
						return "Ticket count is mandatory in Concession Detials.";
					}
					if(dtls.getNetAmount()==null) {
						return "Net amount is mandatory in Concession Detials.";
					}
				}
			}
		}
		
		if(etmTBAssignment.getEarning()==null) {
			return "Earning Details is mandatory.";
		} else {
			if(etmTBAssignment.getEarning().getEarningPerKM()==null) {
				return "Earning Per KM in Earning details is mandatory.";
			}
			if(etmTBAssignment.getEarning().getEarningPerKmFree()==null) {
				return "Earning Per KM Free in Earning details is mandatory.";
			}
			if(etmTBAssignment.getEarning().getLoadFactor()==null) {
				return "Load Factor in Earning details is mandatory.";
			}
			if(etmTBAssignment.getEarning().getTotalRemittance()==null) {
				return "Total Remittance amount in Earning details is mandatory.";
			}
			if(etmTBAssignment.getEarning().getNetAmtToDeposit()==null) {
				return "Net amount to deposit in Earning details is mandatory.";
			}
			if(etmTBAssignment.getEarning().getInspectDtls()==null) {
				return "Inspection details in Earning details is mandatory.";
			}
			if(etmTBAssignment.getEarning().getTotalTicketIssued()==null) {
				return "Total ticket issued in Earning details is mandatory.";
			}
		}
		
		if(etmTBAssignment.getEtmStatewiseDtls()==null) {
			return "ETM Statewise details is mandatory.";
		} else {
			if(etmTBAssignment.getEtmStatewiseDtls().size()==0) {
				return "ETM Statewise details is mandatory.";
			} else {
				for(ETMStatewiseCollection stw : etmTBAssignment.getEtmStatewiseDtls()) {
					if(stw.getStateName()==null) {
						return "State name in Statewise collection details is mandatory.";
					}
					if(stw.getAmount()==null) {
						return "Amount in Statewise collection details is mandatory.";
					}
				}
			}
		}
		
		if(etmTBAssignment.getEtmTripDtls()==null) {
			return "ETM Trip details is mandatory.";
		} else {
			if(etmTBAssignment.getEtmTripDtls().size()==0) {
				return "ETM Trip details is mandatory.";
			} else {
				for(EtmTripDtls dtls : etmTBAssignment.getEtmTripDtls()) {
					if(dtls.getRouteName()==null) {
						return "Route Name in ETM Trip details is mandatory.";
					}
					if(dtls.getTripStartDateTime()==null) {
						return "Trip start date and time in ETM Trip details is mandatory.";
					}
					if(dtls.getTripEndDateTime()==null) {
						return "Trip end date and time in ETM Trip details is mandatory.";
					}
					if(dtls.getDistance()==null) {
						return "Distance in ETM Trip details is mandatory.";
					}
					if(dtls.getAdvBookingAmt()==null) {
						return "Advance booking amount in ETM Trip details is mandatory.";
					}
					if(dtls.getTotalCollection()==null) {
						return "Total collection in ETM Trip details is mandatory.";
					}
					if(dtls.getExpenses()==null) {
						return "Expenses in ETM Trip details is mandatory.";
					}
					if(dtls.getNetTripAmt()==null) {
						return "Net trip amount in ETM Trip details is mandatory.";
					}
				}
			}
		}
		
		if(etmTBAssignment.getEtmTripTotalDtls()==null) {
			return "ETM Trip total details is mandatory.";
		} else {
			if(etmTBAssignment.getEtmTripTotalDtls().getRecords()==null) {
				return "Records in ETM Trip Total details is mandatory.";
			}
			if(etmTBAssignment.getEtmTripTotalDtls().getTotalDistance()==null) {
				return "Total Distance in ETM Trip Total details is mandatory.";
			}
			if(etmTBAssignment.getEtmTripTotalDtls().getTotalAdvBookingAmt()==null) {
				return "Total advance booking amount in ETM Trip Total details is mandatory.";
			}
			if(etmTBAssignment.getEtmTripTotalDtls().getTotalCollection()==null) {
				return "Total collection in ETM Trip Total details is mandatory.";
			}
			if(etmTBAssignment.getEtmTripTotalDtls().getTotalExpenses()==null) {
				return "Total expenses in ETM Trip Total details is mandatory.";
			}
			if(etmTBAssignment.getEtmTripTotalDtls().getTotalTripAmount()==null) {
				return "Total trip amount in ETM Trip Total details is mandatory.";
			}
		}
		
		return "success";
	}

	public ResponseEntity<EtmTBAssignmentDto> fetchEtmDetails(String wayBillNo) {
		
		HttpStatus status = HttpStatus.OK;
		EtmTBAssignmentDto dto = new EtmTBAssignmentDto();
		ConductorEtmBookingDetailsDto conductorEtmDto = new ConductorEtmBookingDetailsDto();
		PassengerClassificationDto passengerDto = new PassengerClassificationDto();
		TripExpensesDto tripDto = new TripExpensesDto();
		BusStandBookingDtlsDto busStandDto = new BusStandBookingDtlsDto();
		EarningFromETMDto earningDto = new EarningFromETMDto();
		EtmTripTotalDtlsDto etmTripDto = new EtmTripTotalDtlsDto();
		List<EtmTripDtlsDto> etmTripListDto = new ArrayList<>();
		List<ConcessionDtlsDto> concessionListDto = new ArrayList<>();
		List<ETMStatewiseCollectionDto> etmStateWiseList = new ArrayList<>();
		EtmTBAssignment etm = new EtmTBAssignment();
		RouteMasterDto routeDto = new RouteMasterDto();  
		IssueEtmTicketBoxEntity issueTicket  =new IssueEtmTicketBoxEntity();
		TicketBoxMasterDto ticketInfo= new TicketBoxMasterDto();

		try {
		etm = etmAsgnRepo.findByWaybillNo(wayBillNo);
		issueTicket = issueTicketRepo.findByWayBillNo(etm.getWayBillNo());
		} catch (Exception e){
			System.out.println("waybill not found!!");
		}
		if(etm!=null){
			try {
				routeDto.setId(etm.getRouteId().getId());
				routeDto.setScheduledKms(etm.getRouteId().getScheduledKms());
				routeDto.setDeadKms(etm.getRouteId().getDeadKms());
			} catch (Exception e) {
				// TODO: handle exception
			}
		try {
			conductorEtmDto.setId(etm.getConductorBookingDetails().getId());
			conductorEtmDto.setBoxAmt(etm.getConductorBookingDetails().getBoxAmt());
			// conductorDto.setAssignment(etm.getConductorBookingDetails().getAssignment());
			conductorEtmDto.setConcessionAmt(etm.getConductorBookingDetails().getConcessionAmt());
			conductorEtmDto.setLuggageAmt(etm.getConductorBookingDetails().getLuggageAmt());
			conductorEtmDto.setMiscAmt(etm.getConductorBookingDetails().getMiscAmt());
			conductorEtmDto.setPassengerAmt(etm.getConductorBookingDetails().getPassengerAmt());
			conductorEtmDto.setTollAmt(etm.getConductorBookingDetails().getTollAmt());
			conductorEtmDto.setTotalAmt(etm.getConductorBookingDetails().getTotalAmt());
		} catch (Exception e) {
			System.out.println("error while feching conductor details");
		}
		try {
			passengerDto.setAdvBooking(etm.getPassengerClassification().getAdvBooking());
			passengerDto.setConcessionTicket(etm.getPassengerClassification().getConcessionTicket());
			passengerDto.setId(etm.getPassengerClassification().getId());
			passengerDto.setKmpl(etm.getPassengerClassification().getKmpl());
			passengerDto.setLuggageTicket(etm.getPassengerClassification().getLuggageTicket());
			passengerDto.setPassengerCounterBooking(etm.getPassengerClassification().getPassengerCounterBooking());
			passengerDto.setPolicWarrent(etm.getPassengerClassification().getPolicWarrent());
			passengerDto.setTicketAmount(etm.getPassengerClassification().getTicketAmount());
			passengerDto.setTotalAdults(etm.getPassengerClassification().getTotalAdults());
			passengerDto.setTotalChilds(etm.getPassengerClassification().getTotalChilds());
			passengerDto.setTrafficInspection(etm.getPassengerClassification().getTrafficInspection());
		} catch (Exception e) {
			System.out.println("error while feching passenger details");
		}

		try {
			tripDto.setId(etm.getTripExpenses().getId());
			tripDto.setBusStandFee(etm.getTripExpenses().getBusStandFee());
			tripDto.setDiesel(etm.getTripExpenses().getDiesel());
			tripDto.setMiscFee(etm.getTripExpenses().getMiscFee());
			tripDto.setRepairBill(etm.getTripExpenses().getRepairBill());
			tripDto.setTicketRefund(etm.getTripExpenses().getTicketRefund());
			tripDto.setTollTax(etm.getTripExpenses().getTollTax());
			tripDto.setTotal(etm.getTripExpenses().getTotal());
		} catch (Exception e) {
			System.out.println("error while feching trip details");
		}

		try {
			if (etm.getBusStandBookingDtls() != null) {
				busStandDto.setAddaBookingAmt(etm.getBusStandBookingDtls().getAddaBookingAmt() != null
						? etm.getBusStandBookingDtls().getAddaBookingAmt() : null);
				busStandDto.setAdvBookingAmtByEBTM(etm.getBusStandBookingDtls().getAdvBookingAmtByEBTM() != null
						? etm.getBusStandBookingDtls().getAdvBookingAmtByEBTM() : null);
				busStandDto.setId(etm.getBusStandBookingDtls().getId());
				busStandDto.setPassengerCount(etm.getBusStandBookingDtls().getPassengerCount() != null
						? etm.getBusStandBookingDtls().getPassengerCount() : null);
				busStandDto.setTotalAmt(etm.getBusStandBookingDtls().getTotalAmt() != null
						? etm.getBusStandBookingDtls().getTotalAmt() : null);
			}
		} catch (Exception e) {
			System.out.println("error while feching bus stand details");
		}
		try {
			earningDto.setEarningPerKM(etm.getEarning().getEarningPerKM());
			earningDto.setEarningPerKmFree(etm.getEarning().getEarningPerKmFree());
			earningDto.setId(etm.getEarning().getId());
			earningDto.setInspectDtls(etm.getEarning().getInspectDtls());
			earningDto.setLoadFactor(etm.getEarning().getLoadFactor());
			earningDto.setNetAmtToDeposit(etm.getEarning().getNetAmtToDeposit());
			earningDto.setTotalRemittance(etm.getEarning().getTotalRemittance());
			earningDto.setTotalTicketIssued(etm.getEarning().getTotalTicketIssued());
		} catch (Exception e) {
			System.out.println("error while feching earning details");
		}
		try {
			etmTripDto.setId(etm.getEtmTripTotalDtls().getId());
			etmTripDto.setRecords(etm.getEtmTripTotalDtls().getRecords());
			etmTripDto.setTotalAdvBookingAmt(etm.getEtmTripTotalDtls().getTotalAdvBookingAmt());
			etmTripDto.setTotalCollection(etm.getEtmTripTotalDtls().getTotalCollection());
			etmTripDto.setTotalDistance(etm.getEtmTripTotalDtls().getTotalDistance());
			etmTripDto.setTotalExpenses(etm.getEtmTripTotalDtls().getTotalExpenses());
			etmTripDto.setTotalTripAmount(etm.getEtmTripTotalDtls().getTotalTripAmount());
		} catch (Exception e) {
			System.out.println("error while feching etm total trip details");
		}
		try {
			for (EtmTripDtls etmtrip : etm.getEtmTripDtls()) {
				EtmTripDtlsDto etmtripdtls = new EtmTripDtlsDto();
				etmtripdtls.setAdvBookingAmt(etmtrip.getAdvBookingAmt());
				etmtripdtls.setDistance(etmtrip.getDistance());
				etmtripdtls.setEmpk(etmtrip.getEmpk());
				etmtripdtls.setExpenses(etmtrip.getExpenses());
				etmtripdtls.setId(etmtrip.getId());
				etmtripdtls.setNetTripAmt(etmtrip.getNetTripAmt());
				etmtripdtls.setRouteName(etmtrip.getRouteName());
				etmtripdtls.setTotalCollection(etmtrip.getTotalCollection());
				etmtripdtls.setTripEndDateTime(etmtrip.getTripEndDateTime());
				etmtripdtls.setTripStartDateTime(etmtrip.getTripStartDateTime());
				etmTripListDto.add(etmtripdtls);
			}
		} catch (Exception e) {
			System.out.println("error occured while fetching etm total list");
		}

		try {
			for (ConcessionDtls cDtls : etm.getConcessionDtls()) {
				ConcessionDtlsDto cDtlsDto = new ConcessionDtlsDto();
				cDtlsDto.setDiscPercentage(cDtls.getDiscPercentage());
				cDtlsDto.setId(cDtls.getId());
				cDtlsDto.setNetAmount(cDtls.getNetAmount());
				cDtlsDto.setPassType(cDtls.getPassType());
				cDtlsDto.setTickets(cDtls.getTickets());
				concessionListDto.add(cDtlsDto);
			}
		} catch (Exception e) {
			System.out.println("error occured while fetching concession detals list");
		}

		try {
			for (ETMStatewiseCollection etmState : etm.getEtmStatewiseDtls()) {
				ETMStatewiseCollectionDto etmStateWiseDto = new ETMStatewiseCollectionDto();
				etmStateWiseDto.setAmount(etmState.getAmount());
				etmStateWiseDto.setStateName(etmState.getStateName());
				etmStateWiseList.add(etmStateWiseDto);

			}
		} catch (Exception e) {
			System.out.println("error occured while fetching etm state wise detals list");
		}
		
		try {
			List<IssueEtmTicketBoxEntityChildDto> totalTickets= new ArrayList<>();
			for(IssueEtmTicketBoxEntityChild obj : issueTicket.getIssueEtmChildList()) {
				IssueEtmTicketBoxEntityChildDto child = new IssueEtmTicketBoxEntityChildDto();
				child.setAmount(obj.getAmount());
				child.setCurrentamount(obj.getCurrentLastNo());
				child.setCurrentLastNo(obj.getCurrentLastNo());
				child.setDenomination(obj.getDenomination());
				child.setEndingSerialNo(obj.getEndingSerialNo());
				child.setStartingSerialNo(obj.getStartingSerialNo());
				child.setId(obj.getId());
				child.setTransportUnitMasterName(obj.getTransportUnitMaster().getTransportName());
				child.setSeriesNumber(obj.getTicketBoxManagementEntity().getCentralTicket().getSeriesNumber());
				totalTickets.add(child);
			}
			dto.setTotalTicketIssued(totalTickets);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
		dto.setConductorName(etm.getConductorId().getConductorName());
		dto.setWayBillNo(etm.getWayBillNo());
		dto.setConductorRegNo(etm.getConductorId().getConductorCode());
		dto.setEtmSubmitDate(etm.getEtmSubmitDate());
		dto.setEtmSubmitStatus(etm.getEtmSubmitStatus());
		dto.setTbSubmitStatus(etm.getTbSubmitStatus());
		dto.setId(etm.getId());
		dto.setDepotId(etm.getConductorId().getDepot().getId().toString());
		dto.setDepotName(etm.getConductorId().getDepot().getDepotName());
		dto.setConductorBookingDetails(conductorEtmDto);
		dto.setRouteId(routeDto);
		dto.setBusRegNo(issueTicket.getDailyRoaster().getBus().getBusRegNumber());
		dto.setDriverName(issueTicket.getDailyRoaster().getBus().getPrimaryDriver().getDriverName());
		dto.setDriverRegNo(issueTicket.getDailyRoaster().getBus().getPrimaryDriver().getDriverCode());
		ticketInfo.setTicketBoxNumber(issueTicket.getTicketBoxNumber().getTicketBoxNumber());
		dto.setBusType(issueTicket.getDailyRoaster().getBus().getBusType().getBusTypeName());
		dto.setTicketBoxId(ticketInfo);

		} catch (Exception e){
			System.out.println("error occured while fetching details");

		}
		dto.setEtmStatewiseDtls(etmStateWiseList);
		dto.setConcessionDtls(concessionListDto);
		dto.setEtmTripDtls(etmTripListDto);
		dto.setEtmTripTotalDtls(etmTripDto);
		dto.setEarning(earningDto);
		dto.setBusStandBookingDtls(busStandDto);
		dto.setTripExpenses(tripDto);
		dto.setPassengerClassification(passengerDto);
		dto.setEtmIdNo(etm.getEtmId().getEtmNumber());
		status= HttpStatus.OK;
		}
		return new ResponseEntity<EtmTBAssignmentDto>(dto,status);
	}
	
	public ResponseEntity<EtmTBAssignmentDto> fetchPartialEtmDetails(String wayBillNo) {
		EtmTBAssignmentDto output = new EtmTBAssignmentDto();
		EtmTBAssignment etm = new EtmTBAssignment();
		IssueEtmTicketBoxEntity issueTicket  =new IssueEtmTicketBoxEntity();
		RouteMasterDto routeDto = new RouteMasterDto();  
		TicketBoxMasterDto ticketInfo= new TicketBoxMasterDto();
		try {
			etm = etmAsgnRepo.findByWaybillNo(wayBillNo);
			issueTicket = issueTicketRepo.findByWayBillNo(etm.getWayBillNo());
			} catch (Exception e){
				System.out.println("waybill not found!!");
			}
		try {
			routeDto.setId(etm.getRouteId().getId());
			routeDto.setScheduledKms(etm.getRouteId().getScheduledKms());
			routeDto.setDeadKms(etm.getRouteId().getDeadKms());
			
		} catch (Exception e) {
			log.info("exception occured while fetching route data");
		}
		try{
		output.setBusRegNo(issueTicket.getDailyRoaster().getBus().getBusRegNumber());
		output.setDriverName(issueTicket.getDailyRoaster().getBus().getPrimaryDriver().getDriverName());
		output.setDriverRegNo(issueTicket.getDailyRoaster().getBus().getPrimaryDriver().getDriverCode());
		output.setDepotId(etm.getConductorId().getDepot().getId().toString());
		output.setDepotName(etm.getConductorId().getDepot().getDepotName());
		output.setWayBillNo(etm.getWayBillNo());
		output.setConductorName(etm.getConductorId().getConductorName());
		output.setConductorRegNo(etm.getConductorId().getConductorCode());
		output.setBusType(issueTicket.getDailyRoaster().getBus().getBusType().getBusTypeName());
		ticketInfo.setTicketBoxNumber(issueTicket.getTicketBoxNumber().getTicketBoxNumber());
		output.setTicketBoxId(ticketInfo);
		output.setEtmIdNo(etm.getEtmId().getEtmNumber());
		//output.setIssueDate(etm.getCreatedOn().toString());
		output.setIssueDate(issueTicket.getCreatedOn().toString());
		output.setScheduledKms(etm.getRouteId().getScheduledKms());
		output.setDeadKms(etm.getRouteId().getDeadKms());
		
		} catch (Exception e){
			log.info("exception occured while fetching basic data");

		}
		return new ResponseEntity<EtmTBAssignmentDto>(output,HttpStatus.OK);
		
	}
}
