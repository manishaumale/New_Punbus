package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class AllDriverOrConductorsByTPTypeDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Employment Type")
	private String employmentType;
	
	@ApiModelProperty(notes = "Employment Id")
	private String employmentId;
	
	@ApiModelProperty(notes = "Total Divers/Conductors")
	private String totalDriversOrConductors;
	
	@ApiModelProperty(notes = "Available Drivers/Conductors")
	private String availableDriversOrConductors;
	
	@ApiModelProperty(notes = "Spare Drivers/Conductors")
	private String spareDriversOrConductors;
	
	@ApiModelProperty(notes = "Unavailable Drivers/Conductors")
	private String unavailableDriversOrConductors;
	
	@ApiModelProperty(notes = "Transport Name")
	private String transportName;
	
	@ApiModelProperty(notes = "Transport Id")
	private String transportId;
	
}