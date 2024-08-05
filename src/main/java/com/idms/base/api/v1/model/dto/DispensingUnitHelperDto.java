package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Dispensing Unit helper Model")
public class DispensingUnitHelperDto {

	
	private String dispensingUnitName;
	
	private String duOpeningReading;
	
	private String duCurrentReading;
}
