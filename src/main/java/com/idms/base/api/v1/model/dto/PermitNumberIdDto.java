package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Permit Name and Id Model")
public class PermitNumberIdDto {
	
	private Integer permitId;
	
	private String permitNumber;
	
	

}
