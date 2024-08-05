package com.idms.base.api.v1.model.dto;

import java.time.LocalTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "TankInspection Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TankInspectionDto {
	
	private Integer id;
	
	private DepotMasterDto depot;

	private FuelTankMasterDto tank;
	
	private Double prevVolume;
	
	private ReadingMasterDto inspectedReading;
	
	private Double inspectedVolume;
	
	private Date inspectionDate;
	
	private LocalTime inspectionTime;
	
	private Double accessShort;
	
	private String remarks;
	
	private Float autoDipReading;
	
	private Date nextInspectionDate;
}
