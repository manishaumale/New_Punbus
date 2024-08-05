package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "EtmTBTripsAssociationDto Model")
public class EtmTBTripsAssociationDto {
	
	private Integer id;
	
	private ETMMasterDto etm;
	
	private TicketBoxMasterDto ticketBox;
	
	private TripMasterDto trip;
	
	private EtmTBAssignmentDto assignment;

}
