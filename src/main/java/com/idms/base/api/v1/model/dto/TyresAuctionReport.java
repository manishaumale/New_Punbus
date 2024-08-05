package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyres Auction Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyresAuctionReport {
	
	@ApiModelProperty(notes = "Purchase Date")
	private String  purchaseDate;
	
	@ApiModelProperty(notes = "Tyre No")
	private String  tyreNumber;
	
	@ApiModelProperty(notes = "Tyre Type")
	private String  tyreType;
	
	@ApiModelProperty(notes = "Make")
	private String  make;
	
	@ApiModelProperty(notes = "Fitment Date")
	private String  fitmentDate;
	
	@ApiModelProperty(notes = "Taken Off Date")
	private String  takenOffDate;
	
	@ApiModelProperty(notes = "KMS Done")
	private String  kmsDone;
	
	@ApiModelProperty(notes = "First Resole KMS")
	private String  firstResoleKms;
	
	@ApiModelProperty(notes = "Second Resole KMS")
	private String  secondResoleKms;
	
	@ApiModelProperty(notes = "Third Resole KMS")
	private String  thirdResoleKms;
	
	@ApiModelProperty(notes = "Hill KMS")
	private String  hillKms;
	
	@ApiModelProperty(notes = "Plain KMS")
	private String  plainKms;
	
	@ApiModelProperty(notes = "Condemtion Date")
	private String  condemtionDate;
	
	

}
