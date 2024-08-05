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
@Table(name = "mst_tyre_position")
@EqualsAndHashCode(callSuper = true)
public class TyrePosition extends BaseEntity {
	
	private static final long serialVersionUID = -6261633213533719251L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tyrePositionId")
    private Integer id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyreCount.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreCount")
	private BusTyreCount tyreCount;

}
