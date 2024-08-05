package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TicketBoxInfoDto {
	
private Integer id;
	
	private String transportUnitMaster;
	
	private String ticketBoxNumber;
	
	private Integer denomination;
	
	private Long startingSerialNo;
	
	private Long endingSerialNo;
	
	private Long amount;
	
	
	
	
	

}
