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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity
@Table(name = "route_category_history")
public class RouteCategoryHistory extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteCategoryMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeOrDriverOrConductOrMasterId", nullable = true)
	private RouteCategoryMaster routeCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeMasterId", nullable = true)
	private RouteMaster routeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "driverMasterId", nullable = true)
	private DriverMaster driverMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "conductorMasterId", nullable = true)
	private ConductorMaster conductorMaster;
	
}
