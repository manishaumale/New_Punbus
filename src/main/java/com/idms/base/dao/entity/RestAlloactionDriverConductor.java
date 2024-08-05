package com.idms.base.dao.entity;

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
@Table(name = "rest_allocation")
@EqualsAndHashCode(callSuper = true)
public class RestAlloactionDriverConductor extends BaseEntity {
	
	private static final long serialVersionUID = 5796809069926845650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "driverId")
	private DriverMaster driverMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "conductorId")
	private ConductorMaster conductorMaster;
	
	private Integer counter;
	
	private Integer restCount;
	
}

