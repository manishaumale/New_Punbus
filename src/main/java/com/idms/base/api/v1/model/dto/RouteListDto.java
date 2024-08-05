
package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Route Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteListDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Route Name")
	private String routeName;
	
	@ApiModelProperty(notes="Route Code")
	private String routeCode;
	

}
