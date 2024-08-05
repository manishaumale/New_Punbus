package com.idms.base.dao.entity;

import java.time.LocalTime;

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

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
@Entity 
@Table(name = "trip_departure_days")
public class TripDepartureAndDaysEntity extends BaseEntity{

	/**
	 * @author 1000956
	 */
	private static final long serialVersionUID = 5122386340305229030L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "tripMasterId", nullable = true)
	private TripMaster tripMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteRotation.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "rotationId", nullable = true)
	RouteRotation routeRotation;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StopagesMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "stopageMasterId", nullable = true)
	StopagesMaster stopageMaster;
	
	private LocalTime departureTime;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TotalNightsMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "totalNightsId", nullable = true)
	private TotalNightsMaster days;
	
}
