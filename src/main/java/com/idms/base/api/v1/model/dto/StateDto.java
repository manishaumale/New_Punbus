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
@ApiModel(description = "State Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateDto extends BaseEntityDto{

    @ApiModelProperty(notes = "State Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
	@ApiModelProperty(notes = "State Code")
	//@Size(min = 1, max = 4)
    private String stateCode;
    
    @ApiModelProperty(notes = "State Name")
	//@Size(min = 1, max = 50)
    private String stateName;
    
    
    @ApiModelProperty(notes = "Status")
    private Boolean status;

	public StateDto(Integer id, String stateName) {
		super();
		this.id = id;
		this.stateName = stateName;
	}
}