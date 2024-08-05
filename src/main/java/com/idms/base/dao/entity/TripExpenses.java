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
@Table(name = "trip_expenses")
@EqualsAndHashCode(callSuper = true)
public class TripExpenses extends BaseEntity {
	
	private static final long serialVersionUID = -4937729349406378042L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EtmTBAssignment.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "assignmentId")
	private EtmTBAssignment assignment;
	
	private Double tollTax;
	
	private Double ticketRefund;
	
	private Double busStandFee;
	
	private Double diesel;
	
	private Double repairBill;
	
	private Double miscFee;
	
	private Double total;

}
