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

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_tax")
@EqualsAndHashCode(callSuper = true)
public class TaxMaster extends BaseEntity{
	
	/**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "taxId")
	private Integer id;
	
	@Column(length = 10)
	private String taxAmount;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "stateId", nullable = false)
	private StateMaster state;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TypeOfTaxMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "taxTypeId")
	private TypeOfTaxMaster typeOfTaxMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyperMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busTypeId")
	private BusTyperMaster busTyperMaster;
	
	private Date applicableFrom;

}
