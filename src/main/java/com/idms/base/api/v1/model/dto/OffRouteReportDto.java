package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Off Route Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffRouteReportDto {

    /**
	 * @author Hemant Makkar
	 */
	private String driverCode;
	@ApiModelProperty(notes = "Driver")
    private String driverName;
	
	
	private String conductorCode;
	private String conductorName;
    
    @ApiModelProperty(notes = "Name")
    private String name;
    
    @ApiModelProperty(notes = "Driver Or Conductor Number")
    private String driverOrConductorNumber;
    
    @ApiModelProperty(notes = "Driver Or Conductor Code")
    private String driverOrConductorCode;
    
    
    @ApiModelProperty(notes = "Off Route Since")
    private String routeDate;
    
}