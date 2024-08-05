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
@ApiModel(description = "Update Supply Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateSupplyDto extends BaseEntityDto{
	
	 /**
		 * @author Hemant Makkar
		 */
		
    @ApiModelProperty(notes = "Update Supply Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    @ApiModelProperty(notes = "Fuel Tank Model")
	private FuelTankMasterDto fuelTankMaster;
	
	private Float dipReadingBefore;
	
	private Float dipReadingAfter;
	
    private Float volumeBeforeSupply;
	
	private Float volumeAfterSupply;
	
	private Float totalVolume;
	
	private Float variants;
	
	private String excessOrShort;
	
	private String excessOrShortVal;
	

}