package com.idms.base.dao.entity;

import java.util.Date;

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
@Table(name = "block_route_off")
@EqualsAndHashCode(callSuper = true)
public class BlockOrRouteOffEntity extends BaseEntity  {
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
	
	private Boolean routeOff;
	
	private Boolean blocked;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "driverId")
	private DriverMaster driverId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "conductorId")
	private ConductorMaster conductorId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "depotId", nullable = true)
	private DepotMaster depotId;
	
	private Date orderDate;
	
	private String orderNumber;
	
	private Boolean isBlocked;
	
	private String reason;
	
	
}
