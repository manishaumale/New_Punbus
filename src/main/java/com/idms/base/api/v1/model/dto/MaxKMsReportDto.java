package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class MaxKMsReportDto {
	
	private String date;
	
	private String routeName;
	
	private String routeCode;
	
	private String schKMs;
	
	private String totalOverTime;
	
	private String overNight;

}
