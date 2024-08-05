package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class IssueEtmObjDto {

	private String conductorName;
	
	private String conductorCode;
	
	private String dateTime;
	
	private String routeCode;
	
	private String routeName;
	
	private String etmNumber;
	
	private String busNumber;
	
	private String tripName;
	
	private String wayBill;
	
	private Integer conductorId;
	
	private Integer etmId;
}
