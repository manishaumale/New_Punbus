package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.TyreChangeAction;
import com.idms.base.dao.entity.TyreCondition;
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
@ApiModel(description = "Bus Tyre Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTyreAssociationDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Bus Tyre Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Tyre Model")
	private TyreMasterDto tyre;
	
	@ApiModelProperty(notes = "Bus Master Model")
	private BusMasterDto bus;
	
	private TyrePositionDto tyrePosition;
	
	private TyreConditionDto tyreCondition;
	
	private Date installDate;
	
	private Double kmsDone;
	
	private Boolean busFitted;
	
	private TyreChangeActionDto changeAction;
	
	private TakenOffReasonDto takenOffReason;
	
	private Integer oldId;
	
	private String remarks;
	
	private Integer currentMileage;
	
	private String sizeName;
	
	private String makerName;
    
	private String tyreType;
	
	private String tyreNumber;
	
	private String installationDate;
	
	private String tyreTag;
	
	private Double recoveredCost;
	
	private Integer kmsDoneOnThisBus;
	
	private Integer kmsInCondition;
	
	private Integer totalKmsDone;
	
	private String kmsDoneByCondition;
	
	private String totalKilometerDone;
	
	private String kilometerDoneOnBus;
	
	private String allKmsinCondition;
	
}
