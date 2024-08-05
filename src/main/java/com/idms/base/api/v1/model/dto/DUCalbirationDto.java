package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.FuelTankMaster;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "DU Calibration Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DUCalbirationDto {
	
	private Integer id;
	
	private DispensingUnitMasterDto dispensingUnit;
	
	private Date calibrationDate;
	
	private Date certificateValidity;
	
	private String remarks;
	
	private Date nextInspectionDueDate;
	
	private DocumentDto calibrationCertificate;
	
	private Double startReading;
	
	private Double endReading;
	
	private String tankName;
	
	private String dispensingUnitCode;
	
	private String dispensingUnitName;
	
    private Double discharge;
	
	private Double excess;
	
	private Double Short;
	
	private Integer duStartReading;

	private Integer duStopReading;

	private Double physicalMeasurement;

	private Boolean fuelSubmittedFlag;

	private Boolean calibrationDoneFlag;
	

}
