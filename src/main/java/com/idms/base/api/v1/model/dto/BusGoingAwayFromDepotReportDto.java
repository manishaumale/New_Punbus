package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Bus Going Away From Depot Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusGoingAwayFromDepotReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Time")
    private String time;
    
    @ApiModelProperty(notes = "Bus Type")
    private String busType;
    
    
    @ApiModelProperty(notes = "Bus No")
    private String busNumber;
    
    @ApiModelProperty(notes = "Driver Code")
    private String driverCode;
    
    @ApiModelProperty(notes = "Conductor Code")
    private String conductorCode;
    
    @ApiModelProperty(notes = "Driver Name")
    private String driverName;
    
    @ApiModelProperty(notes = "Conductor Name")
    private String conductorName;
    
    @ApiModelProperty(notes = "Link VTS")
    private String linkVTS;
    
}
