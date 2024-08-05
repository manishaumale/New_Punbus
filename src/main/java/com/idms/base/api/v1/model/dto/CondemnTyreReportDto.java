package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "Condemn Tyre Report")
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class CondemnTyreReportDto {

	@ApiModelProperty(notes = "Depot Name")
	private String depotName;

	@ApiModelProperty(notes = "Tyre No")
	private String tyreNumber;

	@ApiModelProperty(notes = "Make")
	private String name;

	@ApiModelProperty(notes = "Expected Mileage")
	private String expectedMileage;

	@ApiModelProperty(notes = "Mileage At Taken Off")
	private String mileageAtTakenOff;
	
	private String takenOffDate;

	@ApiModelProperty(notes = "Make")
	private String make;

	@ApiModelProperty(notes = "Hill Mileage")
	private String hillKms;

	@ApiModelProperty(notes = "Plain Mileage")
	private String plainKms;

}
