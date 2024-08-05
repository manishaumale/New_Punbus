package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "DashboardDto Model")
public class DashboardDto {
	
	private Integer totalRoutes;
	
	private Integer activeRoutes;
	
	private Integer totalBuses;
	
	private Integer activeBuses;
	
	private Integer totalDrivers;
	
	private Integer activeDrivers;
	
	private Integer totalConductor;
	
	private Integer activeConductor;
	
	private Integer minDriversAndConRequired;
	
	private List<RouteDetailsDto> routeList;
	
	private List<BusDetailsDto> busList;
	
	private List<DriverMasterDto> driverList;
	
	private List<ConductorMasterDto> conductorList;

}
