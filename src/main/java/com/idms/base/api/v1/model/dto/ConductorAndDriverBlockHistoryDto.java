package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class ConductorAndDriverBlockHistoryDto {
	
	private List<DriverBlockHistoryDto> driverBlockHistory;
	
}
