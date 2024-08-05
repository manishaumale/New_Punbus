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

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "fuel_tank_cleaning_history")
@EqualsAndHashCode(callSuper = true)
public class FuelTankCleaningHistory extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cleaning_id")
	private Integer cleanignId;
	
	private Date cleaningDate;
	
	private String tankUniqueId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelTankId")
	private FuelTankMaster FuelTankMaster;
}
