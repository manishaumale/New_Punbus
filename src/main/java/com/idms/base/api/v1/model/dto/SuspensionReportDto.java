package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Suspension Report Dto")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuspensionReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	 @ApiModelProperty(notes = "Name")
	  private String name;
	
	@ApiModelProperty(notes = "Driver/Conductor No")
    private String driverOrConductotNo;
	
	
	  @ApiModelProperty(notes = "Suspended Since")
	  private String suspendedSince;
	  
	 
     
}



