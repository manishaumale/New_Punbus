package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "VTS Response Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VTSResponseDto {
	
	Integer status;
	
	String msg;
	
	VTSDataDto data;

}
