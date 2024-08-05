package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Submit Etm Ticket View Dto")
public class SubmitEtmTicketViewDto {

	private Integer submitid;
	
	private String conductorName;
	
	private String wayBillNo;
	
	private String ticketBoxNo;
	
	private String etmNo;
	
	private String amountSum;
	
	private Boolean manualEntry;
	
	private Long manualTicketAmount;
	
	private String routeName;
	
}

