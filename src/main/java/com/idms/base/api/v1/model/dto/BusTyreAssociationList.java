package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.TransportUnitMaster;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "BusTyreAssociationList Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTyreAssociationList {
	
	private Integer busId;
	
	private String busNumber;
	
	private String busTypeName;
	
	private Integer reqTyreCount;
	
	private Integer curTyreCount;
	
	private TransportUnitMaster transportUnit;
	
	private Integer currentCountWithSpare;
	

}
