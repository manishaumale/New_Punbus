package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class DutyRosterTotalKMsReportDto {
	
    private String rotaDayCode;
	
	private String rotaDate;
	
	private String depoName;
	
	private String rosterKms;
	
	private String allotedKms;
	
	private String vtsKMs;
	
}
