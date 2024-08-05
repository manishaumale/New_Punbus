package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class IndentTyreFormDto 
{
	private List<TyreMakerDto> tyreMaker;
	private List<TyreTypeDto> tyreType;
	private List<TyreSizeDto> tyreSize;
	private List<TyreMeasurementDto> tyreMeasurement;

}
