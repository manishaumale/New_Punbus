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
@Table(name = "etm_tb_trip_association")
@EqualsAndHashCode(callSuper = true)
public class EtmTBTripsAssociation extends BaseEntity {
	
	private static final long serialVersionUID = 7877180687436053925L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "associationId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ETMMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "etmId")
	private ETMMaster etm;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "ticketBoxId")
	private TicketBoxMaster ticketBox;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tripId")
	private TripMaster trip;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EtmTBAssignment.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "assignmentId")
	private EtmTBAssignment assignment;
	
//	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DailyRoaster.class, cascade = CascadeType.MERGE)
//	@JoinColumn(name = "dailyRoasterId")
//	private DailyRoaster dailyRoaster;
	
}
