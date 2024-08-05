package com.idms.base.api.v1.model.dto;

import java.util.Date;

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
@ApiModel(description = "Dispensing Unit Master Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DispensingUnitMasterDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Dispense Unit Name")
	private String disUnitName;
	
	@ApiModelProperty(notes = "Dispense Unit Code")
	private String disUnitCode;
	
    private DispensingUnitTypeDto dispensingUnitTypeMaster;
	
	private Date installationDate;
	
	private Date inspectionPeriod;
	
	private Double initialReading;
	
	private Double currentReading;
	
	private Boolean status;
	
	public DispensingUnitMasterDto(Integer id,String disUnitName) {
		
		this.id = id;
		this.disUnitName = disUnitName;
		
	}
	
	private String calibrationDate;

}
