package com.idms.base.dao.entity;

import java.util.Date;
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
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "daily_roaster_auto")
@EqualsAndHashCode(callSuper = true)
public class DailyRoasterAuto extends BaseEntity {
	
	private static final long serialVersionUID = -2291250606119805486L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roasterAutoId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class)
    @JoinColumn(name = "busId", nullable = true)
	private BusMaster bus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class)
    @JoinColumn(name = "driverId", nullable = true)
	private DriverMaster driver;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class)
    @JoinColumn(name = "conductorId", nullable = true)
	private ConductorMaster conductor;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class)
    @JoinColumn(name = "routeId", nullable = false)
	private RouteMaster route;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class)
    @JoinColumn(name = "tripId", nullable = false)
	private TripMaster trip;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteTypeMaster.class)
    @JoinColumn(name = "routeTypeId", nullable = false)
	private RouteTypeMaster routeType;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = BusRotaHistory.class, mappedBy = "roaster", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<BusRotaHistory> busRota;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = DriverRotaHistory.class, mappedBy = "roaster", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<DriverRotaHistory> driverRota;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ConductorRotaHistory.class, mappedBy = "roaster", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<ConductorRotaHistory> conductorRota;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RoasterAuto.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "rotaAutoId", nullable = false)
	private RoasterAuto rotaAuto;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean tripStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusRefuelingMaster.class)
    @JoinColumn(name = "refuelingId", nullable = true)
	private BusRefuelingMaster refueling;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isCancelled;
	
	private String cancelReason;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isDeleted;
	
	private String suggestion;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripRotationEntity.class)
    @JoinColumn(name = "trip_rotation", nullable = true)
	private TripRotationEntity tripRotation;
	
	private Date rotationAvailabilityDate;
	
	private String rotaTypeFlag;
	

}
