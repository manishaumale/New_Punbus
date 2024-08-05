package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class TotalTankAndCapacityDto {
	
	
	@ApiModelProperty(notes = "Depot")
	private String depot;
	
	@ApiModelProperty(notes = "Total Tanks")
	private String totalTanks;
	
	@ApiModelProperty(notes = "Total Capacity")
	private String totalCapacity;
	
	private Float Sum;
	
	@ApiModelProperty(notes = "Tank Capacity")
	private String tankCapacity;

}
