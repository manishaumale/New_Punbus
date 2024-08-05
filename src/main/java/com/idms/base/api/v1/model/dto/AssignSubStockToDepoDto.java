package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description ="AssignSubStockinDepo Model")
public class AssignSubStockToDepoDto {
	private Integer subStockId;
	private String depotName;
	private Integer transportId;
	private Integer denomination;
	private List<SubStockSerialNoToDepoDto> substockSerialNoDetails;

}
