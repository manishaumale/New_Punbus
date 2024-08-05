package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Daily Roaster Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoasterDto  {
	
	private Integer id;
	
	private String roastedCode;
	
	private Date generationDate;
	
	private Date rotaDate;
	
	private DepotMasterDto depot;
	
	List<DailyRoasterDto> dailyRoasterList;
	
	private TransportDto transport;
	
	private Boolean isNormalRota; 
	
	private Boolean isSpecialRota;
	
	private Boolean isEdit;
	
	private Integer scheduledKms;
	
	private Integer deadKms;
	
	private Integer fleetStrength;
	
	private String approvalStatus;

}
