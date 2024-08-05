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
@Table(name = "mst_tank_capacity")
@EqualsAndHashCode(callSuper = true)
public class TankCapacityMaster extends BaseEntity {
	
	private static final long serialVersionUID = 8560660798248093850L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tankCapacityId")
    private Integer id;
	
	private String name;
	
	private Double capacity;

}
