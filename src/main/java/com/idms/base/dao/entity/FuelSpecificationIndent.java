package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="mst_fuel_Specification_indent")
@EqualsAndHashCode(callSuper = true)
@Data
public class FuelSpecificationIndent extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fuelspecification_id")
	private Integer id;
	private String name;
	/*@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyrePosition.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fuelmanufactureId")
	private FuelManufactureIndent fuelmanufacture;*/

}
