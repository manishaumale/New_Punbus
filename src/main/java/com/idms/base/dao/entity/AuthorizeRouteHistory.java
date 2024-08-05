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
@Table(name = "authorize_route_history")
@EqualsAndHashCode(callSuper = true)
public class AuthorizeRouteHistory extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = AuthorizeRoute.class)
    @JoinColumn(name = "authorizeRouteId", nullable = false)
	private AuthorizeRoute authorizeRoute;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class)
    @JoinColumn(name = "route_Id", nullable = false)
	private RouteMaster route;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class,cascade = CascadeType.ALL)
	@JoinColumn(name = "transportId") 
	private TransportUnitMaster transport;
	 
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
    @JoinColumn(name = "depotId", nullable = true)
	private DepotMaster depot;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class)
    @JoinColumn(name = "busId", nullable = true)
	private BusMaster bus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class)
    @JoinColumn(name = "driverId", nullable = true)
	private DriverMaster driver;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class)
    @JoinColumn(name = "conductorId", nullable = true)
	private ConductorMaster conductor;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class)
    @JoinColumn(name = "tripId", nullable = true)
	private TripMaster trip;
	
	private String  reason;
}
