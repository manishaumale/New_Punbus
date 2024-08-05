package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class BusesNotGettingDieselIssuedOnTimeReportDto {
	
	private String depotName;

	private String busNo;
	
	private String scheduledDateAndTime;
	
	private String refueledDateAndTime;
	
	private String duration;
	
}
