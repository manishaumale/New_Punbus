package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTyreAssociationForm {
	
	Integer busId;
	
	List<BusTyreAssociationDto> busTyreAssociation;
	
	List<TyreMasterDto> tyreList;
	
	List<TyrePositionDto> tyrePositions;
	
	List<TyreSizeDto> tyreSizeList;
	
	List<TyreMakerDto> tyreMakerList;
	
	List<TyreTypeDto> tyreTypeList;

}
