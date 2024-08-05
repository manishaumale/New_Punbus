package com.idms.base.dao.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "tank_inspection")
@EqualsAndHashCode(callSuper = true)
public class TankInspection extends BaseEntity {

	private static final long serialVersionUID = 3049867543551617449L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tankInspectionId")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
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

	private String remarks;

	private Date nextInspectionDate;

	private Double variation;

	private Boolean variationFlag;

	@Transient
	private String depotCode;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DipChartReadings.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "bookReadingId")
	private DipChartReadings bookReading;

}
