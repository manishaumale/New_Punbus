package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class UnavailableBusesDetailsDto {
	
	@ApiModelProperty(notes = " Bus Unavailable ID")
	private Integer id;
	
	@ApiModelProperty(notes = "Bus Number")
	private String busNo;
	
	@ApiModelProperty(notes = "Bus Type Name")
	private String busTypeName;
	
	@ApiModelProperty(notes = "Reason")
	private String reason;
	
	@ApiModelProperty(notes = "Remarks")
	private String remarks;
	
	private String likelyToBeReadyDate;
	
	private String detentionDate; 
	
	
	
	
}
