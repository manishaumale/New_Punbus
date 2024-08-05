package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Routes Less Than Eight Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutesLessThanEightReportDto {

    /**
	 * @author Hemant Makkar
	 */
	 private String routeCode;
	 @ApiModelProperty(notes = "Name")
	  private String name;
	
	 @ApiModelProperty(notes = "Route No")
     private String routeNo;
	
	
	 @ApiModelProperty(notes = "Route Category")
     private String routeCategory;
	 
	 @ApiModelProperty(notes = "Scheduled Kms")
     private String scheduledLms;
	  
     
}





