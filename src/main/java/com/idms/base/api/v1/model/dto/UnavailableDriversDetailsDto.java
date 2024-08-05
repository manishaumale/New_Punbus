package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class UnavailableDriversDetailsDto {
	
	private Integer id;
	
	@ApiModelProperty(notes = "Driver Name")
	private String driverName;
	
	@ApiModelProperty(notes = "Driver Type")
	private String driverType;
	
	@ApiModelProperty(notes = "Reason")
	private String reason;
	
	@ApiModelProperty(notes = "Remarks")
	private String remarks;
	
	@ApiModelProperty(notes = "Unavailability")
	private String unAvailableUpFrom;
	
	@ApiModelProperty(notes = "Unavailability")
	private String unAvailableUpTo;
	
	private String fromDate;
	
	private String toDate;
	
	private String driverConductorFlag;
	
	private String status;
	
	private String modifiedBy;
	
	private String modifiedOn;
}