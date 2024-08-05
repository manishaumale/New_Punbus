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
@Table(name = "ad_blue_used")
@EqualsAndHashCode(callSuper = true)
public class AdBlueUsed extends BaseEntity{
	
	private static final long serialVersionUID = 5957406422222146176L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "addBlueId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busId")
	private BusMaster busMaster;
	
	private Float quantity;
	
	private String purpuse;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = AddBlueDrumMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "addBlueDrumId")
	private AddBlueDrumMaster addBlueDrumMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "addBlueDocumentId")
	private Document addBlueDocument;
	
    private Boolean isOutSide;
	
	private Float amount;
	
	
	
	
}