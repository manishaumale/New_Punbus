package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Condition")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreConditionDto {
	
	private Integer id;
	
	private String name;
	
	public TyreConditionDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

}
