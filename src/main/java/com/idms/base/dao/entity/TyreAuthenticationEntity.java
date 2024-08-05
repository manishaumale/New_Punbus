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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "tyre_authentication")
@EqualsAndHashCode(callSuper = true)
public class TyreAuthenticationEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="tyre_auth_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busId")
	private BusMaster busMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreId")
	private TyreMaster tyre;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreCondition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreConditionId", nullable=true)
	private TyreCondition tyreCondition;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStatus.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "statusId",nullable=true)
	private MasterStatus masterStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreSize.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreSizeId")
	private TyreSize tyreSize;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaker.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreMake")
	private TyreMaker tyreMake;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyrePosition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyrePositionId")
	private TyrePosition tyrePosition;
	
	@Column(name="tyre_expectedlife")
	private String tyreExpectedLife;
	
	@Column(name="taken_offmileage")
	private Integer takenOffmileage;
	
	private String remarks;
	
	private Double tyreRecoveredCost;
	
	private Date tyreInstallationDate;
	
	private Float kmsDoneBus;
	
	private Float kmsInCondition;
	
	private Float totalKmsDone;
	
	private String comment;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "driverId")
	private DriverMaster driver;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "route_Id")
	private RouteMaster route;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "conductorId", nullable = true)
	private ConductorMaster conductor;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PenaltyType.class, cascade = CascadeType.MERGE )
    @JoinColumn(name = "PenaltyTypeId")
	private PenaltyType penaltyType;
	
	private Date offRouteDate;
	
	private Double penaltyAmount;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TakenOffReason.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "reasonId")
	private TakenOffReason takenOffReason;
	
}
