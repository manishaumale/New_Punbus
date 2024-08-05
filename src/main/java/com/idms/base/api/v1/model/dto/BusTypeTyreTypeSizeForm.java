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
@ApiModel(description = "Form Load Data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTypeTyreTypeSizeForm {
	
	List<BusTypeDto> busTypeList;
	
	List<TyreTypeDto> tyreTypeList;
	
	List<TyreSizeDto> tyreSizeList;

}
