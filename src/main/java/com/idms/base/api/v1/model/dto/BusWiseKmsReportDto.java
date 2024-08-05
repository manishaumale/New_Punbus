package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Bus Wise Kms Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusWiseKmsReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Date")
    private String date;
	
	private String deadKms;
    
	@ApiModelProperty(notes = "Scheduled Kilometers")
    private String achievedKilometers;
    
    
	@ApiModelProperty(notes = "Total Kilometers")
    private String totalKilometers;
	
	@ApiModelProperty(notes = "Special Kilometers")
    private String specialKilometers;
	
	@ApiModelProperty(notes = "Missed Kilometers")
    private String missedKilometers;
	
	@ApiModelProperty(notes = "Spare/Less Buses")
    private String spareOrLessBuses;

	
	@ApiModelProperty(notes = "Spare/Less Drivers")
    private String spareOrLessDrivers;
	
	@ApiModelProperty(notes = "Spare/Less Conductors")
    private String spareOrLessConductors;
	
	private String busType;
	
	private String busNumber;
	
	private String transportName;
	
	private String vtsMissedKMs;
	
	private String vtsKMs;
    
}
