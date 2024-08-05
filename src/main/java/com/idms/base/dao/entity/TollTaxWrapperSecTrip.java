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

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_toll_tax_wrapper_sec")
@EqualsAndHashCode(callSuper = true)
public class TollTaxWrapperSecTrip extends BaseEntity {

    /**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tollWrapperId")
    private Integer id;
    
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TollTaxMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tollIdId")
	private TollTaxMaster tollTaxMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TripMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "tripId", nullable = true, referencedColumnName = "tripId")
	private TripMaster trip;

}

