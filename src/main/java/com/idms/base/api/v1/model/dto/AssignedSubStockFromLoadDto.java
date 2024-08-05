package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignedSubStockFromLoadDto {
	
	private List<DepotMasterDto> depotList;
	
	private List<TransportDto> transportList;
	
	private List<DenominationDto> denominationList;

}
