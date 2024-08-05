package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Issue Etm Ticket Child Model")
public class IssueEtmTicketBoxEntityChildDto {
	
    private Integer id;
	
	private Long startingSerialNo;
	
	private Long endingSerialNo;
	
	private Long amount;
	
	private Long currentLastNo;
	
	private Integer denomination;
	
	private String transportUnitMasterName;
	
	private Long currentamount;
	
	private Integer transportId;
	
	private String seriesNumber;
	
	private Integer ticketBoxManagementEntity;
	
	private Boolean isBookletChecked;
}
