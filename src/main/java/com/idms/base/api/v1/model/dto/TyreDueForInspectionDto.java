package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Due For Inspection Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreDueForInspectionDto {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Due Date For Inspection")
	private String  dueDate;
	
	@ApiModelProperty(notes = "Expected Life")
	private String  expectedLife;
	
	
	@ApiModelProperty(notes = "Status")
	private String  status;
	
	

}

