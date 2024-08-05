package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TyreNotAuctionReportDto {

	private String make;
	
	private String tyreType;
	
	private String tyreSize;
	
	private String tyreNumber;
	
	private String takenOffDate;
	
	private String reason;
}
