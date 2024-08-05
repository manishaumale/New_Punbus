package com.idms.base.api.v1.model.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Tyre Association History Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTyreAssociationHistoryDto {
	
	private Integer id;
	
	private BusMasterDto bus;
	
	private TyreMasterDto tyre;
	
	private Date installDate;
	
	private TyrePositionDto tyrePosition;
	
	private TyreConditionDto tyreCondition;
	
	private Double kmsDone;
	
	private Date removalDate;
	
	private Boolean busFitted;
	
	private TakenOffReasonDto reason;
	
	private String remarks;
	
	private Integer hillKms;
	
	private Integer plainKms;

	// added by ManishaUmale for Old Tyre unfitted  
		
	private Integer kmsTakenOff;
		
	private Integer kmsInstalled;
		
	private Integer totalMileageTakenOff;

	private Integer hillMileageDone;

	private Integer plainMileageDone;
		
}
