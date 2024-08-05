package com.idms.base.api.v1.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusDieselCorrectionDto {

	private Integer busrefuelingId;
	
	private String busRefuelingDate;
	
	private Integer busId;
	
	private String dispensingUnit;
	
	private String DieselTank;
	
	private String DieselTaken;
	
	private String CrtDieselTaken;
}
