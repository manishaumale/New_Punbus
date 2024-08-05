package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Bus Not Sent On Route Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusNotSentOnRouteReportDto {

    /**
	 * @author Hemant Makkar
	 */
	@ApiModelProperty(notes = "transport Name")
    private String transportName;
	
	@ApiModelProperty(notes = "Bus Unit Type")
    private String busUnitType;
    
    @ApiModelProperty(notes = "Bus Type")
    private String busType;
    
    
    @ApiModelProperty(notes = "Bus No")
    private String busNumber;
    
    @ApiModelProperty(notes = "Date Of Last Route")
    private String dateOfLastRoute;
    
}
