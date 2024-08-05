package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Daily Roaster Auto Model")
public class DailyRoasterAutoDto {
	
	private String busModel;
	
	private String primaryDriver;
	
	private String secondryDriver;
	
	private String driverCode;
	
	private String driverName;
	
	private String busNumber;
	
	private Integer scheduledKms;
	
	private String driverLiscenseValidity;
	
	private String driverCategory;
	
	private String conductorName;
	
	private String conductorCode;
	
	private String conductorCategory;
	
	private String routeId;
	
	private String routeName;
	
	private String routeCategory;
	
	private String tripServiceId;
	
	private String tripStartTime; 
	
	private String tripEndTime; 
	
	private String suggestion;
	
	private String availabilityDate;
	
	private String fromState;
	
	private String ToState;
	
	private String fromCity;
	
	private String toCity;
	
	private Integer totalScheduledKms;
	
	private String driverContactNo;
	
	private String conductorContactNo;
	
	private String serviceId;
	
	private String days;
	
	private Integer rotaId;
	
	private Integer tripId;
	
	private Integer counter;
	
	private Integer specialCounter;
	
	private Integer manualCounter;
	
	private String remarks;
	
	private Integer routePk;
	
	private Integer transportId;
	
	private String spareName;
	
	private String sparetype;
	
	private String overrideReason;
	
	
}
