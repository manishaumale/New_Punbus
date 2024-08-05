package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "EtmTripTotalDtlsDto Model")
public class EtmTripTotalDtlsDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;
	
	private Integer records;
	
	private Integer totalDistance;
	
	private Double totalAdvBookingAmt;
	
	private Double totalCollection;
	
	private Double totalExpenses;
	
	private Double totalTripAmount;

}
