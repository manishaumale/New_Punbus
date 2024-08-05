package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Issue Etm Ticket Model")
public class IssueEtmTicketBoxDto {

	private Integer id;

	private Integer conductorMaster;
	
	private Integer ticketBoxNumber;
	
	private Integer etmMaster;
	
	private String wayBillNo;
	
	private Integer dailyRoaster;
	
	private Integer onlineBookingDetails;
	
	private Integer ticketBoxManagementParentEntity;
	
	private ConductorMasterDto conductorMasterDto;
	
	private TicketBoxMasterDto ticketBoxMasterDto;
	
	private ETMMasterDto etmMasterDto;
	
	private DailyRoasterDto dailyRoasterDto;
	
	private OnlineBookingDetailsDto onlineBookingDetailsDto;
	
	private TicketManagementBoxParentDto ticketManagementBoxParentDto;
	
	private RouteOnlineBookingDetailsDto routeOnlineBookingDetailsDto;
	
	private String dpCode;
	
	private List<IssueEtmTicketBoxEntityChildDto> childTicketBoxEntityList;
	
	private String amountSum;
	
	private String totalAmount;
	
	private Integer totalDist;
	
	private Boolean manualEntry;
	
	
	
	
}
