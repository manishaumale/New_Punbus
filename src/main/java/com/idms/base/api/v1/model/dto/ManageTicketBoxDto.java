package com.idms.base.api.v1.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ManageTicketBoxDto {
	
	private Integer id;
	
	private Integer transportUnitMaster;
	
	private Integer ticketBoxNumber;
	
	private Integer denomination;
	
	private Long startingSerialNo;
	
	private Long endingSerialNo;
	
	private Long amount;
	
	//private MasterStockDto masterStock;
	private Integer masterStock;
	
	private Integer centralTicket;
}
