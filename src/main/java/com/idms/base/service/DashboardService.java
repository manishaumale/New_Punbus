package com.idms.base.service;


import java.util.List;

import com.idms.base.api.v1.model.dto.DashboardDto;

public interface DashboardService {

	DashboardDto getSADashboard(String tpGroups);

	DashboardDto getDADashboard(String dpCode);
	
	DashboardDto getTotalRouteDetails(String dpCode);
	
	DashboardDto getTotalBusDetails(String dpCode, String type);

	List<DashboardDto> getAllConductorDetails(String depoCode, String type);

	List<DashboardDto> getAllDriverDetails(String depoCode, String type);
		
}
