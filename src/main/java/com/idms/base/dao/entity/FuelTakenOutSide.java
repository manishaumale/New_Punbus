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


@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_fuel_taken_out")
@EqualsAndHashCode(callSuper = true)
public class FuelTakenOutSide extends BaseEntity{
	
	private static final long serialVersionUID = 7870746565318659267L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fuelTakenId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busId")
	private BusMaster busMaster;
	
	private Float quantity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelSourceMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelSourceId")
	private FuelSourceMaster fuelSourceMaster;
	
	private String billNo;
	
	private Date fuelTakenDate;
	
	private Float amount;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "billDocumentId")
	private Document billDocument;
	
	
	
	
	
}