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
@Table(name = "workflow_history")
@EqualsAndHashCode(callSuper = true)
public class WorkFlowHistory extends BaseEntity {
	
	private static final long serialVersionUID = 5796809069926845650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workFlowId")
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
    @JoinColumn(name = "routeId", nullable = true)
	private RouteMaster route;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class)
    @JoinColumn(name = "tripId", nullable = true)
	private TripMaster trip;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStatus.class)
    @JoinColumn(name = "statusId", nullable = true)
	private MasterStatus masterStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Roaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "rotaId", nullable = true)
	private Roaster rota;
	
	private String remarks;
	
	private String overrideReason;
	
}
