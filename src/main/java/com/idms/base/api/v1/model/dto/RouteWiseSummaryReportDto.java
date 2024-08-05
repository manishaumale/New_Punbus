package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class RouteWiseSummaryReportDto {
	
    private String routeName;
   
    private String date;
   
	private String schKms;
	
	private String trips;
	
	private String allocatedKms;
	
	private String gpsKms;
	

}
