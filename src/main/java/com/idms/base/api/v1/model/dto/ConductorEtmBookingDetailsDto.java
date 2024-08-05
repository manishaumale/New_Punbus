package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "ConductorEtmBookingDetailsDto Model")
public class ConductorEtmBookingDetailsDto {
	
	private Integer id;
	
	private Double passengerAmt;
	
	private Double luggageAmt;
	
	private Double concessionAmt;
	
	private Double tollAmt;
	
	private Double boxAmt;
	
	private Double miscAmt;
	
	private Double totalAmt;
	
	private EtmTBAssignmentDto assignment;

}
