package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Mobil Oil Drum Model")
public class MobilOilDrumMasterDto {
	
	@ApiModelProperty(notes = "Mobil Oil Drum Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Mobil Oil Drum Name")
    private String nameOfDrum;
	
	@ApiModelProperty(notes = "Depot Master Dto")
    private String depotCode;
	
    private Float capacity;
	
	private Integer noOfDrums;
	
	private Float totalCapacity;
	
	private Integer reorderLevel;
	
	private String availableStock;
	
    private Float value;
		
    private TransportDto transportUnit;
	
	public MobilOilDrumMasterDto(Integer id, String nameOfDrum) {
		this.id = id;
		this.nameOfDrum = nameOfDrum;
	}
	
	public MobilOilDrumMasterDto(Integer id, String nameOfDrum,Float totalCapacity) {
		this.id = id;
		this.nameOfDrum = nameOfDrum;
		this.totalCapacity = totalCapacity;
	}

}
