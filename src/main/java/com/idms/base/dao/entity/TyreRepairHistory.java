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

@Data // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity
@Table(name = "tyre_repair")
@EqualsAndHashCode(callSuper = true)
public class TyreRepairHistory extends BaseEntity {

	private static final long serialVersionUID = -6261633213533719251L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyreAssociationHistory.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "associationId")
	private BusTyreAssociationHistory assosiation;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreId")
	private TyreMaster tyre;
	
	
	private String remarks;

}
