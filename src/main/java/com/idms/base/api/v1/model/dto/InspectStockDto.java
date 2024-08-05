package com.idms.base.api.v1.model.dto;

import java.sql.Time;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InspectStockDto {

	private String dieselTank;
	
	private String dieselTankCode;
	
	private Date installationDate;
	
	private Double capacity;
	
	private Integer AutoDipReading ;
	
	private Integer autoReading;
	
	private Integer inspectionDipReading;
	
	private Double inspectedVolume;
	
	private Date inspectionDate;
	
	private Time inspectionTime;
	
	private Double accessShort;
	
	private String remarks;
	
	private Integer id;
	
}
