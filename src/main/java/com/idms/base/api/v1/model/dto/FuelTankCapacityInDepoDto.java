package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel(description="Fuel Tank Capacity In Depo Model")
@AllArgsConstructor
public class FuelTankCapacityInDepoDto {
	private String depoName;
	private Long totalTanks;
	private double totalCapityInDepo;
	

}
