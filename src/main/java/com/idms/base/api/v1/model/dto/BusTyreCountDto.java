package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Tyre Count Model")
public class BusTyreCountDto {
	
	private Integer id;
	
	private Integer count;
	
	public BusTyreCountDto(Integer id, Integer count) {
		super();
		this.id = id;
		this.count = count;
	}

}
