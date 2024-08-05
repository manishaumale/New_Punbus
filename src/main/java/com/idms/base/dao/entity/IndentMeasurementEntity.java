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

@Entity
@Table(name="indent_measurement")
@EqualsAndHashCode(callSuper = true)
@Data
public class IndentMeasurementEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="indentmeasurementid")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMeasurementEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreid")
	private TyreMeasurementEntity tyreMeasurement;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelMeasurementEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelid")
	private FuelMeasurementEntity fuelMeasurement;
	
	
}
