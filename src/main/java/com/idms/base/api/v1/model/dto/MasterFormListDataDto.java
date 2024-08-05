package com.idms.base.api.v1.model.dto;

import javax.mail.Transport;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "MasterFormListDataDto Model")
public class MasterFormListDataDto {
	
	private TransportDto transport;
	
	private DenominationDto denomination;
	
	private Integer ticketCount;
	
	private Integer ticketTotalAmount;

}
