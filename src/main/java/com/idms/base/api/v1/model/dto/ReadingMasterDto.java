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
@ApiModel(description = "Reading Master Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadingMasterDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Reading Name")
    private Float reading;
	
	@ApiModelProperty(notes = "Volume")
	private Float volume;
	
	@ApiModelProperty(notes = "Dip Reading Model")
	@JsonIgnore
	private DipReadingMasterDto dip;
	
	public ReadingMasterDto(Integer id, Float reading, Float volume) {
		this.id = id;
		this.reading = reading;
		this.volume = volume;
	}
	

}
