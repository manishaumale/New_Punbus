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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_driver_details")
@EqualsAndHashCode(callSuper = true)
public class DriverMaster extends BaseEntity {

	private static final long serialVersionUID = 310277667105766376L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driverId")
    private Integer id;
	
	private String driverName;
	
	private String driverCode;
	
	private String fatherName;
	
	private String licenceNo;
	
	private String licenceIssuePlace;
	
	private Date licenceValidity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	private String badgeNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "inductionDepotId")
	private DepotMaster inductionDepot;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EmploymentType.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "employmentType")
	private EmploymentType employmentType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportUnit")
	private TransportUnitMaster transportUnit;
	
	private String epfGpfNumber;
	
	private String esiNumber;
	
	private String address;
	
	private String mobileNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteCategoryMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "driverCategory")
	private RouteCategoryMaster driverCategory;
	
	private Boolean isDeleted;
	
	@Transient
	private LocalTime availableTime;
	
	@Transient
	private CityMaster availableCity;
	
	@Transient
	private LocalTime driverAvailableTime;
	
	@Column(name = "blocked", columnDefinition = "boolean default false")
	private Boolean blocked;

}
