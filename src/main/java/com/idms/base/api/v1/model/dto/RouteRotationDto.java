package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter @ToString @NoArgsConstructor
@ApiModel(description = "Route Rotation Model")
public class RouteRotationDto extends BaseEntityDto{

	/**
	 * @author Piyush
	 * 
	 */
	@ApiModelProperty(notes = "Route Rotation Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "State Model")
	private StateDto fromState;

	@ApiModelProperty(notes = "City Model")
	private CityDto fromCity;
	
	@ApiModelProperty(notes = "State Model")
	private StateDto toState;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto toCity;
	
	@ApiModelProperty(notes = "Trip Day")
	private TotalNightsDto tripDay;
	
	@ApiModelProperty(notes = "Trip Start Time")
	private String tripStartTime;
	
	@ApiModelProperty(notes = "Trip End Time")
	private String tripEndTime;
	
	@ApiModelProperty(notes = "Night Halt")
	private Integer nightHalt;
	
	@ApiModelProperty(notes = "Trip Schedule KM")
	private Integer tripScheduleKm;
	
	@ApiModelProperty(notes = "Permit Model")
	private PermitDetailsDto permit;
	
	@ApiModelProperty(notes = "Stopage Model")
	private List<StopagesMasterDto> stopagesMasterList;
	
	@ApiModelProperty(notes = "TollTax Model")
	private List<TollTaxWrapperDto> tollTaxWrapperList;
	
	@ApiModelProperty(notes = "BusStand Model")
	private List<BusStandWrapperDto> busStandWrapperList;
	
	@ApiModelProperty(notes = "Total Nights Model")
	private TotalNightsDto days;
	
	@ApiModelProperty(notes = "Route Master Model")
	@JsonIgnore
	private RouteMasterDto routeMaster;
	
	private List<TripDepartureDaysDto> triDepDays;
}
