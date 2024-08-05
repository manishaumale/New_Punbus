package com.idms.base.dao.entity;

import java.util.Date;

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
@Table(name = "online_booking_details")
@EqualsAndHashCode(callSuper = true)
public class OnlineBookingDetails extends BaseEntity {
	
	private static final long serialVersionUID = 4192296053522692491L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	private String bookingId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tripId")
	private TripMaster trip;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fromCityId")
	private CityMaster fromCity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "toCityId")
	private CityMaster toCity;
	
	private Date travelDate;
	
	private Double amount;
	
	private Integer seatsCount;
	
	private String seatNumbers;
	
	private boolean isCanceld;
	
	private Date cancelationDate;
	
}
