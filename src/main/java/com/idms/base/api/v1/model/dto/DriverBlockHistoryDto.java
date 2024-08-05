package com.idms.base.api.v1.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DriverBlockHistoryDto {

	private Integer id;

	private String name;

	private String badge;

	private String code;

	private Date fromDate;

	private Date toDate;
	
	private String reason;
	
	private Boolean blocked;
	
	private String entity;
	
	private String type;
	
	private String orderNumber;
	
	private Date orderDate;
	
	private Boolean routeOff;
	
	private Boolean isBlocked;
}

