package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreMasterDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Tyre Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Tyre No")
	private String tyreNumber;
	
	@ApiModelProperty(notes = "Transport Model")
	private TransportDto transportUnit;
	
	@ApiModelProperty(notes = "Bus Type Model")
	private BusTypeDto busType;
	
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
	
	
	
	private BusTyreAssociationDto bus;
	
	private List<BusTyreAssociationHistoryDto> busHistory;
	
	private double kmsDone;
	
	private boolean available;
	
	private TakenOffReasonDto takenOffReason;
	
	private boolean busFitted;
	
	
	private String toDate;
	
	private String fromDate;
	
	
	private Float oldMileage;
	
	private String useTyreID;
	
	private String sourceOfOrigin;
	
	
	
	private String currentMileage;
	
	private Integer hillKms;
	
	private Integer plainKms;
	
	public TyreMasterDto(  Float oldMileage) {
		this.oldMileage = oldMileage;
		
	}
	
	public TyreMasterDto(Integer id, String tyreNumber) {
		this.id = id;
		this.tyreNumber = tyreNumber;
	}
	
    private String invoiceNumber;
	
	private Date invoiceDate;
	
	private String tyreTag;

	// added by ManishaUmale for Old Tyre unfitted  
	private boolean oldTyre;
	
	private Integer kmsRunTillDate;
	
	private Date tyrePurchaseDate;
	
	private String busRegistrationNo;
	
	private String positionName;
	
	private String invoiceDateView;
	
	private String tyrePurchaseDateView;

	// added by sreegouri for tyreConditionBeforeCondemn,totalRecoveredCost
	private String tyreConditionBeforeCondemn;
	private Double totalRecoveredCost;
	


	
	private String conditionName;
	
	private Double recoveredCost;
	
	private Integer kmsDoneOnThisBus;
	
	private Integer totalKmsDone;
	
	private String kmsDoneByCondition;
	
	private String totalKilometerDone;
	
	private String kilometerDoneOnBus;
	
	private String allKmsinCondition;
	
	private DepotMasterDto sourceOfOriginTyre;
	

}
