package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Refueling Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusRefuelingMasterDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Dispense Unit Master Model")
	private DispensingUnitMasterDto dispensingUnitMaster;
	
	@ApiModelProperty(notes = "Fuel Tank Master Model")
	private FuelTankMasterDto fuelTankMaster;
	
	@ApiModelProperty(notes = "Bus Master Model")
	private BusMasterDto busMaster;
	
	@ApiModelProperty(notes = "issuedDiesel")
	private Float issuedDiesel;
	
	@ApiModelProperty(notes = "Route Master Model")
	private RouteMasterDto routeMaster;
	
    private Float extraDeadKms;
	
	private String reasonForExtraDead;
	
	private String remarks;
	
    private FuelTakenOutSideDto fuelTakenOutSide;
	
	private AdBlueUsedDto adBlueUsed;
	
	private MobilOilUsedDto mobilOilUsed;
	
	private Float dieselFromOutside;
	
//	private BusReturnReason reason;
	
	private DepotMasterDto depot;
	
	private Float kmplAsperActualKms;
	
	public BusRefuelingMasterDto(Float kmplAsperActualKms)
	{
		this.kmplAsperActualKms=kmplAsperActualKms;
	}
	
}
