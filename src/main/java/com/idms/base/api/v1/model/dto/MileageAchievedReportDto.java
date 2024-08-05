package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Mileage Achieved Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MileageAchievedReportDto {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Expected Life")
	private String  expectedLife;
	
	@ApiModelProperty(notes = "Mileage At Taken Off")
	private String  mileageAtTakenOff;
	
	@ApiModelProperty(notes = "Taken Off Date")
	private String  takenOffDate;
	

}