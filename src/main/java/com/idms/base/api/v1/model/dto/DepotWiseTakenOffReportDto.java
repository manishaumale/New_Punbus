package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Taken Off Report")
public class DepotWiseTakenOffReportDto {
	
	
	@ApiModelProperty(notes = "Tyre Number")
	private String  tyreNo;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "Tyre Status")
	private String  tyreStatus;
	
	@ApiModelProperty(notes = "date")
	private String  date;
	
	@ApiModelProperty(notes = "Reason")
	private String  reason;
	
	@ApiModelProperty(notes = "Tyre Name")
	private String  tyreName;

	@ApiModelProperty(notes = "tyre Size")
	private String  tyreSize;
	
}

