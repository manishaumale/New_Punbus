package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class PermitDetailsFormDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "State Model List")
	private List<StateDto> fromStateList;
	
	@ApiModelProperty(notes = "Bus Type Model List")
	private List<BusTypeDto> busTyperMasterList;
	
	@ApiModelProperty(notes = "Depot  Model List")
	private DepotMasterDto depotMasterList;
	
	@ApiModelProperty(notes = "Transport  Unit List")
	private List<TransportDto> TransportUnitMasterList;

}
