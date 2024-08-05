package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_fuel_source")
@EqualsAndHashCode(callSuper = true)
public class FuelSourceMaster extends BaseEntity {
	
	private static final long serialVersionUID = -6261633213533719251L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuelSourceId")
    private Integer id;
	
	private String name;

}
