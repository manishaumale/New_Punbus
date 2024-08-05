package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TankCurrentValAndCapacityDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "CurrentValue")
	private Double currentValue;
	
	@ApiModelProperty(notes = "Capacity")
	private Double tankCapacity;

}
