package com.idms.base.dao.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name="dip_chart_readings")
@EqualsAndHashCode(callSuper = true)
public class DipChartReadings extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer dip_id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTankId1")
	private FuelTankMaster fuelTankMasterOne;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTankId2")
	private FuelTankMaster fuelTankMasterSec;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ReadingMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "readingIdTank1")
	private ReadingMaster readingMasterOne;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ReadingMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "readingIdTank2")
	private ReadingMaster readingMasterSec;
	
	private Double fuelVolumeTankOne;
	
	private Double fuelVolumeTankSecond;
	
	private Double totalOpeningFuelVolume;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "duReadingId1")
	private DispensingUnitMaster dipReadingMasterOne;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "duReadingId2")
	private DispensingUnitMaster dipReadingMasterSec;
	
	private Double dipReadingOne;
	
	private Double dipReadingSec;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "closeFuelTankId1")
	private FuelTankMaster closeFuelTankMasterOne;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "closeFuelTankId2")
	private FuelTankMaster closeFuelTankMasterSec;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ReadingMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "closeReadingIdTank1")
	private ReadingMaster closeReadingMasterOne;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ReadingMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "closeReadingIdTank2")
	private ReadingMaster closeReadingMasterSec;
	
	private Double closeFuelVolumeTankOne;
	
	private Double closeFuelVolumeTankSecond;
	
	private Double closeTotalOpeningFuelVolume;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "closeDUReadingId1")
	private DispensingUnitMaster closeDipReadingMasterOne;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "closeDUReadingId2")
	private DispensingUnitMaster closeDipReadingMasterSec;
	
	private Double closeDipReadingOne;
	
	private Double closeDipReadingSec;
	
	private Double dispenseFuelMorning;
	
	private Double dispenseFuelEvening;
	
	private Double currentFuelAllTank;
	
	private Double variationMorning;
	
	private Double variationEvening;
	
    private Boolean variationMorningFlag;
	
	private Boolean variationEveningFlag;
	
    private Double fuelRateMorning;
	
	private Double fuelRateEvening;
	
	private Double fuelRateMorningTank2;
		
	private Double fuelRateEveningTank2;
	
	private Double excessShort;
	
	private Date evengDateTime;
	
	private Boolean incomplete;
	
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depotId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BookReadingClosingMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "closingId")
	private BookReadingClosingMaster closingObj;
	
	
}
