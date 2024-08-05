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

@Entity
@Table(name = "mst_specification")
@EqualsAndHashCode(callSuper = true)
@Data
public class SpecificationEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "specification_id")
	private Integer id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ItemTypeMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "itemTypeId")
	private ItemTypeMaster itemType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ItemNameMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "itemnameid")
	private ItemNameMaster itemNameMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MeasurementEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "measurementid")
	private MeasurementEntity measurementEntity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ManufactureEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "manufactureid")
	private ManufactureEntity manufactureEntity;

}
