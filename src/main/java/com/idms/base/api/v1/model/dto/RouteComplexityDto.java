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
@ApiModel(description = "Route Complexity Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteComplexityDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "Route Complexity Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
    @ApiModelProperty(notes = "Route Complexity Name")
    private String rcName;
    
	public RouteComplexityDto(Integer id, String rcName) {
		super();
		this.id = id;
		this.rcName = rcName;
	}
}	