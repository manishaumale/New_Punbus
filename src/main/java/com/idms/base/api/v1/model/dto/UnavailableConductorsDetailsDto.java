package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class UnavailableConductorsDetailsDto {
	
	private Integer id;
	
	@ApiModelProperty(notes = "Conductor Name")
	private String conductorName;
	
	@ApiModelProperty(notes = "Conductor Type")
	private String conductorType;
	
	@ApiModelProperty(notes = "Reason")
	private String reason;
	
	@ApiModelProperty(notes = "Remarks")
	private String remarks;
	
	@ApiModelProperty(notes = "Unavailability")
	private String unAvailableUpTo;
	
	@ApiModelProperty(notes = "Unavailability")
	private String unAvailableFrom;
	
	private String driverConductorFlag;
	
}
