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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data  
@Entity 
@Table(name = "mst_dispensing_unit")
@EqualsAndHashCode(callSuper = true)
public class DispensingUnitMaster extends BaseEntity {
	
	private static final long serialVersionUID = -1641793177742933883L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispensingUnitId")
    private Integer id;
	
	private String disUnitName;
	
	private String disUnitCode;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitTypeMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dispensingUnitTypeId")
	private DispensingUnitTypeMaster dispensingUnitTypeMaster;
	
	private Date installationDate;
	
	private Date inspectionPeriod;
	
	private Double initialReading;
	
	private Boolean isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	private Double currentReading;
	
	@Transient
	private String depotCode;
	
	
	@Transient
	private String calibrationDate;
	
	
}
