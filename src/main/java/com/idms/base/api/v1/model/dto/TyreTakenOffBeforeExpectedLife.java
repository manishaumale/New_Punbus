package com.idms.base.api.v1.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Taken Off Before Expected Life")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreTakenOffBeforeExpectedLife {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;	
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;	
	
	@ApiModelProperty(notes = "Expected Mileage")
	private String  expectedMileage;
	
	@ApiModelProperty(notes = "Mileage At Taken Off")
	private String  mileageAtTakenOff;
	
	@ApiModelProperty(notes = "Difference")
	private String  difference;
	

}

