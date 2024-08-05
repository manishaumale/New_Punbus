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

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "vts_data")
@EqualsAndHashCode(callSuper = true)
public class VTSData extends BaseEntity {
	
	private static final long serialVersionUID = 4386968611686127622L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vts_data_id")
	private Integer id;
	
	private String bus_no;
	
	private String route_id;
	
	private String depot_code;
	
	private Double vts_kms;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "route")
	private RouteMaster route;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busId")
	private BusMaster busId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depotId;

}
