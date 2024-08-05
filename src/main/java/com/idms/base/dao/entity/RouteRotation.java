package com.idms.base.dao.entity;

import java.time.LocalTime;
import java.util.ArrayList;
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

import lombok.Getter;
import lombok.Setter;

@Getter @Setter   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "route_rotation")
public class RouteRotation extends BaseEntity{

	/**
	 * @author Piyush Sahay
	 */
	private static final long serialVersionUID = 5922551058151939986L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "routeRotationId")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "fromStateId", nullable = false)
	private StateMaster fromState;

	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "fromCityId", nullable = false)
	private CityMaster fromCity;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "toStateId", nullable = false)
	private StateMaster toState;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "toCityId", nullable = false)
	private CityMaster toCity;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TotalNightsMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "tripDayId", nullable = true)
	private TotalNightsMaster tripDay;
	
	@Column
	private LocalTime tripStartTime;
	
	@Column
	private LocalTime tripEndTime;
	
	@Column
	private Integer nightHalt;
	
	@Column
	private Integer tripScheduleKm;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PermitDetailsMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "permitId", nullable = true)
	private PermitDetailsMaster permit;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeId", nullable = true, referencedColumnName = "id")
	private RouteMaster routeMaster;
	
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = StopagesMaster.class, mappedBy = "routeRotation", cascade = CascadeType.ALL)
	private List<StopagesMaster> stopagesMasterList= new ArrayList<StopagesMaster>();
	
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = TollTaxWrapper.class, mappedBy = "routeRotation", cascade = CascadeType.ALL)
	private List<TollTaxWrapper> tollTaxWrapperList = new ArrayList<TollTaxWrapper>();
	
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = BusStandWrapper.class, mappedBy = "routeRotation", cascade = CascadeType.ALL)
	private List<BusStandWrapper> busStandWrapperList = new ArrayList<BusStandWrapper>();
	
}
