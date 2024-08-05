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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity 
@Table(name = "bus_tyre_association")
@EqualsAndHashCode(callSuper = true)
public class BusTyreAssociation extends BaseEntity {
	
	private static final long serialVersionUID = -8417698576759582093L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "associationId")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="bus_id")
	private BusMaster bus;
	
	@OneToOne
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
	
	@Column(columnDefinition = "boolean default false")
	private Boolean busFitted;
	
	@Transient
	private TyreChangeAction changeAction;
	
	@Transient
	private TakenOffReason takenOffReason;
	
	@Transient
	private Integer oldId;
	
	private String remarks;
	
}
