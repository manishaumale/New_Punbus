package com.idms.base.api.v1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TyreIssueSlipDto {

	private String tyreTag;
	
	private String tyreNumber;
	
	private String tyreSize;
	
	private String tyreDepo;
	
	private String tyreManufacturer;
	
	private String tyreCondition;
	
	private String tyrePosition;
	
	private String expectedMileage;
	
	private Float ActualTyreMileage;
}

