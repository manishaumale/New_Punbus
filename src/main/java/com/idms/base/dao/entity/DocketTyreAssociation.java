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
@Table(name = "docket_tyre_association")
@EqualsAndHashCode(callSuper = true)
public class DocketTyreAssociation extends BaseEntity {
	
	private static final long serialVersionUID = 417321551521117307L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dtaId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RetreadingDocket.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "docketId")
	private RetreadingDocket docket;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreId")
	private TyreMaster tyre;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isReceived;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isTreaded;
	
	private Date receivedDate;
	
	private String remarks;
	
	private Integer expectedLife;
	
	private Double tyreCost;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreCondition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "oldConditonId")
	private TyreCondition oldConditon;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreCondition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "newConditonId")
	private TyreCondition newConditon;
	
	
	
}
