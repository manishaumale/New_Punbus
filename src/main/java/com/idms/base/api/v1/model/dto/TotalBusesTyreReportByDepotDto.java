package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/*@Getter
@Setter
@ToString*/
@Data
@NoArgsConstructor
@ApiModel(description = "Total Buses And Tyres Report Model")
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalBusesTyreReportByDepotDto {

	@ApiModelProperty(notes = "Depot Name")
	private String depotName;

	@ApiModelProperty(notes = "Total Buses")
	private String totalBuses;

	@ApiModelProperty(notes = "Total New Tyres")
	private String totalNewTyres;

	private String newTyrePosition;

	// private Integer count;

	// private Integer newTyreCount;

	@ApiModelProperty(notes = "First Resole")
	private String firstResole;

	@ApiModelProperty(notes = "Second Resole")
	private String secondResole;

	@ApiModelProperty(notes = "Third Resole")
	private String thirdResole;

}
