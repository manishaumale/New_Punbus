package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Route Type Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteTypeDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Route Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    @ApiModelProperty(notes = "Route Type Name")
    private String routeTypeName;
    

	public RouteTypeDto(Integer id, String routeTypeName) {
		super();
		this.id = id;
		this.routeTypeName = routeTypeName;
	}
}
