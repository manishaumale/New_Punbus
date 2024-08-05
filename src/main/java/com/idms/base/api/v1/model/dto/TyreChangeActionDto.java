package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@ApiModel(description = "TyreChangeAction Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreChangeActionDto {
	
	private Integer id;
	
	private String actionName;
	
	private String actionCode;

}
