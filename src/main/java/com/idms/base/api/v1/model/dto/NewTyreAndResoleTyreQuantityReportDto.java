package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class NewTyreAndResoleTyreQuantityReportDto {
	
	private String tyreType;
	
	private String toDate;
	
	private String fromDate;
	
	private String count;
	
	private String valueInRs;

}
