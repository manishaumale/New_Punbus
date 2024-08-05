package com.idms.base.api.v1.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MasterStockSerialDto {
	
	private Integer id;

	private Integer startSrNo;

	private Integer endSrNo;
	
	private String ticketBookNo;
	
	private MasterStockDto masterStock;
	
	private DenominationDto denomination;
	
	private String seriesNumber;
	
	private Integer centralStockId;
	
	private CentralTicketStockDto centeralStock;
	
}
