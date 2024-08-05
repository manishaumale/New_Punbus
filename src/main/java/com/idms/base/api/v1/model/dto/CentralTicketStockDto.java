package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CentralTicketStockDto extends BaseEntityDto {

	private Integer id;
	
	private TransportDto transport;

	private StateDto state;

	private DenominationDto denomination;

	private boolean stateTax;
	
	private List<MasterStockDto> bundleStockDtls;
	
	private String bundleNumber;
	
	private String seriesNumber;
	
	private Integer startSrNo;
	
	private Integer endSrNo;
	
	private Integer ticketCount;
	
	private Integer ticketTotalAmount;
	
}
