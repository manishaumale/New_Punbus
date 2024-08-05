package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Taken Off Not Refitted")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreTakenOffNotRefittedDto {
	
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Tyre tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "depot")
	private String  depot;
	
	@ApiModelProperty(notes = "Taken Off Date")
	private String  takenOffDate;
	
	@ApiModelProperty(notes = "Position")
	private String  position;
	
	@ApiModelProperty(notes = "Reason")
	private String  reason;
	

}
