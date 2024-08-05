package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.idms.base.dao.entity.BookReadingClosingMaster;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DipChartReadingsDto {
	
	private Integer id;

	private FuelTankMasterDto fuelTankMasterOne;
	
	private FuelTankMasterDto fuelTankMasterSec;
	
	private ReadingMasterDto readingMasterOne;
	
	private ReadingMasterDto readingMasterSec;
	
	private Double fuelVolumeTankOne;
	
	private Double fuelVolumeTankSecond;
	
	private Double totalOpeningFuelVolume;
	
	private DispensingUnitMasterDto dispensingMasterOne;
	
	private DispensingUnitMasterDto dispensingMasterSec;
	
	private Double dispensingUnitReadingOne;
	
	private Double dispensingUnitReadingSec;
	
	private FuelTankMasterDto closeFuelTankMasterDtoOne;
	
	private FuelTankMasterDto closeFuelTankMasterSec;
	
	private ReadingMasterDto closeReadingMasterOne;
	
	private ReadingMasterDto closeReadingMasterSec;
	
	private Double closeFuelVolumeTankOne;
	
	private Double closeFuelVolumeTankSecond;
	
	private Double closeTotalOpeningFuelVolume;
	
	private DispensingUnitMasterDto closedispensingMasterOne;
	
	private DispensingUnitMasterDto closedispensingMasterSec;
	
	private Double closeDispensingUnitReadingOne;
	
	private Double closeDispensingUnitReadingSec;
	
	private Double dispenseFuelMorning;
	
	private Double dispenseFuelEvening;
	
	private Double currentFuelAllTank;
	
	private Double variationMorning;
	
	private Double variationEvening;
	
    private Boolean variationMorningFlag;
	
	private Boolean variationEveningFlag;
	
    private Double fuelRateMorning;
	
	private Double fuelRateEvening;
	
	private Double excessShort;
	
	private Date morningDate;
	
	private Date evengDateTime;
	
	private Boolean incomplete;
	
	private String user;
	
	private String depotCode;
	
	private Double fuelRateMorningTank2;
	
	private Double fuelRateEveningTank2;

	private BookReadingClosingDto closingObj;

}
