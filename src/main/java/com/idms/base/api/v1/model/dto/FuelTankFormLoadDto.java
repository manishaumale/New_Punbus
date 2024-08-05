package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FuelTankFormLoadDto {
	
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Tank Master List")
	private List<TankCapacityMasterDto> tankList;
	
	@ApiModelProperty(notes = "Dip Reading List")
	private List<DipReadingMasterDto> dipReadingList;

}
