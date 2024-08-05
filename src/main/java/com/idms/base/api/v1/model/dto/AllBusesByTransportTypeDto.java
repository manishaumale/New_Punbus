package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class AllBusesByTransportTypeDto {
	
	@ApiModelProperty(notes = "Bus Type Name")
	private String busTypeName;
	
	@ApiModelProperty(notes = "Bus Type Id")
	private String busTypeId;
	
	@ApiModelProperty(notes = "Total Buses")
	private String totalBuses;
	
	@ApiModelProperty(notes = "Available Buses")
	private String availableBuses;
	
	@ApiModelProperty(notes = "Spare Buses")
	private String spareBuses;
	
	@ApiModelProperty(notes = "Unavailable Buses")
	private String unavailableBuses;
	
	@ApiModelProperty(notes = "Transport Name")
	private String transportName;
	
	@ApiModelProperty(notes = "Transport Id")
	private String transportId;
	
}
