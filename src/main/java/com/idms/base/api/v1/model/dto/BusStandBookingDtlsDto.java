package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "BusStandBookingDtlsDto Model")
public class BusStandBookingDtlsDto {
	
	private Integer id;
	
	private EtmTBAssignmentDto assignment;
	
	private Integer passengerCount;
	
	private Double addaBookingAmt;
	
	private Double advBookingAmtByEBTM;
	
	private Double totalAmt;

}
