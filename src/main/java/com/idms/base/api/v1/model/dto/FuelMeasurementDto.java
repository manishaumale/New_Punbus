package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class FuelMeasurementDto {
	 private Integer id;
	 private Integer liters;
	public FuelMeasurementDto(Integer id,Integer  liters) {
		super();
		this.id = id;
		this. liters =  liters;
	}

}
