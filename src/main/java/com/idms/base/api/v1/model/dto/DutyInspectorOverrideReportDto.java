
package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Duty Inspector Override Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DutyInspectorOverrideReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	 @ApiModelProperty(notes = "Name")
	  private String name;
	
	@ApiModelProperty(notes = "Driver/Conductor No")
    private String driverOrConductotNo;
	
	
	  @ApiModelProperty(notes = "Route Name")
	  private String routeName;
	  
	  @ApiModelProperty(notes = "Override Reason")
	  private String overrideReason;
	  
	  
	  @ApiModelProperty(notes = "New Route")
	  private String newRoute;
    
    
      @ApiModelProperty(notes = "Remarks")
      private String remarks;
     
}


