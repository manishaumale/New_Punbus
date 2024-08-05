package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Submit Concession Ticket Model")
public class SubmitConcessionTicketDto {

	private Integer id;
	
	private Integer totalTickets;
	
	private Integer netAmount;
	
	private ConcessionTicketDto concessionTicketDto;
}
