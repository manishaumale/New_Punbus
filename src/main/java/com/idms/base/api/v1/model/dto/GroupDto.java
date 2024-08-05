package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Group Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto {
	
	private String id;
	
	private String name;
	
	private String path;
	
	private Object subGroups[];

}
