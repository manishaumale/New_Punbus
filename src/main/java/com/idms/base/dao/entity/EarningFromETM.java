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
@Table(name = "earning_from_etm")
@EqualsAndHashCode(callSuper = true)
public class EarningFromETM extends BaseEntity {
	
	private static final long serialVersionUID = -1371566228938361224L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EtmTBAssignment.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "assignmentId")
	private EtmTBAssignment assignment;
	
	private Double earningPerKM;
	
	private Double earningPerKmFree;
	
	private Double loadFactor;
	
	private Double totalRemittance;
	
	private Double netAmtToDeposit;
	
	private String inspectDtls;
	
	private Integer totalTicketIssued;

}
