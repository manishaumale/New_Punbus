package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class KmplComparisonReportDto {
	
	private String routeId;

	private String routeCode;

	@ApiModelProperty(notes = "Total Tanks")
	private String routeName;

	@ApiModelProperty(notes = "Total Capacity")
	private String routeCategory;

	@ApiModelProperty(notes = "Bus Registration Number")
	private String busRegistrationNumber;

	@ApiModelProperty(notes = "Depot")
	private String scheduledKMS;
	@ApiModelProperty(notes = "Total Tanks")
	private String kmplScheduledKms;

	private String vtsKms;

	private String kmsAsPerVtsKms;

	@ApiModelProperty(notes = "Depot")
	private String routeNo;

	@ApiModelProperty(notes = "Total Capacity")
	private String GpsKm;

	@ApiModelProperty(notes = "Total Capacity")
	private String Gkmpl;

}
