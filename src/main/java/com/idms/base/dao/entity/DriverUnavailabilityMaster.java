package com.idms.base.dao.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
@Table(name = "mst_driver_unavailable")
@EqualsAndHashCode(callSuper = true)
public class DriverUnavailabilityMaster extends BaseEntity {

	private static final long serialVersionUID = 310277667105766376L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driverUnAvailabilityId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "driverId")
	private DriverMaster driverMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverUnavailablityReasonMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "driverUnavailabilityReasonId")
	private DriverUnavailablityReasonMaster driverUnavailablityReasonMaster;
	
	private Date fromDate;
	
	private Date toDate;
	
	private String detailedReason;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStatus.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "statusId")
	private MasterStatus masterStatus;
	
	private String modifiedBy;
	
	private Timestamp modifiedOn;

}
