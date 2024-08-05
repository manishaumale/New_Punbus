package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "mst_bus_return_reason")
public class BusReturnReason extends BaseEntity {

	private static final long serialVersionUID = 4099979466596586645L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reasonId")
    private Integer id;
	
	private String reason;
	
}
