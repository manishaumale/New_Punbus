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
@Table(name = "mst_bus_assemble_parts")
@EqualsAndHashCode(callSuper = true)
public class BusAssemblyMaster extends BaseEntity {

	private static final long serialVersionUID = 3203649212729267756L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assemblePartId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PartsMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "partId")
	private PartsMaster part;
	
	private String maker;
	
	private String partNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "bus", nullable = true,referencedColumnName="busId")
	private BusMaster bus;
	
}
