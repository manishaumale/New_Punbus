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
@Table(name = "mst_role_diesel_supply")
@EqualsAndHashCode(callSuper = true)
public class RoleDieselSupply extends BaseEntity {


	private static final long serialVersionUID = -1063874510660616183L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleDieselSupplyId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Role.class, cascade = CascadeType.MERGE)
	private Role roleObj;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RecieveDieselSupplyMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dieselSupplyId", nullable = true)
	private RecieveDieselSupplyMaster recieveDieselSupplyMaster;
	
	
}