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
@Table(name = "mst_permit_via_information")
@EqualsAndHashCode(callSuper = true)
public class ViaInformation extends BaseEntity {

    /**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "viaInfoId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "stateId", nullable = false)
	private StateMaster state;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "cityId", nullable = false)
	private CityMaster city;
	
	 @ManyToOne(fetch = FetchType.LAZY, targetEntity = PermitDetailsMaster.class, cascade = CascadeType.ALL)
	 @JoinColumn(name = "permitDetailsId", nullable = true,referencedColumnName="permitId")
	 private PermitDetailsMaster permitDetailsMaster;
	

}