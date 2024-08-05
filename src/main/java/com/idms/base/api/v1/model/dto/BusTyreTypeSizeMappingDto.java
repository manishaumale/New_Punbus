package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.TyreSize;
import com.idms.base.dao.entity.TyreType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Type, Tyre Type & Size Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTyreTypeSizeMappingDto {
	
	private Integer id;
	
	private BusTypeDto busType;
	
	private TyreTypeDto tyreType;
	
	private TyreSizeDto tyreSize;
	
	 @ApiModelProperty(notes = "Status")
	   private Boolean status;

}
