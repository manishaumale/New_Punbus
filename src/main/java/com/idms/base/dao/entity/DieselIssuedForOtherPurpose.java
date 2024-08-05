package com.idms.base.dao.entity;

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


@Data   
@Entity 
@Table(name = "diesel_issued_other_purpose")
@EqualsAndHashCode(callSuper = true)
public class DieselIssuedForOtherPurpose extends BaseEntity {
	
	private static final long serialVersionUID = 5796809069926845650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dieselIssuedOtherId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dispensingId")
	private DispensingUnitMaster dispensingUnitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = OtherPurpose.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "otherPurposeId")
	private OtherPurpose otherPurpose;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelType.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTypeId")
	private FuelType fuelType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTankId")
	private FuelTankMaster fuelTankMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MobilOilDrumMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "mobilDrumId")
	private MobilOilDrumMaster mobilOilDrumMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = AddBlueDrumMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "addBlueDrumId")
	private AddBlueDrumMaster addBlueDrumMaster;
	
	private Float dieselIssued;
	
    private Double duStartReading;
	
	private Double duEndReading;
	
	private String vehicleNo;
	
	private String remarks;
	
	private Boolean isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@Transient
	private String depotCode;
	
}
