package com.idms.base.api.v1.model.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CalibrationDispensingUnitDto {

	private Integer dispensingUnit;
	
	private String dispensingUnitCode;
	
	private Date calibrationDate;
	
	private String remarks;

	private Date certificateValidity;
	
	private Integer dieselIssued;
	
	private Integer tankId;
	
	private Double discharge;
	
	private Double excess;
	
	private Double Short;
	
	private Date nextDueDateforCalibration;
	
	private String depotCode;
	
    private Integer duStartReading;
	
	private Integer duStopReading;
	
	private Double physicalMeasurement;
	
	private Boolean fuelSubmittedFlag;
	
	private Boolean calibrationDoneFlag;
	
}
