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
@Table(name="mst_fuelmanufacture_indent")
@EqualsAndHashCode(callSuper = true)
@Data
public class FuelManufactureIndent extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fuelmanufacture_id")
	private Integer id;
	private String name;

}
