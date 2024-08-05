package com.idms.base.dao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data   
@Entity 
@Table(name = "tax_calc_submit_etm")
@EqualsAndHashCode(callSuper = true)
public class TaxCalculationOnSubmitETM extends BaseEntity {
	
	private static final long serialVersionUID = -2291250606119805486L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "taxCalculationId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class)
    @JoinColumn(name = "busId", nullable = true)
	private BusMaster bus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DriverMaster.class)
    @JoinColumn(name = "driverId", nullable = true)
	private DriverMaster driver;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class)
    @JoinColumn(name = "conductorId", nullable = true)
	private ConductorMaster conductor;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class)
    @JoinColumn(name = "routeId", nullable = true)
	private RouteMaster route;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DailyRoaster.class)
	@JoinColumn(name = "roster_Id")
	private DailyRoaster dailyRoaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = SubmitEtmTicketBoxEntity.class)
	@JoinColumn(name = "submit_Id")
	private SubmitEtmTicketBoxEntity submitEtmTicketBoxEntity;
	
	
	private Double totalTaxCalculated;
	
	@OneToMany(mappedBy="taxCalculationOnSubmitETM",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<TaxCalculationOnSubmitETMChild> childList;
	
	
	

	

}
