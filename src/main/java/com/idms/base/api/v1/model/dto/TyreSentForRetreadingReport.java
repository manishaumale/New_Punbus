package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Sent For Retreading Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreSentForRetreadingReport {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Taken Off Date")
	private String  takenOffDate;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "Sent Date")
	private String  sentDate;	
	

}

