package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class KMsLikelyToBeReceivedReportDto {

   private String rotaDayCode;
	
	private String rotaDate;
		
	private String toBeReceived;
	
	private String allotedKms;
}
