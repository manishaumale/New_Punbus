package com.idms.base.api.v1.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "New Tyre Issued For Rear Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewTyreIssuedForRearReportDto {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "Tyre Position")
	private String  tyrePosition;
	
	
	@ApiModelProperty(notes = "Installation Date")
	private String  dateOfInstallation;
	
	@ApiModelProperty(notes = "Reason")
	private String  reason;

	
	

}
