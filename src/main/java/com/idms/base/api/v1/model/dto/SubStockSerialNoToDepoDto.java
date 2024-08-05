package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class SubStockSerialNoToDepoDto {
	
	private Integer subStockSerialId;
	private Integer startingSerialNumber;
	private Integer endingSerialNumber;

}
