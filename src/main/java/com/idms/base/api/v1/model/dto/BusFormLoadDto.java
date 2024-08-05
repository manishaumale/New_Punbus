package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Data
@Getter @Setter @ToString @NoArgsConstructor
public class BusFormLoadDto {
	
	@ApiModelProperty(notes = "Bus Maker List")
	private List<BusMakerDto> busMakerList;
	
	@ApiModelProperty(notes = "Transport Unit List")
	private List<TransportDto> transportList;
	
	@ApiModelProperty(notes = "Bus Type List")
	private List<BusTypeDto> busTypeList;
	
	@ApiModelProperty(notes = "Bus Sub Type List")
	private List<BusSubTypeDto> busSubTypeList;
	
	@ApiModelProperty(notes = "Depot List")
	private DepotMasterDto depotList;
	
	@ApiModelProperty(notes = "Driver List")
	private List<DriverMasterDto> driverList;
	
	@ApiModelProperty(notes = "Euro List")
	private List<EuroNormsDto> euroList;
	
	@ApiModelProperty(notes = "Layout List")
	private List<BusLayoutDto> layoutList;
	
	@ApiModelProperty(notes = "Parts List")
	private List<PartsMasterDto> partsList;
	
	@ApiModelProperty(notes = "Tyre Type List")
	private List<TyreTypeDto> tyreTypeList;
	
	@ApiModelProperty(notes = "Tyre Size List")
	private List<TyreSizeDto> tyreSizeList;
	
	@ApiModelProperty(notes = "Tyre Maker List")
	private List<TyreMakerDto> tyreMakerList;
	
	@ApiModelProperty(notes = "Tyre Position List")
	private List<TyrePositionDto> tyrePositionList;
	
	private List<BusTyreCountDto> busTyreCount;
	private Boolean spareBus;
	
}
