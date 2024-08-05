package com.idms.base.api.v1.model.dto;

import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Route Online Booking Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteOnlineBookingDetailsDto {
	
	@ApiModelProperty(notes = "Roaster Id")
	private Integer id;
	
	@ApiModelProperty(notes = "Route Id")
	private String routeId;

	@ApiModelProperty(notes = "Route Name")
	private String routeName;
	
	@ApiModelProperty(notes = "Route Code")
	private String routeCode;
	
	@ApiModelProperty(notes = "Scheduled Kms")
	private Integer scheduledKms;
	
	@ApiModelProperty(notes = "Bus Registered No")
	private String  busRegNumber;
	
	@ApiModelProperty(notes = "Bus Type Model")
	private BusTypeDto busType;
	
	/*@ApiModelProperty(notes = "Bus Sub Type Model")
	private BusSubTypeDto busSubType;
	*/
	@ApiModelProperty(notes = "Driver Id")
    private Integer driverId;
	
	@ApiModelProperty(notes = "Drive Code")
    private String driverCode;

	@ApiModelProperty(notes = "Driver Name")
    private String driverName;
	
	@ApiModelProperty(name = "Conductor Id")
	private Integer conductorId;
	
	@ApiModelProperty(notes = "Condutor Name")
    private String conductorName;

	@ApiModelProperty(notes = "Online Booking Details List")
	private List<OnlineBookingDetailsDto> OnlineBookingDetails;
	
	private String status;
	
}
