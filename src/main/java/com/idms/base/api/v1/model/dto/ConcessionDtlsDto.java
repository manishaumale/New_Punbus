package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "ConcessionDtlsDto Model")
public class ConcessionDtlsDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;
	
	private String passType;
	
	private Integer discPercentage;
	
	private Integer tickets;
	
	private Double netAmount;

}
