package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Concession Ticke Model")
public class ConcessionTicketDto {

	private Integer id;
	
	private String passType;
	
	private String discount;
	
	private Integer totalTickets;
	
	private Integer netAmount;
}
