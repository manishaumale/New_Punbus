package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Online Booking Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineBookingDetailsDto {
	
	private String bookingId="";
	
	private String tripCode="";
	
	private String fromCityCode="";
	
	private String toCityCode="";
	
	private Date travelDate;
	
	private Double amount=0.00;
	
	private Integer seatsCount=0;
	
	private String seatNumbers="";
	
	private Date cancelationDate;

}
