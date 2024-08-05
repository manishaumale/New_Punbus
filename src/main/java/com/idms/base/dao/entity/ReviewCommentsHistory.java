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
@Table(name = "review_comments_history")
@EqualsAndHashCode(callSuper = true)
public class ReviewCommentsHistory extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
	
	private String comments;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeMasterId", nullable = true)
	private RouteMaster routeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "driverMasterId", nullable = true)
	private DriverMaster driverMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "conductorMasterId", nullable = true)
	private ConductorMaster conductorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "busId", nullable = true)
	private BusMaster busMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "depotId", nullable = true)
	private DepotMaster depotId;
	
	private Date nextReviewDate;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteCategoryMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "categoryId", nullable = true)
	private RouteCategoryMaster categoryId;
}
