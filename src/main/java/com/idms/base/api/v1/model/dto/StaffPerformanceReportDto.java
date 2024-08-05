package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Staff Performance Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffPerformanceReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	  @ApiModelProperty(notes = "Name")
	  private String name;
	
	  @ApiModelProperty(notes = "Driver/Conductor No")
      private String driverOrConductotNo;
	
	
	  @ApiModelProperty(notes = "Date Of Inclusion")
	  private String date;
	  
	  @ApiModelProperty(notes = "Deputed Route")
	  private String deputedRoute;
	  
	  @ApiModelProperty(notes = "Depot Name")
	  private String depotName;
	  
	  @ApiModelProperty(notes = "Reciept Month")
	  private String recieptMonth;
	  
	  @ApiModelProperty(notes = "Reason")
	  private String reason; 
     
}





