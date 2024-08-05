package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@ApiModel(description = "TyreChangeForm Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreChangeFormDto {
	
	List<TyreChangeActionDto> tyreChangeActions;
	
	List<TakenOffReasonDto> takenOffReasons;
	
	List<TyrePositionDto> positions;
	
	List<TyreMasterDto> tyreList;
	
	List<BusTyreAssociationDto> associtionsList;
	
	List<TyreSizeDto> sizeList;
	
	List<TyreMakerDto> makeList;
	
	List<TyreTypeDto> typeList;
	
	
	
}
