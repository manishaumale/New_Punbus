package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "PassengerClassificationDto Model")
public class PassengerClassificationDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;
	
	private Integer passengerCounterBooking;
	
	private Integer advBooking;
	
	private Integer luggageTicket;
	
	private Integer policWarrent;
	
	private Integer concessionTicket;
	
	private Integer trafficInspection;
	
	private Double ticketAmount;
	
	private Double kmpl;
	
	private Integer totalAdults;
	
	private Integer totalChilds;

}
