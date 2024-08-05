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
@ApiModel(description = "Bus Refueling Master Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MobilOilUsedDto extends BaseEntityDto{
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Bus Master Model")
	private BusMasterDto busMaster;
	
	private Float quantity;
	
	private String purpuse;
	
	private String mobilOilDrumName;
	
	private DocumentDto mobilOilDocument;
	
    private Boolean isOutSide;
	
	private Float amount;

}
