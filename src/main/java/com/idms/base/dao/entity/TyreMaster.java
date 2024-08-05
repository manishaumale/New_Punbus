package com.idms.base.dao.entity;

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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "mst_tyre")
@EqualsAndHashCode(callSuper = true)
public class TyreMaster extends BaseEntity {
	
	private static final long serialVersionUID = 4536875050242776752L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tyreId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportUnit")
	private TransportUnitMaster transportUnit;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyperMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busType")
	private BusTyperMaster busType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreType.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreType")
	private TyreType tyreType;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = TyreCondition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreConditionId", nullable=true)
	private TyreCondition tyreCondition;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreSize.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreSize")
	private TyreSize tyreSize;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaker.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreMake")
	private TyreMaker tyreMake;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = TyrePosition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyrePositionId")
	private TyrePosition tyrePosition;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@Column(unique = true)
	private String tyreNumber;
	
	private String expectedLife;
	
	private double tyreCost;
	
	@OneToOne(mappedBy="tyre")
	private BusTyreAssociation bus;
	
	@OneToMany(mappedBy = "tyre")
	private List<BusTyreAssociationHistory> busHistory;
	
	@Column(nullable = true)
	private boolean available;
	
	@Column(nullable = true)
	private boolean busFitted;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TakenOffReason.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "reasonId")
	private TakenOffReason takenOffReason;
	
	@Transient
	private double kmsDone;
	
	@Column(name="to_date")
	private String toDate;
	
	@Column(name="from_date")
	private String fromDate;
	
	@Column(name="old_mileage")
	private Float oldMileage;
	
	/*@Column(name="UseTyreID")
	private String useTyreID;*/
	
	/*@Column(name="source_of_origin_tyre")
	private String sourceOfOriginTyre;*/
	
	private String invoiceNumber;
	
	private Date invoiceDate;
	
	private String tyreTag;
	
	// added by ManishaUmale for Old Tyre unfitted  
	@Column(nullable = true)
	private boolean oldTyre;
	
	@Column(name="kms_Run_TillDate")
	private Integer kmsRunTillDate;
	
	@Column(name="tyre_Purchase_Date")
	private Date tyrePurchaseDate; 
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "sourceOfOriginId")
	private DepotMaster sourceOfOriginTyre;
}
