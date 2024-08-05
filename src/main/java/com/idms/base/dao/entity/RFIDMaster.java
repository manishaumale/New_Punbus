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
@Table(name = "mst_rfid")
@EqualsAndHashCode(callSuper = true)
public class RFIDMaster extends BaseEntity {

	private static final long serialVersionUID = -642786556532156485L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rfidId")
    private Integer id;
	
	private String value;
	
}
