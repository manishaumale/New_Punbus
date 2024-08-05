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

@Data
@Entity 
@Table(name = "bus_tyre_association_history")
@EqualsAndHashCode(callSuper = true)
public class BusTyreAssociationHistory extends BaseEntity {
	
	private static final long serialVersionUID = 6804788616699566523L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "associationHistoryId")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="bus_id")
	private BusMaster bus;
	
	@ManyToOne
	@JoinColumn(name = "tyre_id")
	private TyreMaster tyre;
	
	private Date installDate;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyrePosition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyrePositionId")
	private TyrePosition tyrePosition;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreCondition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreConditionId")
	private TyreCondition tyreCondition;
	
	private Double kmsDone;
	
	private Date removalDate;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean busFitted;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TakenOffReason.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "reasonId")
	private TakenOffReason reason;
	
	private String remarks;
	
	@Column(name="hill_Kms")
	private Integer hillKms;
	
	@Column(name="plain_Kms")
	private Integer plainKms;
	
	// added by ManishaUmale for Old Tyre unfitted  
	
	@Column(name="KMs_Taken_Off")
	private Integer kmsTakenOff;
	
	@Column(name="kms_Installed")
	private Integer kmsInstalled;
	
	@Column(name="total_Mileage_TakenOff")
	private Integer totalMileageTakenOff;
	
	@Column(name="hill_Mileage_Done")
	private Integer hillMileageDone;
	
	@Column(name="plain_Mileage_Done")
	private Integer plainMileageDone;
	
}
