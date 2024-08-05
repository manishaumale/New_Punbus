package com.idms.base.api.v1.model.dto;

import java.util.Date;

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
@ApiModel(description = "Daily Roaster Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyRoasterDto extends BaseEntityDto{
	
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
	private BusMasterDto bus;
	
	private DriverMasterDto driver;
	
	private ConductorMasterDto conductor;
	
	private RouteMasterDto route;
	
	private TripMasterDto trip;
	
	private RouteTypeDto routeType;
	
	private TransportDto transportUnit;

}
