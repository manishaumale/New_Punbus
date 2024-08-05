package com.idms.base.api.v1.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TyreRepairDto {

	private Integer id;
	
	private String tyreNumber;
	
	private String tyreTag;
	
	private Boolean available;
	
	private String depot;
	
	private String tyreCondition;
	
	private String busRegNumber;


	private Float totalKmsDone;

	
	private Date date;
	
	private String expectedLife;
	
}
