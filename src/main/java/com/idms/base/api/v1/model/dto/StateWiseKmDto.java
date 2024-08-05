package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "State Wise Km Model")
public class StateWiseKmDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "State Wise Km Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
	@ApiModelProperty(notes = "State Model")
	private StateDto state;
	
	@ApiModelProperty(notes = "kilometers Details")
	private Integer kilometers;
	
	@ApiModelProperty(notes = "Permit Details Model")
	@JsonIgnore
	private PermitDetailsDto permitDetails;
    
  
}
