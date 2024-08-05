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
@ApiModel(description = "Diesel Issued For Other Purpose")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DieselIssuedForOtherPurposeDto extends BaseEntityDto{
	
	 /**
		 * @author Hemant Makkar
		 */
		
    @ApiModelProperty(notes = "Diesel Issued Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	private DispensingUnitMasterDto dispensingUnitMaster;
	
	private OtherPurposeDto otherPurpose;
	
	private FuelTypeDto fuelType;
	
	private FuelTankMasterDto fuelTankMaster;
	
	private MobilOilDrumMasterDto mobilOilDrumMaster;
	
	private AddBlueDrumMasterDto addBlueDrumMaster;
	
	private Float dieselIssued;
	
    private Double duStartReading;
	
	private Double duEndReading;
	
	private String vehicleNo;
	
	private String remarks;
	

}