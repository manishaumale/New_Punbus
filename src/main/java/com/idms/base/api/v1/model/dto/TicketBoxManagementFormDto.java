package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "TicketBoxManagementFormDto Model")
public class TicketBoxManagementFormDto {
	
	private List<TransportDto> tpList;
	
	private List<TicketBoxMasterDto> ticketBoxList;
	
	private List<DenominationDto> denominationList;

}
