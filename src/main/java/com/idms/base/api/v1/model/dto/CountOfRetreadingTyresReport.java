package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Count Of Retreading Tyres Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountOfRetreadingTyresReport {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Total Tyres")
	private String  total;
	
	@ApiModelProperty(notes = "First Retreading")
	private String  firstRetreading;
	
	@ApiModelProperty(notes = "Second Retreading")
	private String  secondRetreading;
	
	@ApiModelProperty(notes = "Third Retreading")
	private String  thirdRetreading;
	
	@ApiModelProperty(notes = "To Date")
	private String  toDate;
	

}

