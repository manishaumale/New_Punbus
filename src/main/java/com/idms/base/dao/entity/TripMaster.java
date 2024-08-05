package com.idms.base.dao.entity;

import java.time.LocalTime;
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
import javax.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter   
@Entity 
@Table(name = "mst_trip")
@EqualsAndHashCode(callSuper = true)
public class TripMaster extends BaseEntity{
	
	/**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tripId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "rootId", nullable = true)
	private RouteMaster routeMaster;
	
//	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@JoinColumn(name = "routId")
//	private RouteMaster routeMaster;
	
    private Integer scheduledKms;
	
	private Integer deadKms;
	
	private LocalTime tripStartTime;
	
	private LocalTime tripEndTime;
	
	//private Integer nightHalt; Added in Trip Rotation
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class)
    @JoinColumn(name = "fromCity", nullable = true)
	private CityMaster fromCity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class)
    @JoinColumn(name = "toCity", nullable = true)
	private CityMaster toCity;
	
	private String upDown;
	
//	@OneToMany(fetch = FetchType.LAZY, targetEntity = StopagesMaster.class, mappedBy = "tripMaster", cascade = CascadeType.ALL)
//	@EqualsAndHashCode.Exclude
//	private List<StopagesMaster> stopagesMasterList;
//	
//	@OneToMany(fetch = FetchType.LAZY, targetEntity = TollTaxWrapper.class, mappedBy = "trip", cascade = CascadeType.ALL)
//	@EqualsAndHashCode.Exclude
//	private List<TollTaxWrapper> tollTaxWrapperList;
//	
//	@OneToMany(fetch = FetchType.LAZY, targetEntity = BusStandWrapper.class, mappedBy = "tripMasterObj", cascade = CascadeType.ALL)
//	@EqualsAndHashCode.Exclude
//	private List<BusStandWrapper> busStandWrapperList;
	
	private Boolean isDeleted;
	
	private String serviceId;
	
	private String tripCode;
	
	private Double totalOt;
	
	private Integer dutyCounts;
	
//	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TotalNightsMaster.class)
//	@JoinColumn(name = "totalNightsId", nullable = true)  Added in Trip Rotation
//	private TotalNightsMaster totalNightsMaster;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = TripDepartureAndDaysEntity.class, mappedBy = "tripMaster", cascade = CascadeType.ALL)
	private List<TripDepartureAndDaysEntity> tripDepartureDays;
	
	@Transient
	private String routeName;
	
	@Transient
	private String routeCode;
	
	@Transient
	private String fromState;
	
	@Transient
	private String fromNewCity;
	
	@Transient
	private String toNewCity;
	
	@Transient
	private String toState;
	
}