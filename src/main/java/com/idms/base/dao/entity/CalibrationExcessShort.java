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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "calibration_excess_short")
@EqualsAndHashCode(callSuper = true)
public class CalibrationExcessShort extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
	
	private Double excessShort;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DispensingUnitCalibration.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "calibrationId")
	private DispensingUnitCalibration calibrationId;
	
	
}
