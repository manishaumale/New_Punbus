package com.idms.base.dao.entity;

import java.time.LocalDateTime;
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


@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_bus_unavailable")
@EqualsAndHashCode(callSuper = true)
public class BusUnavailabilityMaster extends BaseEntity {

	private static final long serialVersionUID = 310277667105766376L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unAvailabilityId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busId")
	private BusMaster busMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusUnavailablityReasonMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busUnavailabilityId")
	private BusUnavailablityReasonMaster busUnavailablityReasonMaster;
	
	private Date detentionDate;
	
	private Date likelyToReadyDate;
	
	private String detailedReason;
	
	private LocalTime busDetentionTime;
	
	private LocalTime busReadyTime;
	

}
