package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.idms.base.dao.entity.TyrePosition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Old Tyre Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OldTyreMasterDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Tyre Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Tyre No")
	private String tyreNumber;
	
	@ApiModelProperty(notes = "Transport Model")
	private TransportDto transportUnit;
	
	@ApiModelProperty(notes = "Tyre Type Model")
	private TyreTypeDto tyreType;
	
	@ApiModelProperty(notes = "Tyre Size Model")
	private TyreSizeDto tyreSize;
	
	@ApiModelProperty(notes = "Tyre Condition Model")
	private TyreConditionDto tyreCondition;
	
	@ApiModelProperty(notes = "Tyre Maker Model")
	private TyreMakerDto tyreMake;
	
	private DepotMasterDto depot;
	
	private String expectedLife;
	
	private double tyreCost;
		
	private List<BusTyreAssociationHistoryDto> busHistory;
	
	private String sourceOfOriginTyre;
	
    private String invoiceNumber;
	
	private String invoiceDate;
	
	private String tyreTag;

	// added by ManishaUmale for Old Tyre unfitted  
	private boolean oldTyre;
	
	private Integer kmsRunTillDate;
	
	private String tyrePurchaseDate;
	
	private String reasonName;
	    
	private String reasonCode;
	
	private String busRegNumber;
	
	private String 	conditionName;
	
	private String takenOffDate;
	
	private String installDate;
	
	private String kmsTakenOff;
	
	private String kmsInstalled;
	
	private String totalMileageTakenOff;
	
	private TyrePositionDto tyrePosition;
}
