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

@Entity
@Table(name = "mst_child_indent")
@EqualsAndHashCode(callSuper = true)
@Data
public class IndentChildEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "indentchild_id")
	private Integer id;

	private Float itemQuantity;
	

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MeasurementEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "measurementid")
	private MeasurementEntity measurementUnit;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ManufactureEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "manufactureid")
	private ManufactureEntity itemManufacture;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = SpecificationEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "specificationid")
	private SpecificationEntity itemSpecification;

	private Date targetDateToReceive;

	private String justification;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ItemTypeMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "itemtypeid")
	private ItemTypeMaster itemtype;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ItemNameMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "itemnameid")
	private ItemNameMaster itemNameMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Indent.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "indentId")
	private Indent indentObj;
    
	private Boolean isDeleted;
	
	private String measurementUnitName;
	
	private String itemSupplier;
	
	private String itemSpecifications;
	
	private String itemName;
	
		
}
