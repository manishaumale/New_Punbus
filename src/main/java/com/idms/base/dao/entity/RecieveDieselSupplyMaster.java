package com.idms.base.dao.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_recieve_diesel_sup")
@EqualsAndHashCode(callSuper = true)
public class RecieveDieselSupplyMaster extends BaseEntity {


	private static final long serialVersionUID = -1063874510660616183L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dieselSupplyId")
    private Integer id;
	
	private String invoiceNumber;
	
	private Date  invoiceDate;
	
	private String tankerNumber;
	
	private Float quantityRecieved;
	
	private Date  entryDate;
	
	private LocalDateTime entryTime;
	
	private Float temprature;
	
	private Float hydrometerReading;
	
	private Float dieselRate;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = UpdateSupply.class,cascade = {CascadeType.ALL})
    @JoinColumn(name = "updateSupplyId", referencedColumnName = "updateSupplyId")
	private UpdateSupply updateSupply;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = RoleDieselSupply.class, mappedBy = "recieveDieselSupplyMaster")
	@EqualsAndHashCode.Exclude
	private List<RoleDieselSupply> roleList;
	
	private Boolean isDeleted;
	
	
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class,cascade = CascadeType.ALL)
	@JoinColumn(name = "transportId") 
    private TransportUnitMaster transportUnitMaster;
    
    private Float oldPrice;
    
    private Float oldVolume;
    
    private Float currentPrice;
	
}
