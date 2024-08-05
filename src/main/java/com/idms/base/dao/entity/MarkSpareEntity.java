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
@Table(name="mark_spare")
@EqualsAndHashCode(callSuper = true)
public class MarkSpareEntity extends BaseEntity 
{
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spare_id")
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity =BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name ="busId",nullable=true)
	private BusMaster bus;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity =DriverMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name ="driverId",nullable=true)
	private DriverMaster driver;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity =ConductorMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name ="conductorId",nullable=true)
	private ConductorMaster conductor;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity =DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name ="depotId",nullable=true)
	private DepotMaster depot;
	@Column(name="from_dt")
	private Date fromDate;
	@Column(name="to_dt")
	private Date toDate;
	private String remarks;

}
