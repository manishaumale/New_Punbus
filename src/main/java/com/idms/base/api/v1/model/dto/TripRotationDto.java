package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Route Rotation Model")
public class TripRotationDto extends BaseEntityDto{
	
	@ApiModelProperty(notes = "Trip Rotation Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Route Master Model")
	@JsonIgnore
	private RouteMasterDto routeMaster;
	
	@ApiModelProperty(notes = "Trip Master Model")
	private TripMasterDto tripMaster;
	
	@ApiModelProperty(notes = "RouteRotation  Model")
	private RouteRotationDto routeRotation;
	
	@ApiModelProperty(notes = "PermitDetailsMaster Model")
	private PermitDetailsDto permitMaster;
	
	@ApiModelProperty("StartTime")
	private String startTime;
	
	@ApiModelProperty("EndTime")
	private String endTime;
	
	@ApiModelProperty("Total Night")
	private TotalNightsDto days;
	
	@ApiModelProperty("Night Halt")
	private Integer nightHalt;

}
