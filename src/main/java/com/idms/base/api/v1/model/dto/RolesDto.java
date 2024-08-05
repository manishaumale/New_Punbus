package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Roles Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolesDto {
	
	String id;
	
	String name;
	
	String description;
	
	String composite;
	
	String clientRole;
	
	String containerId;
	
	Object attributes;
	
	String roleCode;
	
	String ids;
	
	private List<RoleModuleAccessDto> modules;
	
	public RolesDto(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
//	public RolesDto(String name, String description,Integer id) {
//		super();
//		this.name = name;
//		this.description = description;
//		this.id = id;
//	}
	
}
