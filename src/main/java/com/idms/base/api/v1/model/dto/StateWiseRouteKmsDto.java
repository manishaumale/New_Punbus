package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idms.base.dao.entity.RouteMaster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "State Wise Route Km Model")
public class StateWiseRouteKmsDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "State Wise Km Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
	@ApiModelProperty(notes = "State Model")
	private StateDto state;
	
	@ApiModelProperty(notes = "Route Model")
	@JsonIgnore
	private RouteMasterDto routeMaster;
	
	@ApiModelProperty(notes = "Total kilometers")
	 private Integer totalKms;
	 
	@ApiModelProperty(notes = "PlainKms kilometers")
	 private Integer plainKms;
	
	@ApiModelProperty(notes = "Hill kilometers")	
	 private Integer hillKms;
    
  
}
