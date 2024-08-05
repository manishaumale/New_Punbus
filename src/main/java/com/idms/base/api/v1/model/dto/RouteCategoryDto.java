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
@ApiModel(description = "Route Category Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteCategoryDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "Route Category Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    @ApiModelProperty(notes = "Route Category Name")
    private String routeCategoryName;
   
	public RouteCategoryDto(Integer id, String routeCategoryName) {
		super();
		this.id = id;
		this.routeCategoryName = routeCategoryName;
	}
}
