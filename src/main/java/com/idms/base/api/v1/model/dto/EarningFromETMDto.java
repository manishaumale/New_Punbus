package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "EarningFromETMDto Model")
public class EarningFromETMDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;
	
	private Double earningPerKM;
	
	private Double earningPerKmFree;
	
	private Double loadFactor;
	
	private Double totalRemittance;
	
	private Double netAmtToDeposit;
	
	private String inspectDtls;
	
	private Integer totalTicketIssued;

}
