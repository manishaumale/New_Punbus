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
@Table(name = "conductor_block_history")
@EqualsAndHashCode(callSuper = true)
public class ConductorBlockHistory extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
	
	private String reason;
	
	private Date  fromDate;
	
	private Date toDate;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "conductorId")
	private ConductorMaster conductorId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "depotId", nullable = true)
	private DepotMaster depotId;
	
	@Column(name = "is_blocked", columnDefinition = "boolean default true")
	private Boolean isDeleted;
	
	private Date orderDate;
	
	private String orderNumber;
	
	
	
}
