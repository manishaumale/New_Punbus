package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TicketBoxMachIssuReportResModel {

	private String depotName;
	private String depotName1;

	private String ticketBoxNumber;

	private String etmNumber;

	private String conductorName;

	private String conductorNo;
	
	private String date;
	
	private String fromDate;
	private String toDate;
	private String etmCount;
	private String routeCount;
	private String transUnit;
	private String transUnit1;
	private String ticketBoxCount;
	

}
