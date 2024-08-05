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
@Table(name = "tax_calc_submit_etm_child")
@EqualsAndHashCode(callSuper = true)
public class TaxCalculationOnSubmitETMChild extends BaseEntity {
	
	private static final long serialVersionUID = -2291250606119805486L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "taxCalcChildId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TaxMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "taxId", nullable = true)
	private TaxMaster taxMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PermitDetailsMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "permitId", nullable = true)
	private PermitDetailsMaster permitDetailsMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "stateId", nullable = true)
	private StateMaster stateMaster;
	
	private Double taxCalculated;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TaxCalculationOnSubmitETM.class)
	@JoinColumn(name = "taxCalculationOnSubmit")
	private TaxCalculationOnSubmitETM taxCalculationOnSubmitETM;
	
	
	

	

}
