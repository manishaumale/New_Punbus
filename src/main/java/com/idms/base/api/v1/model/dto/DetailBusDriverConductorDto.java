package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class DetailBusDriverConductorDto {
	
	private String  driverOrConductorCode;
	
	private String driverOrConductorName;
	
	private String date;
	
	private String routeName;
	
	private String epkm;
	
	private String busNo;
	
	private String kmpl;

}
