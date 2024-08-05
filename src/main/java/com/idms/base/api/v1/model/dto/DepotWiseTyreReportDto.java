package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Depot Wise Tyre Report Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepotWiseTyreReportDto {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Bus Unit Type Name")
	private String  busType;
	
	@ApiModelProperty(notes = "Bus No")
	private String  busNumber;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Make Name")
	private String  makae;
	
	@ApiModelProperty(notes = "Tyre Size")
	private String  tyreSize;
	
	private String tyreCondition;
	
	private String count;
	
	private String tyreStatus;
	
	
	
	

}
