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
@Table(name = "mst_dip_reading")
@EqualsAndHashCode(callSuper = true)
public class DipReadingMaster extends BaseEntity {
	
	private static final long serialVersionUID = -1641793177742933883L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dipReadingId")
    private Integer id;
	
	private String readingName;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = TankCapacityMaster.class)
    @JoinColumn(name = "capacityMaster", nullable = false)
	private TankCapacityMaster capacityMaster;
	
	@Column(precision = 10, scale = 2)
	private Float diameter;
	
	@Column(precision = 10, scale = 2)
	private Float radius;
	
	@Column(precision = 10, scale = 2)
	private Float length;
	
	@Column(precision = 10, scale = 2)
	private Float deadboard;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ReadingMaster.class, mappedBy = "dip", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<ReadingMaster> readings;

}
