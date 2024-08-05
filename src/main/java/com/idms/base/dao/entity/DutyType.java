package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter   
@Entity 
@Table(name = "mst_duty_type")
@EqualsAndHashCode(callSuper = true)
public class DutyType extends BaseEntity{
	
	/**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dutyTypeId")
	private Integer id;
	
	private String dutyTypeName;
	
	
}
