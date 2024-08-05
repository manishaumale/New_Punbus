package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class InspectionDoneVersusDueReportDto {

	private String depotName;

	private String dueInspection;
	
	private String doneInspection;
	
	private String userName;
	
	private String monthName;
	
	private String roleName;

}
