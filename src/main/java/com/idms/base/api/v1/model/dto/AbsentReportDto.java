package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Absent Report Dto")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbsentReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	 @ApiModelProperty(notes = "Name")
	  private String name;
	
	@ApiModelProperty(notes = "Driver/Conductor No")
    private String driverOrConductotNo;
	
	@ApiModelProperty(notes = "Driver/Conductor Code")
    private String driverOrConductotCode;
	
	  @ApiModelProperty(notes = "Absent Since")
	  private String absentSince;
	  
	  @ApiModelProperty(notes = "Absent Days")
	  private String absentDays;
	  
	  @ApiModelProperty(notes = "Depot Name")
	  private String depotName;
	  
	  @ApiModelProperty(notes = "Rest Due")
	  private String restDues;
	  
	  @ApiModelProperty(notes = "Absent From")
	  private String absentFrom;
	  
	  @ApiModelProperty(notes = "Absent To")
	  private String absentTo;
	  
	 
     
}




