package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Access Model")
public class DepotTicketStockDto {
	
	private DepotMasterDto depot;
	
	private TransportDto transport;
	
	private DenominationDto denomination;
	
	private Integer ticketBookCount;
	
	private Integer totalAmount;

	private Integer centralStockId ;
	
	private Integer stockDtlsId;
	
	private String seriesNumber;
	
	private Integer startSerialNo;
	
	private Integer endSerialNo;
	
	private Integer ticketBookNo;
	
	private String bundleNumber;
	
	private String stateTax;
	
	private String stateName;
	
	private Integer tbCount;
	
	private Integer totalValue;
}
