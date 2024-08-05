package com.idms.base.dao.entity;

import java.time.LocalTime;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "mst_bus_details")
@EqualsAndHashCode(callSuper = true)
public class BusMaster extends BaseEntity {
	
	private static final long serialVersionUID = 5796809069926845650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "busId")
    private Integer id;
	
	private String busRegNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaker.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "maker")
	private BusMaker maker;
	
	private String busModel;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportUnit")
	private TransportUnitMaster transportUnit;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyperMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busType")
	private BusTyperMaster busType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusSubTypeMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busSubType")
	private BusSubTypeMaster busSubType;
	
	private Double busCost;
	
	private Double chessisCost;
	
	private Double bodyCost;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depot")
	private DepotMaster depot;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "primaryDriver")
	private DriverMaster primaryDriver;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "secondaryDriver")
	private DriverMaster secondaryDriver;
	
	private String chessisNumber;
	
	private String engineNumber;
	
	private String frontAxle;
	
	private String rearAxle;
	
	private Integer totalSeats;
	
	private Date chessisPurDate;
	
	private Date bodyFabricateDate;
	
	private String fabricatorName;
	
	private Date busPassingDate;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EuroNorms.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "euroNorm")
	private EuroNorms euroNorm;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusLayout.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "layout")
	private BusLayout layout;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = BusAssemblyMaster.class, mappedBy = "bus", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<BusAssemblyMaster> assemblies;
	
	private Boolean isDeleted;
	
	private Integer typeOfAxle;
	
	@Column(name = "blocked", columnDefinition = "boolean default false")
	private Boolean blocked;
	
	@Transient
	private List<TyreMaster> tyreList;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyreCount.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreCount")
	private BusTyreCount tyreCount;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = BusTyreAssociation.class, mappedBy = "bus", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<BusTyreAssociation> tyreLists;
	
	//private Boolean spareBus;
	
	
	@Transient
	private LocalTime busAvailableTime;
	
	
}
