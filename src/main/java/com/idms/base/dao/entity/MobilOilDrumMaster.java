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
@Table(name = "mst_mobil_drum")
@EqualsAndHashCode(callSuper = true)
public class MobilOilDrumMaster extends BaseEntity {
	
	private static final long serialVersionUID = 5796809069926845650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mobilDrumId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depot")
	private DepotMaster depot;
	
	private String nameOfDrum;
	
	private Float capacity;
	
	private Integer noOfDrums;
	
	private Float TotalCapacity;
	
	private Integer reorderLevel;
	
	private boolean isDeleted;
	
	@Transient
	private String depotCode;
	
	private Float value;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportUnit")
	private TransportUnitMaster transportUnit;
	
	
}
