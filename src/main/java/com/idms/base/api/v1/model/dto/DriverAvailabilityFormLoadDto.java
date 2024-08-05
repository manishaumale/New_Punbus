package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class DriverAvailabilityFormLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Driver Master List")
	private List<DriverMasterDto> driverList;
	
	@ApiModelProperty(notes = "Reason Master List")
	private List<DriverUnavailablityReasonMasterDto> reasonList;
	
	
	
	
}
