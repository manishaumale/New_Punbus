package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Driver Or Conductor Leave Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverOrConductorLeaveReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	 @ApiModelProperty(notes = "Name")
	  private String name;
	
	@ApiModelProperty(notes = "Driver/Conductor No")
    private String driverOrConductotNo;
    
    
    @ApiModelProperty(notes = "Total Presents")
    private String totalPresents;
    
    @ApiModelProperty(notes = "Total Absents")
    private String totalAbsents;
    
    @ApiModelProperty(notes = "Total On Leave")
    private String totalOnLeave;
    
    @ApiModelProperty(notes = "Rest")
    private String rest;
    
    @ApiModelProperty(notes = "CL")
    private String cl;
    
    @ApiModelProperty(notes = "EL")
    private String el;
    
    @ApiModelProperty(notes = "ML")
    private String ml;
    
    @ApiModelProperty(notes = "Off Route")
    private String offRoute;
    
}
