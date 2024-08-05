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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter  
@Entity
@Table(name="mark_codemn")
@EqualsAndHashCode(callSuper = true)
public class MarkCondemn extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="mark_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name ="busId")
	private BusMaster busId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity =DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name ="depotId")
	private DepotMaster depotId;
   
	private String remarks;
	
	private Date condemnDate;
	

}