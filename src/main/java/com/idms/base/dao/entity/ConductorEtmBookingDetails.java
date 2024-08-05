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
@Table(name = "conductor_etm_booking_dtls")
@EqualsAndHashCode(callSuper = true)
public class ConductorEtmBookingDetails extends BaseEntity {
	
	private static final long serialVersionUID = -2419952749868913807L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	private Double passengerAmt;
	
	private Double luggageAmt;
	
	private Double concessionAmt;
	
	private Double tollAmt;
	
	private Double boxAmt;
	
	private Double miscAmt;
	
	private Double totalAmt;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EtmTBAssignment.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "assignmentId")
	private EtmTBAssignment assignment;

}
 