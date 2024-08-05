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
public class TyreMasterFormDto {
	
	private Integer depotId;
	
	private List<TransportDto> tpList;
	
	private List<TyreTypeDto> tyreTypeList;
	
	private List<TyreSizeDto> tyreSizeList;
	
	private List<TyreMakerDto> tyreMakerList;
	
	private List<TyreConditionDto> tyreConditionList;
	
	private List<TyrePositionDto> tyrePositionList;
	
	private List<BusMasterDto> busList;
	
	private List<TyreMgmtDto > tyreNumber;
	
	private List<TakenOffReasonDto > takenOffReasonList;
	
	List<DepotMasterDto> depotList;
	
	
}
