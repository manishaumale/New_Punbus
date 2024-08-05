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
@Table(name = "trip_rotation")
public class TripRotationEntity extends BaseEntity{

	/**
	 * @author 1000956
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tripRotationId")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeId", nullable = true, referencedColumnName = "id")
	private RouteMaster routeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "tripMasterId", nullable = true)
	private TripMaster tripMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteRotation.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "rotationId", nullable = true)
	RouteRotation routeRotation;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PermitDetailsMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "permitId", nullable = true)
	private PermitDetailsMaster permitMaster;
	
	@Column
	private LocalTime startTime;
	
	@Column
	private LocalTime endTime;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TotalNightsMaster.class)
	@JoinColumn(name = "totalNightsId", nullable = true)
	private TotalNightsMaster days;
	
	@Column
	private Integer nightHalt;
}
