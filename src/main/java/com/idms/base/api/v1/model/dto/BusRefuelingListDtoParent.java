package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Refueling Parent Model")
public class BusRefuelingListDtoParent {
	
	List<BusRefuelingListDto> busRefuelingList;
	
	List<DispensingUnitHelperDto> duObjList;

}
