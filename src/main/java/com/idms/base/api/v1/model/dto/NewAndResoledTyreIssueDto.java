package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "New And Resoled Tyre Issue")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewAndResoledTyreIssueDto {
	
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Bus Number")
	private String  busNo;
	
	@ApiModelProperty(notes = "Position")
	private String  position;
	
	@ApiModelProperty(notes = "make")
	private String  make;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "Tyre Size")
	private String  tyreSize;
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depot;
	
	private String conditionName;
	

}
