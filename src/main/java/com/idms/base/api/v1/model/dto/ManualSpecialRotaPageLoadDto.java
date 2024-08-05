package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.TripMaster;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Manual Special Rota Model")
public class ManualSpecialRotaPageLoadDto {
	
	private List<TripMaster> tripList;
	
	private List<DriverMaster> driverList;
	
	private List<ConductorMaster> conductorList;
	
	private List<BusMaster> busList;
	
	
	
	
	
}

