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
@ApiModel(description = "Tank Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TankCapacityMasterDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Tank Name")
    private String name;
	
	@ApiModelProperty(notes = "Tank Capacity")
	private Double capacity;
	
	public TankCapacityMasterDto(Integer id,String name,Double capacity) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
	}

}
