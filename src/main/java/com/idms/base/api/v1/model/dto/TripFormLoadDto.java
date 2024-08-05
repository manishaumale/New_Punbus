package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class TripFormLoadDto {
	
	@ApiModelProperty(notes = "City Model List")
	private List<CityDto> cityList;
	
	@ApiModelProperty(notes = "Total Nights List")
	private List<TotalNightsDto> totalNightsList;
	
	@ApiModelProperty(notes = "Stopage Type List")
	private List<StopageTypeDto> stopageTypeList;
	
	@ApiModelProperty(notes = "Bus Stand List")
	private List<BusStandDto> busStandList;
	
	@ApiModelProperty(notes = "Route Type List")
	private List<RouteMasterDto> routeList;
	
	@ApiModelProperty(notes = "Stopage List")
	private List<StopageDto> stopageList;
	
}
