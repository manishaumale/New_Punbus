package com.idms.base.api.v1.model.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
//@ApiModel(description = "Tyre Model")
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTyreAssoChildWrapper extends BaseEntityDto {

	private String tyreTag;
	
	private TyrePositionDto tyrePosition;
	
	private TyreSizeDto tyreSize;
	
	private TyreMakerDto tyreMake;
	
	private TyreTypeDto tyreType;
	
	private String tyreNumber;
	
    private String expectedLife;
    
    private TyreConditionDto tyreCondition;
	
	private String tyreCost;
	
	private String busFittedFlag;
	
    private String invoiceNumber;
	
	private Date invoiceDate;
	
	private String sourceOfOrigin;
	
	private Date purchaseDate;

	private DepotMasterDto sourceOfOriginTyre;
}
