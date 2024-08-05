package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "Stopages Master Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class StopagesMasterDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Departure Date")
	private String departureTime;
	
	@ApiModelProperty(notes = "Stopage Type Model")
	private StopageTypeDto stopageTypeMaster;
	
	@ApiModelProperty(notes = "Total Nights Model")
	private TotalNightsDto days;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto stpageName;
	
	@ApiModelProperty(notes = "Trip Model")
	@JsonIgnore
	private TripMasterDto tripMaster;
	
	@ApiModelProperty(notes = "Stopage Model")
	private StopageDto stopage;
	
}
