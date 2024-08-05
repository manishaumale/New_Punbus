package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Expected TyreLife Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpectedTyreLifeReport {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	private String tyreTag;
	
	private String tyreNumber;
	private String expectedLife;
	private Integer tyreConditionId;
	
	@ApiModelProperty(notes = "New Tyre Expected Life")
	private String  newTyreExpectedLife;
	
	@ApiModelProperty(notes = "First Resole Tyre Life")
	private String  firstResoleTyreLife;
	
	@ApiModelProperty(notes = "Second Resole Tyre Life")
	private String  secondResoleTyreLife;
	
	@ApiModelProperty(notes = "Third Resole Tyre Life")
	private String  thirdResoleTyreLife;
	

}