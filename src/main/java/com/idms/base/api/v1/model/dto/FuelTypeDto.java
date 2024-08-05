package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Fuel Type Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuelTypeDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Fuel Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Fuel Type Name")
    private String fuelTypeName;
	
	private String shortCode;
	
	public FuelTypeDto(Integer id,String fuelTypeName) {
		   this.id = id;
		   this.fuelTypeName =  fuelTypeName;
	}
	
	
}