package com.idms.base.dao.entity;

import java.util.Date;

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
@Table(name = "route_block")
@EqualsAndHashCode(callSuper = true)
public class RouteBlock extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "routeblockId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class)
    @JoinColumn(name = "routeId", nullable = false)
	private RouteMaster route;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
    @JoinColumn(name = "depotId", nullable = true)
	private DepotMaster depot;
	
	private Date fromDate;
	
	private Date toDate;
	
	private String detailedReason;
	
	
}
