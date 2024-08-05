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

@Getter
@Setter // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity
@Table(name = "trip_stopage")
public class StopagesMaster extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stopageId")
	private Integer id;

	private LocalTime departureTime;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StopageTypeMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "stopageTypeId", nullable = false)
	private StopageTypeMaster stopageTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TotalNightsMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "totalNightsId", nullable = true)
	private TotalNightsMaster days;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "toCityId", nullable = true)
	private CityMaster stpageName;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "tripId", nullable = true, referencedColumnName = "tripId")
	private TripMaster tripMaster;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StopageEntity.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "stopage", nullable = false)
	private StopageEntity stopage;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteRotation.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeRotationId", nullable = true,referencedColumnName = "routeRotationId")
	private RouteRotation routeRotation;

}
