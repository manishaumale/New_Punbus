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
@Table(name="indent_manufacture")
@EqualsAndHashCode(callSuper = true)
@Data
public class IndentManufactureEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="indentmanufacture_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaker.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreid")
	private TyreMaker tyreMaker;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelManufactureIndent.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelid")
	private FuelManufactureIndent fuelManufacture;
	
}
