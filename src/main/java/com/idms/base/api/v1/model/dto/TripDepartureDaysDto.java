package com.idms.base.api.v1.model.dto;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class TripDepartureDaysDto {

	private Integer id;

	private TripMasterDto tripMaster;

	private RouteRotationDto routeRotation;

	private StopagesMasterDto stopageMaster;

	private String departureTime;

	private TotalNightsDto days;

}
