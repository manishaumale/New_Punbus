package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Document sModel")
public class RefuelingViewDto {
	
	private String returnReason;
	
	private String busNumber;
	
	private String duName;
	
	private String fuelTankName;
	
	private Float dieselIssued;
	
	private Float dieselFromOutside;
	
	private String routeName;
	
	private String busType;
	
	private String driverName;
	
	private Integer scheduledKms;
	
	private Integer deadKms;
	
	private Integer plainKms;
	
	private Integer hillKms;
	
	private Float vtsKms;
	
	private String extraDeadKms;
	
	private Integer totalActualKms;
	
	private Float kmplAsSchKms;
	
	private Float kmplAsGrossKms;
	
	private Float kmplAsActualKms;
	
	private Float kmplAsVtsKms;
	
	private String extraDeadReason;
	
	private String remarks;
	
	private Integer grossKms;
	
	private FuelTakenOutSideDto fuelFromOutside;
	
	private MobilOilUsedDto mobileOilUsed;
	
	private AdBlueUsedDto adBluUsed;
	
	private List<DUReadingHistoryDto> readings;
	
}
