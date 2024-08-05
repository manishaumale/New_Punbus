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
public class DispensingUnitReportDto {

	@ApiModelProperty(notes = "Depot Name")
	private String depotName;

	@ApiModelProperty(notes = "Dispensing Unit")
	private String dispensingUnitName;

	private String disUnitTypeName;
	private String updatedOn;

	@ApiModelProperty(notes = "Dispensing Unit Tyre")
	private String dispensingUnitTyre;

	@ApiModelProperty(notes = "last Operated Date")
	private String lastOperatedDate;

}
