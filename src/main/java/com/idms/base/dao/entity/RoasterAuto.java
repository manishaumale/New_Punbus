
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
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "roaster_auto")
@EqualsAndHashCode(callSuper = true)
public class RoasterAuto extends BaseEntity {
	
	private static final long serialVersionUID = 3017067472896760068L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rotaAutoId")
	private Integer id;
	
	private String roastedCode;
	
	private Date generationDate;
	
	@Column(unique=true)
	private Date rotaDate;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
    @JoinColumn(name = "depotId", nullable = false)
	private DepotMaster depot;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = DailyRoasterAuto.class, mappedBy = "rotaAuto", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	List<DailyRoasterAuto> dailyRoasterList;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class)
    @JoinColumn(name = "transportId", nullable = false)
	private TransportUnitMaster transport;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isNormalRota; 
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isSpecialRota;
	
	@Transient
	private Boolean isEdit;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStatus.class)
    @JoinColumn(name = "statusId", nullable = true)
	private MasterStatus masterStatus;
	
}

