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
@Table(name = "passenger_classification")
@EqualsAndHashCode(callSuper = true)
public class PassengerClassification extends BaseEntity {
	
	private static final long serialVersionUID = 1950982932665665049L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EtmTBAssignment.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "assignmentId")
	private EtmTBAssignment assignment;
	
	private Integer passengerCounterBooking;
	
	private Integer advBooking;
	
	private Integer luggageTicket;
	
	private Integer policWarrent;
	
	private Integer concessionTicket;
	
	private Integer trafficInspection;
	
	private Double ticketAmount;
	
	private Double kmpl;
	
	private Integer totalAdults;
	
	private Integer totalChilds;

}
