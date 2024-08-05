package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TicketManagementBoxParentDto {

	 private Integer id;
	 
	 private String transportUnitMaster;
	 
	 private TicketBoxMasterDto ticketBox;
	 
	 private Long totalValue;
	 
	 private List<TicketBoxManagementDto> ticketBoxManagementList;
	 
	 private String amountSum;
}
