package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class FuelSpecificationIndentDto {
	private Integer id;
	private String name;

	public FuelSpecificationIndentDto(Integer id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

}
