package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TyreMeasurementDto {
	private Integer id;

	private Integer count;

	public TyreMeasurementDto(Integer id,Integer   count) {
		super();
		this.id = id;
		this.count =count;
	}

}
