package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Scheduled Kms Missed Report Dto")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduledKmsMissedReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Date")
    private String date;
    
	@ApiModelProperty(notes = "Depot Name")
    private String depotId;
    
    
	@ApiModelProperty(notes = "Route")
    private String route;
	
	@ApiModelProperty(notes = "Trip")
    private String trip;
	
	@ApiModelProperty(notes = "Time")
    private String time;
	
	@ApiModelProperty(notes = "Scheduled Kilometers")
    private String scheduledKilometers;
	
	@ApiModelProperty(notes = "Date Of Last Operation")
    private String dateOfLastOperation;
	
	private String busNo;
	
	private String actualKMs;
	
	
    
}
