package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class RouteFormLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "State Model List")
	private List<StateDto> fromStateList;
	
	@ApiModelProperty(notes = "Bus Type Model List")
	private List<BusTypeDto> busTyperMasterList;
	
	@ApiModelProperty(notes = "Depot  Model List")
	private DepotMasterDto depotMasterList;
	
	@ApiModelProperty(notes = "Total Nights List")
	private List<TotalNightsDto> totalNightsList;
	
	@ApiModelProperty(notes = "Route Category List")
	private List<RouteCategoryDto> routeCategoryList;
	
	@ApiModelProperty(notes = "Route Complexity List")
	private List<RouteComplexityDto> routeComplexityList;
	
	@ApiModelProperty(notes = "Route Type List")
	private List<RouteTypeDto> routeTypeList;
	
	@ApiModelProperty(notes = "Permit Detail List")
	private List<PermitDetailsDto> permitDetailList;
	
	@ApiModelProperty(notes = "Transport List")
	private List<TransportDto> transportList;
	
	@ApiModelProperty(notes = "Stopage Type List")
	private List<StopageTypeDto> stopageTypeList;
	
	@ApiModelProperty(notes = "Bus Stand List")
	private List<BusStandDto> busStandList;
	
	@ApiModelProperty(notes = "Stopage List")
	private List<StopageDto> stopageList;
	
	@ApiModelProperty(notes = "Trip Type List")
	private List<TripTypeDto> tripTypeList;
	

}
