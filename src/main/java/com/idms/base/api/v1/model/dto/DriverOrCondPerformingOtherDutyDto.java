

package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Driver Or Cond Performing Other Duty")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverOrCondPerformingOtherDutyDto {

    /**
	 * @author Hemant Makkar
	 */
	
	 @ApiModelProperty(notes = "Name")
	  private String name;
	 
	 //private String conductorName;
	
	@ApiModelProperty(notes = "Driver/Conductor No")
    private String driverOrConductotNo;
	
	@ApiModelProperty(notes = "Driver/Conductor Code")
    private String driverOrConductorCode;
    
    @ApiModelProperty(notes = "Order No")
    private String orderNumber;
    
    @ApiModelProperty(notes = "Order Date")
    private String orderDate;
    
    @ApiModelProperty(notes = "From Date")
    private String fromDate;
    
    @ApiModelProperty(notes = "To Date")
    private String toDate;
    
    @ApiModelProperty(notes = "Duty Type")
    private String dutyType;
    
    @ApiModelProperty(notes = "Remarks")
    private String remarks;
    
}

