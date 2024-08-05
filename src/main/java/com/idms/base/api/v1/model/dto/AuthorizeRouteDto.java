package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.DepotMaster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(description = "All Authorize Route List View Dto")
public class AuthorizeRouteDto {

	private List<RouteMasterDto> routeList;
	private List<BusMasterDto> busList;
	private List<DriverMasterDto> driverList;
	private List<ConductorMasterDto> conductorList;
	private List<TripMasterDto> tripList;
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

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
	
	private String routeName;
	
	private String busRegNumber;
	
	private String driverCode;
	
	private String driverName;
	
	private String conductorCode;
	
	private String conductorName;
	
	private String depotName;
	
	private Integer authorizeRouteId;
	
	private Boolean isDeleted;
	
	@ApiModelProperty(notes = "Trip Master")
	private TripMasterDto trip;
	
	private String serviceId;
	
	private String tripCode;
	
	private String  reason;
}
