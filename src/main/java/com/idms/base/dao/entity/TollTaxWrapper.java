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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_toll_tax_wrapper")
@EqualsAndHashCode(callSuper = true)
public class TollTaxWrapper extends BaseEntity {

    /**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tollWrapperId")
    private Integer id;
    
	private String tollName;
	
	private Integer tollFees;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "tripId", nullable = true, referencedColumnName = "tripId")
	private TripMaster trip;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteRotation.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "routeRotationId", nullable = true)
	private RouteRotation routeRotation;

}
