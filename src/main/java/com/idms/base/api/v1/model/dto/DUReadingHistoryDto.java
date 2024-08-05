package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "DUReadingHistoryDto Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DUReadingHistoryDto {
	
	private Integer id;
	
	private Double duStartReading;
	
	private Double duEndReading;
	
	public DUReadingHistoryDto(Integer id, Double duStartReading, Double duEndReading) {
		this.id = id;
		this.duStartReading = duStartReading;
		this.duEndReading = duEndReading;
	}

}
