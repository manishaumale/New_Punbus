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
@Table(name = "mst_bus_sub_type")
@EqualsAndHashCode(callSuper = true)
public class BusSubTypeMaster extends BaseEntity {

    /**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "busSubTypeId")
    private Integer id;
	
	@Column(length=500)
    private String busSubTypeDescription;
    
    @Column(length=50)
    private String busSubTypeName;
    
}
