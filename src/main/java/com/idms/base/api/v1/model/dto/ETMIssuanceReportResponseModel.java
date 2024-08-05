package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class ETMIssuanceReportResponseModel {

	private String conductorNo;
	
	private String conductorName;

	private String machineNumber;
	
	private String depotName;
	private String depotName1;

	private String etmIssueTime;

	private String routeStartTime;

	private String howEarlyIssued;

	private String lastIssuedDate;

	private String reasons;
	
	private String noOfDays;
	
	private String fromDate;
	private String toDate;
	private String etmCount;
	private String routeCount;
	private String transUnit;
	private String transUnit1;
	

}
