package com.idms.base.api.v1.model.dto;

import java.util.List;

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
@ApiModel(description = "Dip Reading Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DipReadingMasterDto extends BaseEntityDto{
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Reading Name")
	private String readingName;
	
	@ApiModelProperty(notes = "Tank Capacity Model")
	private TankCapacityMasterDto capacityMaster;
	
	@ApiModelProperty(notes = "Tank Diameter")
    private Float diameter;
	
	@ApiModelProperty(notes = "Tank Radius")
	private Float radius;
	
	@ApiModelProperty(notes = "Tank Length")
	private Float length;
	
	@ApiModelProperty(notes = "Tank Deadboard")
	private Float deadboard;
	
	@ApiModelProperty(notes = "Reading Master List")
	private List<ReadingMasterDto> readings;
	
	
	
	public DipReadingMasterDto(Integer id,String readingName) {
		this.id = id;
		this.readingName = readingName;
	}

}
