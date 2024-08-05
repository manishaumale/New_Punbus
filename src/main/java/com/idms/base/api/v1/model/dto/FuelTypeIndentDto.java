package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class FuelTypeIndentDto {
	private Integer id;
	private String name;
	
	 public FuelTypeIndentDto(Integer id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

}
