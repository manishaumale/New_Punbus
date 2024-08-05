package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "EtmTripDtlsDto Model")
public class EtmTripDtlsDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;

	private String routeName;
	
	private String tripStartDateTime;
	
	private String tripEndDateTime;
	
	private Integer distance;
	
	private Double advBookingAmt;
	
	private Double totalCollection;
	
	private Double expenses;
	
	private Double netTripAmt;
	
	private Double empk;

}
