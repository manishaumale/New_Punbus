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
@ApiModel(description = "Fuel Tank Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DipReadingMasterDto2 {

	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Reading Name")
	private String readingName;
	
	@ApiModelProperty(notes = "Tank Diameter")
    private Float diameter;
	
	@ApiModelProperty(notes = "Tank Radius")
	private Float radius;
	
	@ApiModelProperty(notes = "Tank Length")
	private Float length;
	
	@ApiModelProperty(notes = "Tank Deadboard")
	private Float deadboard;

	public DipReadingMasterDto2(Integer id, String readingName, Float diameter,
			Float radius, Float length, Float deadboard) {
		super();
		this.id = id;
		this.readingName = readingName;
		this.diameter = diameter;
		this.radius = radius;
		this.length = length;
		this.deadboard = deadboard;
	}

	
	
}
