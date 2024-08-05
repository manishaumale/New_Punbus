package com.idms.base.api.v1.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TyreHistoryDto {

	private String tyreNumber;
	
	private Integer tyreId;
	
	private Date repairDate;
	
	private String tyreManufacture;
	
	private String tyreSize;
	
	private String tyreCondition;
	
	private Double tyreRecoverCost;
	
	private String tyreInstallationDate;
	
	private String KmsDoneInBus;
	
	private Float KmsInCondition;
	
	private String totalKmsDone;
	
	private String tyrePosition;	
	

}
