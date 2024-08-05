/*package com.idms.base.dao.entity;

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
@Table(name = "dispensing_unit_calibration")
@EqualsAndHashCode(callSuper = true)
public class DUCalbiration extends BaseEntity {
	
	private static final long serialVersionUID = -5851893515858324289L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "calibrationId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "duId")
	private DispensingUnitMaster dispensingUnit;
	
	private Date calibrationDate;
	
	private Date certificateValidity;
	
	private String remarks;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "certId")
	private Document calibrationCertificate;
	
	private Date nextCalibrationDate;
	
	private Double startReading;
	
	private Double endReading;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;

}
*/