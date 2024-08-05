package com.idms.base.api.v1.model.dto;

import com.idms.base.dao.entity.DepotMaster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "All Authorize Route History List View Dto")
public class AuthorizeRouteHistoryDto {

	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Authorize Route")
	private AuthorizeRouteDto authorizeRoute;

	@ApiModelProperty(notes = "Route Master")
	private RouteMasterDto route;

	@ApiModelProperty(notes = "Depot Master")
	private DepotMaster depot;

	@ApiModelProperty(notes = "Bus Master")
	private BusMasterDto bus;

	@ApiModelProperty(notes = "Driver Master")
	private DriverMasterDto driver;

	@ApiModelProperty(notes = "Conductor Master")
	private ConductorMasterDto conductor;

	@ApiModelProperty(notes = "transport Unit Master")
	private TransportDto transportUnit;
	
	@ApiModelProperty(notes = "Trip Master")
	private TripMasterDto trip;
	
	private String  reason;
	}
