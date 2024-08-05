package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.TripType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Route Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteMasterDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Route Id")
	private String routeId;

	@ApiModelProperty(notes = "Route Name")
	private String routeName;
	
	@ApiModelProperty(notes = "Route Code")
	private String routeCode;
	
	@ApiModelProperty(notes = "Depot Name")
	private String depotName;
	
	@ApiModelProperty(notes = "Total Ot")
	private Integer totalOt;
	
	@ApiModelProperty(notes = "Scheduled Kms")
	private Integer scheduledKms;
	
	@ApiModelProperty(notes = "Dead Kms")
	private Integer deadKms;

	@ApiModelProperty(notes = "State Model")
	private StateDto fromState;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto fromCity;
	
	@ApiModelProperty(notes = "State Model")
	private StateDto toState;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto toCity;
	
//	@ApiModelProperty(notes = "Total Nights Model")
//	private TotalNightsDto totalNightsMaster;
	
	@ApiModelProperty(notes = "Route Type Model")
	private RouteTypeDto routeTypeMaster;
	
	@ApiModelProperty(notes = "Route Complexity Model")
	private RouteComplexityDto routeComplexityMaster;
	
	@ApiModelProperty(notes = "Route Complexity Model")
	private RouteCategoryDto routeCategoryMaster;
	
	@ApiModelProperty(notes = "Bus Type Model")
	private BusTypeDto busTyperMaster;
	
	@ApiModelProperty(notes = "Depot  Model")
	private DepotMasterDto depotMaster;
	
	
	@ApiModelProperty(notes = "Total Kms")
	private Integer totalKms;
	
	@ApiModelProperty(notes = "Plain Kms")
	private Integer plainKms;
	
	@ApiModelProperty(notes = "Hill Kms")
	private Integer hillKms;
	
	@ApiModelProperty(notes = "Duty Counts")
	private Integer dutyCounts;
	
	@ApiModelProperty(notes = "State Wise Route Km List")
	private List<StateWiseRouteKmsDto> stateWiseRouteKms;
	
	@ApiModelProperty(notes = "Route Permit List")
	private List<RoutePermitMasterDto> routePermitMasterList;
	
	private TransportDto transport;
	
	private Boolean status;
	
	@ApiModelProperty(notes = "Route Rotation List")
	private List<RouteRotationDto> routeRotation;
	
	@ApiModelProperty(notes = "Trip Rotation List")
	private List<TripRotationDto> tripRotation;
	
	@ApiModelProperty(notes = "Trip Master Dto")
	private List<TripMasterDto> tripMaster;
	
	private String routeCategory;
	
	private String  routeflag;
	
	private TripTypeDto tripType;
	
	public RouteMasterDto(Integer id, String routeName) {
		super();
		this.id = id;
		this.routeName = routeName;
	}
	
	
	
}
