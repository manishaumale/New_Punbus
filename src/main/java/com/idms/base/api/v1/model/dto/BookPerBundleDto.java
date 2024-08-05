package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "BookPerBundleDto Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookPerBundleDto {
	
	private Integer id;
	
	private Integer tbCount;
	
	public BookPerBundleDto(Integer id, Integer tbCount) {
		super();
		this.id=id;
		this.tbCount=tbCount;
	}

}
