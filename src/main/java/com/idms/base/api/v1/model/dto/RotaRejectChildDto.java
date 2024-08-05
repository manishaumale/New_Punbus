package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Rota Reject Child Dto")
public class RotaRejectChildDto {
	
	private Integer dailyRoasterId;
	
	private String remarks;
	
}
