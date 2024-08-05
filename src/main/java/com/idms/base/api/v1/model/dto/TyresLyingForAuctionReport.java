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
@ApiModel(description = "Tyres Lying For Auction Report")
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class TyresLyingForAuctionReport {

	@ApiModelProperty(notes = "Depot Name")
	private String depotName;

	private String tyreCount;

	@ApiModelProperty(notes = "Tyre No")
	private String tyreNumber;

	@ApiModelProperty(notes = "Make")
	private String make;

	@ApiModelProperty(notes = "Condemn Date")
	private String condemnDate;

	@ApiModelProperty(notes = "Total Tyres")
	private String total;

}
