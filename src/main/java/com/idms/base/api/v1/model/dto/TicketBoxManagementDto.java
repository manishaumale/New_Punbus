package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter

public class TicketBoxManagementDto {
    
	private String transportUnitName;
	private List<TicketBoxNumberDto > ticketBoxNumber;
	private List<DenominationDto> denomination;
	
	private Integer id;
	private String transportUnitMaster;
	private String ticketBox;
	private Integer deno;
	private Long startingSerialNo;
	private Long endingSerialNo;
	private Long currentLastNo;
	private Long amount;
	private Long currentamount;
	private Integer transportId;
	private Integer rowId;
	private Integer totalTicketPunched;
	private String bookSeries;
	private String seriesNumber;
	private Integer ticketBoxManagementEntity;
	private Boolean isBookletChecked;
	
}
