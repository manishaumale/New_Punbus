package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Submit Etm Ticket Model")
public class SubmitEtmTicketBoxDto {

	private Integer id;
	
	private IssueEtmTicketBoxDto issueEtmTicketBoxDto;
	
    private List<SubmitConcessionTicketDto> concessionTicketDto;
    
    private List<IssueEtmTicketBoxEntityChildDto> childSubmitTicketBoxEntityList;
    
    private String conductorName;
    
	private String wayBillNo;
	
	private String ticketBoxNo;
	
	private String etmNo;
	
	private String routename;
	
	private String busRegistrationNo;
	
	private String busType;
	
	private String driverName;
	
	private String amountSum;
}
