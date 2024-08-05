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
@Table(name = "other_duty")
@EqualsAndHashCode(callSuper = true)
public class OtherDuty extends BaseEntity {
	
	private static final long serialVersionUID = 5796809069926845650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otherDutyId")
    private Integer id;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "driverId")
	private DriverMaster driverMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "conductorId")
	private ConductorMaster conductorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DutyType.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dutyTypeId")
	private DutyType dutyType;
	
	private String orderNo;
	
	private String remarks;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Boolean isDeleted;
	
}
