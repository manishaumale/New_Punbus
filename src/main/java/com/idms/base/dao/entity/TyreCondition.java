package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "mst_tyre_condition")
@EqualsAndHashCode(callSuper = true)
public class TyreCondition extends BaseEntity {
	
	private static final long serialVersionUID = 5293796284028428418L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tyreConditionId")
    private Integer id;
	
	private String name;

}
