package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "Route Analysis Report Dto")
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteAnalysisReportDto {

	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Route No")
	private String routeCode;

	@ApiModelProperty(notes = "Name")
	private String routeName;

	@ApiModelProperty(notes = "Starting Time")
	private String routestartingTime;

	@ApiModelProperty(notes = "Kms")
	private String scheduledkms;

	@ApiModelProperty(notes = "Profit Per Kms")
	private String profitPerKms;

	@ApiModelProperty(notes = "Trip")
	private String tripStartTime;

	@ApiModelProperty(notes = "Trip")
	private String tripEnddate;

	private String count;

	@ApiModelProperty(notes = "From Kms")
	private String fromKms;

	@ApiModelProperty(notes = "To Kms")
	private String toKms;

	@ApiModelProperty(notes = "Last Operated")
	private String lastOperated;

	@ApiModelProperty(notes = "No Of Trips")
	private String noOfTrips;

}
