package com.idms.base.dao.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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
@Table(name ="dispensing_unit_calibration")
@EqualsAndHashCode(callSuper = true)
public class DispensingUnitCalibration extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer calibrationId;
	
	private Date calibrationDate;
	
	private Date certificateValidity;
	
	private String remarks;
	
	private Integer dispensingId;
	
	private Integer dieselIssued;
	
	@Transient
	private String dispensingUnitCode;
	
	@Transient
	private String dispensingUnitName;
	
	private Double discharge;
	
	private Double excess;
	
	private Double Short;
	
	private Integer  fuelTankId;
	
	private Date nextInspectionDueDate;
	
	private Integer duStartReading;
	
	private Integer duStopReading;
	
	private Double physicalMeasurement;
	
	private Boolean fuelSubmittedFlag;
	
	private Boolean calibrationDoneFlag;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@Transient
	private String tankName;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "certificateId")
	private Document calibrationCertificate;
}
