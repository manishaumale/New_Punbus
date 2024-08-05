package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter @Setter @ToString @NoArgsConstructor
public class OtherDutyOnLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Driver List")
	private List<DriverMasterDto> driverList;
	
	@ApiModelProperty(notes = "Conductors List")
	private List<ConductorMasterDto> conductorsList;
	
	@ApiModelProperty(notes = "Duty Type List")
	private List<DutyTypeDto> dutyTypeList;

}

