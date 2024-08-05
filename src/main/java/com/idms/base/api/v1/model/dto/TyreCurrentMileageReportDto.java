package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TyreCurrentMileageReportDto {
	
	private String tyreTag;
    private String tyreNumber;
    private String expectedMileage;
    private String achievedKMs;
    private String achievedKMsDate;
    private String busNumber;
    private String tyrePosition;
    private String tyreCondition;

}
