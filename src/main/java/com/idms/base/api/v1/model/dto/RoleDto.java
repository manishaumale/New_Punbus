package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Role Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
	

	/**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	private String name;
	
	private String roleCode;
	
	private String description;
	
	public RoleDto(String name, String description,Integer id) {
    	super();
    	this.name = name;
    	this.description = description;
        this.id = id;
     }

}
