package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class AuthorizedRouteReportDto {
	
	private String driverOrConductorName;
	
	private String driverOrConductorCode;
	
	private String routeName;
	
	private String fromDate;
	
	private String upToDate;
	
	private String reason;

}
