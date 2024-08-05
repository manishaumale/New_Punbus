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
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_route")
public class RouteMaster extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	private String routeId;
	
	@Column(length=10, unique = true)
	private String routeName;
	
	private String routeCode;
	
	//private Integer totalOt; Added in TripMaster
	
//	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TotalNightsMaster.class, cascade = CascadeType.ALL)
//	@JoinColumn(name = "totalNightsId", nullable = false)
//	private TotalNightsMaster totalNightsMaster;
	
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
	
	private Integer scheduledKms;
	
	private Integer deadKms;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteComplexityMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeComlexityId", nullable = false)
	private RouteComplexityMaster routeComplexityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteCategoryMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeCategoryId", nullable = false)
	private RouteCategoryMaster routeCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteTypeMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeTypeId", nullable = false)
	private RouteTypeMaster routeTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyperMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "busTypeId")
	private BusTyperMaster busTyperMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "depotId")
	private DepotMaster depotMaster;
	
	//private Integer dutyCounts; Added in TripMaster
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = StateWiseRouteKms.class, mappedBy = "routeMaster", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<StateWiseRouteKms> stateWiseRouteKms;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = RoutePermitMaster.class, mappedBy = "route", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<RoutePermitMaster> routePermitMasterList;
	
	private Boolean isDeleted;
	
	@Column(name = "blocked", columnDefinition = "boolean default false")
	private Boolean blocked=false;
	
	
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class,cascade = CascadeType.ALL)
	@JoinColumn(name = "transportId") 
	private TransportUnitMaster transport;
    
    @OneToMany(fetch = FetchType.LAZY, targetEntity = TripMaster.class, mappedBy = "routeMaster", cascade = CascadeType.ALL)
   	private List<TripMaster> tripMaster;
	 
    @OneToMany(fetch = FetchType.LAZY, targetEntity = RouteRotation.class, mappedBy = "routeMaster", cascade = CascadeType.ALL)
	private List<RouteRotation> routeRotation;
    
    @OneToMany(fetch = FetchType.LAZY, targetEntity = TripRotationEntity.class, mappedBy = "routeMaster", cascade = CascadeType.ALL)
	private List<TripRotationEntity> tripRotation;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TripType.class,cascade = CascadeType.ALL)
   	@JoinColumn(name = "tripTypeId") 
   	private TripType tripType;
	
}
