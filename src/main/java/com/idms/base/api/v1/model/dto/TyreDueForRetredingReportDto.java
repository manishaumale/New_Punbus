package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TyreDueForRetredingReportDto {
	private String transName;
    private String depotName;
    private String tyreTag;
    private String tyreNumber;
    private String tyreManufacture;
    private String size;
    private String name;
    private String tyreCondition;                
    private String expectedLife;
    private String totalKmsDone;                
    private String extraKmsDone;
    private String tyreRecoveredCost;              
    private String tyreInstallationDate;
    private String tyrePosition;               
    private String driverName;
    private String busNo;               
    private String routeName;
    private String retreadingDuedate;

}
