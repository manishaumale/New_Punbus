package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.idms.base.dao.entity.PenaltyType;

import lombok.Data;

@Data
public class TyreAuthenticationDto {
	
	private Integer id;

	private BusMasterDto busMaster;
	
	private  TyreMasterDto tyre;

	private TyreConditionDto tyreCondition;
	
	private TyreMakerDto tyremake;

	private String tyreExpectedlife;

	private String takenOffmileage;

	private String remarks;

	private String masterStatus;

	private TyreSizeDto tyreSize;
	
	private Double tyreRecoveredCost;
	
	private String tyreInstallationDate;
	
	private Float kmsDoneBus;
	
	private Float kmsInCondition;
	
	private Float totalKmsDone;
	
	private TyrePositionDto tyrePosition;
	
	private String transportName;
	
	private String takenoffby;
	
	private Integer busId;
	
	private Integer reasonId;
	
	private String reasonName;
	
	private String reasonCode;
	
	private DriverMasterDto driver;
	
	private RouteMasterDto route;
	
	private ConductorMasterDto conductor;
	
	private PenaltyTypeDto penaltyType;
	
	private List<PenaltyTypeDto> penaltyTypeList;
}
