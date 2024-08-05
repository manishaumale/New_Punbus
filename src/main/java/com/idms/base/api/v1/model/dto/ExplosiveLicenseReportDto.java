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
public class ExplosiveLicenseReportDto {

	@ApiModelProperty(notes = "Depot Name")
	private String depotName;

	@ApiModelProperty(notes = "Explosive License")
	private String explosiveLicenseValidity;

	private Integer explosive_licence_id;

	private String document_name;

	private String document_path;

	@ApiModelProperty(notes = "License Details")
	private String licenseDetails;

}
