package com.idms.base.api.v1.model.dto;

import java.sql.Timestamp;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Inspection Tank Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspectionTankDto {
	
	private DipChartReadingsDto bookReading;
	
    private FuelTankMasterDto fuelTankMasterOne;
	
	private FuelTankMasterDto fuelTankMasterSec;
	
	private ReadingMasterDto readingMasterOne;
	
	private ReadingMasterDto readingMasterSec;
	
	private Double fuelVolumeTankOne;
	
	private Double fuelVolumeTankSecond;
	
	private Double closeTotalOpeningFuelVolume;
	
	private DispensingUnitMasterDto dispensingMasterOne;
	
	private DispensingUnitMasterDto dispensingMasterSec;
	
	private Double dispensingUnitReadingOne;
	
	private Double dispensingUnitReadingSec;
	
	private String remarks;
	
	private Date nextInspectionDate;
	
	private Double dispenseFuelEvening;
	
    private Double variation;
	
    private Boolean variationFlag;
    
    private Timestamp createdOn;
}
