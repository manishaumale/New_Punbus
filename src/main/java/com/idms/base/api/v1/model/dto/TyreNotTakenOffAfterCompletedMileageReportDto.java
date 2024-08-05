package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TyreNotTakenOffAfterCompletedMileageReportDto {
	
	private String transName;
    private String depotName;
    private String tyreTag;
    private String tyreNumber;
    private String tyreManufacture;
    private String size;              
    private String tyreCost;
    private String tyreRecoveredCost;              
    private String tyreInstallationDate;
    private String kms;                
    private String totalKmsDone;             
    private String mileage;
}
