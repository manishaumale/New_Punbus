package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Route Analysis Report Dto")
public class DateWiseProductiveReportDto {
	
	private String driverOrConductorCode;
	
	private String date;
	
	private String schKms;
	
	private String driverOrConductorName;
	
	private String routeName;
	
	private String epkm;

}
