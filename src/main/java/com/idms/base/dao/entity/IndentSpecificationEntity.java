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
@Table(name="indent_Specification")
@EqualsAndHashCode(callSuper = true)
@Data
public class IndentSpecificationEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="indentSpecification_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreSize.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreid")
	private TyreSize tyreSize;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity =  FuelSpecificationIndent.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelid")
	private FuelSpecificationIndent fuelSpecificationIndent;
	

}
