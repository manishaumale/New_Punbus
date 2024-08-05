package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Document sModel")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentDto {
	
	private Integer id;
	
	private String documentName;
	
	public DocumentDto(Integer id, String documentName) {
		super();
		this.id = id;
		this.documentName = documentName;
	}

}
