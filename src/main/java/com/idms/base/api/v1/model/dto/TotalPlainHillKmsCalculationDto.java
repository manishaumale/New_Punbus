package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Total Plain HillKms")
public class TotalPlainHillKmsCalculationDto {
	
    private String plainKms;
	
	private String hillKms;
	
	private String totKms;

}
