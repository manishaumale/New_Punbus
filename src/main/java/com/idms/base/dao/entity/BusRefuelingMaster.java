package com.idms.base.dao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data  
@Entity 
@Table(name = "mst_bus_refueling")
@EqualsAndHashCode(callSuper = true)
public class BusRefuelingMaster extends BaseEntity {
	
	
	
	private static final long serialVersionUID = 3585321237071824207L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "busRefuelingId")
    private Integer id;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dispensingId")
	private DispensingUnitMaster dispensingUnitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTankId")
	private FuelTankMaster fuelTankMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busId")
	private BusMaster busMaster;
	
	private Float issuedDiesel;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "routeMaster")
	private RouteMaster routeMaster;
	
	private Float extraDeadKms;
	
	private String reasonForExtraDead;
	
	private String remarks;
	
	private Integer scheduledKms;
	
	private Integer deadKms;
	
	private Integer plainKms;
	
	private Integer hillKms;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = FuelTakenOutSide.class,cascade = {CascadeType.ALL})
    @JoinColumn(name = "fuelTakenOutSideId", referencedColumnName = "fuelTakenId")
	private FuelTakenOutSide fuelTakenOutSide;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = AdBlueUsed.class,cascade = {CascadeType.ALL})
    @JoinColumn(name = "adBlueId", referencedColumnName = "addBlueId")
	private AdBlueUsed adBlueUsed;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = MobilOilUsed.class,cascade = {CascadeType.ALL})
    @JoinColumn(name = "MmbilOilUsedId", referencedColumnName = "mobilOilUsedId")
	private MobilOilUsed mobilOilUsed;
	
	@Transient
	private Double currentReading;
	
	private Integer totalActualKms;
	
	private Float kmplAsScheduledKilometer;
	
	private Float kmplAsPerGrossKms;
	
	private Float kmplAsperActualKms;
	
	private Float kmplAsPerVTSKms;
	
	private Float dieselFromOutside; 
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusReturnReason.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "reasonId")
	private BusReturnReason reason;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = DUReadingHistory.class, mappedBy = "refueling", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<DUReadingHistory> duReadings;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = DailyRoaster.class, mappedBy = "refueling", cascade = CascadeType.MERGE)
	@EqualsAndHashCode.Exclude
	private List<DailyRoaster> dailyRota;
	
	@Transient
	private String dpCode;
	
	private Float vtsKms;
	
	private Integer grossKms;
	
	private Integer tripId;
	
	private Boolean diesel_corrected;
	
	@Transient
	private Integer rotaId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Roaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "rotaId", nullable = true)
	private Roaster rota;
	
	private Boolean isDeleted;
	
	private Float currentPrice;
	
	

}