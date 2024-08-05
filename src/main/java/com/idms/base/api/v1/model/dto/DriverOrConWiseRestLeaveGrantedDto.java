
package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Driver Or ConWiseRest Leave Granted Dto")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverOrConWiseRestLeaveGrantedDto {

    /**
	 * @author Hemant Makkar
	 */
	
	 @ApiModelProperty(notes = "Date")
	  private String date;
    
    
    @ApiModelProperty(notes = "Total Drivers")
    private String totalDrivers;
    
    @ApiModelProperty(notes = "Total CL Granted")
    private String totalCL;
    
    @ApiModelProperty(notes = "Total REST Granted")
    private String totalRestGranted;
    
    @ApiModelProperty(notes = "Total Driver Present")
    private String totalDriversPresent;
    
    @ApiModelProperty(notes = "Minimum Required Dreivers")
    private String minimum;
    
    
    @ApiModelProperty(notes = "Remarks")
    private String remarks;
    
}

