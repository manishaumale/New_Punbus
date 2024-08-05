package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Setter;

@Entity
@Table(name="substock_Serialno")
@Data
@Setter
public class SubStockSerialNoToDepoEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="subStockSerial_Id")
	private Integer subStockSerialId;
	@Column(name="startingSerial_no")
	private Integer startingSerialNumber;
	@Column(name="endingSerial_no")
	private Integer endingSerialNumber;
}
