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
@Table(name = "etm_trip_dtls")
@EqualsAndHashCode(callSuper = true)
public class EtmTripDtls extends BaseEntity {
	
	private static final long serialVersionUID = 6450659817345260246L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EtmTBAssignment.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "assignmentId")
	private EtmTBAssignment assignment;

	private String routeName;
	
	private String tripStartDateTime;
	
	private String tripEndDateTime;
	
	private Integer distance;
	
	private Double advBookingAmt;
	
	private Double totalCollection;
	
	private Double expenses;
	
	private Double netTripAmt;
	
	private Double empk;

}
