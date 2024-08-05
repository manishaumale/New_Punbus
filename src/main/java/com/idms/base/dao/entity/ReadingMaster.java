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
@Table(name = "mst_reading")
@EqualsAndHashCode(callSuper = true)
public class ReadingMaster extends BaseEntity {

	private static final long serialVersionUID = 886647752756069846L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "readingId")
    private Integer id;
	
	@Column(precision = 10, scale = 2)
	private Float reading;
	
	@Column(precision = 10, scale = 2)
	private Float volume;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DipReadingMaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "dipReadingId", nullable = false,referencedColumnName="dipReadingId")
	private DipReadingMaster dip;

}
