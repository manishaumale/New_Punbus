package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Resole Tyre Mileage Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResoleTyreMileageReport {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "KMs Achieved")
	private String  kmsAchieved;
	
	@ApiModelProperty(notes = "Resole")
	private String  resole;
	
	@ApiModelProperty(notes = "Expected Mileage")
	private String  expectedMileage;
	
	
	@ApiModelProperty(notes = "Expected Mileage Date")
	private String  expectedMileageDate;
	
	@ApiModelProperty(notes = "GPS Mileage")
	private String  gpsMileage;

	
	

}

