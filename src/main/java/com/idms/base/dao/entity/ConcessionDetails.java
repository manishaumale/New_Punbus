package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data  
@Entity 
@Table(name = "concession_details")
@EqualsAndHashCode(callSuper = true)
public class ConcessionDetails extends BaseEntity {
	
	private static final long serialVersionUID = -2650456265845564411L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
//	private ConcessionType concessionType;
//	
//	private ConcessionPercentage conPer;
	
	private Integer concessionTickets;
	
	private Double concessionAmount;

}
