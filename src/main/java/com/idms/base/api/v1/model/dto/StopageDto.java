package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Stopages Model")
public class StopageDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	private String stopageName;
	
	@ApiModelProperty(notes = "Route Rotation Model")
	@JsonIgnore
	private RouteRotationDto routeRotation;
	@JsonIgnore
	private CityDto city;
	
	public StopageDto(Integer id, String stopageName) {
		super();
		this.id = id;
		this.stopageName = stopageName;
	}

}
