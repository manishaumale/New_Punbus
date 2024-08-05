package com.idms.base.api.v1.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Completion Of Expected Tyre Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompletionOfExpectedTyreReportDto {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	
	@ApiModelProperty(notes = "Expected Life")
	private String  expectedLife;
	
	@ApiModelProperty(notes = "Due Date For Taken")
	private String  dueDate;
	
	@ApiModelProperty(notes = "Status")
	private String  status;
	
	@ApiModelProperty(notes = "Transport Name")
	private String  transName;
	
	@ApiModelProperty(notes = "Bus Number")
	private String  busNo;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "KMs Achieved")
	private String  kmsAchieved;
	
	

}

