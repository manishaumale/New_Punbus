
package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Condemn Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreCondemnReport {
	
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "Tyre Status")
	private String  tyreStatus;
	
	@ApiModelProperty(notes = "Date")
	private String  date;
	
	@ApiModelProperty(notes = "Reason")
	private String  reason;
	

}

