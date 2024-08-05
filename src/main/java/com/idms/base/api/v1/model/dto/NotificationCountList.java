package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class NotificationCountList {

	
	NotificationCount fuelCounts;
	
	NotificationCount tyreCounts;
	
	NotificationCount routeCrewCounts;
	NotificationCount ticketCounts;
	NotificationCount driverCounts;
	NotificationCount conductorCounts;
	NotificationCount busCounts;
}
