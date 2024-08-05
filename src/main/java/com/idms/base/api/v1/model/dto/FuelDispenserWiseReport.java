package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FuelDispenserWiseReport {

	@ApiModelProperty(notes = "Depot")
	private String depotName;

	private String disUnitCode;

	@ApiModelProperty(notes = "Total Capacity")
	private String startingReading;

	@ApiModelProperty(notes = "Total Tanks")
	private String lasReading;

	private String total;

	@ApiModelProperty(notes = "Total Capacity")
	private String totalDieselIssuance;

	//@ApiModelProperty(notes = "Total Tanks")
	private String fuelDispenserID;
		
	private String busRegNumber;
		
	private String fuelIssuanceDate;
	
	private String transUnit;
	

}
