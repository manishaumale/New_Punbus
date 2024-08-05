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
@Table(name = "mobil_oil_used")
@EqualsAndHashCode(callSuper = true)
public class MobilOilUsed extends BaseEntity{
	
	private static final long serialVersionUID = 8041774031741311156L;

	/**
	 *  Hemant Makkar
	 */
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mobilOilUsedId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busId")
	private BusMaster busMaster;
	
	private Float quantity;
	
	private String purpuse;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MobilOilDrumMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "mobilDrumId")
	private MobilOilDrumMaster mobilOilDrumMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "mobilOilDocumentId")
	private Document mobilOilDocument;
	
	private Boolean isOutSide;
	
	private Float amount;
	
	
	
	
}
