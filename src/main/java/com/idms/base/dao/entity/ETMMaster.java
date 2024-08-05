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
@Table(name = "mst_etm")
@EqualsAndHashCode(callSuper = true)
public class ETMMaster extends BaseEntity {

	private static final long serialVersionUID = -642786556532156485L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etmId")
    private Integer id;
	
	private String etmNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportId")
	private TransportUnitMaster transportUnitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ETMMakerMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "etmMakerId")
	private ETMMakerMaster etmMakerMaster;
	
	private String etmSerNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = GSMAndGPSMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "gsmAndGPSId")
	private GSMAndGPSMaster gsmAndGPSMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RFIDMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "rfidId")
	private RFIDMaster rfidMaster;
	
	@Column(columnDefinition = "boolean default true")
	private Boolean etmAvailable;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	private String depotCode;
	
}
