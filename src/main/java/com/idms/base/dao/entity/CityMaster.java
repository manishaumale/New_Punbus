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
@Table(name = "mst_city")
@EqualsAndHashCode(callSuper = true)
public class CityMaster extends BaseEntity {

    /**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cityId")
    private Integer id;
    
    @Column(length=50)
    private String cityName;
    
    @Column(length=10, unique = true)
    private String cityCode;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "stateId", nullable = false)
    private StateMaster state;

}