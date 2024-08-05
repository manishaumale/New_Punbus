package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.TyreMaker;
import com.idms.base.dao.entity.TyreSize;
import com.idms.base.dao.entity.TyreType;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "MakerTyreDetails Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakerTyreDetailsDto {
	
	private Integer id;
	
	private TyreMakerDto maker;
	
	private TyreTypeDto tyreType;
	
	private TyreSizeDto tyreSize;
	
	private Integer expectedLife;
	
	private double tyreCost; 

}
