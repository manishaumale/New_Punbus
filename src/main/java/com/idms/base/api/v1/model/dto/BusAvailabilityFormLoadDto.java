package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class BusAvailabilityFormLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Bus Master List")
	private List<BusMasterDto> busList;
	
	@ApiModelProperty(notes = "Reason Master List")
	private List<BusUnavailablityReasonMasterDto> reasonList;
	
	
	
	
}
