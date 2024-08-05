package com.idms.base.api.v1.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Current Tyre Status Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentTyreStatusReportDto {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre Number")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Tyre Position")
	private String  tyrePosition;
	
	@ApiModelProperty(notes = "Taken Off For Retreading")
	private String  takenOff;
	
	@ApiModelProperty(notes = "Condemnation")
	private String  condemnation;
	
	@ApiModelProperty(notes = "Refitting ")
	private String  refitting ;
	
	@ApiModelProperty(notes = "At plant")
	private String  atplant;
	
	@ApiModelProperty(notes = "Auction Date")
	private String  auctionDate;
	
	

}

