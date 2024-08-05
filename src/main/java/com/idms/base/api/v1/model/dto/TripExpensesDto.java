package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "TripExpensesDto Model")
public class TripExpensesDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;
	
	private Double tollTax;
	
	private Double ticketRefund;
	
	private Double busStandFee;
	
	private Double diesel;
	
	private Double repairBill;
	
	private Double miscFee;
	
	private Double total;

}
