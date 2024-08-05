package com.idms.base.dao.entity;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data  
@Entity 
@Table(name = "du_reading_history")
@EqualsAndHashCode(callSuper = true)
public class DUReadingHistory extends BaseEntity {
	
	private static final long serialVersionUID = 107549827916960627L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "duHistoryId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dispensingUnitId")
	private DispensingUnitMaster dispensingUnit;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTankId")
	private FuelTankMaster fuelTank;
	
	private Double duStartReading;
	
	private Double duEndReading;
	
	private Float issuedDiesel;
	
	private Float currentDiesel;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusRefuelingMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "refuelingId")
	private BusRefuelingMaster refueling; 
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RecieveDieselSupplyMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dieselSupplyId")
	private RecieveDieselSupplyMaster dieselSupply;
	
	/*@ManyToOne(fetch = FetchType.LAZY, targetEntity = DUCalbiration.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "calibrationId")
	private DUCalbiration duCalibration;*/
	
	/*@ManyToOne(fetch = FetchType.LAZY, targetEntity = TankInspection.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "inspectionId")
	private TankInspection tankInspection;*/

}
