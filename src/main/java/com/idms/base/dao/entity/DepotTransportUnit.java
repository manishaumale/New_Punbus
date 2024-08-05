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
@Table(name = "mst_depot_transport")
@EqualsAndHashCode(callSuper = true)
public class DepotTransportUnit extends BaseEntity{
	
	/**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "depotTransportId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "depotId", nullable = true,referencedColumnName="depotId")
	private DepotMaster depotMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportId", nullable = false)
	private TransportUnitMaster transportUnitMaster;
	
	

}
