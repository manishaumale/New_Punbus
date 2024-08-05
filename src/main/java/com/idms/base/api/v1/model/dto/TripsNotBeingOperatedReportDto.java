package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TripsNotBeingOperatedReportDto {
	private String routeId;
	
	private String routeName;
	
	private String tripId;
	
	private String depoName;
	
	private String schKMs;
	
	private String lastOperatedDate;
}
