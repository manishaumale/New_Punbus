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
@ApiModel(description = "Via Info Model")
public class ViaInformationDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "Via Info Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
	@ApiModelProperty(notes = "State Model")
	private StateDto state;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto city;
	
	@ApiModelProperty(notes = "Permit Details Model")
	@JsonIgnore
	private PermitDetailsDto permitDetailsMaster;
    
  
}