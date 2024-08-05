

package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Not Sent For Retreading Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreNotSentForRetreadingReport {
	     
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Sent Date")
	private String  takenOffDate;
	
	@ApiModelProperty(notes = "Status")
	private String  status;
	
	@ApiModelProperty(notes = "Since Not Recieved")
	private String  sinceRecieved;
	
	@ApiModelProperty(notes = "Tyre Tag")
	private String  tyreTag;
	
	@ApiModelProperty(notes = "Docket Number")
	private String  docketNumber;
	
	@ApiModelProperty(notes = "Recieved Date")
	private String  recievedDate;
	

}

