package com.idms.base.api.v1.model.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Other Duty Model")
public class OtherDutyDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
    private DriverMasterDto driverMaster;
	
	private ConductorMasterDto conductorMaster;
	
	private DutyTypeDto dutyType;
	
	private String orderNo;
	
	private String remarks;
	
	private Date fromDate;
	
	private Date toDate;
	

}
