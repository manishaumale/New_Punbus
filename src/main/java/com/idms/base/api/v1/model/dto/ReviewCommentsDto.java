package com.idms.base.api.v1.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ReviewCommentsDto {

	private Integer id;
	
	private String type;
	
	private String Name;
	
	private String routeName;
	
	private String remarks;
	
	private Date reviewDate;
	
	private Date nextReviewDate;
	
	private String category;
	
	private String typeCode;
	
	private String UniqueId;
	
}
