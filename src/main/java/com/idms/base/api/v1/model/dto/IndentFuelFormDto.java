package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class IndentFuelFormDto {
	private List<FuelTypeIndentDto> fuelTypeIndent;
	private List<FuelManufactureIndentDto> fuelManufactureIndent;
	private List<FuelSpecificationIndentDto> fuelSpecificationIndent;
	private List<FuelMeasurementDto> fuelMeasurement;

}
