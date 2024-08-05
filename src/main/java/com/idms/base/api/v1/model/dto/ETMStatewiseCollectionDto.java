package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "ETMStatewiseCollectionDto Model")
public class ETMStatewiseCollectionDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;
	
	private String stateName;
	
	private Double amount;

}
