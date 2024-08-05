package com.idms.base.dao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "assign_sub_Stocks")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssignSubStockToDepoEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="subStock_id")
	private Integer subStockId;
	@Column(name="depot_nm")
	private String depotName;
	@Column(name="transport_Id")
	private Integer transportId;
    private Integer denomination;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="subStock_id")
	private List<SubStockSerialNoToDepoEntity> substockSerialNoDetails;
	

}
