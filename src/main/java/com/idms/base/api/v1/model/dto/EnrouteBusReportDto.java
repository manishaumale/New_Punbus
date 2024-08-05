package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class EnrouteBusReportDto {
	
	private String busNumber;
	private String busUpTime;
	private String busDownTime;
	private String busType;
	private String routeName;
	private String driverCode;
	private String driverName;
	private String conductorCode;
	private String conductorName;
	private String gpsLink;
	private String transportName;

}
