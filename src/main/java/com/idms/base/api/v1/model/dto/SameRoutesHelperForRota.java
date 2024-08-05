package com.idms.base.api.v1.model.dto;

import java.time.LocalTime;

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DriverMaster;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class SameRoutesHelperForRota {
	
	private DriverMaster driverMaster;
	
	private ConductorMaster conductorMaster;
	
	private BusMaster busMaster;
	
	private LocalTime tripStartTime;
	
	private LocalTime tripEndTime;
	
	private Integer nightMasterId;
	
	private String scheduledKms;
	

}
