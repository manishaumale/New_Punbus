package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter @Setter @ToString @NoArgsConstructor
public class OtherPurposeFormLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Fuel Type List")
	private List<FuelTypeDto> fuelTypeList;
	
	@ApiModelProperty(notes = "Other Purpose  List")
	private List<OtherPurposeDto> otherpurposeList;
	
	@ApiModelProperty(notes = "Mobil Oil List")
	private List<MobilOilDrumMasterDto> mobilList;
	
	@ApiModelProperty(notes = "Add Blue List")
	private List<AddBlueDrumMasterDto> addBlueList;
	
	@ApiModelProperty(notes = "Fuel Tank List")
	private List<FuelTankMasterDto> fuelTankList;
	
	@ApiModelProperty(notes = "Dispensing List")
	private List<DispensingUnitMasterDto> dispensingList;
	
	
	
	
}
