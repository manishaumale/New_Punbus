package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import com.idms.base.dao.entity.EtmTripDtls;
import com.idms.base.dao.entity.EtmTripTotalDtls;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "EtmTBAssignmentDto Model")
public class EtmTBAssignmentDto {
	
	private Integer id;
	
	private ETMMasterDto etmId;
	
	private String wayBillNo;
	
	private TicketBoxMasterDto ticketBoxId;
	
	private ConductorMasterDto conductorId;
	
	private RouteMasterDto routeId;
	
	private Date etmAssignmentDate;
	
	private Date etmSubmitDate;
	
	private Boolean etmSubmitStatus;
	
	private Boolean tbSubmitStatus;
	
	private List<EtmTBTripsAssociationDto> associationList;
	
	private ConductorEtmBookingDetailsDto conductorBookingDetails;
	
	private BusStandBookingDtlsDto busStandBookingDtls;
	
	private TripExpensesDto tripExpenses;
	
	private PassengerClassificationDto passengerClassification;
	
	private List<ConcessionDtlsDto> concessionDtls;
	
	private EarningFromETMDto earning;
	
	private List<ETMStatewiseCollectionDto> etmStatewiseDtls;
	
	private List<EtmTripDtlsDto> etmTripDtls;
	
	private EtmTripTotalDtlsDto etmTripTotalDtls;
	
	private String ConductorName;
	
	private String ConductorRegNo;
	
	private String depotId;
	
	private String etmIdNo;
	
	private String busRegNo;
	
	private String driverName;
	
	private String driverRegNo;
	
	private String busType;
	
	private String issueDate;
	
	private List<IssueEtmTicketBoxEntityChildDto> totalTicketIssued;
	
	private String depotName;

	private Integer scheduledKms;
	
	private Integer deadKms;
}
