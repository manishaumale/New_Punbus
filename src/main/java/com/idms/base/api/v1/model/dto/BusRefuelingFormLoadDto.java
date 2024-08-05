package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.idms.base.dao.entity.BusReturnReason;
import com.idms.base.dao.entity.MobilOilDrumMaster;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BusRefuelingFormLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Dispensing Unit List")
	private List<DispensingUnitMasterDto> dispensingList;
	
	@ApiModelProperty(notes = "Fuel Tank List")
	private List<FuelTankMasterDto> fuelTankList;
	
	@ApiModelProperty(notes = "Bus Master List")
	private List<BusMasterDto> busMasterList;
	
	@ApiModelProperty(notes = "Bus Master List")
	private List<FuelSourceMasterDto> fuelSourceList;
	
	private List<BusReturnReason> busReturnReasonList;
	
	private List<MobilOilDrumMasterDto> drumList;
	
	private List<AddBlueDrumMasterDto> addBlueDrumList; 
	
}
