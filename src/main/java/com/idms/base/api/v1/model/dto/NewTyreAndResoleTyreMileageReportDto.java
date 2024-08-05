package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class NewTyreAndResoleTyreMileageReportDto {

	private String tyreTag;
	
	private String tyreNumber;
	
	private String tyreCondition;
	
	private String fromDate;
	
	private String toDate;
	
	private String kmsDone;
}
