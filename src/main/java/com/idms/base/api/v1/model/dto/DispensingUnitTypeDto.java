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
@ApiModel(description = "Dispensing Unit Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DispensingUnitTypeDto extends BaseEntityDto{
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Dispensing Unit Name")
    private String disUnitTypeName;
	
	public DispensingUnitTypeDto(Integer id,String disUnitTypeName) {
		this.id = id;
		this.disUnitTypeName = disUnitTypeName;
	}

}
