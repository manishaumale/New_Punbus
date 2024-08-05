package com.idms.base.dao.entity;

import java.time.LocalTime;
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
@Table(name = "driver_roaster_history")
@EqualsAndHashCode(callSuper = true)
public class DriverRotaHistory extends BaseEntity {
	
	private static final long serialVersionUID = -6242172502573145235L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "driverRoasterId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class)
    @JoinColumn(name = "driverId", nullable = false)
	private DriverMaster driver;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class)
    @JoinColumn(name = "tripId", nullable = false)
	private TripMaster trip;
	
	private String upDown;
	
	private Date returnDate;
	
	private LocalTime returnTime;
	
	private String tripStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverRotaHistory.class)
    @JoinColumn(name = "parentId", nullable = true)
	private DriverRotaHistory parentId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DailyRoaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "roasterId", nullable = false)
	private DailyRoaster roaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
    @JoinColumn(name = "depotId", nullable = false)
	private DepotMaster depot;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isDeleted;

}
