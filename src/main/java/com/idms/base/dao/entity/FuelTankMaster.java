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

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_fuel_tank")
@EqualsAndHashCode(callSuper = true)
public class FuelTankMaster extends BaseEntity {

	private static final long serialVersionUID = -7587050397685706428L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuelTankId")
    private Integer id;
	
	private String tankName;
	
	private String tankCode;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TankCapacityMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "capacityId")
	private TankCapacityMaster capacity;
	
	private Float diameter;
	
	private Float length;
	
	private Float radius;
	
	private Float deadBoards;
	
	private Date installationDate;
	
	private Date tankValidity;
	
	private Float reorderLevel;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DipReadingMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dipId")
	private DipReadingMaster dip;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "explosiveLicenceId")
	private Document explosiveLicence;
	
	private Date explosiveLicenceValidity;
	
	private Double currentValue;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@Transient
	private String depotCode;
	
	private Date cleaningDate;
	
	private String tankUniqueId;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ExplosiveLicenseDetails.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "explosiveId")
	private ExplosiveLicenseDetails explosiveDetails;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankCleaningHistory.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "cleanignId")
	private FuelTankCleaningHistory fueltankcleaninghistory;
	
	private Boolean isDeleted;
	
	private Float currentPrice;
}
