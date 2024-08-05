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
@Table(name = "bus_roaster_history")
@EqualsAndHashCode(callSuper = true)
public class BusRotaHistory extends BaseEntity {
	
	private static final long serialVersionUID = 6032380351847372056L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "busRoasterId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class)
    @JoinColumn(name = "busId", nullable = false)
	private BusMaster bus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class)
    @JoinColumn(name = "tripId", nullable = false)
	private TripMaster trip;
	
	private String upDown;
	
	private Date returnDate;
	
	private LocalTime returnTime;
	
	private String tripStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusRotaHistory.class)
    @JoinColumn(name = "parentId", nullable = true)
	private BusRotaHistory parentId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DailyRoaster.class, cascade = CascadeType.MERGE )
    @JoinColumn(name = "roasterId", nullable = false)
	private DailyRoaster roaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
    @JoinColumn(name = "depotId", nullable = false)
	private DepotMaster depot;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isDeleted;

}
