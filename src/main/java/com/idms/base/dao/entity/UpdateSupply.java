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
@Table(name = "mst_update_supply")
@EqualsAndHashCode(callSuper = true)
public class UpdateSupply extends BaseEntity {

	
	private static final long serialVersionUID = -6268138151507280816L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "updateSupplyId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTankId")
	private FuelTankMaster fuelTankMaster;
	
	private Float dipReadingBefore;
	
	private Float dipReadingAfter;
	
    private Float volumeBeforeSupply;
	
	private Float volumeAfterSupply;
	
	private Float totalVolume;
	
	private Float variants;
	
	private String excessOrShort;
	
	private String excessOrShortVal;
	
}
