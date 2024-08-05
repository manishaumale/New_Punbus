package com.idms.base.api.v1.model.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "BaseEntity Model")
public class BaseEntityDto {
	
	private String createdBy;
	
	private Timestamp createdOn;
	
	private String createdClientIp;
	
	private String updatedBy;
	
	private Timestamp updatedOn;
	
	private String updatedClientIp;
	
	private Boolean status;

}
